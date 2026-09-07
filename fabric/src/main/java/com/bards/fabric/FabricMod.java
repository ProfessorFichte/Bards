package com.bards.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;

import com.bards.BardsMod;
import com.bards.fabric.platform.FabricPlatform;
import com.bards.platform.Platform;
import com.bards.worldgen.villages.BardVillagers;

public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        Platform.set(new FabricPlatform());

        BardsMod.init();
        BardsMod.registerBlocks();
        BardsMod.registerEffects();
        BardsMod.registerItems();
        BardsMod.registerSounds();
        BardsMod.registerParticles();

        PointOfInterestHelper.register(BardVillagers.PROFESSION_ID,
                BardVillagers.POI_TICKET_COUNT, BardVillagers.POI_SEARCH_DISTANCE,
                BardVillagers.poiBlockStates());
        BardsMod.registerVillagers();
        BardVillagers.TRADES.forEach((tier, factories) ->
                TradeOfferHelper.registerVillagerOffers(BardVillagers.PROFESSION, tier,
                        list -> list.addAll(factories)));
    }
}
