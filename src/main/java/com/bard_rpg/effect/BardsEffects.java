package com.bard_rpg.effect;

import com.bard_rpg.BardsMod;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.more_rpg_classes.entity.attribute.MRPGCEntityAttributes;
import net.spell_engine.api.effect.ActionImpairing;
import net.spell_engine.api.effect.EntityActionsAllowed;
import net.spell_engine.api.effect.Synchronized;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static com.bard_rpg.BardsMod.MOD_ID;

public class BardsEffects {

    // All spell power schools from SpellSchools and MoreSpellSchools, standing in for the
    // GENERIC spell power attribute that doesn't exist on 1.20.1.
    private static final List<SpellSchool> ALL_SPELL_SCHOOLS = List.of(
            SpellSchools.ARCANE, SpellSchools.FIRE, SpellSchools.FROST,
            SpellSchools.HEALING, SpellSchools.LIGHTNING, SpellSchools.SOUL,
            MoreSpellSchools.EARTH, MoreSpellSchools.WATER, MoreSpellSchools.AIR
    );

    private static StatusEffect addAttributeModifierAllSchools(StatusEffect effect, String seed, double amount, EntityAttributeModifier.Operation operation) {
        for (var school : ALL_SPELL_SCHOOLS) {
            var uuid = UUID.nameUUIDFromBytes((MOD_ID + ":" + seed + ":" + school.id).getBytes(StandardCharsets.UTF_8));
            effect.addAttributeModifier(school.attribute, uuid.toString(), amount, operation);
        }
        return effect;
    }

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
    public static StatusEffect HYMN_OF_THE_GOLDEN_LIGHT = new HymnOfTheGoldenLightEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff, 2.0F);
    public static StatusEffect SONG_OF_THE_TURNING_SKY = new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff);
    public static StatusEffect DISCORDANT_NOTE = new CustomStatusEffect(StatusEffectCategory.HARMFUL, 0x9999ff);
    public static StatusEffect CANTICLES_OF_THE_TIDES = new CanticlesOfTheTidesEffect(StatusEffectCategory.BENEFICIAL, 0x33aaff);
    public static StatusEffect TALE_OF_THE_DRAGON_SLAYER = new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff);

    public static float critChanceIncrease = 0.01F;
    public static float critDamageIncrease = 0.03F;
    public static float offensiveReductionDiscordant = -0.02F;
    public static float dragonSlayerMultiplier = 0.01F;
    public static float troubadoursMinuetIncomingDamageReduction = -0.03F;
    public static float naturesMinneHealingTakenIncrease = 0.03F;
    public static float songOfTheTurningSkyIncomingDamageReduction = -0.03F;

    public static void register() {
        addAttributeModifierAllSchools(BALLAD, "ballad_spell_power", 0.025, EntityAttributeModifier.Operation.MULTIPLY_BASE)
            .addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, "a1b2c3d4-e5f6-7890-abcd-ef1234567802", 0.025, EntityAttributeModifier.Operation.MULTIPLY_BASE);

        SONG_OF_CELERITY
            .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "a1b2c3d4-e5f6-7890-abcd-ef1234567810", 0.03, EntityAttributeModifier.Operation.MULTIPLY_BASE);

        TROUBADOURS_MINUET
            .addAttributeModifier(MRPGCEntityAttributes.DAMAGE_TAKEN, "7a1e4c6b-2f3d-49a8-9c5e-1b6d8f0a3c72", troubadoursMinuetIncomingDamageReduction, EntityAttributeModifier.Operation.MULTIPLY_BASE);

        NATURES_MINNE
            .addAttributeModifier(MRPGCEntityAttributes.HEALING_TAKEN, "8b2f5d7c-3a4e-4b91-8d6f-2c7e9a1b4d83", naturesMinneHealingTakenIncrease, EntityAttributeModifier.Operation.MULTIPLY_BASE);

        SONG_OF_THE_TURNING_SKY
            .addAttributeModifier(MRPGCEntityAttributes.DAMAGE_TAKEN, "9c3a6e8d-4b5f-4ca2-9e70-3d8f0b2c5e94", songOfTheTurningSkyIncomingDamageReduction, EntityAttributeModifier.Operation.MULTIPLY_BASE);

        VICIOUS_MOCKERY
                .addAttributeModifier(MRPGCEntityAttributes.DAMAGE_TAKEN, "3f8a1c2e-7b9d-4a6f-8c21-5d3e9b7f1024", BardsMod.effectsConfig.value.vicious_mockery_incoming_damage_increase, EntityAttributeModifier.Operation.MULTIPLY_BASE);

        addAttributeModifierAllSchools(DISCORDANT_NOTE, "discordant_note_spell_power", offensiveReductionDiscordant, EntityAttributeModifier.Operation.MULTIPLY_BASE)
            .addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, "a1b2c3d4-e5f6-7890-abcd-ef1234567831", offensiveReductionDiscordant, EntityAttributeModifier.Operation.MULTIPLY_BASE);

        addAttributeModifierAllSchools(TALE_OF_THE_DRAGON_SLAYER, "tale_of_the_dragonslayer_spell_power", dragonSlayerMultiplier, EntityAttributeModifier.Operation.MULTIPLY_BASE)
            .addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, "a1b2c3d4-e5f6-7890-abcd-ef1234567841", dragonSlayerMultiplier, EntityAttributeModifier.Operation.MULTIPLY_BASE);

        WANDERERS_MINUET
            .addAttributeModifier(SpellPowerMechanics.CRITICAL_CHANCE.attribute, "a1b2c3d4-e5f6-7890-abcd-ef1234567850", critChanceIncrease, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                .addAttributeModifier(SpellPowerMechanics.CRITICAL_DAMAGE.attribute, "a1b2c3d4-e5f6-7890-abcd-ef1234567851", critDamageIncrease, EntityAttributeModifier.Operation.MULTIPLY_BASE);

        CRESCENDO
                .addAttributeModifier(MRPGCEntityAttributes.DAMAGE_TAKEN, "5c9b2d3f-8c0e-5b7f-9d32-6e4fa0f82135", BardsMod.effectsConfig.value.crescendo_incoming_damage_increase, EntityAttributeModifier.Operation.MULTIPLY_BASE);

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
        Synchronized.configure(HYMN_OF_THE_GOLDEN_LIGHT, true);
        Synchronized.configure(SONG_OF_THE_TURNING_SKY, true);
        Synchronized.configure(DISCORDANT_NOTE, true);
        Synchronized.configure(CANTICLES_OF_THE_TIDES, true);
        Synchronized.configure(TALE_OF_THE_DRAGON_SLAYER, true);
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
        Registry.register(Registries.STATUS_EFFECT, id++, new Identifier(MOD_ID, "hymn_of_the_golden_light").toString(), HYMN_OF_THE_GOLDEN_LIGHT);
        Registry.register(Registries.STATUS_EFFECT, id++, new Identifier(MOD_ID, "song_of_the_turning_sky").toString(), SONG_OF_THE_TURNING_SKY);
        Registry.register(Registries.STATUS_EFFECT, id++, new Identifier(MOD_ID, "discordant_note").toString(), DISCORDANT_NOTE);
        Registry.register(Registries.STATUS_EFFECT, id++, new Identifier(MOD_ID, "canticle_of_the_tides").toString(), CANTICLES_OF_THE_TIDES);
        Registry.register(Registries.STATUS_EFFECT, id++, new Identifier(MOD_ID, "tale_of_the_dragonslayer").toString(), TALE_OF_THE_DRAGON_SLAYER);
    }
}
