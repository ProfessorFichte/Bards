package com.bards.block;

import com.bards.platform.Platform;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
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
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.5F)
                    .sounds(BlockSoundGroup.WOOD)
                    .nonOpaque()),
            "Music note Stand");

    public static void register() {
        for (var e : all) {
            Registry.register(Registries.BLOCK, Identifier.of(MOD_ID, e.name()), e.block());
            Registry.register(Registries.ITEM, Identifier.of(MOD_ID, e.name()), e.item());
        }
        Platform.get().onItemGroupModify(com.bards.item.Group.KEY, entries -> {
            for (var e : all) entries.add(e.item());
        });
    }

    public static BiConsumer<Block, RenderLayer> cutoutRenderLayerRegistrar;

    public static void registerClient() {
        if (cutoutRenderLayerRegistrar == null) {
            throw new IllegalStateException("cutoutRenderLayerRegistrar was not set by the platform module before registerClient() ran");
        }
        cutoutRenderLayerRegistrar.accept(MUSIC_STAND.block(), RenderLayer.getCutout());
    }
}
