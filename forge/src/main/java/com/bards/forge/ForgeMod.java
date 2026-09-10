package com.bards.forge;

import com.bards.block.BardBlocks;
import com.bards.content.BardParticles;
import com.bards.content.BardsSounds;
import com.bards.effect.BardsEffects;
import com.bards.forge.platform.ForgePlatform;
import com.bards.item.Group;
import com.bards.platform.Platform;
import com.bards.worldgen.villages.BardVillagers;
import net.minecraft.registry.RegistryKeys;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;
import net.spell_engine.api.effect.Effects;

import com.bards.BardsMod;

@Mod(BardsMod.MOD_ID)
public final class ForgeMod {
    @SuppressWarnings("removal")
    public ForgeMod() {
        Platform.set(new ForgePlatform());
        BardsMod.init();
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(EventPriority.NORMAL, false, RegisterEvent.class, ForgeMod::register);
        modBus.addListener(EventPriority.NORMAL, false, BuildCreativeModeTabContentsEvent.class, ForgePlatform::dispatchItemGroup);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, VillagerTradesEvent.class, ForgeMod::onVillagerTrades);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            com.bards.forge.client.ForgeClient.register(modBus);
        }
    }

    /// Forge only clears the vanilla `NamespacedWrapper`'s lock from 47.4.0 onwards, so on Forge
    /// 47.0-47.3 (and NeoForge 1.20.1) `Registry.register` throws "Can not register to a locked
    /// registry" even inside the correct `RegisterEvent` window. Everything here therefore writes
    /// through the `RegisterHelper` the event hands out.
    ///
    /// The loops below duplicate what `common` runs on Fabric, on purpose: the workaround stays inside
    /// `forge/` and the Fabric path is untouched. Each block is declared unconditionally - Forge posts
    /// one event per registry and `event.register` is a no-op unless its key matches.
    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.SOUND_EVENT, helper -> {
            BardsSounds.soundsToRegister().forEach(helper::register);
            // `LuthierSongs` reads `Entry#entry()`, which only the register-reference path fills in.
            BardsSounds.linkEntries();
        });

        event.register(RegistryKeys.BLOCK, helper -> {
            BardBlocks.blocksToRegister().forEach(helper::register);
        });

        event.register(RegistryKeys.STATUS_EFFECT, helper -> {
            BardsMod.effectsToRegister().forEach(helper::register);
            Effects.linkEntries(BardsEffects.entries);
            BardsMod.effectsConfig.save();
        });

        event.register(RegistryKeys.PARTICLE_TYPE, helper -> {
            BardParticles.particlesToRegister().forEach(helper::register);
        });

        event.register(RegistryKeys.ITEM, helper -> {
            BardsMod.itemsToRegister().forEach(helper::register);
            BardsMod.itemConfig.save();
        });

        // `creative_mode_tab` is `RegisterEvent` 65 while `item` is 7 - registering the group from the
        // ITEM pass would write into a registry whose event has not fired yet.
        event.register(RegistryKeys.ITEM_GROUP, helper -> {
            BardsMod.createItemGroup();
            helper.register(Group.ID, Group.BARDS);
        });

        event.register(RegistryKeys.VILLAGER_PROFESSION, helper -> {
            BardVillagers.professionsToRegister().forEach(helper::register);
            // The `VillagerTradesEvent` listener below reads `BardVillagers.TRADES`.
            BardVillagers.buildTrades();
        });

        event.register(RegistryKeys.POINT_OF_INTEREST_TYPE, helper -> {
            helper.register(BardVillagers.PROFESSION_ID, BardVillagers.createPoi());
        });
    }

    private static void onVillagerTrades(VillagerTradesEvent event) {
        if (event.getType() != BardVillagers.PROFESSION) {
            return;
        }
        BardVillagers.TRADES.forEach((tier, factories) -> {
            var tierList = event.getTrades().get(tier.intValue());
            if (tierList != null) {
                tierList.addAll(factories);
            }
        });
    }
}
