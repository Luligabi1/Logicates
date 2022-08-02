package me.luligabi.logicates.common.block.logicate;

import me.luligabi.logicates.common.block.DualInputLogicateBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OrLogicateBlock extends DualInputLogicateBlock {


    @Override
    protected int getPower(World world, BlockPos pos, BlockState state) {
        return (getInputA(world, pos, state) > 0 || getInputB(world, pos, state) > 0) ? 15 : 0;
    }

}
