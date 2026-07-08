package com.bard_rpg.config;

import net.fabric_extras.ranged_weapon.api.RangedConfig;
import net.spell_engine.api.item.ItemConfig;

import java.util.LinkedHashMap;

public class BardItemConfig {
    public LinkedHashMap<String, ItemConfig.Weapon> melee_weapons = new LinkedHashMap<>();
    public LinkedHashMap<String, ItemConfig.ArmorSet> armor_sets = new LinkedHashMap<>();
    public LinkedHashMap<String, RangedConfig> ranged_weapons = new LinkedHashMap<>();
}
