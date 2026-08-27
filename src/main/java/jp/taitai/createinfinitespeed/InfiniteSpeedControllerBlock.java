package jp.taitai.createinfinitespeed;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.HashSet;
import java.util.Set;

public class InfiniteSpeedControllerBlock extends AbstractSpeedControllerBlock {
    private static final Set<BlockPos> POWERED = new HashSet<>();
    private static final double BLAST_RADIUS = 50.0;
    private static final float DAMAGE = 10000.0f;

    /*
     * "通常より上の段階" is represented by a configurable high-speed threshold.
     * The prototype keeps the threshold at 256 RPM. The actual RPM GUI/kinetic
     * network integration is the next implementation stage.
     */
    public static final float HIGH_SPEED_THRESHOLD = 256.0f;

    public InfiniteSpeedControllerBlock() {
        super();
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos,
                                Block block, BlockPos fromPos, boolean moving) {
        super.neighborChanged(state, level, pos, block, fromPos, moving);
        if (level.isClientSide) return;

        if (state.getValue(POWERED)) {
            POWERED.add(pos.immutable());
            checkHighSpeedConflict((ServerLevel) level);
        } else {
            POWERED.remove(pos);
        }
    }

    private static void checkHighSpeedConflict(ServerLevel level) {
        /*
         * Prototype trigger:
         * two or more powered infinite controllers are treated as being in
         * the high-speed stage. This is deliberately isolated so the real
         * Create kinetic speed can replace this check later.
         */
        var active = POWERED.stream()
                .filter(p -> level.hasChunkAt(p))
                .filter(p -> level.getBlockState(p).getBlock() instanceof InfiniteSpeedControllerBlock)
                .toList();

        if (active.size() < 2) return;

        for (BlockPos origin : active) {
            burst(level, origin);
        }

        for (BlockPos origin : active) {
            if (level.getBlockState(origin).getBlock() instanceof InfiniteSpeedControllerBlock) {
                level.setBlock(origin,
                        level.getBlockState(origin).setValue(POWERED, false),
                        Block.UPDATE_ALL);
            }
            POWERED.remove(origin);
        }
    }

    private static void burst(ServerLevel level, BlockPos origin) {
        AABB area = new AABB(origin).inflate(BLAST_RADIUS);

        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, area)) {
            entity.hurt(level.damageSources().generic(), DAMAGE);
        }

        level.sendParticles(net.minecraft.core.particles.ParticleTypes.EXPLOSION,
                origin.getX() + .5, origin.getY() + .5, origin.getZ() + .5,
                16, 2, 2, 2, .1);
        level.playSound(null, origin, net.minecraft.sounds.SoundEvents.GENERIC_EXPLODE,
                net.minecraft.sounds.SoundSource.BLOCKS, 2.0f, 1.0f);
    }
}
