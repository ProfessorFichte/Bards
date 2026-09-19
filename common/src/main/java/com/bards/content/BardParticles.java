package com.bards.content;

import com.bards.BardsMod;
import com.mojang.serialization.Codec;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public class BardParticles {
    public static ParticleType<PopupParticleEffect> SPELL_STOLEN_POPUP;

    public static void register() {
        particlesToRegister().forEach((id, type) -> Registry.register(Registries.PARTICLE_TYPE, id, type));
    }

    /// Creation only - Forge registers through the helper `RegisterEvent` hands out and iterates this
    /// instead of calling {@link #register}.
    public static Map<Identifier, ParticleType<?>> particlesToRegister() {
        if (SPELL_STOLEN_POPUP == null) {
            // 1.20.1 particle types take the deserializer factory in the constructor and expose a
            // plain `Codec` (no `PacketCodec`); `ParticleEffect` itself carries the buffer writer.
            SPELL_STOLEN_POPUP = new ParticleType<PopupParticleEffect>(false, PopupParticleEffect.FACTORY) {
                @Override
                public Codec<PopupParticleEffect> getCodec() {
                    return PopupParticleEffect.createCodec(this);
                }
            };
        }
        var toRegister = new LinkedHashMap<Identifier, ParticleType<?>>();
        var id = BardsMod.id("spell_stolen_popup");
        if (!Registries.PARTICLE_TYPE.containsId(id)) {
            toRegister.put(id, SPELL_STOLEN_POPUP);
        }
        return toRegister;
    }
}
