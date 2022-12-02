package me.luligabi.logicates.client.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import me.luligabi.logicates.common.Logicates;
import me.luligabi.logicates.common.block.logicate.inputless.keypad.KeypadLogicateBlock;
import me.luligabi.logicates.common.misc.screenhandler.KeypadLogicateScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
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

        addButton(new ClosingDelayButtonWidget(x - 29, y + 44, 13));
        addButton(new ResetPasswordButtonWidget(x - 29, y + 66));
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
        drawTexture(matrices, x - 35, y + 16, 196, 0, 32, 76);

        if((handler.hasPassword() && handler.getCurrentPassword() > 0) || (!handler.hasPassword() && handler.getPassword() > 0)) {
            drawCenteredShadowless(matrices,
                    String.valueOf(handler.getActivePassword()),
                    width / 2, y + 22, 0x143021
            );
        }
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        Text title = Text.translatable("container.logicates.keypad_logicate");

        if(!handler.hasPassword()) {
            title = Text.translatable("container.logicates.keypad_logicate.2");
        } else if(handler.onPasswordReset()) {
            title = Text.translatable("container.logicates.keypad_logicate.3");
        }

        ScreenUtil.drawCenteredShadowless(
                matrices,
                title,
                titleX,
                titleY,
                Formatting.DARK_GRAY.getColorValue()
        );
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

    private class ResetPasswordButtonWidget extends ItemButtonWidget {

        protected ResetPasswordButtonWidget(int x, int y) {
            super(
                    x, y,
                    Items.BARRIER,
                    Text.translatable("text.logicates.keypad_logicate.reset_password.title").formatted(Formatting.GRAY),
                    12
            );
        }

        @Override
        public void tick() {
            setDisabled(!handler.hasPassword());
        }

        @Override
        List<Text> getTooltipText() {
            return List.of(
                    Text.translatable("text.logicates.keypad_logicate.reset_password.title.2").formatted(Formatting.GRAY),
                    Text.translatable(
                            KeypadLogicateScreen.this.handler.onPasswordReset() ?
                                    "text.logicates.keypad_logicate.reset_password.enabled":
                                    ""
                    ).formatted(Formatting.GREEN)
            );
        }
    }

    private class ClosingDelayButtonWidget extends ItemButtonWidget {

        protected ClosingDelayButtonWidget(int x, int y, int id) {
            super(
                    x, y,
                    Items.CLOCK,
                    Text.translatable("text.logicates.keypad_logicate.closing_delay.title").formatted(Formatting.GRAY),
                    id
            );
        }

        @Override
        public void tick() {
            int newOffset = KeypadLogicateScreen.this.handler.getClosingDelay() + KeypadLogicateScreen.this.handler.getClosingDelayOffset();
            setDisabled((newOffset < KeypadLogicateBlock.MIN_CLOSING_DELAY) || (newOffset > KeypadLogicateBlock.MAX_CLOSING_DELAY));
        }

        @Override
        List<Text> getTooltipText() {
            return List.of(
                    Text.translatable(
                            "text.logicates.keypad_logicate.closing_delay.title.2",
                            KeypadLogicateScreen.this.handler.getClosingDelay()
                    ),
                    Text.empty(),
                    Text.translatable("text.logicates.keypad_logicate.closing_delay.title.3"),
                    Text.translatable("text.logicates.keypad_logicate.closing_delay.title.4")
            );
        }
    }

    private abstract class ItemButtonWidget extends PressableWidget implements LogicateButtonWidget {

        private final ItemStack icon;
        protected final int id;
        private boolean disabled = false;

        protected ItemButtonWidget(int x, int y, ItemConvertible icon, Text text, int id) {
            super(x, y, 20, 20, text);
            this.icon = new ItemStack(icon);
            this.id = id;
        }

        @Override
        public void onPress() {
            if(isDisabled()) return;
            MinecraftClient.getInstance().interactionManager.clickButton(handler.syncId, id);
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
            drawTexture(matrices, x, y, 176, v, width, height);

            setZOffset(100);
            itemRenderer.zOffset = 100F;
            RenderSystem.enableDepthTest();
            itemRenderer.renderInGuiWithOverrides(icon, x + 2, y + 2, x + y * backgroundWidth);
            itemRenderer.renderGuiItemOverlay(textRenderer, icon, x + 2, y + 2);
            itemRenderer.zOffset = 0F;
            setZOffset(0);
        }

        abstract List<Text> getTooltipText();

        @Override
        public void renderTooltip(MatrixStack matrices, int mouseX, int mouseY) {
            ArrayList<Text> text = new ArrayList<>();
            text.add(getMessage());
            text.add(Text.empty());
            text.addAll(getTooltipText());

            KeypadLogicateScreen.this.renderTooltip(
                    matrices,
                    text,
                    mouseX, mouseY
            );
        }

        @Override
        public boolean shouldRenderTooltip() {
            return isHovered() && !isDisabled();
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