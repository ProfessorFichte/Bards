package com.bards.client.effect;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.render.CustomLayers;
import net.spell_engine.api.render.CustomModels;
import net.spell_engine.api.render.LightEmission;

import static com.bards.BardsMod.MOD_ID;

public class ArmysPaeonCircleRenderer implements CustomModelStatusEffect.Renderer {
    private static final RenderLayer RENDER_LAYER = CustomLayers.spellEffect(LightEmission.RADIATE, false);
    public static final Identifier modelIdCircle = Identifier.of(MOD_ID, "spell_effect/armys_paeon_circle");
    private static final float CIRCLE_MODEL_SCALE = 3.0F;
    private static final float NOTE_MODEL_SCALE = 0.75F;

    private static final float SPIN_SPEED = 2.0F;
    private static final float BOB_SPEED = 0.03F;
    private static final float BOB_AMPLITUDE = 0.15F;
    private static final float BOB_BASE_HEIGHT = 2.0F;

    @Override
    public void renderEffect(long appliedAtWorldTime, int amplifier, LivingEntity livingEntity, float delta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light) {
        var itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        var camera = MinecraftClient.getInstance().gameRenderer.getCamera();

        float time = livingEntity.age + delta;

        float bobOffset = BOB_BASE_HEIGHT + BOB_AMPLITUDE * (float) Math.sin(time * BOB_SPEED * Math.PI * 2.0F);

        matrixStack.push();
        matrixStack.translate(0.0F, bobOffset, 0.0F);

        float spinAngle = time * SPIN_SPEED;
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(spinAngle));

        matrixStack.scale(CIRCLE_MODEL_SCALE, CIRCLE_MODEL_SCALE, CIRCLE_MODEL_SCALE);
        CustomModels.render(RENDER_LAYER, itemRenderer, modelIdCircle, matrixStack, vertexConsumers, light, livingEntity.getId());
        matrixStack.pop();

        // Note Renderer
        var direction = camera.getPos().subtract(livingEntity.getPos()).normalize().multiply(livingEntity.getWidth() * 0.5F);
        float noteTime = time * 0.05F;
        float noteFloatOffset = 1.225F + 0.325F * (float) Math.sin(noteTime);

        matrixStack.push();
        matrixStack.translate(direction.x, direction.y + livingEntity.getHeight() + noteFloatOffset, direction.z + -0.15F);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180F + (float) Math.toDegrees(Math.atan2(direction.x, direction.z))));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrixStack.scale(NOTE_MODEL_SCALE, NOTE_MODEL_SCALE, NOTE_MODEL_SCALE);
        CustomModels.render(RENDER_LAYER, itemRenderer, ArmysPaeonNoteRenderer.modelIdNote, matrixStack, vertexConsumers, light, livingEntity.getId());
        matrixStack.pop();
    }
}
