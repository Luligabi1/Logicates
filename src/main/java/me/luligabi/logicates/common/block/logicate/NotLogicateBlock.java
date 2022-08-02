package me.luligabi.logicates.common.block.logicate;

import me.luligabi.logicates.common.block.SingleInputLogicateBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NotLogicateBlock extends SingleInputLogicateBlock {

    public NotLogicateBlock() {
        super();
        setDefaultState(getStateManager().getDefaultState().with(POWERED, true));
    }

    @Override
    protected int getPower(World world, BlockPos pos, BlockState state) {
        return getInput(world, pos, state) == 0 ? 15 : 0;
    }

}