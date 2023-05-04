package me.luligabi.logicates.common.block.logicate.inputless;

import me.luligabi.logicates.common.block.logicate.Logicatable;
import me.luligabi.logicates.common.block.logicate.LogicateType;
import me.luligabi.logicates.common.block.logicate.inputless.weather.property.PlateType;
import me.luligabi.logicates.common.block.logicate.inputless.weather.property.PropertyRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class PressurePlateLogicateBlock extends PressurePlateBlock implements Logicatable {

    public PressurePlateLogicateBlock() {
        super(
                null,
                FabricBlockSettings.of(Material.STONE).requiresTool().noCollision().strength(0.5F),
                BlockSetType.STONE
        );
        setDefaultState(stateManager.getDefaultState().with(POWERED, false).with(FACING, Direction.NORTH));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!player.getAbilities().allowModifyWorld) return ActionResult.PASS;
        world.setBlockState(pos, state.cycle(PLATE_TYPE));
        return ActionResult.success(world.isClient);
    }

    @Override
    protected int getRedstoneOutput(World world, BlockPos pos) {
        PlateType plateType = world.getBlockState(pos).get(PLATE_TYPE);
        boolean hasValidEntityInput = world.getNonSpectatingEntities(
                plateType.getFilterEntity(),
                BOX.offset(pos)
            ).stream().anyMatch(plateType.getEntityPredicate());
        return hasValidEntityInput ? 15 : 0;
    }


    @Override
    public LogicateType getLogicateType() {
        return LogicateType.INPUTLESS;
    }

    @Override
    public List<MutableText> getLogicateTooltip() {
        ArrayList<MutableText> tooltip = new ArrayList<>();

        tooltip.add(Text.translatable("tooltip.logicates.pressure_plate_logicate.1"));
        if(Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("tooltip.logicates.pressure_plate_logicate.2"));
            tooltip.add(Text.empty());
            for (int i = 3; i < 9; i++) {
                tooltip.add(Text.translatable(
                        String.format("tooltip.logicates.pressure_plate_logicate.%d", i)
                ));
            }
        } else {
            tooltip.add(Text.translatable("tooltip.logicates.pressure_plate_logicate.9").formatted(Formatting.ITALIC));
        }

        return tooltip;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        appendLogicateTooltip(tooltip);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(PLATE_TYPE, FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final EnumProperty<PlateType> PLATE_TYPE = PropertyRegistry.PLATE_TYPE;

}
