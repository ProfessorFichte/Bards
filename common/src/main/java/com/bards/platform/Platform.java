package com.bards.platform;

import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public interface Platform {
    AtomicReference<Platform> HOLDER = new AtomicReference<>();

    static void set(Platform platform) {
        HOLDER.set(platform);
    }

    static Platform get() {
        var platform = HOLDER.get();
        if (platform == null) {
            throw new IllegalStateException("Platform.set(...) must be called before Platform.get()");
        }
        return platform;
    }

    void onItemGroupModify(RegistryKey<ItemGroup> group, Consumer<Entries> modifier);

    interface Entries {
        void add(ItemStack stack);
        default void add(ItemConvertible item) { add(new ItemStack(item)); }
        void removeByItem(Item item);
    }
}
