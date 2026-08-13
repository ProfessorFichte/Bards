package com.bard_rpg.content;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

import static com.bard_rpg.BardsMod.MOD_ID;

public class BardsSounds {
    public static final class Entry {
        private final Identifier id;
        private final SoundEvent soundEvent;
        private int variants = 1;

        public Entry(String name) {
            this.id = new Identifier(MOD_ID, name);
            this.soundEvent = SoundEvent.of(this.id);
        }

        public Entry variants(int variants) {
            this.variants = variants;
            return this;
        }

        public Identifier id() { return id; }
        public SoundEvent soundEvent() { return soundEvent; }
        public int variants() { return variants; }
    }

    public static final List<Entry> entries = new ArrayList<>();
    public static Entry add(Entry entry) { entries.add(entry); return entry; }

    public static final Entry lyre_channel = add(new Entry("lyre_channel").variants(3));
    public static final Entry lyre_hit = add(new Entry("lyre_hit").variants(3));
    public static final Entry lute_hit = add(new Entry("lute_hit").variants(2));
    public static final Entry bard_impact = add(new Entry("bard_impact").variants(3));
    public static final Entry bard_buff = add(new Entry("bard_buff").variants(3));
    public static final Entry magical_ballad = add(new Entry("magical_ballad"));
    public static final Entry encore_channel = add(new Entry("encore_channel"));
    public static final Entry encore_cooldown_impact = add(new Entry("encore_cooldown_impact"));
    public static final Entry armys_paeon_buff = add(new Entry("armys_paeon_buff"));
    public static final Entry armys_paeon_impact = add(new Entry("armys_paeon_impact").variants(3));
    public static final Entry armys_paeon_release = add(new Entry("armys_paeon_release").variants(3));
    public static final Entry harp_crossbow_shoot = add(new Entry("harp_crossbow_shoot").variants(4));
    public static final Entry vicious_mockery = add(new Entry("vicious_mockery").variants(3));
    public static final Entry crescendo_launch = add(new Entry("crescendo_launch"));
    //SONGS
    public static final Entry troubadours_minuet = add(new Entry("troubadours_minuet"));
    public static final Entry wanderers_minuet = add(new Entry("wanderers_minuet").variants(3));
    public static final Entry tale_of_the_dragonslayer = add(new Entry("tale_of_the_dragonslayer").variants(2));
    public static final Entry discordant_note = add(new Entry("discordant_note").variants(2));
    public static final Entry secret_sonata = add(new Entry("secret_sonata").variants(2));
    public static final Entry natures_minne = add(new Entry("natures_minne").variants(3));
    public static final Entry song_of_celerity = add(new Entry("song_of_celerity").variants(3));
    public static final Entry canticle_of_the_tides = add(new Entry("canticle_of_the_tides").variants(3));
    public static final Entry hymn_of_the_golden_light = add(new Entry("hymn_of_the_golden_light").variants(4));
    public static final Entry song_of_the_turning_sky = add(new Entry("song_of_the_turning_sky").variants(4));
    //LUTHIER SONGS
    public static final Entry troubadours_minuet_full = add(new Entry("troubadours_minuet_full"));
    public static final Entry wanderers_minuet_full = add(new Entry("wanderers_minuet_full"));
    public static final Entry tale_of_the_dragonslayer_full = add(new Entry("tale_of_the_dragonslayer_full"));
    public static final Entry natures_minne_full = add(new Entry("natures_minne_full"));
    public static final Entry song_of_celerity_full = add(new Entry("song_of_celerity_full"));
    public static final Entry canticle_of_the_tides_full = add(new Entry("canticle_of_the_tides_full"));
    public static final Entry hymn_of_the_golden_light_full = add(new Entry("hymn_of_the_golden_light_full"));
    public static final Entry song_of_the_turning_sky_full = add(new Entry("song_of_the_turning_sky_full"));
    public static final Entry discordant_note_full = add(new Entry("discordant_note_full"));
    public static final Entry secret_sonata_full = add(new Entry("secret_sonata_full"));

    public static void register() {
        for (var entry : entries) {
            Registry.register(Registries.SOUND_EVENT, entry.id(), entry.soundEvent());
        }
    }

    public static void playSoundEvent(World world, Entity entity, SoundEvent soundEvent) {
        playSoundEvent(world, entity, soundEvent, 1, 1);
    }

    public static void playSoundEvent(World world, Entity entity, SoundEvent soundEvent, float volume, float pitch) {
        world.playSound(
                (PlayerEntity) null,
                entity.getX(), entity.getY(), entity.getZ(),
                soundEvent,
                SoundCategory.PLAYERS,
                volume, pitch
        );
    }
}
