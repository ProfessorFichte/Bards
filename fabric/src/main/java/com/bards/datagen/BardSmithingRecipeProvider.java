package com.bards.datagen;

import com.bards.item.Armors;
import com.bards.item.Weapons;
import com.google.common.hash.Hashing;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.more_rpg_classes.datagen.SmithingRecipeGenerator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

import static com.bards.BardsMod.MOD_ID;

public class BardSmithingRecipeProvider extends SmithingRecipeGenerator {

    public BardSmithingRecipeProvider(FabricDataOutput output) {
        super(output, MOD_ID);
    }

    @Override
    public String getName() {
        return "Smithing Recipes (" + MOD_ID + ")";
    }

    @Override
    public void generate() {
        // ==========================================
        // NETHERITE UPGRADES
        // ==========================================
        // WEAPONS
        createSimpleSmithingRecipe(
                "netherite_rapier",
                Weapons.diamond_rapier.item(),
                Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
                Items.NETHERITE_INGOT,
                Weapons.netherite_rapier.item()
        );
        createSimpleSmithingRecipe(
                "netherite_lyre",
                Weapons.diamond_lyre.item(),
                Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
                Items.NETHERITE_INGOT,
                Weapons.netherite_lyre.item()
        );
        createSimpleSmithingRecipe(
                "netherite_lute",
                Weapons.diamond_lute.item(),
                Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
                Items.NETHERITE_INGOT,
                Weapons.netherite_lute.item()
        );
        createSimpleSmithingRecipe(
                "netherite_harp_crossbow",
                Weapons.harp_crossbow.item(),
                Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
                Items.NETHERITE_INGOT,
                Weapons.netherite_harp_crossbow.item()
        );
        // ====================
        // BETTERNETHER (Ruby)
        // ====================
        var rubyRapier = Weapons.meleeEntries.stream()
                .filter(e -> e.id().getPath().equals("ruby_rapier"))
                .findFirst().map(e -> e.item()).orElse(null);

        if (rubyRapier != null) {
            createSmithingTransformRecipe(
                    "ruby_rapier",
                    Weapons.netherite_rapier.item(),
                    Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
                    Identifier.of("betternether", "nether_ruby"),
                    rubyRapier,
                    "betternether"
            );
        }
        var rubyLute = Weapons.meleeEntries.stream()
                .filter(e -> e.id().getPath().equals("ruby_lute"))
                .findFirst().map(e -> e.item()).orElse(null);

        if (rubyLute != null) {
            createSmithingTransformRecipe(
                    "ruby_lute",
                    Weapons.netherite_lute.item(),
                    Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
                    Identifier.of("betternether", "nether_ruby"),
                    rubyLute,
                    "betternether"
            );
        }

        var rubyHarpCrossbow = Weapons.rangedEntries.stream()
                .filter(e -> e.id().getPath().equals("ruby_harp_crossbow"))
                .findFirst().map(e -> e.item()).orElse(null);

        if (rubyHarpCrossbow != null) {
            createSmithingTransformRecipe(
                    "ruby_harp_crossbow",
                    Weapons.netherite_harp_crossbow.item(),
                    Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
                    Identifier.of("betternether", "nether_ruby"),
                    rubyHarpCrossbow,
                    "betternether"
            );
        }
        /// ARMOR
        // ====================
        // ARMOR UPGRADES - TROUBADOUR TO Netherite TROUBADOUR
        // ====================
        createSimpleArmorSetUpgrade(
                "smithing",
                Armors.troubadourArmorSet.armorSet(),
                Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
                Items.NETHERITE_INGOT,
                Armors.netheriteTroubadourArmorSet.armorSet()
        );

        // ====================
        // ARMOR UPGRADES - Netherite Troubadour TO Storyteller
        // ====================

        if (Armors.storytellerArmorSet != null) {
            // From Netherite Troubadour to Storyteller
            Identifier upgradeCrystal = Identifier.of("more_rpg_classes", "virtuoso_upgrade_crystal");
            createSmithingTransformRecipe(
                    "smithing_storyteller_head_netherite_troubadour_head",
                    (Item) Armors.netheriteTroubadourArmorSet.armorSet().head,
                    Identifier.of("armory_rpgs", "epic_armor_upgrade"),
                    upgradeCrystal,
                    (Item) Armors.storytellerArmorSet.armorSet().head,
                    "armory_rpgs"
            );
            createSmithingTransformRecipe(
                    "smithing_storyteller_chest_netherite_troubadour_chest",
                    (Item) Armors.netheriteTroubadourArmorSet.armorSet().chest,
                    Identifier.of("armory_rpgs", "epic_armor_upgrade"),
                    upgradeCrystal,
                    (Item) Armors.storytellerArmorSet.armorSet().chest,
                    "armory_rpgs"
            );
            createSmithingTransformRecipe(
                    "smithing_storyteller_legs_netherite_troubadour_legs",
                    (Item) Armors.netheriteTroubadourArmorSet.armorSet().legs,
                    Identifier.of("armory_rpgs", "epic_armor_upgrade"),
                    upgradeCrystal,
                    (Item) Armors.storytellerArmorSet.armorSet().legs,
                    "armory_rpgs"
            );
            createSmithingTransformRecipe(
                    "smithing_storyteller_feet_netherite_troubadour_feet",
                    (Item) Armors.netheriteTroubadourArmorSet.armorSet().feet,
                    Identifier.of("armory_rpgs", "epic_armor_upgrade"),
                    upgradeCrystal,
                    (Item) Armors.storytellerArmorSet.armorSet().feet,
                    "armory_rpgs"
            );

            // ====================
            // LOOT N EXPLORE -UPGRADES
            // ====================
            var glacialrapier = Weapons.meleeEntries.stream()
                    .filter(e -> e.id().getPath().equals("glacial_rapier"))
                    .findFirst().map(e -> e.item()).orElse(null);

            if (glacialrapier != null) {
                createSmithingTransformRecipe(
                        "glacial_rapier",
                        Weapons.netherite_rapier.item(),
                        Identifier.of("loot_n_explore", "frostmonarch_upgrade_smithing_template"),
                        Identifier.of("loot_n_explore", "frozen_soul"),
                        glacialrapier,
                        "loot_n_explore"
                );
            }

            var elderGuardianrapier = Weapons.meleeEntries.stream()
                    .filter(e -> e.id().getPath().equals("elder_guardian_rapier"))
                    .findFirst().map(e -> e.item()).orElse(null);

            if (elderGuardianrapier != null) {
                createSmithingTransformRecipe(
                        "elder_guardian_rapier",
                        Weapons.netherite_rapier.item(),
                        Identifier.of("loot_n_explore", "guardian_upgrade_smithing_template"),
                        Identifier.of("loot_n_explore", "elder_guardian_eye"),
                        elderGuardianrapier,
                        "loot_n_explore"
                );
            }

            var elderGuardianLyre = Weapons.meleeEntries.stream()
                    .filter(e -> e.id().getPath().equals("elder_guardian_lyre"))
                    .findFirst().map(e -> e.item()).orElse(null);

            if (elderGuardianLyre != null) {
                createSmithingTransformRecipe(
                        "elder_guardian_lyre",
                        Weapons.netherite_lyre.item(),
                        Identifier.of("loot_n_explore", "guardian_upgrade_smithing_template"),
                        Identifier.of("loot_n_explore", "elder_guardian_eye"),
                        elderGuardianrapier,
                        "loot_n_explore"
                );
            }

            var elderGuardianHarpCrossbow = Weapons.rangedEntries.stream()
                    .filter(e -> e.id().getPath().equals("elder_guardian_harp_crossbow"))
                    .findFirst().map(e -> e.item()).orElse(null);

            if (elderGuardianHarpCrossbow != null) {
                createSmithingTransformRecipe(
                        "elder_guardian_harp_crossbow",
                        Weapons.netherite_harp_crossbow.item(),
                        Identifier.of("loot_n_explore", "guardian_upgrade_smithing_template"),
                        Identifier.of("loot_n_explore", "elder_guardian_eye"),
                        elderGuardianHarpCrossbow,
                        "loot_n_explore"
                );
            }

            var enderDragonrapier = Weapons.meleeEntries.stream()
                    .filter(e -> e.id().getPath().equals("ender_dragon_rapier"))
                    .findFirst().map(e -> e.item()).orElse(null);

            if (enderDragonrapier != null) {
                createSmithingTransformRecipe(
                        "ender_dragon_rapier",
                        Weapons.netherite_rapier.item(),
                        Identifier.of("loot_n_explore", "dragon_upgrade_smithing_template"),
                        Identifier.of("loot_n_explore", "ender_dragon_scales"),
                        enderDragonrapier,
                        "loot_n_explore"
                );
            }
            var enderDragonLute = Weapons.meleeEntries.stream()
                    .filter(e -> e.id().getPath().equals("ender_dragon_lute"))
                    .findFirst().map(e -> e.item()).orElse(null);

            if (enderDragonLute != null) {
                createSmithingTransformRecipe(
                        "ender_dragon_lute",
                        Weapons.netherite_lute.item(),
                        Identifier.of("loot_n_explore", "dragon_upgrade_smithing_template"),
                        Identifier.of("loot_n_explore", "ender_dragon_scales"),
                        enderDragonrapier,
                        "loot_n_explore"
                );
            }

            var witherrapier = Weapons.meleeEntries.stream()
                    .filter(e -> e.id().getPath().equals("wither_rapier"))
                    .findFirst().map(e -> e.item()).orElse(null);

            if (witherrapier != null) {
                createSmithingTransformRecipe(
                        "wither_rapier",
                        Weapons.netherite_rapier.item(),
                        Identifier.of("loot_n_explore", "wither_upgrade_smithing_template"),
                        Identifier.of("loot_n_explore", "wither_spine"),
                        witherrapier,
                        "loot_n_explore"
                );
            }
            // ====================
            // ARMOR UPGRADES - troubadour TO storyteller
            // ====================
            createSmithingTransformRecipe(
                    "smithing_storyteller_head_troubadour_head",
                    (Item) Armors.troubadourArmorSet.armorSet().head,
                    Identifier.of("armory_rpgs", "epic_armor_upgrade"),
                    upgradeCrystal,
                    (Item) Armors.storytellerArmorSet.armorSet().head,
                    "armory_rpgs"
            );
            createSmithingTransformRecipe(
                    "smithing_storyteller_chest_troubadour_chest",
                    (Item) Armors.troubadourArmorSet.armorSet().chest,
                    Identifier.of("armory_rpgs", "epic_armor_upgrade"),
                    upgradeCrystal,
                    (Item) Armors.storytellerArmorSet.armorSet().chest,
                    "armory_rpgs"
            );
            createSmithingTransformRecipe(
                    "smithing_storyteller_legs_troubadour_legs",
                    (Item) Armors.troubadourArmorSet.armorSet().legs,
                    Identifier.of("armory_rpgs", "epic_armor_upgrade"),
                    upgradeCrystal,
                    (Item) Armors.storytellerArmorSet.armorSet().legs,
                    "armory_rpgs"
            );
            createSmithingTransformRecipe(
                    "smithing_storyteller_feet_troubadour_feet",
                    (Item) Armors.troubadourArmorSet.armorSet().feet,
                    Identifier.of("armory_rpgs", "epic_armor_upgrade"),
                    upgradeCrystal,
                    (Item) Armors.storytellerArmorSet.armorSet().feet,
                    "armory_rpgs"
            );

        }

    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        return super.run((path, data, hashCode) -> {
            var fixedPath = fixRecipePath(path);
            var json = JsonParser.parseString(new String(data, StandardCharsets.UTF_8));
            fixRecipeJson(json.getAsJsonObject());
            writeSorted(writer, json, fixedPath);
        });
    }

    private static Path fixRecipePath(Path path) {
        var parent = path.getParent();
        if (parent != null && "recipe".equals(parent.getFileName().toString())) {
            return parent.getParent().resolve("recipes").resolve(path.getFileName().toString());
        }
        return path;
    }

    private static void fixRecipeJson(JsonObject recipe) {
        if (recipe.has("neoforge:conditions")) {
            var conditions = recipe.remove("neoforge:conditions").getAsJsonArray();
            for (var element : conditions) {
                var condition = element.getAsJsonObject();
                var type = condition.get("type").getAsString();
                if (type.startsWith("neoforge:")) {
                    condition.addProperty("type", "forge:" + type.substring("neoforge:".length()));
                }
                if (condition.has("conditions")) {
                    for (var inner : condition.getAsJsonArray("conditions")) {
                        var innerObject = inner.getAsJsonObject();
                        var innerType = innerObject.get("type").getAsString();
                        if (innerType.startsWith("neoforge:")) {
                            innerObject.addProperty("type", "forge:" + innerType.substring("neoforge:".length()));
                        }
                    }
                }
            }
            recipe.add("conditions", conditions);
        }
        if (recipe.has("result")) {
            var result = recipe.getAsJsonObject("result");
            if (result.has("id")) {
                result.addProperty("item", result.remove("id").getAsString());
            }
        }
    }

    private static void writeSorted(DataWriter writer, JsonElement json, Path path) throws IOException {
        var bytes = new ByteArrayOutputStream();
        try (var jsonWriter = new JsonWriter(new OutputStreamWriter(bytes, StandardCharsets.UTF_8))) {
            jsonWriter.setSerializeNulls(false);
            jsonWriter.setIndent("  ");
            JsonHelper.writeSorted(jsonWriter, json, DataProvider.JSON_KEY_SORTING_COMPARATOR);
        }
        var data = bytes.toByteArray();
        writer.write(path, data, Hashing.sha1().hashBytes(data));
    }
}
