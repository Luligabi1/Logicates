package me.luligabi.logicates.common;

import me.luligabi.logicates.common.block.BlockRegistry;
import me.luligabi.logicates.common.block.logicate.property.PropertyRegistry;
import me.luligabi.logicates.common.item.ItemRegistry;
import me.luligabi.logicates.common.misc.ItemGroupInit;
import me.luligabi.logicates.common.misc.packet.ServerPlayReceiverRegistry;
import me.luligabi.logicates.common.misc.recipe.RecipeRegistry;
import me.luligabi.logicates.common.misc.screenhandler.ScreenHandlingRegistry;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class Logicates implements ModInitializer {

    @Override
    public void onInitialize() {
        PropertyRegistry.init();
        BlockRegistry.init();
        ItemRegistry.init();

        RecipeRegistry.init();
        ScreenHandlingRegistry.init();
        ServerPlayReceiverRegistry.init();
        ItemGroupInit.init();
    }


    public static Identifier id(String id) {
        return new Identifier(MOD_ID, id);
    }

    public static final String MOD_ID = "logicates";

    @SuppressWarnings("unused")
    public static final RegistryKey<ItemGroup> ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, id("item_group"));
}