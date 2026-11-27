package com.bards.mixin;

import com.bards.client.LuthierArms;
import com.bards.client.LuthierArmsModel;
import com.bards.tags.BardTags;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.VillagerResemblingModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerResemblingModel.class)
public abstract class VillagerResemblingModelMixin implements LuthierArmsModel {
    @Shadow @Final private ModelPart root;
    @Unique private boolean bards_rpg$holdsInstrument;

    @Inject(method = "getModelData", at = @At("RETURN"))
    private static void bards_rpg$addArms(CallbackInfoReturnable<ModelData> cir) {
        LuthierArms.addParts(cir.getReturnValue().getRoot());
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void bards_rpg$hideArms(ModelPart root, CallbackInfo ci) {
        LuthierArms.setSeparated(root, false);
    }

    @Inject(method = "setAngles", at = @At("TAIL"))
    private void bards_rpg$poseArms(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, CallbackInfo ci) {
        bards_rpg$holdsInstrument = entity instanceof LivingEntity living && living.getMainHandStack().isIn(BardTags.TWO_MODEL_INSTRUMENT);
        LuthierArms.pose(root, bards_rpg$holdsInstrument, limbAngle, limbDistance);
    }

    @Override
    public boolean bards_rpg$holdsInstrument() {
        return bards_rpg$holdsInstrument;
    }
}
