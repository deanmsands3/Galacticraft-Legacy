package micdoodle8.mods.galacticraft.planets.mars.client.gui;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.entities.SlimelingEntity;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars.EnumSimplePacketMars;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;

public class GuiSlimeling extends Screen
{
    private final int xSize;
    private final int ySize;
    private static final ResourceLocation slimelingPanelGui = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/slimeling_panel0.png");
    private final SlimelingEntity slimeling;

    public EditBox nameField;
    public Button stayButton;

    public static boolean renderingOnGui = false;

    private int invX;
    private int invY;
    private final int invWidth = 18;
    private final int invHeight = 18;

    public GuiSlimeling(SlimelingEntity slimeling)
    {
        super(new TextComponent("gui.slimeling.main"));
        this.slimeling = slimeling;
        this.xSize = 176;
        this.ySize = 147;
    }

    @Override
    public void init()
    {
        super.init();
        this.buttons.clear();
//        Keyboard.enableRepeatEvents(true);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.stayButton = new Button(i + 120, j + 122, 50, 20, "", (button) ->
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(EnumSimplePacketMars.S_UPDATE_SLIMELING_DATA, GCCoreUtil.getDimensionType(this.slimeling.level), new Object[]{this.slimeling.getId(), 0, ""}));
        });
        this.stayButton.active = this.slimeling.isOwnedBy(this.minecraft.player);
        this.stayButton.setMessage(this.slimeling.isSitting() ? GCCoreUtil.translate("gui.slimeling.button.follow") : GCCoreUtil.translate("gui.slimeling.button.sit"));
        this.buttons.add(this.stayButton);
        this.nameField = new EditBox(this.font, i + 44, j + 59, 103, 12, this.slimeling.getName().getColoredString());
        this.nameField.setBordered(false);
        this.nameField.setMaxLength(30);
        this.nameField.setFocus(this.slimeling.isOwnedBy(this.minecraft.player));
        this.nameField.setCanLoseFocus(false);
        this.invX = i + 151;
        this.invY = j + 76;
    }

    @Override
    public void tick()
    {
//        if (this.slimeling.isOwner(this.minecraft.player))
//        {
//            this.nameField.updateCursorCounter();
//        }
        this.stayButton.setMessage(this.slimeling.isSitting() ? GCCoreUtil.translate("gui.slimeling.button.follow") : GCCoreUtil.translate("gui.slimeling.button.sit"));
    }

//    @Override
//    public void onGuiClosed()
//    {
//        Keyboard.enableRepeatEvents(false);
//    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

//    @Override
//    protected void keyTyped(char typedChar, int keyCode) throws IOException
//    {
//        if (this.slimeling.isOwner(this.minecraft.player))
//        {
//            if (this.nameField.textboxKeyTyped(typedChar, keyCode))
//            {
//                this.slimeling.setSlimelingName(this.nameField.getText());
//                GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(EnumSimplePacketMars.S_UPDATE_SLIMELING_DATA, GCCoreUtil.getDimensionID(this.slimeling.world), new Object[] { this.slimeling.getEntityId(), 1, this.slimeling.getSlimelingName() }));
//            }
//        }
//        super.keyTyped(typedChar, keyCode);
//    } TODO Slimeling names


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
    {
        if (this.slimeling.isOwnedBy(this.minecraft.player))
        {
            if (this.nameField.mouseClicked(mouseX, mouseY, mouseButton))
            {
                return true;
            }
        }
        if (mouseX >= this.invX && mouseX < this.invX + this.invWidth && mouseY >= this.invY && mouseY < this.invY + this.invHeight)
        {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(EnumSimplePacketMars.S_UPDATE_SLIMELING_DATA, GCCoreUtil.getDimensionType(this.slimeling.level), new Object[]{this.slimeling.getId(), 6, ""}));
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        GlStateManager._pushMatrix();
        GlStateManager._translatef(0, 0, -70.0F);
        GuiComponent.fill(i, j, i + this.xSize, j + this.ySize - 20, 0xFF000000);
        GlStateManager._popMatrix();

        int yOffset = (int) Math.floor(30.0D * (1.0F - this.slimeling.getScale()));

        GuiSlimeling.drawSlimelingOnGui(this.slimeling, this.width / 2, j + 62 - yOffset, 70, i + 51 - mouseX, j + 75 - 50 - mouseY);

        GlStateManager._pushMatrix();
        GlStateManager._translatef(0, 0, 150.0F);
        this.minecraft.textureManager.bind(GuiSlimeling.slimelingPanelGui);
        this.blit(i, j, 0, 0, this.xSize, this.ySize);
        this.blit(i + this.xSize - 15, j + 9, 176, 0, 9, 9);
        this.blit(i + this.xSize - 15, j + 22, 185, 0, 9, 9);
        this.blit(i + this.xSize - 15, j + 35, 194, 0, 9, 9);
        String str = "" + Math.round(this.slimeling.getColorRed() * 1000) / 10.0F + "% ";
        this.drawString(this.font, str, i + this.xSize - 15 - this.font.width(str), j + 10, ColorUtil.to32BitColor(255, 255, 0, 0));
        str = "" + Math.round(this.slimeling.getColorGreen() * 1000) / 10.0F + "% ";
        this.drawString(this.font, str, i + this.xSize - 15 - this.font.width(str), j + 23, ColorUtil.to32BitColor(255, 0, 255, 0));
        str = "" + Math.round(this.slimeling.getColorBlue() * 1000) / 10.0F + "% ";
        this.drawString(this.font, str, i + this.xSize - 15 - this.font.width(str), j + 36, ColorUtil.to32BitColor(255, 0, 0, 255));

        this.minecraft.textureManager.bind(GuiSlimeling.slimelingPanelGui);
        GlStateManager._color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.blit(this.invX, this.invY, 176, 9, this.invWidth, this.invHeight);

        super.render(mouseX, mouseY, partialTicks);

        int dX = -45;
        int dY = 65;
        int startX = -20 + i + 60;
        int startY = dY + j - 10;
        int width = this.xSize - 60;
        int height = 15;
        GuiComponent.fill(startX, startY, startX + width, startY + height, 0xffA0A0A0);
        GuiComponent.fill(startX + 1, startY + 1, startX + width - 1, startY + height - 1, 0xFF000000);
        this.font.draw(GCCoreUtil.translate("gui.slimeling") + ": ", dX + i + 55, dY + j - 6, 0x404040);
//        this.font.drawString(GCCoreUtil.translate("gui.slimeling.owner") + ": " + this.slimeling.getOwnerUsername(), dX + i + 55, dY + j + 7, 0x404040);
        // TODO Owner username?
        this.font.draw(GCCoreUtil.translate("gui.slimeling.kills") + ": " + this.slimeling.getKillCount(), dX + i + 55, dY + j + 20, 0x404040);
        this.font.draw(GCCoreUtil.translate("gui.slimeling.scale") + ": " + Math.round(this.slimeling.getAge() / (float) this.slimeling.MAX_AGE * 1000.0F) / 10.0F + "%", dX + i + 55, dY + j + 33, 0x404040);
        str = "" + (this.slimeling.isSitting() ? GCCoreUtil.translate("gui.slimeling.sitting") : GCCoreUtil.translate("gui.slimeling.following"));
        this.font.draw(str, i + 145 - this.font.width(str) / 2, j + 112, 0x404040);
        str = GCCoreUtil.translate("gui.slimeling.damage") + ": " + Math.round(this.slimeling.getDamage() * 100.0F) / 100.0F;
        this.font.draw(str, dX + i + 55, dY + j + 33 + 13, 0x404040);
        str = GCCoreUtil.translate("gui.slimeling.food") + ": ";
        this.font.draw(str, dX + i + 55, dY + j + 46 + 13, 0x404040);

        Lighting.turnBackOn();
        GlStateManager._enableBlend();
        GlStateManager._enableLighting();
        GlStateManager._enableRescaleNormal();
        this.minecraft.getItemRenderer().renderAndDecorateItem(new ItemStack(this.slimeling.getFavoriteFood()), dX + i + 55 + this.font.width(str), dY + j + 41 + 14);
        GlStateManager._disableLighting();
        GlStateManager._disableBlend();
        this.nameField.renderButton(mouseX, mouseY, partialTicks);
        GlStateManager._popMatrix();
        ItemStack foodStack = new ItemStack(this.slimeling.getFavoriteFood());

        if (!foodStack.isEmpty() && mouseX >= this.invX - 66 && mouseX < this.invX + this.invWidth - 68 && mouseY >= this.invY + 44 && mouseY < this.invY + this.invHeight + 42)
        {
            this.renderTooltip(foodStack, mouseX, mouseY);
        }
    }

    public static void drawSlimelingOnGui(SlimelingEntity slimeling, int x, int y, int scale, float mouseX, float mouseY)
    {
        GuiSlimeling.renderingOnGui = true;
        float f = (float)Math.atan((double)(mouseX / 40.0F));
        float f1 = (float)Math.atan((double)(mouseY / 40.0F));
        RenderSystem.pushMatrix();
        RenderSystem.translatef((float)x, (float)y, 1050.0F);
        RenderSystem.scalef(1.0F, 1.0F, -1.0F);
        PoseStack matrixstack = new PoseStack();
        matrixstack.translate(0.0D, 0.0D, 1000.0D);
        matrixstack.scale((float)scale, (float)scale, (float)scale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
        quaternion.mul(quaternion1);
        matrixstack.mulPose(quaternion);
        float f2 = slimeling.yBodyRot;
        float f3 = slimeling.yRot;
        float f4 = slimeling.xRot;
        float f5 = slimeling.yHeadRotO;
        float f6 = slimeling.yHeadRot;
        slimeling.yBodyRot = 180.0F + f * 20.0F;
        slimeling.yRot = 180.0F + f * 40.0F;
        slimeling.xRot = -f1 * 20.0F;
        slimeling.yHeadRot = slimeling.yRot;
        slimeling.yHeadRotO = slimeling.yRot;
        EntityRenderDispatcher entityrenderermanager = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion1.conj();
        entityrenderermanager.overrideCameraOrientation(quaternion1);
        entityrenderermanager.setRenderShadow(false);
        MultiBufferSource.BufferSource irendertypebuffer$impl = Minecraft.getInstance().renderBuffers().bufferSource();
        entityrenderermanager.render(slimeling, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack, irendertypebuffer$impl, 15728880);
        irendertypebuffer$impl.endBatch();
        entityrenderermanager.setRenderShadow(true);
        slimeling.yBodyRot = f2;
        slimeling.yRot = f3;
        slimeling.xRot = f4;
        slimeling.yHeadRotO = f5;
        slimeling.yHeadRot = f6;
        RenderSystem.popMatrix();
        GuiSlimeling.renderingOnGui = false;
    }
}
