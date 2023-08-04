package me.luligabi.logicates.common.block.logicate.misc.keypad;

import me.luligabi.logicates.common.block.BlockRegistry;
import me.luligabi.logicates.common.block.ClientSyncedBlockEntity;
import me.luligabi.logicates.common.misc.screenhandler.KeypadLogicateScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class KeypadLogicateBlockEntity extends ClientSyncedBlockEntity implements ExtendedScreenHandlerFactory {

    public KeypadLogicateBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.KEYPAD_LOGICATE_BLOCK_ENTITY_TYPE, pos, state);
        this.state = state;
    }


    public void insertNumber(String input) {
        setActivePassword(getActivePassword().concat(input));
    }

    public void removeNumber() {
        setActivePassword(getActivePassword().substring(0, getActivePassword().length() - 1));
    }

    public void enableHasPassword() {
        hasPassword = true;
    }

    public void resetCurrentPassword() {
        currentPassword = "";
    }

    public void reset() {
        password = "";
        currentPassword = "";
        hasPassword = false;
        passwordReset = false;
    }

    public void togglePasswordReset() {
        passwordReset = !passwordReset;
    }

    public void changeClosingDelay(boolean decrease) {
        int offset = closingDelay + (decrease ? -1 : 1);
        if((offset >= KeypadLogicateBlock.MIN_CLOSING_DELAY) && (offset <= KeypadLogicateBlock.MAX_CLOSING_DELAY)) {
            closingDelay = offset;
        }
    }

    public String getActivePassword() {
        return hasPassword ? currentPassword : password;
    }

    public void setActivePassword(String value) {
        if(hasPassword) {
            currentPassword = value;
        } else {
            password = value;
        }
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new KeypadLogicateScreenHandler(syncId, ScreenHandlerContext.create(world, pos));
    }

    @Override
    public void toTag(NbtCompound nbt) {
        nbt.putString("Password", password);
        nbt.putString("CurrentPassword", currentPassword);
        nbt.putBoolean("HasPassword", hasPassword);
        nbt.putBoolean("PasswordReset", passwordReset);
        nbt.putInt("ClosingDelay", (byte) closingDelay);
    }

    @Override
    public void fromTag(NbtCompound nbt) {
        password = nbt.getString("Password");
        currentPassword = nbt.getString("CurrentPassword");
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
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    @Override
    public Text getDisplayName() {
        return Text.empty();
    }

    public String password = "";
    public String currentPassword = "";
    public boolean hasPassword = false;
    public boolean passwordReset = false;
    public int closingDelay = 4;
    public BlockState state;

}