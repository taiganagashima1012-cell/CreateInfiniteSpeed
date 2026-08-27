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

public class InfiniteSpeedControllerBlock
        extends AbstractSpeedControllerBlock {

    // 稼働中の無限速度コントローラー
    private static final Set<BlockPos> ACTIVE_CONTROLLERS =
            new HashSet<>();

    private static final double BLAST_RADIUS = 50.0;
    private static final float DAMAGE = 10000.0f;

    /*
     * 通常より上の段階
     * 現在はプロトタイプとして256 RPM
     */
    public static final float HIGH_SPEED_THRESHOLD = 256.0f;

    public InfiniteSpeedControllerBlock() {
        super();
    }

    @Override
    public void neighborChanged(
            BlockState state,
            Level level,
            BlockPos pos,
            Block block,
            BlockPos fromPos,
            boolean moving) {

        super.neighborChanged(
                state,
                level,
                pos,
                block,
                fromPos,
                moving
        );

        if (level.isClientSide) return;

        if (state.getValue(POWERED)) {

            ACTIVE_CONTROLLERS.add(pos.immutable());

            checkHighSpeedConflict((ServerLevel) level);

        } else {

            ACTIVE_CONTROLLERS.remove(pos);

        }
    }

    private static void checkHighSpeedConflict(ServerLevel level) {

        var active = ACTIVE_CONTROLLERS.stream()
                .filter(level::hasChunkAt)
                .filter(p ->
                        level.getBlockState(p).getBlock()
                                instanceof InfiniteSpeedControllerBlock)
                .toList();

        // 2台以上なら暴走
        if (active.size() < 2) return;

        for (BlockPos origin : active) {
            burst(level, origin);
        }

        // 暴走後、すべてOFF
        for (BlockPos origin : active) {

            BlockState state = level.getBlockState(origin);

            if (state.getBlock()
                    instanceof InfiniteSpeedControllerBlock) {

                level.setBlock(
                        origin,
                        state.setValue(POWERED, false),
                        Block.UPDATE_ALL
                );
            }

            ACTIVE_CONTROLLERS.remove(origin);
        }
    }

    private static void burst(
            ServerLevel level,
            BlockPos origin) {

        AABB area = new AABB(origin).inflate(BLAST_RADIUS);

        for (LivingEntity entity :
                level.getEntitiesOfClass(
                        LivingEntity.class,
                        area)) {

            entity.hurt(
                    level.damageSources().generic(),
                    DAMAGE
            );
        }

        level.sendParticles(
                net.minecraft.core.particles.ParticleTypes.EXPLOSION,
                origin.getX() + .5,
                origin.getY() + .5,
                origin.getZ() + .5,
                16,
                2,
                2,
                2,
                .1
        );

        level.playSound(
                null,
                origin,
                net.minecraft.sounds.SoundEvents.GENERIC_EXPLODE,
                net.minecraft.sounds.SoundSource.BLOCKS,
                2.0f,
                1.0f
        );
    }
}
