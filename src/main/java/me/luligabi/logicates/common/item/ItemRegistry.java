package me.luligabi.logicates.common.item;

import me.luligabi.logicates.common.Logicates;
import me.luligabi.logicates.common.misc.ItemGroupInit;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ItemRegistry {

    public static void init() {
        initItem("logicate_plate", LOGICATE_PLATE);
        initItem("timer_logicate_pointer", TIMER_LOGICATE_POINTER, true);
    }

    public static final Item LOGICATE_PLATE = new Item(new FabricItemSettings());

    public static final Item TIMER_LOGICATE_POINTER = new Item(new FabricItemSettings());



    private static void initItem(String id, Item item) {
        initItem(id, item, false);
    }

    private static void initItem(String id, Item item, boolean isHidden) {
        Registry.register(Registries.ITEM, Logicates.id(id), item);
        if(!isHidden) {
            ItemGroupInit.ITEMS.add(new ItemStack(item));
        }
    }

}