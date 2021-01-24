package micdoodle8.mods.galacticraft.planets.mars.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.planets.mars.entities.LandingBalloonsEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;

public class ParachuteBalloonModel extends EntityModel<LandingBalloonsEntity>
{
    private final ModelPart[] parachute = new ModelPart[3];
    private final ModelPart[] parachuteStrings = new ModelPart[4];

    public ParachuteBalloonModel()
    {
        this(0.0F);
    }

    public ParachuteBalloonModel(float scale)
    {
        super();

        this.parachute[0] = new ModelPart(this, 0, 0).setTexSize(512, 256);
        this.parachute[0].addBox(-20.0F, -45.0F, -20.0F, 10, 2, 40, scale);
        this.parachute[0].setPos(15.0F, 4.0F, 0.0F);
        this.parachute[1] = new ModelPart(this, 0, 42).setTexSize(512, 256);
        this.parachute[1].addBox(-20.0F, -45.0F, -20.0F, 40, 2, 40, scale);
        this.parachute[1].setPos(0.0F, 0.0F, 0.0F);
        this.parachute[2] = new ModelPart(this, 0, 0).setTexSize(512, 256);
        this.parachute[2].addBox(-20.0F, -45.0F, -20.0F, 10, 2, 40, scale);
        this.parachute[2].setPos(11F, -11, 0.0F);

        this.parachuteStrings[0] = new ModelPart(this, 100, 0).setTexSize(512, 256);
        this.parachuteStrings[0].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, scale);
        this.parachuteStrings[0].setPos(0.0F, 0.0F, 0.0F);
        this.parachuteStrings[1] = new ModelPart(this, 100, 0).setTexSize(512, 256);
        this.parachuteStrings[1].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, scale);
        this.parachuteStrings[1].setPos(0.0F, 0.0F, 0.0F);
        this.parachuteStrings[2] = new ModelPart(this, 100, 0).setTexSize(512, 256);
        this.parachuteStrings[2].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, scale);
        this.parachuteStrings[2].setPos(0.0F, 0.0F, 0.0F);
        this.parachuteStrings[3] = new ModelPart(this, 100, 0).setTexSize(512, 256);
        this.parachuteStrings[3].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, scale);
        this.parachuteStrings[3].setPos(0.0F, 0.0F, 0.0F);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        int i;

        for (i = 0; i < this.parachute.length; i++)
        {
            this.parachute[i].render(matrixStack, buffer, packedLight, packedOverlay);
        }

        for (i = 0; i < this.parachuteStrings.length; i++)
        {
            this.parachuteStrings[i].render(matrixStack, buffer, packedLight, packedOverlay);
        }

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

    public void renderParachute()
    {
    }

    @Override
    public void setRotationAngles(LandingBalloonsEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
    }
}