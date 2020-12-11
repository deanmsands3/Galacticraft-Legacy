package team.galacticraft.galacticraft.common.core.client.model;

import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.entities.EntityParachest;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Calendar;

public class ModelParaChest extends EntityModel<EntityParachest>
{
    private static final ResourceLocation grayParachuteTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/gray.png");

    private final ModelPart singleLid;
    private final ModelPart singleBottom;
    private final ModelPart singleLatch;
    private boolean isChristmas;

    public ModelPart[] parachute = new ModelPart[3];
    public ModelPart[] parachuteStrings = new ModelPart[4];

    public ModelParaChest()
    {
        this(0.0F);
    }

    public ModelParaChest(float par1)
    {
        super();

        Calendar calendar = Calendar.getInstance();
        if (calendar.get(2) + 1 == 12 && calendar.get(5) >= 24 && calendar.get(5) <= 26) {
            this.isChristmas = true;
        }

        this.singleBottom = new ModelPart(64, 64, 0, 19);
        this.singleBottom.addBox(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F, 0.0F);
        this.singleLid = new ModelPart(64, 64, 0, 0);
        this.singleLid.addBox(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F, 0.0F);
        this.singleLid.y = 9.0F;
        this.singleLid.z = 1.0F;
        this.singleLatch = new ModelPart(64, 64, 0, 0);
        this.singleLatch.addBox(7.0F, -1.0F, 15.0F, 2.0F, 4.0F, 1.0F, 0.0F);
        this.singleLatch.y = 8.0F;

        this.parachute[0] = new ModelPart(this, 0, 0).setTexSize(256, 256);
        this.parachute[0].addBox(-20.0F, -45.0F, -20.0F, 10, 2, 40, par1);
        this.parachute[0].setPos(15.0F, 4.0F, 0.0F);
        this.parachute[1] = new ModelPart(this, 0, 42).setTexSize(256, 256);
        this.parachute[1].addBox(-20.0F, -45.0F, -20.0F, 40, 2, 40, par1);
        this.parachute[1].setPos(0.0F, 0.0F, 0.0F);
        this.parachute[2] = new ModelPart(this, 0, 0).setTexSize(256, 256);
        this.parachute[2].addBox(-20.0F, -45.0F, -20.0F, 10, 2, 40, par1);
        this.parachute[2].setPos(11F, -11, 0.0F);

        this.parachuteStrings[0] = new ModelPart(this, 100, 0).setTexSize(256, 256);
        this.parachuteStrings[0].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, par1);
        this.parachuteStrings[0].setPos(0.0F, 0.0F, 0.0F);
        this.parachuteStrings[1] = new ModelPart(this, 100, 0).setTexSize(256, 256);
        this.parachuteStrings[1].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, par1);
        this.parachuteStrings[1].setPos(0.0F, 0.0F, 0.0F);
        this.parachuteStrings[2] = new ModelPart(this, 100, 0).setTexSize(256, 256);
        this.parachuteStrings[2].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, par1);
        this.parachuteStrings[2].setPos(0.0F, 0.0F, 0.0F);
        this.parachuteStrings[3] = new ModelPart(this, 100, 0).setTexSize(256, 256);
        this.parachuteStrings[3].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, par1);
        this.parachuteStrings[3].setPos(0.0F, 0.0F, 0.0F);
    }

    @Override
    public void setRotationAngles(EntityParachest entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        this.parachute[0].yRot = 0 / Constants.RADIANS_TO_DEGREES;
        this.parachute[2].yRot = -(0 / Constants.RADIANS_TO_DEGREES);
        this.parachuteStrings[0].yRot = 0 / Constants.RADIANS_TO_DEGREES;
        this.parachuteStrings[1].yRot = 0 / Constants.RADIANS_TO_DEGREES;
        this.parachuteStrings[2].yRot = -(0 / Constants.RADIANS_TO_DEGREES);
        this.parachuteStrings[3].yRot = -(0 / Constants.RADIANS_TO_DEGREES);

        this.parachute[0].setPos(-5.85F, -11.0F, 2.0F);
        this.parachute[1].setPos(9F, -7F, 2.0F);
        this.parachute[2].setPos(-2.15F, 4.0F, 2.0F);
        this.parachute[0].zRot = 210F / Constants.RADIANS_TO_DEGREES;
        this.parachute[1].zRot = 180F / Constants.RADIANS_TO_DEGREES;
        this.parachute[2].zRot = -(210F / Constants.RADIANS_TO_DEGREES);
        this.parachuteStrings[0].zRot = (155F + 180F) / Constants.RADIANS_TO_DEGREES;
        this.parachuteStrings[0].xRot = 23F / Constants.RADIANS_TO_DEGREES;
        this.parachuteStrings[0].setPos(9.0F, 3.0F, 2.0F);
        this.parachuteStrings[1].zRot = (155F + 180F) / Constants.RADIANS_TO_DEGREES;
        this.parachuteStrings[1].xRot = -(23F / Constants.RADIANS_TO_DEGREES);
        this.parachuteStrings[1].setPos(9.0F, 3.0F, 2.0F);

        this.parachuteStrings[2].zRot = -((155F + 180F) / Constants.RADIANS_TO_DEGREES);
        this.parachuteStrings[2].xRot = 23F / Constants.RADIANS_TO_DEGREES;
        this.parachuteStrings[2].setPos(9.0F, 3.0F, 2.0F);
        this.parachuteStrings[3].zRot = -((155F + 180F) / Constants.RADIANS_TO_DEGREES);
        this.parachuteStrings[3].xRot = -(23F / Constants.RADIANS_TO_DEGREES);
        this.parachuteStrings[3].setPos(9.0F, 3.0F, 2.0F);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        int i;

        for (i = 0; i < this.parachute.length; i++)
        {
            this.parachute[i].render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        }

        for (i = 0; i < this.parachuteStrings.length; i++)
        {
            this.parachuteStrings[i].render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        }

        singleLid.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        singleLatch.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        singleBottom.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);

//        Minecraft.getInstance().textureManager.bindTexture(RenderParaChest.PARACHEST_TEXTURE);
    }
}
