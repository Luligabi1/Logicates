package me.luligabi.logicates.common.block.logicate.inputless.timer;

import me.luligabi.logicates.common.block.BlockRegistry;
import me.luligabi.logicates.common.block.ClientSyncedBlockEntity;
import me.luligabi.logicates.common.block.logicate.LogicateBlock;
import me.luligabi.logicates.common.misc.screenhandler.TimerLogicateScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
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
                return switch(index) {
                    case 0 -> TimerLogicateBlockEntity.this.maxTicks;
                    case 1 -> TimerLogicateBlockEntity.this.mute ? 1 : 0;
                    default -> throw new IllegalStateException("Unexpected value: " + index);
                };
            }

            @Override
            public void set(int index, int value) {
                switch(index) {
                    case 0 -> TimerLogicateBlockEntity.this.maxTicks = value;
                    case 1 -> TimerLogicateBlockEntity.this.mute = value == 1; // 1 -> true, anything else -> false
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new TimerLogicateScreenHandler(syncId, propertyDelegate);
    }

    public static void tick(World world, BlockPos pos, BlockState state, TimerLogicateBlockEntity blockEntity) {
        if(blockEntity.maxTicks < TimerLogicateBlock.MIN_VALUE) { // Failsafe if screen-validation/nbt corruption causes maxTicks to be too low/negative
            blockEntity.maxTicks = TimerLogicateBlock.MIN_VALUE;
        }

        if(blockEntity.ticks <= blockEntity.maxTicks) {
            blockEntity.ticks++;
        } else {
            world.setBlockState(pos, state.with(LogicateBlock.POWERED, true), 1);
            if(!blockEntity.mute) {
                world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.UI_BUTTON_CLICK,
                        SoundCategory.BLOCKS, 0.5F, 1F, pos.asLong());
            }
            blockEntity.ticks = 0;
        }

        if (blockEntity.ticks == 1) {
            world.setBlockState(pos, state.with(LogicateBlock.POWERED, false), 1);
        }
        blockEntity.sync();
    }

    @Override
    public void toTag(NbtCompound nbt) {
        nbt.putInt("Ticks", ticks);
        nbt.putInt("MaxTicks", maxTicks);
        nbt.putBoolean("Mute", mute);
    }

    @Override
    public void fromTag(NbtCompound nbt) {
        this.ticks = nbt.getInt("Ticks");
        this.maxTicks = nbt.getInt("MaxTicks");
        this.mute = nbt.getBoolean("Mute");
    }

    @Override
    public void toClientTag(NbtCompound nbt) {
        nbt.putInt("Ticks", ticks);
        nbt.putInt("MaxTicks", maxTicks);
    }

    @Override
    public void fromClientTag(NbtCompound nbt) {
        this.ticks = nbt.getInt("Ticks");
        this.maxTicks = nbt.getInt("MaxTicks");
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.logicates.timer_logicate");
    }

    public int ticks = 0;
    public int maxTicks = 200;
    protected boolean mute = false;
    protected final PropertyDelegate propertyDelegate;
}
