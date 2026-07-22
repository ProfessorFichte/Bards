package com.bards.content;

import com.bards.effect.BardsEffects;
import com.bards.tags.BardTags;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.custom.SpellBuilderHelper;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.render.LightEmission;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.PlayerAnimation;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.api.util.TriState;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_engine.internals.target.SpellTarget;
import net.spell_power.api.SpellSchools;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.bards.BardsMod.MOD_ID;

public class BardsSpells {
    public enum Book {BARD}
    public enum WeaponGroup { LUTE, LYRE, DRAGON_LUTE, OCEAN_LYRE, SPELLTHIEF_LUTE, RUBY_VERDICT_LUTE, APOLLO_LYRE,ANTECAEL_LYRE}
    public record Entry(Identifier id, Spell spell, String title, String description,
                        @Nullable SpellTooltip.DescriptionMutator mutator,
                        @Nullable List<WeaponGroup> weaponGroups,
                        @Nullable Book book) {
        public Entry(Identifier id, Spell spell, String title, String description) {
            this(id, spell, title, description, null,List.of(), null);
        }
        public Entry mutator(SpellTooltip.DescriptionMutator mutator) {
            return new Entry(id, spell, title, description, mutator,weaponGroups ,book);
        }
        public Entry weaponGroup(WeaponGroup weaponGroup) {
            var newGroups = new ArrayList<>(weaponGroups != null ? weaponGroups : List.of());
            newGroups.add(weaponGroup);
            return new Entry(id, spell, title, description, mutator, newGroups, book);
        }
        public Entry book(Book book) {
            return new Entry(id, spell, title, description, mutator, weaponGroups,book);
        }
    }

    public static final List<Entry> entries = new ArrayList<>();

    private static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }

    public static final String ENCOURAGE =  "encourage";
    public static final String HUMILIATE = "humiliate";

    public static Spell.Trigger meleeImpactTrigger(float triggerChance) {
        Spell.Trigger trigger = new Spell.Trigger();
        trigger.chance = triggerChance;
        trigger.type = Spell.Trigger.Type.MELEE_IMPACT;
        return trigger;
    }
    public static Spell.Trigger arrowImpactTrigger(float triggerChance) {
        Spell.Trigger trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.ARROW_IMPACT;
        trigger.chance = triggerChance;
        return trigger;
    }
    public static Spell.Trigger spellImpatSpecificTrigger(float triggerChance, Spell.Impact.Action.Type spellImpactType) {
        Spell.Trigger trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.SPELL_IMPACT_SPECIFIC;
        trigger.chance = triggerChance;
        trigger.spell = new Spell.Trigger.SpellCondition();
        trigger.spell.type = Spell.Type.ACTIVE;
        trigger.impact = new Spell.Trigger.ImpactCondition();
        trigger.impact.impact_type = spellImpactType.toString();
        return trigger;
    }


    public static Spell.Trigger armiesPaeonMelee() {
        Spell.Trigger trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.MELEE_IMPACT;
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        trigger.aoe_source_override = Spell.Trigger.TargetSelector.CASTER;
        return trigger;
    }
    public static Spell.Trigger armiesPaeonRanged() {
        Spell.Trigger trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.ARROW_IMPACT;
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        trigger.aoe_source_override = Spell.Trigger.TargetSelector.CASTER;
        return trigger;
    }
    public static Spell.Trigger armiesPaeonSpell() {
        Spell.Trigger trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.SPELL_IMPACT_SPECIFIC;
        trigger.spell = new Spell.Trigger.SpellCondition();
        trigger.spell.type = Spell.Type.ACTIVE;
        trigger.impact = new Spell.Trigger.ImpactCondition();
        trigger.impact.impact_type = Spell.Impact.Action.Type.DAMAGE.toString();
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        trigger.aoe_source_override = Spell.Trigger.TargetSelector.CASTER;
        return trigger;
    }
    public static void bardSongWeaponSkillCooldown(Spell spell) {
        SpellBuilder.Cost.exhaust(spell, 0.2F);
        SpellBuilder.Cost.cooldown(spell, 5);
        SpellBuilder.Cost.cooldownGroupWeapon(spell);
    }
    private static PlayerAnimation bardCastAnimation() {
        return new PlayerAnimation("bards_rpg:sing_channel")
                .withEquipmentOverride(EquipmentSlot.MAINHAND, "#" + BardTags.LYRES.id().toString(), "bards_rpg:lyre_channel")
                .withEquipmentOverride(EquipmentSlot.MAINHAND, "#" + BardTags.LUTES.id().toString(), "bards_rpg:lute_channel")
                .withEquipmentOverride(EquipmentSlot.MAINHAND, "#" + BardTags.HARP_CROSSBOWS.id().toString(), "bards_rpg:harp_channel");
    }

    private static PlayerAnimation bardReleaseAnimation() {
        return new PlayerAnimation("bards_rpg:sing_release")
                .withEquipmentOverride(EquipmentSlot.MAINHAND, "#" + BardTags.LYRES.id().toString(), "bards_rpg:lyre_release")
                .withEquipmentOverride(EquipmentSlot.MAINHAND, "#" + BardTags.LUTES.id().toString(), "bards_rpg:lute_release")
                .withEquipmentOverride(EquipmentSlot.MAINHAND, "#" + BardTags.HARP_CROSSBOWS.id().toString(), "bards_rpg:harp_release");
    }

    private static ParticleBatch musicParticles(Float particleCount) {
        return new ParticleBatch(
                "more_rpg_classes:music_note",
                ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                particleCount, 0.4F, 0.5F);
    }
    private static ParticleBatch musicImpactParticles(Float particleCount) {
        return new ParticleBatch(
                "more_rpg_classes:music_note",
                ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                particleCount, 0.6F, 0.8F);
    }
    public static Spell bardSongSkill(long color, Identifier bardsong){
        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.ARCANE;
        spell.range = 5;
        spell.tier = 1;

        spell.active.cast.duration = 10.0F;
        spell.active.cast.movement_speed = 1.5F;
        spell.active.cast.type = Spell.Active.Cast.Type.CHANNEL;
        spell.active.cast.channel = new Spell.Active.Cast.Channel();
        spell.active.cast.channel.ticks = 10;
        spell.active.cast.animation = bardCastAnimation();
        spell.active.cast.sound =  Sound.withVolume(bardsong,0.7F);
        spell.active.cast.particles = new ParticleBatch[] {
                musicParticles(0.5F).color(color).extent(2.0F)
        };

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.vertical_range_multiplier = 1.5F;
        spell.target.area.include_caster = true;
        return spell;
    }
    public static Spell.Impact bardSongDamageImpact(long color, float coefficient){
        var damage = SpellBuilder.Impacts.damage(coefficient);
        damage.particles = new ParticleBatch[]{
                new ParticleBatch( SpellEngineParticles.MagicParticles.get(
                        SpellEngineParticles.MagicParticles.Shape.SPELL,
                        SpellEngineParticles.MagicParticles.Motion.BURST
                ).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        10, 0.5F, 0.5F)
                        .color(color),};
        damage.sound = Sound.withVolume(BardsSounds.bard_impact.id(),0.6F);
        return damage;
    }
    public static Spell.Impact bardSongBuffImpact(long color, String effect, int amplifierCap){
        var buff = SpellBuilder.Impacts.effectAdd(effect,9,1,amplifierCap);
        buff.school = SpellSchools.HEALING;
        buff.action.status_effect.amplifier_cap_power_multiplier = 0.15F;
        buff.action.status_effect.refresh_duration = true;
        buff.particles = new ParticleBatch[]{
                musicImpactParticles(0.5F).extent(0.5F).color(color)
        };
        return buff;
    }
    public static Spell.Impact bardSongDebuffImpact(long color, String effect, int amplifierCap){
        var debuff = SpellBuilder.Impacts.effectAdd(effect,9,1,amplifierCap);
        debuff.action.status_effect.amplifier_cap_power_multiplier = 0.1F;
        debuff.action.status_effect.refresh_duration = true;
        debuff.particles = new ParticleBatch[]{
                musicImpactParticles(0.5F).extent(0.5F).color(color)
        };
        return debuff;
    }
    public static Spell.Impact bardSongHealingImpact(float coefficient){
        var heal = SpellBuilder.Impacts.heal(coefficient);
        heal.school = SpellSchools.HEALING;
        return heal;
    }
    private static Spell.TargetCondition lowHealthCondition() {
        var deadCondition = new Spell.TargetCondition();
        deadCondition.health_percent_below = 0.5F;
        deadCondition.health_percent_above = 0.01F;
        return deadCondition;
    }
    private static Spell.TargetCondition highHealthCondition() {
        var deadCondition = new Spell.TargetCondition();
        deadCondition.health_percent_below = 1.0F;
        deadCondition.health_percent_above = 0.51F;
        return deadCondition;
    }
    /// WEAPON SKILLS
    public static float songWithoutHealDmg = 0.5F;
    public static  float songWithHealDmg = 0.35F;
    public static  float songHealing = 0.05F;
    public static  int effectCapWithoutHeal = 5;
    public static  int effectCapWithHeal = 4;

    public static final Entry troubadours_minuet = add(troubadours_minuet());
    private static Entry troubadours_minuet() {
        var id = Identifier.of(MOD_ID, "troubadours_minuet");
        var title = "Troubadour's Minuet";
        var description = "Minuet that deals {damage} damage to enemies, heals allies by {heal} and reduces incoming damage by {bonus}. " +
                "Can be stacked {effect_amplifier_cap} times.";
        var buffEffect = BardsEffects.TROUBADOURS_MINUET;
        var stringEffect = buffEffect.id.toString();
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = buffEffect.config().firstModifier();
            var bonus = SpellTooltip.bonus(Math.abs(modifier.value), modifier.operation);
            return args.description()
                    .replace("{bonus}", bonus);
        };
        var spellColor = BardSkillColors.troubadours_minuet.toRGBA();
        var bardSong = BardsSounds.troubadours_minuet.id();

        var spell = bardSongSkill(spellColor,bardSong);
        var damage = bardSongDamageImpact(spellColor,songWithHealDmg);
        var buff = bardSongBuffImpact(spellColor,stringEffect,effectCapWithHeal);
        var heal = bardSongHealingImpact(songHealing);

        spell.impacts = List.of(damage, buff, heal);
        bardSongWeaponSkillCooldown(spell);

        return new Entry(id, spell, title, description).mutator(mutator)
                .weaponGroup(WeaponGroup.LUTE);
    }
    public static final Entry wanderers_minuet = add(wanderers_minuet());
    private static Entry wanderers_minuet() {
        var id = Identifier.of(MOD_ID, "wanderers_minuet");
        var title = "Wanderer's Minuet";
        var description = "Minuet that deals {damage} damage to enemies and increases critical chance by {bonus} and critical damage by {bonus2} for allies. " +
                "Can be stacked {effect_amplifier_cap} times.";
        var buffEffect = BardsEffects.WANDERERS_MINUET;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = buffEffect.config().attributes().get(1);
            var modifier2 = buffEffect.config().attributes().get(0);
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            var bonus2 = SpellTooltip.bonus(modifier2.value, modifier2.operation);
            return args.description()
                    .replace("{bonus}", bonus)
                    .replace("{bonus2}", bonus2);
        };
        var stringEffect = buffEffect.id.toString();
        var spellColor = BardSkillColors.wanderers_minuet.toRGBA();
        var bardSong = BardsSounds.wanderers_minuet.id();
        var spell = bardSongSkill(spellColor,bardSong);
        var damage = bardSongDamageImpact(spellColor,songWithoutHealDmg);
        var buff = bardSongBuffImpact(spellColor,stringEffect,effectCapWithoutHeal);

        spell.impacts = List.of(damage, buff);
        bardSongWeaponSkillCooldown(spell);

        return new Entry(id, spell, title, description).mutator(mutator)
                .weaponGroup(WeaponGroup.LUTE);
    }
    public static final Entry secret_sonata = add(secret_sonata());
    private static Entry secret_sonata() {
        var id = Identifier.of(MOD_ID, "secret_sonata");
        var title = "Secret Sonata";
        var description = "A secret song that performs a random impact from all the other Song's. Status Effects are weaker than from the original song's. ";
        var bardSong = BardsSounds.secret_sonata.id();

        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.ARCANE;
        spell.range = 5;
        spell.tier = 1;

        spell.learn = new Spell.Learn();

        spell.active.cast.duration = 10.0F;
        spell.active.cast.movement_speed = 1.5F;
        spell.active.cast.type = Spell.Active.Cast.Type.CHANNEL;
        spell.active.cast.channel = new Spell.Active.Cast.Channel();
        spell.active.cast.channel.ticks = 10;

        spell.active.cast.animation = bardCastAnimation();
        spell.active.cast.sound =  new Sound(bardSong);
        spell.active.cast.particles = new ParticleBatch[] {
        new ParticleBatch(
                "more_rpg_classes:rainbow_music_note",
                ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                0.5F, 0.4F, 0.5F)
        };

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.vertical_range_multiplier = 1.5F;
        spell.target.area.include_caster = true;


        var secretSonataHarmful = new Spell.Impact();
        secretSonataHarmful.action = new Spell.Impact.Action();
        secretSonataHarmful.action.custom = new Spell.Impact.Action.Custom();
        secretSonataHarmful.action.type = Spell.Impact.Action.Type.CUSTOM;
        secretSonataHarmful.action.custom.intent = SpellTarget.Intent.HARMFUL;
        secretSonataHarmful.action.custom.handler = "bards_rpg:secret_sonata_impact";

        var secretSonataHelpful = new Spell.Impact();
        secretSonataHelpful.action = new Spell.Impact.Action();
        secretSonataHelpful.action.custom = new Spell.Impact.Action.Custom();
        secretSonataHelpful.action.type = Spell.Impact.Action.Type.CUSTOM;
        secretSonataHelpful.action.custom.handler = "bards_rpg:secret_sonata_impact";

        spell.impacts = List.of(secretSonataHarmful,secretSonataHelpful);
        bardSongWeaponSkillCooldown(spell);

        return new Entry(id, spell, title, description)
                .weaponGroup(WeaponGroup.RUBY_VERDICT_LUTE);
    }
    public static final Entry natures_minne = add(natures_minne());
    private static Entry natures_minne() {
        var id = Identifier.of(MOD_ID, "natures_minne");
        var title = "Natures Minne";
        var description = "Soothing song that deals {damage} damage to enemies, heals allies by {heal} and increases healing taken by {bonus}. " +
                "Can be stacked {effect_amplifier_cap} times.";
        var buffEffect = BardsEffects.NATURES_MINNE;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = buffEffect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description()
                    .replace("{bonus}", bonus);
        };
        var stringEffect = buffEffect.id.toString();
        var spellColor = BardSkillColors.natures_minne.toRGBA();
        var bardSong = BardsSounds.natures_minne.id();
        var spell = bardSongSkill(spellColor,bardSong);
        var damage = bardSongDamageImpact(spellColor,songWithHealDmg);
        var buff = bardSongBuffImpact(spellColor,stringEffect,effectCapWithHeal);
        var heal = bardSongHealingImpact(songHealing);

        spell.impacts = List.of(damage, buff, heal);
        bardSongWeaponSkillCooldown(spell);

        return new Entry(id, spell, title, description).mutator(mutator)
                .weaponGroup(WeaponGroup.LYRE);
    }
    public static final Entry song_of_celerity = add(song_of_celerity());
    private static Entry song_of_celerity() {
        var id = Identifier.of(MOD_ID, "song_of_celerity");
        var title = "Song of Celerity";
        var description = "Lively song that deals {damage} damage to enemies and increases movement speed by {bonus} for allies. " +
                "Can be stacked {effect_amplifier_cap} times.";
        var buffEffect = BardsEffects.SONG_OF_CELERITY;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = buffEffect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description()
                    .replace("{bonus}", bonus);
        };
        var stringEffect = buffEffect.id.toString();
        var spellColor = BardSkillColors.song_of_celerity.toRGBA();
        var bardSong = BardsSounds.song_of_celerity.id();
        var spell = bardSongSkill(spellColor,bardSong);
        var damage = bardSongDamageImpact(spellColor,songWithoutHealDmg);
        var buff = bardSongBuffImpact(spellColor,stringEffect,effectCapWithoutHeal);

        spell.impacts = List.of(damage, buff);
        bardSongWeaponSkillCooldown(spell);

        return new Entry(id, spell, title, description).mutator(mutator)
                .weaponGroup(WeaponGroup.LYRE);
    }
    public static final Entry discordant_note = add(discordant_note());
    private static Entry discordant_note() {
        var id = Identifier.of(MOD_ID, "discordant_note");
        var title = "Discordant Note";
        var description = "A Discordant Note that deals {damage} damage to enemies and reduces their offensive stats by {bonus}. Can be stacked {effect_amplifier_cap} times.";
        var buffEffect = BardsEffects.DISCORDANT_NOTE;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = buffEffect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description()
                    .replace("{bonus}", bonus);
        };
        var stringEffect = buffEffect.id.toString();
        var spellColor = BardSkillColors.discordant_note.toRGBA();
        var bardSong = BardsSounds.discordant_note.id();
        var spell = bardSongSkill(spellColor,bardSong);
        var damage = bardSongDamageImpact(spellColor,songWithoutHealDmg);
        var debuff = bardSongDebuffImpact(spellColor,stringEffect,effectCapWithoutHeal);

        spell.impacts = List.of(damage, debuff);
        bardSongWeaponSkillCooldown(spell);

        return new Entry(id, spell, title, description).mutator(mutator)
                .weaponGroup(WeaponGroup.SPELLTHIEF_LUTE);
    }
    public static final Entry tale_of_the_dragonslayer = add(tale_of_the_dragonslayer());
    private static Entry tale_of_the_dragonslayer() {
        var id = Identifier.of(MOD_ID, "tale_of_the_dragonslayer");
        var title = "Tale of the Dragon Slayer";
        var description = "Tale that deals {damage} damage to enemies. Increases offensive stats for allies by {bonus}, also allies deal additional magic damage with arrows & melee hits. Can be stacked {effect_amplifier_cap} time.";
        var buffEffect = BardsEffects.TALE_OF_THE_DRAGON_SLAYER;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = buffEffect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description()
                    .replace("{bonus}", bonus);
        };
        var stringEffect = buffEffect.id.toString();
        var spellColor = BardSkillColors.tale_of_the_dragonslayer.toRGBA();
        var bardSong = BardsSounds.tale_of_the_dragonslayer.id();
        var spell = bardSongSkill(spellColor,bardSong);
        var damage = bardSongDamageImpact(spellColor,songWithoutHealDmg);
        var buff = bardSongBuffImpact(spellColor,stringEffect,effectCapWithoutHeal);

        spell.impacts = List.of(damage, buff);
        bardSongWeaponSkillCooldown(spell);

        return new Entry(id, spell, title, description).mutator(mutator)
                .weaponGroup(WeaponGroup.DRAGON_LUTE);
    }
    public static final Entry hymn_of_the_golden_light = add(hymn_of_the_golden_light());
    private static Entry hymn_of_the_golden_light() {
        var buffEffect = BardsEffects.HYMN_OF_THE_GOLDEN_LIGHT;
        var id = Identifier.of(MOD_ID, "hymn_of_the_golden_light");
        var title = buffEffect.title;
        var description = "Soothing song that deals {damage} damage to enemies, heals allies by {heal}. Adds {bonus} absorption hearts, that refresh every 2 seconds. " +
                "Can be stacked {effect_amplifier_cap} times.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = buffEffect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description()
                    .replace("{bonus}", bonus);
        };
        var stringEffect = buffEffect.id.toString();
        var spellColor = BardSkillColors.hymn_of_the_golden_light.toRGBA();
        var bardSong = BardsSounds.hymn_of_the_golden_light.id();
        var spell = bardSongSkill(spellColor,bardSong);
        var damage = bardSongDamageImpact(spellColor,songWithHealDmg);
        var buff = bardSongBuffImpact(spellColor,stringEffect,effectCapWithHeal);
        var heal = bardSongHealingImpact(songHealing);

        spell.impacts = List.of(damage, buff, heal);
        bardSongWeaponSkillCooldown(spell);

        return new Entry(id, spell, title, description).mutator(mutator)
                .weaponGroup(WeaponGroup.APOLLO_LYRE);
    }
    public static final Entry song_of_the_turning_sky = add(song_of_the_turning_sky());
    private static Entry song_of_the_turning_sky() {
        var buffEffect = BardsEffects.SONG_OF_THE_TURNING_SKY;
        var id = Identifier.of(MOD_ID, "song_of_the_turning_sky");
        var title = buffEffect.title;
        var description = "Periodic song that deals {damage} damage to enemies. Heals allies by {heal} if they're below 50%% health. Reduces damage taken by {bonus} per stack if the ally is above 50%% health.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = buffEffect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description()
                    .replace("{bonus}", bonus);
        };
        var stringEffect = buffEffect.id.toString();
        var spellColor = BardSkillColors.song_of_the_turning_sky.toRGBA();
        var bardSong = BardsSounds.song_of_the_turning_sky.id();
        var spell = bardSongSkill(spellColor,bardSong);
        var damage = bardSongDamageImpact(spellColor,songWithHealDmg);
        var buff = bardSongBuffImpact(spellColor,stringEffect,effectCapWithHeal);
        var heal = bardSongHealingImpact(songHealing);

        var conditionBuff = new Spell.TargetCondition();
        conditionBuff.health_percent_above = 0.5F;
        var modifierBuff = new Spell.Impact.TargetModifier();
        modifierBuff.conditions = List.of(conditionBuff);
        modifierBuff.execute = TriState.ALLOW;
        buff.target_modifiers = List.of(modifierBuff);
        var conditionHeal = new Spell.TargetCondition();
        conditionHeal.health_percent_below = 0.49F;
        var modifierHeal = new Spell.Impact.TargetModifier();
        modifierHeal.conditions = List.of(conditionHeal);
        modifierHeal.execute = TriState.ALLOW;
        heal.target_modifiers = List.of(modifierHeal);

        spell.impacts = List.of(damage, buff, heal);
        bardSongWeaponSkillCooldown(spell);

        return new Entry(id, spell, title, description).mutator(mutator)
                .weaponGroup(WeaponGroup.ANTECAEL_LYRE);
    }
    public static final Entry canticle_of_the_tides = add(canticle_of_the_tides());
    private static Entry canticle_of_the_tides() {
        var buffEffect = BardsEffects.CANTICLES_OF_THE_TIDES;
        var id = Identifier.of(MOD_ID, "canticle_of_the_tides");
        var title = buffEffect.title;
        var description = "Deep sea canticle that deals {damage} damage to enemies. Adds a pulsating area heal on allies for {effect_duration} that scales with their max health.";
        var stringEffect = buffEffect.id.toString();
        var spellColor = BardSkillColors.canticle_of_the_tides.toRGBA();
        var bardSong = BardsSounds.canticle_of_the_tides.id();
        var spell = bardSongSkill(spellColor,bardSong);
        var damage = bardSongDamageImpact(spellColor,songWithHealDmg);
        var buff = bardSongBuffImpact(spellColor,stringEffect,effectCapWithoutHeal);

        spell.impacts = List.of(damage, buff);
        bardSongWeaponSkillCooldown(spell);

        return new Entry(id, spell, title, description)
                .weaponGroup(WeaponGroup.OCEAN_LYRE);
    }
    // ACTIVE SPELLS
    public static final Entry magical_ballad = add(magical_ballad());
    private static Entry magical_ballad() {
        var id = Identifier.of(MOD_ID, "magical_ballad");
        var title = "Magical Ballad";
        var buffEffect = BardsEffects.BALLAD;
        var description = "Launch magical ballads, piercing thru {pierce} targets, dealing {damage} damage " +
                "and increase offensive stats for allies by {bonus}.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = buffEffect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description()
                    .replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.ARCANE;
        spell.range = 20;
        spell.tier = 2;
        spell.group = ENCOURAGE;
        spell.secondary_archetype = Spell.ExtendedArchetype.ANY;
        var spellColor = BardSkillColors.magical_ballad.toRGBA();

        spell.learn = new Spell.Learn();

        spell.active.cast.duration = 4.0F;
        spell.active.cast.movement_speed = 1.5F;
        spell.active.cast.animation = bardCastAnimation();
        spell.active.cast.sound = new Sound(BardsSounds.magical_ballad.id());
        spell.active.cast.type = Spell.Active.Cast.Type.CHANNEL;
        spell.active.cast.channel = new Spell.Active.Cast.Channel();
        spell.active.cast.channel.ticks = 4;
        spell.active.cast.particles = new ParticleBatch[]{
                musicParticles(0.5F).color(spellColor).extent(2.0F)
        };

        spell.release = new Spell.Release();
        spell.target.type = Spell.Target.Type.AIM;
        spell.target.aim = new Spell.Target.Aim();

        spell.deliver.type = Spell.Delivery.Type.PROJECTILE;
        spell.deliver.projectile = new Spell.Delivery.ShootProjectile();
        spell.deliver.projectile.launch_properties.velocity = 1.5F;

        var projectile = new Spell.ProjectileData();
        projectile.homing_angle = 0F;
        projectile.perks.pierce = 3;
        projectile.client_data = new Spell.ProjectileData.Client();
        projectile.client_data.light_level = 12;
        projectile.client_data.travel_particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        ParticleBatch.Rotation.LOOK, 5,0.14F,0.15F, 0).extent(1.5F)
                        .roll(18).color(spellColor),
                new ParticleBatch(
                        "more_rpg_classes:music_note",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        ParticleBatch.Rotation.LOOK, 2,0.14F,0.15F, 0).rollOffset(180).extent(1.5F)
                        .color(spellColor),
                new ParticleBatch(
                        "more_rpg_classes:music_note",
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        ParticleBatch.Rotation.LOOK, 1,0F,0.05F, 0).extent(1.5F)
                        .color(spellColor),
        };
        var magicalBalladModel = SpellBuilder.ProjectileModels.model("bards_rpg:spell_projectile/magical_ballad", 2.0F, LightEmission.RADIATE);
        magicalBalladModel.rotate_degrees_per_tick = 0F;
        projectile.client_data.composite_model = SpellBuilder.ProjectileModels.composite(magicalBalladModel);
        projectile.hitbox = new Spell.ProjectileData.HitBox(0.6F, 0.8F);
        spell.deliver.projectile.projectile = projectile;

        var damage = SpellBuilder.Impacts.damage(0.75F, 1.0F);
        damage.particles = new ParticleBatch[] {
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                        SpellEngineParticles.MagicParticles.Shape.SPARK,
                        SpellEngineParticles.MagicParticles.Motion.BURST
                        ).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        15, 0.1F, 0.25F).color(Color.ARCANE.toRGBA()),
                musicImpactParticles(0.5F).extent(0.5F).color(spellColor)
        };
        damage.sound = new Sound(BardsSounds.bard_impact.id());

        var buff = SpellBuilder.Impacts.effectAdd(BardsEffects.BALLAD.id.toString(),8,1,3);
        buff.school = SpellSchools.HEALING;
        buff.action.status_effect.amplifier_cap_power_multiplier = 0.15F;
        buff.particles = new ParticleBatch[] {
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.ASCEND
                        ).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.FEET,
                        15, 0.1F, 0.25F).color(Color.ARCANE.toRGBA()),
                musicImpactParticles(0.5F).extent(0.5F).color(spellColor)
        };
        buff.sound = new Sound(BardsSounds.bard_buff.id());

        spell.impacts = List.of(damage, buff);

        SpellBuilder.Cost.exhaust(spell, 0.2F);
        SpellBuilder.Cost.cooldown(spell, 10);

        return new Entry(id, spell, title, description).mutator(mutator).book(Book.BARD);
    }
    public static final Entry vicious_mockery = add(vicious_mockery());
    private static Entry vicious_mockery() {
        var id = Identifier.of(MOD_ID, "vicious_mockery");
        var effect = BardsEffects.VICIOUS_MOCKERY;
        var title = "Vicious Mockery";
        var description = "Throw a string of insults at a target, taunting it, damaging it by {damage} and decreasing it's offensive power and increasing incoming damage by {bonus} per stack. " +
                "Stacking up to {effect_amplifier_cap} times.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description()
                    .replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.ARCANE;
        spell.range = 16;
        spell.tier = 2;
        spell.group = HUMILIATE;
        spell.secondary_archetype = Spell.ExtendedArchetype.ANY;
        spell.learn = new Spell.Learn();
        var spellColor = BardSkillColors.vicious_mockery.toRGBA();



        SpellBuilder.Casting.channel(spell, 5, 15);
        spell.active.cast.animation = bardCastAnimation();
        spell.active.cast.particles = new ParticleBatch[]{
                musicParticles(0.5F).color(spellColor).extent(2.0F)
        };
        spell.active.cast.sound = Sound.withVolume(BardsSounds.vicious_mockery.id(),0.7F);

        spell.target.type = Spell.Target.Type.AIM;
        spell.target.aim = new Spell.Target.Aim();
        spell.target.aim.required = true;

        spell.release = new Spell.Release();

        var damage = SpellBuilder.Impacts.damage(0.75F, 0);
        damage.particles = new ParticleBatch[]{
                musicImpactParticles(0.5F).extent(0.5F).color(spellColor),
                new ParticleBatch(
                        "more_rpg_classes:rage_particle",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        10, 0.5F, 0.8F).color(spellColor).extent(0.25F)

        };

        var debuff = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 5,1,5);

        var taunt = SpellBuilder.Impacts.taunt();

        spell.impacts = List.of(damage, debuff,taunt);

        SpellBuilder.Cost.exhaust(spell, 0.3F);
        SpellBuilder.Cost.cooldown(spell, 15);
        spell.cost.cooldown.proportional = true;

        return new Entry(id, spell, title, description).book(Book.BARD).mutator(mutator);
    }
    public static final Entry wardens_paean = add(wardens_paean());
    private static Entry wardens_paean() {
        var id = Identifier.of(MOD_ID, "wardens_paean");
        var title = "Warden's Paean";
        var description = "The next applied status effects (up to {effect_amplifier_cap_1}), for {effect_duration_1} seconds get removed. " +
                "Beneficial Effects from Enemies, Harmful Effects from allies.";

        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.ARCANE;
        spell.range = 10;
        spell.tier = 3;
        spell.group = HUMILIATE;
        spell.secondary_archetype = Spell.ExtendedArchetype.ANY;
        spell.learn = new Spell.Learn();
        var spellColor = BardSkillColors.wardens_paean.toRGBA();

        spell.target.type = Spell.Target.Type.AIM;
        spell.target.aim = new Spell.Target.Aim();
        spell.target.aim.use_caster_as_fallback =  true;
        
        spell.release = new Spell.Release();
        spell.release.animation = bardReleaseAnimation();
        spell.release.particles = new ParticleBatch[]{
                musicParticles(10F).color(spellColor).extent(2.0F)
        };

        var harmful = SpellBuilder.Impacts.effectSet_ScaledAmplifier_Cap(
                BardsEffects.HARMFUL_WARDENS_PAEAN.id.toString(),8,0,0.3F,4);
        harmful.sound = new Sound(BardsSounds.bard_impact.id());
        var beneficial =  SpellBuilder.Impacts.effectSet_ScaledAmplifier_Cap(
                BardsEffects.BENEFICIAL_WARDENS_PAEAN.id.toString(),8,0,0.3F,4);
        beneficial.school = SpellSchools.HEALING;
        beneficial.sound = new Sound(BardsSounds.bard_buff.id());

        spell.impacts = List.of(harmful, beneficial);

        SpellBuilder.Cost.exhaust(spell, 0.3F);
        SpellBuilder.Cost.cooldown(spell, 25);

        return new Entry(id, spell, title, description).book(Book.BARD);
    }
    public static final Entry encore = add(encore());
    private static Entry encore() {
        var id = Identifier.of(MOD_ID, "encore");
        var title = "Encore";
        var description = "Deals {damage} damage to nearby targets and motivates allies by slightly reducing active spells cooldowns. The range increases, the longer you charge.";

        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.ARCANE;
        spell.range = 4;
        spell.tier = 3;
        spell.group = ENCOURAGE;
        spell.secondary_archetype = Spell.ExtendedArchetype.ANY;
        var spellColor = BardSkillColors.encore.toRGBA();

        spell.learn = new Spell.Learn();

        var charge = SpellBuilder.Casting.charge(spell, 1.5F);
        charge.min_release_ratio = 0.2F;
        charge.output_scaling = 0.8F;
        charge.bonus.range_add = 12.0F;

        spell.active.cast.duration = 1.25F;
        spell.active.cast.movement_speed = 1.5F;
        spell.active.cast.animation = bardCastAnimation();
        spell.active.cast.sound =  new Sound(BardsSounds.encore_channel.id());

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.vertical_range_multiplier = 1.0F;
        spell.target.area.include_caster = true;

        spell.release = new Spell.Release();
        spell.release.animation = bardReleaseAnimation();
        spell.release.particles = new ParticleBatch[]{
                musicParticles(10F).color(spellColor).extent(2.0F)
        };

        var damage = SpellBuilder.Impacts.damage(0.6F, 0.5F);
        damage.particles = new ParticleBatch[] {
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.BURST
                        ).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        30, 0.2F, 0.7F)
                        .color(SpellBuilderHelper.CYAN.toRGBA()),
                musicImpactParticles(0.5F).extent(0.5F).color(spellColor)
        };
        damage.sound = new Sound(BardsSounds.bard_impact.id());

        Spell.Impact cooldown = new Spell.Impact();
        cooldown.action = new Spell.Impact.Action();
        cooldown.action.type = Spell.Impact.Action.Type.COOLDOWN;
        cooldown.action.cooldown = new Spell.Impact.Action.Cooldown();
        cooldown.action.cooldown.actives = new Spell.Impact.Action.Cooldown.Modify();
        cooldown.action.cooldown.actives.duration_multiplier = 0.8F;
        cooldown.particles = new ParticleBatch[] {
                musicImpactParticles(0.5F).extent(0.5F).color(spellColor)
        };
        cooldown.sound = new Sound(BardsSounds.encore_cooldown_impact.id());

        spell.impacts = List.of(damage, cooldown);

        SpellBuilder.Cost.exhaust(spell, 0.2F);
        SpellBuilder.Cost.cooldown(spell, 20);

        return new Entry(id, spell, title, description).book(Book.BARD);
    }
    public static final Entry armys_paeon = add(armys_paeon());
    private static Entry armys_paeon() {
        var id = Identifier.of(MOD_ID, "armys_paeon");
        var title = "Army's Paeon";
        var description = "Buff nearby allies for {effect_duration} sec, enhance their strength if you damage enemies. The effect can be stacked {effect_amplifier_cap} times.";
        var stashEffect = BardsEffects.ARMYS_PAEON_STASH;
        var buffEffect = BardsEffects.ARMYS_PAEON;

        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.ARCANE;
        spell.range = 5;
        spell.tier = 4;
        spell.group = ENCOURAGE;
        spell.secondary_archetype = Spell.ExtendedArchetype.ANY;
        var spellColor = BardSkillColors.armys_paeon.toRGBA();

        spell.release.animation = bardReleaseAnimation();
        spell.release.particles = new ParticleBatch[]{
                musicParticles(10F).color(spellColor).extent(2.0F)
        };
        spell.release.sound = new Sound(BardsSounds.armys_paeon_release.id());

        spell.deliver.type = Spell.Delivery.Type.STASH_EFFECT;
        spell.deliver.stash_effect = new Spell.Delivery.StashEffect();
        spell.deliver.stash_effect.id = stashEffect.id.toString();
        spell.deliver.stash_effect.consume = 0;
        spell.deliver.stash_effect.triggers = List.of(armiesPaeonMelee(), armiesPaeonRanged(), armiesPaeonSpell());

        var buff = SpellBuilder.Impacts.effectAdd(buffEffect.id.toString(),8,1,3);
        buff.sound = new Sound(BardsSounds.armys_paeon_impact.id());
        buff.action.status_effect.amplifier_cap_power_multiplier = 0.15F;
        buff.school = SpellSchools.HEALING;
        buff.particles = new ParticleBatch[]{
                new ParticleBatch( SpellEngineParticles.MagicParticles.get(
                        SpellEngineParticles.MagicParticles.Shape.SPARK,
                        SpellEngineParticles.MagicParticles.Motion.DECELERATE
                        ).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        30, 0.2F, 0.2F)
                        .color(SpellBuilderHelper.GOLD.toRGBA()),
                new ParticleBatch(
                        SpellEngineParticles.area_circle_1.id().toString(),
                        ParticleBatch.Shape.LINE_VERTICAL, ParticleBatch.Origin.FEET,
                        1, 0.3F, 0.3F)
                        .followEntity(true)
                        .scale(1.0F)
                        .maxAge(0.6F)
                        .color(SpellBuilderHelper.GOLD.toRGBA()),
                musicImpactParticles(0.5F).extent(1.5F).color(spellColor)
        };
        buff.sound = new Sound(BardsSounds.armys_paeon_buff.id());

        spell.impacts = List.of(buff);

        SpellBuilder.Cost.exhaust(spell, 0.2F);
        SpellBuilder.Cost.cooldown(spell, 30);

        return new Entry(id, spell, title, description).book(Book.BARD);
    }
    public static final Entry crescendo = add(crescendo());
    private static Entry crescendo() {
        var id = Identifier.of(MOD_ID, "crescendo");
        var title = "Crescendo";
        var debuffEffect = BardsEffects.CRESCENDO;
        var description = "Strikes an irresistible chord, stunning any enemy it passes through, dealing {damage} damage. " +
                "Also increases incoming damage by {bonus} per stack.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = debuffEffect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description()
                    .replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.ARCANE;
        spell.range = 30;
        spell.tier = 4;
        spell.group = HUMILIATE;
        spell.secondary_archetype = Spell.ExtendedArchetype.ANY;
        var spellColor = BardSkillColors.crescendo.toRGBA();

        spell.learn = new Spell.Learn();

        spell.release = new Spell.Release();
        spell.release.animation = bardReleaseAnimation();
        spell.release.particles = new ParticleBatch[]{
                musicParticles(4.0F).color(spellColor).extent(2.0F)
        };
        spell.release.sound = Sound.of(BardsSounds.crescendo_launch.id());
        spell.target.type = Spell.Target.Type.AIM;
        spell.target.aim = new Spell.Target.Aim();

        spell.deliver.type = Spell.Delivery.Type.PROJECTILE;
        spell.deliver.projectile = new Spell.Delivery.ShootProjectile();
        spell.deliver.projectile.launch_properties.velocity = 2.0F;

        var projectile = new Spell.ProjectileData();
        projectile.homing_angle = 0F;
        projectile.perks.pierce = 999999;
        projectile.client_data = new Spell.ProjectileData.Client();
        projectile.client_data.light_level = 12;
        projectile.client_data.travel_particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        ParticleBatch.Rotation.LOOK, 5,0.14F,0.15F, 0).extent(1.5F)
                        .roll(18).color(spellColor),
                new ParticleBatch(
                        "more_rpg_classes:music_note",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        ParticleBatch.Rotation.LOOK, 2,0.14F,0.15F, 0).rollOffset(180).extent(1.5F)
                        .color(spellColor),
                new ParticleBatch(
                        "more_rpg_classes:music_note",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        ParticleBatch.Rotation.LOOK, 1,0F,0.05F, 0).extent(1.5F)
                        .color(spellColor),
        };
        var crescendoModel = SpellBuilder.ProjectileModels.model("bards_rpg:spell_projectile/crescendo", 3.5F, LightEmission.RADIATE);
        crescendoModel.rotate_degrees_per_tick = 0F;
        projectile.client_data.composite_model = SpellBuilder.ProjectileModels.composite(crescendoModel);
        projectile.hitbox = new Spell.ProjectileData.HitBox(3.0F, 0.8F);
        spell.deliver.projectile.projectile = projectile;

        var damage = SpellBuilder.Impacts.damage(0.8F, 0.0F);
        damage.particles = new ParticleBatch[] {
                musicImpactParticles(0.5F).extent(0.5F).color(spellColor),
        };
        damage.sound = new Sound(BardsSounds.bard_impact.id());

        var debuff = SpellBuilder.Impacts.effectAdd(BardsEffects.CRESCENDO.id.toString(),2.5F,1,3);
        debuff.action.status_effect.amplifier_cap_power_multiplier = 0.15F;
        debuff.particles = new ParticleBatch[] {
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.ASCEND
                        ).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.FEET,
                        15, 0.1F, 0.25F).color(Color.ARCANE.toRGBA()),
                musicImpactParticles(0.5F).extent(0.5F).color(spellColor)
        };

        spell.impacts = List.of(damage, debuff);

        SpellBuilder.Cost.exhaust(spell, 0.2F);
        SpellBuilder.Cost.cooldown(spell, 35);

        return new Entry(id, spell, title, description).mutator(mutator).book(Book.BARD);
    }
    /// HELPER IMPACTS
    public static final Entry armys_paeon_impact = add(armys_paeon_impact());
    private static Entry armys_paeon_impact() {
        var id = Identifier.of(MOD_ID, "helper/armys_paeon_impact");
        var title = "Army's Paeon";
        var description = "";
        var stashEffect = BardsEffects.ARMYS_PAEON;

        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.GENERIC;
        spell.range = 0;
        spell.tier = 1;
        var spellColor = SpellBuilderHelper.GOLD.toRGBA();

        spell.deliver.type = Spell.Delivery.Type.STASH_EFFECT;
        spell.deliver.stash_effect = new Spell.Delivery.StashEffect();
        spell.deliver.stash_effect.id = stashEffect.id.toString();
        spell.deliver.stash_effect.consume = 0;
        spell.deliver.stash_effect.triggers = List.of(SpellBuilder.Triggers.meleeAttackImpact(), SpellBuilder.Triggers.arrowHit(),
                SpellBuilder.Triggers.spellHit(1.0F,null));

        var custom = new Spell.Impact();
        custom.action = new Spell.Impact.Action();
        custom.action.custom = new Spell.Impact.Action.Custom();
        custom.action.type = Spell.Impact.Action.Type.CUSTOM;
        custom.action.custom.intent = SpellTarget.Intent.HARMFUL;
        custom.action.custom.handler = "bards_rpg:armies_paeon_impact";
        custom.particles = new ParticleBatch[]{
                new ParticleBatch( SpellEngineParticles.MagicParticles.get(
                        SpellEngineParticles.MagicParticles.Shape.SPELL,
                        SpellEngineParticles.MagicParticles.Motion.BURST
                ).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        20, 0.3F, 0.5F)
                        .color(SpellBuilderHelper.GOLD.toRGBA()),
                musicImpactParticles(0.5F).extent(0.25F).color(spellColor)
        };

        spell.impacts = List.of(custom);

        SpellBuilder.Cost.cooldown(spell, 1.5F);

        return new Entry(id, spell, title, description);
    }
    public static final Entry tale_of_the_dragonslayer_impact = add(tale_of_the_dragonslayer_impact());
    private static Entry tale_of_the_dragonslayer_impact() {
        var id = Identifier.of(MOD_ID, "helper/tale_of_the_dragonslayer_impact");
        var title = "";
        var description = "";
        var stashEffect = BardsEffects.TALE_OF_THE_DRAGON_SLAYER;

        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.GENERIC;
        spell.range = 0;
        spell.tier = 1;

        spell.deliver.type = Spell.Delivery.Type.STASH_EFFECT;
        spell.deliver.stash_effect = new Spell.Delivery.StashEffect();
        spell.deliver.stash_effect.id = stashEffect.id.toString();
        spell.deliver.stash_effect.consume = 0;
        spell.deliver.stash_effect.triggers = List.of(SpellBuilder.Triggers.meleeAttackImpact(), SpellBuilder.Triggers.arrowHit());

        var damage = new Spell.Impact();
        damage.attribute = EntityAttributes.GENERIC_MAX_HEALTH.getIdAsString();
        damage.attribute_from_target = true;
        damage.action = new Spell.Impact.Action();
        damage.action.type = Spell.Impact.Action.Type.DAMAGE;
        damage.action.damage = new Spell.Impact.Action.Damage();
        damage.action.damage.spell_power_coefficient = 0.01F;
        damage.particles = new ParticleBatch[]{
                new ParticleBatch( SpellEngineParticles.MagicParticles.get(
                        SpellEngineParticles.MagicParticles.Shape.SKULL,
                        SpellEngineParticles.MagicParticles.Motion.BURST
                ).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        20, 0.3F, 0.5F)
                        .color(Color.ARCANE.toRGBA()),
        };

        spell.impacts = List.of(damage);

        SpellBuilder.Cost.cooldown(spell, 1);

        return new Entry(id, spell, title, description);
    }
    public static final Entry canticle_of_the_tides_heal = add(canticle_of_the_tides_heal());
    private static Entry canticle_of_the_tides_heal() {
        var id = Identifier.of(MOD_ID, "helper/canticle_of_the_tides_heal");
        var title = "";
        var description = "";

        var spell = SpellBuilder.createSpellActive();
        spell.school = ExternalSpellSchools.HEALTH;
        spell.range = 2;
        spell.tier = 1;

        spell.release.particles_scaled_with_ranged = new ParticleBatch[]{
                new ParticleBatch(SpellEngineParticles.area_effect_658.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.GROUND,
                        1, 0, 0)
                        .scale(0.5F)
                        .followEntity(true)
                        .color(SpellBuilderHelper.CYAN.toRGBA())
        };

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.vertical_range_multiplier = 0.5F;
        spell.target.area.include_caster = true;

        var heal = SpellBuilder.Impacts.heal(0.01F);

        spell.impacts = List.of(heal);

        SpellBuilder.Cost.cooldown(spell, 1);

        return new Entry(id, spell, title, description);
    }
    /// MODIFIERS
    public static final Entry improved_encore= add(improved_encore());
    private static Entry improved_encore() {
        var id = Identifier.of(MOD_ID, "improved_encore");
        var title = "Improved Encore";
        var description = "Increases the range of Encore by {range_add}";
        var spell = new Spell();
        spell.school = SpellSchools.ARCANE;
        spell.range = 0;
        spell.tier = 1;

        spell.type = Spell.Type.MODIFIER;

        spell.tooltip = new Spell.Tooltip();
        spell.tooltip.show_header = false;
        spell.tooltip.name = new Spell.Tooltip.LineOptions(false, false);
        spell.tooltip.description.color = "gray";
        spell.tooltip.description.show_in_compact = true;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "bards_rpg:encore";
        modifier.range_add = 2.0F;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description);
    }
    /// PASSIVE SPELLS
    public static Entry spellthief = add(spellthief());
    private static Entry spellthief() {
        var id = Identifier.of(MOD_ID, "spellthief");
        var title = "Spellthief";
        var description = "On spell impact: {trigger_chance_1} chance to steal beneficial status effects and to cast a random spell from the damaged entity.";
        var spell = SpellBuilder.createSpellPassive();
        spell.tier = 10;
        spell.school = SpellSchools.ARCANE;
        spell.range = 7F;

        float trigger_chance = 0.2F;
        spell.passive.triggers = List.of(meleeImpactTrigger(trigger_chance),
                spellImpatSpecificTrigger(trigger_chance,Spell.Impact.Action.Type.DAMAGE),
                spellImpatSpecificTrigger(trigger_chance,Spell.Impact.Action.Type.HEAL),
                spellImpatSpecificTrigger(trigger_chance,Spell.Impact.Action.Type.STATUS_EFFECT));

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.include_caster = true;
        spell.target.area.angle_degrees = 360.0F;

        var spellTheftHarmful = new Spell.Impact();
        spellTheftHarmful.action = new Spell.Impact.Action();
        spellTheftHarmful.action.custom = new Spell.Impact.Action.Custom();
        spellTheftHarmful.action.type = Spell.Impact.Action.Type.CUSTOM;
        spellTheftHarmful.action.custom.intent = SpellTarget.Intent.HARMFUL;
        spellTheftHarmful.action.custom.handler = "more_rpg_classes:spellthief_impact";

        var spellTheftHelpful = new Spell.Impact();
        spellTheftHelpful.action = new Spell.Impact.Action();
        spellTheftHelpful.action.custom = new Spell.Impact.Action.Custom();
        spellTheftHelpful.action.type = Spell.Impact.Action.Type.CUSTOM;
        spellTheftHelpful.action.custom.handler = "more_rpg_classes:spellthief_impact";
        spell.impacts = List.of(spellTheftHarmful, spellTheftHelpful);

        SpellBuilder.Cost.cooldown(spell,5.0F);
        spell.cost.cooldown.hosting_item = false;

        return new Entry(id, spell, title, description);
    }
    public static Entry melody_of_the_meteor = add(melody_of_the_meteor());
    private static Entry melody_of_the_meteor() {
        var id = Identifier.of(MOD_ID, "melody_of_the_meteor");
        var title = "Melody of the Meteor";
        var description = "On spell impact: {trigger_chance} to spawn ruby meteorites above the target, dealing {damage} damage.";
        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.ARCANE;
        spell.tier = 10;
        spell.range = 10F;

        var trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.SPELL_IMPACT_SPECIFIC;
        trigger.chance = 0.3F;
        trigger.impact = new Spell.Trigger.ImpactCondition();
        trigger.impact.impact_type = Spell.Impact.Action.Type.DAMAGE.toString();
        trigger.spell = new Spell.Trigger.SpellCondition();
        trigger.spell.type = Spell.Type.ACTIVE;
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        spell.deliver.type = Spell.Delivery.Type.METEOR;
        spell.deliver.meteor = new Spell.Delivery.Meteor();
        spell.deliver.meteor.launch_height = 7;
        spell.deliver.meteor.launch_radius = 4;
        spell.deliver.meteor.launch_properties.velocity = 0.9F;
        spell.deliver.meteor.launch_properties.extra_launch_count = 2;
        spell.deliver.meteor.launch_properties.extra_launch_delay = 3;


        var projectile = new Spell.ProjectileData();
        projectile.client_data = new Spell.ProjectileData.Client();
        projectile.client_data.light_level = 12;
        projectile.client_data.travel_particles = new ParticleBatch[] {
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.ARCANE,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        null, 20, 0.2F, 0.7F, 0.0F, 0F)
                        .color(Color.RED.toRGBA())
        };
        projectile.client_data.composite_model = SpellBuilder.ProjectileModels.single("bards_rpg:spell_projectile/melody_of_the_meteor", 2F);
        spell.deliver.meteor.projectile = projectile;

        var damage = SpellBuilder.Impacts.damage(0.5F, 1.5F);
        damage.particles = new ParticleBatch[] {
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.ARCANE,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        null, 20, 0.2F, 0.7F, 0.0F, 0F)
                        .color(Color.RED.toRGBA())
        };
        spell.impacts = List.of(damage);

        spell.area_impact = new Spell.AreaImpact();
        spell.area_impact.radius = 2.0F;
        spell.area_impact.area.distance_dropoff = Spell.Target.Area.DropoffCurve.SQUARED;

        SpellBuilder.Cost.cooldown(spell,5.0F);

        return new Entry(id, spell, title, description);
    }
    public static Entry eclipse_mantle = add(eclipse_mantle());
    private static Entry eclipse_mantle() {
        var id = Identifier.of(MOD_ID, "eclipse_mantle");
        var effect = BardsEffects.ECLIPSE_MANTLE;
        var title = "Eclipse Mantle";
        var description = "On effect applied: {trigger_chance} chance to heal your ally by {heal} hearts and increase evasion chance by {bonus}.";
        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.HEALING;
        spell.tier = 10;
        spell.range = 2F;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description()
                    .replace("{bonus}", bonus);
        };

        var trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.SPELL_IMPACT_SPECIFIC;
        trigger.chance = 0.2F;
        trigger.impact = new Spell.Trigger.ImpactCondition();
        trigger.impact.impact_type = Spell.Impact.Action.Type.STATUS_EFFECT.toString();
        trigger.spell = new Spell.Trigger.SpellCondition();
        trigger.spell.type = Spell.Type.ACTIVE;
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var buff = SpellBuilder.Impacts.effectSet(effect.id.toString(), 5,1);
        buff.particles = new ParticleBatch[]{
                new ParticleBatch( SpellEngineParticles.MagicParticles.get(
                        SpellEngineParticles.MagicParticles.Shape.SPARK,
                        SpellEngineParticles.MagicParticles.Motion.DECELERATE
                        ).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        20, 0.1F, 0.1F)
                        .color(SpellBuilderHelper.MAGENTA.toRGBA()),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.HOLY,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE
                        ).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        15, 0.2F, 0.25F)
                        .color(SpellBuilderHelper.MAGENTA.toRGBA()),
        };
        var heal = SpellBuilder.Impacts.heal(0.1F);
        spell.impacts = List.of(buff, heal);

        SpellBuilder.Cost.cooldown(spell,10.0F);
        spell.cost.cooldown.hosting_item = false;

        return new Entry(id, spell, title, description).mutator(mutator);
    }
    
    public static final Color STARSHOT_COLOR = SpellBuilderHelper.GOLD.blend(Color.WHITE, 0.5F);
    public static final Entry starshots = add(starshots());
    private static Entry starshots() {
        var id = Identifier.of(MOD_ID, "starshots");
        var effect = BardsEffects.ASTRAL_SHOTS;
        var title = "Astral Shooting";
        var description = "Arrow hits have {trigger_chance_1} chance to enhance your arrows with astral magic for {stash_duration} sec, dealing extra {damage} magic damage. Your overall pull time is heavily increased by {bonus}. ";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            if (bonus.startsWith("-")) bonus = bonus.substring(1);
            return args.description()
                    .replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        spell.release.particles = new ParticleBatch[]{
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_arrow.id(), STARSHOT_COLOR),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.ASCEND).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        15, 0.1F, 0.3F).color(STARSHOT_COLOR.toRGBA())
        };

        var trigger = SpellBuilder.Triggers.arrowHit();
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        trigger.chance = 0.2F;

        spell.passive.triggers = List.of(trigger);

        var stashTrigger = SpellBuilder.Triggers.arrowShot(false);
        SpellBuilder.Deliver.stash(spell, effect.id.toString(), 5F, stashTrigger);
        spell.deliver.stash_effect.impact_mode = Spell.Delivery.StashEffect.ImpactMode.TRANSFER;
        spell.deliver.stash_effect.consume = 0;

        spell.arrow_perks = new Spell.ArrowPerks();
        spell.arrow_perks.pierce = 10;
        spell.arrow_perks.bypass_iframes = true;
        spell.arrow_perks.composite_model = SpellBuilder.ProjectileModels.single("bards_rpg:spell_projectile/star_arrow", 1.2F, LightEmission.RADIATE);
        spell.arrow_perks.launch_particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.LAUNCH_POINT,
                        ParticleBatch.Rotation.LOOK, 50,0.18F,0.2F, 0)
                        .color(STARSHOT_COLOR.toRGBA()),
                new ParticleBatch(
                        "more_rpg_classes:star",
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.LAUNCH_POINT,
                        ParticleBatch.Rotation.LOOK, 0.2F,0.28F,0.3F, 0)
                        .color(STARSHOT_COLOR.toRGBA()).scale(0.3F)
        };
        spell.arrow_perks.travel_particles = new ParticleBatch[]{
                new ParticleBatch(
                        "more_rpg_classes:star",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.FEET,
                        0.1F, 0.1F, 0.2F)
                        .color(STARSHOT_COLOR.toRGBA()).scale(0.3F)
        };

        var impact = SpellBuilder.Impacts.damage(0.5F, 0);
        impact.school = SpellSchools.ARCANE;
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        "more_rpg_classes:star",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        5, 0.4F, 0.5F)
                        .color(STARSHOT_COLOR.toRGBA()).scale(0.3F),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        25, 0.7F, 0.8F)
                        .color(STARSHOT_COLOR.toRGBA())
        };
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 15F);
        spell.cost.cooldown.hosting_item = false;

        return new Entry(id, spell, title, description).mutator(mutator);
    }
}
