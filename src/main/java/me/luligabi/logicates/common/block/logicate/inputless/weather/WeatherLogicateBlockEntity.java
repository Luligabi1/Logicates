package me.luligabi.logicates.common.block.logicate.inputless.weather;

import me.luligabi.logicates.common.block.BlockRegistry;
import me.luligabi.logicates.common.block.ClientSyncedBlockEntity;
import me.luligabi.logicates.common.block.logicate.LogicateBlock;
import me.luligabi.logicates.common.block.property.PropertyRegistry;
import me.luligabi.logicates.common.misc.screenhandler.TimerLogicateScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class WeatherLogicateBlockEntity extends BlockEntity {

    public WeatherLogicateBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.WEATHER_LOGICATE_BLOCK_ENTITY_TYPE, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, WeatherLogicateBlockEntity blockEntity) {
        if(world.getTime() % 20L != 0L) return;
        switch(state.get(PropertyRegistry.WEATHER_TYPE)) {
            case SUNNY -> checkWeather(
                    !world.isRaining() && !world.isThundering(),
                    world, pos, state
            );
            case RAINY -> checkWeather(
                    world.isRaining() && !world.isThundering(),
                    world, pos, state
            );
            case STORMY -> checkWeather(
                    world.isThundering(),
                    world, pos, state
            );
        }
    }

    private static void checkWeather(boolean requiredCheck, World world, BlockPos pos, BlockState state) {
        if(requiredCheck) {
            if(state.get(LogicateBlock.POWERED)) return;
            world.setBlockState(pos, state.with(LogicateBlock.POWERED, true), 2);
        } else {
            world.setBlockState(pos, state.with(LogicateBlock.POWERED, false), 2);
        }
    }

}