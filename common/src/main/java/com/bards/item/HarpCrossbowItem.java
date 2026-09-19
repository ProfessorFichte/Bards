package com.bards.item;

import com.bards.content.BardsSounds;
import net.fabric_extras.ranged_weapon.api.CustomCrossbow;
import net.fabric_extras.ranged_weapon.api.RangedConfig;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtElement;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.spell_engine.internals.SpellTriggers;
import net.spell_engine.internals.delivery.arrow.ArrowExtension;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class HarpCrossbowItem extends CustomCrossbow {
    public HarpCrossbowItem(Settings settings, RangedConfig config, Supplier<Ingredient> repairIngredientSupplier) {
        super(settings, config, repairIngredientSupplier);
    }

    public static final String TOOLTIP_KEY = "item.bards_rpg.harp_crossbow.tooltip";

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable(TOOLTIP_KEY));
    }

    /// 1.20.1's `CrossbowItem.shootAll` is `public static` (it only became an overridable instance method in
    /// 1.21), so the extra volley is hung off `use` instead — the one call site that fires a charged crossbow
    /// for a player. Consequence: a mob `CrossbowUser` firing this weapon shoots the vanilla single bolt.
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!CrossbowItem.isCharged(stack)) {
            return super.use(world, user, hand);
        }

        ItemStack fireworkStack = chargedFirework(stack);
        boolean isFirework = !fireworkStack.isEmpty();
        // Same speed vanilla's `use` passes to `shootAll` (`CrossbowItem.getSpeed`, which is private).
        float speed = isFirework ? 1.6F : 3.15F;

        var result = super.use(world, user, hand);
        if (world.isClient()) return result;

        shootExtraProjectiles(world, user, stack, fireworkStack, speed);
        return result;
    }

    private static ItemStack chargedFirework(ItemStack crossbow) {
        var nbt = crossbow.getNbt();
        if (nbt == null || !nbt.contains("ChargedProjectiles", NbtElement.LIST_TYPE)) return ItemStack.EMPTY;
        var list = nbt.getList("ChargedProjectiles", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < list.size(); i++) {
            var projectile = ItemStack.fromNbt(list.getCompound(i));
            if (projectile.isOf(Items.FIREWORK_ROCKET)) return projectile;
        }
        return ItemStack.EMPTY;
    }

    private void shootExtraProjectiles(World world, LivingEntity shooter, ItemStack stack, ItemStack fireworkStack, float speed) {
        Identifier itemId = Registries.ITEM.getId(stack.getItem());
        boolean isLightning = itemId.getPath().equals("unique_harp_crossbow_0");

        Vec3d look = shooter.getRotationVector();
        Vec3d up = new Vec3d(0, 1, 0);
        Vec3d right = look.crossProduct(up).normalize();
        if (right.lengthSquared() < 1e-6) {
            right = new Vec3d(1, 0, 0);
        }

        Vec3d eyePos = shooter.getEyePos();
        float[] offsets = isLightning ? new float[]{-1.75f, -0.75f, 0.75f, 1.75f} : new float[]{-0.75f, 0.75f};

        boolean isFirework = !fireworkStack.isEmpty();
        for (float offset : offsets) {
            Vec3d spawnPos = eyePos.add(right.multiply(offset));
            if (isFirework) {
                FireworkRocketEntity rocket = new FireworkRocketEntity(world, fireworkStack.copy(), shooter,
                        spawnPos.x, spawnPos.y, spawnPos.z, true);
                rocket.setVelocity(look.x * speed, look.y * speed, look.z * speed);
                world.spawnEntity(rocket);
            } else {
                ArrowEntity arrow = new ArrowEntity(world, spawnPos.x, spawnPos.y, spawnPos.z);
                arrow.setOwner(shooter);
                arrow.setVelocity(look.x * speed, look.y * speed, look.z * speed);
                arrow.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
                world.spawnEntity(arrow);
                if (shooter instanceof PlayerEntity player) {
                    SpellTriggers.onArrowShot((ArrowExtension)(Object) arrow, player, false);
                }
            }
        }

        world.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(),
                BardsSounds.harp_crossbow_shoot.soundEvent(), SoundCategory.PLAYERS,
                1.0F, 0.9F + world.getRandom().nextFloat() * 0.2F);
    }
}
