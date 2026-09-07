package com.bards.neoforge.client;

import com.bards.BardsMod;
import com.bards.block.BardBlocks;
import com.bards.client.BardClient;
import com.bards.client.particle.PopupParticle;
import com.bards.content.BardParticles;
import net.minecraft.client.render.RenderLayers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = BardsMod.MOD_ID, value = Dist.CLIENT)
public class NeoForgeClient {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        BardBlocks.cutoutRenderLayerRegistrar = RenderLayers::setRenderLayer;
        BardClient.init();
    }

    @SubscribeEvent
    public static void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpecial(BardParticles.SPELL_STOLEN_POPUP, new PopupParticle.Factory());
    }
}
