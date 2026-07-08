package com.bard_rpg.mixin;

import com.bard_rpg.BardsMod;
import com.bard_rpg.effect.BardsEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.more_rpg_classes.util.CustomMethods;
import net.spell_engine.api.spell.ParticleBatch;
import net.spell_engine.particle.ParticleHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class ArmiesMotivationMixin {

    private static final ThreadLocal<Boolean> IS_DEALING_EXTRA_DAMAGE = ThreadLocal.withInitial(() -> false);

    private static final ParticleBatch[] NOTE_PARTICLES = {
        new ParticleBatch("more_rpg_classes:music_note_gold", ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER, null, 3, 0.3F, 0.6F, 0, 0)
    };

    @Inject(method = "damage", at = @At("RETURN"))
    private void armiesMotivation$extraDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;
        if (IS_DEALING_EXTRA_DAMAGE.get()) return;

        LivingEntity livingAttacker = getLivingAttacker(source);
        if (livingAttacker == null) return;
        if (livingAttacker.getWorld().isClient()) return;

        var effect = livingAttacker.getStatusEffect(BardsEffects.ARMYS_PAEON);
        if (effect == null) return;

        int amplifier = effect.getAmplifier();
        double highestAttr = CustomMethods.getHighestDamageAttribute(livingAttacker);
        float extraDamage = (float)(highestAttr * BardsMod.tweaksConfig.value.armies_paeon_impact_multiplier * (amplifier + 1));

        LivingEntity target = (LivingEntity)(Object)this;
        IS_DEALING_EXTRA_DAMAGE.set(true);
        try {
            target.timeUntilRegen = 0;
            target.damage(target.getWorld().getDamageSources().magic(), extraDamage);
        } finally {
            IS_DEALING_EXTRA_DAMAGE.set(false);
        }
        ParticleHelper.sendBatches(target, NOTE_PARTICLES);
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
