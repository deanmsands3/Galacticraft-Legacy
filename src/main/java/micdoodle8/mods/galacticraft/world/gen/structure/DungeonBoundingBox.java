package micdoodle8.mods.galacticraft.world.gen.structure;

import net.minecraft.util.math.Vec3i;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class DungeonBoundingBox extends StructureBoundingBox {
	public final int minX, minY, minZ, maxX, maxY, maxZ;

	public DungeonBoundingBox(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax) {
        this.minX = xMin;
        this.minY = yMin;
        this.minZ = zMin;
        this.maxX = xMax;
        this.maxY = yMax;
        this.maxZ = zMax;
	}
	
    /**
     * Discover if a coordinate is inside the bounding box area.
     */
    @Override
	public boolean intersectsWith(int minXIn, int minZIn, int maxXIn, int maxZIn)
    {
        return this.maxX >= minXIn && this.minX <= maxXIn && this.maxZ >= minZIn && this.minZ <= maxZIn;
    }
	
    /**
     * Checks if given Vec3i is inside of StructureBoundingBox
     */
    @Override
	public boolean isVecInside(Vec3i vec)
    {
        return vec.getX() >= this.minX && vec.getX() <= this.maxX && vec.getZ() >= this.minZ && vec.getZ() <= this.maxZ && vec.getY() >= this.minY && vec.getY() <= this.maxY;
    }

    @Override
	public Vec3i getLength()
    {
        return new Vec3i(this.maxX - this.minX, this.maxY - this.minY, this.maxZ - this.minZ);
    }
	
    /**
     * Get dimension of the bounding box in the x direction.
     */
    @Override
	public int getXSize()
    {
        return this.maxX - this.minX + 1;
    }

    /**
     * Get dimension of the bounding box in the y direction.
     */
    @Override
	public int getYSize()
    {
        return this.maxY - this.minY + 1;
    }

    /**
     * Get dimension of the bounding box in the z direction.
     */
    @Override
	public int getZSize()
    {
        return this.maxZ - this.minZ + 1;
    }
}
