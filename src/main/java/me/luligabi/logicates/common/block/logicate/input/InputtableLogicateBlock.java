package me.luligabi.logicates.common.block.logicate.input;

import me.luligabi.logicates.common.block.logicate.LogicateBlock;
import me.luligabi.logicates.common.block.logicate.property.InputSides;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class InputtableLogicateBlock extends LogicateBlock {


    protected abstract EnumProperty<InputSides> getInputSideProperty();

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!player.getAbilities().allowModifyWorld) return ActionResult.PASS;
        world.setBlockState(pos, state.cycle(getInputSideProperty()));
        updateNeighbors(state, world, pos, FACING);
        return ActionResult.success(world.isClient);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(getInputSideProperty());
    }

}