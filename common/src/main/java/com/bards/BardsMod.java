package com.bards;

import com.bards.block.BardBlocks;
import com.bards.config.BardItemConfig;
import com.bards.config.Default;
import com.bards.config.TweaksConfig;
import com.bards.content.BardParticles;
import com.bards.content.BardsSounds;
import com.bards.content.CustomSpellImpacts;
import com.bards.effect.BardsEffects;
import com.bards.item.Armors;
import com.bards.item.Group;
import com.bards.item.MusicDiscs;
import net.spell_engine.Platform;
import com.bards.worldgen.villages.BardVillagers;
import com.bards.item.Weapons;
import net.fabric_extras.structure_pool.api.StructurePoolAPI;
import net.fabric_extras.structure_pool.api.StructurePoolConfig;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.spell_engine.rpg_series.config.ConfigFile;
import net.tiny_config.ConfigManager;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.bards.compat.CompatLoadingCheck.armoryLoadCheck;

public final class BardsMod {
    public static final String MOD_ID = "bards_rpg";
    public static ConfigManager<BardItemConfig> itemConfig = new ConfigManager<BardItemConfig>
            ("equipment", Default.itemConfig)
            .builder()
            .setDirectory(MOD_ID)
            .sanitize(true)
            .build();

    public static ConfigManager<ConfigFile.Effects> effectsConfig = new ConfigManager<>
            ("effects", new ConfigFile.Effects())
            .builder()
            .setDirectory(MOD_ID)
            .sanitize(true)
            .build();

    public static ConfigManager<StructurePoolConfig> villageConfig = new ConfigManager<>
            ("villages", Default.villageConfig)
            .builder()
            .setDirectory(MOD_ID)
            .sanitize(true)
            .build();

    public static ConfigManager<TweaksConfig> tweaksConfig = new ConfigManager<>
            ("tweaks", new TweaksConfig())
            .builder()
            .setDirectory(MOD_ID)
            .sanitize(true)
            .build();

    public static void init() {
        itemConfig.refresh();
        effectsConfig.refresh();
        villageConfig.refresh();
        tweaksConfig.refresh();
        CustomSpellImpacts.registerCustomImpacts();
        if (Platform.util().isDevelopmentEnvironment()) {
            tweaksConfig.value.ignore_items_required_mods = true;
        }
        if (!Platform.util().isModLoaded("lithostitched")) {
            // Only inject the village if the Lithostitched is not present
            StructurePoolAPI.injectAll(BardsMod.villageConfig.value);
        }
    }

    public static void registerItems() {
        itemsToRegister().forEach((id, item) -> Registry.register(Registries.ITEM, id, item));
        registerItemGroup();
        itemConfig.save();
    }

    /// Creation only - Forge registers through the helper `RegisterEvent` hands out and iterates this
    /// from its own ITEM window. `config.save()` is a trailing side effect of the original
    /// `registerItems()` and stays with whoever writes the registry.
    public static Map<Identifier, Item> itemsToRegister() {
        if (itemConfig.value == null) itemConfig.value = new BardItemConfig();
        var items = new LinkedHashMap<Identifier, Item>();
        items.putAll(BardBlocks.itemsToRegister());
        items.putAll(Weapons.itemsToRegister(itemConfig.value.ranged_weapons, itemConfig.value.melee_weapons));
        items.putAll(Armors.itemsToRegister(itemConfig.value.armor_sets));
        items.putAll(MusicDiscs.itemsToRegister());
        return items;
    }

    /// `creative_mode_tab` is `RegisterEvent` 65 while `item` is 7, so on Forge this gets its own window.
    /// The icon supplier is lazy, so the group can be built after the items it points at.
    public static void registerItemGroup() {
        createItemGroup();
        Registry.register(Registries.ITEM_GROUP, Group.KEY, Group.BARDS);
    }

    public static void createItemGroup() {
        if (Group.BARDS != null) { return; }
        Group.BARDS = new ItemGroup.Builder(ItemGroup.Row.TOP, 0)
                .icon(() -> new ItemStack(Armors.troubadourArmorSet.armorSet().head))
                .displayName(Text.translatable("itemGroup.bards_rpg.general"))
                .build();
    }

    public static void registerEffects() {
        BardsEffects.register(effectsConfig.value);
        effectsConfig.save();
    }

    /// Creation only - see {@link #itemsToRegister()}. `effectsConfig.save()` is the trailing side
    /// effect of `registerEffects()` and stays with whoever writes the registry.
    public static Map<Identifier, StatusEffect> effectsToRegister() {
        return BardsEffects.effectsToRegister(effectsConfig.value);
    }
    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    public static void registerBlocks() {
        BardBlocks.register();
    }


    public static void registerSounds() {
        BardsSounds.register();
    }

    public static void registerParticles() {
        BardParticles.register();
    }

    public static void registerVillagers() {
        BardVillagers.registerVillagers();
    }
}
