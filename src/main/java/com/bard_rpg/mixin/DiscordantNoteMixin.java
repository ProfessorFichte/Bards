package com.bard_rpg.mixin;

import com.bard_rpg.BardsMod;
import com.bard_rpg.effect.BardsEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class DiscordantNoteMixin {

    @Inject(method = "damage", at = @At("HEAD"))
    private void discordantNote$selfDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Entity attacker = source.getAttacker();
        if (!(attacker instanceof LivingEntity livingAttacker)) return;
        if (livingAttacker.getWorld().isClient()) return;

        var effectInstance = livingAttacker.getStatusEffect(BardsEffects.DISCORDANT_NOTE);
        if (effectInstance == null) return;

        int amplifier = effectInstance.getAmplifier();
        float chance = BardsMod.tweaksConfig.value.discordant_selfdamage_chance_per_stack * (amplifier + 1);
        if (livingAttacker.getRandom().nextFloat() >= chance) return;

        float selfDamage = amount * BardsMod.tweaksConfig.value.discordant_selfdamage_multiplier_per_stack * (amplifier + 1);
        livingAttacker.damage(livingAttacker.getWorld().getDamageSources().magic(), selfDamage);
    }
}
