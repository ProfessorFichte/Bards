package com.bard_rpg.item;

import net.fabric_extras.ranged_weapon.api.CustomRangedWeapon;
import net.fabric_extras.ranged_weapon.api.RangedConfig;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.spell_engine.api.item.ItemConfig;
import net.spell_engine.api.item.weapon.SpellSwordItem;
import net.spell_engine.api.item.weapon.StaffItem;
import net.spell_engine.api.item.weapon.Weapon;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Supplier;

import static com.bard_rpg.BardsMod.MOD_ID;
import static com.bard_rpg.BardsMod.tweaksConfig;

public class Weapons {
    public static final ArrayList<Weapon.Entry> entries = new ArrayList<>();
    public static final ArrayList<RangedEntry> rangedEntries = new ArrayList<>();
    public record RangedEntry(Identifier id, Item item, RangedConfig defaults) { }

    private static final String BETTER_END = "betterend";
    private static final String BETTER_NETHER = "betternether";
    private static final String LNE = "loot_n_explore";

    private static Supplier<Ingredient> ingredient(String idString, boolean modLoaded, Item fallback) {
        var id = new Identifier(idString);
        if (!modLoaded) {
            return () -> Ingredient.ofItems(fallback);
        }
        return () -> {
            var item = Registries.ITEM.get(id);
            return Ingredient.ofItems(item != null ? item : fallback);
        };
    }

    private static Weapon.Entry entry(String name, Weapon.CustomMaterial material, Item item, ItemConfig.Weapon defaults) {
        var e = new Weapon.Entry(MOD_ID, name, material, item, defaults, null);
        entries.add(e);
        return e;
    }

    // Spell power weapon balancing
    private static final float STEP            = 0.5F;
    private static final float BASE_START      = 3.0F;
    private static final float LUTE_ARC_START  = round05(BASE_START);
    private static final float LUTE_HEAL_START = round05(BASE_START);
    private static final float LYRE_ARC_START  = round05(BASE_START * 0.5F);
    private static final float LYRE_HEAL_START = round05(BASE_START * 1.25F);
    private static final float HARP_ARC_START  = round05(BASE_START * 0.75F);
    private static final float HARP_HEAL_START = round05(BASE_START * 0.5F);

    private static float round05(float v) { return Math.round(v * 2) / 2.0F; }
    private static float tier(float start, int idx) {
        return round05(start + STEP * Math.min(idx, 3));
    }

    private static RangedEntry harpCrossbow(String name, int durability, boolean fireproof, Supplier<Ingredient> repair, RangedConfig defaults, int tier) {
        var settings = new Item.Settings().maxDamage(durability);
        if (fireproof) settings = settings.fireproof();
        var item = new HarpCrossbowItem(settings, repair, tier(HARP_ARC_START, tier), tier(HARP_HEAL_START, tier));
        ((CustomRangedWeapon) item).configure(defaults);
        var e = new RangedEntry(new Identifier(MOD_ID, name), item, defaults);
        rangedEntries.add(e);
        return e;
    }

    private static final float rapier_attack_speed = -2.0F;
    private static Weapon.Entry rapier(String name, Weapon.CustomMaterial material, float damage) {
        var item = new SpellSwordItem(material, new Item.Settings());
        return entry(name, material, item, new ItemConfig.Weapon(damage, rapier_attack_speed));
    }

    private static final float lute_attack_speed = -3.0F;
    private static Weapon.Entry lute(String name, Weapon.CustomMaterial material, float damage, int tier) {
        var item = new StaffItem(material, new Item.Settings());
        return entry(name, material, item, new ItemConfig.Weapon(damage, lute_attack_speed))
                .attribute(ItemConfig.Attribute.bonus(SpellSchools.ARCANE.id, tier(LUTE_ARC_START, tier)))
                .attribute(ItemConfig.Attribute.bonus(SpellSchools.HEALING.id, tier(LUTE_HEAL_START, tier)));
    }

    private static final float lyre_attack_speed = -2.2F;
    private static final float lyre_attack_damage = 3.0F;
    private static Weapon.Entry lyre(String name, Weapon.CustomMaterial material, int tier) {
        var item = new StaffItem(material, new Item.Settings());
        return entry(name, material, item, new ItemConfig.Weapon(lyre_attack_damage, lyre_attack_speed))
                .attribute(ItemConfig.Attribute.bonus(SpellSchools.ARCANE.id, tier(LYRE_ARC_START, tier)))
                .attribute(ItemConfig.Attribute.bonus(SpellSchools.HEALING.id, tier(LYRE_HEAL_START, tier)));
    }

    public static final Weapon.Entry golden_rapier = rapier("golden_rapier",
            Weapon.CustomMaterial.matching(ToolMaterials.GOLD, () -> Ingredient.ofItems(Items.GOLD_INGOT)), 2.1F);
    public static final Weapon.Entry iron_rapier = rapier("iron_rapier",
            Weapon.CustomMaterial.matching(ToolMaterials.IRON, () -> Ingredient.ofItems(Items.IRON_INGOT)), 3.6F);
    public static final Weapon.Entry diamond_rapier = rapier("diamond_rapier",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(Items.DIAMOND)), 4.4F);
    public static final Weapon.Entry netherite_rapier = rapier("netherite_rapier",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.NETHERITE_INGOT)), 5.9F);

    public static final Weapon.Entry wooden_lute = lute("wooden_lute",
            Weapon.CustomMaterial.matching(ToolMaterials.IRON, () -> Ingredient.ofItems(Items.IRON_INGOT)), 4.0F, 0);
    public static final Weapon.Entry diamond_lute = lute("diamond_lute",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(Items.DIAMOND)), 6.0F, 1);
    public static final Weapon.Entry netherite_lute = lute("netherite_lute",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.NETHERITE_INGOT)), 8.0F, 2);

    public static final Weapon.Entry golden_lyre = lyre("golden_lyre",
            Weapon.CustomMaterial.matching(ToolMaterials.IRON, () -> Ingredient.ofItems(Items.GOLD_INGOT)), 0);
    public static final Weapon.Entry diamond_lyre = lyre("diamond_lyre",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(Items.DIAMOND)), 1);
    public static final Weapon.Entry netherite_lyre = lyre("netherite_lyre",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.NETHERITE_INGOT)), 2);

    private static final int pullTimeTicks = 24;
    public static final RangedEntry harp_crossbow = harpCrossbow("harp_crossbow",
            326, false, () -> Ingredient.ofItems(Items.STICK), new RangedConfig(pullTimeTicks, 6.0F, 0), 0);
    public static final RangedEntry diamond_harp_crossbow = harpCrossbow("diamond_harp_crossbow",
            562, false, () -> Ingredient.ofItems(Items.GOLD_INGOT), new RangedConfig(pullTimeTicks, 7.0F, 0), 1);
    public static final RangedEntry netherite_harp_crossbow = harpCrossbow("netherite_harp_crossbow",
            562, true, () -> Ingredient.ofItems(Items.NETHERITE_INGOT), new RangedConfig(pullTimeTicks, 8.0F, 0), 2);

    public static void register(Map<String, RangedConfig> rangedConfigs, Map<String, ItemConfig.Weapon> configs) {
        if (tweaksConfig.value.ignore_items_required_mods || FabricLoader.getInstance().isModLoaded(BETTER_NETHER)) {
            boolean loaded = FabricLoader.getInstance().isModLoaded(BETTER_NETHER);
            var repair = ingredient("betternether:nether_ruby", loaded, Items.NETHERITE_INGOT);
            rapier("ruby_rapier", Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, repair), 6.7F);
            lute("ruby_lute", Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, repair), 10.0F, 3);
            harpCrossbow("ruby_harp_crossbow", 562, true, repair, new RangedConfig(pullTimeTicks, 9.0F, 0), 3);
        }
        if (tweaksConfig.value.ignore_items_required_mods || FabricLoader.getInstance().isModLoaded(BETTER_END)) {
            boolean loaded = FabricLoader.getInstance().isModLoaded(BETTER_END);
            var repair = ingredient("betterend:aeternium_ingot", loaded, Items.NETHERITE_INGOT);
            rapier("aeternium_rapier", Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, repair), 6.7F);
            lyre("aeternium_lyre", Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, repair), 3);
        }
        if (tweaksConfig.value.ignore_items_required_mods || FabricLoader.getInstance().isModLoaded(LNE)) {
            rapier("ender_dragon_rapier",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.AMETHYST_SHARD)), 6.7F);
            rapier("elder_guardian_rapier",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.PRISMARINE_SHARD)), 6.7F);
            rapier("wither_rapier",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.BONE)), 6.7F);
            rapier("glacial_rapier",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.BLUE_ICE)), 6.7F);
            lute("ender_dragon_lute",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.AMETHYST_SHARD)), 10.0F, 3);
            lyre("elder_guardian_lyre",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.PRISMARINE_SHARD)), 3);
            harpCrossbow("elder_guardian_harp_crossbow", 562, false,
                    () -> Ingredient.ofItems(Items.PRISMARINE_SHARD), new RangedConfig(pullTimeTicks, 9.0F, 0), 3);
        }

        Weapon.register(configs, entries, Group.KEY);

        for (var entry : rangedEntries) {
            var config = rangedConfigs.get(entry.id().toString());
            if (config == null) {
                config = entry.defaults();
                rangedConfigs.put(entry.id().toString(), config);
            }
            ((CustomRangedWeapon) entry.item()).configure(config);
            Registry.register(Registries.ITEM, entry.id(), entry.item());
        }
        ItemGroupEvents.modifyEntriesEvent(Group.KEY).register(content -> {
            for (var entry : rangedEntries) content.add(entry.item());
        });
    }
}
