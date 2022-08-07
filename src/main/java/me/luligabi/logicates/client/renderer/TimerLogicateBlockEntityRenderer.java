package me.luligabi.logicates.client.renderer;

import me.luligabi.logicates.common.block.BlockRegistry;
import me.luligabi.logicates.common.block.logicate.LogicateBlock;
import me.luligabi.logicates.common.block.logicate.inputless.timer.TimerLogicateBlockEntity;
import me.luligabi.logicates.common.item.ItemRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

public class TimerLogicateBlockEntityRenderer implements BlockEntityRenderer<TimerLogicateBlockEntity> {

    public TimerLogicateBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
    }


    @Override
    public void render(TimerLogicateBlockEntity entity, float tickDelta, MatrixStack ms, VertexConsumerProvider vcp, int light, int overlay) {
        if(!entity.getWorld().getBlockState(entity.getPos()).isOf(BlockRegistry.TIMER_LOGICATE)) return;
        ms.push();
        ms.translate(0.5D, 0.2D, 0.5D);

        ms.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90F));
        Direction direction = entity.getWorld().getBlockState(entity.getPos()).get(LogicateBlock.FACING);
        ms.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(((float) entity.ticks * 360.0F / entity.maxTicks) + getItemAngle(direction))); // TODO: Interpolate this

        MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(ItemRegistry.TIMER_LOGICATE_POINTER), ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, ms, vcp, (int) entity.getPos().asLong());
        ms.pop();
    }


    private float getItemAngle(Direction direction) {
        return switch(direction) {
            case NORTH -> 0;
            case SOUTH -> 2 * 90;
            case WEST -> 3 * 90; // oposto
            case EAST -> 90; // oposto
            default -> throw new IllegalStateException("Unexpected Timer Logicate direction: " + direction);
        };
    }

}
