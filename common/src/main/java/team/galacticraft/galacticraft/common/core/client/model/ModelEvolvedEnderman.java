package team.galacticraft.galacticraft.common.core.client.model;

import com.google.common.collect.ImmutableList;
import team.galacticraft.galacticraft.core.entities.EntityEvolvedEnderman;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@Environment(EnvType.CLIENT)
public class ModelEvolvedEnderman extends HumanoidModel<EntityEvolvedEnderman>
{
    public boolean isCarrying;
    public boolean isAttacking;
    ModelPart oxygenMask;
    ModelPart tank1;
    ModelPart tank2;
    ModelPart tube1;
    ModelPart tube2;
    ModelPart tube3;
    ModelPart tube4;
    ModelPart tube5;
    ModelPart tube6;
    ModelPart tube7;
    ModelPart tube8;
    ModelPart tube9;
    ModelPart tube10;
    ModelPart tube11;
    ModelPart tube12;
    ModelPart tube13;
    ModelPart tube14;

    public ModelEvolvedEnderman()
    {
        super(0.0F/*, -14.0F, 64, 64*/);
        float f1 = -14.0F;
        this.hat = new ModelPart(this, 0, 16);
        this.hat.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F - 0.5F);
        this.hat.setPos(0.0F, 0.0F + f1, 0.0F);
        this.body = new ModelPart(this, 32, 16);
        this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
        this.body.setPos(0.0F, 0.0F + f1, 0.0F);
        this.rightArm = new ModelPart(this, 56, 0);
        this.rightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 30, 2, 0.0F);
        this.rightArm.setPos(-3.0F, 2.0F + f1, 0.0F);
        this.leftArm = new ModelPart(this, 56, 0);
        this.leftArm.mirror = true;
        this.leftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 30, 2, 0.0F);
        this.leftArm.setPos(5.0F, 2.0F + f1, 0.0F);
        this.rightLeg = new ModelPart(this, 56, 0);
        this.rightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 30, 2, 0.0F);
        this.rightLeg.setPos(-2.0F, 12.0F + f1, 0.0F);
        this.leftLeg = new ModelPart(this, 56, 0);
        this.leftLeg.mirror = true;
        this.leftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 30, 2, 0.0F);
        this.leftLeg.setPos(2.0F, 12.0F + f1, 0.0F);

        this.oxygenMask = new ModelPart(this, 0, 44);
        this.oxygenMask.addBox(-5F, -9F, -5F, 10, 10, 10);
        this.oxygenMask.setPos(0F, -14F, 0F);

        this.tank1 = new ModelPart(this, 52, 54);
        this.tank1.addBox(0F, 0F, 0F, 3, 7, 3);
        this.tank1.setPos(0F, -11F, 2F);

        this.tank2 = new ModelPart(this, 52, 54);
        this.tank2.addBox(0F, 0F, 0F, 3, 7, 3);
        this.tank2.setPos(-3F, -11F, 2F);

        this.tube1 = new ModelPart(this, 44, 62);
        this.tube1.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube1.setPos(-2F, -10.5F, 5F);

        this.tube2 = new ModelPart(this, 44, 62);
        this.tube2.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube2.setPos(-2F, -11F, 6F);

        this.tube3 = new ModelPart(this, 44, 62);
        this.tube3.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube3.setPos(-2F, -12F, 7F);

        this.tube4 = new ModelPart(this, 44, 62);
        this.tube4.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube4.setPos(-2F, -13F, 7F);

        this.tube5 = new ModelPart(this, 44, 62);
        this.tube5.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube5.setPos(-2F, -14F, 7F);

        this.tube6 = new ModelPart(this, 44, 62);
        this.tube6.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube6.setPos(-2F, -15F, 6F);

        this.tube7 = new ModelPart(this, 44, 62);
        this.tube7.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube7.setPos(-2F, -16F, 5F);

        this.tube8 = new ModelPart(this, 44, 62);
        this.tube8.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube8.setPos(1F, -10.5F, 5F);

        this.tube9 = new ModelPart(this, 44, 62);
        this.tube9.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube9.setPos(1F, -11F, 6F);

        this.tube10 = new ModelPart(this, 44, 62);
        this.tube10.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube10.setPos(1F, -12F, 7F);

        this.tube11 = new ModelPart(this, 44, 62);
        this.tube11.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube11.setPos(1F, -13F, 7F);

        this.tube12 = new ModelPart(this, 44, 62);
        this.tube12.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube12.setPos(1F, -14F, 7F);

        this.tube13 = new ModelPart(this, 44, 62);
        this.tube13.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube13.setPos(1F, -15F, 6F);

        this.tube14 = new ModelPart(this, 44, 62);
        this.tube14.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube14.setPos(1F, -16F, 5F);
    }

    @Override
    public void setRotationAngles(EntityEvolvedEnderman entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.head.visible = true;
        float f6 = -14.0F;
        this.body.xRot = 0.0F;
        this.body.y = f6;
        this.body.z = -0.0F;
        this.rightLeg.xRot -= 0.0F;
        this.leftLeg.xRot -= 0.0F;
        this.rightArm.xRot = (float) (this.rightArm.xRot * 0.5D);
        this.leftArm.xRot = (float) (this.leftArm.xRot * 0.5D);
        this.rightLeg.xRot = (float) (this.rightLeg.xRot * 0.5D);
        this.leftLeg.xRot = (float) (this.leftLeg.xRot * 0.5D);
        float f7 = 0.4F;

        if (this.rightArm.xRot > f7)
        {
            this.rightArm.xRot = f7;
        }
        if (this.leftArm.xRot > f7)
        {
            this.leftArm.xRot = f7;
        }
        if (this.rightArm.xRot < -f7)
        {
            this.rightArm.xRot = -f7;
        }
        if (this.leftArm.xRot < -f7)
        {
            this.leftArm.xRot = -f7;
        }
        if (this.rightLeg.xRot > f7)
        {
            this.rightLeg.xRot = f7;
        }
        if (this.leftLeg.xRot > f7)
        {
            this.leftLeg.xRot = f7;
        }
        if (this.rightLeg.xRot < -f7)
        {
            this.rightLeg.xRot = -f7;
        }
        if (this.leftLeg.xRot < -f7)
        {
            this.leftLeg.xRot = -f7;
        }

        if (this.isCarrying)
        {
            this.rightArm.xRot = -0.5F;
            this.leftArm.xRot = -0.5F;
            this.rightArm.zRot = 0.05F;
            this.leftArm.zRot = -0.05F;
        }

        this.rightArm.z = 0.0F;
        this.leftArm.z = 0.0F;
        this.rightLeg.z = 0.0F;
        this.leftLeg.z = 0.0F;
        this.rightLeg.y = 9.0F + f6;
        this.leftLeg.y = 9.0F + f6;
        this.head.z = -0.0F;
        this.head.y = f6 + 1.0F;
        this.hat.x = this.head.x;
        this.hat.y = this.head.y;
        this.hat.z = this.head.z;
        this.hat.xRot = this.head.xRot;
        this.hat.yRot = this.head.yRot;
        this.hat.zRot = this.head.zRot;

        this.oxygenMask.x = this.head.x;
        this.oxygenMask.y = this.head.y;
        this.oxygenMask.z = this.head.z;
        this.oxygenMask.xRot = this.head.xRot;
        this.oxygenMask.yRot = this.head.yRot;
        this.oxygenMask.zRot = this.head.zRot;

        if (this.isAttacking)
        {
            float f8 = 1.0F;
            this.head.y -= f8 * 5.0F;
            this.oxygenMask.y -= f8 * 5.0F;
        }
    }

    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.head, oxygenMask);
    }

    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body, this.rightArm, this.leftArm, this.rightLeg, this.leftLeg, this.hat, oxygenMask, tank1, tank2, tube1, tube2, tube3, tube4, tube5, tube6, tube7, tube8, tube9, tube10, tube11, tube12, tube13, tube14);
    }
}