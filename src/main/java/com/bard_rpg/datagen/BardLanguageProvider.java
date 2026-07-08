package com.bard_rpg.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import static com.bard_rpg.BardsMod.MOD_ID;

public class BardLanguageProvider extends FabricLanguageProvider {
    public BardLanguageProvider(FabricDataOutput output) {
        super(output, "en_us");
    }

    @Override
    public void generateTranslations(TranslationBuilder builder) {
        builder.add("itemGroup." + MOD_ID + ".general", "Bards RPG");
        builder.add("item." + MOD_ID + ".spell_book/bard", "Bard's Songbook");
        builder.add("item." + MOD_ID + ".spell_scroll/bard", "Bard's Scroll");
        builder.add("entity.minecraft.villager." + MOD_ID + ".luthier", "Luthier");

        builder.add("item." + MOD_ID + ".golden_rapier", "Golden Rapier");
        builder.add("item." + MOD_ID + ".iron_rapier", "Iron Rapier");
        builder.add("item." + MOD_ID + ".diamond_rapier", "Diamond Rapier");
        builder.add("item." + MOD_ID + ".netherite_rapier", "Netherite Rapier");
        builder.add("item." + MOD_ID + ".wooden_lute", "Lute");
        builder.add("item." + MOD_ID + ".diamond_lute", "Diamond Lute");
        builder.add("item." + MOD_ID + ".netherite_lute", "Netherite Lute");
        builder.add("item." + MOD_ID + ".golden_lyre", "Lyre");
        builder.add("item." + MOD_ID + ".diamond_lyre", "Diamond Lyre");
        builder.add("item." + MOD_ID + ".netherite_lyre", "Netherite Lyre");
        builder.add("item." + MOD_ID + ".harp_crossbow", "Harp Crossbow");
        builder.add("item." + MOD_ID + ".diamond_harp_crossbow", "Diamond Harp Crossbow");
        builder.add("item." + MOD_ID + ".netherite_harp_crossbow", "Netherite Harp Crossbow");
        builder.add("item." + MOD_ID + ".ender_dragon_rapier", "Dragon Rapier");
        builder.add("item." + MOD_ID + ".elder_guardian_rapier", "Elder Rapier");
        builder.add("item." + MOD_ID + ".wither_rapier", "Wither Rapier");
        builder.add("item." + MOD_ID + ".glacial_rapier", "Glacial Rapier");
        builder.add("item." + MOD_ID + ".ender_dragon_lute", "Dragon Lute");
        builder.add("item." + MOD_ID + ".elder_guardian_lyre", "Siren's Lyre");

        builder.add("item." + MOD_ID + ".entertainers_garb_head", "Entertainer's Cap");
        builder.add("item." + MOD_ID + ".entertainers_garb_chest", "Entertainer's Garb");
        builder.add("item." + MOD_ID + ".entertainers_garb_legs", "Entertainer's Tights");
        builder.add("item." + MOD_ID + ".entertainers_garb_feet", "Entertainer's Boots");
        builder.add("item." + MOD_ID + ".troubadours_garb_head", "Troubadour's Hat");
        builder.add("item." + MOD_ID + ".troubadours_garb_chest", "Troubadour's Coat");
        builder.add("item." + MOD_ID + ".troubadours_garb_legs", "Troubadour's Trousers");
        builder.add("item." + MOD_ID + ".troubadours_garb_feet", "Troubadour's Boots");
        builder.add("item." + MOD_ID + ".netherite_troubadours_garb_head", "Netherite Troubadour's Hat");
        builder.add("item." + MOD_ID + ".netherite_troubadours_garb_chest", "Netherite Troubadour's Coat");
        builder.add("item." + MOD_ID + ".netherite_troubadours_garb_legs", "Netherite Troubadour's Trousers");
        builder.add("item." + MOD_ID + ".netherite_troubadours_garb_feet", "Netherite Troubadour's Boots");

        builder.add("effect." + MOD_ID + ".ballad", "Magical Ballad");
        builder.add("effect." + MOD_ID + ".troubadours_minuet", "Troubadour's Minuet");
        builder.add("effect." + MOD_ID + ".wanderers_minuet", "Wanderer's Minuet");
        builder.add("effect." + MOD_ID + ".armys_paeon", "Army's Paeon");
        builder.add("effect." + MOD_ID + ".armys_motivation", "Army's Motivation");
        builder.add("effect." + MOD_ID + ".natures_minne", "Nature's Minne");
        builder.add("effect." + MOD_ID + ".song_of_celerity", "Song of Celerity");
        builder.add("effect." + MOD_ID + ".crescendo", "Crescendo");
        builder.add("effect." + MOD_ID + ".vicious_mockery", "Vicious Mockery");
        builder.add("effect." + MOD_ID + ".wardens_paean_harmful", "Warden's Paean");
        builder.add("effect." + MOD_ID + ".wardens_paean_beneficial", "Warden's Paean");
        builder.add("effect." + MOD_ID + ".astral_shots", "Astral Shots");
        builder.add("effect." + MOD_ID + ".hymn_of_the_golden_light", "Hymn of the Golden Light");
        builder.add("effect." + MOD_ID + ".song_of_the_turning_sky", "Song of the Turning Sky");
        builder.add("effect." + MOD_ID + ".discordant_note", "Discordant Note");
        builder.add("effect." + MOD_ID + ".canticle_of_the_tides", "Canticle of the Tides");
        builder.add("effect." + MOD_ID + ".tale_of_the_dragonslayer", "Tale of the Dragon Slayer");
        builder.add("effect." + MOD_ID + ".eclipse_mantle", "Eclipse Mantle");

        builder.add("spell." + MOD_ID + ".troubadours_minuet.name", "Troubadour's Minuet");
        builder.add("spell." + MOD_ID + ".troubadours_minuet.description", "Minuet that deals {damage} damage to enemies and heals allies. Reduces incoming damage per stack.");
        builder.add("spell." + MOD_ID + ".wanderers_minuet.name", "Wanderer's Minuet");
        builder.add("spell." + MOD_ID + ".wanderers_minuet.description", "Minuet that deals {damage} damage and increases critical chance for allies per stack.");
        builder.add("spell." + MOD_ID + ".natures_minne.name", "Nature's Minne");
        builder.add("spell." + MOD_ID + ".natures_minne.description", "Soothing song that deals {damage} damage, heals allies and increases healing taken per stack.");
        builder.add("spell." + MOD_ID + ".song_of_celerity.name", "Song of Celerity");
        builder.add("spell." + MOD_ID + ".song_of_celerity.description", "Lively song that deals {damage} damage and increases movement speed for allies per stack.");
        builder.add("spell." + MOD_ID + ".hymn_of_the_golden_light.name", "Hymn of the Golden Light");
        builder.add("spell." + MOD_ID + ".hymn_of_the_golden_light.description", "Soothing hymn that deals {damage} damage, heals allies and adds absorption hearts per stack.");
        builder.add("spell." + MOD_ID + ".song_of_the_turning_sky.name", "Song of the Turning Sky");
        builder.add("spell." + MOD_ID + ".song_of_the_turning_sky.description", "Periodic song that deals {damage} damage. Heals low-health allies, reduces damage for high-health allies.");
        builder.add("spell." + MOD_ID + ".canticle_of_the_tides.name", "Canticle of the Tides");
        builder.add("spell." + MOD_ID + ".canticle_of_the_tides.description", "Deep sea canticle that deals {damage} damage and adds a pulsating area heal on allies.");
        builder.add("spell." + MOD_ID + ".discordant_note.name", "Discordant Note");
        builder.add("spell." + MOD_ID + ".discordant_note.description", "A discordant note that deals {damage} damage and reduces offensive stats of enemies per stack.");
        builder.add("spell." + MOD_ID + ".tale_of_the_dragonslayer.name", "Tale of the Dragon Slayer");
        builder.add("spell." + MOD_ID + ".tale_of_the_dragonslayer.description", "Tale that deals {damage} damage and increases offensive stats for allies per stack.");
        builder.add("spell." + MOD_ID + ".secret_sonata.name", "Secret Sonata");
        builder.add("spell." + MOD_ID + ".secret_sonata.description", "A secret song that performs a random impact from all the other Songs.");
        builder.add("spell." + MOD_ID + ".magical_ballad.name", "Magical Ballad");
        builder.add("spell." + MOD_ID + ".magical_ballad.description", "Launch a magical ballad dealing {damage} damage to enemies and increasing offensive stats for allies.");
        builder.add("spell." + MOD_ID + ".vicious_mockery.name", "Vicious Mockery");
        builder.add("spell." + MOD_ID + ".vicious_mockery.description", "Throw a string of insults at a target, dealing {damage} damage and decreasing its offensive power.");
        builder.add("spell." + MOD_ID + ".wardens_paean.name", "Warden's Paean");
        builder.add("spell." + MOD_ID + ".wardens_paean.description", "Strip status effects: remove beneficial effects from enemies and harmful effects from allies.");
        builder.add("spell." + MOD_ID + ".encore.name", "Encore");
        builder.add("spell." + MOD_ID + ".encore.description", "Deals {damage} damage to nearby targets.");
        builder.add("spell." + MOD_ID + ".armys_paeon.name", "Army's Paeon");
        builder.add("spell." + MOD_ID + ".armys_paeon.description", "Buff nearby allies. Enhance their strength when you damage enemies.");
        builder.add("spell." + MOD_ID + ".crescendo.name", "Crescendo");
        builder.add("spell." + MOD_ID + ".crescendo.description", "Strikes an irresistible chord, dealing {damage} damage and stunning enemies in range.");
    }
}
