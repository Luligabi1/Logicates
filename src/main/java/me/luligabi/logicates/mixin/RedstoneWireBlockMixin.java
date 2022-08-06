package me.luligabi.logicates.mixin;

import me.luligabi.logicates.common.block.logicate.input.dual.DualInputLogicateBlock;
import me.luligabi.logicates.common.block.logicate.LogicateBlock;
import me.luligabi.logicates.common.block.logicate.input.single.SingleInputLogicateBlock;
import me.luligabi.logicates.common.block.logicate.inputless.InputlessLogicateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Direction;
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
        if(block instanceof DualInputLogicateBlock) {
            Pair<Direction, Direction> inputDirection = ((DualInputLogicateBlock) block).getInputSides(state);
            Direction outputDirection = state.get(LogicateBlock.FACING);

            callbackInfo.setReturnValue(
                    dir == inputDirection.getLeft().getOpposite() ||
                    dir == inputDirection.getRight().getOpposite() ||
                    dir == outputDirection
            );
        }
        if(block instanceof InputlessLogicateBlock) {
            callbackInfo.setReturnValue(dir == state.get(LogicateBlock.FACING));
        }
    }
}