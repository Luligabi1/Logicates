package me.luligabi.logicates.mixin;

import me.luligabi.logicates.common.block.LogicateBlock;
import me.luligabi.logicates.common.block.SingleInputLogicateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.block.RepeaterBlock;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RedstoneWireBlock.class)
public class RedstoneWireBlockMixin {

    @Inject(
            method = "connectsTo(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Direction;)Z",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void logicates_connectsTo(BlockState state, Direction dir, CallbackInfoReturnable<Boolean> callbackInfo) {
        Block block = state.getBlock();
        if(block instanceof SingleInputLogicateBlock) {
            Direction inputDirection = ((SingleInputLogicateBlock) block).getInputSide(state).getOpposite();
            Direction outputDirection = state.get(LogicateBlock.FACING);

            callbackInfo.setReturnValue(dir == inputDirection || dir == outputDirection);
        }
    }
}