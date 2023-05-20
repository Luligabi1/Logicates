package me.luligabi.logicates.common.block.logicate.inputless.weather;

import me.luligabi.logicates.common.block.BlockRegistry;
import me.luligabi.logicates.common.block.logicate.inputless.InputlessLogicateBlock;
import me.luligabi.logicates.common.block.logicate.property.PropertyRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WeatherLogicateBlock extends InputlessLogicateBlock implements BlockEntityProvider {


    public WeatherLogicateBlock() {
        super(FabricBlockSettings.copyOf(Blocks.REPEATER));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!player.getAbilities().allowModifyWorld) return ActionResult.PASS;
        world.setBlockState(pos, state.cycle(PropertyRegistry.WEATHER_TYPE));
        updateNeighbors(state, world, pos, FACING);
        return ActionResult.success(world.isClient);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WeatherLogicateBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return !world.isClient && world.getDimension().hasSkyLight() ? checkType(type, BlockRegistry.WEATHER_LOGICATE_BLOCK_ENTITY_TYPE, WeatherLogicateBlockEntity::tick) : null;
    }

    @Override
    public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
        super.onSyncedBlockEvent(state, world, pos, type, data);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity != null && blockEntity.onSyncedBlockEvent(type, data);
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> checkType(BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
        return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(PropertyRegistry.WEATHER_TYPE);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
    }

    @Override
    public List<MutableText> getLogicateTooltip() {
        return List.of(
                Text.translatable("tooltip.logicates.weather_logicate.1"),
                Text.translatable("tooltip.logicates.weather_logicate.2")
        );
    }

}