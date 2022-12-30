package me.luligabi.logicates.common;

import me.luligabi.logicates.common.block.BlockRegistry;
import me.luligabi.logicates.common.block.logicate.inputless.weather.property.PropertyRegistry;
import me.luligabi.logicates.common.item.ItemRegistry;
import me.luligabi.logicates.common.misc.recipe.RecipeRegistry;
import me.luligabi.logicates.common.misc.screenhandler.ScreenHandlingRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class Logicates implements ModInitializer {

    @Override
    public void onInitialize() {
        PropertyRegistry.init();
        BlockRegistry.init();
        ItemRegistry.init();

        RecipeRegistry.init();
        ScreenHandlingRegistry.init();

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(entries ->
                entries.addAfter(new ItemStack(Items.COMPARATOR), ITEMS)
        );
    }


    public static Identifier id(String id) {
        return new Identifier(MOD_ID, id);
    }

    public static final String MOD_ID = "logicates";

    public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder(id("test_group"))
            .displayName(Text.translatable("itemGroup.logicates.item_group"))
            .icon(() -> new ItemStack(BlockRegistry.XNOR_LOGICATE))
            .entries((enabledFeatures, entries, operatorEnabled) ->
                    entries.addAll(Logicates.ITEMS)
            )
            .build();

    public static final List<ItemStack> ITEMS = new ArrayList<>();

}