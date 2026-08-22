package com.bards.content;

import com.bards.BardsMod;
import com.bards.effect.BardsEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.more_rpg_classes.util.CustomMethods;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.event.SpellHandlers;
import net.spell_engine.internals.SpellExecution;
import net.spell_power.api.SpellPower;

public class ArmiesPaeonImpact implements SpellHandlers.CustomImpact {

    @Override
    public SpellHandlers.ImpactResult onSpellImpact(
            RegistryEntry<Spell> spell,
            SpellPower.Result powerResult,
            LivingEntity caster,
            Entity target,
            SpellExecution.ImpactContext context
    ) {
        double highestDamageAttribute = CustomMethods.getHighestDamageAttribute(caster);
        int effectAmplifier = caster.getStatusEffect(BardsEffects.ARMYS_PAEON.entry).getAmplifier() +1;
        float damageMultiplier = BardsMod.tweaksConfig.value.armies_paeon_impact_multiplier * effectAmplifier;

        target.timeUntilRegen = 0;
        target.damage(target.getDamageSources().magic(), (float) (highestDamageAttribute * damageMultiplier));
        return new SpellHandlers.ImpactResult(true, false);
    }
}
