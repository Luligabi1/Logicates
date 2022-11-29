package me.luligabi.logicates.client.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import me.luligabi.logicates.common.Logicates;
import me.luligabi.logicates.common.misc.screenhandler.KeypadLogicateScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class KeypadLogicateScreen extends HandledScreen<KeypadLogicateScreenHandler> {


    public KeypadLogicateScreen(KeypadLogicateScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.backgroundWidth = 80;
        this.backgroundHeight = 132;
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
        x = width / 2 - backgroundWidth / 2;
        y = height / 2 - backgroundHeight / 2;

        buttons.clear();
        addButton(new NumberButtonWidget(x + 30, y + 102, Text.literal("0"), 0));
        addButton(new NumberButtonWidget(x + 8, y + 36, Text.literal("1"), 1));
        addButton(new NumberButtonWidget(x + 30, y + 36, Text.literal("2"), 2));
        addButton(new NumberButtonWidget(x + 52, y + 36, Text.literal("3"), 3));
        addButton(new NumberButtonWidget(x + 8, y + 58, Text.literal("4"), 4));
        addButton(new NumberButtonWidget(x + 30, y + 58, Text.literal("5"), 5));
        addButton(new NumberButtonWidget(x + 52, y + 58, Text.literal("6"), 6));
        addButton(new NumberButtonWidget(x + 8, y + 80, Text.literal("7"), 7));
        addButton(new NumberButtonWidget(x + 30, y + 80, Text.literal("8"), 8));
        addButton(new NumberButtonWidget(x + 52, y + 80, Text.literal("9"), 9));

        addButton(new DeleteButtonWidget(x + 8, y + 102));
        addButton(new ConfirmButtonWidget(x + 52, y + 102));
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

        if((handler.hasPassword() && handler.getCurrentPassword() > 0) || (!handler.hasPassword() && handler.getPassword() > 0)) {
            drawCenteredShadowless(matrices,
                    String.valueOf(handler.getActivePassword()),
                    width / 2, y + 22, 0x143021
            );
        }
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

    private void drawCenteredShadowless(MatrixStack matrices, String text, int centerX, int y, int color) {
        textRenderer.draw(matrices, text, (float)(centerX - textRenderer.getWidth(text) / 2), (float) y, color);
    }

    private final List<LogicateButtonWidget> buttons = Lists.newArrayList();
    private static final Identifier TEXTURE = Logicates.id("textures/gui/keypad_logicate.png");


    private class NumberButtonWidget extends PressableWidget implements LogicateButtonWidget {

        private final int id;
        private boolean disabled = false;

        protected NumberButtonWidget(int x, int y, Text message, int id) {
            super(x, y, 20, 20, message);
            this.id = id;
        }

        @Override
        public void onPress() {
            if(isDisabled()) return;
            MinecraftClient.getInstance().interactionManager.clickButton(handler.syncId, id);
        }

        @Override
        public void tick() {
            int length = (int) (Math.log10(handler.getActivePassword()) + 1);
            setDisabled(length >= 9);
            if(id == 0) { // Don't allow 0 as the first digit on the password
                setDisabled(handler.getActivePassword() <= 0 || length >= 9);
            }
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
            return false;
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


    private class DeleteButtonWidget extends NumberButtonWidget {

        private boolean disabled = false;

        protected DeleteButtonWidget(int x, int y) {
            super(x, y, Text.of("x"), 11);
        }

        @Override
        public void tick() {
            setDisabled(handler.getActivePassword() <= 0);
        }

        @Override
        public boolean shouldRenderTooltip() {
            return false;
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

    private class ConfirmButtonWidget extends NumberButtonWidget {

        private boolean disabled = false;

        protected ConfirmButtonWidget(int x, int y) {
            super(x, y, Text.of("→"), 10);
        }

        @Override
        public void tick() {
            setDisabled(handler.getActivePassword() <= 0);
        }

        @Override
        public boolean shouldRenderTooltip() {
            return false;
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