package com.bards.datagen;

import com.bards.block.BardBlocks;
import com.bards.item.Armors;
import com.bards.item.Weapons;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

import static com.bards.BardsMod.MOD_ID;

public class BardRecipeProvider extends FabricRecipeProvider {

    private static Item getOrFallback(Identifier id, Item fallback) {
        var item = Registries.ITEM.get(id);
        return item != null && item != Items.AIR ? item : fallback;
    }

    public BardRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public String getName() {
        return "Crafting Recipes (" + MOD_ID + ")";
    }

    @Override
    public void generate(RecipeExporter exporter) {
        // ==========================================
        // BLOCKS
        // ==========================================
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, BardBlocks.MUSIC_STAND.item())
                .pattern("G G")
                .pattern("SBS")
                .pattern(" T ")
                .input('T', Items.STICK)
                .input('S', Items.STRING)
                .input('G', Items.GOLD_INGOT)
                .input('B', Items.BOOK)
                .criterion(hasItem(Items.BOOK), conditionsFromItem(Items.BOOK))
                .offerTo(exporter, Identifier.of(MOD_ID, "music_stand_block"));
        // ==========================================
        // RAPIERS
        // ==========================================
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, Weapons.iron_rapier.item())
                .pattern("  W")
                .pattern(" W ")
                .pattern("RW ")
                .input('W', Items.IRON_INGOT)
                .input('R', Items.LEATHER)
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .offerTo(exporter, Identifier.of(MOD_ID, "iron_rapier"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, Weapons.golden_rapier.item())
                .pattern("  W")
                .pattern(" W ")
                .pattern("RW ")
                .input('W', Items.GOLD_INGOT)
                .input('R', Items.LEATHER)
                .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                .offerTo(exporter, Identifier.of(MOD_ID, "golden_rapier"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, Weapons.diamond_rapier.item())
                .pattern("  W")
                .pattern(" W ")
                .pattern("RW ")
                .input('W', Items.DIAMOND)
                .input('R', Items.LEATHER)
                .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                .offerTo(exporter, Identifier.of(MOD_ID, "diamond_rapier"));
        createConditionalShapedRecipe(exporter, "aeternium_rapier",
                RecipeCategory.COMBAT,
                "bards_rpg:aeternium_rapier",
                new String[]{"  W", " W ", "RW "},
                'W', "betterend:aeternium_ingot",
                'R', "minecraft:leather",
                "betterend");

        // ==========================================
        // LUTES
        // ==========================================
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, Weapons.wooden_lute.item())
                .pattern(" AR")
                .pattern("WRA")
                .pattern("WW ")
                .input('W', Items.BIRCH_PLANKS)
                .input('R', Items.STRING)
                .input('A', Items.STICK)
                .criterion(hasItem(Items.STRING), conditionsFromItem(Items.STRING))
                .offerTo(exporter, Identifier.of(MOD_ID, "wooden_lute"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, Weapons.diamond_lute.item())
                .pattern(" AR")
                .pattern("DRA")
                .pattern("DD ")
                .input('D', Items.DIAMOND)
                .input('R', Items.STRING)
                .input('A', Items.GOLD_INGOT)
                .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                .offerTo(exporter, Identifier.of(MOD_ID, "diamond_lute"));

        // ==========================================
        // LYRES
        // ==========================================
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, Weapons.golden_lyre.item())
                .pattern("WRW")
                .pattern("WRW")
                .pattern(" W ")
                .input('W', Items.GOLD_INGOT)
                .input('R', Items.STRING)
                .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                .offerTo(exporter, Identifier.of(MOD_ID, "golden_lyre"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, Weapons.diamond_lyre.item())
                .pattern("DRD")
                .pattern("DRD")
                .pattern(" D ")
                .input('D', Items.DIAMOND)
                .input('R', Items.STRING)
                .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                .offerTo(exporter, Identifier.of(MOD_ID, "diamond_lyre"));
        createConditionalShapedRecipe(exporter, "aeternium_lyre",
                RecipeCategory.COMBAT,
                "bards_rpg:aeternium_lyre",
                new String[]{"WRW", "WRW", " W "},
                'W', "betterend:aeternium_ingot",
                'R', "betterend:crystal_shards",
                "betterend");

        // ==========================================
        // HARP CROSSBOWS
        // ==========================================
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, Weapons.harp_crossbow.item())
                .pattern("GGG")
                .pattern("SSS")
                .pattern("C C")
                .input('G', Items.STICK)
                .input('S', Items.STRING)
                .input('C', Items.TRIPWIRE_HOOK)
                .criterion(hasItem(Items.TRIPWIRE_HOOK), conditionsFromItem(Items.TRIPWIRE_HOOK))
                .offerTo(exporter, Identifier.of(MOD_ID, "harp_crossbow"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, Weapons.diamond_harp_crossbow.item())
                .pattern("DGD")
                .pattern("SSS")
                .pattern("C C")
                .input('G', Items.GOLD_INGOT)
                .input('D', Items.DIAMOND)
                .input('S', Items.STRING)
                .input('C', Items.TRIPWIRE_HOOK)
                .criterion(hasItem(Items.TRIPWIRE_HOOK), conditionsFromItem(Items.TRIPWIRE_HOOK))
                .offerTo(exporter, Identifier.of(MOD_ID, "diamond_harp_crossbow"));
        //// ARMOR
        //HATS
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, (Item) Armors.entertainerArmorSet.armorSet().head)
                .pattern("  A")
                .pattern("BCB")
                .pattern("B B")
                .input('A', Items.FEATHER)
                .input('B', ItemTags.WOOL)
                .input('C', Items.PURPLE_DYE)
                .criterion(hasItem(Items.FEATHER), conditionsFromItem(Items.FEATHER))
                .offerTo(exporter, Identifier.of(Armors.entertainerArmorSet.armorSet().head.toString()));
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, (Item) Armors.troubadourArmorSet.armorSet().head)
                .pattern("  A")
                .pattern("BCB")
                .pattern("B B")
                .input('A', Items.FEATHER)
                .input('B', Items.RABBIT_HIDE)
                .input('C', Items.GOLD_INGOT)
                .criterion(hasItem(Items.RABBIT_HIDE), conditionsFromItem(Items.RABBIT_HIDE))
                .offerTo(exporter, Identifier.of(Armors.troubadourArmorSet.armorSet().head.toString()));
        //CHESTS
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, (Item) Armors.entertainerArmorSet.armorSet().chest)
                .pattern("A A")
                .pattern("BBB")
                .pattern("CAC")
                .input('A', Items.LEATHER)
                .input('B', ItemTags.WOOL)
                .input('C', Items.PURPLE_DYE)
                .criterion(hasItem(Items.LEATHER), conditionsFromItem(Items.LEATHER))
                .offerTo(exporter, Identifier.of( Armors.entertainerArmorSet.armorSet().chest.toString()));
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, (Item) Armors.troubadourArmorSet.armorSet().chest)
                .pattern("A A")
                .pattern("BBB")
                .pattern("CAC")
                .input('A', Items.RABBIT_HIDE)
                .input('B', ItemTags.WOOL)
                .input('C', Items.GOLD_INGOT)
                .criterion(hasItem(Items.RABBIT_HIDE), conditionsFromItem(Items.RABBIT_HIDE))
                .offerTo(exporter, Identifier.of(Armors.troubadourArmorSet.armorSet().chest.toString()));
        //LEGS
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, (Item) Armors.entertainerArmorSet.armorSet().legs)
                .pattern("BAB")
                .pattern("C C")
                .pattern("A A")
                .input('A', Items.LEATHER)
                .input('B', ItemTags.WOOL)
                .input('C', Items.PURPLE_DYE)
                .criterion(hasItem(Items.LEATHER), conditionsFromItem(Items.LEATHER))
                .offerTo(exporter, Identifier.of(Armors.entertainerArmorSet.armorSet().legs.toString()));
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, (Item) Armors.troubadourArmorSet.armorSet().legs)
                .pattern("BAB")
                .pattern("C C")
                .pattern("A A")
                .input('A', Items.RABBIT_HIDE)
                .input('B', ItemTags.WOOL)
                .input('C', Items.GOLD_INGOT)
                .criterion(hasItem(Items.RABBIT_HIDE), conditionsFromItem(Items.RABBIT_HIDE))
                .offerTo(exporter, Identifier.of(Armors.troubadourArmorSet.armorSet().legs.toString()));
        //FEET
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, (Item) Armors.entertainerArmorSet.armorSet().feet)
                .pattern("C C")
                .pattern("A A")
                .input('A', Items.LEATHER)
                .input('C', Items.PURPLE_DYE)
                .criterion(hasItem(Items.LEATHER), conditionsFromItem(Items.LEATHER))
                .offerTo(exporter, Identifier.of(Armors.entertainerArmorSet.armorSet().feet.toString()));
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, (Item) Armors.troubadourArmorSet.armorSet().feet)
                .pattern("CBC")
                .pattern("A A")
                .input('A', Items.RABBIT_HIDE)
                .input('B', Items.GOLD_INGOT)
                .input('C', Items.LEATHER)
                .criterion(hasItem(Items.RABBIT_HIDE), conditionsFromItem(Items.RABBIT_HIDE))
                .offerTo(exporter, Identifier.of(Armors.troubadourArmorSet.armorSet().feet.toString()));
    }

    private void createConditionalShapedRecipe(RecipeExporter exporter, String name, RecipeCategory category,
                                               String resultId, String[] pattern,
                                               char key, String ingredientId, char key2, String ingredientId2, String requiredMod) {
        // The required mod (e.g. betterend) isn't present during datagen, so its items don't resolve here.
        // We fall back to a vanilla stand-in to build valid recipe JSON; the recipe only actually loads
        // at runtime (via the resource condition below) when the required mod - and its real items - are present.
        var conditionalExporter = withConditions(exporter, ResourceConditions.allModsLoaded(requiredMod));

        Item result = getOrFallback(Identifier.of(resultId), Items.BARRIER);
        Item ingredient = getOrFallback(Identifier.of(ingredientId), Items.NETHERITE_INGOT);
        Item ingredient2 = getOrFallback(Identifier.of(ingredientId2), Items.AMETHYST_SHARD);

        var builder = ShapedRecipeJsonBuilder.create(category, result);
        for (String row : pattern) {
            builder.pattern(row);
        }
        builder.input(key, ingredient)
                .input(key2, ingredient2)
                .criterion(hasItem(ingredient), conditionsFromItem(ingredient))
                .offerTo(conditionalExporter, Identifier.of(MOD_ID, name));
    }
}
