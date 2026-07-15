package com.bard_rpg.mixin;

import com.bard_rpg.BardsMod;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.SpellContainer;
import net.spell_engine.internals.SpellContainerHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

@Mixin(SpellContainerHelper.class)
public abstract class RangedWeaponSpellSourceMixin {

    @Unique
    private static final Set<Identifier> BARD_SPELL_BOOK_IDS = Set.of(
            new Identifier(BardsMod.MOD_ID, "bard_spell_book"),
            new Identifier(BardsMod.MOD_ID, "bard2_spell_book")
    );

    @Inject(
            method = "getEquipped(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/PlayerEntity;)Lnet/spell_engine/api/spell/SpellContainer;",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void bards_rpg$mergeBardSpellBooks(ItemStack heldItemStack, PlayerEntity player, CallbackInfoReturnable<SpellContainer> cir) {
        boolean isRangedWeapon = heldItemStack.getItem() instanceof BowItem || heldItemStack.getItem() instanceof CrossbowItem;
        if (!isRangedWeapon) return;

        var bardSpellIds = bards_rpg$equippedBardBookSpellIds(player);
        if (bardSpellIds.isEmpty()) return;

        SpellContainer base = cir.getReturnValue();
        if (base != null) {
            var merged = new LinkedHashSet<String>(base.spell_ids);
            merged.addAll(bardSpellIds);
            cir.setReturnValue(new SpellContainer(base.content, false, null, 0, new ArrayList<>(merged)));
        } else {
            // Vanilla bows/crossbows carry no innate spell container, so build one from scratch
            // out of the equipped bard spellbooks instead of only merging into an existing base.
            cir.setReturnValue(new SpellContainer(SpellContainer.ContentType.ARCHERY, false, null, 0, new ArrayList<>(bardSpellIds)));
        }
    }

    @Unique
    private static Set<String> bards_rpg$equippedBardBookSpellIds(PlayerEntity player) {
        var component = TrinketsApi.getTrinketComponent(player);
        if (component.isEmpty()) return Set.of();

        var trinketComponent = component.get();
        var charm = trinketComponent.getInventory().get("charm");
        if (charm == null) return Set.of();
        var spellBookSlot = charm.get("spell_book");
        if (spellBookSlot == null) return Set.of();

        var items = new LinkedHashSet<ItemStack>();
        items.add(spellBookSlot.getStack(0));
        trinketComponent.getAllEquipped().forEach(pair -> items.add(pair.getRight()));

        var result = new LinkedHashSet<String>();
        for (ItemStack stack : items) {
            if (stack.isEmpty()) continue;
            var itemId = Registries.ITEM.getId(stack.getItem());
            if (!BARD_SPELL_BOOK_IDS.contains(itemId)) continue;

            var container = SpellContainerHelper.containerFromItemStack(stack);
            if (container != null && container.isValid()) {
                result.addAll(container.spell_ids);
            }
        }
        return result;
    }
}
