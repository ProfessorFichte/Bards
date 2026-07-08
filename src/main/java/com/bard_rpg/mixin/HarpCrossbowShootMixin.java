package com.bard_rpg.mixin;

import com.bard_rpg.content.BardsSounds;
import com.bard_rpg.item.HarpCrossbowItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrossbowItem.class)
public class HarpCrossbowShootMixin {

    @Inject(method = "shootAll", at = @At("HEAD"))
    private static void bards_rpg$harpCrossbowShootAll(World world, LivingEntity shooter, Hand hand, ItemStack stack, float speed, float divergence, CallbackInfo ci) {
        if (!(stack.getItem() instanceof HarpCrossbowItem)) return;
        if (world.isClient()) return;

        boolean isFirework = CrossbowItem.hasProjectile(stack, Items.FIREWORK_ROCKET);
        ItemStack fireworkStack = ItemStack.EMPTY;
        if (isFirework) {
            NbtList list = stack.getOrCreateNbt().getList("ChargedProjectiles", NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < list.size(); i++) {
                ItemStack proj = ItemStack.fromNbt(list.getCompound(i));
                if (proj.isOf(Items.FIREWORK_ROCKET)) {
                    fireworkStack = proj;
                    break;
                }
            }
        }

        Identifier itemId = Registries.ITEM.getId(stack.getItem());
        float[] offsets = itemId.getPath().equals("unique_harp_crossbow_0")
                ? new float[]{-1.75f, -0.75f, 0.75f, 1.75f}
                : new float[]{-0.75f, 0.75f};

        Vec3d look = shooter.getRotationVector();
        Vec3d up = new Vec3d(0, 1, 0);
        Vec3d right = look.crossProduct(up).normalize();
        if (right.lengthSquared() < 1e-6) right = new Vec3d(1, 0, 0);

        Vec3d eyePos = shooter.getEyePos();
        final ItemStack fw = fireworkStack;

        for (float offset : offsets) {
            Vec3d pos = eyePos.add(right.multiply(offset));
            if (isFirework) {
                FireworkRocketEntity rocket = new FireworkRocketEntity(world, fw.copy(), shooter, pos.x, pos.y, pos.z, true);
                rocket.setVelocity(look.x * speed, look.y * speed, look.z * speed);
                world.spawnEntity(rocket);
            } else {
                ArrowEntity arrow = new ArrowEntity(world, pos.x, pos.y, pos.z);
                arrow.setOwner(shooter);
                arrow.setVelocity(look.x * speed, look.y * speed, look.z * speed);
                arrow.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
                world.spawnEntity(arrow);
            }
        }

        world.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(),
                BardsSounds.harp_crossbow_shoot.soundEvent(), SoundCategory.PLAYERS,
                1.0F, 0.9F + world.getRandom().nextFloat() * 0.2F);
    }
}
