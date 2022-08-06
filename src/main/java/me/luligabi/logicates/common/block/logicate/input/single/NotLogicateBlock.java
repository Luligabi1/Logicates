package me.luligabi.logicates.common.block.logicate.input.single;

import net.minecraft.block.BlockState;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class NotLogicateBlock extends SingleInputLogicateBlock {

    public NotLogicateBlock() {
        super();
        setDefaultState(getStateManager().getDefaultState().with(POWERED, true));
    }

    @Override
    protected int getPower(World world, BlockPos pos, BlockState state) {
        return getInput(world, pos, state) == 0 ? 15 : 0;
    }

    @Override
    protected List<MutableText> getLogicateTooltip() {
        return List.of(
                Text.translatable("tooltip.logicates.not_logicate")
        );
    }

}