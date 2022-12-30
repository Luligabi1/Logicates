package me.luligabi.logicates.common.block;

import me.luligabi.logicates.common.Logicates;
import me.luligabi.logicates.common.block.fabricator.LogicateFabricatorBlock;
import me.luligabi.logicates.common.block.logicate.input.dual.*;
import me.luligabi.logicates.common.block.logicate.input.single.NotLogicateBlock;
import me.luligabi.logicates.common.block.logicate.input.single.ToggleLogicateBlock;
import me.luligabi.logicates.common.block.logicate.inputless.PressurePlateLogicateBlock;
import me.luligabi.logicates.common.block.logicate.inputless.timer.TimerLogicateBlock;
import me.luligabi.logicates.common.block.logicate.inputless.timer.TimerLogicateBlockEntity;
import me.luligabi.logicates.common.block.logicate.inputless.weather.WeatherLogicateBlock;
import me.luligabi.logicates.common.block.logicate.inputless.weather.WeatherLogicateBlockEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class BlockRegistry {

    public static void init() {
        initBlock("and_logicate", AND_LOGICATE);
        initBlock("or_logicate", OR_LOGICATE);
        initBlock("xor_logicate", XOR_LOGICATE);
        initBlock("not_logicate", NOT_LOGICATE);
        initBlock("nand_logicate", NAND_LOGICATE);
        initBlock("nor_logicate", NOR_LOGICATE);
        initBlock("xnor_logicate", XNOR_LOGICATE);

        initBlock("toggle_logicate", TOGGLE_LOGICATE);

        initBlock("timer_logicate", TIMER_LOGICATE);
        TIMER_LOGICATE_BLOCK_ENTITY_TYPE = Registry.register(Registries.BLOCK_ENTITY_TYPE, Logicates.id("timer_logicate"), FabricBlockEntityTypeBuilder.create(TimerLogicateBlockEntity::new, TIMER_LOGICATE).build());

        initBlock("weather_logicate", WEATHER_LOGICATE);
        WEATHER_LOGICATE_BLOCK_ENTITY_TYPE = Registry.register(Registries.BLOCK_ENTITY_TYPE, Logicates.id("weather_logicate"), FabricBlockEntityTypeBuilder.create(WeatherLogicateBlockEntity::new, WEATHER_LOGICATE).build());

        initBlock("pressure_plate_logicate", PRESSURE_PLATE_LOGICATE);

        initBlock("logicate_fabricator", LOGICATE_FABRICATOR);
    }


    public static final Block AND_LOGICATE = new AndLogicateBlock();
    public static final Block OR_LOGICATE = new OrLogicateBlock();
    public static final Block XOR_LOGICATE = new XorLogicateBlock();
    public static final Block NOT_LOGICATE = new NotLogicateBlock();
    public static final Block NAND_LOGICATE = new NandLogicateBlock();
    public static final Block NOR_LOGICATE = new NorLogicateBlock();
    public static final Block XNOR_LOGICATE = new XnorLogicateBlock();

    public static final Block TOGGLE_LOGICATE = new ToggleLogicateBlock();

    public static final Block TIMER_LOGICATE = new TimerLogicateBlock();
    public static BlockEntityType<TimerLogicateBlockEntity> TIMER_LOGICATE_BLOCK_ENTITY_TYPE;

    public static final Block WEATHER_LOGICATE = new WeatherLogicateBlock();
    public static BlockEntityType<WeatherLogicateBlockEntity> WEATHER_LOGICATE_BLOCK_ENTITY_TYPE;

    public static final Block PRESSURE_PLATE_LOGICATE = new PressurePlateLogicateBlock();

    public static final Block LOGICATE_FABRICATOR = new LogicateFabricatorBlock(FabricBlockSettings.copy(Blocks.SMITHING_TABLE));

    private static void initBlock(String identifier, Block block) {
        initBlock(identifier, block, false);
    }


    private static void initBlock(String identifier, Block block, boolean isHidden) {
        Registry.register(Registries.BLOCK, Logicates.id(identifier), block);
        Registry.register(Registries.ITEM, Logicates.id(identifier), new BlockItem(block, new FabricItemSettings()));
        if(!isHidden) {
            Logicates.ITEMS.add(new ItemStack(block));
        }
    }

}