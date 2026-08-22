package com.bards.item;

import com.bards.BardsMod;
import com.bards.content.BardsSpells;
import net.fabric_extras.ranged_weapon.api.RangedConfig;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.item.ItemGroup;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.more_rpg_classes.custom.MrpgLibSpells;
import net.more_rpg_classes.item.MRPGCItemGroups;
import net.spell_engine.rpg_series.config.AttributeModifier;
import net.spell_engine.rpg_series.config.WeaponConfig;
import net.spell_engine.api.spell.container.SpellContainers;
import net.spell_engine.rpg_series.item.Equipment;
import net.spell_engine.api.item.weapon.StaffItem;
import net.spell_engine.rpg_series.item.RangedWeapon;
import net.spell_engine.rpg_series.item.Weapon;
import net.spell_engine.api.item.weapon.SpellSwordItem;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.bards.BardsMod.MOD_ID;

public class Weapons {
    public static final ArrayList<Weapon.Entry> meleeEntries = new ArrayList<>();
    public static final ArrayList<RangedWeapon.Entry> rangedEntries = new ArrayList<>();

    private static final Map<Weapon.Entry, RegistryKey<ItemGroup>> groupOverrides = new IdentityHashMap<>();
    private static final Map<RangedWeapon.Entry, RegistryKey<ItemGroup>> rangedGroupOverrides = new IdentityHashMap<>();

    private static Weapon.Entry groupKey(Weapon.Entry entry, RegistryKey<ItemGroup> key) {
        groupOverrides.put(entry, key);
        return entry;
    }

    private static RangedWeapon.Entry groupKey(RangedWeapon.Entry entry, RegistryKey<ItemGroup> key) {
        rangedGroupOverrides.put(entry, key);
        return entry;
    }

    private static Weapon.Entry meleeEntry(String name, Weapon.CustomMaterial material, Weapon.Factory factory, WeaponConfig defaults, Equipment.WeaponType category) {
        var entry = new Weapon.Entry(MOD_ID, name, material, factory, defaults, category);
        if (entry.isRequiredModInstalled()) {
            meleeEntries.add(entry);
            entry.loot(Equipment.LootProperties.of(""));
        }
        return entry;
    }


    private static Supplier<Ingredient> ingredient(String idString, boolean requirement, Item fallback) {
        var id = Identifier.of(idString);
        if (requirement) {
            return () -> {
                return Ingredient.ofItems(fallback);
            };
        } else {
            return () -> {
                var item = Registries.ITEM.get(id);
                var ingredient = item != null ? item : fallback;
                return Ingredient.ofItems(ingredient);
            };
        }
    }

    private static final String AETHER = "aether";
    private static final String BETTER_END = "betterend";
    private static final String BETTER_NETHER = "betternether";
    private static final String LNE = "loot_n_explore";
    private static final String ARSENAL = "arsenal";
    // Rapiers
    private static final float rapier_attack_speed = -2.0F;
    private static Weapon.Entry rapier(String name, Weapon.CustomMaterial material, float damage) {
        return meleeEntry(name, material, SpellSwordItem::new, new WeaponConfig(damage, rapier_attack_speed), Equipment.WeaponType.SWORD)
                .spellContainer(SpellContainers.forMeleeWeapon().withSpellId(MrpgLibSpells.puncture.id()));
    }
    // Spell power weapon balancing
    private static final float STEP            = 0.5F;
    private static final float BASE_START      = 3.0F;
    private static final float LUTE_ARC_START  = round05(BASE_START);
    private static final float LUTE_HEAL_START = round05(BASE_START);
    private static final float LYRE_ARC_START  = round05(BASE_START * 0.5F);
    private static final float LYRE_HEAL_START = round05(BASE_START * 1.5F);
    private static final float HARP_ARC_START  = round05(BASE_START * 0.75F);
    private static final float HARP_HEAL_START = round05(BASE_START * 0.5F);

    private static float round05(float v) { return Math.round(v * 2) / 2.0F; }
    private static float tier(float start, int idx) {
        return round05(start + STEP * Math.min(idx, 3));
    }
    private static float harpSpellPower(Equipment.Tier equipTier, boolean arcane) {
        int idx = equipTier.ordinal();
        return arcane ? tier(HARP_ARC_START, idx) : tier(HARP_HEAL_START, idx);
    }

    private static final float lute_attack_speed = -3.0F;
    private static Weapon.Entry lute(String name, Weapon.CustomMaterial material, float damage, int tier) {
        return meleeEntry(name, material, StaffItem::new, new WeaponConfig(damage, lute_attack_speed), Equipment.WeaponType.DAMAGE_STAFF)
                .spellContainer(SpellContainers.forMagicWeapon()).withSpellChoices("bards_rpg:weapon/lute")
                .attribute(AttributeModifier.bonus(SpellSchools.ARCANE.id, tier(LUTE_ARC_START, tier)))
                .attribute(AttributeModifier.bonus(SpellSchools.HEALING.id, tier(LUTE_HEAL_START, tier)));
    }
    private static final float lyre_attack_speed = -2.2F;
    private static final float lyre_attack_damage = 3.0F;
    private static Weapon.Entry lyre(String name, Weapon.CustomMaterial material, int tier) {
        return meleeEntry(name, material, StaffItem::new, new WeaponConfig(lyre_attack_damage, lyre_attack_speed), Equipment.WeaponType.HEALING_STAFF)
                .spellContainer(SpellContainers.forMagicWeapon()).withSpellChoices("bards_rpg:weapon/lyre")
                .attribute(AttributeModifier.bonus(SpellSchools.ARCANE.id, tier(LYRE_ARC_START, tier)))
                .attribute(AttributeModifier.bonus(SpellSchools.HEALING.id, tier(LYRE_HEAL_START, tier)));
    }
    /// RAPIERS
    public static final Weapon.Entry golden_rapier = rapier("golden_rapier",
            Weapon.CustomMaterial.matching(ToolMaterials.GOLD, () -> Ingredient.ofItems(Items.GOLD_INGOT)), 2.1F)
            .translatedName("Golden Rapier")
            .loot(Equipment.LootProperties.of("golden"));
    public static final Weapon.Entry iron_rapier = rapier("iron_rapier",
            Weapon.CustomMaterial.matching(ToolMaterials.IRON, () -> Ingredient.ofItems(Items.IRON_INGOT)), 3.6F)
            .translatedName("Iron Rapier")
            .loot(Equipment.LootProperties.of(1));
    public static final Weapon.Entry diamond_rapier = rapier("diamond_rapier",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(Items.DIAMOND)), 4.4F)
            .translatedName("Diamond Rapier")
            .loot(Equipment.LootProperties.of(2));
    public static final Weapon.Entry netherite_rapier = rapier("netherite_rapier",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.NETHERITE_INGOT)), 5.9F)
            .translatedName("Netherite Rapier")
            .loot(Equipment.LootProperties.of(3));
    /// LUTES
    public static final Weapon.Entry wooden_lute = lute("wooden_lute",
            Weapon.CustomMaterial.matching(ToolMaterials.IRON, () -> Ingredient.ofItems(Items.IRON_INGOT)), 4.0F, 0)
            .translatedName("Wooden Lute")
            .loot(Equipment.LootProperties.of(1));
    public static final Weapon.Entry diamond_lute = lute("diamond_lute",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(Items.DIAMOND)), 6.0F, 1)
            .translatedName("Diamond Lute")
            .loot(Equipment.LootProperties.of(2));
    public static final Weapon.Entry netherite_lute = lute("netherite_lute",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.NETHERITE_INGOT)), 8.0F, 2)
            .translatedName("Netherite Lute")
            .loot(Equipment.LootProperties.of(3));
    /// LYRES
    public static final Weapon.Entry golden_lyre = lyre("golden_lyre",
            Weapon.CustomMaterial.matching(ToolMaterials.IRON, () -> Ingredient.ofItems(Items.GOLD_INGOT)), 0)
            .translatedName("Golden Lyre")
            .loot(Equipment.LootProperties.of("golden"));
    public static final Weapon.Entry diamond_lyre = lyre("diamond_lyre",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(Items.DIAMOND)), 1)
            .translatedName("Diamond Lyre")
            .loot(Equipment.LootProperties.of(2));
    public static final Weapon.Entry netherite_lyre = lyre("netherite_lyre",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.NETHERITE_INGOT)), 2)
            .translatedName("Netherite Lyre")
            .loot(Equipment.LootProperties.of(3));
    ///HARP CROSSBOW
    // Pull time in seconds added on top of the base crossbow pull time (Rapid=0F, Heavy=0.75F)
    private static final float PULL_TIME_HARP_CROSSBOW = 0.2F;
    private static final float VELOCITY_HARP_CROSSBOW = 0F;
    // ~20% damage penalty vs Rapid Crossbow per tier (extra 2 arrows + spell power compensate)
    public static float penaltyMultiplier = 0.8F;
    private static final float[] RAPID_CROSSBOW_DAMAGE = { 7.5F, 8.0F, 9.5F, 10.5F, 11.25F, 11.25F};
    private static float rangedDamage(int tier) {
        float base = RAPID_CROSSBOW_DAMAGE[Math.min(tier, RAPID_CROSSBOW_DAMAGE.length - 1)];
        return Math.round(base * penaltyMultiplier * 10) / 10.0F;
    }

    private static RangedWeapon.Entry harpCrossbow(String name, Equipment.Tier tier, Supplier<Ingredient> repairIngredientSupplier) {
        var entry = new RangedWeapon.Entry(Identifier.of(MOD_ID, name), tier, HarpCrossbowItem::new,
                new RangedConfig(rangedDamage(tier.getNumber()), PULL_TIME_HARP_CROSSBOW, VELOCITY_HARP_CROSSBOW)
                        .withAttribute(SpellSchools.ARCANE.id, EntityAttributeModifier.Operation.ADD_VALUE, harpSpellPower(tier, true))
                        .withAttribute(SpellSchools.HEALING.id, EntityAttributeModifier.Operation.ADD_VALUE, harpSpellPower(tier, false))

                , repairIngredientSupplier, Equipment.WeaponType.RAPID_CROSSBOW);
        rangedEntries.add(entry);
        return entry;
    }

    public static final RangedWeapon.Entry harp_crossbow = harpCrossbow("harp_crossbow",
            Equipment.Tier.TIER_1, () -> Ingredient.ofItems(Items.STICK))
            .translatedName("Harp Crossbow");
    public static final RangedWeapon.Entry diamond_harp_crossbow = harpCrossbow("diamond_harp_crossbow",
            Equipment.Tier.TIER_2, () -> Ingredient.ofItems(Items.GOLD_INGOT))
            .translatedName("Diamond Harp Crossbow");
    public static final RangedWeapon.Entry netherite_harp_crossbow = harpCrossbow("netherite_harp_crossbow",
            Equipment.Tier.TIER_3, () -> Ingredient.ofItems(Items.NETHERITE_INGOT))
            .translatedName("Netherite Harp Crossbow");


    /// REGISTRY
    private static final float rapier_t5_attack_damage = 6.7F;
    private static final float lute_t5_attack_damage = 10;
    public static Weapon.Entry uniqueRapier0;
    public static void register(Map<String, RangedConfig> rangedConfig, Map<String, WeaponConfig> meleeConfig) {
        if (BardsMod.tweaksConfig.value.ignore_items_required_mods || FabricLoader.getInstance().isModLoaded(BETTER_NETHER)) {
            var repair = ingredient("betternether:nether_ruby", FabricLoader.getInstance().isModLoaded(BETTER_NETHER), Items.NETHERITE_INGOT);
            rapier("ruby_rapier", Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, repair), rapier_t5_attack_damage)
                    .translatedName("Ruby Rapier")
                    .loot(Equipment.LootProperties.of(4));
            lute("ruby_lute", Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, repair), lute_t5_attack_damage, 3)
                    .translatedName("Ruby Lute")
                    .loot(Equipment.LootProperties.of(4));
            harpCrossbow("ruby_harp_crossbow",Equipment.Tier.TIER_4,repair)
                    .translatedName("Heavenly Harp Crossbow");
        }
        if (BardsMod.tweaksConfig.value.ignore_items_required_mods || FabricLoader.getInstance().isModLoaded(BETTER_END)) {
            var repair = ingredient("betterend:aeternium_ingot", FabricLoader.getInstance().isModLoaded(BETTER_END), Items.NETHERITE_INGOT);
            rapier("aeternium_rapier", Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, repair), rapier_t5_attack_damage)
                    .translatedName("Aeternium Rapier")
                    .loot(Equipment.LootProperties.of(4));
            lyre("aeternium_lyre", Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, repair), 3)
                    .translatedName("Aeternium Lyre")
                    .loot(Equipment.LootProperties.of(4));
        }
        if (BardsMod.tweaksConfig.value.ignore_items_required_mods || FabricLoader.getInstance().isModLoaded(AETHER) || FabricLoader.getInstance().isDevelopmentEnvironment()) {
            var repair = ingredient("aether:ambrosium_shard", FabricLoader.getInstance().isModLoaded(AETHER), Items.NETHERITE_INGOT);
            rapier("aether_rapier", Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, repair), rapier_t5_attack_damage)
                    .translatedName("Valkyrie Rapier")
                    .loot(Equipment.LootProperties.of("aether"));
            lute("aether_lute", Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, repair), lute_t5_attack_damage, 3)
                    .translatedName("Angelic Lute")
                    .loot(Equipment.LootProperties.of("aether"));
            lyre("aether_lyre", Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, repair), 3)
                    .translatedName("Valkyrie Lyre")
                    .loot(Equipment.LootProperties.of("aether"));
            harpCrossbow("aether_harp_crossbow",Equipment.Tier.TIER_4,repair)
                    .translatedName("Divine Harp Crossbow").loot(-1, "aether");
        }
        if (BardsMod.tweaksConfig.value.ignore_items_required_mods || FabricLoader.getInstance().isModLoaded(LNE) || FabricLoader.getInstance().isDevelopmentEnvironment()) {
            rapier("ender_dragon_rapier", Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.AMETHYST_SHARD)), rapier_t5_attack_damage)
                    .translatedName("Dragon's Rapier")
                    .withAdditionalSpell(MrpgLibSpells.dragonclaw_melee.id().toString())
                    .rarity = Rarity.RARE;
            rapier("elder_guardian_rapier", Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.PRISMARINE_SHARD)), rapier_t5_attack_damage)
                    .translatedName("Coral Rapier")
                    .withAdditionalSpell(MrpgLibSpells.waterbomb_melee.id().toString())
                    .rarity = Rarity.RARE;
            rapier("wither_rapier", Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.BONE)), rapier_t5_attack_damage)
                    .translatedName("Withered Rapier")
                    .withAdditionalSpell(MrpgLibSpells.wither_pulse_melee.id().toString())
                    .rarity = Rarity.RARE;
            rapier("glacial_rapier", Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.BLUE_ICE)), rapier_t5_attack_damage)
                    .translatedName("Glacial Rapier")
                    .withAdditionalSpell(MrpgLibSpells.avalanche_melee.id().toString())
                    .rarity = Rarity.RARE;
            lute("ender_dragon_lute", Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.AMETHYST_SHARD)), lute_t5_attack_damage, 3)
                    .translatedName("Dragon Lute")
                    .withSpellChoices("bards_rpg:weapon/dragon_lute")
                    .withAdditionalSpell(MrpgLibSpells.dragonslayers_fury.id().toString())
                    .rarity = Rarity.RARE;
            lyre("elder_guardian_lyre", Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.PRISMARINE_SHARD)), 3)
                    .translatedName("Siren's Lyre")
                    .withSpellChoices("bards_rpg:weapon/ocean_lyre")
                    .withAdditionalSpell(MrpgLibSpells.sirens_tears.id().toString())
                    .rarity = Rarity.RARE;
            harpCrossbow("elder_guardian_harp_crossbow",Equipment.Tier.TIER_5,() -> Ingredient.ofItems(Items.PRISMARINE_SHARD))
                    .translatedName("Atlantis Harp Crossbow")
                    .spellContainer(SpellContainers.forRangedWeapon().withSpellId(Identifier.of(MrpgLibSpells.reef_arrows.id().toString())));

        }
        if (BardsMod.tweaksConfig.value.ignore_items_required_mods || FabricLoader.getInstance().isModLoaded(ARSENAL) || FabricLoader.getInstance().isDevelopmentEnvironment()) {
            uniqueRapier0 = groupKey(rapier("unique_rapier_0", Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.GOLD_BLOCK)), rapier_t5_attack_damage)
                    .translatedName("Singing Blade")
                    .loot(Equipment.LootProperties.of(5, "divine"))
                    .withAdditionalSpell("arsenal:radiance_melee"), MRPGCItemGroups.ARSENAL_KEY);
            uniqueRapier0.rarity = Rarity.RARE;
            var uniqueRapier1 = groupKey(rapier("unique_rapier_1", Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.IRON_BLOCK)), rapier_t5_attack_damage)
                    .translatedName("Duelist's Rapier")
                    .loot(Equipment.LootProperties.of(5))
                    .withAdditionalSpell(MrpgLibSpells.duelists_focus.id().toString()), MRPGCItemGroups.ARSENAL_KEY);
            uniqueRapier1.rarity = Rarity.RARE;
            var uniqueLute0 = groupKey(lute("unique_lute_0", Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.REDSTONE_BLOCK)), lute_t5_attack_damage, 3)
                    .translatedName("Lute of Ruby Verdict")
                    .withSpellChoices("bards_rpg:weapon/ruby_verdict_lute")
                    .withAdditionalSpell(BardsSpells.melody_of_the_meteor.id().toString())
                    .loot(Equipment.LootProperties.of(5)), MRPGCItemGroups.ARSENAL_KEY);
            uniqueLute0.rarity = Rarity.RARE;
            var uniqueLute1 = groupKey(lute("unique_lute_1", Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.IRON_BLOCK)), lute_t5_attack_damage, 3)
                    .translatedName("Spellthief's Lute")
                    .withSpellChoices("bards_rpg:weapon/spellthief_lute")
                    .withAdditionalSpell(BardsSpells.spellthief.id().toString())
                    .loot(Equipment.LootProperties.of(5)), MRPGCItemGroups.ARSENAL_KEY);
            uniqueLute1.rarity = Rarity.RARE;
            var uniqueLyre0 = groupKey(lyre("unique_lyre_0", Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.GOLD_BLOCK)), 3)
                    .translatedName("Lyre of Apollo")
                    .withSpellChoices("bards_rpg:weapon/apollo_lyre")
                    .withAdditionalSpell("arsenal:radiance_spell")
                    .loot(Equipment.LootProperties.of(5, "divine")), MRPGCItemGroups.ARSENAL_KEY);
            uniqueLyre0.rarity = Rarity.RARE;
            var uniqueLyre1 = groupKey(lyre("unique_lyre_1", Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.GOLD_BLOCK)), 3)
                    .translatedName("Lyre of Antecael")
                    .withSpellChoices("bards_rpg:weapon/antecael_lyre")
                    .withAdditionalSpell(BardsSpells.eclipse_mantle.id().toString())
                    .loot(Equipment.LootProperties.of(5, "elven")), MRPGCItemGroups.ARSENAL_KEY);
            uniqueLyre1.rarity = Rarity.RARE;
            groupKey(harpCrossbow("unique_harp_crossbow_0",Equipment.Tier.TIER_5,() -> Ingredient.ofItems(Items.NETHERITE_INGOT))
                    .translatedName("Lightning Harp Crossbow")
                    .spellContainer(SpellContainers.forRangedWeapon().withSpellId(Identifier.of(MrpgLibSpells.lightning_strike_ranged.id().toString()))), MRPGCItemGroups.ARSENAL_KEY);
            groupKey(harpCrossbow("unique_harp_crossbow_1",Equipment.Tier.TIER_5,() -> Ingredient.ofItems(Items.NETHERITE_INGOT))
                    .translatedName("Starshot Harp Crossbow")
                    .loot(5, "divine")
                    .spellContainer(SpellContainers.forRangedWeapon().withSpellId(Identifier.of(BardsSpells.starshots.id().toString()))), MRPGCItemGroups.ARSENAL_KEY);
        }

        Weapon.register(meleeConfig, meleeEntries, Group.KEY);
        RangedWeapon.register(rangedConfig, rangedEntries, Group.KEY);

        for (var override : groupOverrides.entrySet()) {
            var entry = override.getKey();
            var key = override.getValue();
            ItemGroupEvents.modifyEntriesEvent(Group.KEY).register(content -> {
                content.getDisplayStacks().removeIf(stack -> stack.isOf(entry.item()));
                content.getSearchTabStacks().removeIf(stack -> stack.isOf(entry.item()));
            });
            ItemGroupEvents.modifyEntriesEvent(key).register(content -> content.add(entry.item()));
        }
        for (var override : rangedGroupOverrides.entrySet()) {
            var entry = override.getKey();
            var key = override.getValue();
            ItemGroupEvents.modifyEntriesEvent(Group.KEY).register(content -> {
                content.getDisplayStacks().removeIf(stack -> stack.isOf(entry.item()));
                content.getSearchTabStacks().removeIf(stack -> stack.isOf(entry.item()));
            });
            ItemGroupEvents.modifyEntriesEvent(key).register(content -> content.add(entry.item()));
        }
    }
}
