package com.bards.client;

import com.bards.BardsMod;
import com.bards.block.BardBlocks;
import com.bards.client.armor.CustomArmorRenderer;
import com.bards.client.effect.ArmysPaeonCircleRenderer;
import com.bards.client.effect.WardensPaeanEffectRenderer;
import com.bards.client.particle.PopupParticle;
import com.bards.content.BardParticles;
import com.bards.content.BardsSpells;
import com.bards.effect.BardsEffects;
import com.bards.item.Armors;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import mod.azure.azurelibarmor.common.render.armor.AzArmorRenderer;
import mod.azure.azurelibarmor.common.render.armor.AzArmorRendererRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.more_rpg_classes.custom.SpellBuilderHelper;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.api.render.BuffParticleSpawner;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.render.StunParticleSpawner;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_engine.rpg_series.item.Armor;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class BardClient {
    private static final Identifier wardensPaeanHelpfulModelId = Identifier.of(BardsMod.MOD_ID, "spell_effect/warens_paean_helpful");
    private static final Identifier wardensPaeanHarmfulModelId = Identifier.of(BardsMod.MOD_ID, "spell_effect/warens_paean_harmful");

    public static void init() {
        BardsSpells.registerTooltipTokens();

        BardBlocks.registerClient();

        registerArmorRenderer(Armors.entertainerArmorSet.armorSet(), CustomArmorRenderer::entertainer_armor);
        registerArmorRenderer(Armors.troubadourArmorSet.armorSet(), CustomArmorRenderer::troubadour_armor);
        registerArmorRenderer(Armors.netheriteTroubadourArmorSet.armorSet(), CustomArmorRenderer::netherite_troubadour_armor);
        if (FabricLoader.getInstance().isModLoaded("armory_rpgs") || BardsMod.tweaksConfig.value.ignore_items_required_mods) {
            registerArmorRenderer(Armors.storytellerArmorSet.armorSet(), CustomArmorRenderer::storyteller_armor);
        }
        registerEffectParticles();
        CustomModelStatusEffect.register(BardsEffects.ARMYS_PAEON_STASH.effect, new ArmysPaeonCircleRenderer());
        CustomModelStatusEffect.register(BardsEffects.BENEFICIAL_WARDENS_PAEAN.effect, new WardensPaeanEffectRenderer(wardensPaeanHelpfulModelId, false));
        CustomModelStatusEffect.register(BardsEffects.HARMFUL_WARDENS_PAEAN.effect, new WardensPaeanEffectRenderer(wardensPaeanHarmfulModelId, true));
        ParticleFactoryRegistry.getInstance().register(BardParticles.SPELL_STOLEN_POPUP, new PopupParticle.Factory());
    }
    private static void registerArmorRenderer(Armor.Set set, Supplier<AzArmorRenderer> armorRendererSupplier) {
        AzArmorRendererRegistry.register(armorRendererSupplier, set.head, set.chest, set.legs, set.feet);
    }
    private static void registerEffectParticles() {
        CustomParticleStatusEffect.register(
                BardsEffects.CRESCENDO.effect,
                new StunParticleSpawner()
        );
        CustomParticleStatusEffect.register(
                BardsEffects.ECLIPSE_MANTLE.effect,
                new BuffParticleSpawner(eclipseMantleParticles())
                        .invertFrequency().withFrequency(20).scaleWithAmplifier(false)
        );
        CustomParticleStatusEffect.register(
                BardsEffects.HYMN_OF_THE_GOLDEN_LIGHT.effect,
                new BuffParticleSpawner(hymnOfTheGoldenLightParticles())
                        .withFrequency(20).scaleWithAmplifier(false)
        );

    }

    private static ParticleGroup eclipseMantleParticles() {
        var group = BuffParticleSpawner.defaultBatch(
                SpellEngineParticles.area_circle_1.id().toString(),
                1,
                SpellBuilderHelper.MAGENTA.toRGBA());
        group.appearance.attachment(ParticleGroup.Attachment.POSITION);
        return group;
    }

    private static ParticleGroup hymnOfTheGoldenLightParticles() {
        var group = BuffParticleSpawner.defaultBatch(
                SpellEngineParticles.area_circle_1.id().toString(),
                1,
                SpellBuilderHelper.GOLD.toRGBA());
        group.appearance.attachment(ParticleGroup.Attachment.POSITION);
        return group;
    }
}
