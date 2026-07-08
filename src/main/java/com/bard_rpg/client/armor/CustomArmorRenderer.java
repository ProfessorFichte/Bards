package com.bard_rpg.client.armor;

import com.bard_rpg.item.BardArmorItem;
import mod.azure.azurelibarmor.renderer.GeoArmorRenderer;

public class CustomArmorRenderer extends GeoArmorRenderer<BardArmorItem> {
    public CustomArmorRenderer(String modelName, String textureName) {
        super(new CustomArmorModel(modelName, textureName));
    }
}
