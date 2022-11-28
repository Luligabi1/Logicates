package me.luligabi.logicates.common.block.logicate.input.single;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;

import java.util.List;

public class ToggleLogicateBlock extends SingleInputLogicateBlock {

    public ToggleLogicateBlock() {
        super();
        setDefaultState(getDefaultState().with(INPUT_POWER, false));
    }

    @Override
    protected void updatePowered(World world, BlockPos pos, BlockState state) {
        boolean hasInputPower = state.get(INPUT_POWER);
        boolean hasBlockPower = getInput(world, pos, state) > 0;

        if(world.getBlockTickScheduler().isTicking(pos, this)) return;
        if(hasInputPower == hasBlockPower) return;

        TickPriority tickPriority = TickPriority.HIGH;
        if(isTargetNotAligned(world, pos, state)) {
            tickPriority = TickPriority.EXTREMELY_HIGH;
        } else if(hasInputPower) {
            tickPriority = TickPriority.VERY_HIGH;
        }

        world.createAndScheduleBlockTick(pos, this, getUpdateDelayInternal(state), tickPriority);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        boolean hasInputPower = state.get(INPUT_POWER);
        boolean hasBlockPower = getInput(world, pos, state) > 0;

        if(hasInputPower && !hasBlockPower) {
            world.setBlockState(pos, state.with(INPUT_POWER, false), 2);
        } else if(!hasInputPower) {
            world.setBlockState(pos, state.with(INPUT_POWER, true).with(POWERED, !state.get(POWERED)), 2);
            if(!hasBlockPower) {
                world.createAndScheduleBlockTick(pos, this, getUpdateDelayInternal(state), TickPriority.VERY_HIGH);
            }
        }
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        if(!state.get(POWERED)) return 0;
        return state.get(FACING) == direction ? 15 : 0;
    }

    @Override
    protected boolean isValidInput(BlockState state) {
        return isRedstoneGate(state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(INPUT_POWER);
    }

    @Override
    public List<MutableText> getLogicateTooltip() {
        return List.of(
                Text.translatable("tooltip.logicates.toggle_logicate")
        );
    }


    public static final BooleanProperty INPUT_POWER = BooleanProperty.of("input_power");

}