package me.luligabi.logicates.common.misc.screenhandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;

public class TimerLogicateScreenHandler extends ScreenHandler {


    public TimerLogicateScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, new ArrayPropertyDelegate(1));
    }

    public TimerLogicateScreenHandler(int syncId, PropertyDelegate propertyDelegate) {
        super(ScreenHandlingRegistry.TIMER_LOGICATE_SCREEN_HANDLER, syncId);
        checkDataCount(propertyDelegate, 1);
        this.propertyDelegate = propertyDelegate;
    }


    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        return ItemStack.EMPTY;
    }

    public PropertyDelegate getPropertyDelegate() {
        return propertyDelegate;
    }


    private final PropertyDelegate propertyDelegate;

}