package com.bard_rpg.client;

import com.bard_rpg.BardsMod;
import com.bard_rpg.effect.BardsEffects;
import com.bard_rpg.effect.HymnOfTheGoldenLightEffect;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.util.Identifier;
import net.spell_engine.client.gui.SpellTooltip;

import java.text.DecimalFormat;

import static com.bard_rpg.BardsMod.MOD_ID;

// Spell Engine 1.20.1 doesn't resolve the "{bonus}" description mutator on its own (unlike 1.21.1's
// SpellTooltip.bonus helper) - each spell that shows a %-amount from a status effect's attribute
// modifier needs its own mutator registered here, mirroring the values used in BardsEffects.register().
public class SpellDescriptionMutators {

    public static void register() {
        addBonus("crescendo", () -> BardsMod.effectsConfig.value.crescendo_incoming_damage_increase, EntityAttributeModifier.Operation.MULTIPLY_BASE);
        addBonus("discordant_note", () -> BardsEffects.offensiveReductionDiscordant, EntityAttributeModifier.Operation.MULTIPLY_BASE);
        addBonus("magical_ballad", () -> 0.025F, EntityAttributeModifier.Operation.MULTIPLY_BASE);
        addBonus("natures_minne_0", () -> BardsEffects.naturesMinneHealingTakenIncrease, EntityAttributeModifier.Operation.MULTIPLY_BASE);
        addBonus("natures_minne", () -> BardsEffects.naturesMinneHealingTakenIncrease, EntityAttributeModifier.Operation.MULTIPLY_BASE);
        addBonus("song_of_celerity", () -> 0.03F, EntityAttributeModifier.Operation.MULTIPLY_BASE);
        addBonus("song_of_the_turning_sky", () -> BardsEffects.songOfTheTurningSkyIncomingDamageReduction, EntityAttributeModifier.Operation.MULTIPLY_BASE);
        addBonus("tale_of_the_dragonslayer", () -> BardsEffects.dragonSlayerMultiplier, EntityAttributeModifier.Operation.MULTIPLY_BASE);
        addBonus("troubadours_minuet_0", () -> BardsEffects.troubadoursMinuetIncomingDamageReduction, EntityAttributeModifier.Operation.MULTIPLY_BASE);
        addBonus("troubadours_minuet", () -> BardsEffects.troubadoursMinuetIncomingDamageReduction, EntityAttributeModifier.Operation.MULTIPLY_BASE);
        addBonus("vicious_mockery", () -> BardsMod.effectsConfig.value.vicious_mockery_incoming_damage_increase, EntityAttributeModifier.Operation.MULTIPLY_BASE);
        addBonus("wanderers_minuet", () -> BardsEffects.critChanceIncrease, EntityAttributeModifier.Operation.MULTIPLY_BASE);

        var hymnEffect = (HymnOfTheGoldenLightEffect) BardsEffects.HYMN_OF_THE_GOLDEN_LIGHT;
        addLiteral("hymn_of_the_golden_light", () -> formattedNumber(hymnEffect.getAbsorptionPerLevel()));
    }

    private interface FloatSupplier { float get(); }

    private static void addBonus(String spellPath, FloatSupplier amount, EntityAttributeModifier.Operation operation) {
        var id = new Identifier(MOD_ID, spellPath);
        SpellTooltip.addDescriptionMutator(id, args -> args.description()
                .replace("{bonus}", bonus(amount.get(), operation)));
    }

    private static void addLiteral(String spellPath, java.util.function.Supplier<String> value) {
        var id = new Identifier(MOD_ID, spellPath);
        SpellTooltip.addDescriptionMutator(id, args -> args.description()
                .replace("{bonus}", value.get()));
    }

    public static String bonus(float amount, EntityAttributeModifier.Operation operation) {
        switch (operation) {
            case ADDITION -> {
                return formattedNumber(amount);
            }
            case MULTIPLY_BASE -> {
                return percent(amount);
            }
            case MULTIPLY_TOTAL -> {
                return percent(amount - 1F);
            }
        }
        return "";
    }

    private static String percent(float chance) {
        return (int) (chance * 100) + "%";
    }

    private static String formattedNumber(float number) {
        DecimalFormat formatter = new DecimalFormat();
        formatter.setMaximumFractionDigits(1);
        return formatter.format(number);
    }
}
