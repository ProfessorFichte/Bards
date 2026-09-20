package com.bards.item;

import com.bards.content.BardsSounds;
import com.bards.platform.Platform;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.bards.BardsMod.MOD_ID;

public class MusicDiscs {
    public record Entry(String name, BardMusicDiscItem item) { }

    public static final ArrayList<Entry> all = new ArrayList<>();

    private static final int COMPARATOR_OUTPUT = 12;
    private static final int LENGTH_IN_SECONDS = 180;

    public static String titleCase(String name) {
        var builder = new StringBuilder();
        for (var word : name.split("_")) {
            if (builder.length() > 0) builder.append(' ');
            builder.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
        }
        return builder.toString();
    }

    private static Entry entry(String name, SoundEvent sound) {
        var settings = new Item.Settings().rarity(Rarity.RARE);
        var item = new BardMusicDiscItem(COMPARATOR_OUTPUT, sound, settings, LENGTH_IN_SECONDS,
                titleCase(name), "jukebox_song." + MOD_ID + "." + name);
        var e = new Entry(name, item);
        all.add(e);
        return e;
    }

    public static final Entry TROUBADOURS_MINUET = entry("troubadours_minuet", BardsSounds.troubadours_minuet_full.soundEvent());
    public static final Entry WANDERERS_MINUET = entry("wanderers_minuet", BardsSounds.wanderers_minuet_full.soundEvent());
    public static final Entry TALE_OF_THE_DRAGONSLAYER = entry("tale_of_the_dragonslayer", BardsSounds.tale_of_the_dragonslayer_full.soundEvent());
    public static final Entry NATURES_MINNE = entry("natures_minne", BardsSounds.natures_minne_full.soundEvent());
    public static final Entry SONG_OF_CELERITY = entry("song_of_celerity", BardsSounds.song_of_celerity_full.soundEvent());
    public static final Entry CANTICLE_OF_THE_TIDES = entry("canticle_of_the_tides", BardsSounds.canticle_of_the_tides_full.soundEvent());
    public static final Entry HYMN_OF_THE_GOLDEN_LIGHT = entry("hymn_of_the_golden_light", BardsSounds.hymn_of_the_golden_light_full.soundEvent());
    public static final Entry SONG_OF_THE_TURNING_SKY = entry("song_of_the_turning_sky", BardsSounds.song_of_the_turning_sky_full.soundEvent());
    public static final Entry DISCORDANT_NOTE = entry("discordant_note", BardsSounds.discordant_note_full.soundEvent());
    public static final Entry SECRET_SONATA = entry("secret_sonata", BardsSounds.secret_sonata_full.soundEvent());

    public static void register() {
        itemsToRegister().forEach((id, item) -> Registry.register(Registries.ITEM, id, item));
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
            Platform.get().onItemGroupModify(ItemGroups.TOOLS, entries -> {
                for (var e : all) entries.add(e.item());
            });
        }
        return toRegister;
    }

    private static boolean itemGroupCallbackInstalled = false;
}
