package com.bards.worldgen.villages;

import com.bards.block.BardBlocks;
import com.bards.content.BardsSounds;
import com.bards.item.Armors;
import com.bards.item.Weapons;
import com.google.common.collect.ImmutableSet;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Schedule;
import net.minecraft.entity.ai.brain.ScheduleBuilder;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import static com.bards.BardsMod.MOD_ID;

public class BardVillagers {
    public static final String LUTHIER = "luthier";
    public static final Identifier PROFESSION_ID  = Identifier.of(MOD_ID, LUTHIER);
    @Nullable
    public static VillagerProfession LUTHIER_PROFESSION;
    public static final int POI_TICKET_COUNT = 1;
    public static final int POI_SEARCH_DISTANCE = 10;
    public static final Schedule LUTHIER_SCHEDULE = new Schedule();

    public static Set<BlockState> poiBlockStates() {
        return ImmutableSet.copyOf(BardBlocks.MUSIC_STAND.block().getStateManager().getStates());
    }

    public static VillagerProfession PROFESSION;

    public static final LinkedHashMap<Integer, List<TradeOffers.Factory>> TRADES = new LinkedHashMap<>();

    public static VillagerProfession registerProfession(String name, RegistryKey<PointOfInterestType> workStation) {
        var id = Identifier.of(MOD_ID, name);
        return Registry.register(Registries.VILLAGER_PROFESSION, Identifier.of(MOD_ID, name), new VillagerProfession(
                id.toString(),
                (entry) -> {
                    return entry.matchesKey(workStation);
                },
                (entry) -> {
                    return entry.matchesKey(workStation);
                },
                ImmutableSet.of(),
                ImmutableSet.of(),
                BardsSounds.lute_hit.soundEvent())
        );
    }

    public static void registerVillagers() {
        PROFESSION = registerProfession(
                LUTHIER,
                RegistryKey.of(Registries.POINT_OF_INTEREST_TYPE.getKey(), PROFESSION_ID));
        TRADES.clear();
        TRADES.put(1, List.of(
                new TradeOffers.BuyItemFactory(Items.STRING, 8, 12, 4, 5),
                new TradeOffers.SellItemFactory(Items.ARROW, 2, 8, 128, 3, 0.01f)
        ));
        TRADES.put(2, List.of(
                new TradeOffers.BuyItemFactory(Items.GOLD_INGOT, 12, 12, 5, 8),
                new TradeOffers.SellItemFactory(Weapons.wooden_lute.item(), 12, 1, 12, 10),
                new TradeOffers.SellItemFactory(Weapons.harp_crossbow.item(), 18, 1, 12, 10),
                new TradeOffers.SellItemFactory(Armors.entertainerArmorSet.armorSet().head, 15, 1, 12, 13)
        ));
        TRADES.put(3, List.of(
                new TradeOffers.SellItemFactory(Weapons.iron_rapier.item(), 14, 1, 12, 15),
                new TradeOffers.SellItemFactory(Weapons.golden_lyre.item(), 18, 1, 12, 15),
                new TradeOffers.SellItemFactory(Armors.entertainerArmorSet.armorSet().feet, 15, 1, 12, 15),
                new TradeOffers.SellItemFactory(Armors.entertainerArmorSet.armorSet().legs, 15, 1, 12, 15)
        ));
        TRADES.put(4, List.of(
                new TradeOffers.SellItemFactory(Armors.entertainerArmorSet.armorSet().chest, 15, 1, 12, 15),
                new TradeOffers.SellItemFactory(Items.RABBIT_HIDE, 15, 1, 12, 5)
        ));
        TRADES.put(5, List.of(
                (entity, random) -> new TradeOffers.SellEnchantedToolFactory(
                        Weapons.diamond_lute.item(),
                        30,
                        3,
                        30,
                        0F).create(entity, random),
                (entity, random) -> new TradeOffers.SellEnchantedToolFactory(
                        Weapons.diamond_lyre.item(),
                        30,
                        3,
                        30,
                        0F).create(entity, random),
                (entity, random) -> new TradeOffers.SellEnchantedToolFactory(
                        Weapons.diamond_rapier.item(),
                        40,
                        3,
                        30,
                        0F).create(entity, random),
                (entity, random) -> new TradeOffers.SellEnchantedToolFactory(
                        Weapons.diamond_harp_crossbow.item(),
                        40,
                        3,
                        30,
                        0F).create(entity, random)
        ));
    }
    public static void registerSchedule() {
        new ScheduleBuilder(LUTHIER_SCHEDULE)
                .withActivity(10,    Activity.IDLE)
                .withActivity(2000,  Activity.WORK)
                .withActivity(9000,  Activity.MEET)
                .withActivity(11000, Activity.IDLE)
                .withActivity(12000, Activity.WORK)
                .withActivity(14000, Activity.REST)
                .build();
        Registry.register(Registries.SCHEDULE, Identifier.of(MOD_ID, "luthier"), LUTHIER_SCHEDULE);
    }
}
