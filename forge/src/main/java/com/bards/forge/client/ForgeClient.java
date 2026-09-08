package com.bards.forge.client;

import com.bards.block.BardBlocks;
import com.bards.client.BardClient;
import com.bards.client.particle.PopupParticle;
import com.bards.content.BardParticles;
import net.minecraft.client.render.RenderLayers;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/// Forge 47 has no `@EventBusSubscriber(value = Dist.CLIENT)` equivalent that is safe to classload on a
/// dedicated server, so the listeners are attached explicitly from `ForgeMod` behind a `Dist.CLIENT` check.
public class ForgeClient {
    public static void register(IEventBus modBus) {
        modBus.addListener(EventPriority.NORMAL, false, FMLClientSetupEvent.class, ForgeClient::onClientSetup);
        modBus.addListener(EventPriority.NORMAL, false, RegisterParticleProvidersEvent.class, ForgeClient::onRegisterParticleProviders);
    }

    public static void onClientSetup(FMLClientSetupEvent event) {
        BardBlocks.cutoutRenderLayerRegistrar = RenderLayers::setRenderLayer;
        BardClient.init();
    }

    public static void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpecial(BardParticles.SPELL_STOLEN_POPUP, new PopupParticle.Factory());
    }
}
