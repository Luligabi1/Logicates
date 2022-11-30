package me.luligabi.logicates.common.block.logicate.inputless.keypad;

import me.luligabi.logicates.common.block.BlockRegistry;
import me.luligabi.logicates.common.block.logicate.Logicatable;
import me.luligabi.logicates.common.block.logicate.LogicateType;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
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
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class KeypadLogicateBlock extends AbstractRedstoneGateBlock implements Logicatable, BlockEntityProvider {

    public KeypadLogicateBlock() {
        super(FabricBlockSettings.copyOf(Blocks.REPEATER));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(world.isClient) return ActionResult.SUCCESS;

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if(blockEntity instanceof KeypadLogicateBlockEntity) {
            player.openHandledScreen((KeypadLogicateBlockEntity) blockEntity);
        }
        return ActionResult.CONSUME;
    }

    @SuppressWarnings("ConstantConditions")
    public void powerOn(BlockState state, World world, BlockPos pos) {
        KeypadLogicateBlockEntity blockEntity = ((KeypadLogicateBlockEntity) world.getBlockEntity(pos));

        world.setBlockState(pos, state.with(POWERED, true), 3);
        world.updateNeighborsAlways(pos, state.getBlock());
        world.createAndScheduleBlockTick(
                pos,
                this,
                blockEntity.closingDelay *20
        );
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(POWERED)) {
            world.setBlockState(pos, state.with(POWERED, false), 3);
            world.updateNeighborsAlways(pos, state.getBlock());
        }
    }

    @Override
    protected int getPower(World world, BlockPos pos, BlockState state) {
        return world.getBlockState(pos).get(POWERED) ? 15 : 0;
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return world.getBlockState(pos).get(POWERED) ? 15 : 0;
    }

    @Override
    protected int getUpdateDelayInternal(BlockState state) {
        return 2;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new KeypadLogicateBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, BlockRegistry.KEYPAD_LOGICATE_BLOCK_ENTITY_TYPE, KeypadLogicateBlockEntity::tick);
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
        builder.add(FACING, POWERED);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.fullCube();
    }

    @Override
    public LogicateType getLogicateType() {
        return LogicateType.INPUTLESS;
    }

    @Override
    public List<MutableText> getLogicateTooltip() {
        return List.of(
                Text.translatable("tooltip.logicates.keypad_logicate")
        );
    }

    public static final int MIN_CLOSING_DELAY = 1;
    public static final int MAX_CLOSING_DELAY = 30;
}