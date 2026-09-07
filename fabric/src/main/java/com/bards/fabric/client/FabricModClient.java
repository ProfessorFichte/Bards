package com.bards.fabric.client;

import com.bards.block.BardBlocks;
import com.bards.client.BardClient;
import com.bards.client.particle.PopupParticle;
import com.bards.content.BardParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public final class FabricModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BardBlocks.cutoutRenderLayerRegistrar = BlockRenderLayerMap.INSTANCE::putBlock;
        BardClient.init();
        ParticleFactoryRegistry.getInstance().register(BardParticles.SPELL_STOLEN_POPUP, new PopupParticle.Factory());
    }
}
