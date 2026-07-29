package com.bard_rpg.content;

import com.bard_rpg.BardsMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.CustomSpellHandler;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.SpellInfo;
import net.spell_engine.internals.SpellCooldownManager;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.SpellRegistry;
import net.spell_engine.internals.casting.SpellCasterEntity;
import net.spell_engine.utils.TargetHelper;

import java.util.ArrayList;
import java.util.List;

import static com.bard_rpg.BardsMod.MOD_ID;
import static net.spell_engine.internals.SpellRegistry.getSpell;

public class CustomSpellImpacts {

    // 1.20.1 Spell Engine has no spell tags, so the "songs" pool Secret Sonata draws from
    // is a real spell_pools.json entry (data/bards_rpg/spell_pools/secret_sonata_songs.json),
    // mirroring the tag-driven pool used by the 1.21.1 version.
    private static final Identifier SECRET_SONATA_SONGS_POOL = new Identifier(MOD_ID, "secret_sonata_songs");

    public static void register() {
        CustomSpellHandler.register(new Identifier(MOD_ID, "vicious_mockery"), (data) -> {
            CustomSpellHandler.Data d = (CustomSpellHandler.Data) data;
            if (d.caster().getWorld().isClient()) return true;
            for (Entity target : d.targets()) {
                SpellHelper.performImpacts(d.caster().getWorld(), d.caster(), target, target,
                        new SpellInfo(getSpell(new Identifier(MOD_ID, "vicious_mockery")), new Identifier(MOD_ID)),
                        d.impactContext());
                if (target instanceof MobEntity mob) {
                    mob.setTarget(d.caster());
                }
            }
            return false;
        });

        Identifier encoreId = new Identifier(MOD_ID, "encore");
        Float encoreReduction = 1.0F - BardsMod.tweaksConfig.value.encore_cooldown_reduction;
        CustomSpellHandler.register(encoreId, (data) -> {
            CustomSpellHandler.Data d = (CustomSpellHandler.Data) data;
            if (d.caster().getWorld().isClient()) return true;

            for (Entity target : d.targets()) {
                SpellHelper.performImpacts(d.caster().getWorld(), d.caster(), target, target,
                        new SpellInfo(getSpell(encoreId), encoreId), d.impactContext());
            }

            var cooldownManager = ((SpellCasterEntity) d.caster()).getCooldownManager();
            reduceOtherCooldowns(d.caster(), cooldownManager, encoreId, encoreReduction);

            float range = getSpell(encoreId).range;
            List<Entity> allies = d.caster().getWorld().getOtherEntities(d.caster(),
                    d.caster().getBoundingBox().expand(range),
                    target -> target instanceof ServerPlayerEntity && target != d.caster());
            for (Entity target : allies) {
                if (!(target instanceof ServerPlayerEntity ally)) continue;
                var allyCooldowns = ((SpellCasterEntity) ally).getCooldownManager();
                reduceOtherCooldowns(ally, allyCooldowns, encoreId, encoreReduction);
            }
            return true;
        });

        Identifier secretSonataId = new Identifier(MOD_ID, "secret_sonata");
        CustomSpellHandler.register(secretSonataId, (data) -> {
            CustomSpellHandler.Data d = (CustomSpellHandler.Data) data;
            if (d.caster().getWorld().isClient()) return true;
            // damage impacts for each target
            for (Entity target : d.targets()) {
                SpellHelper.performImpacts(d.caster().getWorld(), d.caster(), target, target,
                        new SpellInfo(getSpell(secretSonataId), new Identifier(MOD_ID)),
                        d.impactContext());
            }

            // Pick a random song from the dummy spell pool and re-apply just that song's own
            // STATUS_EFFECT impact, so its real effect_id/duration/amplifier definition is used
            // instead of a hardcoded one, and so harmful songs (e.g. discordant_note) go through
            // the same intent/relation checks performImpacts uses for every other spell.
            var pool = SpellRegistry.spellPool(SECRET_SONATA_SONGS_POOL);
            List<Identifier> candidates = new ArrayList<>(pool.spellIds());
            candidates.removeIf(secretSonataId::equals);
            if (candidates.isEmpty()) return true;

            Identifier songId = candidates.get(d.caster().getWorld().getRandom().nextInt(candidates.size()));
            Spell song = getSpell(songId);
            if (song == null || song.impact == null) return true;

            var statusEffectImpact = impactOfType(song, Spell.Impact.Action.Type.STATUS_EFFECT);
            if (statusEffectImpact == null) return true;

            var singleImpactSpell = new Spell();
            singleImpactSpell.school = song.school;
            singleImpactSpell.impact = new Spell.Impact[]{ statusEffectImpact };
            var singleImpactInfo = new SpellInfo(singleImpactSpell, songId);

            List<Entity> songTargets = new ArrayList<>(d.targets());
            if (!songTargets.contains(d.caster())) {
                songTargets.add(d.caster());
            }
            for (Entity target : songTargets) {
                SpellHelper.performImpacts(d.caster().getWorld(), d.caster(), target, target, singleImpactInfo, d.impactContext());
            }
            return false;
        });

        Identifier songOfTheTurningSkyId = new Identifier(MOD_ID, "song_of_the_turning_sky");
        CustomSpellHandler.register(songOfTheTurningSkyId, (data) -> {
            CustomSpellHandler.Data d = (CustomSpellHandler.Data) data;
            if (d.caster().getWorld().isClient()) return true;
            var spell = getSpell(songOfTheTurningSkyId);
            boolean performed = false;
            for (Entity target : d.targets()) {
                if (!(target instanceof LivingEntity living) || !living.isAttackable()) continue;

                Spell.Impact.Action.Type actionType;
                if (TargetHelper.getRelation(d.caster(), living) == TargetHelper.Relation.FRIENDLY) {
                    boolean lowHealth = living.getHealth() < living.getMaxHealth() * 0.5F;
                    actionType = lowHealth ? Spell.Impact.Action.Type.HEAL : Spell.Impact.Action.Type.STATUS_EFFECT;
                } else {
                    actionType = Spell.Impact.Action.Type.DAMAGE;
                }

                var chosenImpact = impactOfType(spell, actionType);
                if (chosenImpact == null) continue;

                var singleImpactSpell = new Spell();
                singleImpactSpell.school = spell.school;
                singleImpactSpell.impact = new Spell.Impact[]{ chosenImpact };
                var singleImpactInfo = new SpellInfo(singleImpactSpell, songOfTheTurningSkyId);

                boolean result = SpellHelper.performImpacts(d.caster().getWorld(), d.caster(), living, living, singleImpactInfo, d.impactContext());
                performed = performed || result;
            }
            return false;
        });
    }

    private static Spell.Impact impactOfType(Spell spell, Spell.Impact.Action.Type type) {
        for (var impact : spell.impact) {
            if (impact.action.type == type) return impact;
        }
        return null;
    }

    private static void reduceOtherCooldowns(LivingEntity caster, SpellCooldownManager cooldownManager,
                                              Identifier excludedSpellId, float reduction) {
        for (var entry : SpellRegistry.all().entrySet()) {
            Identifier id = entry.getKey();
            if (id.equals(excludedSpellId)) continue;
            if (!cooldownManager.isCoolingDown(id)) continue;
            Spell spell = entry.getValue().spell;
            float progress = cooldownManager.getCooldownProgress(id, 0);
            int totalTicks = Math.round(SpellHelper.getCooldownDuration(caster, spell) * 20F);
            cooldownManager.set(id, (int) (totalTicks * progress * reduction));
        }
    }
}
