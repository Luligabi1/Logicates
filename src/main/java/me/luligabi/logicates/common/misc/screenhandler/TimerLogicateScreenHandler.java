package me.luligabi.logicates.common.misc.screenhandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;

public class TimerLogicateScreenHandler extends ScreenHandler {


    public TimerLogicateScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, new ArrayPropertyDelegate(2));
    }

    public TimerLogicateScreenHandler(int syncId, PropertyDelegate propertyDelegate) {
        super(ScreenHandlingRegistry.TIMER_LOGICATE_SCREEN_HANDLER, syncId);
        checkDataCount(propertyDelegate, 2);
        this.propertyDelegate = propertyDelegate;
        this.addProperties(propertyDelegate);
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        switch(id) {
            // --
            case 0 -> {
                setProperty(0, propertyDelegate.get(0) - 4000);
                return true;
            }
            case 1 -> {
                setProperty(0, propertyDelegate.get(0) - 200);
                return true;
            }

            // -
            case 2 -> {
                setProperty(0, propertyDelegate.get(0) - 20);
                return true;
            }
            case 3 -> {
                setProperty(0, propertyDelegate.get(0) - 1);
                return true;
            }

            // +
            case 4 -> {
                setProperty(0, propertyDelegate.get(0) + 20);
                return true;
            }
            case 5 -> {
                setProperty(0, propertyDelegate.get(0) + 1);
                return true;
            }

            // ++
            case 6 -> {
                setProperty(0, propertyDelegate.get(0) + 4000);
                return true;
            }
            case 7 -> {
                setProperty(0, propertyDelegate.get(0) + 200);
                return true;
            }

            // Mute button
            case 8 -> { // Unmute
                setProperty(1, 0);
                return true;
            }
            case 9 -> { // Mute
                setProperty(1, 1);
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        return ItemStack.EMPTY;
    }

    public void setProperty(int index, int value) {
        super.setProperty(index, value);
        this.sendContentUpdates();
    }

    public PropertyDelegate getPropertyDelegate() {
        return propertyDelegate;
    }


    private final PropertyDelegate propertyDelegate;

}