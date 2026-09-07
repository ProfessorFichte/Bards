package com.bards.client.armor;

import net.minecraft.util.Identifier;
import net.rpg_foundation.armor_api.client.GeoArmorRenderer;

import static com.bards.BardsMod.MOD_ID;

public final class CustomArmorRenderer {
    private CustomArmorRenderer() { }

    public static GeoArmorRenderer entertainer_armor() {
        return make("entertainer_armor", "entertainer_armor");
    }
    public static GeoArmorRenderer troubadour_armor() {
        return make("troubadour_armor", "troubadour_armor");
    }
    public static GeoArmorRenderer netherite_troubadour_armor() {
        return make("troubadour_armor", "netherite_troubadour_armor");
    }
    public static GeoArmorRenderer storyteller_armor() {
        return make("storyteller_armor", "storyteller_armor");
    }

    private static GeoArmorRenderer make(String modelName, String textureName) {
        return GeoArmorRenderer.of(
                Identifier.of(MOD_ID, "geo/" + modelName + ".geo.json"),
                Identifier.of(MOD_ID, "textures/armor/" + textureName + ".png"));
    }
}
