package com.bard_rpg;

import com.bard_rpg.block.BardBlocks;
import com.bard_rpg.client.SpellDescriptionMutators;
import com.bard_rpg.client.effect.ArmysPaeonCircleRenderer;
import com.bard_rpg.client.effect.ArmysPaeonNoteRenderer;
import com.bard_rpg.effect.BardsEffects;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.render.CustomModels;

import java.util.List;

import static com.bard_rpg.BardsMod.MOD_ID;

@Environment(EnvType.CLIENT)
public class BardClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CustomModels.registerModelIds(List.of(
                ArmysPaeonNoteRenderer.modelIdNote,
                ArmysPaeonCircleRenderer.modelIdCircle,
                new Identifier(MOD_ID, "projectile/crescendo"),
                new Identifier(MOD_ID, "projectile/magical_ballad"),
                new ModelIdentifier(MOD_ID, "wooden_lute_model", "inventory"),
                new ModelIdentifier(MOD_ID, "diamond_lute_model", "inventory"),
                new ModelIdentifier(MOD_ID, "netherite_lute_model", "inventory"),
                new ModelIdentifier(MOD_ID, "ruby_lute_model", "inventory"),
                new ModelIdentifier(MOD_ID, "golden_lyre_model", "inventory"),
                new ModelIdentifier(MOD_ID, "diamond_lyre_model", "inventory"),
                new ModelIdentifier(MOD_ID, "netherite_lyre_model", "inventory"),
                new ModelIdentifier(MOD_ID, "aeternium_lyre_model", "inventory")
        ));

        CustomModelStatusEffect.register(BardsEffects.ARMYS_PAEON_STASH, new ArmysPaeonCircleRenderer());
        BardBlocks.registerClient();
        SpellDescriptionMutators.register();
    }
}
