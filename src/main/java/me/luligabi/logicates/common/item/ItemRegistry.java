package me.luligabi.logicates.common.item;

import me.luligabi.logicates.common.Logicates;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class ItemRegistry {

    public static void init() {
        initItem("logicate_plate", LOGICATE_PLATE);
    }

    public static final Item LOGICATE_PLATE = new Item(new FabricItemSettings().group(Logicates.ITEM_GROUP));

    private static void initItem(String id, Item item) {
        Registry.register(Registry.ITEM, Logicates.id(id), item);
    }

}