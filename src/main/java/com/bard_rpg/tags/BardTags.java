package com.bard_rpg.tags;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import static com.bard_rpg.BardsMod.MOD_ID;

public class BardTags {
    public static final TagKey<Item> LUTES = register("lutes");
    public static final TagKey<Item> LYRES = register("lyres");
    public static final TagKey<Item> HARP_CROSSBOWS = register("harp_crossbows");
    public static final TagKey<Item> TWO_MODEL_INSTRUMENT = register("two_mode_instrument");

    private static TagKey<Item> register(String id) {
        return TagKey.of(RegistryKeys.ITEM, new Identifier(MOD_ID, id));
    }
}
