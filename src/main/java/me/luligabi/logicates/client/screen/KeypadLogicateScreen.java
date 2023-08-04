package me.luligabi.logicates.client.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import me.luligabi.logicates.client.RenderUtil;
import me.luligabi.logicates.common.Logicates;
import me.luligabi.logicates.common.block.logicate.misc.keypad.KeypadLogicateBlock;
import me.luligabi.logicates.common.block.logicate.misc.keypad.KeypadLogicateBlockEntity;
import me.luligabi.logicates.common.misc.screenhandler.KeypadLogicateScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class KeypadLogicateScreen extends HandledScreen<KeypadLogicateScreenHandler> {


    @SuppressWarnings("ConstantConditions")
    public KeypadLogicateScreen(KeypadLogicateScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        blockEntity = (KeypadLogicateBlockEntity) inventory.player.getWorld().getBlockEntity(handler.clientPos);
    }

    @Override
    protected void init() {
        super.init();
        backgroundWidth = 80;
        backgroundHeight = 132;
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

        addButton(new ClosingDelayButtonWidget(x - 29, y + 22));
        addButton(new ResetPasswordButtonWidget(x - 29, y + 44));
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.render(ctx, mouseX, mouseY, delta);
        drawMouseoverTooltip(ctx, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext ctx, float delta, int mouseX, int mouseY) {
        renderBackground(ctx);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        ctx.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
        ctx.drawTexture(TEXTURE, x - 35, y + 16, 196, 0, 32, 54);

        if((blockEntity.hasPassword && !StringUtils.isEmpty(blockEntity.currentPassword)) || (!blockEntity.hasPassword && !StringUtils.isEmpty(blockEntity.password))) {
            RenderUtil.drawCenteredShadowless(
                    ctx,
                    Text.of(blockEntity.getActivePassword()),
                    width / 2, y + 22,
                    0x143021
            );
        }
    }

    @Override
    protected void drawForeground(DrawContext ctx, int mouseX, int mouseY) {
        Text title = Text.translatable(getTitleTranslationKey()).formatted(Formatting.DARK_GRAY);

        RenderUtil.drawCenteredShadowless(ctx, title, backgroundWidth / 2, 6, 0);
        for(LogicateButtonWidget buttonWidget : this.buttons) {
            if(!buttonWidget.shouldRenderTooltip()) continue;
            buttonWidget.renderTooltip(ctx, mouseX - x, mouseY - y);
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

    private String getTitleTranslationKey() {
        if(blockEntity.passwordReset) {
            return "container.logicates.keypad_logicate.reset";
        } else if(!blockEntity.hasPassword) {
            return "container.logicates.keypad_logicate.set";
        }
        return "container.logicates.keypad_logicate";
    }

    private PacketByteBuf blockPosBuf() {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(handler.clientPos);

        return buf;
    }


    @NotNull
    private final KeypadLogicateBlockEntity blockEntity;
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
            PacketByteBuf buf = blockPosBuf();
            buf.writeString(Integer.toString(id));

            ClientPlayNetworking.send(KeypadLogicateBlock.KEYPAD_INSERT, buf);
        }

        @Override
        public void tick() {
            int length = blockEntity.getActivePassword().length();
            setDisabled(length >= 9);
        }

        @Override
        public void renderButton(DrawContext ctx, int mouseX, int mouseY, float delta) {
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

            int v = 20;
            if(isDisabled()) {
                v = 0;
            } else if(isHovered()) {
                v = 40;
            }
            ctx.drawTexture(TEXTURE, getX(), getY(), 176, v, width, height);
            ctx.drawCenteredTextWithShadow(textRenderer, getMessage(), getX() + width / 2, getY() + (height - 8) / 2, active ? 0xFFFFFF : 0xA0A0A0 | MathHelper.ceil(alpha * 255.0F) << 24);
        }

        @Override
        public boolean shouldRenderTooltip() {
            return false;
        }

        @Override
        public void renderTooltip(DrawContext ctx, int mouseX, int mouseY) {
        }

        public boolean isDisabled() {
            return this.disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }

        @Override
        protected void appendClickableNarrations(NarrationMessageBuilder builder) {
            appendDefaultNarrations(builder);
        }

    }


    private class DeleteButtonWidget extends NumberButtonWidget {

        private boolean disabled = false;

        protected DeleteButtonWidget(int x, int y) {
            super(x, y, Text.of("x"), 10);
        }


        @Override
        public void onPress() {
            if(isDisabled()) return;
            ClientPlayNetworking.send(KeypadLogicateBlock.KEYPAD_DELETE, blockPosBuf());
        }

        @Override
        public void tick() {
            setDisabled(StringUtils.isEmpty(blockEntity.getActivePassword()));
        }

        public boolean isDisabled() {
            return this.disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }

    }

    private class ConfirmButtonWidget extends NumberButtonWidget {

        private boolean disabled = false;

        protected ConfirmButtonWidget(int x, int y) {
            super(x, y, Text.of("→"), 11);
        }

        @Override
        public void onPress() {
            if(isDisabled()) return;
            ClientPlayNetworking.send(KeypadLogicateBlock.KEYPAD_CONFIRM, blockPosBuf());
        }

        @Override
        public void tick() {
            setDisabled(StringUtils.isEmpty(blockEntity.getActivePassword()));
        }

        public boolean isDisabled() {
            return this.disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }

    }

    private class ResetPasswordButtonWidget extends ItemButtonWidget {

        protected ResetPasswordButtonWidget(int x, int y) {
            super(
                    x, y,
                    Items.BARRIER,
                    Text.translatable("text.logicates.keypad_logicate.reset_password.title").formatted(Formatting.GRAY),
                    13
            );
        }

        @Override
        public void onPress() {
            if(isDisabled()) return;
            ClientPlayNetworking.send(KeypadLogicateBlock.KEYPAD_TOGGLE_PASSWORD_RESET, blockPosBuf());
        }

        @Override
        public void tick() {
            setDisabled(!blockEntity.hasPassword);
        }

        @Override
        List<Text> getTooltipText() {
            return List.of(
                    Text.translatable("text.logicates.keypad_logicate.reset_password.title.2")
            );
        }
    }

    private class ClosingDelayButtonWidget extends ItemButtonWidget {

        protected ClosingDelayButtonWidget(int x, int y) {
            super(
                    x, y,
                    Items.CLOCK,
                    Text.translatable("text.logicates.keypad_logicate.closing_delay.title").formatted(Formatting.GRAY),
                    12
            );
        }


        @Override
        public void onPress() {
            if(isDisabled()) return;
            PacketByteBuf buf = blockPosBuf();
            buf.writeBoolean(Screen.hasShiftDown());

            ClientPlayNetworking.send(KeypadLogicateBlock.KEYPAD_CLOSING_DELAY, buf);
        }

        @Override
        public void tick() {
            int newOffset = blockEntity.closingDelay + (Screen.hasShiftDown() ? -1 : 1);
            setDisabled((newOffset < KeypadLogicateBlock.MIN_CLOSING_DELAY) || (newOffset > KeypadLogicateBlock.MAX_CLOSING_DELAY));
        }

        @Override
        List<Text> getTooltipText() {
            return List.of(
                    Text.translatable(
                            "text.logicates.keypad_logicate.closing_delay.title.2",
                            blockEntity.closingDelay
                    ),
                    Text.empty(),
                    Text.translatable("text.logicates.keypad_logicate.closing_delay.title.3"),
                    Text.translatable("text.logicates.keypad_logicate.closing_delay.title.4")
            );
        }

        @Override
        public boolean shouldRenderTooltip() {
            return isHovered();
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
        public void renderButton(DrawContext ctx, int mouseX, int mouseY, float delta) {
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

            int v = 20;
            if(isDisabled()) {
                v = 0;
            } else if(isHovered()) {
                v = 40;
            }
            ctx.drawTexture(TEXTURE, getX(), getY(), 176, v, width, height);

            RenderSystem.enableDepthTest();
            ctx.drawItem(icon, getX() + 2, getY() + 2, getX() + getY() * backgroundWidth);
            RenderSystem.disableDepthTest();
        }

        abstract List<Text> getTooltipText();

        @Override
        public void renderTooltip(DrawContext ctx, int mouseX, int mouseY) {
            ArrayList<Text> text = new ArrayList<>();
            text.add(getMessage());
            text.add(Text.empty());
            text.addAll(getTooltipText());

            ctx.drawTooltip(MinecraftClient.getInstance().textRenderer, text, mouseX, mouseY);
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
        protected void appendClickableNarrations(NarrationMessageBuilder builder) {
            appendDefaultNarrations(builder);
        }

    }

}