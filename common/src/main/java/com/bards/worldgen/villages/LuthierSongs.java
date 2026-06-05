package com.bards.worldgen.villages;

import com.bards.content.BardSkillColors;
import com.bards.content.BardsSounds;
import com.bards.effect.BardsEffects;
import com.bards.item.Weapons;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;

import java.util.List;

public class LuthierSongs {

    public record Song(
            String name,
            Item heldInstrument,
            net.spell_engine.client.util.Color noteColor,
            RegistryEntry<StatusEffect> effect,
            int effectAmplifier,
            RegistryEntry<SoundEvent> sound,
            int soundDurationTicks
    ) {}

    public static final List<Song> SONGS = List.of(
            new Song(BardsEffects.TROUBADOURS_MINUET.title, Weapons.wooden_lute.item(), BardSkillColors.troubadours_minuet, BardsEffects.TROUBADOURS_MINUET.entry, 0, BardsSounds.troubadours_minuet_full.entry(), 520),
            new Song(BardsEffects.WANDERERS_MINUET.title, Weapons.wooden_lute.item(), BardSkillColors.wanderers_minuet, BardsEffects.WANDERERS_MINUET.entry, 0, BardsSounds.wanderers_minuet_full.entry(), 620),
            new Song(BardsEffects.TALE_OF_THE_DRAGON_SLAYER.title, Weapons.diamond_lute.item(), BardSkillColors.tale_of_the_dragonslayer, BardsEffects.TALE_OF_THE_DRAGON_SLAYER.entry, 0, BardsSounds.tale_of_the_dragonslayer_full.entry(), 720),
            new Song(BardsEffects.NATURES_MINNE.title, Weapons.golden_lyre.item(), BardSkillColors.natures_minne, BardsEffects.NATURES_MINNE.entry, 0, BardsSounds.natures_minne_full.entry(), 740),
            new Song(BardsEffects.SONG_OF_CELERITY.title, Weapons.golden_lyre.item(), BardSkillColors.song_of_celerity, BardsEffects.SONG_OF_CELERITY.entry, 0, BardsSounds.song_of_celerity_full.entry(), 720),
            new Song(BardsEffects.CANTICLES_OF_THE_TIDES.title, Weapons.diamond_lyre.item(), BardSkillColors.canticle_of_the_tides, BardsEffects.CANTICLES_OF_THE_TIDES.entry, 0, BardsSounds.canticle_of_the_tides_full.entry(), 660),
            new Song(BardsEffects.HYMN_OF_THE_GOLDEN_LIGHT.title, Weapons.diamond_lyre.item(), BardSkillColors.hymn_of_the_golden_light, BardsEffects.HYMN_OF_THE_GOLDEN_LIGHT.entry, 0, BardsSounds.hymn_of_the_golden_light_full.entry(), 1000),
            new Song(BardsEffects.SONG_OF_THE_TURNING_SKY.title, Weapons.diamond_lyre.item(), BardSkillColors.song_of_the_turning_sky, BardsEffects.SONG_OF_THE_TURNING_SKY.entry, 0, BardsSounds.song_of_the_turning_sky_full.entry(), 700)
    );
}
