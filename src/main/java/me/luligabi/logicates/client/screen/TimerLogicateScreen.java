package me.luligabi.logicates.client.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import me.luligabi.logicates.common.Logicates;
import me.luligabi.logicates.common.misc.screenhandler.TimerLogicateScreenHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class TimerLogicateScreen extends HandledScreen<TimerLogicateScreenHandler> {


    public TimerLogicateScreen(TimerLogicateScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.backgroundHeight = 80;
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
        x = width / 2 - backgroundWidth / 2;
        y = height / 2 - backgroundHeight / 2;

        buttons.clear();
        addButton(new ButtonWidget(x + 7, y + 54, -20, Text.literal("--")));
        addButton(new ButtonWidget(x + 43, y + 54, -1, Text.literal("-")));
        addButton(new ButtonWidget(x + 101, y + 54, 1, Text.literal("+")));
        addButton(new ButtonWidget(x + 137, y + 54, 20, Text.literal("++")));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        this.renderBackground(matrices);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

        drawCenteredText(matrices, textRenderer, Text.of(String.valueOf(handler.getPropertyDelegate().get(0))), width / 2, y + 32, 0x404040);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        this.textRenderer.draw(matrices, title, (float) titleX, (float) titleY, 0x404040);
        for(TimerButtonWidget buttonWidget : this.buttons) {
            if(!buttonWidget.shouldRenderTooltip()) continue;
            buttonWidget.renderTooltip(matrices, mouseX - x, mouseY - y);
            break;
        }
    }

    @Override
    public void handledScreenTick() {
        super.handledScreenTick();
        buttons.forEach(TimerButtonWidget::tick);
    }


    private <T extends ClickableWidget> void addButton(T button) {
        this.addDrawableChild(button);
        this.buttons.add((TimerButtonWidget) button);
    }

    private final List<TimerButtonWidget> buttons = Lists.newArrayList();
    private static final Identifier TEXTURE = Logicates.id("textures/gui/timer_logicate.png");


    interface TimerButtonWidget {

        boolean shouldRenderTooltip();

        void renderTooltip(MatrixStack var1, int var2, int var3);

        void tick();
    }

    private class ButtonWidget extends PressableWidget implements TimerButtonWidget {

        private final int timerOffset;
        private boolean disabled = false;

        protected ButtonWidget(int x, int y, int timerOffset, Text message) {
            super(x, y, 32, 20, message);
            this.timerOffset = timerOffset;
        }

        @Override
        public void onPress() {
            int newOffset = TimerLogicateScreen.this.handler.getPropertyDelegate().get(0) + getTimerOffset();
            if(newOffset >= 0 && newOffset <= 10000*20) {
                TimerLogicateScreen.this.handler.getPropertyDelegate().set(0, newOffset);
            }
        }

        @Override
        public void tick() {
            int newOffset = TimerLogicateScreen.this.handler.getPropertyDelegate().get(0) + getTimerOffset();
            setDisabled((newOffset < 0) || (newOffset > 10000*20));
        }

        @Override
        public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, TEXTURE);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

            int v = 20;

            if(isDisabled()) {
                v = 0;
            } else if(isHovered()) {
                v = 40;
            }
            this.drawTexture(matrices, x, y, 176, v, width, height);
            ClickableWidget.drawCenteredText(matrices, textRenderer, getMessage(), x + width / 2, y + (height - 8) / 2, active ? 0xFFFFFF : 0xA0A0A0 | MathHelper.ceil(alpha * 255.0f) << 24);
        }

        @Override
        public boolean shouldRenderTooltip() {
            return this.hovered && !disabled;
        }

        public int getTimerOffset() {
            return Screen.hasShiftDown() ? timerOffset*5 : timerOffset;
        }

        public boolean isDisabled() {
            return this.disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }

        @Override
        public void appendNarrations(NarrationMessageBuilder builder) {
            this.appendDefaultNarrations(builder);
        }
    }

}