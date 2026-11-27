package com.bards.client;

import com.mrpg_lib.compat.player_animator.api.MobAnimations;
import com.mrpg_lib.compat.player_animator.client.MobAnimationRenderHook;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.VillagerResemblingModel;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.RotationAxis;

public final class LuthierInstrumentRenderer {
    private LuthierInstrumentRenderer() {
    }

    public static boolean render(LivingEntity entity, EntityModel<?> model, HeldItemRenderer heldItemRenderer,
                                 MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (!(model instanceof VillagerResemblingModel<?> villagerModel)) return false;
        ModelPart root = villagerModel.getPart();
        ItemStack stack = entity.getMainHandStack();
        if (stack.isEmpty() || !LuthierArms.isSeparated(root)) return false;

        matrices.push();
        root.getChild(LuthierArms.RIGHT_ARM).rotate(matrices);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
        matrices.translate(1.0F / 16.0F, 0.125F, -0.625F);
        if (MobAnimations.isAvailable()) {
            MobAnimationRenderHook.applyHeldItemTransform(entity, Arm.RIGHT, matrices);
        }
        heldItemRenderer.renderItem(entity, stack, ModelTransformationMode.THIRD_PERSON_RIGHT_HAND, false, matrices, vertexConsumers, light);
        matrices.pop();
        return true;
    }
}
