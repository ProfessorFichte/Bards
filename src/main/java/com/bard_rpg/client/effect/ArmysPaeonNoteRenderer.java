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

import static com.bard_rpg.BardsMod.MOD_ID;

public class ArmysPaeonNoteRenderer implements CustomModelStatusEffect.Renderer {
    private static final RenderLayer RENDER_LAYER = CustomLayers.spellEffect(LightEmission.RADIATE, false);
    public static final Identifier modelIdNote = new Identifier(MOD_ID, "effect/key_musicnote");
    private static final float MODEL_SCALE = 0.75F;

    @Override
    public void renderEffect(int amplifier, LivingEntity livingEntity, float delta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light) {
        var itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        var camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        var direction = camera.getPos().subtract(livingEntity.getPos()).normalize().multiply(livingEntity.getWidth() * 0.5F);

        float time = (livingEntity.age + delta) * 0.05F;
        float floatOffset = 1.225F + 0.325F * (float) Math.sin(time);

        matrixStack.push();
        matrixStack.translate(direction.x, direction.y + livingEntity.getHeight() + floatOffset, direction.z + -0.15F);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180F + (float) Math.toDegrees(Math.atan2(direction.x, direction.z))));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrixStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
        CustomModels.render(RENDER_LAYER, itemRenderer, modelIdNote, matrixStack, vertexConsumers, light, livingEntity.getId());
        matrixStack.pop();
    }
}
