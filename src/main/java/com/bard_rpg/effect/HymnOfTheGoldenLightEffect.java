package com.bard_rpg.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class HymnOfTheGoldenLightEffect extends StatusEffect {
    private final float absorptionPerLevel;

    public HymnOfTheGoldenLightEffect(StatusEffectCategory category, int color, float absorptionPerLevel) {
        super(category, color);
        this.absorptionPerLevel = absorptionPerLevel;
    }

    private float effectContribution(int amplifier) {
        return absorptionPerLevel * (amplifier + 1);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        float contribution = effectContribution(amplifier);
        if (entity.getAbsorptionAmount() < contribution) {
            entity.setAbsorptionAmount(contribution);
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration % 40 == 0;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        float contribution = effectContribution(amplifier);
        if (entity.getAbsorptionAmount() < contribution) {
            entity.setAbsorptionAmount(contribution);
        }
    }
}
