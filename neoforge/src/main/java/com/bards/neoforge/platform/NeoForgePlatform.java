package com.bards.neoforge.platform;

import com.bards.platform.Platform;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class NeoForgePlatform implements Platform {
    private static final Map<RegistryKey<ItemGroup>, List<Consumer<Entries>>> itemGroupModifiers = new HashMap<>();

    @Override
    public void onItemGroupModify(RegistryKey<ItemGroup> group, Consumer<Entries> modifier) {
        itemGroupModifiers.computeIfAbsent(group, key -> new ArrayList<>()).add(modifier);
    }

    public static void dispatchItemGroup(BuildCreativeModeTabContentsEvent event) {
        var modifiers = itemGroupModifiers.get(event.getTabKey());
        if (modifiers == null) return;
        var entries = new Entries() {
            @Override
            public void add(ItemStack stack) {
                event.add(stack);
            }

            @Override
            public void removeByItem(Item item) {
                for (var stack : new ArrayList<>(event.getParentEntries())) {
                    if (stack.isOf(item)) event.remove(stack, ItemGroup.StackVisibility.PARENT_TAB_ONLY);
                }
                for (var stack : new ArrayList<>(event.getSearchEntries())) {
                    if (stack.isOf(item)) event.remove(stack, ItemGroup.StackVisibility.SEARCH_TAB_ONLY);
                }
            }
        };
        for (var modifier : modifiers) modifier.accept(entries);
    }

}
