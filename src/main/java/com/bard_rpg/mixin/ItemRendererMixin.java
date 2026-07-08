package com.bard_rpg.mixin;

import com.bard_rpg.tags.BardTags;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static com.bard_rpg.BardsMod.MOD_ID;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @ModifyVariable(method = "renderItem", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel useItemModel(BakedModel value, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (stack.isIn(BardTags.TWO_MODEL_INSTRUMENT)
                && renderMode != ModelTransformationMode.GUI
                && renderMode != ModelTransformationMode.GROUND
                && renderMode != ModelTransformationMode.FIXED) {
            String name = stack.getTranslationKey();
            String name2 = name.replace("item.bards_rpg.", "");
            return ((ItemRendererAccessor) this).bard$getModels().getModelManager()
                    .getModel(new ModelIdentifier(MOD_ID, name2 + "_model", "inventory"));
        }
        return value;
    }
}
