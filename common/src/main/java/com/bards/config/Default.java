package com.bards.config;

import net.fabric_extras.structure_pool.api.StructurePoolConfig;
import net.spell_engine.api.config.ConfigFile;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Default {
    public final static BardItemConfig itemConfig;
    public final static StructurePoolConfig villageConfig;
    static {
        itemConfig = new BardItemConfig();
        villageConfig = new StructurePoolConfig();
        var weight = 3;
        var limit = 1;
        villageConfig.entries.addAll(List.of(
                new StructurePoolConfig.Entry("minecraft:village/desert/houses", "bards_rpg:village/desert/pub", weight, limit),
                new StructurePoolConfig.Entry("minecraft:village/savanna/houses", "bards_rpg:village/savanna/pub", weight, limit),
                new StructurePoolConfig.Entry("minecraft:village/plains/houses", "bards_rpg:village/plains/pub", weight, limit),
                new StructurePoolConfig.Entry("minecraft:village/taiga/houses", "bards_rpg:village/taiga/pub", weight, limit),
                new StructurePoolConfig.Entry("minecraft:village/snowy/houses", "bards_rpg:village/snowy/pub", weight, limit)
        ));
    }

    @SafeVarargs
    private static <T> List<T> joinLists(List<T>... lists) {
        return Arrays.stream(lists).flatMap(Collection::stream).collect(Collectors.toList());
    }
}
