package com.bards.forge;

import com.bards.forge.platform.ForgePlatform;
import com.bards.platform.Platform;
import com.bards.worldgen.villages.BardVillagers;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;

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

    /// One registry per `RegisterEvent` window - Forge keeps every other registry locked while a window is open.
    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.BLOCK, reg -> {
            BardsMod.registerBlocks();
        });
        event.register(RegistryKeys.SOUND_EVENT, reg -> {
            BardsMod.registerSounds();
        });
        event.register(RegistryKeys.ITEM, reg -> {
            BardsMod.registerItems();
        });
        event.register(RegistryKeys.STATUS_EFFECT, reg -> {
            BardsMod.registerEffects();
        });
        event.register(RegistryKeys.PARTICLE_TYPE, reg -> {
            BardsMod.registerParticles();
        });
        event.register(RegistryKeys.POINT_OF_INTEREST_TYPE, reg -> {
            try {
                Registry.register(Registries.POINT_OF_INTEREST_TYPE, BardVillagers.PROFESSION_ID,
                        new PointOfInterestType(BardVillagers.poiBlockStates(),
                                BardVillagers.POI_TICKET_COUNT, BardVillagers.POI_SEARCH_DISTANCE));
            } catch (Exception e) { }
        });
        event.register(RegistryKeys.VILLAGER_PROFESSION, reg -> {
            BardsMod.registerVillagers();
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
