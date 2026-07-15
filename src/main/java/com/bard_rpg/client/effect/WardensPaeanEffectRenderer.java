package com.bard_rpg.client.effect;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.render.CustomLayers;
import net.spell_engine.api.render.CustomModels;
import net.spell_engine.api.render.LightEmission;

public class WardensPaeanEffectRenderer implements CustomModelStatusEffect.Renderer {
    private static final RenderLayer RENDER_LAYER = CustomLayers.spellEffect(LightEmission.RADIATE, true);

    // Model is authored around a player-sized hitbox; other entities (e.g. the Warden) scale relative to it.
    private static final float REFERENCE_WIDTH = 0.6F;
    private static final float REFERENCE_HEIGHT = 1.8F;
    private static final float SPIN_SPEED = 1.2F;
    private static final float EXTRA_SCALE = 1.25F;
    // The model sits slightly into the ground at its authored origin, so lift it clear of the feet.
    private static final float LIFT_HEIGHT = 0.5F;

    private final Identifier modelId;
    private final float spinDirection;

    public WardensPaeanEffectRenderer(Identifier modelId, boolean reverseSpin) {
        this.modelId = modelId;
        this.spinDirection = reverseSpin ? -1F : 1F;
    }

    @Override
    public void renderEffect(int amplifier, LivingEntity livingEntity, float delta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light) {
        var itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        float time = livingEntity.age + delta;

        float widthScale = (livingEntity.getWidth() / REFERENCE_WIDTH) * EXTRA_SCALE;
        float heightScale = (livingEntity.getHeight() / REFERENCE_HEIGHT) * EXTRA_SCALE;

        matrixStack.push();
        matrixStack.translate(0.0F, LIFT_HEIGHT * heightScale, 0.0F);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(time * SPIN_SPEED * spinDirection));
        matrixStack.scale(widthScale, heightScale, widthScale);
        CustomModels.render(RENDER_LAYER, itemRenderer, modelId, matrixStack, vertexConsumers, light, livingEntity.getId());
        matrixStack.pop();
    }
}
