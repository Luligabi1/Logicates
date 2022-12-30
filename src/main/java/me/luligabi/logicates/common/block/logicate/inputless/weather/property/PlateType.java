package me.luligabi.logicates.common.block.logicate.inputless.weather.property;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.StringIdentifiable;

import java.util.function.Predicate;

public enum PlateType implements StringIdentifiable {

    PLAYER("player", PlayerEntity.class),
    ITEM("item", ItemEntity.class),
    AGGRESSIVE("aggressive", HostileEntity.class, e -> !e.canAvoidTraps()),
    PASSIVE("passive", PassiveEntity.class, e -> !e.canAvoidTraps()),
    VILLAGER("villager", MerchantEntity.class),
    PET("pet", TameableEntity.class, TameableEntity::isTamed);


    private final String name;
    private final Class<Entity> filterEntity;
    private final Predicate<Entity> predicate;

    // i hate every single pixel of my screen that has ever been disgraced with the duty to display this shitty ass code
    <T extends Entity> PlateType(String name, Class<T> filterEntity) {
        this.name = name;
        this.filterEntity = (Class<Entity>) filterEntity;
        this.predicate = e -> true;
    }

    <T extends Entity> PlateType(String name, Class<T> filterEntity, Predicate<T> predicate) {
        this.name = name;
        this.filterEntity = (Class<Entity>) filterEntity;
        this.predicate = (Predicate<Entity>) predicate;
    }

    public String toString() {
        return this.name;
    }

    public String asString() {
        return this.name;
    }

    public Class<Entity> getFilterEntity() {
        return filterEntity;
    }

    public Predicate<Entity> getPredicate() {
        return predicate;
    }

}