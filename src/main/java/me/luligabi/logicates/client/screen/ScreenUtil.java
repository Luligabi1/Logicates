package me.luligabi.logicates.client.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

public class ScreenUtil {

    public static void drawCenteredShadowless(MatrixStack matrices, Text text, int centerX, int y, int color) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        OrderedText orderedText = text.asOrderedText();
        textRenderer.draw(matrices, orderedText, (float) (centerX - textRenderer.getWidth(orderedText) / 2), (float) y, color);
    }

}
