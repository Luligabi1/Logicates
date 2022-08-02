package me.luligabi.logicates.common.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.Direction;

public abstract class LogicateBlock extends AbstractRedstoneGateBlock {

    protected LogicateBlock() {
        super(FabricBlockSettings.copyOf(Blocks.REPEATER));
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(POWERED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
    }

    @Override
    protected int getUpdateDelayInternal(BlockState state) {
        return 2;
    }

}