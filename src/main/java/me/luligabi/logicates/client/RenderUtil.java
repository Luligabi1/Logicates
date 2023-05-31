package me.luligabi.logicates.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class RenderUtil {

    public static void drawCenteredShadowless(DrawContext ctx, Text text, int centerX, int y, int color) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        ctx.drawText(textRenderer, text, centerX - textRenderer.getWidth(text) / 2, y, color, false);
    }

}
