package com.bard_rpg;

import com.bard_rpg.config.BardItemConfig;
import com.bard_rpg.config.Default;
import com.bard_rpg.config.TweaksConfig;
import com.bard_rpg.config.EffectsConfig;
import com.bard_rpg.block.BardBlocks;
import com.bard_rpg.content.BardsSounds;
import com.bard_rpg.content.CustomSpellImpacts;
import com.bard_rpg.effect.BardsEffects;
import com.bard_rpg.item.Armors;
import com.bard_rpg.item.Group;
import com.bard_rpg.item.Weapons;
import com.bard_rpg.worldgen.villages.BardVillagerProfessions;
import com.bard_rpg.worldgen.villages.BardVillagerTrades;
import com.bard_rpg.worldgen.villages.LuthierSongs;
import net.fabric_extras.structure_pool.api.StructurePoolConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.spell_engine.api.item.trinket.SpellBooks;
import net.tinyconfig.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BardsMod implements ModInitializer {
    public static final String MOD_ID = "bards_rpg";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static ConfigManager<BardItemConfig> itemConfig = new ConfigManager<BardItemConfig>
            ("equipment", Default.itemConfig)
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

    public static ConfigManager<EffectsConfig> effectsConfig = new ConfigManager<EffectsConfig>
            ("effects", new EffectsConfig())
            .builder()
            .setDirectory(MOD_ID)
            .sanitize(true)
            .build();

    public static ConfigManager<StructurePoolConfig> villagesConfig = new ConfigManager<>
            ("villages", Default.villageConfig)
            .builder()
            .setDirectory(MOD_ID)
            .sanitize(true)
            .build();

    @Override
    public void onInitialize() {
        itemConfig.refresh();
        tweaksConfig.refresh();
        effectsConfig.refresh();
        villagesConfig.refresh();
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            tweaksConfig.value.ignore_items_required_mods = true;
        }
        BardsSounds.register();
        BardsEffects.register();
        BardBlocks.register();
        Group.BARDS = FabricItemGroup.builder()
                .icon(() -> new ItemStack(Armors.troubadourArmorSet.head))
                .displayName(Text.translatable(Group.translationKey))
                .build();
        Registry.register(Registries.ITEM_GROUP, Group.KEY, Group.BARDS);
        SpellBooks.createAndRegister(new Identifier(MOD_ID, "bard"), Group.KEY);
        SpellBooks.createAndRegister(new Identifier(MOD_ID, "bard2"), Group.KEY);
        Weapons.register(itemConfig.value.ranged_weapons, itemConfig.value.melee_weapons);
        Armors.register(itemConfig.value.armor_sets);
        itemConfig.save();
        CustomSpellImpacts.register();
        BardVillagerProfessions.registerPoiTypes();
        BardVillagerProfessions.registerProfessions();
        LuthierSongs.init();
        BardVillagerTrades.registerSchedule();
        BardVillagerTrades.registerTrades();
        tweaksConfig.save();
        effectsConfig.save();
    }
}
