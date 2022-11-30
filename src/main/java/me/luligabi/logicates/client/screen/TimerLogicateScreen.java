package me.luligabi.logicates.client.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import me.luligabi.logicates.common.Logicates;
import me.luligabi.logicates.common.block.logicate.inputless.timer.TimerLogicateBlock;
import me.luligabi.logicates.common.misc.screenhandler.TimerLogicateScreenHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.text.DecimalFormat;
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
        addButton(new ButtonWidget(x + 7, y + 54, -4000, Text.literal("--"), 0));
        addButton(new ButtonWidget(x + 43, y + 54, -20, Text.literal("-"), 2));
        addButton(new ButtonWidget(x + 101, y + 54, 20, Text.literal("+"), 4));
        addButton(new ButtonWidget(x + 137, y + 54, 4000, Text.literal("++"), 6));

        addButton(new MuteButtonWidget(x + 149, y + 6));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @SuppressWarnings("ConstantConditions") // Formatting#getColorValue will never return a null value using DARK_GRAY.
    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        this.renderBackground(matrices);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

        int ticks = handler.getPropertyDelegate().get(0);
        drawCenteredShadowless(matrices,
                Text.translatable(ticks >= 39 ? "text.logicates.timer_logicate.ticks_plural" : "text.logicates.timer_logicate.ticks", ticks, df.format((float) ticks / 20)), // >= 39 since values are changed only every two ticks, so 39 ticks is considered 2 seconds
                width / 2, y + 32, Formatting.DARK_GRAY.getColorValue());
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        this.textRenderer.draw(matrices, title, (float) titleX, (float) titleY, 0x404040);
        for(LogicateButtonWidget buttonWidget : this.buttons) {
            if(!buttonWidget.shouldRenderTooltip()) continue;
            buttonWidget.renderTooltip(matrices, mouseX - x, mouseY - y);
            break;
        }
    }

    @Override
    public void handledScreenTick() {
        super.handledScreenTick();
        buttons.forEach(LogicateButtonWidget::tick);
    }


    private <T extends ClickableWidget> void addButton(T button) {
        this.addDrawableChild(button);
        this.buttons.add((LogicateButtonWidget) button);
    }

    private void drawCenteredShadowless(MatrixStack matrices, Text text, int centerX, int y, int color) {
        OrderedText orderedText = text.asOrderedText();
        textRenderer.draw(matrices, orderedText, (float)(centerX - textRenderer.getWidth(orderedText) / 2), (float) y, color);
    }

    DecimalFormat df = new DecimalFormat("##.#");
    private final List<LogicateButtonWidget> buttons = Lists.newArrayList();
    private static final Identifier TEXTURE = Logicates.id("textures/gui/timer_logicate.png");


    private class ButtonWidget extends PressableWidget implements LogicateButtonWidget {

        private final int id;
        private final int timerOffset;
        private boolean disabled = false;

        protected ButtonWidget(int x, int y, int timerOffset, Text message, int id) {
            super(x, y, 32, 20, message);
            this.timerOffset = timerOffset;
            this.id = id;
        }

        @Override
        public void onPress() {
            if(isDisabled()) return;
            int newOffset = TimerLogicateScreen.this.handler.getPropertyDelegate().get(0) + getTimerOffset();
            if(newOffset >= TimerLogicateBlock.MIN_VALUE && newOffset <= TimerLogicateBlock.MAX_VALUE) {
                //TimerLogicateScreen.this.handler.getPropertyDelegate().set(0, newOffset);
                client.interactionManager.clickButton(handler.syncId, Screen.hasShiftDown() ? id + 1 : id);
            }
        }

        @Override
        public void tick() {
            int newOffset = TimerLogicateScreen.this.handler.getPropertyDelegate().get(0) + getTimerOffset();
            setDisabled((newOffset < TimerLogicateBlock.MIN_VALUE) || (newOffset > TimerLogicateBlock.MAX_VALUE));
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
        public void renderTooltip(MatrixStack matrices, int mouseX, int mouseY) {
            String firstLineKey = timerOffset > 0 ? "text.logicates.timer_logicate.increase" : "text.logicates.timer_logicate.decrease";
            String secondLineKey = timerOffset > 0 ? "text.logicates.timer_logicate.shift_increase" : "text.logicates.timer_logicate.shift_decrease";
            TimerLogicateScreen.this.renderTooltip(matrices, List.of(
                    Text.translatable(firstLineKey, timerOffset > 0 ? timerOffset : Math.abs(timerOffset)),
                    Text.translatable(secondLineKey, timerOffset > 0 ? timerOffset/20 : Math.abs(timerOffset/20))
            ), mouseX, mouseY);
        }

        @Override
        public boolean shouldRenderTooltip() {
            return this.hovered && !disabled;
        }

        public int getTimerOffset() {
            return Screen.hasShiftDown() ? timerOffset/20 : timerOffset;
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

    private class MuteButtonWidget extends PressableWidget implements LogicateButtonWidget {

        protected MuteButtonWidget(int x, int y) {
            super(x, y, 20, 20, Text.empty());
        }

        @Override
        public void onPress() {
            client.interactionManager.clickButton(handler.syncId, TimerLogicateScreen.this.handler.getPropertyDelegate().get(1) == 1 ? 8 : 9);
        }

        @Override
        public void tick() {}

        @Override
        public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, TEXTURE);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

            int u = TimerLogicateScreen.this.handler.getPropertyDelegate().get(1) == 1 ? 196 : 176;
            int v = isHovered() ? 80 : 60;
            this.drawTexture(matrices, x, y, u, v, width, height);
            ClickableWidget.drawCenteredText(matrices, textRenderer, getMessage(), x + width / 2, y + (height - 8) / 2, active ? 0xFFFFFF : 0xA0A0A0 | MathHelper.ceil(alpha * 255.0f) << 24);
        }

        @Override
        public void renderTooltip(MatrixStack matrices, int mouseX, int mouseY) {
            String key = TimerLogicateScreen.this.handler.getPropertyDelegate().get(1) == 1 ? "text.logicates.timer_logicate.unmute" : "text.logicates.timer_logicate.mute";
            TimerLogicateScreen.this.renderTooltip(matrices,
                    Text.translatable(key).formatted(Formatting.GRAY),
            mouseX, mouseY);
        }

        @Override
        public boolean shouldRenderTooltip() {
            return this.hovered;
        }

        @Override
        public void appendNarrations(NarrationMessageBuilder builder) {
            this.appendDefaultNarrations(builder);
        }
    }

}