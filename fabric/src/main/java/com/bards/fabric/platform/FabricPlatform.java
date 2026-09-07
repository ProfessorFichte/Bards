package com.bards.fabric.platform;

import com.bards.platform.Platform;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;

import java.util.function.Consumer;

public final class FabricPlatform implements Platform {
    @Override
    public void onItemGroupModify(RegistryKey<ItemGroup> group, Consumer<Entries> modifier) {
        ItemGroupEvents.modifyEntriesEvent(group).register(content -> modifier.accept(new Entries() {
            @Override
            public void add(ItemStack stack) {
                content.add(stack);
            }

            @Override
            public void removeByItem(Item item) {
                content.getDisplayStacks().removeIf(stack -> stack.isOf(item));
                content.getSearchTabStacks().removeIf(stack -> stack.isOf(item));
            }
        }));
    }
}
