package com.bard_rpg.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.fabric_extras.ranged_weapon.api.CustomCrossbow;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.spell_power.api.SpellSchools;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class HarpCrossbowItem extends CustomCrossbow {
    public static final String TOOLTIP_KEY = "item.bards_rpg.harp_crossbow.tooltip";

    private static final UUID ARCANE_UUID  = UUID.fromString("5b08e7cb-ef4d-4a23-ae25-7e5f89d05f93");
    private static final UUID HEALING_UUID = UUID.fromString("09a4de93-e2c0-4f1b-9e47-b82d3f3e2e56");

    private final float arcanePower;
    private final float healingPower;

    public HarpCrossbowItem(Settings settings, Supplier<Ingredient> repairIngredientSupplier, float arcanePower, float healingPower) {
        super(settings, repairIngredientSupplier);
        this.arcanePower = arcanePower;
        this.healingPower = healingPower;
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (slot != EquipmentSlot.MAINHAND) return super.getAttributeModifiers(slot);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(super.getAttributeModifiers(slot));
        EntityAttribute arcaneAttr  = Registries.ATTRIBUTE.get(SpellSchools.ARCANE.id);
        EntityAttribute healingAttr = Registries.ATTRIBUTE.get(SpellSchools.HEALING.id);
        if (arcaneAttr  != null) builder.put(arcaneAttr,  new EntityAttributeModifier(ARCANE_UUID,  "Spell Power", arcanePower,  EntityAttributeModifier.Operation.ADDITION));
        if (healingAttr != null) builder.put(healingAttr, new EntityAttributeModifier(HEALING_UUID, "Spell Power", healingPower, EntityAttributeModifier.Operation.ADDITION));
        return builder.build();
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable(TOOLTIP_KEY));
    }
}
