package me.luligabi.logicates.common.block.logicate.inputless.keypad;

import me.luligabi.logicates.common.block.BlockRegistry;
import me.luligabi.logicates.common.block.ClientSyncedBlockEntity;
import me.luligabi.logicates.common.misc.screenhandler.KeypadLogicateScreenHandler;
import net.minecraft.block.BlockState;
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

public class KeypadLogicateBlockEntity extends ClientSyncedBlockEntity implements NamedScreenHandlerFactory {

    public KeypadLogicateBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.KEYPAD_LOGICATE_BLOCK_ENTITY_TYPE, pos, state);
        this.state = state;
        this.propertyDelegate = new PropertyDelegate() {

            @Override
            public int get(int index) {
                return switch(index) {
                    case 0 -> KeypadLogicateBlockEntity.this.password;
                    case 1 -> KeypadLogicateBlockEntity.this.currentPassword;
                    case 2 -> KeypadLogicateBlockEntity.this.hasPassword ? 1 : 0;
                    case 3 -> KeypadLogicateBlockEntity.this.passwordReset ? 1 : 0;
                    case 4 -> KeypadLogicateBlockEntity.this.closingDelay;
                    default -> throw new IllegalStateException("Unexpected value: " + index);
                };
            }

            @Override
            public void set(int index, int value) {
                switch(index) {
                    case 0 -> KeypadLogicateBlockEntity.this.password = value;
                    case 1 -> KeypadLogicateBlockEntity.this.currentPassword = value;
                    case 2 -> KeypadLogicateBlockEntity.this.hasPassword = value == 1; // 1 -> true, anything else -> false
                    case 3 -> KeypadLogicateBlockEntity.this.passwordReset = value == 1;
                    case 4 -> KeypadLogicateBlockEntity.this.closingDelay = value;
                    default -> throw new IllegalStateException("Unexpected value: " + index);
                }
            }

            @Override
            public int size() {
                return 5;
            }
        };
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new KeypadLogicateScreenHandler(syncId, propertyDelegate, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, KeypadLogicateBlockEntity blockEntity) {
    }

    @Override
    public void toTag(NbtCompound nbt) {
        nbt.putInt("Password", password);
        nbt.putInt("CurrentPassword", currentPassword);
        nbt.putBoolean("HasPassword", hasPassword);
        nbt.putBoolean("PasswordReset", passwordReset);
        nbt.putInt("ClosingDelay", (byte) closingDelay);
    }

    @Override
    public void fromTag(NbtCompound nbt) {
        password = nbt.getInt("Password");
        currentPassword = nbt.getInt("CurrentPassword");
        hasPassword = nbt.getBoolean("HasPassword");
        passwordReset = nbt.getBoolean("PasswordReset");
        closingDelay = nbt.getInt("ClosingDelay");
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
        return Text.translatable(
                (!hasPassword || passwordReset) ?
                "container.logicates.keypad_logicate.2" :
                "container.logicates.keypad_logicate"
        );
    }


    public int password, currentPassword;
    public boolean hasPassword = false;
    public boolean passwordReset = false;
    public int closingDelay = 4;
    public BlockState state;
    public PropertyDelegate propertyDelegate;

}