package me.luligabi.logicates.common.block;

import me.luligabi.logicates.common.Logicates;
import me.luligabi.logicates.common.block.logicate.AndLogicateBlock;
import me.luligabi.logicates.common.block.logicate.NotLogicateBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.util.registry.Registry;

public class BlockRegistry {

    public static void init() {
        initBlock("and_logicate", AND_LOGICATE);

        initBlock("not_logicate", NOT_LOGICATE);
    }


    public static final Block AND_LOGICATE = new AndLogicateBlock();
    // OR
    // XOR
    public static final Block NOT_LOGICATE = new NotLogicateBlock();
    // NAND
    // NOR
    // XNOR


    private static void initBlock(String identifier, Block block) {
        Registry.register(Registry.BLOCK, Logicates.id(identifier), block);
        Registry.register(Registry.ITEM, Logicates.id(identifier), new BlockItem(block, new FabricItemSettings().group(Logicates.ITEM_GROUP)));
    }
}