package micdoodle8.mods.galacticraft.planets.mars.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import micdoodle8.mods.galacticraft.planets.mars.entities.Tier2RocketEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;

public class Tier2RocketModel extends EntityModel<Tier2RocketEntity>
{
    // Roof, Floor, Bottom
    ModelPart[] inside = new ModelPart[3];
    // 4 sets of 5 fin parts
    ModelPart[][] fins = new ModelPart[4][5];
    // 8 parts to the top of the rocket, last is the long center piece
    ModelPart[] top = new ModelPart[8];
    // 4 sets of 3 booster parts (attachment, main, top)
    ModelPart[][] boosters = new ModelPart[4][3];
    // 3 booster base parts, starting from bottom
    ModelPart[] base = new ModelPart[3];
    // 7 LogicalSide pars, first four are the front, then right, and so on
    ModelPart[] sides = new ModelPart[7];

    public Tier2RocketModel()
    {
        this(0.0F);
    }

    public Tier2RocketModel(float scale)
    {
        this.texWidth = 256;
        this.texHeight = 256;

        float halfPI = (float) (Math.PI / 2.0);
        float fullPI = (float) Math.PI;

        this.inside[0] = new ModelPart(this, 0, 59);
        this.inside[0].addBox(-9F, -57F, -9F, 18, 1, 18, scale);
        this.inside[0].setPos(0F, 23F, 0F);
        this.inside[0].setTexSize(256, 256);
        this.inside[0].mirror = true;
        this.setStartingAngles(this.inside[0], 0F, 0F, 0F);
        this.inside[1] = new ModelPart(this, 0, 78);
        this.inside[1].addBox(-8.5F, -16F, -8.5F, 17, 1, 17, scale);
        this.inside[1].setPos(0F, 23F, 0F);
        this.inside[1].setTexSize(256, 256);
        this.inside[1].mirror = true;
        this.setStartingAngles(this.inside[1], 0F, 0F, 0F);
        this.inside[2] = new ModelPart(this, 0, 40);
        this.inside[2].addBox(-9F, -4F, -9F, 18, 1, 18, scale);
        this.inside[2].setPos(0F, 23F, 0F);
        this.inside[2].setTexSize(256, 256);
        this.inside[2].mirror = true;
        this.setStartingAngles(this.inside[2], 0F, 0F, 0F);

        this.fins[0][1] = new ModelPart(this, 66, 0);
        this.fins[0][1].addBox(-1F, -9F, -19.4F, 2, 8, 2, scale);
        this.fins[0][1].setPos(0F, 24F, 0F);
        this.fins[0][1].setTexSize(256, 256);
        this.fins[0][1].mirror = true;
        this.setStartingAngles(this.fins[0][1], 0F, 0.7853982F, 0F);
        this.fins[0][2] = new ModelPart(this, 66, 0);
        this.fins[0][2].addBox(-1F, -12F, -17.4F, 2, 8, 2, scale);
        this.fins[0][2].setPos(0F, 24F, 0F);
        this.fins[0][2].setTexSize(256, 256);
        this.fins[0][2].mirror = true;
        this.setStartingAngles(this.fins[0][2], 0F, 0.7853982F, 0F);
        this.fins[0][3] = new ModelPart(this, 66, 0);
        this.fins[0][3].addBox(-1F, -14F, -15.4F, 2, 8, 2, scale);
        this.fins[0][3].setPos(0F, 24F, 0F);
        this.fins[0][3].setTexSize(256, 256);
        this.fins[0][3].mirror = true;
        this.setStartingAngles(this.fins[0][3], 0F, 0.7853982F, 0F);
        this.fins[0][4] = new ModelPart(this, 66, 0);
        this.fins[0][4].addBox(-1F, -15F, -13.5F, 2, 8, 2, scale);
        this.fins[0][4].setPos(0F, 24F, 0F);
        this.fins[0][4].setTexSize(256, 256);
        this.fins[0][4].mirror = true;
        this.setStartingAngles(this.fins[0][4], 0F, 0.7853982F, 0F);

        this.fins[0][0] = new ModelPart(this, 60, 0);
        this.fins[0][0].addBox(-1F, -14F, -20.4F, 2, 15, 1, scale);
        this.fins[0][0].setPos(0F, 24F, 0F);
        this.fins[0][0].setTexSize(256, 256);
        this.fins[0][0].mirror = true;
        this.setStartingAngles(this.fins[0][0], 0F, 0.7853982F, 0F);

        this.fins[1][0] = new ModelPart(this, 74, 0);
        this.fins[1][0].addBox(-20.4F, -14F, -1F, 1, 15, 2, scale);
        this.fins[1][0].setPos(0F, 24F, 0F);
        this.fins[1][0].setTexSize(256, 256);
        this.fins[1][0].mirror = true;
        this.setStartingAngles(this.fins[1][0], 0F, 0.7853982F, 0F);
        this.fins[1][1] = new ModelPart(this, 66, 0);
        this.fins[1][1].addBox(-19.4F, -9F, -1F, 2, 8, 2, scale);
        this.fins[1][1].setPos(0F, 24F, 0F);
        this.fins[1][1].setTexSize(256, 256);
        this.fins[1][1].mirror = true;
        this.setStartingAngles(this.fins[1][1], 0F, 0.7853982F, 0F);
        this.fins[1][2] = new ModelPart(this, 66, 0);
        this.fins[1][2].addBox(-17.4F, -12F, -1F, 2, 8, 2, scale);
        this.fins[1][2].setPos(0F, 24F, 0F);
        this.fins[1][2].setTexSize(256, 256);
        this.fins[1][2].mirror = true;
        this.setStartingAngles(this.fins[1][2], 0F, 0.7853982F, 0F);
        this.fins[1][3] = new ModelPart(this, 66, 0);
        this.fins[1][3].addBox(-15.4F, -14F, -1F, 2, 8, 2, scale);
        this.fins[1][3].setPos(0F, 24F, 0F);
        this.fins[1][3].setTexSize(256, 256);
        this.fins[1][3].mirror = true;
        this.setStartingAngles(this.fins[1][3], 0F, 0.7853982F, 0F);
        this.fins[1][4] = new ModelPart(this, 66, 0);
        this.fins[1][4].addBox(-13.5F, -15F, -1F, 2, 8, 2, scale);
        this.fins[1][4].setPos(0F, 24F, 0F);
        this.fins[1][4].setTexSize(256, 256);
        this.fins[1][4].mirror = true;
        this.setStartingAngles(this.fins[1][4], 0F, 0.7853982F, 0F);

        this.fins[2][0] = new ModelPart(this, 60, 0);
        this.fins[2][0].addBox(-1F, -14F, 19.5F, 2, 15, 1, scale);
        this.fins[2][0].setPos(0F, 24F, 0F);
        this.fins[2][0].setTexSize(256, 256);
        this.fins[2][0].mirror = true;
        this.setStartingAngles(this.fins[2][0], 0F, 0.7853982F, 0F);
        this.fins[2][1] = new ModelPart(this, 66, 0);
        this.fins[2][1].addBox(-1F, -9F, 17.5F, 2, 8, 2, scale);
        this.fins[2][1].setPos(0F, 24F, 0F);
        this.fins[2][1].setTexSize(256, 256);
        this.fins[2][1].mirror = true;
        this.setStartingAngles(this.fins[2][1], 0F, 0.7853982F, 0F);
        this.fins[2][2] = new ModelPart(this, 66, 0);
        this.fins[2][2].addBox(-1F, -12F, 15.5F, 2, 8, 2, scale);
        this.fins[2][2].setPos(0F, 24F, 0F);
        this.fins[2][2].setTexSize(256, 256);
        this.fins[2][2].mirror = true;
        this.setStartingAngles(this.fins[2][2], 0F, 0.7853982F, 0F);
        this.fins[2][3] = new ModelPart(this, 66, 0);
        this.fins[2][3].addBox(-1F, -14F, 13.5F, 2, 8, 2, scale);
        this.fins[2][3].setPos(0F, 24F, 0F);
        this.fins[2][3].setTexSize(256, 256);
        this.fins[2][3].mirror = true;
        this.setStartingAngles(this.fins[2][3], 0F, 0.7853982F, 0F);
        this.fins[2][4] = new ModelPart(this, 66, 0);
        this.fins[2][4].addBox(-1F, -15F, 11.6F, 2, 8, 2, scale);
        this.fins[2][4].setPos(0F, 24F, 0F);
        this.fins[2][4].setTexSize(256, 256);
        this.fins[2][4].mirror = true;
        this.setStartingAngles(this.fins[2][4], 0F, 0.7853982F, 0F);

        this.fins[3][0] = new ModelPart(this, 74, 0);
        this.fins[3][0].addBox(19.5F, -14F, -1F, 1, 15, 2, scale);
        this.fins[3][0].setPos(0F, 24F, 0F);
        this.fins[3][0].setTexSize(256, 256);
        this.fins[3][0].mirror = true;
        this.setStartingAngles(this.fins[3][0], 0F, 0.7853982F, 0F);
        this.fins[3][1] = new ModelPart(this, 66, 0);
        this.fins[3][1].addBox(17.5F, -9F, -1F, 2, 8, 2, scale);
        this.fins[3][1].setPos(0F, 24F, 0F);
        this.fins[3][1].setTexSize(256, 256);
        this.fins[3][1].mirror = true;
        this.setStartingAngles(this.fins[3][1], 0F, 0.7853982F, 0F);
        this.fins[3][2] = new ModelPart(this, 66, 0);
        this.fins[3][2].addBox(15.5F, -12F, -1F, 2, 8, 2, scale);
        this.fins[3][2].setPos(0F, 24F, 0F);
        this.fins[3][2].setTexSize(256, 256);
        this.fins[3][2].mirror = true;
        this.setStartingAngles(this.fins[3][2], 0F, 0.7853982F, 0F);
        this.fins[3][3] = new ModelPart(this, 66, 0);
        this.fins[3][3].addBox(13.5F, -14F, -1F, 2, 8, 2, scale);
        this.fins[3][3].setPos(0F, 24F, 0F);
        this.fins[3][3].setTexSize(256, 256);
        this.fins[3][3].mirror = true;
        this.setStartingAngles(this.fins[3][3], 0F, 0.7853982F, 0F);
        this.fins[3][4] = new ModelPart(this, 66, 0);
        this.fins[3][4].addBox(11.6F, -15F, -1F, 2, 8, 2, scale);
        this.fins[3][4].setPos(0F, 24F, 0F);
        this.fins[3][4].setTexSize(256, 256);
        this.fins[3][4].mirror = true;
        this.setStartingAngles(this.fins[3][4], 0F, 0.7853982F, 0F);

        this.top[0] = new ModelPart(this, 192, 60);
        this.top[0].addBox(-8F, -60F, -8F, 16, 2, 16, scale);
        this.top[0].setPos(0F, 24F, 0F);
        this.top[0].setTexSize(256, 256);
        this.top[0].mirror = true;
        this.setStartingAngles(this.top[0], 0F, 0F, 0F);
        this.top[1] = new ModelPart(this, 200, 78);
        this.top[1].addBox(-7F, -62F, -7F, 14, 2, 14, scale);
        this.top[1].setPos(0F, 24F, 0F);
        this.top[1].setTexSize(256, 256);
        this.top[1].mirror = true;
        this.setStartingAngles(this.top[1], 0F, 0F, 0F);
        this.top[2] = new ModelPart(this, 208, 94);
        this.top[2].addBox(-6F, -64F, -6F, 12, 2, 12, scale);
        this.top[2].setPos(0F, 24F, 0F);
        this.top[2].setTexSize(256, 256);
        this.top[2].mirror = true;
        this.setStartingAngles(this.top[2], 0F, 0F, 0F);
        this.top[3] = new ModelPart(this, 216, 108);
        this.top[3].addBox(-5F, -66F, -5F, 10, 2, 10, scale);
        this.top[3].setPos(0F, 24F, 0F);
        this.top[3].setTexSize(256, 256);
        this.top[3].mirror = true;
        this.setStartingAngles(this.top[3], 0F, 0F, 0F);
        this.top[4] = new ModelPart(this, 224, 120);
        this.top[4].addBox(-4F, -68F, -4F, 8, 2, 8, scale);
        this.top[4].setPos(0F, 24F, 0F);
        this.top[4].setTexSize(256, 256);
        this.top[4].mirror = true;
        this.setStartingAngles(this.top[4], 0F, 0F, 0F);
        this.top[5] = new ModelPart(this, 232, 130);
        this.top[5].addBox(-3F, -70F, -3F, 6, 2, 6, scale);
        this.top[5].setPos(0F, 24F, 0F);
        this.top[5].setTexSize(256, 256);
        this.top[5].mirror = true;
        this.setStartingAngles(this.top[5], 0F, 0F, 0F);
        this.top[6] = new ModelPart(this, 240, 138);
        this.top[6].addBox(-2F, -72F, -2F, 4, 2, 4, scale);
        this.top[6].setPos(0F, 24F, 0F);
        this.top[6].setTexSize(256, 256);
        this.top[6].mirror = true;
        this.setStartingAngles(this.top[6], 0F, 0F, 0F);
        this.top[7] = new ModelPart(this, 248, 144);
        this.top[7].addBox(-1F, -88F, -1F, 2, 18, 2, scale);
        this.top[7].setPos(0F, 24F, 0F);
        this.top[7].setTexSize(256, 256);
        this.top[7].mirror = true;
        this.setStartingAngles(this.top[7], 0F, 0F, 0F);

        this.base[0] = new ModelPart(this, 0, 0);
        this.base[0].addBox(-7F, -1F, -7F, 14, 1, 14, scale);
        this.base[0].setPos(0F, 24F, 0F);
        this.base[0].setTexSize(256, 256);
        this.base[0].mirror = true;
        this.setStartingAngles(this.base[0], 0F, 0F, 0F);
        this.base[1] = new ModelPart(this, 0, 15);
        this.base[1].addBox(-6F, -2F, -6F, 12, 1, 12, scale);
        this.base[1].setPos(0F, 24F, 0F);
        this.base[1].setTexSize(256, 256);
        this.base[1].mirror = true;
        this.setStartingAngles(this.base[1], 0F, 0F, 0F);
        this.base[2] = new ModelPart(this, 0, 28);
        this.base[2].addBox(-5F, -4F, -5F, 10, 2, 10, scale);
        this.base[2].setPos(0F, 24F, 0F);
        this.base[2].setTexSize(256, 256);
        this.base[2].mirror = true;
        this.setStartingAngles(this.base[2], 0F, 0F, 0F);

        this.sides[0] = new ModelPart(this, 85, 0);
        this.sides[0].addBox(-3.9F, -58F, -8.9F, 8, 17, 1, scale);
        this.sides[0].setPos(0F, 24F, 0F);
        this.sides[0].setTexSize(256, 256);
        this.sides[0].mirror = true;
        this.setStartingAngles(this.sides[0], 0F, 0F, 0F);
        this.sides[1] = new ModelPart(this, 103, 0);
        this.sides[1].addBox(3.9F, -58F, -8.9F, 5, 54, 1, scale);
        this.sides[1].setPos(0F, 24F, 0F);
        this.sides[1].setTexSize(256, 256);
        this.sides[1].mirror = true;
        this.setStartingAngles(this.sides[1], 0F, 0F, 0F);
        this.sides[2] = new ModelPart(this, 85, 18);
        this.sides[2].addBox(-3.9F, -34F, -8.9F, 8, 30, 1, scale);
        this.sides[2].setPos(0F, 24F, 0F);
        this.sides[2].setTexSize(256, 256);
        this.sides[2].mirror = true;
        this.setStartingAngles(this.sides[2], 0F, 0F, 0F);
        this.sides[3] = new ModelPart(this, 103, 55);
        this.sides[3].addBox(-8.9F, -58F, -8.9F, 5, 54, 1, scale);
        this.sides[3].setPos(0F, 24F, 0F);
        this.sides[3].setTexSize(256, 256);
        this.sides[3].mirror = true;
        this.setStartingAngles(this.sides[3], 0F, 0F, 0F);

        this.sides[4] = new ModelPart(this, 120, 0);
        this.sides[4].addBox(-8.9F, -58F, -7.9F, 1, 54, 16, scale);
        this.sides[4].setPos(0F, 24F, 0F);
        this.sides[4].setTexSize(256, 256);
        this.sides[4].mirror = true;
        this.setStartingAngles(this.sides[4], 0F, 0F, 0F);
        this.sides[5] = new ModelPart(this, 120, 141);
        this.sides[5].addBox(-8.9F, -58F, 8.1F, 17, 54, 1, scale);
        this.sides[5].setPos(0F, 24F, 0F);
        this.sides[5].setTexSize(256, 256);
        this.sides[5].mirror = true;
        this.setStartingAngles(this.sides[5], 0F, 0F, 0F);
        this.sides[6] = new ModelPart(this, 119, 70);
        this.sides[6].addBox(8.1F, -58F, -7.9F, 1, 54, 17, scale);
        this.sides[6].setPos(0F, 24F, 0F);
        this.sides[6].setTexSize(256, 256);
        this.sides[6].mirror = true;
        this.setStartingAngles(this.sides[6], 0F, 0F, 0F);

        this.boosters[0][0] = new ModelPart(this, 154, 19);
        this.boosters[0][0].addBox(-10.9F, -10F, -0.5F, 3, 5, 1, scale);
        this.boosters[0][0].setPos(0F, 24F, 0F);
        this.boosters[0][0].setTexSize(256, 256);
        this.boosters[0][0].mirror = true;
        this.setStartingAngles(this.boosters[0][0], 0F, -halfPI, 0F);
        this.boosters[0][1] = new ModelPart(this, 154, 6);
        this.boosters[0][1].addBox(-14.9F, -11F, -2.5F, 5, 8, 5, scale);
        this.boosters[0][1].setPos(0F, 24F, 0F);
        this.boosters[0][1].setTexSize(256, 256);
        this.boosters[0][1].mirror = true;
        this.setStartingAngles(this.boosters[0][1], 0F, -halfPI, 0F);
        this.boosters[0][2] = new ModelPart(this, 154, 0);
        this.boosters[0][2].addBox(-14.4F, -13F, -2F, 4, 2, 4, scale);
        this.boosters[0][2].setPos(0F, 24F, 0F);
        this.boosters[0][2].setTexSize(256, 256);
        this.boosters[0][2].mirror = true;
        this.setStartingAngles(this.boosters[0][2], 0F, -halfPI, 0F);

        this.boosters[1][0] = new ModelPart(this, 154, 19);
        this.boosters[1][0].addBox(-10.9F, -10F, -0.5F, 3, 5, 1, scale);
        this.boosters[1][0].setPos(0F, 24F, 0F);
        this.boosters[1][0].setTexSize(256, 256);
        this.boosters[1][0].mirror = true;
        this.setStartingAngles(this.boosters[1][0], 0F, 0F, 0F);
        this.boosters[1][1] = new ModelPart(this, 154, 6);
        this.boosters[1][1].addBox(-14.9F, -11F, -2.5F, 5, 8, 5, scale);
        this.boosters[1][1].setPos(0F, 24F, 0F);
        this.boosters[1][1].setTexSize(256, 256);
        this.boosters[1][1].mirror = true;
        this.setStartingAngles(this.boosters[1][1], 0F, 0F, 0F);
        this.boosters[1][2] = new ModelPart(this, 154, 0);
        this.boosters[1][2].addBox(-14.4F, -13F, -2F, 4, 2, 4, scale);
        this.boosters[1][2].setPos(0F, 24F, 0F);
        this.boosters[1][2].setTexSize(256, 256);
        this.boosters[1][2].mirror = true;
        this.setStartingAngles(this.boosters[1][2], 0F, 0F, 0F);

        this.boosters[2][0] = new ModelPart(this, 154, 19);
        this.boosters[2][0].addBox(-10.9F, -10F, -0.5F, 3, 5, 1, scale);
        this.boosters[2][0].setPos(0F, 24F, 0F);
        this.boosters[2][0].setTexSize(256, 256);
        this.boosters[2][0].mirror = true;
        this.setStartingAngles(this.boosters[2][0], 0F, halfPI, 0F);
        this.boosters[2][1] = new ModelPart(this, 154, 6);
        this.boosters[2][1].addBox(-14.9F, -11F, -2.5F, 5, 8, 5, scale);
        this.boosters[2][1].setPos(0F, 24F, 0F);
        this.boosters[2][1].setTexSize(256, 256);
        this.boosters[2][1].mirror = true;
        this.setStartingAngles(this.boosters[2][1], 0F, halfPI, 0F);
        this.boosters[2][2] = new ModelPart(this, 154, 0);
        this.boosters[2][2].addBox(-14.4F, -13F, -2F, 4, 2, 4, scale);
        this.boosters[2][2].setPos(0F, 24F, 0F);
        this.boosters[2][2].setTexSize(256, 256);
        this.boosters[2][2].mirror = true;
        this.setStartingAngles(this.boosters[2][2], 0F, halfPI, 0F);

        this.boosters[3][0] = new ModelPart(this, 154, 19);
        this.boosters[3][0].addBox(-10.9F, -10F, -0.5F, 3, 5, 1, scale);
        this.boosters[3][0].setPos(0F, 24F, 0F);
        this.boosters[3][0].setTexSize(256, 256);
        this.boosters[3][0].mirror = true;
        this.setStartingAngles(this.boosters[3][0], 0F, fullPI, 0F);
        this.boosters[3][1] = new ModelPart(this, 154, 6);
        this.boosters[3][1].addBox(-14.9F, -11F, -2.5F, 5, 8, 5, scale);
        this.boosters[3][1].setPos(0F, 24F, 0F);
        this.boosters[3][1].setTexSize(256, 256);
        this.boosters[3][1].mirror = true;
        this.setStartingAngles(this.boosters[3][1], 0F, fullPI, 0F);
        this.boosters[3][2] = new ModelPart(this, 154, 0);
        this.boosters[3][2].addBox(-14.4F, -13F, -2F, 4, 2, 4, scale);
        this.boosters[3][2].setPos(0F, 24F, 0F);
        this.boosters[3][2].setTexSize(256, 256);
        this.boosters[3][2].mirror = true;
        this.setStartingAngles(this.boosters[3][2], 0F, fullPI, 0F);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        for (ModelPart model : this.inside)
        {
            model.render(matrixStack, buffer, packedLight, packedOverlay);
        }

        for (ModelPart model : this.top)
        {
            model.render(matrixStack, buffer, packedLight, packedOverlay);
        }

        for (ModelPart model : this.base)
        {
            model.render(matrixStack, buffer, packedLight, packedOverlay);
        }

        for (ModelPart model : this.sides)
        {
            model.render(matrixStack, buffer, packedLight, packedOverlay);
        }

        int var1 = 0;
        int var2 = 0;

        for (var1 = 0; var1 < this.fins.length; var1++)
        {
            for (var2 = 0; var2 < this.fins[var1].length; var2++)
            {
                this.fins[var1][var2].render(matrixStack, buffer, packedLight, packedOverlay);
            }
        }

        for (var1 = 0; var1 < this.boosters.length; var1++)
        {
            for (var2 = 0; var2 < this.boosters[var1].length; var2++)
            {
                this.boosters[var1][var2].render(matrixStack, buffer, packedLight, packedOverlay);
            }
        }
    }

    private void setStartingAngles(ModelPart model, float x, float y, float z)
    {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }

    @Override
    public void setRotationAngles(Tier2RocketEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
    }
}