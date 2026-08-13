package com.bard_rpg.worldgen.villages;

import com.bard_rpg.block.BardBlocks;
import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

import static com.bard_rpg.BardsMod.MOD_ID;

public class BardVillagerProfessions {

    public static final RegistryKey<PointOfInterestType> LUTHIER_POI_KEY =
            RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, new Identifier(MOD_ID, "luthier"));

    public static PointOfInterestType LUTHIER_POI;
    public static VillagerProfession LUTHIER;

    public static void registerPoiTypes() {
        LUTHIER_POI = PointOfInterestHelper.register(new Identifier(MOD_ID, "luthier"), 1, 1, BardBlocks.MUSIC_STAND.block());
    }

    public static void registerProfessions() {
        LUTHIER = Registry.register(Registries.VILLAGER_PROFESSION, new Identifier(MOD_ID, "luthier"),
                new VillagerProfession("luthier",
                        entry -> entry.matchesKey(LUTHIER_POI_KEY),
                        entry -> entry.matchesKey(LUTHIER_POI_KEY),
                        ImmutableSet.of(), ImmutableSet.of(),
                        SoundEvents.ENTITY_VILLAGER_WORK_FLETCHER));
    }
}
