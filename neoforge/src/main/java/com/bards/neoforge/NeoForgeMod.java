package com.bards.neoforge;

import com.bards.neoforge.platform.NeoForgePlatform;
import com.bards.platform.Platform;
import com.bards.worldgen.villages.BardVillagers;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.poi.PointOfInterestType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

import com.bards.BardsMod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(BardsMod.MOD_ID)
public final class NeoForgeMod {
    public NeoForgeMod(IEventBus modBus) {
        Platform.set(new NeoForgePlatform());
        BardsMod.init();
        modBus.addListener(RegisterEvent.class, NeoForgeMod::register);
        modBus.addListener(BuildCreativeModeTabContentsEvent.class, NeoForgePlatform::dispatchItemGroup);
        NeoForge.EVENT_BUS.addListener(VillagerTradesEvent.class, NeoForgeMod::onVillagerTrades);
    }
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
            BardVillagers.registerVillagers();
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
