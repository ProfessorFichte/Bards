package com.bard_rpg.mixin;

import com.bard_rpg.BardsMod;
import com.bard_rpg.effect.BardsEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.spell_engine.utils.TargetHelper;
import net.spell_power.api.SpellPower;
import net.spell_power.api.SpellSchools;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class ArmiesPaeonMixin {

    @Inject(method = "damage", at = @At("RETURN"))
    private void armiesPaeon$onDamaged(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;

        LivingEntity livingAttacker = getLivingAttacker(source);
        if (livingAttacker == null) return;
        if (livingAttacker.getWorld().isClient()) return;
        if (!livingAttacker.hasStatusEffect(BardsEffects.ARMYS_PAEON_STASH)) return;

        float healPower = (float) SpellPower.getSpellPower(SpellSchools.HEALING, livingAttacker).baseValue();
        int amplifierCap = BardsMod.tweaksConfig.value.armys_paeon_amplifier_cap + (int)(healPower * BardsMod.tweaksConfig.value.armys_paeon_cap_multiplier );

        float range = 5.0F;
        for (Entity ally : livingAttacker.getWorld().getOtherEntities(livingAttacker, livingAttacker.getBoundingBox().expand(range))) {
            if (ally instanceof LivingEntity livingAlly && TargetHelper.getRelation(livingAttacker, livingAlly) == TargetHelper.Relation.FRIENDLY) {
                applyOrIncrementMotivation(livingAlly, amplifierCap);
            }
        }
        applyOrIncrementMotivation(livingAttacker, amplifierCap);
    }

    private static void applyOrIncrementMotivation(LivingEntity entity, int amplifierCap) {
        var existing = entity.getStatusEffect(BardsEffects.ARMYS_PAEON);
        if (existing == null) {
            entity.addStatusEffect(new StatusEffectInstance(BardsEffects.ARMYS_PAEON, 200, 0, false, true, true));
        } else if (existing.getAmplifier() < amplifierCap) {
            entity.addStatusEffect(new StatusEffectInstance(BardsEffects.ARMYS_PAEON, existing.getDuration(), existing.getAmplifier() + 1, false, true, true));
        }
    }

    private static LivingEntity getLivingAttacker(DamageSource source) {
        if (source.getAttacker() instanceof LivingEntity living) {
            return living;
        }
        if (source.getSource() instanceof PersistentProjectileEntity projectile
                && projectile.getOwner() instanceof LivingEntity living) {
            return living;
        }
        return null;
    }
}
