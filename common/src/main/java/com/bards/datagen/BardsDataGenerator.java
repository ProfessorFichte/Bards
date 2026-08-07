package com.bards.datagen;

import com.bards.block.BardBlocks;
import com.bards.content.BardsSounds;
import com.bards.content.BardsSpells;
import com.bards.effect.BardsEffects;
import com.bards.item.Armors;
import com.bards.item.Group;
import com.bards.item.MusicDiscs;
import com.bards.item.Weapons;
import com.bards.tags.BardTags;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.spell_engine.api.datagen.SimpleSoundGeneratorV2;
import net.spell_engine.api.datagen.SpellGenerator;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.api.tags.SpellTags;
import net.spell_engine.rpg_series.item.Armor;
import net.spell_engine.rpg_series.item.Weapon;
import net.spell_engine.api.tags.SpellEngineItemTags;
import net.spell_engine.rpg_series.datagen.RPGSeriesDataGen;
import net.spell_engine.rpg_series.tags.RPGSeriesItemTags;
import net.spell_power.api.SpellPowerTags;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.bards.BardsMod.MOD_ID;

public class BardsDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        BardVanillaAdvancementProvider.init();
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(SpellGen::new);
        pack.addProvider(LangGenerator::new);
        pack.addProvider(ItemTagGenerator::new);
        pack.addProvider(ModModelProvider::new);
        pack.addProvider(BardRecipeProvider::new);
        pack.addProvider(BardSmithingRecipeProvider::new);
        pack.addProvider(WeaponAttributesGenerator::new);
        pack.addProvider(BardAdvancementProvider::new);
        pack.addProvider(BardVanillaAdvancementProvider::new);
        pack.addProvider(SoundGen::new);
        pack.addProvider(SpellTagGenerator::new);
        pack.addProvider(UnsmeltGenerator::new);
    }
    public static class SpellGen extends SpellGenerator {
        public SpellGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(dataOutput, registryLookup);
        }

        @Override
        public void generateSpells(Builder builder) {
            for (var entry: BardsSpells.entries) {
                builder.add(entry.id(), entry.spell());
            }
        }
    }
    public static class LangGenerator extends FabricLanguageProvider {
        protected LangGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(dataOutput, "en_us", registryLookup);
        }

        @Override
        public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, FabricLanguageProvider.TranslationBuilder translationBuilder) {
            translationBuilder.add(Group.translationKey, "Bards");
            BardBlocks.all.forEach(entry ->
                    translationBuilder.add(entry.block().getTranslationKey(), entry.translation())
            );
            translationBuilder.add("entity.minecraft.villager.bards_rpg.luthier", "Luthier");

            translationBuilder.add("item." + MOD_ID + ".spell_book/bard", "Bard's Stories");
            translationBuilder.add("item.bards_rpg.spell_book/bard.spell_binding.description",
                    "Spell Book of Bards, using different instruments to play powerful music. The Bard improves stats and abilities of himself and party members and makes his enemies weaker .\n- Strengths: Mobility and Stat Enhancements.\n- Weaknesses: Low Armor & Damage\n- Equipment: Light Armor");
            translationBuilder.add("item." + MOD_ID + ".spell_scroll/bard", "Bard Ballad");

            translationBuilder.add("item.bards_rpg.bard_spell_book","");
            translationBuilder.add("item.bards_rpg.bard_spell_scroll","");
            Weapons.meleeEntries.forEach(entry ->
                    translationBuilder.add(entry.item().getTranslationKey(), entry.translatedName())
            );
            Weapons.rangedEntries.forEach(entry ->
                    translationBuilder.add(entry.item().getTranslationKey(), entry.translatedName())
            );
            translationBuilder.add(com.bards.item.HarpCrossbowItem.TOOLTIP_KEY,
                    "§7Half Musical Instrument and fully deadly weapon, the Harp Crossbow shoots multiple arrows");
            MusicDiscs.all.forEach(entry -> {
                translationBuilder.add(entry.item().getTranslationKey(), "Music Disc");
                translationBuilder.add("jukebox_song." + MOD_ID + "." + entry.name(), MusicDiscs.titleCase(entry.name()));
            });
            BardsSpells.entries.stream().filter(entry -> !entry.id().getPath().startsWith("helper/")).forEach(entry -> {
                var id = entry.id();
                translationBuilder.add("spell." + id.getNamespace() + "." + id.getPath() + ".name" , entry.title());
                translationBuilder.add("spell." + id.getNamespace() + "." + id.getPath() + ".description" , entry.description());
            });
            Armors.entries.forEach(entry -> {
                var translations = new LinkedHashMap<String, String>();
                translations.put(((Item)entry.armorSet().head).getTranslationKey(), entry.armorSet().headTranslation);
                translations.put(((Item)entry.armorSet().chest).getTranslationKey(), entry.armorSet().chestTranslation);
                translations.put(((Item)entry.armorSet().legs).getTranslationKey(), entry.armorSet().legsTranslation);
                translations.put(((Item)entry.armorSet().feet).getTranslationKey(), entry.armorSet().feetTranslation);
                for (var armorEntry: translations.entrySet()) {
                    translationBuilder.add(armorEntry.getKey(), armorEntry.getValue());
                }
            });
            BardsEffects.entries.forEach(entry -> {
                translationBuilder.add(entry.effect.getTranslationKey(), entry.title);
                translationBuilder.add(entry.effect.getTranslationKey() + ".description", entry.description);
            });
            // Advancements
            for (var entry : BardAdvancementProvider.getEntries()) {
                translationBuilder.add(entry.titleKey(), entry.title());
                translationBuilder.add(entry.descriptionKey(), entry.description());
            }
            for (var entry : BardVanillaAdvancementProvider.getEntries()) {
                translationBuilder.add(entry.titleKey(), entry.title());
                translationBuilder.add(entry.descriptionKey(), entry.description());
            }
            // Equipment Set
            translationBuilder.add("equipment_set." + MOD_ID + ".storyteller", "The Storyteller");
            ///Entities
            translationBuilder.add("entity.minecraft.villager.luthier", "Luthier");
            translationBuilder.add("entity.minecraft.villager:luthier", "Luthier");
        }
    }

    public static class ItemTagGenerator extends RPGSeriesDataGen.ItemTagGenerator {
        public ItemTagGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(dataOutput, registryLookup);
        }
        public void armoryTags(List<Armor.Entry> armors) {
            this.armoryTags(armors, EnumSet.noneOf(RPGSeriesItemTags.ArmorMetaType.class));
        }

        public void armoryTags(List<Armor.Entry> armors, RPGSeriesItemTags.ArmorMetaType metaType) {
            this.armoryTags(armors, EnumSet.of(metaType));
        }

        public void armoryTags(List<Armor.Entry> armors, EnumSet<RPGSeriesItemTags.ArmorMetaType> metaTypes) {
            Iterator var3 = armors.iterator();

            while(var3.hasNext()) {
                Armor.Entry armor = (Armor.Entry)var3.next();
                Armor.Set set = armor.armorSet();
                FabricTagProvider<Item>.FabricTagBuilder headTag = this.getOrCreateTagBuilder(ItemTags.HEAD_ARMOR);
                headTag.addOptional(set.idOf(set.head));
                FabricTagProvider<Item>.FabricTagBuilder chestTag = this.getOrCreateTagBuilder(ItemTags.CHEST_ARMOR);
                chestTag.addOptional(set.idOf(set.chest));
                FabricTagProvider<Item>.FabricTagBuilder legsTag = this.getOrCreateTagBuilder(ItemTags.LEG_ARMOR);
                legsTag.addOptional(set.idOf(set.legs));
                FabricTagProvider<Item>.FabricTagBuilder feetTag = this.getOrCreateTagBuilder(ItemTags.FOOT_ARMOR);
                feetTag.addOptional(set.idOf(set.feet));
                Iterator var12;

                String lootTheme = armor.lootProperties().theme();
                if (lootTheme != null && !lootTheme.isEmpty()) {
                    FabricTagProvider<Item>.FabricTagBuilder themeTag = this.getOrCreateTagBuilder(RPGSeriesItemTags.LootThemes.get(lootTheme));
                    Iterator var19 = armor.armorSet().pieceIds().iterator();

                    while(var19.hasNext()) {
                        Object id = var19.next();
                        themeTag.addOptional((Identifier)id);
                    }
                }

                var12 = metaTypes.iterator();

                while(var12.hasNext()) {
                    RPGSeriesItemTags.ArmorMetaType metaType = (RPGSeriesItemTags.ArmorMetaType)var12.next();
                    FabricTagProvider<Item>.FabricTagBuilder metaTag = this.getOrCreateTagBuilder(RPGSeriesItemTags.ArmorType.get(metaType));
                    Iterator var15 = armor.armorSet().pieceIds().iterator();

                    while(var15.hasNext()) {
                        Object id = var15.next();
                        metaTag.addOptional((Identifier)id);
                    }
                }
            }

        }

        public void generateBardWeaponTags(List<Weapon.Entry> weapons, TagKey tagKey) {
            Iterator var2 = weapons.iterator();
            while(var2.hasNext()) {
                Weapon.Entry weapon = (Weapon.Entry)var2.next();
                FabricTagProvider<Item>.FabricTagBuilder tag = this.getOrCreateTagBuilder(tagKey);
                tag.addOptional(weapon.id());
                int tier = weapon.lootProperties().tier();
                if (tier >= 0) {
                    FabricTagProvider<Item>.FabricTagBuilder tierTag = this.getOrCreateTagBuilder(RPGSeriesItemTags.LootTiers.get(tier, RPGSeriesItemTags.LootCategory.WEAPONS));
                    tierTag.addOptional(weapon.id());
                }
                String lootTheme = weapon.lootProperties().theme();
                if (lootTheme != null && !lootTheme.isEmpty()) {
                    FabricTagProvider<Item>.FabricTagBuilder themeTag = this.getOrCreateTagBuilder(RPGSeriesItemTags.LootThemes.get(lootTheme));
                    themeTag.addOptional(weapon.id());
                }
            }
        }

        List<String> armoryKeywords = List.of("storyteller");
        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            var armorTagOptions1 = new ArmorOptions(false, true);
            var armorTagOptions2 = new ArmorOptions(true, true);
            armoryTags(
                    Armors.entries.stream().filter(entry -> armoryKeywords.stream().anyMatch(entry.name()::contains)).toList(),
                    RPGSeriesItemTags.ArmorMetaType.MAGIC
            );
            generateArmorTags(
                    Armors.entries.stream().filter(entry -> armoryKeywords.stream().noneMatch(entry.name()::contains)).toList(),
                    RPGSeriesItemTags.ArmorMetaType.MAGIC,
                    armorTagOptions2
            );
            generateWeaponTags(Weapons.meleeEntries.stream()
                    .filter(entry -> entry.name().toLowerCase().contains("rapier"))
                    .toList());
            generateBardWeaponTags(
                    Weapons.meleeEntries.stream()
                            .filter(entry -> entry.name().toLowerCase().contains("lute"))
                            .toList(),
                    BardTags.LUTES
            );
            generateBardWeaponTags(
                    Weapons.meleeEntries.stream()
                            .filter(entry -> entry.name().toLowerCase().contains("lyre"))
                            .toList(),
                    BardTags.LYRES
            );
            var rangedEntries = Weapons.rangedEntries.stream().map(entry ->
                    new RPGSeriesDataGen.BowEntry(entry.id(), entry.category, entry.lootProperties)
            ).toList();
            generateBowTags(rangedEntries);

            var twoModels = getOrCreateTagBuilder(BardTags.TWO_MODEL_INSTRUMENT);
            twoModels.addOptionalTag(BardTags.LUTES);
            twoModels.addOptionalTag(BardTags.LYRES);

            var harpCrossbowTag = getOrCreateTagBuilder(BardTags.HARP_CROSSBOWS);
            Weapons.rangedEntries.forEach(entry -> harpCrossbowTag.addOptional(entry.id()));

            var spellInfinityTag = getOrCreateTagBuilder(SpellEngineItemTags.ENCHANTABLE_SPELL_INFINITY);
            spellInfinityTag.addOptionalTag(BardTags.LUTES);
            spellInfinityTag.addOptionalTag(BardTags.LYRES);
            spellInfinityTag.addOptionalTag(BardTags.HARP_CROSSBOWS);
            var spellHasteTag = getOrCreateTagBuilder(SpellPowerTags.Items.Enchantable.HASTE);
            spellHasteTag.addOptionalTag(BardTags.LUTES);
            spellHasteTag.addOptionalTag(BardTags.LYRES);
            spellHasteTag.addOptionalTag(BardTags.HARP_CROSSBOWS);
            var criticalDamageTag  = getOrCreateTagBuilder(SpellPowerTags.Items.Enchantable.CRITICAL_DAMAGE);
            criticalDamageTag.addOptionalTag(BardTags.LUTES);
            criticalDamageTag.addOptionalTag(BardTags.LYRES);
            criticalDamageTag.addOptionalTag(BardTags.HARP_CROSSBOWS);
            var criticalChanceTag  = getOrCreateTagBuilder(SpellPowerTags.Items.Enchantable.CRITICAL_CHANCE);
            criticalChanceTag.addOptionalTag(BardTags.LUTES);
            criticalChanceTag.addOptionalTag(BardTags.LYRES);
            criticalChanceTag.addOptionalTag(BardTags.HARP_CROSSBOWS);
            var spellPowerTag  = getOrCreateTagBuilder(SpellPowerTags.Items.Enchantable.SPELL_POWER_GENERIC);
            spellPowerTag.addOptionalTag(BardTags.LUTES);
            spellPowerTag.addOptionalTag(BardTags.LYRES);
            spellPowerTag.addOptionalTag(BardTags.HARP_CROSSBOWS);
            var unbreakingTag = getOrCreateTagBuilder(ItemTags.DURABILITY_ENCHANTABLE);
            unbreakingTag.addOptionalTag(BardTags.LUTES);
            unbreakingTag.addOptionalTag(BardTags.LYRES);
        }
    }

    public static class SoundGen extends SimpleSoundGeneratorV2 {
        public SoundGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(dataOutput, registryLookup);
        }

        @Override
        public void generateSounds(Builder builder) {
            builder.entries.add(new Entry(MOD_ID,
                            BardsSounds.entries.stream()
                                    .map(entry -> SoundEntry.withVariants(entry.id().getPath(), entry.variants()))
                                    .toList()
                    )
            );
        }
    }

    public static class SpellTagGenerator extends FabricTagProvider<Spell> {
        public SpellTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, SpellRegistry.KEY, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            var namespace = MOD_ID;
            var treasureTagBuilder = getOrCreateTagBuilder(SpellTags.TREASURE);
            var processedBooks = new HashSet<BardsSpells.Book>();
            BardsSpells.entries.forEach(entry -> {
                if (entry.book() != null) {
                    var bookTagKey = SpellTags.spellBook(namespace, entry.book().toString().toLowerCase());
                    var bookTag = getOrCreateTagBuilder(bookTagKey);
                    bookTag.addOptional(entry.id());
                    var scrollTagKey = SpellTags.spellScroll(namespace, entry.book().toString().toLowerCase());
                    var scrollTag = getOrCreateTagBuilder(scrollTagKey);
                    scrollTag.addOptional(entry.id());
                    if (processedBooks.add(entry.book())) {
                        treasureTagBuilder.addOptionalTag(scrollTagKey);
                    }
                }
                for (var group : entry.weaponGroups()) {
                    var weaponGroupTagKey = SpellTags.weapon(namespace, group.toString().toLowerCase());
                    var weaponGroupTag = getOrCreateTagBuilder(weaponGroupTagKey);
                    weaponGroupTag.addOptional(entry.id());
                }
            });

            var songsTagKey = TagKey.of(SpellRegistry.KEY, Identifier.of(MOD_ID, "songs"));
            var songsTag = getOrCreateTagBuilder(songsTagKey);
            songsTag.addOptionalTag(Identifier.of(MOD_ID, "weapon/lyre"));
            songsTag.addOptionalTag(Identifier.of(MOD_ID, "weapon/lute"));

            var dragonLuteKey = TagKey.of(SpellRegistry.KEY, Identifier.of(MOD_ID, "weapon/dragon_lute"));
            var dragonLuteTag = getOrCreateTagBuilder(dragonLuteKey);
            dragonLuteTag.addOptionalTag(Identifier.of(MOD_ID, "weapon/lute"));
            var spellthiefLuteKey = TagKey.of(SpellRegistry.KEY, Identifier.of(MOD_ID, "weapon/spellthief_lute"));
            var spellthiefTag = getOrCreateTagBuilder(spellthiefLuteKey);
            spellthiefTag.addOptionalTag(Identifier.of(MOD_ID, "weapon/lute"));
            var rubyLuteKey = TagKey.of(SpellRegistry.KEY, Identifier.of(MOD_ID, "weapon/ruby_verdict_lute"));
            var rubyTag = getOrCreateTagBuilder(rubyLuteKey);
            rubyTag.addOptionalTag(Identifier.of(MOD_ID, "weapon/lute"));

            var oceanLyreKey = TagKey.of(SpellRegistry.KEY, Identifier.of(MOD_ID, "weapon/ocean_lyre"));
            var oceanLyreTag = getOrCreateTagBuilder(oceanLyreKey);
            oceanLyreTag.addOptionalTag(Identifier.of(MOD_ID, "weapon/lyre"));
            var apolloLyreKey = TagKey.of(SpellRegistry.KEY, Identifier.of(MOD_ID, "weapon/apollo_lyre"));
            var apolloLyreTag = getOrCreateTagBuilder(apolloLyreKey);
            apolloLyreTag.addOptionalTag(Identifier.of(MOD_ID, "weapon/lyre"));
            var antecaelLyreKey = TagKey.of(SpellRegistry.KEY, Identifier.of(MOD_ID, "weapon/antecael_lyre"));
            var antecaelLyreTag = getOrCreateTagBuilder(antecaelLyreKey);
            antecaelLyreTag.addOptionalTag(Identifier.of(MOD_ID, "weapon/lyre"));

        }
    }
    public static class UnsmeltGenerator extends FabricRecipeProvider {
        public UnsmeltGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        public static int UNSMELT_TIME = 300;

        @Override
        public void generate(RecipeExporter exporter) {
            disassembleArmor(exporter, Armors.entertainerArmorSet.armorSet(), Items.LEATHER);
            disassembleArmor(exporter, Armors.troubadourArmorSet.armorSet(), Items.GOLD_NUGGET);
            disassembleArmor(exporter, Armors.netheriteTroubadourArmorSet.armorSet(), Items.NETHERITE_SCRAP);

            disassemble(exporter,
                    Weapons.meleeEntries.stream()
                            .filter(entry -> entry.id().getPath().contains("gold"))
                            .map(entry -> (ItemConvertible) entry.item()).toList(),
                    Items.GOLD_NUGGET);
            disassemble(exporter,
                    Weapons.meleeEntries.stream()
                            .filter(entry -> entry.id().getPath().contains("iron"))
                            .map(entry -> (ItemConvertible) entry.item()).toList(),
                    Items.IRON_NUGGET);
            disassemble(exporter,
                    Weapons.meleeEntries.stream()
                            .filter(entry -> entry.id().getPath().contains("netherite"))
                            .map(entry -> (ItemConvertible) entry.item()).toList(),
                    Items.NETHERITE_SCRAP);
            disassemble(exporter,
                    Weapons.rangedEntries.stream()
                            .filter(entry -> entry.id().getPath().contains("diamond"))
                            .map(entry -> (ItemConvertible) entry.item()).toList(),
                    Items.GOLD_NUGGET);
            disassemble(exporter,
                    Weapons.rangedEntries.stream()
                            .filter(entry -> entry.id().getPath().contains("netherite"))
                            .map(entry -> (ItemConvertible) entry.item()).toList(),
                    Items.NETHERITE_SCRAP);
        }

        private static void disassembleArmor(RecipeExporter exporter, Armor.Set armorSet, Item output) {
            FabricRecipeProvider.offerSmelting(exporter,
                    armorSet.pieces(),
                    RecipeCategory.MISC,
                    output,
                    0.1f,
                    UNSMELT_TIME,
                    "disassemble"
            );
            FabricRecipeProvider.offerBlasting(exporter,
                    armorSet.pieces(),
                    RecipeCategory.MISC,
                    output,
                    0.1f,
                    UNSMELT_TIME / 2,
                    "disassemble"
            );
        }

        private static void disassemble(RecipeExporter exporter, List<ItemConvertible> items, Item output) {
            FabricRecipeProvider.offerSmelting(exporter,
                    items,
                    RecipeCategory.MISC,
                    output,
                    0.1f,
                    UNSMELT_TIME,
                    "disassemble"
            );
            FabricRecipeProvider.offerBlasting(exporter,
                    items,
                    RecipeCategory.MISC,
                    output,
                    0.1f,
                    UNSMELT_TIME / 2,
                    "disassemble"
            );
        }
    }
}
