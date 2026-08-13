package com.bard_rpg.datagen;

import com.bard_rpg.item.MusicDiscs;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class MusicDiscTagProvider extends FabricTagProvider.ItemTagProvider {
    public MusicDiscTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        var builder = getOrCreateTagBuilder(ItemTags.MUSIC_DISCS);
        for (var entry : MusicDiscs.all) {
            builder.add(entry.item());
        }
    }
}
