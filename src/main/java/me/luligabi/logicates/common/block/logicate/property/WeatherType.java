package me.luligabi.logicates.common.block.logicate.property;

import net.minecraft.util.StringIdentifiable;

public enum WeatherType implements StringIdentifiable {

    SUNNY("sunny"),
    RAINY("rainy"),
    STORMY("stormy");


    private final String name;

    WeatherType(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public String asString() {
        return this.name;
    }

}