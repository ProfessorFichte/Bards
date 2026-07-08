package com.bard_rpg.worldgen.villages;

import com.bard_rpg.item.Armors;
import com.bard_rpg.item.Weapons;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Schedule;
import net.minecraft.entity.ai.brain.ScheduleBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;

import java.util.LinkedHashMap;
import java.util.List;

import static com.bard_rpg.BardsMod.MOD_ID;

public class BardVillagerTrades {

    public static final Schedule LUTHIER_SCHEDULE = new Schedule();

    public static void registerSchedule() {
        new ScheduleBuilder(LUTHIER_SCHEDULE)
                .withActivity(10, Activity.IDLE)
                .withActivity(2000, Activity.WORK)
                .withActivity(9000, Activity.MEET)
                .withActivity(11000, Activity.IDLE)
                .withActivity(12000, Activity.WORK)
                .withActivity(14000, Activity.REST)
                .build();
        Registry.register(Registries.SCHEDULE, new Identifier(MOD_ID, "luthier"), LUTHIER_SCHEDULE);
    }

    private static TradeOffers.Factory buyFactory(Item item, int price, int maxUses, int experience, float priceMultiplier) {
        return (entity, random) -> new TradeOffer(
                new ItemStack(item, price),
                new ItemStack(Items.EMERALD, 1),
                maxUses, experience, priceMultiplier
        );
    }

    public static void registerTrades() {
        VillagerProfession profession = BardVillagerProfessions.LUTHIER;

        LinkedHashMap<Integer, List<TradeOffers.Factory>> trades = new LinkedHashMap<>();
        trades.put(1, List.of(
                buyFactory(Items.STRING, 8, 12, 4, 0.05f),
                new TradeOffers.SellItemFactory(new ItemStack(Items.ARROW), 2, 8, 128, 3, 0.01f)
        ));
        trades.put(2, List.of(
                buyFactory(Items.GOLD_INGOT, 12, 12, 5, 0.05f),
                new TradeOffers.SellItemFactory(new ItemStack(Weapons.wooden_lute.item()), 12, 1, 12, 10, 0.05f),
                new TradeOffers.SellItemFactory(new ItemStack(Weapons.harp_crossbow.item()), 18, 1, 12, 10, 0.05f),
                new TradeOffers.SellItemFactory(new ItemStack(Armors.entertainerArmorSet.head.asItem()), 15, 1, 12, 13, 0.05f)
        ));
        trades.put(3, List.of(
                new TradeOffers.SellItemFactory(new ItemStack(Weapons.iron_rapier.item()), 14, 1, 12, 15, 0.05f),
                new TradeOffers.SellItemFactory(new ItemStack(Weapons.golden_lyre.item()), 18, 1, 12, 15, 0.05f),
                new TradeOffers.SellItemFactory(new ItemStack(Armors.entertainerArmorSet.feet.asItem()), 15, 1, 12, 15, 0.05f),
                new TradeOffers.SellItemFactory(new ItemStack(Armors.entertainerArmorSet.legs.asItem()), 15, 1, 12, 15, 0.05f)
        ));
        trades.put(4, List.of(
                new TradeOffers.SellItemFactory(new ItemStack(Armors.entertainerArmorSet.chest.asItem()), 15, 1, 12, 15, 0.05f),
                new TradeOffers.SellItemFactory(new ItemStack(Items.RABBIT_HIDE), 15, 1, 12, 5, 0.05f)
        ));

        for (var entry : trades.entrySet()) {
            TradeOfferHelper.registerVillagerOffers(profession, entry.getKey(), factories -> {
                factories.addAll(entry.getValue());
            });
        }

        TradeOfferHelper.registerVillagerOffers(profession, 5, factories -> {
            factories.add(new TradeOffers.SellEnchantedToolFactory(Weapons.diamond_lute.item(), 30, 3, 30, 0F));
            factories.add(new TradeOffers.SellEnchantedToolFactory(Weapons.diamond_lyre.item(), 30, 3, 30, 0F));
            factories.add(new TradeOffers.SellEnchantedToolFactory(Weapons.diamond_rapier.item(), 40, 3, 30, 0F));
            factories.add(new TradeOffers.SellEnchantedToolFactory((Item) Weapons.diamond_harp_crossbow.item(), 40, 3, 30, 0F));
        });
    }
}
