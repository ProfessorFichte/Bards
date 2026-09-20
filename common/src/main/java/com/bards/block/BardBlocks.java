package com.bards.block;

import com.bards.platform.Platform;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static com.bards.BardsMod.MOD_ID;

public class BardBlocks {

    public record Entry(String name, Block block, BlockItem item, String translation) {
        public Entry(String name, Block block, String translation) {
            this(name, block, new BlockItem(block, new Item.Settings()), translation);
        }
    }

    public static final ArrayList<Entry> all = new ArrayList<>();

    private static Entry entry(String name, Block block, String translation) {
        var e = new Entry(name, block, translation);
        all.add(e);
        return e;
    }

    public static final Entry MUSIC_STAND = entry("music_stand",
            new MusicStandBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(Instrument.BASS)
                    .strength(2.5F)
                    .sounds(BlockSoundGroup.WOOD)
                    .nonOpaque()),
            "Music note Stand");

    public static void register() {
        blocksToRegister().forEach((id, block) -> Registry.register(Registries.BLOCK, id, block));
    }

    public static void registerItems() {
        itemsToRegister().forEach((id, item) -> Registry.register(Registries.ITEM, id, item));
    }

    public static Map<Identifier, Block> blocksToRegister() {
        var toRegister = new LinkedHashMap<Identifier, Block>();
        for (var e : all) {
            var id = Identifier.of(MOD_ID, e.name());
            if (Registries.BLOCK.containsId(id)) { continue; }
            toRegister.put(id, e.block());
        }
        return toRegister;
    }

    public static Map<Identifier, Item> itemsToRegister() {
        var toRegister = new LinkedHashMap<Identifier, Item>();
        for (var e : all) {
            var id = Identifier.of(MOD_ID, e.name());
            if (Registries.ITEM.containsId(id)) { continue; }
            toRegister.put(id, e.item());
        }
        if (!itemGroupCallbackInstalled) {
            itemGroupCallbackInstalled = true;
            Platform.get().onItemGroupModify(com.bards.item.Group.KEY, entries -> {
                for (var e : all) entries.add(e.item());
            });
        }
        return toRegister;
    }

    private static boolean itemGroupCallbackInstalled = false;

    public static BiConsumer<Block, RenderLayer> cutoutRenderLayerRegistrar;

    public static void registerClient() {
        if (cutoutRenderLayerRegistrar == null) {
            throw new IllegalStateException("cutoutRenderLayerRegistrar was not set by the platform module before registerClient() ran");
        }
        cutoutRenderLayerRegistrar.accept(MUSIC_STAND.block(), RenderLayer.getCutout());
    }
}
