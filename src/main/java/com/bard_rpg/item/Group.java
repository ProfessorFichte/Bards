package com.bard_rpg.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class Group {
    public static final Identifier ID = new Identifier("bards_rpg", "general");
    public static final RegistryKey<ItemGroup> KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), ID);
    public static final String translationKey = "itemGroup.bards_rpg.general";
    public static ItemGroup BARDS;
}
