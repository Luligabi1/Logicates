package me.luligabi.logicates.common.misc.screenhandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;

public class KeypadLogicateScreenHandler extends ScreenHandler {


    public KeypadLogicateScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId);
        clientPos = buf.readBlockPos();
    }

    public KeypadLogicateScreenHandler(int syncId) {
        super(ScreenHandlingRegistry.KEYPAD_LOGICATE_SCREEN_HANDLER, syncId);
        clientPos = BlockPos.ORIGIN;
    }

    public BlockPos clientPos;

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        return ItemStack.EMPTY;
    }
}