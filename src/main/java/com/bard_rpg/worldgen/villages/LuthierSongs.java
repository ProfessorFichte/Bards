package com.bard_rpg.worldgen.villages;

import com.bard_rpg.content.BardsSounds;
import com.bard_rpg.effect.BardsEffects;
import com.bard_rpg.item.Weapons;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.List;

public class LuthierSongs {

    public record Song(
            String name,
            Item heldInstrument,
            StatusEffect effect,
            int effectAmplifier,
            SoundEvent sound,
            Identifier soundId,
            int soundDurationTicks
    ) {}

    public static List<Song> SONGS;

    public static void init() {
        SONGS = List.of(
                new Song("troubadours_minuet", Weapons.wooden_lute.item(),
                        BardsEffects.TROUBADOURS_MINUET, 0,
                        BardsSounds.troubadours_minuet.soundEvent(),
                        BardsSounds.troubadours_minuet.id(), 520),
                new Song("natures_minne", Weapons.golden_lyre.item(),
                        BardsEffects.NATURES_MINNE, 0,
                        BardsSounds.natures_minne.soundEvent(),
                        BardsSounds.natures_minne.id(), 740),
                new Song("song_of_celerity", Weapons.wooden_lute.item(),
                        BardsEffects.SONG_OF_CELERITY, 0,
                        BardsSounds.song_of_celerity.soundEvent(),
                        BardsSounds.song_of_celerity.id(), 720)
        );
    }
}
