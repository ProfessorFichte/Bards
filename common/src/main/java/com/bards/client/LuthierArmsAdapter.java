package com.bards.client;

import com.mrpg_lib.compat.player_animator.client.MobModelAdapter;
import com.mrpg_lib.compat.player_animator.client.MobModelParts;
import com.mrpg_lib.compat.player_animator.client.VillagerModelAdapter;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.VillagerResemblingModel;
import org.jetbrains.annotations.Nullable;

public final class LuthierArmsAdapter implements MobModelAdapter {
    private final VillagerModelAdapter crossedArms = new VillagerModelAdapter();

    @Override
    @Nullable
    public MobModelParts parts(EntityModel<?> model) {
        if (!(model instanceof VillagerResemblingModel<?> villager) || !LuthierArms.hasParts(villager.getPart())) return null;
        ModelPart root = villager.getPart();
        return new MobModelParts(
                villager.getHead(),
                root.getChild("body"),
                root.getChild(LuthierArms.LEFT_ARM),
                root.getChild(LuthierArms.RIGHT_ARM),
                root.getChild("left_leg"),
                root.getChild("right_leg"));
    }

    @Override
    public void prepare(EntityModel<?> model) {
        if (separated(model)) {
            LuthierArms.setSeparated(((VillagerResemblingModel<?>) model).getPart(), true);
        } else {
            crossedArms.prepare(model);
        }
    }

    @Override
    public void finish(EntityModel<?> model) {
        if (!separated(model)) crossedArms.finish(model);
    }

    private static boolean separated(EntityModel<?> model) {
        return model instanceof LuthierArmsModel arms && arms.bards_rpg$holdsInstrument();
    }
}
