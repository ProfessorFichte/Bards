package com.bard_rpg.content;

import com.bard_rpg.effect.BardsEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.CustomSpellHandler;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.SpellInfo;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.SpellRegistry;
import net.spell_engine.internals.casting.SpellCasterEntity;

import java.util.List;

import static com.bard_rpg.BardsMod.MOD_ID;
import static net.spell_engine.internals.SpellRegistry.getSpell;

public class CustomSpellImpacts {

    private static final List<String> BARD_SONGS = List.of(
            MOD_ID + ":troubadours_minuet",
            MOD_ID + ":wanderers_minuet",
            MOD_ID + ":natures_minne",
            MOD_ID + ":song_of_celerity",
            MOD_ID + ":song_of_the_turning_sky",
            MOD_ID + ":discordant_note",
            MOD_ID + ":tale_of_the_dragonslayer",
            MOD_ID + ":hymn_of_the_golden_light",
            MOD_ID + ":canticle_of_the_tides"
    );

    public static void register() {
        // Handler ID must match the spell's own identifier ("vicious_mockery", not "vicious_mockery_taunt")
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
            return true;
        });

        Identifier encoreId = new Identifier(MOD_ID, "encore");
        CustomSpellHandler.register(encoreId, (data) -> {
            CustomSpellHandler.Data d = (CustomSpellHandler.Data) data;
            if (d.caster().getWorld().isClient()) return true;
            // Reduce caster's cooldowns to 20% of remaining
            var cooldownManager = ((SpellCasterEntity) d.caster()).getCooldownManager();
            for (var entry : SpellRegistry.all().entrySet()) {
                Identifier id = entry.getKey();
                if (id.equals(encoreId)) continue;
                if (!cooldownManager.isCoolingDown(id)) continue;
                Spell spell = entry.getValue().spell;
                float progress = cooldownManager.getCooldownProgress(id, 0);
                int totalTicks = Math.round(SpellHelper.getCooldownDuration(d.caster(), spell) * 20F);
                cooldownManager.set(id, (int)(totalTicks * progress * 0.2f));
            }
            // Reduce cooldowns for all nearby allied players
            float range = getSpell(encoreId).range;
            List<Entity> allies = d.caster().getWorld().getOtherEntities(d.caster(),
                    d.caster().getBoundingBox().expand(range),
                    target -> target instanceof ServerPlayerEntity && target != d.caster());
            for (Entity target : allies) {
                if (!(target instanceof ServerPlayerEntity ally)) continue;
                var allyCooldowns = ((SpellCasterEntity) ally).getCooldownManager();
                for (var entry : SpellRegistry.all().entrySet()) {
                    Identifier id = entry.getKey();
                    if (id.equals(encoreId)) continue;
                    if (!allyCooldowns.isCoolingDown(id)) continue;
                    Spell spell = entry.getValue().spell;
                    float progress = allyCooldowns.getCooldownProgress(id, 0);
                    int totalTicks = Math.round(SpellHelper.getCooldownDuration(ally, spell) * 20F);
                    allyCooldowns.set(id, (int)(totalTicks * progress * 0.2f));
                }
            }
            return true;
        });

        CustomSpellHandler.register(new Identifier(MOD_ID, "secret_sonata"), (data) -> {
            CustomSpellHandler.Data d = (CustomSpellHandler.Data) data;
            if (d.caster().getWorld().isClient()) return true;
            // Apply normal impacts (DAMAGE) for each target
            for (Entity target : d.targets()) {
                SpellHelper.performImpacts(d.caster().getWorld(), d.caster(), target, target,
                        new SpellInfo(getSpell(new Identifier(MOD_ID, "secret_sonata")), new Identifier(MOD_ID)),
                        d.impactContext());
            }
            // Apply a random bard song effect to all targets and the caster
            int songIndex = d.caster().getWorld().getRandom().nextInt(BARD_SONGS.size());
            String songId = BARD_SONGS.get(songIndex);
            var effectToApply = switch (songId) {
                case MOD_ID + ":troubadours_minuet" -> BardsEffects.TROUBADOURS_MINUET;
                case MOD_ID + ":wanderers_minuet" -> BardsEffects.WANDERERS_MINUET;
                case MOD_ID + ":natures_minne" -> BardsEffects.NATURES_MINNE;
                case MOD_ID + ":song_of_celerity" -> BardsEffects.SONG_OF_CELERITY;
                case MOD_ID + ":song_of_the_turning_sky" -> BardsEffects.SONG_OF_THE_TURNING_SKY;
                case MOD_ID + ":discordant_note" -> null;
                case MOD_ID + ":tale_of_the_dragonslayer" -> BardsEffects.TALE_OF_THE_DRAGON_SLAYER;
                case MOD_ID + ":hymn_of_the_golden_light" -> BardsEffects.HYMN_OF_THE_GOLDEN_LIGHT;
                case MOD_ID + ":canticle_of_the_tides" -> BardsEffects.CANTICLES_OF_THE_TIDES;
                default -> null;
            };
            if (effectToApply == null) return true;
            for (Entity target : d.targets()) {
                if (target instanceof LivingEntity living) {
                    living.addStatusEffect(new StatusEffectInstance(effectToApply, 180, 0));
                }
            }
            d.caster().addStatusEffect(new StatusEffectInstance(effectToApply, 180, 0));
            return true;
        });
    }
}
