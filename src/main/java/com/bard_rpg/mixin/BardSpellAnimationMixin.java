package com.bard_rpg.mixin;

import com.bard_rpg.tags.BardTags;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.spell_engine.client.animation.AnimatablePlayer;
import net.spell_engine.internals.casting.SpellCast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class BardSpellAnimationMixin {

    @Unique
    private boolean bards_rpg$redirecting = false;

    @Inject(method = "playSpellAnimation", at = @At("HEAD"), cancellable = true, remap = false)
    private void bards_rpg$overrideSpellAnimation(SpellCast.Animation type, String name, float speed, CallbackInfo ci) {
        if (bards_rpg$redirecting || name == null || name.isEmpty()) return;

        PlayerEntity player = (PlayerEntity)(Object) this;
        ItemStack mainHand = player.getMainHandStack();

        String overrideName = null;

        if (mainHand.isIn(BardTags.LYRES)) {
            overrideName = switch (type) {
                case CASTING -> "bards_rpg:lyre_channel";
                case RELEASE -> "bards_rpg:lyre_release";
                default -> null;
            };
        } else if (mainHand.isIn(BardTags.LUTES)) {
            overrideName = switch (type) {
                case CASTING -> "bards_rpg:lute_channel";
                case RELEASE -> "bards_rpg:lute_release";
                default -> null;
            };
        }

        if (overrideName != null) {
            bards_rpg$redirecting = true;
            ((AnimatablePlayer) this).playSpellAnimation(type, overrideName, speed);
            bards_rpg$redirecting = false;
            ci.cancel();
        }
    }
}
