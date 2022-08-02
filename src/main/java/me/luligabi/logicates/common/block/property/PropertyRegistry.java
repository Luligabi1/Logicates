package me.luligabi.logicates.common.block.property;

import net.minecraft.state.property.EnumProperty;

public class PropertyRegistry {

    public static final EnumProperty<InputSides> SINGLE_INPUT_SIDE = EnumProperty.of("single_input_side", InputSides.class, InputSides.BACK, InputSides.RIGHT, InputSides.LEFT);


    public static void init() {
        // NO-OP
    }

    private PropertyRegistry() {
        // NO-OP
    }

}