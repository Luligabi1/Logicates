package me.luligabi.logicates.common.block.logicate.misc.neonlamp;

import me.luligabi.logicates.common.block.logicate.Logicatable;
import me.luligabi.logicates.common.block.logicate.LogicateType;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneLampBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NeonLampBlock extends RedstoneLampBlock implements Logicatable {

    public NeonLampBlock(DyeColor color, boolean inverted) {
        super(FabricBlockSettings.copyOf(Blocks.REDSTONE_LAMP).mapColor(color));
        this.inverted = inverted;
        setDefaultState(getDefaultState().with(LIT, inverted));
    }


    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        boolean poweringCondition = ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos());

        if(inverted) poweringCondition = !poweringCondition;
        return getDefaultState().with(LIT, poweringCondition);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if(world.isClient()) return;

        boolean isPowered = state.get(LIT);
        boolean powerOffCondition = world.isReceivingRedstonePower(pos);
        if(inverted) powerOffCondition = !powerOffCondition;
        
        if(isPowered != powerOffCondition) {
            if(isPowered) {
                world.scheduleBlockTick(pos, this, 4);
            } else {
                world.setBlockState(pos, state.cycle(LIT), 2);
            }
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        boolean powerOffCondition = !world.isReceivingRedstonePower(pos);
        if(inverted) powerOffCondition = !powerOffCondition;

        if(state.get(LIT) && powerOffCondition) {
            world.setBlockState(pos, state.cycle(LIT), 2);
        }
    }


    @Override
    public LogicateType getLogicateType() {
        return LogicateType.MISCELLANEOUS;
    }

    @Override
    public List<MutableText> getLogicateTooltip() { // TODO
        return List.of();
    }

    private final boolean inverted;
}
