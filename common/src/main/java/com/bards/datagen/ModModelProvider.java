package com.bards.datagen;

import com.bards.item.Armors;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.bards.item.Weapons;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import static com.bards.BardsMod.MOD_ID;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        Armors.entries.forEach(entry -> {
            for (var piece: entry.armorSet().pieces()) {
                itemModelGenerator.register((Item) piece, Models.GENERATED);
            }
        });
        for (var entry : Weapons.meleeEntries) {
            Item item = entry.item();
            if (item == null) continue;

            Identifier itemId = Registries.ITEM.getId(item);
            String name = itemId.getPath();

            if (name.contains("rapier")) {
                generateRapierModel(itemModelGenerator, itemId, name);
            } else if (name.contains("lute")) {
                generateLuteInventoryModel(itemModelGenerator, itemId, name);
                if(name.contains("wooden") || name.contains("diamond") || name.contains("netherite") || name.contains("ruby")|| name.contains("aether")){
                    generateLuteOverworldModel(itemModelGenerator, itemId, name);
                }
            } else if (name.contains("lyre")) {
                generateLyreInventoryModel(itemModelGenerator, itemId, name);
                if(name.contains("golden") || name.contains("diamond") || name.contains("netherite") || name.contains("aeternium")){
                    generateLyreOverworldModel(itemModelGenerator, itemId, name);
                }
            }
        }
        for (var entry : Weapons.rangedEntries) {
            Item item = entry.item();
            if (item == null) continue;

            Identifier itemId = Registries.ITEM.getId(item);
            String name = itemId.getPath();

            boolean isHarpCrossbow = name.contains("harp_crossbow");
            if (isHarpCrossbow) {
                generateCrossbowModel(itemModelGenerator, itemId, name);
            }
        }
    }


    private void generateRapierModel(ItemModelGenerator gen, Identifier itemId, String name) {
        Identifier modelId = Identifier.of(itemId.getNamespace(), "item/" + name);

        JsonObject json = new JsonObject();
        json.addProperty("parent", MOD_ID + ":item/rapier_model");

        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", MOD_ID + ":item/" + name);
        json.add("textures", textures);

        gen.writer.accept(modelId, () -> json);
    }

    private void generateLuteInventoryModel(ItemModelGenerator gen, Identifier itemId, String name) {
        Identifier modelId = Identifier.of(itemId.getNamespace(), "item/" + name);

        JsonObject json = new JsonObject();
        json.addProperty("parent", "minecraft:item/generated");

        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", MOD_ID + ":item/" + name + "_inventory");
        json.add("textures", textures);

        gen.writer.accept(modelId, () -> json);
    }

    private void generateLuteOverworldModel(ItemModelGenerator gen, Identifier itemId, String name) {
        Identifier modelId = Identifier.of(itemId.getNamespace(), "item/" + name + "_model");

        JsonObject json = new JsonObject();
        json.addProperty("parent", MOD_ID + ":item/generic_lute");

        JsonObject textures = new JsonObject();
        textures.addProperty("4", MOD_ID + ":item/" + name);
        textures.addProperty("particle", MOD_ID + ":item/" + name);
        json.add("textures", textures);

        gen.writer.accept(modelId, () -> json);
    }

    private void generateLyreInventoryModel(ItemModelGenerator gen, Identifier itemId, String name) {
        Identifier modelId = Identifier.of(itemId.getNamespace(), "item/" + name);

        JsonObject json = new JsonObject();
        json.addProperty("parent", "minecraft:item/generated");

        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", MOD_ID + ":item/" + name + "_inventory");
        json.add("textures", textures);

        gen.writer.accept(modelId, () -> json);
    }

    private void generateLyreOverworldModel(ItemModelGenerator gen, Identifier itemId, String name) {
        Identifier modelId = Identifier.of(itemId.getNamespace(), "item/" + name + "_model");

        JsonObject json = new JsonObject();
        json.addProperty("parent", MOD_ID + ":item/generic_lyre");

        JsonObject textures = new JsonObject();
        textures.addProperty("0", MOD_ID + ":item/" + name);
        textures.addProperty("particle", MOD_ID + ":item/" + name);
        json.add("textures", textures);

        gen.writer.accept(modelId, () -> json);
    }

    private void generateCrossbowModel(ItemModelGenerator itemModelGenerator, Identifier itemId, String name) {
        Identifier modelId = Identifier.of(itemId.getNamespace(), "item/" + name);

        JsonObject json = new JsonObject();
        if(name.contains("unique_harp_crossbow_1")){
            json.addProperty("parent", "bards_rpg:item/harp_crossbow_big");
        } else {
            json.addProperty("parent", "bards_rpg:item/harp_crossbow_model");
        }

        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", MOD_ID + ":item/" + name);
        json.add("textures", textures);

        // Add overrides for crossbow states
        JsonArray overrides = new JsonArray();

        // Pulling override
        JsonObject pullingOverride = new JsonObject();
        JsonObject pullingPredicate = new JsonObject();
        pullingPredicate.addProperty("pulling", 1);
        pullingOverride.add("predicate", pullingPredicate);
        pullingOverride.addProperty("model", MOD_ID + ":item/" + name + "_pulling_0");
        overrides.add(pullingOverride);

        // Pull states
        for (int i = 0; i <= 2; i++) {
            JsonObject pullOverride = new JsonObject();
            JsonObject pullPredicate = new JsonObject();
            pullPredicate.addProperty("pulling", 1);
            pullPredicate.addProperty("pull", (i + 1) * 0.333);
            pullOverride.add("predicate", pullPredicate);
            pullOverride.addProperty("model", MOD_ID + ":item/" + name + "_pulling_" + i);
            overrides.add(pullOverride);
        }

        // Charged override
        JsonObject chargedOverride = new JsonObject();
        JsonObject chargedPredicate = new JsonObject();
        chargedPredicate.addProperty("charged", 1);
        chargedOverride.add("predicate", chargedPredicate);
        chargedOverride.addProperty("model", MOD_ID + ":item/" + name + "_arrow");
        overrides.add(chargedOverride);

        // Firework override
        JsonObject fireworkOverride = new JsonObject();
        JsonObject fireworkPredicate = new JsonObject();
        fireworkPredicate.addProperty("charged", 1);
        fireworkPredicate.addProperty("firework", 1);
        fireworkOverride.add("predicate", fireworkPredicate);
        fireworkOverride.addProperty("model", MOD_ID + ":item/" + name + "_firework");
        overrides.add(fireworkOverride);

        json.add("overrides", overrides);
        itemModelGenerator.writer.accept(modelId, () -> json);

        // Generate pulling state models
        for (int i = 0; i <= 2; i++) {
            Identifier pullingModelId = Identifier.of(itemId.getNamespace(), "item/" + name + "_pulling_" + i);
            JsonObject pullingJson = new JsonObject();
            pullingJson.addProperty("parent", MOD_ID + ":item/" + name);
            JsonObject pullingTextures = new JsonObject();
            pullingTextures.addProperty("layer0", MOD_ID + ":item/bow_pulling/" + name + "_pulling_" + i);
            pullingJson.add("textures", pullingTextures);
            itemModelGenerator.writer.accept(pullingModelId, () -> pullingJson);
        }

        // Generate charged (arrow) model
        Identifier arrowModelId = Identifier.of(itemId.getNamespace(), "item/" + name + "_arrow");
        JsonObject arrowJson = new JsonObject();
        arrowJson.addProperty("parent", MOD_ID + ":item/" + name);
        JsonObject arrowTextures = new JsonObject();
        arrowTextures.addProperty("layer0", MOD_ID + ":item/" + name + "_arrow");
        arrowJson.add("textures", arrowTextures);
        itemModelGenerator.writer.accept(arrowModelId, () -> arrowJson);

        // Generate firework model
        Identifier fireworkModelId = Identifier.of(itemId.getNamespace(), "item/" + name + "_firework");
        JsonObject fireworkJson = new JsonObject();
        fireworkJson.addProperty("parent", MOD_ID + ":item/" + name);
        JsonObject fireworkTextures = new JsonObject();
        fireworkTextures.addProperty("layer0", MOD_ID + ":item/" + name + "_firework");
        fireworkJson.add("textures", fireworkTextures);
        itemModelGenerator.writer.accept(fireworkModelId, () -> fireworkJson);
    }
}
