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
import net.more_rpg_classes.client.particle.MoreParticles;
import net.spell_engine.api.spell.fx.Fx;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.api.spell.tooltip.TooltipTokens;
import net.spell_engine.api.util.TriState;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_engine.internals.target.SpellTarget;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellSchools;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.bards.BardsMod.MOD_ID;

public class BardsSpells {
    public enum Book {BARD}
    public enum WeaponGroup { LUTE, LYRE, DRAGON_LUTE, OCEAN_LYRE, SPELLTHIEF_LUTE, RUBY_VERDICT_LUTE, APOLLO_LYRE,ANTECAEL_LYRE}
    public record Entry(Identifier id, Spell spell, String title, String description,
                        @Nullable List<WeaponGroup> weaponGroups,
                        @Nullable Book book) {
        public Entry(Identifier id, Spell spell, String title, String description) {
            this(id, spell, title, description, List.of(), null);
        }
        public Entry weaponGroup(WeaponGroup weaponGroup) {
            var newGroups = new ArrayList<>(weaponGroups != null ? weaponGroups : List.of());
            newGroups.add(weaponGroup);
            return new Entry(id, spell, title, description, newGroups, book);
        }
        public Entry book(Book book) {
            return new Entry(id, spell, title, description, weaponGroups, book);
        }
    }

    public static final List<Entry> entries = new ArrayList<>();

    private static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }

    private static final Identifier ATTACK_DAMAGE = Identifier.of(EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString());

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

    /// V1 `ParticleBatch.count` below 1 was the probability of spawning a SINGLE particle,
    /// on the one-shot and per-tick paths alike. V2 reads a fractional count as an emission
    /// period and ignores it entirely at a one-shot site, so `count(1).chance(c)` is the
    /// exact equivalent at both kinds of site.
    private static void musicCount(ParticleGroup.Batch batch, float count) {
        if (count < 1F) {
            batch.count(1F).chance(count);
        } else {
            batch.count(count);
        }
    }

    private static ParticleGroup musicParticles(float particleCount, long color, float extent) {
        return ParticleGroupBuilder.of(MoreParticles.MUSIC_NOTE)
                .color(color)
                .batch(b -> {
                    b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F)
                            .speed(0.4F, 0.5F)
                            .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                            .extent(extent);
                    musicCount(b, particleCount);
                });
    }
    private static ParticleGroup musicImpactParticles(float particleCount, long color, float extent) {
        return ParticleGroupBuilder.of(MoreParticles.MUSIC_NOTE)
                .color(color)
                .batch(b -> {
                    b.shape(ParticleGroup.Shape.CIRCLE)
                            .speed(0.6F, 0.8F)
                            .extent(extent);
                    musicCount(b, particleCount);
                });
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
        spell.active.cast.particles = List.of(
                musicParticles(0.5F, color, 2.0F));

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.vertical_range_multiplier = 1.5F;
        spell.target.area.include_caster = true;
        return spell;
    }
    public static Spell.Impact bardSongDamageImpact(long color, float coefficient){
        var damage = SpellBuilder.Impacts.damage(coefficient);
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spell, ParticleGroup.Motion.BURST)
                        .color(color)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(10).speed(0.5F, 0.5F)));
        damage.sound = Sound.withVolume(BardsSounds.bard_impact.id(),0.6F);
        return damage;
    }
    public static Spell.Impact bardSongBuffImpact(long color, String effect, int amplifierCap){
        var buff = SpellBuilder.Impacts.effectAdd(effect,9,1,amplifierCap);
        buff.school = SpellSchools.HEALING;
        buff.action.status_effect.amplifier_cap_power_multiplier = 0.15F;
        buff.action.status_effect.refresh_duration = true;
        buff.visuals = Fx.Visuals.of(
                musicImpactParticles(0.5F, color, 0.5F));
        return buff;
    }
    public static Spell.Impact bardSongDebuffImpact(long color, String effect, int amplifierCap){
        var debuff = SpellBuilder.Impacts.effectAdd(effect,9,1,amplifierCap);
        debuff.action.status_effect.amplifier_cap_power_multiplier = 0.1F;
        debuff.action.status_effect.refresh_duration = true;
        debuff.visuals = Fx.Visuals.of(
                musicImpactParticles(0.5F, color, 0.5F));
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
        var buffEffect = BardsEffects.TROUBADOURS_MINUET;
        // Sole modifier (damage taken), stored negative - `ABS` keeps the "reduces ... by" phrasing positive.
        var description = "Minuet that deals {damage} damage to enemies, heals allies by {heal} and reduces incoming damage by "
                + TooltipTokens.effect(buffEffect.id, 0, null, TooltipTokens.Format.ABS) + ". " +
                "Can be stacked {effect_amplifier_cap} times.";
        var stringEffect = buffEffect.id.toString();
        var spellColor = BardSkillColors.troubadours_minuet.toRGBA();
        var bardSong = BardsSounds.troubadours_minuet.id();

        var spell = bardSongSkill(spellColor,bardSong);
        var damage = bardSongDamageImpact(spellColor,songWithHealDmg);
        var buff = bardSongBuffImpact(spellColor,stringEffect,effectCapWithHeal);
        var heal = bardSongHealingImpact(songHealing);

        spell.impacts = List.of(damage, buff, heal);
        bardSongWeaponSkillCooldown(spell);

        return new Entry(id, spell, title, description)
                .weaponGroup(WeaponGroup.LUTE);
    }
    public static final Entry wanderers_minuet = add(wanderers_minuet());
    private static Entry wanderers_minuet() {
        var id = Identifier.of(MOD_ID, "wanderers_minuet");
        var title = "Wanderer's Minuet";
        var buffEffect = BardsEffects.WANDERERS_MINUET;
        var description = "Minuet that deals {damage} damage to enemies and increases critical chance by "
                + TooltipTokens.effect(buffEffect.id, 0, SpellPowerMechanics.CRITICAL_CHANCE.id)
                + " and critical damage by "
                + TooltipTokens.effect(buffEffect.id, 0, SpellPowerMechanics.CRITICAL_DAMAGE.id) + " for allies. " +
                "Can be stacked {effect_amplifier_cap} times.";
        var stringEffect = buffEffect.id.toString();
        var spellColor = BardSkillColors.wanderers_minuet.toRGBA();
        var bardSong = BardsSounds.wanderers_minuet.id();
        var spell = bardSongSkill(spellColor,bardSong);
        var damage = bardSongDamageImpact(spellColor,songWithoutHealDmg);
        var buff = bardSongBuffImpact(spellColor,stringEffect,effectCapWithoutHeal);

        spell.impacts = List.of(damage, buff);
        bardSongWeaponSkillCooldown(spell);

        return new Entry(id, spell, title, description)
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
        spell.active.cast.particles = List.of(
                ParticleGroupBuilder.of("more_rpg_classes:rainbow_music_note")
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F)
                                .verticalOrigin(0.1F)
                                .count(0.5F).speed(0.4F, 0.5F)));

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
        var buffEffect = BardsEffects.NATURES_MINNE;
        var description = "Soothing song that deals {damage} damage to enemies, heals allies by {heal} and increases healing taken by "
                + TooltipTokens.effect(buffEffect.id) + ". " +
                "Can be stacked {effect_amplifier_cap} times.";
        var stringEffect = buffEffect.id.toString();
        var spellColor = BardSkillColors.natures_minne.toRGBA();
        var bardSong = BardsSounds.natures_minne.id();
        var spell = bardSongSkill(spellColor,bardSong);
        var damage = bardSongDamageImpact(spellColor,songWithHealDmg);
        var buff = bardSongBuffImpact(spellColor,stringEffect,effectCapWithHeal);
        var heal = bardSongHealingImpact(songHealing);

        spell.impacts = List.of(damage, buff, heal);
        bardSongWeaponSkillCooldown(spell);

        return new Entry(id, spell, title, description)
                .weaponGroup(WeaponGroup.LYRE);
    }
    public static final Entry song_of_celerity = add(song_of_celerity());
    private static Entry song_of_celerity() {
        var id = Identifier.of(MOD_ID, "song_of_celerity");
        var title = "Song of Celerity";
        var buffEffect = BardsEffects.SONG_OF_CELERITY;
        var description = "Lively song that deals {damage} damage to enemies and increases movement speed by "
                + TooltipTokens.effect(buffEffect.id) + " for allies. " +
                "Can be stacked {effect_amplifier_cap} times.";
        var stringEffect = buffEffect.id.toString();
        var spellColor = BardSkillColors.song_of_celerity.toRGBA();
        var bardSong = BardsSounds.song_of_celerity.id();
        var spell = bardSongSkill(spellColor,bardSong);
        var damage = bardSongDamageImpact(spellColor,songWithoutHealDmg);
        var buff = bardSongBuffImpact(spellColor,stringEffect,effectCapWithoutHeal);

        spell.impacts = List.of(damage, buff);
        bardSongWeaponSkillCooldown(spell);

        return new Entry(id, spell, title, description)
                .weaponGroup(WeaponGroup.LYRE);
    }
    public static final Entry discordant_note = add(discordant_note());
    private static Entry discordant_note() {
        var id = Identifier.of(MOD_ID, "discordant_note");
        var title = "Discordant Note";
        var buffEffect = BardsEffects.DISCORDANT_NOTE;
        // Three modifiers sharing one value; the token names attack damage so the pick is deterministic.
        // Stored negative, `ABS` keeps the "reduces ... by" phrasing positive.
        var description = "A Discordant Note that deals {damage} damage to enemies and reduces their offensive stats by "
                + TooltipTokens.effect(buffEffect.id, 0, ATTACK_DAMAGE, TooltipTokens.Format.ABS)
                + ". Can be stacked {effect_amplifier_cap} times.";
        var stringEffect = buffEffect.id.toString();
        var spellColor = BardSkillColors.discordant_note.toRGBA();
        var bardSong = BardsSounds.discordant_note.id();
        var spell = bardSongSkill(spellColor,bardSong);
        var damage = bardSongDamageImpact(spellColor,songWithoutHealDmg);
        var debuff = bardSongDebuffImpact(spellColor,stringEffect,effectCapWithoutHeal);

        spell.impacts = List.of(damage, debuff);
        bardSongWeaponSkillCooldown(spell);

        return new Entry(id, spell, title, description)
                .weaponGroup(WeaponGroup.SPELLTHIEF_LUTE);
    }
    public static final Entry tale_of_the_dragonslayer = add(tale_of_the_dragonslayer());
    private static Entry tale_of_the_dragonslayer() {
        var id = Identifier.of(MOD_ID, "tale_of_the_dragonslayer");
        var title = "Tale of the Dragon Slayer";
        var buffEffect = BardsEffects.TALE_OF_THE_DRAGON_SLAYER;
        // Three modifiers sharing one value; the token names attack damage so the pick is deterministic.
        var description = "Tale that deals {damage} damage to enemies. Increases offensive stats for allies by "
                + TooltipTokens.effect(buffEffect.id, 0, ATTACK_DAMAGE)
                + ", also allies deal additional magic damage with arrows & melee hits. Can be stacked {effect_amplifier_cap} time.";
        var stringEffect = buffEffect.id.toString();
        var spellColor = BardSkillColors.tale_of_the_dragonslayer.toRGBA();
        var bardSong = BardsSounds.tale_of_the_dragonslayer.id();
        var spell = bardSongSkill(spellColor,bardSong);
        var damage = bardSongDamageImpact(spellColor,songWithoutHealDmg);
        var buff = bardSongBuffImpact(spellColor,stringEffect,effectCapWithoutHeal);

        spell.impacts = List.of(damage, buff);
        bardSongWeaponSkillCooldown(spell);

        return new Entry(id, spell, title, description)
                .weaponGroup(WeaponGroup.DRAGON_LUTE);
    }
    public static final Entry hymn_of_the_golden_light = add(hymn_of_the_golden_light());
    private static Entry hymn_of_the_golden_light() {
        var buffEffect = BardsEffects.HYMN_OF_THE_GOLDEN_LIGHT;
        var id = Identifier.of(MOD_ID, "hymn_of_the_golden_light");
        var title = buffEffect.title;
        // Sole modifier (max absorption) is an ADD_VALUE, so the token renders a flat number, not a percent.
        var description = "Soothing song that deals {damage} damage to enemies, heals allies by {heal}. Adds "
                + TooltipTokens.effect(buffEffect.id) + " absorption hearts, that refresh every 2 seconds. " +
                "Can be stacked {effect_amplifier_cap} times.";
        var stringEffect = buffEffect.id.toString();
        var spellColor = BardSkillColors.hymn_of_the_golden_light.toRGBA();
        var bardSong = BardsSounds.hymn_of_the_golden_light.id();
        var spell = bardSongSkill(spellColor,bardSong);
        var damage = bardSongDamageImpact(spellColor,songWithHealDmg);
        var buff = bardSongBuffImpact(spellColor,stringEffect,effectCapWithHeal);
        var heal = bardSongHealingImpact(songHealing);

        spell.impacts = List.of(damage, buff, heal);
        bardSongWeaponSkillCooldown(spell);

        return new Entry(id, spell, title, description)
                .weaponGroup(WeaponGroup.APOLLO_LYRE);
    }
    public static final Entry song_of_the_turning_sky = add(song_of_the_turning_sky());
    private static Entry song_of_the_turning_sky() {
        var buffEffect = BardsEffects.SONG_OF_THE_TURNING_SKY;
        var id = Identifier.of(MOD_ID, "song_of_the_turning_sky");
        var title = buffEffect.title;
        // Sole modifier (damage taken), stored negative - `ABS` keeps the "Reduces ... by" phrasing positive.
        var description = "Periodic song that deals {damage} damage to enemies. Heals allies by {heal} if they're below 50%% health. Reduces damage taken by "
                + TooltipTokens.effect(buffEffect.id, 0, null, TooltipTokens.Format.ABS)
                + " per stack if the ally is above 50%% health.";
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

        return new Entry(id, spell, title, description)
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
        // Three modifiers sharing one value; the token names attack damage so the pick is deterministic.
        var description = "Launch magical ballads, piercing thru {pierce} targets, dealing {damage} damage " +
                "and increase offensive stats for allies by " + TooltipTokens.effect(buffEffect.id, 0, ATTACK_DAMAGE) + ".";

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
        spell.active.cast.particles = List.of(
                musicParticles(0.5F, spellColor, 2.0F));

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
        projectile.client_data.travel_particles = List.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE)
                        .color(spellColor)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .alignment(ParticleGroup.Alignment.LOOK)
                                .count(5).speed(0.14F, 0.15F)
                                .extent(1.5F).roll(18F)),
                ParticleGroupBuilder.of(MoreParticles.MUSIC_NOTE)
                        .color(spellColor)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .alignment(ParticleGroup.Alignment.LOOK)
                                .count(2).speed(0.14F, 0.15F)
                                .extent(1.5F).rollOffset(180F)),
                ParticleGroupBuilder.of(MoreParticles.MUSIC_NOTE)
                        .color(spellColor)
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .alignment(ParticleGroup.Alignment.LOOK)
                                .count(1).speed(0F, 0.05F)
                                .extent(1.5F)));
        var magicalBalladModel = SpellBuilder.ProjectileModels.model("bards_rpg:spell_projectile/magical_ballad", 2.0F, LightEmission.RADIATE);
        magicalBalladModel.rotate_degrees_per_tick = 0F;
        projectile.client_data.composite_model = SpellBuilder.ProjectileModels.composite(magicalBalladModel);
        projectile.hitbox = new Spell.ProjectileData.HitBox(0.6F, 0.8F);
        spell.deliver.projectile.projectile = projectile;

        var damage = SpellBuilder.Impacts.damage(0.75F, 1.0F);
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.BURST, Color.ARCANE)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(15).speed(0.1F, 0.25F)),
                musicImpactParticles(0.5F, spellColor, 0.5F));
        damage.sound = new Sound(BardsSounds.bard_impact.id());

        var buff = SpellBuilder.Impacts.effectAdd(BardsEffects.BALLAD.id.toString(),8,1,3);
        buff.school = SpellSchools.HEALING;
        buff.action.status_effect.amplifier_cap_power_multiplier = 0.15F;
        buff.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.ASCEND, Color.ARCANE)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .verticalOrigin(0.1F)
                                .count(15).speed(0.1F, 0.25F)),
                musicImpactParticles(0.5F, spellColor, 0.5F));
        buff.sound = new Sound(BardsSounds.bard_buff.id());

        spell.impacts = List.of(damage, buff);

        SpellBuilder.Cost.exhaust(spell, 0.2F);
        SpellBuilder.Cost.cooldown(spell, 10);

        return new Entry(id, spell, title, description).book(Book.BARD);
    }
    public static final Entry vicious_mockery = add(vicious_mockery());
    private static Entry vicious_mockery() {
        var id = Identifier.of(MOD_ID, "vicious_mockery");
        var effect = BardsEffects.VICIOUS_MOCKERY;
        var title = "Vicious Mockery";
        // Four modifiers: the three offensive ones are negative, damage-taken is positive, all of the same
        // magnitude - the sentence quotes that shared magnitude, so `ABS` on a named modifier reads correctly.
        var description = "Throw a string of insults at a target, taunting it, damaging it by {damage} and decreasing it's offensive power and increasing incoming damage by "
                + TooltipTokens.effect(effect.id, 0, ATTACK_DAMAGE, TooltipTokens.Format.ABS) + " per stack. " +
                "Stacking up to {effect_amplifier_cap} times.";

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
        spell.active.cast.particles = List.of(
                musicParticles(0.5F, spellColor, 2.0F));
        spell.active.cast.sound = Sound.withVolume(BardsSounds.vicious_mockery.id(),0.7F);

        spell.target.type = Spell.Target.Type.AIM;
        spell.target.aim = new Spell.Target.Aim();
        spell.target.aim.required = true;

        spell.release = new Spell.Release();

        var damage = SpellBuilder.Impacts.damage(0.75F, 0);
        damage.visuals = Fx.Visuals.of(
                musicImpactParticles(0.5F, spellColor, 0.5F),
                ParticleGroupBuilder.of(MoreParticles.RAGE_PAR)
                        .color(spellColor)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(10).speed(0.5F, 0.8F)
                                .extent(0.25F)));

        var debuff = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 5,1,5);

        var taunt = SpellBuilder.Impacts.taunt();

        spell.impacts = List.of(damage, debuff,taunt);

        SpellBuilder.Cost.exhaust(spell, 0.3F);
        SpellBuilder.Cost.cooldown(spell, 15);
        spell.cost.cooldown.proportional = true;

        return new Entry(id, spell, title, description).book(Book.BARD);
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
        spell.release.visuals = Fx.Visuals.of(
                musicParticles(10F, spellColor, 2.0F));

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
        spell.release.visuals = Fx.Visuals.of(
                musicParticles(10F, spellColor, 2.0F));

        var damage = SpellBuilder.Impacts.damage(0.6F, 0.5F);
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark,
                                ParticleGroup.Motion.BURST, SpellBuilderHelper.CYAN)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(30).speed(0.2F, 0.7F)),
                musicImpactParticles(0.5F, spellColor, 0.5F));
        damage.sound = new Sound(BardsSounds.bard_impact.id());

        Spell.Impact cooldown = new Spell.Impact();
        cooldown.action = new Spell.Impact.Action();
        cooldown.action.type = Spell.Impact.Action.Type.COOLDOWN;
        cooldown.action.cooldown = new Spell.Impact.Action.Cooldown();
        cooldown.action.cooldown.actives = new Spell.Impact.Action.Cooldown.Modify();
        cooldown.action.cooldown.actives.duration_multiplier = 0.8F;
        cooldown.visuals = Fx.Visuals.of(
                musicImpactParticles(0.5F, spellColor, 0.5F));
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
        spell.release.visuals = Fx.Visuals.of(
                musicParticles(10F, spellColor, 2.0F));
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
        buff.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark,
                                ParticleGroup.Motion.DECELERATE, SpellBuilderHelper.GOLD)
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F)
                                .count(30).speed(0.2F, 0.2F)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)),
                ParticleGroupBuilder.of(SpellEngineParticles.area_circle_1)
                        .color(SpellBuilderHelper.GOLD)
                        .attached()
                        .playbackSpeed(1F / 0.6F)
                        .batch(b -> b.shape(ParticleGroup.Shape.LINE_VERTICAL)
                                .count(1).speed(0.3F, 0.3F)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)),
                musicImpactParticles(0.5F, spellColor, 1.5F));
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
                "Also increases incoming damage by " + TooltipTokens.effect(debuffEffect.id) + " per stack.";

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
        spell.release.visuals = Fx.Visuals.of(
                musicParticles(4.0F, spellColor, 2.0F));
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
        projectile.client_data.travel_particles = List.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE)
                        .color(spellColor)
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .alignment(ParticleGroup.Alignment.LOOK)
                                .count(5).speed(0.14F, 0.15F)
                                .extent(1.5F).roll(18F)),
                ParticleGroupBuilder.of(MoreParticles.MUSIC_NOTE)
                        .color(spellColor)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .alignment(ParticleGroup.Alignment.LOOK)
                                .count(2).speed(0.14F, 0.15F)
                                .extent(1.5F).rollOffset(180F)),
                ParticleGroupBuilder.of(MoreParticles.MUSIC_NOTE)
                        .color(spellColor)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .alignment(ParticleGroup.Alignment.LOOK)
                                .count(1).speed(0F, 0.05F)
                                .extent(1.5F)));
        var crescendoModel = SpellBuilder.ProjectileModels.model("bards_rpg:spell_projectile/crescendo", 3.5F, LightEmission.RADIATE);
        crescendoModel.rotate_degrees_per_tick = 0F;
        projectile.client_data.composite_model = SpellBuilder.ProjectileModels.composite(crescendoModel);
        projectile.hitbox = new Spell.ProjectileData.HitBox(3.0F, 0.8F);
        spell.deliver.projectile.projectile = projectile;

        var damage = SpellBuilder.Impacts.damage(0.8F, 0.0F);
        damage.visuals = Fx.Visuals.of(
                musicImpactParticles(0.5F, spellColor, 0.5F));
        damage.sound = new Sound(BardsSounds.bard_impact.id());

        var debuff = SpellBuilder.Impacts.effectAdd(BardsEffects.CRESCENDO.id.toString(),2.5F,1,3);
        debuff.action.status_effect.amplifier_cap_power_multiplier = 0.15F;
        debuff.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark,
                                ParticleGroup.Motion.ASCEND, Color.ARCANE)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(15).speed(0.1F, 0.25F)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)),
                musicImpactParticles(0.5F, spellColor, 0.5F));

        spell.impacts = List.of(damage, debuff);

        SpellBuilder.Cost.exhaust(spell, 0.2F);
        SpellBuilder.Cost.cooldown(spell, 35);

        return new Entry(id, spell, title, description).book(Book.BARD);
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
        custom.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spell,
                                ParticleGroup.Motion.BURST, SpellBuilderHelper.GOLD)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(20).speed(0.3F, 0.5F)),
                musicImpactParticles(0.5F, spellColor, 0.25F));

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
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_skull, ParticleGroup.Motion.BURST, Color.ARCANE)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(20).speed(0.3F, 0.5F)));

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

        spell.release.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.area_effect_658)
                        .color(SpellBuilderHelper.CYAN)
                        .scaleWith(Fx.ScaleWith.RANGE)
                        .attached()
                        .batch(b -> b.shape(ParticleGroup.Shape.NONE)
                                .count(1).anchor(ParticleGroup.Anchor.GROUND)));

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
        projectile.client_data.travel_particles = List.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_arcane, ParticleGroup.Motion.BURST, Color.RED)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(20).speed(0.2F, 0.7F)));
        projectile.client_data.composite_model = SpellBuilder.ProjectileModels.single("bards_rpg:spell_projectile/melody_of_the_meteor", 2F);
        spell.deliver.meteor.projectile = projectile;

        var damage = SpellBuilder.Impacts.damage(0.5F, 1.5F);
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_arcane, ParticleGroup.Motion.BURST, Color.RED)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(20).speed(0.2F, 0.7F)));
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
        var description = "On effect applied: {trigger_chance} chance to heal your ally by {heal} hearts and increase evasion chance by "
                + TooltipTokens.effect(effect.id) + ".";
        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.HEALING;
        spell.tier = 10;
        spell.range = 2F;

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
        buff.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE, SpellBuilderHelper.MAGENTA)
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                .count(20).speed(0.1F, 0.1F)),
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_holy, ParticleGroup.Motion.DECELERATE, SpellBuilderHelper.MAGENTA)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(15).speed(0.2F, 0.25F)));
        var heal = SpellBuilder.Impacts.heal(0.1F);
        spell.impacts = List.of(buff, heal);

        SpellBuilder.Cost.cooldown(spell,10.0F);
        spell.cost.cooldown.hosting_item = false;

        return new Entry(id, spell, title, description);
    }
    
    public static final Color STARSHOT_COLOR = SpellBuilderHelper.GOLD.blend(Color.WHITE, 0.5F);
    public static final Entry starshots = add(starshots());
    private static Entry starshots() {
        var id = Identifier.of(MOD_ID, "starshots");
        var effect = BardsEffects.ASTRAL_SHOTS;
        var title = "Astral Shooting";
        // `{bonus}` stays render-time custom - see `registerTooltipTokens()`. A declarative
        // `{effect|...}` token can't reproduce it: the modifier is ADD_MULTIPLIED_TOTAL, whose
        // rendering subtracts 1 from the value, and `Format.ABS` takes the absolute value *before*
        // that offset - so it would print "-80%" where this spell has always shown "80%".
        var description = "Arrow hits have {trigger_chance_1} chance to enhance your arrows with astral magic for {stash_duration} sec, dealing extra {damage} magic damage. Your overall pull time is heavily increased by {bonus}. ";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        spell.release.visuals = Fx.Visuals.of(
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_arrow.id(), STARSHOT_COLOR),
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.ASCEND, STARSHOT_COLOR)
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                .count(15).speed(0.1F, 0.3F)));

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
        spell.arrow_perks.launch_visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE, STARSHOT_COLOR)
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .anchor(ParticleGroup.Anchor.LAUNCH_POINT)
                                .alignment(ParticleGroup.Alignment.LOOK)
                                .count(50).speed(0.18F, 0.2F)),
                ParticleGroupBuilder.of(MoreParticles.STAR)
                        .color(STARSHOT_COLOR).scale(0.3F)
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .anchor(ParticleGroup.Anchor.LAUNCH_POINT)
                                .alignment(ParticleGroup.Alignment.LOOK)
                                .count(1).chance(0.2F)));
        spell.arrow_perks.travel_particles = List.of(
                ParticleGroupBuilder.of(MoreParticles.STAR)
                        .color(STARSHOT_COLOR).scale(0.3F)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                .count(1).chance(0.1F)
                                .speed(0.1F, 0.2F)));

        var impact = SpellBuilder.Impacts.damage(0.5F, 0);
        impact.school = SpellSchools.ARCANE;
        impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(MoreParticles.STAR)
                        .color(STARSHOT_COLOR).scale(0.3F)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(5).speed(0.4F, 0.5F)),
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE, STARSHOT_COLOR)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(25).speed(0.7F, 0.8F)));
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 15F);
        spell.cost.cooldown.hosting_item = false;

        return new Entry(id, spell, title, description);
    }

    public static void registerTooltipTokens() {
        // Astral Shots' modifier is ADD_MULTIPLIED_TOTAL: `TooltipTokens.bonus` renders it as
        // `percent(value - 1)`, i.e. "-80%" for a configured 0.2. The description phrases it as an
        // increase, so the sign is stripped - which `Format.ABS` cannot do (it takes the absolute value
        // before the -1 offset). Since RWA 3.0.0, this is a HASTE modifier (not PULL_TIME) with the sign
        // flipped to reproduce the same slowdown - abs() here keeps the displayed magnitude ("80%")
        // unchanged regardless of that sign flip.
        TooltipTokens.registerCustom(starshots.id(), args -> {
            var modifier = BardsEffects.ASTRAL_SHOTS.config().firstModifier();
            var bonus = TooltipTokens.bonus(Math.abs(modifier.value), modifier.operation);
            if (bonus.startsWith("-")) bonus = bonus.substring(1);
            return args.description().replace("{bonus}", bonus);
        });
    }
}
