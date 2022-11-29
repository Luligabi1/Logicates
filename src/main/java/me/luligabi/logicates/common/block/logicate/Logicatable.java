package me.luligabi.logicates.common.block.logicate;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public interface Logicatable {

    LogicateType getLogicateType();

    List<MutableText> getLogicateTooltip();

    default void appendLogicateTooltip(List<Text> tooltip) {
        tooltip.add(getLogicateType().getText().formatted(Formatting.BLUE));
        tooltip.add(Text.empty());
        getLogicateTooltip().forEach(text -> tooltip.add(text.formatted(Formatting.GRAY)));
    }

    default void updateNeighbors(BlockState state, World world, BlockPos pos, DirectionProperty directionProperty) {
        world.updateNeighborsAlways(pos, state.getBlock());
        world.updateNeighborsAlways(pos.offset(state.get(directionProperty).getOpposite()), state.getBlock());
    }

}
