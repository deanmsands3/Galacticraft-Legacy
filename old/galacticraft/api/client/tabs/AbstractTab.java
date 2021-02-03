package micdoodle8.mods.galacticraft.api.client.tabs;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;

public abstract class AbstractTab extends Button
{
	ResourceLocation texture = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
	ItemStack renderStack;
	public int potionOffsetLast;
    protected ItemRenderer itemRender;
    private int index;

	public AbstractTab(int index, ItemStack renderStack)
	{
		super(0, 0, 28, 32, new TextComponent(""), (b) -> { ((AbstractTab) b).onTabClicked(); });
		this.renderStack = renderStack;
        this.itemRender = Minecraft.getInstance().getItemRenderer();
        this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public void render(PoseStack pose, int mouseX, int mouseY, float partialTicks) {
		int newPotionOffset = TabRegistry.getPotionOffsetNEI();
		Screen screen = Minecraft.getInstance().screen;
		if (screen instanceof InventoryScreen)
		{
			newPotionOffset += TabRegistry.getRecipeBookOffset((InventoryScreen) screen) - TabRegistry.recipeBookOffset;
		}
		if (newPotionOffset != this.potionOffsetLast)
		{
			this.x += newPotionOffset - this.potionOffsetLast;
			this.potionOffsetLast = newPotionOffset;
		}
		if (this.visible)
		{
			GlStateManager._color4f(1.0F, 1.0F, 1.0F, 1.0F);
			int yTexPos = this.active ? 3 : 32;
			int ySize = this.active ? 25 : 32;
			int yPos = this.y + (this.active ? 3 : 0);

			Minecraft mc = Minecraft.getInstance();
			mc.textureManager.bind(this.texture);
			this.blit(pose, this.x, yPos, index * 28, yTexPos, 28, ySize);

			Lighting.turnBackOn();
			this.setBlitOffset(100);
			this.itemRender.blitOffset = 100.0F;
			GlStateManager._enableLighting();
			GlStateManager._enableRescaleNormal();
			this.itemRender.renderAndDecorateItem(this.renderStack, this.x + 6, this.y + 8);
			this.itemRender.renderGuiItemDecorations(mc.font, this.renderStack, this.x + 6, this.y + 8, null);
			GlStateManager._disableLighting();
			GlStateManager._enableBlend();
			this.itemRender.blitOffset = 0.0F;
			this.setBlitOffset(0);
			Lighting.turnOff();
		}
	}

//	@Override
//	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
//	{
//		boolean inWindow = this.enabled && this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
//
//		if (inWindow)
//		{
//			this.onTabClicked();
//		}
//
//		return inWindow;
//	}

	public abstract void onTabClicked();

	public abstract boolean shouldAddToList();
}
