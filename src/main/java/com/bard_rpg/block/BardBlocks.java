package com.bard_rpg.block;

import com.bard_rpg.item.Group;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

import static com.bard_rpg.BardsMod.MOD_ID;

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
            new MusicStandBlock(FabricBlockSettings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(Instrument.BASS)
                    .strength(2.5F)
                    .sounds(BlockSoundGroup.WOOD)
                    .nonOpaque()),
            "Music Stand");

    public static void register() {
        for (var e : all) {
            Registry.register(Registries.BLOCK, new Identifier(MOD_ID, e.name()), e.block());
            Registry.register(Registries.ITEM, new Identifier(MOD_ID, e.name()), e.item());
        }
        ItemGroupEvents.modifyEntriesEvent(Group.KEY).register(content -> {
            for (var e : all) content.add(e.item());
        });
    }

    public static void registerClient() {
        net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap.INSTANCE.putBlock(
                MUSIC_STAND.block(), net.minecraft.client.render.RenderLayer.getCutout());
    }
}
