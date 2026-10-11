package com.bards.tags;

import com.bards.BardsMod;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class BardTags {
    public static final TagKey<Item> LUTES = register("lutes");
    public static final TagKey<Item> LYRES = register("lyres");
    public static final TagKey<Item> RAPIERS = register("rapiers");
    public static final TagKey<Item> HARP_CROSSBOWS = register("harp_crossbows");
    public static final TagKey<Item> TWO_MODEL_INSTRUMENT = register("two_mode_instrument");

    private static TagKey<Item> register(String id) {
        return TagKey.of(RegistryKeys.ITEM, BardsMod.id(id));
    }
}
