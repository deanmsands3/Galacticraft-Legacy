package micdoodle8.mods.galacticraft.core.client.gui.container;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.screen.InventoryTabGalacticraft;
import micdoodle8.mods.galacticraft.core.inventory.ContainerEnergyStorageModule;
import micdoodle8.mods.galacticraft.core.inventory.ContainerExtendedInventory;
import micdoodle8.mods.galacticraft.core.inventory.InventoryExtended;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GuiExtendedInventory extends EffectRenderingInventoryScreen<ContainerExtendedInventory>
{
    private static final ResourceLocation inventoryTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/inventory.png");
    private int potionOffsetLast;
    private static float rotation = 0.0F;
    private boolean initWithPotion;

    public GuiExtendedInventory(ContainerExtendedInventory container, Inventory playerInv, Component title)
    {
        super(container, playerInv, title);
//        super(new ContainerExtendedInventory(playerInv, inventory), playerInv.inventory, new StringTextComponent("Extended Inventory"));
    }

    @Override
    protected void renderLabels(int mouseX, int mouseY)
    {
        GuiExtendedInventory.drawPlayerOnGui(this.minecraft, 33, 80, 29);
    }

    @Override
    protected void init()
    {
        super.init();

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.leftPos += this.getPotionOffset();
        this.potionOffsetLast = this.getPotionOffsetNEI();

        int cornerX = this.leftPos;
        int cornerY = this.topPos;

        TabRegistry.updateTabValues(cornerX, cornerY, InventoryTabGalacticraft.class);
        TabRegistry.addTabsToList(this::addButton);

        this.buttons.add(new Button(this.leftPos + 10, this.topPos + 71, 7, 7, "", (button) ->
        {
            GuiExtendedInventory.rotation += 10.0F;
        }));
        this.buttons.add(new Button(this.leftPos + 51, this.topPos + 71, 7, 7, "", (button) ->
        {
            GuiExtendedInventory.rotation -= 10.0F;
        }));
    }

    @Override
    protected void renderBg(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager._color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(GuiExtendedInventory.inventoryTexture);
        this.blit(this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        int newPotionOffset = this.getPotionOffsetNEI();

        if (newPotionOffset < this.potionOffsetLast)
        {
            int diff = newPotionOffset - this.potionOffsetLast;
            this.potionOffsetLast = newPotionOffset;
            this.leftPos += diff;

//            for (int k = 0; k < this.buttons.size(); ++k)
//            {
//                Widget widget = this.buttons.get(k);
//
//                if (!(widget instanceof AbstractTab))
//                {
//                    widget.x += diff;
//                }
//            }
        }
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderTooltip(mouseX, mouseY);
    }

    public static void drawPlayerOnGui(Minecraft minecraft, int x, int y, int scale)
    {
        LocalPlayer player = minecraft.player;
        RenderSystem.pushMatrix();
//        RenderSystem.translatef((float)x, (float)y, 1050.0F);
//        RenderSystem.scalef(1.0F, 1.0F, -1.0F);
        PoseStack matrixstack = new PoseStack();
        matrixstack.translate(x, y, 1000.0D);
        matrixstack.scale((float)scale, (float)scale, (float)-scale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
//        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
//        quaternion.multiply(quaternion1);
        matrixstack.mulPose(quaternion);
        float f2 = player.yBodyRot;
        float f3 = player.yRot;
        float f4 = player.xRot;
        float f5 = player.yHeadRotO;
        float f6 = player.yHeadRot;
        player.yBodyRot = GuiExtendedInventory.rotation;
        player.xRot = (float) Math.sin(Util.getMillis() / 500.0F) * 3.0F;
        player.yRot = GuiExtendedInventory.rotation;
        player.yHeadRotO = player.yRot;
        EntityRenderDispatcher entityrenderermanager = Minecraft.getInstance().getEntityRenderDispatcher();
//        quaternion1.conjugate();
//        entityrenderermanager.setCameraOrientation(quaternion1);
        entityrenderermanager.setRenderShadow(false);
        MultiBufferSource.BufferSource irendertypebuffer$impl = Minecraft.getInstance().renderBuffers().bufferSource();
        entityrenderermanager.render(player, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack, irendertypebuffer$impl, 15728880);
        irendertypebuffer$impl.endBatch();
        entityrenderermanager.setRenderShadow(true);
        player.yBodyRot = f2;
        player.yRot = f3;
        player.xRot = f4;
        player.yHeadRotO = f5;
        player.yHeadRot = f6;
        RenderSystem.popMatrix();
    }

    //Instanced method of this to have the instance field initWithPotion
    public int getPotionOffset()
    {
        /*Disabled in 1.12.2 because a vanilla bug means potion offsets are currently not a thing
         *The vanilla bug is that GuiInventory.init() resets GuiLeft to the recipe book version of GuiLeft,
         *and in GuiRecipeBook.updateScreenPosition() it takes no account of potion offset even if the recipe book is inactive.

        // If at least one potion is active...
        if (this.hasActivePotionEffects)
        {
            this.initWithPotion = true;
            return 60 + TabRegistry.getPotionOffsetJEI() + getPotionOffsetNEI();
        }
         */
        // No potions, no offset needed
        this.initWithPotion = false;
        return 0;
    }

    //Instanced method of this to use the instance field initWithPotion
    public int getPotionOffsetNEI()
    {
//        if (this.initWithPotion && TabRegistry.clazzNEIConfig != null)
//        {
//            try
//            {
//                // Check whether NEI is hidden and enabled
//                Object hidden = TabRegistry.clazzNEIConfig.getMethod("isHidden").invoke(null);
//                Object enabled = TabRegistry.clazzNEIConfig.getMethod("isEnabled").invoke(null);
//
//                if (hidden instanceof Boolean && enabled instanceof Boolean)
//                {
//                    if ((Boolean) hidden || !((Boolean) enabled))
//                    {
//                        // If NEI is disabled or hidden, offset the tabs by the standard 60
//                        return 0;
//                    }
//                    //Active NEI undoes the standard potion offset
//                    return -60;
//                }
//            }
//            catch (Exception e)
//            {
//            }
//        } TODO Inv tabs
        //No NEI, no change
        return 0;
    }
}
