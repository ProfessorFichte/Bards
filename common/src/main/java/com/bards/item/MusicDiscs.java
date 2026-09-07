package com.bards.item;

import com.bards.platform.Platform;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.JukeboxPlayableComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryPair;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.ArrayList;

import static com.bards.BardsMod.MOD_ID;

public class MusicDiscs {
    public record Entry(String name, BardMusicDiscItem item) { }

    public static final ArrayList<Entry> all = new ArrayList<>();

    public static String titleCase(String name) {
        var builder = new StringBuilder();
        for (var word : name.split("_")) {
            if (builder.length() > 0) builder.append(' ');
            builder.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
        }
        return builder.toString();
    }

    private static Entry entry(String name) {
        var songKey = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(MOD_ID, name));
        var settings = new Item.Settings()
                .component(DataComponentTypes.JUKEBOX_PLAYABLE, new JukeboxPlayableComponent(new RegistryPair<>(songKey), false))
                .component(DataComponentTypes.RARITY, Rarity.RARE);
        var item = new BardMusicDiscItem(settings, titleCase(name));
        var e = new Entry(name, item);
        all.add(e);
        return e;
    }

    public static final Entry TROUBADOURS_MINUET = entry("troubadours_minuet");
    public static final Entry WANDERERS_MINUET = entry("wanderers_minuet");
    public static final Entry TALE_OF_THE_DRAGONSLAYER = entry("tale_of_the_dragonslayer");
    public static final Entry NATURES_MINNE = entry("natures_minne");
    public static final Entry SONG_OF_CELERITY = entry("song_of_celerity");
    public static final Entry CANTICLE_OF_THE_TIDES = entry("canticle_of_the_tides");
    public static final Entry HYMN_OF_THE_GOLDEN_LIGHT = entry("hymn_of_the_golden_light");
    public static final Entry SONG_OF_THE_TURNING_SKY = entry("song_of_the_turning_sky");
    public static final Entry DISCORDANT_NOTE = entry("discordant_note");
    public static final Entry SECRET_SONATA = entry("secret_sonata");

    public static void register() {
        for (var e : all) {
            Registry.register(Registries.ITEM, Identifier.of(MOD_ID, e.name()), e.item());
        }
        Platform.get().onItemGroupModify(ItemGroups.TOOLS, entries -> {
            for (var e : all) entries.add(e.item());
        });
    }
}
