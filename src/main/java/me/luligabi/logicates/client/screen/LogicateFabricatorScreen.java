package me.luligabi.logicates.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import me.luligabi.logicates.common.Logicates;
import me.luligabi.logicates.common.misc.recipe.LogicateFabricationRecipe;
import me.luligabi.logicates.common.misc.screenhandler.LogicateFabricatorScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class LogicateFabricatorScreen extends HandledScreen<LogicateFabricatorScreenHandler> {


    public LogicateFabricatorScreen(LogicateFabricatorScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    protected void init() {
        super.init();
        handler.setContentsChangedListener(this::onInventoryChange);
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
        --titleY;
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        this.renderBackground(matrices);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int i = x;
        int j = y;
        drawTexture(matrices, i, j, 0, 0, backgroundWidth, backgroundHeight);
        int k = (int) (41.0F * this.scrollAmount);
        drawTexture(matrices, i + 119, j + 15 + k, 176 + (shouldScroll() ? 0 : 12), 0, 12, 15);
        int l = x + 52;
        int m = y + 14;
        int n = scrollOffset + 12;
        renderRecipeBackground(matrices, mouseX, mouseY, l, m, n);
        renderRecipeIcons(l, m, n);
    }

    protected void drawMouseoverTooltip(MatrixStack matrices, int x, int y) {
        super.drawMouseoverTooltip(matrices, x, y);
        if(canCraft) {
            int i = x + 52;
            int j = y + 14;
            int k = scrollOffset + 12;
            List<LogicateFabricationRecipe> list = handler.getAvailableRecipes();

            for(int l = scrollOffset; l < k && l < handler.getAvailableRecipeCount(); ++l) {
                int m = l - scrollOffset;
                int n = i + m % 4 * 16;
                int o = j + m / 4 * 18 + 2;
                if (x >= n && x < n + 16 && y >= o && y < o + 18) {
                    renderTooltip(matrices, list.get(l).getOutput(), x, y);
                }
            }
        }

    }

    private void renderRecipeBackground(MatrixStack matrices, int mouseX, int mouseY, int x, int y, int scrollOffset) {
        for(int i = this.scrollOffset; i < scrollOffset && i < handler.getAvailableRecipeCount(); ++i) {
            int j = i - this.scrollOffset;
            int k = x + j % 4 * 16;
            int l = j / 4;
            int m = y + l * 18 + 2;
            int n = this.backgroundHeight;
            if (i == this.handler.getSelectedRecipe()) {
                n += 18;
            } else if (mouseX >= k && mouseY >= m && mouseX < k + 16 && mouseY < m + 18) {
                n += 36;
            }

            this.drawTexture(matrices, k, m - 1, 0, n, 16, 18);
        }

    }

    private void renderRecipeIcons(int x, int y, int scrollOffset) {
        List<LogicateFabricationRecipe> list = this.handler.getAvailableRecipes();

        for(int i = this.scrollOffset; i < scrollOffset && i < this.handler.getAvailableRecipeCount(); ++i) {
            int j = i - this.scrollOffset;
            int k = x + j % 4 * 16;
            int l = j / 4;
            int m = y + l * 18 + 2;
            client.getItemRenderer().renderInGuiWithOverrides(list.get(i).getOutput(), k, m);
        }

    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.mouseClicked = false;
        if (this.canCraft) {
            int i = x + 52;
            int j = y + 14;
            int k = scrollOffset + 12;

            for(int l = scrollOffset; l < k; ++l) {
                int m = l - scrollOffset;
                double d = mouseX - (double) (i + m % 4 * 16);
                double e = mouseY - (double) (j + m / 4 * 18);
                if (d >= 0.0 && e >= 0.0 && d < 16.0 && e < 18.0 && this.handler.onButtonClick(client.player, l)) {
                    MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                    client.interactionManager.clickButton((this.handler).syncId, l);
                    return true;
                }
            }

            i = x + 119;
            j = y + 9;
            if (mouseX >= (double) i && mouseX < (double)(i + 12) && mouseY >= (double)j && mouseY < (double)(j + 54)) {
                this.mouseClicked = true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (mouseClicked && shouldScroll()) {
            int i = y + 14;
            int j = i + 54;
            scrollAmount = ((float) mouseY - (float) i - 7.5F) / ((float) (j - i) - 15.0F);
            scrollAmount = MathHelper.clamp(scrollAmount, 0.0F, 1.0F);
            scrollOffset = (int) ((double) (scrollAmount * (float) getMaxScroll()) + 0.5) * 4;
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (this.shouldScroll()) {
            int i = this.getMaxScroll();
            float f = (float)amount / (float)i;
            scrollAmount = MathHelper.clamp(this.scrollAmount - f, 0.0F, 1.0F);
            scrollOffset = (int)((double)(this.scrollAmount * (float)i) + 0.5) * 4;
        }

        return true;
    }

    private boolean shouldScroll() {
        return canCraft && handler.getAvailableRecipeCount() > 12;
    }

    protected int getMaxScroll() {
        return (handler.getAvailableRecipeCount() + 4 - 1) / 4 - 3;
    }

    private void onInventoryChange() {
        canCraft = handler.canCraft();
        if (!canCraft) {
            scrollAmount = 0.0F;
            scrollOffset = 0;
        }

    }

    private float scrollAmount;
    private boolean mouseClicked;
    private int scrollOffset;
    private boolean canCraft;
    private static final Identifier TEXTURE = Logicates.id("textures/gui/logicate_fabricator.png");

}