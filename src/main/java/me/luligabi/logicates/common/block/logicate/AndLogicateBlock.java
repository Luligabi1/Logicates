package me.luligabi.logicates.common.block.logicate;

import me.luligabi.logicates.common.block.DualInputLogicateBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class AndLogicateBlock extends DualInputLogicateBlock {


    @Override
    protected int getPower(World world, BlockPos pos, BlockState state) {
        Pair<Direction, Direction> inputSides = getInputSides(state);
        return (world.getEmittedRedstonePower(pos.offset(inputSides.getLeft()), inputSides.getLeft()) > 0) &&
                (world.getEmittedRedstonePower(pos.offset(inputSides.getRight()), inputSides.getRight()) > 0)
                ? 15 : 0;
    }

}