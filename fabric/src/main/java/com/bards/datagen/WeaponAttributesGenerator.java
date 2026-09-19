package com.bards.datagen;

import com.bards.item.Weapons;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.RegistryWrapper;
import net.spell_engine.rpg_series.item.Weapon;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.bards.BardsMod.MOD_ID;

public class WeaponAttributesGenerator implements DataProvider {
    private final DataOutput.PathResolver pathResolver;

    public WeaponAttributesGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        this.pathResolver = output.getResolver(DataOutput.OutputType.DATA_PACK, "weapon_attributes");
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        List<CompletableFuture<?>> futures = new ArrayList<>();
        for (Weapon.Entry entry : Weapons.meleeEntries) {
            if (!entry.id().getPath().contains("rapier")) {
                continue;
            }
            JsonObject json = new JsonObject();
            json.addProperty("parent", MOD_ID + ":rapier");
            Path path = pathResolver.resolveJson(entry.id());
            futures.add(DataProvider.writeToPath(writer, json, path));
        }
        for (Weapon.Entry entry : Weapons.meleeEntries) {
            if (!entry.id().getPath().contains("lute")) {
                continue;
            }
            JsonObject json = new JsonObject();
            json.addProperty("parent", MOD_ID + ":lute");
            Path path = pathResolver.resolveJson(entry.id());
            futures.add(DataProvider.writeToPath(writer, json, path));
        }
        for (Weapon.Entry entry : Weapons.meleeEntries) {
            if (!entry.id().getPath().contains("lyre")) {
                continue;
            }
            JsonObject json = new JsonObject();
            json.addProperty("parent", MOD_ID + ":lyre");
            Path path = pathResolver.resolveJson(entry.id());
            futures.add(DataProvider.writeToPath(writer, json, path));
        }
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Bard Weapon Attributes";
    }
}
