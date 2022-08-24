package me.luligabi.logicates.client;

import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import me.luligabi.logicates.client.renderer.TimerLogicateBlockEntityRenderer;
import me.luligabi.logicates.client.screen.LogicateFabricatorScreen;
import me.luligabi.logicates.client.screen.TimerLogicateScreen;
import me.luligabi.logicates.common.block.BlockRegistry;
import me.luligabi.logicates.common.misc.screenhandler.LogicateFabricatorScreenHandler;
import me.luligabi.logicates.common.misc.screenhandler.ScreenHandlingRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class LogicatesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HandledScreens.register(ScreenHandlingRegistry.LOGICATE_FABRICATOR_SCREEN_HANDLER, LogicateFabricatorScreen::new);
        HandledScreens.register(ScreenHandlingRegistry.TIMER_LOGICATE_SCREEN_HANDLER, TimerLogicateScreen::new);

        BlockEntityRendererRegistry.register(BlockRegistry.TIMER_LOGICATE_BLOCK_ENTITY_TYPE, TimerLogicateBlockEntityRenderer::new);

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
                BlockRegistry.AND_LOGICATE,
                BlockRegistry.OR_LOGICATE,
                BlockRegistry.XOR_LOGICATE,
                BlockRegistry.NOT_LOGICATE,
                BlockRegistry.NAND_LOGICATE,
                BlockRegistry.NOR_LOGICATE,
                BlockRegistry.XNOR_LOGICATE,

                BlockRegistry.TIMER_LOGICATE,
                BlockRegistry.WEATHER_LOGICATE
        );
    }

}