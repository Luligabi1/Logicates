package me.luligabi.logicates.common.block.logicate.inputless.timer;

import me.luligabi.logicates.common.block.BlockRegistry;
import me.luligabi.logicates.common.block.ClientSyncedBlockEntity;
import me.luligabi.logicates.common.block.logicate.LogicateBlock;
import me.luligabi.logicates.common.misc.screenhandler.TimerLogicateScreenHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TimerLogicateBlockEntity extends ClientSyncedBlockEntity implements NamedScreenHandlerFactory {

    public TimerLogicateBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.TIMER_LOGICATE_BLOCK_ENTITY_TYPE, pos, state);
        this.propertyDelegate = new PropertyDelegate() {

            @Override
            public int get(int index) {
                return TimerLogicateBlockEntity.this.maxTicks;
            }

            @Override
            public void set(int index, int value) {
                TimerLogicateBlockEntity.this.maxTicks = value;
            }

            @Override
            public int size() {
                return 1;
            }
        };
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new TimerLogicateScreenHandler(syncId, propertyDelegate);
    }

    public static void tick(World world, BlockPos pos, BlockState state, TimerLogicateBlockEntity blockEntity) { // TODO: Add way to stop timer
        if(blockEntity.ticks <= blockEntity.maxTicks) {
            blockEntity.ticks++;
        } else {
            world.setBlockState(pos, state.with(LogicateBlock.POWERED, true), Block.NOTIFY_ALL);
            world.setBlockState(pos, state.with(LogicateBlock.POWERED, false), Block.NOTIFY_ALL);
            blockEntity.ticks = 0;
        }
        blockEntity.sync();
    }

    @Override
    public void toTag(NbtCompound nbt) {
        nbt.putInt("Ticks", ticks);
        nbt.putInt("MaxTicks", maxTicks);
    }

    @Override
    public void fromTag(NbtCompound nbt) {
        this.ticks = nbt.getInt("Ticks");
        this.maxTicks = nbt.getInt("MaxTicks");
    }

    @Override
    public void toClientTag(NbtCompound nbt) {
        toTag(nbt);
    }

    @Override
    public void fromClientTag(NbtCompound nbt) {
        fromTag(nbt);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.logicates.timer_logicate");
    }

    public int ticks = 0;
    public int maxTicks = 200;
    protected final PropertyDelegate propertyDelegate;
}
