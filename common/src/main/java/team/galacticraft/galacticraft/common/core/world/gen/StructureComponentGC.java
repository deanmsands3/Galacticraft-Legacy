package team.galacticraft.galacticraft.common.core.world.gen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import java.util.Random;

public abstract class StructureComponentGC extends StructurePiece
{
    protected StructureComponentGC(StructurePieceType type, int componentType)
    {
        super(type, componentType);
    }

    public StructureComponentGC(StructurePieceType type, CompoundTag nbt)
    {
        super(type, nbt);
    }

    public static BoundingBox getComponentToAddBoundingBox(int x, int y, int z, int lengthOffset, int heightOffset, int widthOffset, int length, int height, int width, Direction coordBaseMode)
    {
        if (coordBaseMode != null)
        {
            switch (SwitchEnumFacing.field_176064_a[coordBaseMode.ordinal()])
            {
            case 0:
                return new BoundingBox(x + lengthOffset, y + heightOffset, z + widthOffset, x + length + lengthOffset, y + height + heightOffset, z + width + widthOffset);
            case 1:
                return new BoundingBox(x - width + widthOffset, y + heightOffset, z + lengthOffset, x + widthOffset, y + height + heightOffset, z + length + lengthOffset);
            case 2:
                return new BoundingBox(x - length - lengthOffset, y + heightOffset, z - width - widthOffset, x - lengthOffset, y + height + heightOffset, z - widthOffset);
            case 3:
                return new BoundingBox(x + widthOffset, y + heightOffset, z - length, x + width + widthOffset, y + height + heightOffset, z + lengthOffset);
            }
        }
        return new BoundingBox(x + lengthOffset, y + heightOffset, z + widthOffset, x + length + lengthOffset, y + height + heightOffset, z + width + widthOffset);
    }

    protected void placeSpawnerAtCurrentPosition(Level var1, Random var2, int var3, int var4, int var5, EntityType<?> var6, BoundingBox var7)
    {
        final int var8 = this.getWorldX(var3, var5);
        final int var9 = this.getWorldY(var4);
        final int var10 = this.getWorldZ(var3, var5);

        BlockPos pos = new BlockPos(var8, var9, var10);
        if (var7.isInside(pos) && var1.getBlockState(pos).getBlock() != Blocks.SPAWNER)
        {
            var1.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), 2);
            final SpawnerBlockEntity var11 = (SpawnerBlockEntity) var1.getBlockEntity(pos);

            if (var11 != null)
            {
                var11.getSpawner().setEntityId(var6);
            }
        }
    }

    protected int[] offsetTowerCoords(int var1, int var2, int var3, int var4, int var5)
    {
        final int var6 = this.getWorldX(var1, var3);
        final int var7 = this.getWorldY(var2);
        final int var8 = this.getWorldZ(var1, var3);
        return var5 == 0 ? new int[]{var6 + 1, var7 - 1, var8 - var4 / 2} : var5 == 1 ? new int[]{var6 + var4 / 2, var7 - 1, var8 + 1} : var5 == 2 ? new int[]{var6 - 1, var7 - 1, var8 + var4 / 2} : var5 == 3 ? new int[]{var6 - var4 / 2, var7 - 1, var8 - 1} : new int[]{var1, var2, var3};
    }

    public int[] getOffsetAsIfRotated(int[] var1, Direction var2)
    {
        final Direction var3 = getOrientation();
        final int[] var4 = new int[3];
        this.setOrientation(var2);
        var4[0] = this.getWorldX(var1[0], var1[2]);
        var4[1] = this.getWorldY(var1[1]);
        var4[2] = this.getWorldZ(var1[0], var1[2]);
        this.setOrientation(var3);
        return var4;
    }

    @Override
    protected int getWorldX(int var1, int var2)
    {
        switch (getOrientation().get2DDataValue())
        {
        case 0:
            return this.boundingBox.x0 + var1;

        case 1:
            return this.boundingBox.x1 - var2;

        case 2:
            return this.boundingBox.x1 - var1;

        case 3:
            return this.boundingBox.x0 + var2;

        default:
            return var1;
        }
    }

    @Override
    protected int getWorldZ(int var1, int var2)
    {
        switch (getOrientation().get2DDataValue())
        {
        case 0:
            return this.boundingBox.z0 + var2;

        case 1:
            return this.boundingBox.z0 + var1;

        case 2:
            return this.boundingBox.z1 - var2;

        case 3:
            return this.boundingBox.z1 - var1;

        default:
            return var2;
        }
    }

    @Override
    protected int getWorldY(int var1)
    {
        return super.getWorldY(var1);
    }

    protected static class SwitchEnumFacing
    {
        protected static int[] field_176064_a = new int[Direction.values().length];

        static
        {
            try
            {
                field_176064_a[Direction.NORTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError var4)
            {
            }

            try
            {
                field_176064_a[Direction.SOUTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError var3)
            {
            }

            try
            {
                field_176064_a[Direction.WEST.ordinal()] = 3;
            }
            catch (NoSuchFieldError var2)
            {
            }

            try
            {
                field_176064_a[Direction.EAST.ordinal()] = 4;
            }
            catch (NoSuchFieldError var1)
            {
            }
        }
    }
}
