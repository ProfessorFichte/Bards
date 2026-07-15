package com.bards.content;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.event.SpellHandlers;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.fx.ParticleHelper;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.target.SpellTarget;
import net.spell_engine.utils.SoundHelper;
import net.spell_power.api.SpellPower;

import java.util.ArrayList;
import java.util.List;

import static com.bards.BardsMod.MOD_ID;

public class SecretSonataImpact implements SpellHandlers.CustomImpact {

    private static final TagKey<Spell> SONGS_TAG = TagKey.of(SpellRegistry.KEY, Identifier.of(MOD_ID, "songs"));
    // All songs are authored with amplifier=1, apply_mode=ADD, amplifier_cap 4-5 and
    // amplifier_cap_power_multiplier=0.15, so direct casting can stack a song up to Level 5-6.
    // Borrowed songs are floored at Level 1 (cap 0) with only a small power-scaled ceiling
    // (0.1, below the source songs' own 0.15) so a maxed-power caster still gets a token bonus
    // without approaching what dedicated single-song builds reach.
    private static final int BORROWED_AMPLIFIER_CAP = 0;
    private static final float BORROWED_AMPLIFIER_CAP_MULTIPLIER = 0.1F;

    private static List<Spell.Impact> withCappedStatusEffectAmplifiers(List<Spell.Impact> original) {
        var result = new ArrayList<Spell.Impact>(original.size());
        for (var impact : original) {
            if (impact.action.type == Spell.Impact.Action.Type.STATUS_EFFECT && impact.action.status_effect != null) {
                var cappedImpact = new Spell.Impact();
                cappedImpact.chance = impact.chance;
                cappedImpact.school = impact.school;
                cappedImpact.attribute_from_target = impact.attribute_from_target;
                cappedImpact.attribute = impact.attribute;
                cappedImpact.target_modifiers = impact.target_modifiers;
                cappedImpact.particles = impact.particles;
                cappedImpact.sound = impact.sound;

                var action = new Spell.Impact.Action();
                action.type = impact.action.type;
                action.allow_on_center_target = impact.action.allow_on_center_target;
                action.apply_to_caster = impact.action.apply_to_caster;
                action.min_power = impact.action.min_power;
                action.max_power = impact.action.max_power;

                var src = impact.action.status_effect;
                var se = new Spell.Impact.Action.StatusEffect();
                se.effect_id = src.effect_id;
                se.duration = src.duration;
                se.amplifier = Math.min(src.amplifier, BORROWED_AMPLIFIER_CAP);
                se.amplifier_power_multiplier = 0;
                se.amplifier_cap = BORROWED_AMPLIFIER_CAP;
                se.amplifier_cap_power_multiplier =BORROWED_AMPLIFIER_CAP_MULTIPLIER;
                se.refresh_duration = src.refresh_duration;
                // Forcing SET (instead of copying the source song's own ADD) is what actually
                // stops the exploit: ADD lets repeated Secret Sonata casts climb the same way
                // repeated direct casts of one song do, which is how a caster could stack every
                // song's effect to its cap "for free". SET re-applies the same floor each time.
                se.apply_mode = Spell.Impact.Action.StatusEffect.ApplyMode.SET;
                se.apply_limit = src.apply_limit;
                se.show_particles = src.show_particles;
                se.remove = src.remove;
                action.status_effect = se;

                cappedImpact.action = action;
                result.add(cappedImpact);
            } else {
                result.add(impact);
            }
        }
        return result;
    }

    @Override
    public SpellHandlers.ImpactResult onSpellImpact(
            RegistryEntry<Spell> spell,
            SpellPower.Result powerResult,
            LivingEntity caster,
            Entity target,
            SpellHelper.ImpactContext context
    ) {
        if (caster.getWorld().isClient()) {
            return new SpellHandlers.ImpactResult(true, false);
        }

        var registry = SpellRegistry.from(caster.getWorld());
        var songsEntryList = registry.getEntryList(SONGS_TAG).orElse(null);
        if (songsEntryList == null || songsEntryList.size() == 0) {
            return new SpellHandlers.ImpactResult(false, false);
        }

        List<RegistryEntry<Spell>> candidates = new ArrayList<>();
        songsEntryList.forEach(candidates::add);

        Identifier currentId = spell.getKey().map(k -> k.getValue()).orElse(null);
        if (currentId != null) {
            candidates.removeIf(e -> e.getKey().map(k -> k.getValue().equals(currentId)).orElse(false));
        }
        if (candidates.isEmpty()) {
            return new SpellHandlers.ImpactResult(false, false);
        }

        RegistryEntry<Spell> selected = candidates.get(caster.getRandom().nextInt(candidates.size()));
        Spell selectedSpell = selected.value();

        if (selectedSpell.release != null) {
            ParticleHelper.sendBatches(caster, selectedSpell.release.particles);
            SoundHelper.playSound(caster.getWorld(), caster, selectedSpell.release.sound);
        }

        if (selectedSpell.impacts != null) {
            Vec3d pos = target instanceof LivingEntity lt ? lt.getEyePos() : caster.getEyePos();
            SpellHelper.ImpactContext ctx = new SpellHelper.ImpactContext()
                    .power(powerResult)
                    .position(pos)
                    .target(SpellTarget.FocusMode.DIRECT);

            var originalImpacts = selectedSpell.impacts;
            selectedSpell.impacts = withCappedStatusEffectAmplifiers(originalImpacts);
            try {
                SpellHelper.performImpacts(caster.getWorld(), caster, target, caster, selected,
                        selectedSpell.impacts, ctx, false, null);
            } finally {
                selectedSpell.impacts = originalImpacts;
            }
        }

        return new SpellHandlers.ImpactResult(true, false);
    }
}
