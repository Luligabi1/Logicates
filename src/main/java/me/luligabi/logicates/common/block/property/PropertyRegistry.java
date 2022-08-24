package me.luligabi.logicates.common.block.property;

import net.minecraft.state.property.EnumProperty;

public class PropertyRegistry {

    public static final EnumProperty<InputSides> SINGLE_INPUT_SIDE = EnumProperty.of("single_input_side", InputSides.class, InputSides.BACK, InputSides.RIGHT, InputSides.LEFT);
    public static final EnumProperty<InputSides> DUAL_INPUT_SIDE = EnumProperty.of("dual_input_side", InputSides.class, InputSides.LEFT_RIGHT, InputSides.LEFT_BACK, InputSides.RIGHT_BACK);

    public static final EnumProperty<WeatherType> WEATHER_TYPE = EnumProperty.of("weather_type", WeatherType.class, WeatherType.SUNNY, WeatherType.RAINY, WeatherType.STORMY);

    public static void init() {
        // NO-OP
    }

    private PropertyRegistry() {
        // NO-OP
    }

}