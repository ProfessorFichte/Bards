package com.bards.worldgen.villages;

import com.bards.block.BardBlocks;
import com.bards.content.BardsSounds;
import com.bards.item.Armors;
import com.bards.item.Weapons;
import com.google.common.collect.ImmutableSet;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Schedule;
import net.minecraft.entity.ai.brain.ScheduleBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    public static VillagerProfession createProfession(String name, RegistryKey<PointOfInterestType> workStation) {
        var id = Identifier.of(MOD_ID, name);
        return new VillagerProfession(
                id.toString(),
                (entry) -> {
                    return entry.matchesKey(workStation);
                },
                (entry) -> {
                    return entry.matchesKey(workStation);
                },
                ImmutableSet.of(),
                ImmutableSet.of(),
                BardsSounds.lute_hit.soundEvent()
        );
    }

    /// Creation only - Forge registers through the helper `RegisterEvent` hands out and iterates this
    /// instead of calling {@link #registerVillagers}. Follow it with {@link #buildTrades()}: the Forge
    /// `VillagerTradesEvent` listener reads {@link #TRADES}, which `registerVillagers()` used to fill in
    /// right after registering the profession.
    public static Map<Identifier, VillagerProfession> professionsToRegister() {
        PROFESSION = createProfession(
                LUTHIER,
                RegistryKey.of(Registries.POINT_OF_INTEREST_TYPE.getKey(), PROFESSION_ID));
        var toRegister = new LinkedHashMap<Identifier, VillagerProfession>();
        if (!Registries.VILLAGER_PROFESSION.containsId(PROFESSION_ID)) {
            toRegister.put(PROFESSION_ID, PROFESSION);
        }
        return toRegister;
    }

    /// Creation only - see {@link #professionsToRegister()}.
    public static PointOfInterestType createPoi() {
        return new PointOfInterestType(poiBlockStates(), POI_TICKET_COUNT, POI_SEARCH_DISTANCE);
    }

    public static void registerVillagers() {
        professionsToRegister().forEach((id, profession) ->
                Registry.register(Registries.VILLAGER_PROFESSION, id, profession));
        buildTrades();
    }

    public static void buildTrades() {
        TRADES.clear();
        TRADES.put(1, List.of(
                buyForEmeralds(Items.STRING, 8, 12, 4, 5),
                sell(new ItemStack(Items.ARROW), 2, 8, 128, 3, 0.01f)
        ));
        TRADES.put(2, List.of(
                buyForEmeralds(Items.GOLD_INGOT, 12, 12, 5, 8),
                sell(Weapons.wooden_lute.item(), 12, 1, 12, 10),
                sell(Weapons.harp_crossbow.item(), 18, 1, 12, 10),
                sell(Armors.entertainerArmorSet.armorSet().head, 15, 1, 12, 13)
        ));
        TRADES.put(3, List.of(
                sell(Weapons.iron_rapier.item(), 14, 1, 12, 15),
                sell(Weapons.golden_lyre.item(), 18, 1, 12, 15),
                sell(Armors.entertainerArmorSet.armorSet().feet, 15, 1, 12, 15),
                sell(Armors.entertainerArmorSet.armorSet().legs, 15, 1, 12, 15)
        ));
        TRADES.put(4, List.of(
                sell(Armors.entertainerArmorSet.armorSet().chest, 15, 1, 12, 15),
                sell(Items.RABBIT_HIDE, 15, 1, 12, 5)
        ));
        TRADES.put(5, List.of(
                sellEnchanted(Weapons.diamond_lute.item(), 30, 3, 30, 0F),
                sellEnchanted(Weapons.diamond_lyre.item(), 30, 3, 30, 0F),
                sellEnchanted(Weapons.diamond_rapier.item(), 40, 3, 30, 0F),
                sellEnchanted(Weapons.diamond_harp_crossbow.item(), 40, 3, 30, 0F)
        ));
    }
    /// 1.20.1 has no `TradeOffers.BuyItemFactory` (it only ships `BuyForOneEmeraldFactory`), so the
    /// 1.21 offer is rebuilt on the raw `TradeOffer` constructor with the same 0.05 price multiplier.
    private static TradeOffers.Factory buyForEmeralds(net.minecraft.item.Item item, int count, int maxUses, int experience, int price) {
        return (entity, random) -> new TradeOffer(
                new ItemStack(item, count), new ItemStack(Items.EMERALD, price), maxUses, experience, 0.05F);
    }

    /// `TradeOffers.SellItemFactory` and `TradeOffers.SellEnchantedToolFactory` are **package-private** in
    /// vanilla 1.20.1 and stay so after Forge's access transformer. They compile here only because a mod on
    /// `common`'s classpath contributes an access widener the production runtime lacks - at runtime Forge
    /// throws `IllegalAccessError`. Both are rebuilt on the raw `TradeOffer` constructor, reproducing
    /// vanilla's argument order, price cap and multipliers exactly.
    private static TradeOffers.Factory sell(net.minecraft.item.Item item, int price, int count, int maxUses, int experience) {
        return sell(new ItemStack(item), price, count, maxUses, experience, 0.05F);
    }

    private static TradeOffers.Factory sell(ItemStack stack, int price, int count, int maxUses, int experience, float multiplier) {
        return (entity, random) -> new TradeOffer(
                new ItemStack(Items.EMERALD, price), new ItemStack(stack.getItem(), count),
                maxUses, experience, multiplier);
    }

    private static TradeOffers.Factory sellEnchanted(net.minecraft.item.Item item, int basePrice, int maxUses, int experience, float multiplier) {
        return (entity, random) -> {
            int level = 5 + random.nextInt(15);
            var enchanted = EnchantmentHelper.enchant(random, new ItemStack(item), level, false);
            int price = Math.min(basePrice + level, 64);
            return new TradeOffer(new ItemStack(Items.EMERALD, price), enchanted, maxUses, experience, multiplier);
        };
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
