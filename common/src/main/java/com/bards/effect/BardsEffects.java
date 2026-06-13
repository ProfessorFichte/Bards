package com.bards.effect;

import net.fabric_extras.ranged_weapon.api.EntityAttributes_RangedWeapon;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.spell_engine.api.config.AttributeModifier;
import net.spell_engine.api.config.ConfigFile;
import net.spell_engine.api.config.EffectConfig;
import net.spell_engine.api.effect.*;
import net.spell_engine.api.entity.SpellEngineAttributes;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.List;

import static com.bards.BardsMod.MOD_ID;

public class BardsEffects {
    public static final List<Effects.Entry> entries = new ArrayList<>();
    private static Effects.Entry add(Effects.Entry entry) {
        entries.add(entry);
        return entry;
    }

    public static Effects.Entry BALLAD = add(new Effects.Entry(Identifier.of(MOD_ID, "ballad"),
            "Ballad",
            "Increases Attack Damage, Ranged Damage & Spell Power.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellSchools.GENERIC.id.toString(),
                                    0.025F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(),
                                    0.025F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    EntityAttributes_RangedWeapon.DAMAGE.id.toString(),
                                    0.025F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry TROUBADOURS_MINUET = add(new Effects.Entry(Identifier.of(MOD_ID, "troubadours_minuet"),
            "Troubadours Minuet",
            "Reduces damage taken.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.DAMAGE_TAKEN.id.toString(),
                                    -0.03F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static float critChanceIncrease = 0.02F;
    public static float critDamageIncrease = 0.04F;
    public static Effects.Entry WANDERERS_MINUET = add(new Effects.Entry(Identifier.of(MOD_ID, "wanderers_minuet"),
            "Wanderer's Minuet",
            "Increases Critical Chance and Critical Damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellPowerMechanics.CRITICAL_CHANCE.id,
                                    critChanceIncrease,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    "critical_strike:chance",
                                    critChanceIncrease,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    SpellPowerMechanics.CRITICAL_DAMAGE.id,
                                    critDamageIncrease,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    "critical_strike:damage",
                                    critDamageIncrease,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry ARMYS_PAEON_STASH = add(new Effects.Entry(Identifier.of(MOD_ID, "armys_paeon"),
            "Army's Paeon",
            "The player buffs nearby allies if he damages enemies with melee hits, arrows or spells.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff),
            new EffectConfig(
                    List.of(
                    )
            )
    ));
    public static Effects.Entry ARMYS_PAEON = add(new Effects.Entry(Identifier.of(MOD_ID, "armys_motivation"),
            "Army's Motivation",
            "With each melee-, arrow- & spell-hit you deal magic damage, according to the highest attribute, scaling with the effect amplifier.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff),
            new EffectConfig(
                    List.of(
                    )
            )
    ));
    public static Effects.Entry NATURES_MINNE = add(new Effects.Entry(Identifier.of(MOD_ID, "natures_minne"),
            "Natures Minne",
            "Increases Healing taken.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.HEALING_TAKEN.id.toString(),
                                    0.03F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry SONG_OF_CELERITY = add(new Effects.Entry(Identifier.of(MOD_ID, "song_of_celerity"),
            "Song of Celerity",
            "Increases Movement Speed.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_MOVEMENT_SPEED.getIdAsString(),
                                    0.03F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry CRESCENDO = add(new Effects.Entry(Identifier.of(MOD_ID, "crescendo"),
            "Crescendo",
            "Stuns the target and increases damage taken.",
            new CustomStatusEffect(StatusEffectCategory.HARMFUL, 0x9999ff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.DAMAGE_TAKEN.id,
                                    0.05F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry VICIOUS_MOCKERY = add(new Effects.Entry(Identifier.of(MOD_ID, "vicious_mockery"),
            "Vicious Mockery",
            "Decreases Attack Damage, Ranged Damage & Spell Power.",
            new CustomStatusEffect(StatusEffectCategory.HARMFUL, 0x9999ff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellSchools.GENERIC.id,
                                    -0.05F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(),
                                    -0.05F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    EntityAttributes_RangedWeapon.DAMAGE.id.toString(),
                                    -0.05F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    SpellEngineAttributes.DAMAGE_TAKEN.id.toString(),
                                    0.05F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )

                    )
            )
    ));
    public static Effects.Entry HARMFUL_WARDENS_PAEAN = add(new Effects.Entry(Identifier.of(MOD_ID, "wardens_paean_harmful"),
            "Warden's Paean",
            "Removes a beneficial status effect when applied, if none is present, the next beneficial status effect wont get applied.",
            new WardensPaeanHarmfulEffect(StatusEffectCategory.HARMFUL, 0x9999ff),
            new EffectConfig(
                    List.of(
                    )
            )
    ));
    public static Effects.Entry BENEFICIAL_WARDENS_PAEAN = add(new Effects.Entry(Identifier.of(MOD_ID, "wardens_paean_beneficial"),
            "Warden's Paean",
            "Removes a harmful status effect when applied, if none is present, the next harmful status effect wont get applied.",
            new WardensPaeanBeneficialEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff),
            new EffectConfig(
                    List.of(
                    )
            )
    ));
        public static Effects.Entry ASTRAL_SHOTS = add(new Effects.Entry(Identifier.of(MOD_ID, "astral_shots"),
            "Astral Shot's",
            "Fast ranged shot with magical extra damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes_RangedWeapon.PULL_TIME.id,
                                    0.2F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                            )
                    )
            )
    ));
    public static Effects.Entry HYMN_OF_THE_GOLDEN_LIGHT = add(new Effects.Entry(Identifier.of(MOD_ID, "hymn_of_the_golden_light"),
            "Hymn of the Golden Light",
            "Refreshes the absorption hearts granted by this effect every 2 seconds.",
            new HymnOfTheGoldenLightEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff, 2.0F),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_MAX_ABSORPTION.getIdAsString(),
                                    2.0F,
                                    EntityAttributeModifier.Operation.ADD_VALUE
                            )
                    )
            )
    ));
    public static Effects.Entry SONG_OF_THE_TURNING_SKY = add(new Effects.Entry(Identifier.of(MOD_ID, "song_of_the_turning_sky"),
            "Song of the Turning Sky",
            "Reduces damage taken.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.DAMAGE_TAKEN.id.toString(),
                                    -0.02F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static float offensiveReductionDiscordant = -0.02F;
    public static Effects.Entry DISCORDANT_NOTE = add(new Effects.Entry(Identifier.of(MOD_ID, "discordant_note"),
            "Discordant Note",
            "Reduces Attack Damage, Ranged Damage & Spell Power. There is also a chance, that the entity will hurt itself.",
            new CustomStatusEffect(StatusEffectCategory.HARMFUL, 0x9999ff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellSchools.GENERIC.id.toString(),
                                    offensiveReductionDiscordant,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(),
                                    offensiveReductionDiscordant,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    EntityAttributes_RangedWeapon.DAMAGE.id.toString(),
                                    offensiveReductionDiscordant,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry CANTICLES_OF_THE_TIDES = add(new Effects.Entry(Identifier.of(MOD_ID, "canticle_of_the_tides"),
            "Canticles of the Tides",
            "Periodically heals the caster and nearby allies every 3 seconds, scales with the effects amplifier. ",
            new CanticlesOfTheTidesEffect(StatusEffectCategory.BENEFICIAL, 0x33aaff),
            new EffectConfig(List.of())
    ));
    public static float dragonSlayerMultiplier = 0.01F;
    public static Effects.Entry TALE_OF_THE_DRAGON_SLAYER = add(new Effects.Entry(Identifier.of(MOD_ID, "tale_of_the_dragonslayer"),
            "Tale of the Dragon Slayer",
            "Increases Attack Damage, Ranged Damage & Spell Power.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellSchools.GENERIC.id.toString(),
                                    dragonSlayerMultiplier,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(),
                                    dragonSlayerMultiplier,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    EntityAttributes_RangedWeapon.DAMAGE.id.toString(),
                                    dragonSlayerMultiplier,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry ECLIPSE_MANTLE = add(new Effects.Entry(Identifier.of(MOD_ID, "eclipse_mantle"),
            "Eclipse Mantle",
            "Increases Evasion",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.EVASION_CHANCE.id.toString(),
                                    0.15F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));

    public static void register(ConfigFile.Effects config) {
        for (var entry : entries) {
            Synchronized.configure(entry.effect, true);
        }
        ActionImpairing.configure(CRESCENDO.effect, EntityActionsAllowed.STUN);
        Effects.register(entries, config.effects);

    }
}
