package me.luligabi.logicates.client;

import me.luligabi.logicates.client.renderer.TimerLogicateBlockEntityRenderer;
import me.luligabi.logicates.client.screen.KeypadLogicateScreen;
import me.luligabi.logicates.client.screen.LogicateFabricatorScreen;
import me.luligabi.logicates.client.screen.TimerLogicateScreen;
import me.luligabi.logicates.common.block.BlockRegistry;
import me.luligabi.logicates.common.misc.screenhandler.ScreenHandlingRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

@Environment(EnvType.CLIENT)
public class LogicatesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HandledScreens.register(ScreenHandlingRegistry.LOGICATE_FABRICATOR_SCREEN_HANDLER, LogicateFabricatorScreen::new);
        HandledScreens.register(ScreenHandlingRegistry.TIMER_LOGICATE_SCREEN_HANDLER, TimerLogicateScreen::new);
        HandledScreens.register(ScreenHandlingRegistry.KEYPAD_LOGICATE_SCREEN_HANDLER, KeypadLogicateScreen::new);

        BlockEntityRendererFactories.register(BlockRegistry.TIMER_LOGICATE_BLOCK_ENTITY_TYPE, TimerLogicateBlockEntityRenderer::new);

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
                BlockRegistry.AND_LOGICATE,
                BlockRegistry.OR_LOGICATE,
                BlockRegistry.XOR_LOGICATE,
                BlockRegistry.NOT_LOGICATE,
                BlockRegistry.NAND_LOGICATE,
                BlockRegistry.NOR_LOGICATE,
                BlockRegistry.XNOR_LOGICATE,

                BlockRegistry.TOGGLE_LOGICATE,
                BlockRegistry.TIMER_LOGICATE,

                BlockRegistry.WEATHER_LOGICATE
        );
    }
}