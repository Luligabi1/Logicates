package me.luligabi.logicates.common.block.logicate.input.dual;

import net.minecraft.block.BlockState;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class NandLogicateBlock extends DualInputLogicateBlock {

    public NandLogicateBlock() {
        setDefaultState(getStateManager().getDefaultState().with(POWERED, true));
    }


    @Override
    protected int getPower(World world, BlockPos pos, BlockState state) {
        return (getInputA(world, pos, state) <= 0 || getInputB(world, pos, state) <= 0) ? 15 : 0;
    }

    @Override
    public List<MutableText> getLogicateTooltip() {
        return List.of(
                Text.translatable("tooltip.logicates.nand_logicate")
        );
    }

}
