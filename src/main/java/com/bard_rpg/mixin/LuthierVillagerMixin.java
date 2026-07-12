package com.bard_rpg.mixin;

import com.bard_rpg.worldgen.villages.BardVillagerProfessions;
import com.bard_rpg.worldgen.villages.BardVillagerTrades;
import com.bard_rpg.worldgen.villages.LuthierSongs;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.Schedule;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.StopSoundS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.VillagerData;
import net.spell_engine.api.spell.ParticleBatch;
import net.spell_engine.particle.ParticleHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static net.minecraft.state.property.Properties.HORIZONTAL_FACING;

@Mixin(VillagerEntity.class)
public abstract class LuthierVillagerMixin {

    @Shadow public abstract VillagerData getVillagerData();
    @Shadow public abstract Brain<VillagerEntity> getBrain();

    @Unique private int bards_tickCounter = 0;
    @Unique private long bards_lastSoundTime = -1L;
    @Unique private long bards_hurtUntil = 0L;
    @Unique private boolean bards_wasPlaying = false;
    @Unique private boolean bards_isReacting = false;
    @Unique private int bards_reactionTickCount = 0;
    @Unique private int bards_nearbyLuthierId = -1;
    @Unique private float bards_spinYaw = 0;

    @WrapOperation(
            method = "initBrain",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/brain/Brain;setSchedule(Lnet/minecraft/entity/ai/brain/Schedule;)V")
    )
    private void wrapInitBrain(Brain instance, Schedule schedule, Operation<Void> original) {
        if (getVillagerData().getProfession().equals(BardVillagerProfessions.LUTHIER)) {
            original.call(instance, BardVillagerTrades.LUTHIER_SCHEDULE);
        } else {
            original.call(instance, schedule);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        VillagerEntity villager = (VillagerEntity)(Object) this;
        if (!(villager.getWorld() instanceof ServerWorld serverWorld)) return;

        long timeOfDay = serverWorld.getTimeOfDay() % 24000;
        boolean isPerformanceTime = timeOfDay >= 8500 && timeOfDay < 11000;

        if (villager.getVillagerData().getProfession().equals(BardVillagerProfessions.LUTHIER)) {
            bards_tickLuthier(villager, serverWorld, isPerformanceTime);
        } else {
            bards_tickReaction(villager, serverWorld, isPerformanceTime);
        }
    }

    @Unique
    private void bards_tickLuthier(VillagerEntity villager, ServerWorld serverWorld, boolean isPerformanceTime) {
        if (!isPerformanceTime) {
            if (!villager.getEquippedStack(EquipmentSlot.MAINHAND).isEmpty()) {
                villager.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            }
            bards_tickCounter = 0;
            bards_lastSoundTime = -1L;
            bards_wasPlaying = false;
            return;
        }

        var jobSite = villager.getBrain().getOptionalRegisteredMemory(MemoryModuleType.JOB_SITE);
        if (jobSite.isEmpty()) return;

        BlockPos standPos = jobSite.get().getPos();
        var standState = serverWorld.getBlockState(standPos);
        Direction facing = standState.contains(HORIZONTAL_FACING)
                ? standState.get(HORIZONTAL_FACING)
                : Direction.NORTH;

        BlockPos targetPos = standPos.offset(facing);
        int dist = villager.getBlockPos().getManhattanDistance(targetPos);

        if (dist > 2) {
            if (villager.getNavigation().isIdle()) {
                villager.getNavigation().startMovingTo(
                        targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5, 0.5);
            }
            return;
        }

        villager.getNavigation().stop();
        Vec3d vel = villager.getVelocity();
        villager.setVelocity(0, Math.min(vel.y, 0), 0);

        double dx = standPos.getX() + 0.5 - villager.getX();
        double dz = standPos.getZ() + 0.5 - villager.getZ();
        float targetYaw = (float)(Math.atan2(-dx, dz) * (180.0 / Math.PI));
        villager.setYaw(targetYaw);
        villager.setBodyYaw(targetYaw);
        villager.setHeadYaw(targetYaw);

        LuthierSongs.Song song = bards_getDailySong(villager, serverWorld);
        villager.equipStack(EquipmentSlot.MAINHAND, new ItemStack(song.heldInstrument()));

        long currentTime = serverWorld.getTime();

        if (villager.hurtTime > 0 && currentTime >= bards_hurtUntil) {
            bards_hurtUntil = currentTime + 60;
            bards_lastSoundTime = currentTime;
            bards_stopSongForNearbyPlayers(serverWorld, villager, song);
        }

        if (currentTime < bards_hurtUntil) return;

        if (bards_lastSoundTime < 0 || currentTime - bards_lastSoundTime >= song.soundDurationTicks()) {
            villager.playSound(song.sound(), 1.0f, 1.0f);
            bards_lastSoundTime = currentTime;
        }

        if (bards_tickCounter++ % 20 != 0) return;

        var noteBatch = new ParticleBatch(song.noteParticleId(), ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                null, 5, 0.1F, 0.2F, 0);
        ParticleHelper.sendBatches(villager, new ParticleBatch[]{ noteBatch });

        Box effectBox = Box.of(villager.getPos(), 8, 4, 8);
        List<PlayerEntity> nearbyPlayers = serverWorld.getEntitiesByClass(PlayerEntity.class, effectBox, p -> true);
        for (PlayerEntity player : nearbyPlayers) {
            player.addStatusEffect(new StatusEffectInstance(song.effect(), 200, song.effectAmplifier(), false, true, true));
        }
    }

    @Unique
    private void bards_tickReaction(VillagerEntity villager, ServerWorld serverWorld, boolean isPerformanceTime) {
        if (bards_reactionTickCount++ % 20 == 0) {
            if (!isPerformanceTime) {
                bards_isReacting = false;
                bards_nearbyLuthierId = -1;
            } else {
                VillagerEntity found = bards_findPlayingLuthier(villager, serverWorld);
                if (found == null) {
                    bards_isReacting = false;
                    bards_nearbyLuthierId = -1;
                } else {
                    bards_nearbyLuthierId = found.getId();
                    if (!bards_isReacting) {
                        bards_isReacting = bards_isNitwit(villager) || villager.getRandom().nextFloat() < 0.65f;
                    }
                }
            }
        }

        if (!bards_isReacting || bards_nearbyLuthierId < 0) {
            bards_spinYaw = 0;
            return;
        }

        if (!(serverWorld.getEntityById(bards_nearbyLuthierId) instanceof VillagerEntity luthier)) {
            bards_isReacting = false;
            bards_nearbyLuthierId = -1;
            return;
        }

        boolean isNitwit = bards_isNitwit(villager);

        if (isNitwit) {
            villager.getNavigation().stop();
            double ndx = luthier.getX() - villager.getX();
            double ndz = luthier.getZ() - villager.getZ();
            float lookYaw = (float)(Math.atan2(-ndx, ndz) * (180.0 / Math.PI));
            villager.setYaw(lookYaw);
            villager.setBodyYaw(lookYaw);
            villager.setHeadYaw(lookYaw);
        } else {
            villager.getNavigation().stop();
            bards_spinYaw += 8.0f;
            villager.setBodyYaw(bards_spinYaw);
            villager.setHeadYaw(bards_spinYaw);

            if (bards_reactionTickCount % 30 == 0) {
                LuthierSongs.Song song = bards_getDailySong(luthier, serverWorld);
                var noteBatch = new ParticleBatch(song.noteParticleId(), ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        null, 2, 0.05F, 0.2F, 0);
                ParticleHelper.sendBatches(villager, new ParticleBatch[]{ noteBatch });
            }
        }
    }

    @Unique
    private static boolean bards_isNitwit(VillagerEntity villager) {
        var id = net.minecraft.registry.Registries.VILLAGER_PROFESSION.getId(villager.getVillagerData().getProfession());
        return id != null && "minecraft".equals(id.getNamespace()) && "nitwit".equals(id.getPath());
    }

    @Unique
    private static VillagerEntity bards_findPlayingLuthier(VillagerEntity self, ServerWorld world) {
        Box searchBox = Box.of(self.getPos(), 20, 8, 20);
        List<VillagerEntity> nearby = world.getEntitiesByClass(VillagerEntity.class, searchBox,
                e -> e != self && e.getVillagerData().getProfession().equals(BardVillagerProfessions.LUTHIER));
        for (VillagerEntity luthier : nearby) {
            if (self.canSee(luthier)) return luthier;
        }
        return null;
    }

    @Unique
    private static void bards_stopSongForNearbyPlayers(ServerWorld world, VillagerEntity villager, LuthierSongs.Song song) {
        var packet = new StopSoundS2CPacket(song.soundId(), null);
        Box range = Box.of(villager.getPos(), 64, 32, 64);
        for (PlayerEntity player : world.getEntitiesByClass(PlayerEntity.class, range, p -> true)) {
            if (player instanceof ServerPlayerEntity serverPlayer) {
                serverPlayer.networkHandler.sendPacket(packet);
            }
        }
    }

    @Unique
    private static LuthierSongs.Song bards_getDailySong(VillagerEntity villager, ServerWorld world) {
        long day = world.getTimeOfDay() / 24000;
        int idx = (int) Math.abs((villager.getUuid().getLeastSignificantBits() ^ day) % LuthierSongs.SONGS.size());
        return LuthierSongs.SONGS.get(idx);
    }
}
