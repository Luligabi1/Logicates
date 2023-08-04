package me.luligabi.logicates.common.block.logicate.misc.keypad;

import me.luligabi.logicates.common.Logicates;
import me.luligabi.logicates.common.block.BlockRegistry;
import me.luligabi.logicates.common.block.logicate.misc.MiscellaneousLogicateBlock;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("deprecation")
public class KeypadLogicateBlock extends MiscellaneousLogicateBlock implements BlockEntityProvider {


    public KeypadLogicateBlock() {
        super(FabricBlockSettings.copyOf(Blocks.REPEATER).strength(0.5F, 3600000.0F).pistonBehavior(PistonBehavior.BLOCK));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(world.isClient()) return ActionResult.SUCCESS;

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if(blockEntity instanceof KeypadLogicateBlockEntity keypadLogicateBlockEntity) {
            player.openHandledScreen(keypadLogicateBlockEntity);
            keypadLogicateBlockEntity.sync();
        }
        return ActionResult.CONSUME;
    }


    @SuppressWarnings("ConstantConditions")
    public void powerOn(BlockState state, World world, BlockPos pos) {
        KeypadLogicateBlockEntity blockEntity = ((KeypadLogicateBlockEntity) world.getBlockEntity(pos));

        world.setBlockState(pos, state.with(POWERED, true), 3);
        world.updateNeighborsAlways(pos, state.getBlock());
        world.scheduleBlockTick(pos, this, blockEntity.closingDelay * 20);
    }


    public void powerOff(BlockState state, World world, BlockPos pos) {
        if(!state.get(POWERED)) return;
        world.setBlockState(pos, state.with(POWERED, false), 3);
        world.updateNeighborsAlways(pos, state.getBlock());
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(state.get(POWERED)) {
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
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
       return true;
    }


    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new KeypadLogicateBlockEntity(pos, state);
    }

    @Override
    public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
        super.onSyncedBlockEvent(state, world, pos, type, data);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity != null && blockEntity.onSyncedBlockEvent(type, data);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch(state.get(FACING)) {
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            case EAST -> EAST_SHAPE;
            default -> VoxelShapes.empty();
        };
    }

    @Override
    public List<MutableText> getLogicateTooltip() {
        return List.of(
                Text.translatable("tooltip.logicates.keypad_logicate")
        );
    }

    @SuppressWarnings("ConstantConditions")
    public static void initBlockBreakingLogic() {
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
                if(state.getBlock() != BlockRegistry.KEYPAD_LOGICATE) return true;
                return StringUtils.isEmpty(((KeypadLogicateBlockEntity) blockEntity).password) || state.get(POWERED) || (player.isCreative() && player.hasPermissionLevel(3));
                }
        );
    }

    public static final int MIN_CLOSING_DELAY = 1;
    public static final int MAX_CLOSING_DELAY = 30;
    public static final Identifier KEYPAD_INSERT = Logicates.id("keypad_insert");
    public static final Identifier KEYPAD_DELETE = Logicates.id("keypad_delete");
    public static final Identifier KEYPAD_CONFIRM = Logicates.id("keypad_confirm");
    public static final Identifier KEYPAD_TOGGLE_PASSWORD_RESET = Logicates.id("keypad_toggle_password_reset");
    public static final Identifier KEYPAD_CLOSING_DELAY = Logicates.id("keypad_closing_delay");

    public static final VoxelShape NORTH_SHAPE = createCuboidShape(5.75, 5, 15, 10.25, 11, 16);
    public static final VoxelShape SOUTH_SHAPE = createCuboidShape(5.75, 5, 0, 10.25, 11, 1);
    public static final VoxelShape WEST_SHAPE = createCuboidShape(15, 5, 5.75, 16, 11, 10.25);
    public static final VoxelShape EAST_SHAPE = createCuboidShape(0, 5, 5.75, 1, 11, 10.25);
}