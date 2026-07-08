package com.bard_rpg.effect;

import com.bard_rpg.BardsMod;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.entity.attribute.MRPGCEntityAttributes;
import net.spell_engine.api.effect.ActionImpairing;
import net.spell_engine.api.effect.EntityActionsAllowed;
import net.spell_engine.api.effect.Synchronized;
import net.spell_power.api.SpellSchools;

import static com.bard_rpg.BardsMod.MOD_ID;

public class BardsEffects {

    public static StatusEffect BALLAD = new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff);
    public static StatusEffect TROUBADOURS_MINUET = new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff);
    public static StatusEffect WANDERERS_MINUET = new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff);
    public static StatusEffect ARMYS_PAEON_STASH = new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff);
    public static StatusEffect ARMYS_PAEON = new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff);
    public static StatusEffect NATURES_MINNE = new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff);
    public static StatusEffect SONG_OF_CELERITY = new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff);
    public static StatusEffect CRESCENDO = new CustomStatusEffect(StatusEffectCategory.HARMFUL, 0x9999ff);
    public static StatusEffect VICIOUS_MOCKERY = new CustomStatusEffect(StatusEffectCategory.HARMFUL, 0x9999ff);
    public static StatusEffect HARMFUL_WARDENS_PAEAN = new WardensPaeanHarmfulEffect(StatusEffectCategory.HARMFUL, 0x9999ff);
    public static StatusEffect BENEFICIAL_WARDENS_PAEAN = new WardensPaeanBeneficialEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff);
    public static StatusEffect ASTRAL_SHOTS = new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff);
    public static StatusEffect HYMN_OF_THE_GOLDEN_LIGHT = new HymnOfTheGoldenLightEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff, 2.0F);
    public static StatusEffect SONG_OF_THE_TURNING_SKY = new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff);
    public static StatusEffect DISCORDANT_NOTE = new CustomStatusEffect(StatusEffectCategory.HARMFUL, 0x9999ff);
    public static StatusEffect CANTICLES_OF_THE_TIDES = new CanticlesOfTheTidesEffect(StatusEffectCategory.BENEFICIAL, 0x33aaff);
    public static StatusEffect TALE_OF_THE_DRAGON_SLAYER = new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff);
    public static StatusEffect ECLIPSE_MANTLE = new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff);

    public static float critChanceIncrease = 0.01F;
    public static float critDamageIncrease = 0.03F;
    public static float offensiveReductionDiscordant = -0.02F;
    public static float dragonSlayerMultiplier = 0.01F;

    public static void register() {
        BALLAD
            .addAttributeModifier(SpellSchools.ARCANE.attribute, "a1b2c3d4-e5f6-7890-abcd-ef1234567801", 0.025, EntityAttributeModifier.Operation.MULTIPLY_BASE)
            .addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, "a1b2c3d4-e5f6-7890-abcd-ef1234567802", 0.025, EntityAttributeModifier.Operation.MULTIPLY_BASE);

        SONG_OF_CELERITY
            .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "a1b2c3d4-e5f6-7890-abcd-ef1234567810", 0.03, EntityAttributeModifier.Operation.MULTIPLY_BASE);

        VICIOUS_MOCKERY
                .addAttributeModifier(MRPGCEntityAttributes.DAMAGE_TAKEN, "3f8a1c2e-7b9d-4a6f-8c21-5d3e9b7f1024", BardsMod.effectsConfig.value.vicious_mockery_incoming_damage_increase, EntityAttributeModifier.Operation.MULTIPLY_BASE);

        DISCORDANT_NOTE
            .addAttributeModifier(SpellSchools.ARCANE.attribute, "a1b2c3d4-e5f6-7890-abcd-ef1234567830", offensiveReductionDiscordant, EntityAttributeModifier.Operation.MULTIPLY_BASE)
            .addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, "a1b2c3d4-e5f6-7890-abcd-ef1234567831", offensiveReductionDiscordant, EntityAttributeModifier.Operation.MULTIPLY_BASE);

        TALE_OF_THE_DRAGON_SLAYER
            .addAttributeModifier(SpellSchools.ARCANE.attribute, "a1b2c3d4-e5f6-7890-abcd-ef1234567840", dragonSlayerMultiplier, EntityAttributeModifier.Operation.MULTIPLY_BASE)
            .addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, "a1b2c3d4-e5f6-7890-abcd-ef1234567841", dragonSlayerMultiplier, EntityAttributeModifier.Operation.MULTIPLY_BASE);

        WANDERERS_MINUET
            .addAttributeModifier(SpellSchools.ARCANE.attribute, "a1b2c3d4-e5f6-7890-abcd-ef1234567850", critChanceIncrease, EntityAttributeModifier.Operation.MULTIPLY_BASE);

        CRESCENDO
                .addAttributeModifier(MRPGCEntityAttributes.DAMAGE_TAKEN, "3f8a1c2e-7b9d-4a6f-8c21-5d3e9b7f1024", BardsMod.effectsConfig.value.crescendo_incoming_damage_increase, EntityAttributeModifier.Operation.MULTIPLY_BASE);

        Synchronized.configure(BALLAD, true);
        Synchronized.configure(TROUBADOURS_MINUET, true);
        Synchronized.configure(WANDERERS_MINUET, true);
        Synchronized.configure(ARMYS_PAEON_STASH, true);
        Synchronized.configure(ARMYS_PAEON, true);
        Synchronized.configure(NATURES_MINNE, true);
        Synchronized.configure(SONG_OF_CELERITY, true);
        Synchronized.configure(CRESCENDO, true);
        Synchronized.configure(VICIOUS_MOCKERY, true);
        Synchronized.configure(HARMFUL_WARDENS_PAEAN, true);
        Synchronized.configure(BENEFICIAL_WARDENS_PAEAN, true);
        Synchronized.configure(ASTRAL_SHOTS, true);
        Synchronized.configure(HYMN_OF_THE_GOLDEN_LIGHT, true);
        Synchronized.configure(SONG_OF_THE_TURNING_SKY, true);
        Synchronized.configure(DISCORDANT_NOTE, true);
        Synchronized.configure(CANTICLES_OF_THE_TIDES, true);
        Synchronized.configure(TALE_OF_THE_DRAGON_SLAYER, true);
        Synchronized.configure(ECLIPSE_MANTLE, true);

        ActionImpairing.configure(CRESCENDO, EntityActionsAllowed.STUN);

        int id = 6000;
        Registry.register(Registries.STATUS_EFFECT, id++, new Identifier(MOD_ID, "ballad").toString(), BALLAD);
        Registry.register(Registries.STATUS_EFFECT, id++, new Identifier(MOD_ID, "troubadours_minuet").toString(), TROUBADOURS_MINUET);
        Registry.register(Registries.STATUS_EFFECT, id++, new Identifier(MOD_ID, "wanderers_minuet").toString(), WANDERERS_MINUET);
        Registry.register(Registries.STATUS_EFFECT, id++, new Identifier(MOD_ID, "armys_paeon").toString(), ARMYS_PAEON_STASH);
        Registry.register(Registries.STATUS_EFFECT, id++, new Identifier(MOD_ID, "armys_motivation").toString(), ARMYS_PAEON);
        Registry.register(Registries.STATUS_EFFECT, id++, new Identifier(MOD_ID, "natures_minne").toString(), NATURES_MINNE);
        Registry.register(Registries.STATUS_EFFECT, id++, new Identifier(MOD_ID, "song_of_celerity").toString(), SONG_OF_CELERITY);
        Registry.register(Registries.STATUS_EFFECT, id++, new Identifier(MOD_ID, "crescendo").toString(), CRESCENDO);
        Registry.register(Registries.STATUS_EFFECT, id++, new Identifier(MOD_ID, "vicious_mockery").toString(), VICIOUS_MOCKERY);
        Registry.register(Registries.STATUS_EFFECT, id++, new Identifier(MOD_ID, "wardens_paean_harmful").toString(), HARMFUL_WARDENS_PAEAN);
        Registry.register(Registries.STATUS_EFFECT, id++, new Identifier(MOD_ID, "wardens_paean_beneficial").toString(), BENEFICIAL_WARDENS_PAEAN);
        Registry.register(Registries.STATUS_EFFECT, id++, new Identifier(MOD_ID, "astral_shots").toString(), ASTRAL_SHOTS);
        Registry.register(Registries.STATUS_EFFECT, id++, new Identifier(MOD_ID, "hymn_of_the_golden_light").toString(), HYMN_OF_THE_GOLDEN_LIGHT);
        Registry.register(Registries.STATUS_EFFECT, id++, new Identifier(MOD_ID, "song_of_the_turning_sky").toString(), SONG_OF_THE_TURNING_SKY);
        Registry.register(Registries.STATUS_EFFECT, id++, new Identifier(MOD_ID, "discordant_note").toString(), DISCORDANT_NOTE);
        Registry.register(Registries.STATUS_EFFECT, id++, new Identifier(MOD_ID, "canticle_of_the_tides").toString(), CANTICLES_OF_THE_TIDES);
        Registry.register(Registries.STATUS_EFFECT, id++, new Identifier(MOD_ID, "tale_of_the_dragonslayer").toString(), TALE_OF_THE_DRAGON_SLAYER);
        Registry.register(Registries.STATUS_EFFECT, id++, new Identifier(MOD_ID, "eclipse_mantle").toString(), ECLIPSE_MANTLE);
    }
}
