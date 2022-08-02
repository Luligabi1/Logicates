package me.luligabi.logicates.common;

import me.luligabi.logicates.common.block.BlockRegistry;
import me.luligabi.logicates.common.block.property.PropertyRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;

public class Logicates implements ModInitializer {

    @Override
    public void onInitialize() {
        PropertyRegistry.init();
        BlockRegistry.init();
    }


    public static Identifier id(String id) {
        return new Identifier(MOD_ID, id);
    }

    public static final String MOD_ID = "logicates";

    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(
                    new Identifier(MOD_ID, "item_group"))
            //.icon(() -> new ItemStack(BlockRegistry._LOGICATE))
            .build();

}