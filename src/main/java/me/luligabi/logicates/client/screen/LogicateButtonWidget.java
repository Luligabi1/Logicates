package me.luligabi.logicates.client.screen;

import net.minecraft.client.gui.DrawContext;

public interface LogicateButtonWidget {

    boolean shouldRenderTooltip();

    void renderTooltip(DrawContext ctx, int mouseX, int mouseY);

    void tick();
}