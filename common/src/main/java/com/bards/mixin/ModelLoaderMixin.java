package com.bards.mixin;

import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import static com.bards.BardsMod.MOD_ID;

@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin {
    @Shadow
    protected abstract void addModel(ModelIdentifier id);
    @Unique
    private boolean bardsModelsAdded;

    @Inject(method = "addModel", at = @At("TAIL"))
    private void addItemModel(ModelIdentifier id, CallbackInfo ci) {
        if (this.bardsModelsAdded || !ModelLoader.MISSING_ID.equals(id)) {
            return;
        }
        this.bardsModelsAdded = true;
        ///LUTES
        this.addModel(new ModelIdentifier(Identifier.of(MOD_ID, "wooden_lute_model"), "inventory"));
        this.addModel(new ModelIdentifier(Identifier.of(MOD_ID, "diamond_lute_model"), "inventory"));
        this.addModel(new ModelIdentifier(Identifier.of(MOD_ID, "netherite_lute_model"), "inventory"));
        this.addModel(new ModelIdentifier(Identifier.of(MOD_ID, "ruby_lute_model"), "inventory"));
        this.addModel(new ModelIdentifier(Identifier.of(MOD_ID, "aether_lute_model"), "inventory"));
        this.addModel(new ModelIdentifier(Identifier.of(MOD_ID, "unique_lute_0_model"), "inventory"));
        this.addModel(new ModelIdentifier(Identifier.of(MOD_ID, "unique_lute_1_model"), "inventory"));
        this.addModel(new ModelIdentifier(Identifier.of(MOD_ID, "ender_dragon_lute_model"), "inventory"));
        ///LYRES
        this.addModel(new ModelIdentifier(Identifier.of(MOD_ID, "golden_lyre_model"), "inventory"));
        this.addModel(new ModelIdentifier(Identifier.of(MOD_ID, "diamond_lyre_model"), "inventory"));
        this.addModel(new ModelIdentifier(Identifier.of(MOD_ID, "netherite_lyre_model"), "inventory"));
        this.addModel(new ModelIdentifier(Identifier.of(MOD_ID, "aeternium_lyre_model"), "inventory"));
        this.addModel(new ModelIdentifier(Identifier.of(MOD_ID, "aether_lyre_model"), "inventory"));
        this.addModel(new ModelIdentifier(Identifier.of(MOD_ID, "unique_lyre_0_model"), "inventory"));
        this.addModel(new ModelIdentifier(Identifier.of(MOD_ID, "unique_lyre_1_model"), "inventory"));
        this.addModel(new ModelIdentifier(Identifier.of(MOD_ID, "elder_guardian_lyre_model"), "inventory"));
    }
}

