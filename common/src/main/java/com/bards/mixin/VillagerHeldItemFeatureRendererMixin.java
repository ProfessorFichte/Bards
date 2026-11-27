package com.bards.mixin;

import com.bards.client.LuthierInstrumentRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.VillagerHeldItemFeatureRenderer;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerHeldItemFeatureRenderer.class)
public abstract class VillagerHeldItemFeatureRendererMixin {
    @Shadow @Final private HeldItemRenderer heldItemRenderer;

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V",
            at = @At("HEAD"), cancellable = true)
    private void bards_rpg$renderPlayedInstrument(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity,
                                                  float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch,
                                                  CallbackInfo ci) {
        if (LuthierInstrumentRenderer.render(entity, ((FeatureRenderer<?, ?>) (Object) this).getContextModel(), heldItemRenderer,
                matrices, vertexConsumers, light)) {
            ci.cancel();
        }
    }
}
