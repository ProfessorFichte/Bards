package com.bard_rpg.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class WardensPaeanBeneficialEffect extends StatusEffect {
    public static final Set<UUID> PENDING_REMOVAL = Collections.newSetFromMap(new ConcurrentHashMap<>());
    public static final Set<UUID> PENDING_DECREMENT = Collections.newSetFromMap(new ConcurrentHashMap<>());
    public static final Set<UUID> DECREMENTING = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public WardensPaeanBeneficialEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (entity.getWorld().isClient()) return;
        if (DECREMENTING.remove(entity.getUuid())) return;
        for (var instance : new ArrayList<>(entity.getStatusEffects())) {
            if (!instance.getEffectType().isBeneficial()) {
                entity.removeStatusEffect(instance.getEffectType());
                if (amplifier == 0) {
                    PENDING_REMOVAL.add(entity.getUuid());
                } else {
                    PENDING_DECREMENT.add(entity.getUuid());
                }
                return;
            }
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (PENDING_REMOVAL.remove(entity.getUuid())) {
            entity.removeStatusEffect(BardsEffects.BENEFICIAL_WARDENS_PAEAN);
        } else if (PENDING_DECREMENT.remove(entity.getUuid())) {
            var current = entity.getStatusEffect(BardsEffects.BENEFICIAL_WARDENS_PAEAN);
            if (current != null) {
                DECREMENTING.add(entity.getUuid());
                entity.removeStatusEffect(BardsEffects.BENEFICIAL_WARDENS_PAEAN);
                entity.addStatusEffect(new StatusEffectInstance(
                    BardsEffects.BENEFICIAL_WARDENS_PAEAN,
                    current.getDuration(),
                    current.getAmplifier() - 1
                ));
            }
        }
    }
}
