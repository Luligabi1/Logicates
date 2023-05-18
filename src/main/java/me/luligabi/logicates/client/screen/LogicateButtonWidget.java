package me.luligabi.logicates.client.screen;

import net.minecraft.client.util.math.MatrixStack;

public interface LogicateButtonWidget {

    boolean shouldRenderTooltip();

    void renderTooltip(MatrixStack var1, int mouseX, int mouseY);

    void tick();
}