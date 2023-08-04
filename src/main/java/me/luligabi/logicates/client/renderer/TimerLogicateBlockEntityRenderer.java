package me.luligabi.logicates.client.renderer;

import me.luligabi.logicates.common.block.BlockRegistry;
import me.luligabi.logicates.common.block.logicate.LogicateBlock;
import me.luligabi.logicates.common.block.logicate.misc.timer.TimerLogicateBlockEntity;
import me.luligabi.logicates.common.item.ItemRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

public class TimerLogicateBlockEntityRenderer implements BlockEntityRenderer<TimerLogicateBlockEntity> {

    @SuppressWarnings("unused")
    public TimerLogicateBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
    }


    @SuppressWarnings("ConstantConditions")
    @Override
    public void render(TimerLogicateBlockEntity entity, float tickDelta, MatrixStack ms, VertexConsumerProvider vcp, int light, int overlay) {
        if(!entity.getWorld().getBlockState(entity.getPos()).isOf(BlockRegistry.TIMER_LOGICATE)) return;
        ms.push();
        ms.translate(0.5D, 0.2D, 0.5D);


        ms.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90F));
        Direction direction = entity.getWorld().getBlockState(entity.getPos()).get(LogicateBlock.FACING);
        ms.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(((float) entity.ticks * 360.0F / entity.maxTicks) + getItemAngle(direction))); // TODO: Interpolate this

        MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(ItemRegistry.TIMER_LOGICATE_POINTER), ModelTransformationMode.FIXED, light, OverlayTexture.DEFAULT_UV, ms, vcp, entity.getWorld(), (int) entity.getPos().asLong());
        ms.pop();
    }


    private float getItemAngle(Direction direction) {
        return switch(direction) {
            case NORTH -> 0;
            case SOUTH -> 2 * 90;
            case WEST -> 3 * 90;
            case EAST -> 90;
            default -> throw new IllegalStateException("Unexpected Timer Logicate direction: " + direction);
        };
    }

}
