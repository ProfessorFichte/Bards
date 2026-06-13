package com.bards.content;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

import static com.bards.BardsMod.MOD_ID;

public class BardsSounds {
    public static final class Entry {
        private final Identifier id;
        private final SoundEvent soundEvent;
        private RegistryEntry<SoundEvent> entry;
        private int variants = 1;

        public Entry(Identifier id, SoundEvent soundEvent) {
            this.id = id;
            this.soundEvent = soundEvent;
        }

        public Entry(String name) {
            this(Identifier.of(MOD_ID, name));
        }

        public Entry(Identifier id) {
            this(id, SoundEvent.of(id));
        }

        public Entry travelDistance(float distance) {
            return new Entry(id, SoundEvent.of(id, distance));
        }

        public Entry variants(int variants) {
            this.variants = variants;
            return this;
        }

        public Identifier id() {
            return id;
        }

        public SoundEvent soundEvent() {
            return soundEvent;
        }

        public RegistryEntry<SoundEvent> entry() {
            return entry;
        }

        public int variants() {
            return variants;
        }
    }
    public static final List<Entry> entries = new ArrayList<>();
    public static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }

    public static float travelDistanceSpellSounds = 32;
    public static final Entry lyre_channel = add(new Entry("lyre_channel").variants(3));
    public static final Entry lyre_hit = add(new Entry("lyre_hit").variants(3));
    public static final Entry lyre_release = add(new Entry("lyre_release").variants(1));
    public static final Entry lute_hit = add(new Entry("lute_hit").variants(2));
    public static final Entry bard_impact = add(new Entry("bard_impact").variants(3));
    public static final Entry bard_buff = add(new Entry("bard_buff").variants(3));
    public static final Entry magical_ballad = add(new Entry("magical_ballad")).travelDistance(travelDistanceSpellSounds);
    public static final Entry encore_channel = add(new Entry("encore_channel")).travelDistance(travelDistanceSpellSounds);
    public static final Entry encore_cooldown_impact = add(new Entry("encore_cooldown_impact"));
    public static final Entry armys_paeon_buff = add(new Entry("armys_paeon_buff"));
    public static final Entry armys_paeon_impact = add(new Entry("armys_paeon_impact").variants(3));
    public static final Entry armys_paeon_release = add(new Entry("armys_paeon_release").variants(3));
    public static final Entry harp_crossbow_pull = add(new Entry("harp_crossbow_pull"));
    public static final Entry harp_crossbow_shoot = add(new Entry("harp_crossbow_shoot").variants(4));
    public static final Entry harp_crossbow_hit = add(new Entry("harp_crossbow_hit"));
    public static final Entry vicious_mockery = add(new Entry("vicious_mockery")).variants(3).travelDistance(travelDistanceSpellSounds);
    public static final Entry crescendo_launch = add(new Entry("crescendo_launch"));
    //SONGS
    public static final Entry troubadours_minuet = add(new Entry("troubadours_minuet")).travelDistance(travelDistanceSpellSounds);
    public static final Entry wanderers_minuet = add(new Entry("wanderers_minuet")).variants(3).travelDistance(travelDistanceSpellSounds);
    public static final Entry tale_of_the_dragonslayer = add(new Entry("tale_of_the_dragonslayer")).variants(2).travelDistance(travelDistanceSpellSounds);
    public static final Entry discordant_note = add(new Entry("discordant_note")).variants(2).travelDistance(travelDistanceSpellSounds);
    public static final Entry secret_sonata = add(new Entry("secret_sonata")).variants(2).travelDistance(travelDistanceSpellSounds);
    public static final Entry natures_minne = add(new Entry("natures_minne")).variants(3).travelDistance(travelDistanceSpellSounds);
    public static final Entry song_of_celerity = add(new Entry("song_of_celerity")).variants(3).travelDistance(travelDistanceSpellSounds);
    public static final Entry canticle_of_the_tides = add(new Entry("canticle_of_the_tides")).variants(3).travelDistance(travelDistanceSpellSounds);
    public static final Entry hymn_of_the_golden_light = add(new Entry("canticle_of_the_tides")).variants(4).travelDistance(travelDistanceSpellSounds);
    public static final Entry song_of_the_turning_sky = add(new Entry("song_of_the_turning_sky")).variants(5).travelDistance(travelDistanceSpellSounds);
    //LUTHIER SONGS
    public static final Entry troubadours_minuet_full = add(new Entry("troubadours_minuet_full"));
    public static final Entry wanderers_minuet_full = add(new Entry("wanderers_minuet_full"));
    public static final Entry tale_of_the_dragonslayer_full = add(new Entry("wanderers_minuet_full"));
    public static final Entry natures_minne_full = add(new Entry("natures_minne_full"));
    public static final Entry song_of_celerity_full = add(new Entry("song_of_celerity_full"));
    public static final Entry canticle_of_the_tides_full = add(new Entry("canticle_of_the_tides_full"));
    public static final Entry hymn_of_the_golden_light_full = add(new Entry("hymn_of_the_golden_light_full"));
    public static final Entry song_of_the_turning_sky_full = add(new Entry("song_of_the_turning_sky_full"));



    public static void register() {
        for (var entry: entries) {
            entry.entry = Registry.registerReference(Registries.SOUND_EVENT, entry.id(), entry.soundEvent());
        }
    }

    public static void playSoundEvent(World world, Entity entity, SoundEvent soundEvent) {
        playSoundEvent(world, entity, soundEvent, 1, 1);
    }

    public static void playSoundEvent(World world, Entity entity, SoundEvent soundEvent, float volume, float pitch) {
        world.playSound(
                (PlayerEntity)null,
                entity.getX(),
                entity.getY(),
                entity.getZ(),
                soundEvent,
                SoundCategory.PLAYERS,
                volume,
                pitch);
    }
}
