package team.galacticraft.galacticraft.common.api.vector;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

/**
 * Vector3 Class is used for defining objects in a 3D space.
 *
 * @author Calclavia
 */

public class Vector3D implements Cloneable
{
    public double x;
    public double y;
    public double z;

    public Vector3D(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D()
    {
        this(0, 0, 0);
    }

    public Vector3D(Vector3D vector)
    {
        this(vector.x, vector.y, vector.z);
    }

    public Vector3D(double amount)
    {
        this(amount, amount, amount);
    }

    public Vector3D(Entity par1)
    {
        this(par1.getX(), par1.getY(), par1.getZ());
    }

    public Vector3D(BlockPos pos)
    {
        this(pos.getX(), pos.getY(), pos.getZ());
    }

    public Vector3D(BlockEntity par1)
    {
        this(par1.getBlockPos());
    }

    public Vector3D(Vec3 par1)
    {
        this(par1.x, par1.y, par1.z);
    }

    public Vector3D(HitResult par1)
    {
        this(par1.getLocation());
    }

    public Vector3D(Direction direction)
    {
        this(direction.getStepX(), direction.getStepY(), direction.getStepZ());
    }

    public Vector3D(CompoundTag nbt)
    {
        this(nbt.getDouble("x"), nbt.getDouble("y"), nbt.getDouble("z"));
    }

    public Vector3D(BlockVec3 vec)
    {
        this(vec.x, vec.y, vec.z);
    }

    public BlockPos toBlockPos()
    {
        return new BlockPos(intX(), intY(), intZ());
    }

    public int intX()
    {
        return (int) Math.floor(this.x);
    }

    public int intY()
    {
        return (int) Math.floor(this.y);
    }

    public int intZ()
    {
        return (int) Math.floor(this.z);
    }

    public double getMagnitudeSquared()
    {
        return x * x + y * y + z * z;
    }

    public double getMagnitude()
    {
        return Math.sqrt(this.getMagnitudeSquared());
    }

    public Vector3D translate(Vector3D par1)
    {
        this.x += par1.x;
        this.y += par1.y;
        this.z += par1.z;
        return this;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector3D vector3D = (Vector3D) o;
        return Double.compare(vector3D.x, x) == 0 && Double.compare(vector3D.y, y) == 0 && Double.compare(vector3D.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString()
    {
        return "Vector3D [" + this.x + "," + this.y + "," + this.z + "]";
    }
}