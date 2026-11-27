package com.bards.client;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.util.math.MathHelper;

public final class LuthierArms {
    public static final String RIGHT_ARM = "bards_rpg_right_arm";
    public static final String LEFT_ARM = "bards_rpg_left_arm";
    private static final String CROSSED_ARMS = "arms";

    private LuthierArms() {
    }

    public static void addParts(ModelPartData root) {
        root.addChild(RIGHT_ARM, ModelPartBuilder.create()
                        .uv(44, 22).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F)
                        .uv(42, 38).cuboid(-3.0F, 6.0F, -2.0F, 4.0F, 4.0F, 4.0F),
                ModelTransform.pivot(-5.0F, 2.0F, 0.0F));
        root.addChild(LEFT_ARM, ModelPartBuilder.create()
                        .uv(44, 22).mirrored().cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F)
                        .uv(42, 38).cuboid(-1.0F, 6.0F, -2.0F, 4.0F, 4.0F, 4.0F),
                ModelTransform.pivot(5.0F, 2.0F, 0.0F));
    }

    public static boolean hasParts(ModelPart root) {
        return root.hasChild(RIGHT_ARM) && root.hasChild(LEFT_ARM) && root.hasChild(CROSSED_ARMS);
    }

    public static void setSeparated(ModelPart root, boolean separated) {
        if (!hasParts(root)) return;
        root.getChild(CROSSED_ARMS).visible = !separated;
        root.getChild(RIGHT_ARM).visible = separated;
        root.getChild(LEFT_ARM).visible = separated;
    }

    public static void pose(ModelPart root, boolean holdsInstrument, float limbAngle, float limbDistance) {
        if (!hasParts(root)) return;
        setSeparated(root, holdsInstrument);
        if (!holdsInstrument) return;
        ModelPart rightArm = root.getChild(RIGHT_ARM);
        ModelPart leftArm = root.getChild(LEFT_ARM);
        rightArm.resetTransform();
        leftArm.resetTransform();
        float swing = MathHelper.cos(limbAngle * 0.6662F) * limbDistance;
        rightArm.pitch = -swing * 0.5F - MathHelper.PI / 10.0F;
        leftArm.pitch = swing;
    }

    public static boolean isSeparated(ModelPart root) {
        return hasParts(root) && root.getChild(RIGHT_ARM).visible;
    }
}
