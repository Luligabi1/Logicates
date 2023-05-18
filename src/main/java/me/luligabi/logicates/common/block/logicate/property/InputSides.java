package me.luligabi.logicates.common.block.logicate.property;

import net.minecraft.util.StringIdentifiable;

public enum InputSides implements StringIdentifiable {

    // Single Input
    LEFT("left"),
    RIGHT("right"),
    BACK("back"),

    // Dual Input
    LEFT_RIGHT("left_right"),
    LEFT_BACK("left_back"),
    RIGHT_BACK("right_back");

    private final String name;

    InputSides(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public String asString() {
        return this.name;
    }
}