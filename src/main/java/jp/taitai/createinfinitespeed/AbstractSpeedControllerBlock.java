package jp.taitai.createinfinitespeed;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public abstract class AbstractSpeedControllerBlock extends Block {

    public static final BooleanProperty POWERED =
            BooleanProperty.create("powered");

    protected AbstractSpeedControllerBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(3.5f)
                .requiresCorrectToolForDrops());

        registerDefaultState(
                defaultBlockState().setValue(POWERED, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @Override
    public void neighborChanged(
            BlockState state,
            Level level,
            BlockPos pos,
            Block block,
            BlockPos fromPos,
            boolean moving) {

        if (level.isClientSide) return;

        boolean powered = level.hasNeighborSignal(pos);

        if (powered != state.getValue(POWERED)) {
            level.setBlock(
                    pos,
                    state.setValue(POWERED, powered),
                    Block.UPDATE_ALL
            );
        }

        super.neighborChanged(
                state,
                level,
                pos,
                block,
                fromPos,
                moving
        );
    }

    public static boolean hasRedstone(Level level, BlockPos pos) {
        return level.hasNeighborSignal(pos);
    }
}
