package com.bards.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.fx.ReleaseFx;
import net.spell_engine.internals.SpellExecution;
import net.spell_engine.internals.impact.SpellImpacts;
import net.spell_engine.internals.target.SpellTarget;
import net.spell_engine.utils.TargetHelper;
import net.spell_power.api.SpellPower;

import static com.bards.BardsMod.MOD_ID;

public class CanticlesOfTheTidesEffect extends StatusEffect {
    private static final Identifier HEAL_IMPACT_ID = Identifier.of(MOD_ID, "helper/canticle_of_the_tides_heal");

    protected CanticlesOfTheTidesEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity.getWorld().isClient()) return true;

        var registry = SpellRegistry.from(entity.getWorld());
        var spellEntry = registry.getEntry(HEAL_IMPACT_ID).orElse(null);
        if (spellEntry == null) return true;

        Spell spell = spellEntry.value();
        if (spell.impacts == null || spell.impacts.isEmpty()) return true;

        SpellPower.Result power = SpellPower.getSpellPower(spell.school, entity);
        SpellExecution.ImpactContext ctx = new SpellExecution.ImpactContext()
                .power(power)
                .position(entity.getPos())
                .target(SpellTarget.FocusMode.AREA);

        if (spell.release != null) {
            // Emits the release visuals with `scale_with = RANGE` bound to the holder's own
            // reach, replacing the hand-rolled `particles_scaled_with_ranged` loop. The helper
            // spell authors no release sound, so `ReleaseFx`'s sound step is a no-op here.
            ReleaseFx.send(entity.getWorld(), entity, spellEntry, 1F);
        }

        if (spell.target != null && spell.target.area != null && spell.target.area.include_caster) {
            SpellImpacts.performImpacts(entity.getWorld(), entity, entity, entity, spellEntry, spell.impacts, ctx, false, null);
        }

        float range = spell.range > 0 ? spell.range : 2.0F;
        for (Entity target : TargetHelper.targetsFromArea(entity, range, spell.target != null ? spell.target.area : null, e -> e != entity)) {
            SpellImpacts.performImpacts(entity.getWorld(), entity, target, entity, spellEntry, spell.impacts, ctx, false, null);
        }

        return true;
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        if (duration % 60 == 0) return true;
        for (int i = 1; i <= amplifier; i++) {
            if (duration % 60 == (60 - i * 5) % 60) return true;
        }
        return false;
    }
}
