package com.bard_rpg.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.spell_engine.utils.TargetHelper;
import net.spell_power.api.SpellPower;
import net.spell_power.api.SpellSchools;

public class CanticlesOfTheTidesEffect extends StatusEffect {

    public CanticlesOfTheTidesEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration % 60 == 0;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity.getWorld().isClient()) return;

        float healPower = (float) SpellPower.getSpellPower(SpellSchools.HEALING, entity).baseValue();
        float healAmount = 1.0F + healPower * 0.1F * (amplifier + 1);

        entity.heal(healAmount);

        float range = 5.0F;
        for (Entity target : entity.getWorld().getOtherEntities(entity, entity.getBoundingBox().expand(range))) {
            if (target instanceof LivingEntity living
                    && TargetHelper.getRelation(entity, living) == TargetHelper.Relation.FRIENDLY) {
                living.heal(healAmount);
            }
        }
    }
}
