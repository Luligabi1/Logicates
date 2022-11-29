package me.luligabi.logicates.common.misc.screenhandler;

import me.luligabi.logicates.common.block.logicate.inputless.keypad.KeypadLogicateBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

public class KeypadLogicateScreenHandler extends ScreenHandler {


    public KeypadLogicateScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, new ArrayPropertyDelegate(3), BlockPos.ORIGIN, null);
    }

    public KeypadLogicateScreenHandler(int syncId, PropertyDelegate propertyDelegate, BlockPos pos, BlockState state) {
        super(ScreenHandlingRegistry.KEYPAD_LOGICATE_SCREEN_HANDLER, syncId);
        checkDataCount(propertyDelegate, 3);
        this.propertyDelegate = propertyDelegate;
        this.addProperties(propertyDelegate);
        this.pos = pos;
        this.state = state;
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        switch(id) {
            case 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 -> { // Insert
                insertNumber(id);
                return true;
            }
            case 10 -> { // Confirm
                if(hasPassword()) { // Check password
                    if(getCurrentPassword() == getPassword()) {
                        player.sendMessage(
                                Text.translatable(
                                        "message.logicates.keypad_logicate.correct_password"
                                ).formatted(Formatting.GREEN),
                                true
                        );
                        ((KeypadLogicateBlock) player.world.getBlockState(pos).getBlock()).powerOn(state, player.world, pos);
                    } else {
                        player.sendMessage(
                                Text.translatable(
                                        "message.logicates.keypad_logicate.incorrect_password"
                                ).formatted(Formatting.RED),
                                true
                        );
                    }
                } else { // Set password
                    setProperty(2, 1);
                    player.sendMessage(
                            Text.translatable(
                                    "message.logicates.keypad_logicate.set_password"
                            ).formatted(Formatting.YELLOW),
                            true
                    );
                }
                setProperty(1, 0);
                player.closeHandledScreen();
                return true;
            }
            case 11 -> { // Delete
                if(getActivePassword() <= 0) return false;
                setProperty(hasPassword() ? 1 : 0, getActivePassword() / 10);
                return true;
            }
            default -> {
                return false;
            }
        }
    }


    private void insertNumber(int number) {
        setProperty(hasPassword() ? 1 : 0, getActivePassword() * 10 + number);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setProperty(int index, int value) {
        super.setProperty(index, value);
        this.sendContentUpdates();
    }

    public int getActivePassword() {
        return hasPassword() ? getCurrentPassword() : getPassword();
    }

    public int getPassword() {
        return propertyDelegate.get(0);
    }

    public int getCurrentPassword() {
        return propertyDelegate.get(1);
    }

    public boolean hasPassword() {
        return propertyDelegate.get(2) == 1;
    }

    private BlockPos pos;
    private BlockState state;
    private final PropertyDelegate propertyDelegate;
}