package com.bards.item;

import com.bards.platform.Platform;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.more_rpg_classes.item.MRPGCItemGroups;
import net.spell_engine.rpg_series.config.ArmorSetConfig;
import net.spell_engine.rpg_series.config.AttributeModifier;
import net.spell_engine.rpg_series.item.Equipment;
import net.spell_engine.rpg_series.item.Armor;
import net.spell_engine.api.spell.SpellDataComponents;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.bards.BardsMod.MOD_ID;
import static com.bards.compat.CompatLoadingCheck.armoryLoadCheck;

public class Armors {
    public static final ArrayList<Armor.Entry> entries = new ArrayList<>();
    private static Armor.Entry create(RegistryEntry<ArmorMaterial> material, Identifier id, int durability, int tier,
                                      Armor.Set.ItemFactory factory, ArmorSetConfig defaults, Armor.ItemSettingsTweaker settings) {
        var entry = Armor.Entry.create(
                material,
                id,
                durability,
                factory,
                defaults,
                Equipment.LootProperties.of(tier),
                settings
        );
        entries.add(entry);
        return entry;
    }

    private static final Map<Armor.Entry, RegistryKey<ItemGroup>> groupOverrides = new IdentityHashMap<>();

    private static Armor.Entry groupKey(Armor.Entry entry, RegistryKey<ItemGroup> key) {
        groupOverrides.put(entry, key);
        return entry;
    }

    public static RegistryEntry<ArmorMaterial> material(
            String name, int protectionHead, int protectionChest, int protectionLegs, int protectionFeet,
            int enchantability, RegistryEntry<SoundEvent> equipSound, Supplier<Ingredient> repairIngredient) {

        var material = new ArmorMaterial(
                Map.of(
                        ArmorItem.Type.HELMET, protectionHead,
                        ArmorItem.Type.CHESTPLATE, protectionChest,
                        ArmorItem.Type.LEGGINGS, protectionLegs,
                        ArmorItem.Type.BOOTS, protectionFeet),
                enchantability, equipSound, repairIngredient,
                List.of(new ArmorMaterial.Layer(Identifier.of(MOD_ID, name))),
                0,0
        );
        return Registry.registerReference(Registries.ARMOR_MATERIAL, Identifier.of(MOD_ID, name), material);
    }
    private static final Supplier<Ingredient> BARD_INGREDIENTS = () -> Ingredient.ofItems(
            Items.WHITE_WOOL
    );
    private static Armor.ItemSettingsTweaker commonSettings(Identifier equipmentSetId) {
        return Armor.ItemSettingsTweaker.standard(itemSettings -> {
            itemSettings
                    .component(SpellDataComponents.EQUIPMENT_SET, equipmentSetId)
                    .component(DataComponentTypes.RARITY, Rarity.RARE);
        });
    }
    public static RegistryEntry<ArmorMaterial> entertainers_garb = material(
            "entertainers_garb",
            1, 3, 2, 1,
            9,
            SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, () -> { return Ingredient.fromTag(ItemTags.WOOL); });

    public static RegistryEntry<ArmorMaterial> troubadours_garb = material(
            "troubadours_garb",
            1, 3, 2, 1,
            10,
            SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, () -> { return Ingredient.fromTag(ItemTags.WOOL); });

    public static RegistryEntry<ArmorMaterial> netherite_troubadours_garb = material(
            "netherite_troubadours_garb",
            1, 3, 2, 1,
            15,
            SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, () -> { return Ingredient.ofItems(Items.NETHERITE_INGOT); });
    public static RegistryEntry<ArmorMaterial> storytellers_garb = material(
            "storytellers_garb",
            1, 3, 2, 1,
            18,
            SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, () -> { return Ingredient.ofItems(Items.NETHERITE_INGOT); });

    public static final float bard_speed_T1 = 0.03F;
    public static final float bard_speed_T2 = 0.04F;
    public static final float bard_speed_T3 = 0.05F;
    public static final float bard_speed_T5 = 0.06F;

    public static final float bard_spell_power_t1 = 0.15F;
    public static final float bard_spell_power_t2 = 0.2F;
    public static final float bard_spell_power_t3 = 0.25F;
    public static final float bard_spell_power_t5 = 0.3F;

    private static final Identifier MOVEMENT_SPEED_ID = Identifier.ofVanilla("generic.movement_speed");
    private static AttributeModifier movementSpeed(float value) {
        return new AttributeModifier(
                MOVEMENT_SPEED_ID.toString(),
                value,
                EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    }


    public static final Armor.Entry entertainerArmorSet = create(
            entertainers_garb,
            Identifier.of(MOD_ID, "entertainer_garb"),
            10,
            1,
            Armor.CustomItem::new,
            ArmorSetConfig.with(
                    new ArmorSetConfig.Piece(entertainers_garb.value().getProtection(ArmorItem.Type.HELMET))
                            .add(AttributeModifier.multiply(SpellSchools.ARCANE.id, bard_spell_power_t1))
                            .add(AttributeModifier.multiply(SpellSchools.HEALING.id, bard_spell_power_t1))
                            .add(movementSpeed(bard_speed_T1)),
                    new ArmorSetConfig.Piece(entertainers_garb.value().getProtection(ArmorItem.Type.CHESTPLATE))
                            .add(AttributeModifier.multiply(SpellSchools.ARCANE.id, bard_spell_power_t1))
                            .add(AttributeModifier.multiply(SpellSchools.HEALING.id, bard_spell_power_t1))
                            .add(movementSpeed(bard_speed_T1)),
                    new ArmorSetConfig.Piece(entertainers_garb.value().getProtection(ArmorItem.Type.LEGGINGS))
                            .add(AttributeModifier.multiply(SpellSchools.ARCANE.id, bard_spell_power_t1))
                            .add(AttributeModifier.multiply(SpellSchools.HEALING.id, bard_spell_power_t1))
                            .add(movementSpeed(bard_speed_T1)),
                    new ArmorSetConfig.Piece(entertainers_garb.value().getProtection(ArmorItem.Type.BOOTS))
                            .add(AttributeModifier.multiply(SpellSchools.ARCANE.id, bard_spell_power_t1))
                            .add(AttributeModifier.multiply(SpellSchools.HEALING.id, bard_spell_power_t1))
                            .add(movementSpeed(bard_speed_T1))
            ),
            commonSettings(null))
            .translatedName("Entertainer Hat", "Entertainer Garb", "Entertainer Trousers", "Entertainer Boots");
    public static final Armor.Entry troubadourArmorSet = create(
            troubadours_garb,
            Identifier.of(MOD_ID, "troubadour_garb"),
            20,
            2,
            Armor.CustomItem::new,
            ArmorSetConfig.with(
                    new ArmorSetConfig.Piece(troubadours_garb.value().getProtection(ArmorItem.Type.HELMET))
                            .add(AttributeModifier.multiply(SpellSchools.ARCANE.id, bard_spell_power_t2))
                            .add(AttributeModifier.multiply(SpellSchools.HEALING.id, bard_spell_power_t2))
                            .add(movementSpeed(bard_speed_T2)),
                    new ArmorSetConfig.Piece(troubadours_garb.value().getProtection(ArmorItem.Type.CHESTPLATE))
                            .add(AttributeModifier.multiply(SpellSchools.ARCANE.id, bard_spell_power_t2))
                            .add(AttributeModifier.multiply(SpellSchools.HEALING.id, bard_spell_power_t2))
                            .add(movementSpeed(bard_speed_T2)),
                    new ArmorSetConfig.Piece(troubadours_garb.value().getProtection(ArmorItem.Type.LEGGINGS))
                            .add(AttributeModifier.multiply(SpellSchools.ARCANE.id, bard_spell_power_t2))
                            .add(AttributeModifier.multiply(SpellSchools.HEALING.id, bard_spell_power_t2))
                            .add(movementSpeed(bard_speed_T2)),
                    new ArmorSetConfig.Piece(troubadours_garb.value().getProtection(ArmorItem.Type.BOOTS))
                            .add(AttributeModifier.multiply(SpellSchools.ARCANE.id, bard_spell_power_t2))
                            .add(AttributeModifier.multiply(SpellSchools.HEALING.id, bard_spell_power_t2))
                            .add(movementSpeed(bard_speed_T2))
            ),
            commonSettings(null))
            .translatedName("Troubadour Hat", "Troubadour Garb", "Troubadour Trousers", "Troubadour Boots");
    public static final Armor.Entry netheriteTroubadourArmorSet = create(
            netherite_troubadours_garb,
            Identifier.of(MOD_ID, "netherite_troubadour_garb"),
            30,
            3,
            Armor.CustomItem::new,
            ArmorSetConfig.with(
                    new ArmorSetConfig.Piece(netherite_troubadours_garb.value().getProtection(ArmorItem.Type.HELMET))
                            .add(AttributeModifier.multiply(SpellSchools.ARCANE.id, bard_spell_power_t3))
                            .add(AttributeModifier.multiply(SpellSchools.HEALING.id, bard_spell_power_t3))
                            .add(movementSpeed(bard_speed_T3)),
                    new ArmorSetConfig.Piece(netherite_troubadours_garb.value().getProtection(ArmorItem.Type.CHESTPLATE))
                            .add(AttributeModifier.multiply(SpellSchools.ARCANE.id, bard_spell_power_t3))
                            .add(AttributeModifier.multiply(SpellSchools.HEALING.id, bard_spell_power_t3))
                            .add(movementSpeed(bard_speed_T3)),
                    new ArmorSetConfig.Piece(netherite_troubadours_garb.value().getProtection(ArmorItem.Type.LEGGINGS))
                            .add(AttributeModifier.multiply(SpellSchools.ARCANE.id, bard_spell_power_t3))
                            .add(AttributeModifier.multiply(SpellSchools.HEALING.id, bard_spell_power_t3))
                            .add(movementSpeed(bard_speed_T3)),
                    new ArmorSetConfig.Piece(netherite_troubadours_garb.value().getProtection(ArmorItem.Type.BOOTS))
                            .add(AttributeModifier.multiply(SpellSchools.ARCANE.id, bard_spell_power_t3))
                            .add(AttributeModifier.multiply(SpellSchools.HEALING.id, bard_spell_power_t3))
                            .add(movementSpeed(bard_speed_T3))
            ),
            commonSettings(null))
            .translatedName("Netherite Troubadour Hat", "Netherite Troubadour Garb", "Netherite Troubadour Trousers", "Netherite Troubadour Boots");

    public static Armor.Entry storytellerArmorSet;
    public static Identifier storyteller_passive = Identifier.of(MOD_ID, "storyteller");

    public static void register(Map<String, ArmorSetConfig> configs) {
        if (armoryLoadCheck()) {
            storytellerArmorSet = groupKey(create(
                    storytellers_garb,
                    Identifier.of(MOD_ID, "storyteller_garb"),
                    40,
                    5,
                    Armor.CustomItem::new,
                    ArmorSetConfig.with(
                            new ArmorSetConfig.Piece(storytellers_garb.value().getProtection(ArmorItem.Type.HELMET))
                                    .add(AttributeModifier.multiply(SpellSchools.ARCANE.id, bard_spell_power_t5))
                                    .add(AttributeModifier.multiply(SpellSchools.HEALING.id, bard_spell_power_t5))
                                    .add(movementSpeed(bard_speed_T5)),
                            new ArmorSetConfig.Piece(storytellers_garb.value().getProtection(ArmorItem.Type.CHESTPLATE))
                                    .add(AttributeModifier.multiply(SpellSchools.ARCANE.id, bard_spell_power_t5))
                                    .add(AttributeModifier.multiply(SpellSchools.HEALING.id, bard_spell_power_t5))
                                    .add(movementSpeed(bard_speed_T5)),
                            new ArmorSetConfig.Piece(storytellers_garb.value().getProtection(ArmorItem.Type.LEGGINGS))
                                    .add(AttributeModifier.multiply(SpellSchools.ARCANE.id, bard_spell_power_t5))
                                    .add(AttributeModifier.multiply(SpellSchools.HEALING.id, bard_spell_power_t5))
                                    .add(movementSpeed(bard_speed_T5)),
                            new ArmorSetConfig.Piece(storytellers_garb.value().getProtection(ArmorItem.Type.BOOTS))
                                    .add(AttributeModifier.multiply(SpellSchools.ARCANE.id, bard_spell_power_t5))
                                    .add(AttributeModifier.multiply(SpellSchools.HEALING.id, bard_spell_power_t5))
                                    .add(movementSpeed(bard_speed_T5))
                    ),
                    commonSettings(storyteller_passive))
                    .translatedName("Storyteller Hat", "Storyteller Tunic", "Storyteller Trousers", "Storyteller Boots"), MRPGCItemGroups.ARMORY_KEY);
        }
        Armor.register(configs, entries, Group.KEY);
        for (var override : groupOverrides.entrySet()) {
            var entry = override.getKey();
            var key = override.getValue();
            var pieces = entry.armorSet().pieces();
            Platform.get().onItemGroupModify(Group.KEY, tabEntries -> {
                for (var piece : pieces) tabEntries.removeByItem((ArmorItem) piece);
            });
            Platform.get().onItemGroupModify(key, tabEntries -> {
                for (var piece : pieces) {
                    tabEntries.add((ArmorItem) piece);
                }
            });
        }
    }
}
