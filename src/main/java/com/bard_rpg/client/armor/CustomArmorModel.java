package com.bard_rpg.client.armor;

import com.bard_rpg.item.BardArmorItem;
import mod.azure.azurelibarmor.model.GeoModel;
import net.minecraft.util.Identifier;

public class CustomArmorModel extends GeoModel<BardArmorItem> {
    private final Identifier modelId;
    private final Identifier textureId;

    public CustomArmorModel(String modelName, String textureName) {
        this.modelId = new Identifier("bards_rpg", "geo/" + modelName + ".geo.json");
        this.textureId = new Identifier("bards_rpg", "textures/armor/" + textureName + ".png");
    }

    @Override
    public Identifier getModelResource(BardArmorItem object) {
        return modelId;
    }

    @Override
    public Identifier getTextureResource(BardArmorItem object) {
        return textureId;
    }

    @Override
    public Identifier getAnimationResource(BardArmorItem object) {
        return null;
    }
}
