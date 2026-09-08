package com.bards.content;

import com.bards.BardsMod;
import com.mojang.serialization.Codec;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class BardParticles {
    public static ParticleType<PopupParticleEffect> SPELL_STOLEN_POPUP;

    public static void register() {
        SPELL_STOLEN_POPUP = Registry.register(
            Registries.PARTICLE_TYPE,
            BardsMod.id("spell_stolen_popup"),
            // 1.20.1 particle types take the deserializer factory in the constructor and expose a
            // plain `Codec` (no `PacketCodec`); `ParticleEffect` itself carries the buffer writer.
            new ParticleType<PopupParticleEffect>(false, PopupParticleEffect.FACTORY) {
                @Override
                public Codec<PopupParticleEffect> getCodec() {
                    return PopupParticleEffect.createCodec(this);
                }
            }
        );
    }
}
