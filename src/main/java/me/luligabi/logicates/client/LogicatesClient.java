package me.luligabi.logicates.client;

import me.luligabi.logicates.client.screen.LogicateFabricatorScreen;
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
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
                BlockRegistry.NOT_LOGICATE
        );

        HandledScreens.register(ScreenHandlingRegistry.LOGICATE_FABRICATOR_SCREEN_HANDLER, LogicateFabricatorScreen::new);
    }

}