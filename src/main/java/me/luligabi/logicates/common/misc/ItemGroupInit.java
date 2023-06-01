package me.luligabi.logicates.common.misc;

import me.luligabi.logicates.common.Logicates;
import me.luligabi.logicates.common.block.BlockRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ItemGroupInit {

    public static void init() {
        //noinspection UnstableApiUsage
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(entries -> // TODO organize stuff better
                entries.addAfter(new ItemStack(Items.COMPARATOR), ITEMS)
        );

        Registry.register(Registries.ITEM_GROUP, Logicates.ITEM_GROUP, FabricItemGroup.builder()
                .displayName(Text.translatable("itemGroup.logicates.item_group"))
                .icon(() -> new ItemStack(BlockRegistry.XNOR_LOGICATE))
                .entries((context, entries) ->
                        entries.addAll(ItemGroupInit.ITEMS)
                )
                .build());
    }

    public static final List<ItemStack> ITEMS = new ArrayList<>();
}
