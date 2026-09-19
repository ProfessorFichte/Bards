package com.bards.mixin;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

import static com.bards.BardsMod.MOD_ID;

@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin {
    // 1.20.1 has no `loadItemModel`; the constructor queues models through the private `addModel`.
    // Direct `<init>` call sites, in order: MISSING_ID (0), the item loop (1), TRIDENT_IN_HAND (2),
    // SPYGLASS_IN_HAND (3) - the calls inside `forEach` lambdas live in synthetic methods and are not
    // counted. Injecting after ordinal 3 lands in the same place as 1.21's `loadItemModel` ordinal 1:
    // after the special models, before `setParents` resolves the queue.
    @Shadow
    protected abstract void addModel(ModelIdentifier id);
    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/ModelLoader;addModel(Lnet/minecraft/client/util/ModelIdentifier;)V", ordinal = 3, shift = At.Shift.AFTER))
    public void addItemModel(BlockColors blockColors, Profiler profiler, Map<Identifier, JsonUnbakedModel> jsonUnbakedModels, Map<Identifier, List<ModelLoader.SourceTrackedData>> blockStates, CallbackInfo ci) {
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

