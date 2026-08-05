package com.bard_rpg.item;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.spell_engine.api.item.ItemConfig;
import net.spell_engine.api.item.armor.Armor;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.bard_rpg.BardsMod.MOD_ID;

public class Armors {
    public static final ArrayList<Armor.Entry> entries = new ArrayList<>();

    private static Armor.Set<BardArmorItem> create(Armor.CustomMaterial material, ItemConfig.ArmorSet defaults,
                                                    String modelName, String textureName) {
        return new Armor.Entry(material, null, defaults)
                .bundle(mat -> new Armor.Set<>(MOD_ID,
                        new BardArmorItem(mat, ArmorItem.Type.HELMET, new net.minecraft.item.Item.Settings(), modelName, textureName),
                        new BardArmorItem(mat, ArmorItem.Type.CHESTPLATE, new net.minecraft.item.Item.Settings(), modelName, textureName),
                        new BardArmorItem(mat, ArmorItem.Type.LEGGINGS, new net.minecraft.item.Item.Settings(), modelName, textureName),
                        new BardArmorItem(mat, ArmorItem.Type.BOOTS, new net.minecraft.item.Item.Settings(), modelName, textureName)
                ))
                .put(entries)
                .armorSet();
    }

    public static final float bard_speed_T1 = 0.03F;
    public static final float bard_speed_T2 = 0.04F;
    public static final float bard_speed_T3 = 0.05F;

    public static final float bard_spell_power_t1 = 0.15F;
    public static final float bard_spell_power_t2 = 0.2F;
    public static final float bard_spell_power_t3 = 0.25F;

    private static ItemConfig.Attribute spellPower(float value) {
        return ItemConfig.Attribute.multiply(SpellSchools.ARCANE.id, value);
    }
    private static ItemConfig.Attribute healingPower(float value) {
        return ItemConfig.Attribute.multiply(SpellSchools.HEALING.id, value);
    }
    private static ItemConfig.Attribute movementSpeed(float value) {
        return ItemConfig.Attribute.multiply(new Identifier("minecraft", "generic.movement_speed"), value);
    }

    public static final Armor.Set entertainerArmorSet = create(
            new Armor.CustomMaterial("entertainer_garb", 10, 9, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, () -> Ingredient.ofItems(Items.WHITE_WOOL)),
            ItemConfig.ArmorSet.with(
                    new ItemConfig.ArmorSet.Piece(1).addAll(List.of(spellPower(bard_spell_power_t1), healingPower(bard_spell_power_t1), movementSpeed(bard_speed_T1))),
                    new ItemConfig.ArmorSet.Piece(3).addAll(List.of(spellPower(bard_spell_power_t1), healingPower(bard_spell_power_t1), movementSpeed(bard_speed_T1))),
                    new ItemConfig.ArmorSet.Piece(2).addAll(List.of(spellPower(bard_spell_power_t1), healingPower(bard_spell_power_t1), movementSpeed(bard_speed_T1))),
                    new ItemConfig.ArmorSet.Piece(1).addAll(List.of(spellPower(bard_spell_power_t1), healingPower(bard_spell_power_t1), movementSpeed(bard_speed_T1)))
            ),
            "entertainer_armor", "entertainer_armor"
    );

    public static final Armor.Set troubadourArmorSet = create(
            new Armor.CustomMaterial("troubadour_garb", 20, 10, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, () -> Ingredient.ofItems(Items.WHITE_WOOL)),
            ItemConfig.ArmorSet.with(
                    new ItemConfig.ArmorSet.Piece(2).addAll(List.of(spellPower(bard_spell_power_t2), healingPower(bard_spell_power_t2), movementSpeed(bard_speed_T2))),
                    new ItemConfig.ArmorSet.Piece(5).addAll(List.of(spellPower(bard_spell_power_t2), healingPower(bard_spell_power_t2), movementSpeed(bard_speed_T2))),
                    new ItemConfig.ArmorSet.Piece(3).addAll(List.of(spellPower(bard_spell_power_t2), healingPower(bard_spell_power_t2), movementSpeed(bard_speed_T2))),
                    new ItemConfig.ArmorSet.Piece(2).addAll(List.of(spellPower(bard_spell_power_t2), healingPower(bard_spell_power_t2), movementSpeed(bard_speed_T2)))
            ),
            "troubadour_armor", "troubadour_armor"
    );

    public static final Armor.Set netheriteTroubadourArmorSet = create(
            new Armor.CustomMaterial("netherite_troubadour_garb", 25, 15, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, () -> Ingredient.ofItems(Items.NETHERITE_INGOT)),
            ItemConfig.ArmorSet.with(
                    new ItemConfig.ArmorSet.Piece(2).addAll(List.of(spellPower(bard_spell_power_t3), healingPower(bard_spell_power_t3), movementSpeed(bard_speed_T3))),
                    new ItemConfig.ArmorSet.Piece(5).addAll(List.of(spellPower(bard_spell_power_t3), healingPower(bard_spell_power_t3), movementSpeed(bard_speed_T3))),
                    new ItemConfig.ArmorSet.Piece(3).addAll(List.of(spellPower(bard_spell_power_t3), healingPower(bard_spell_power_t3), movementSpeed(bard_speed_T3))),
                    new ItemConfig.ArmorSet.Piece(2).addAll(List.of(spellPower(bard_spell_power_t3), healingPower(bard_spell_power_t3), movementSpeed(bard_speed_T3)))
            ),
            "troubadour_armor", "netherite_troubadour_armor"
    );

    public static void register(Map<String, ItemConfig.ArmorSet> configs) {
        Armor.register(configs, entries, Group.KEY);
    }
}
