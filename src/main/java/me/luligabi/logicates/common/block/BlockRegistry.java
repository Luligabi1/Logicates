package me.luligabi.logicates.common.block;

import me.luligabi.logicates.common.Logicates;
import me.luligabi.logicates.common.block.logicate.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.util.registry.Registry;

public class BlockRegistry {

    public static void init() {
        initBlock("and_logicate", AND_LOGICATE);
        initBlock("or_logicate", OR_LOGICATE);
        initBlock("xor_logicate", XOR_LOGICATE);
        initBlock("not_logicate", NOT_LOGICATE);
        initBlock("nand_logicate", NAND_LOGICATE);
        initBlock("nor_logicate", NOR_LOGICATE);
        initBlock("xnor_logicate", XNOR_LOGICATE);
    }


    public static final Block AND_LOGICATE = new AndLogicateBlock();
    public static final Block OR_LOGICATE = new OrLogicateBlock();
    public static final Block XOR_LOGICATE = new XorLogicateBlock();
    public static final Block NOT_LOGICATE = new NotLogicateBlock();
    public static final Block NAND_LOGICATE = new NandLogicateBlock();
    public static final Block NOR_LOGICATE = new NorLogicateBlock();
    public static final Block XNOR_LOGICATE = new XnorLogicateBlock();



    private static void initBlock(String identifier, Block block) {
        Registry.register(Registry.BLOCK, Logicates.id(identifier), block);
        Registry.register(Registry.ITEM, Logicates.id(identifier), new BlockItem(block, new FabricItemSettings().group(Logicates.ITEM_GROUP)));
    }
}