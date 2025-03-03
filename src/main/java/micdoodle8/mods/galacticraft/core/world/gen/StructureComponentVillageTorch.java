package micdoodle8.mods.galacticraft.core.world.gen;

import java.util.List;
import java.util.Random;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockGlowstoneTorch;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class StructureComponentVillageTorch extends StructureComponentVillage
{

    private int averageGroundLevel = -1;

    public StructureComponentVillageTorch()
    {
    }

    public StructureComponentVillageTorch(StructureComponentVillageStartPiece par1ComponentVillageStartPiece, int par2, Random par3Random, StructureBoundingBox par4StructureBoundingBox, EnumFacing par5)
    {
        super(par1ComponentVillageStartPiece, par2);
        this.setCoordBaseMode(par5);
        this.boundingBox = par4StructureBoundingBox;
    }

    @Override
    protected void writeStructureToNBT(NBTTagCompound nbt)
    {
        super.writeStructureToNBT(nbt);

        nbt.setInteger("AvgGroundLevel", this.averageGroundLevel);
    }

    @Override
    protected void readStructureFromNBT(NBTTagCompound nbt, TemplateManager manager)
    {
        super.readStructureFromNBT(nbt, manager);

        this.averageGroundLevel = nbt.getInteger("AvgGroundLevel");
    }

    public static StructureBoundingBox func_74904_a(StructureComponentVillageStartPiece par0ComponentVillageStartPiece, List<StructureComponent> par1List, Random par2Random, int par3, int par4, int par5, EnumFacing par6)
    {
        final StructureBoundingBox var7 = StructureBoundingBox.getComponentToAddBoundingBox(par3, par4, par5, 0, 0, 0, 3, 4, 2, par6);
        return StructureComponent.findIntersecting(par1List, var7) != null ? null : var7;
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs,
     * Mob Spawners, it closes Mineshafts at the end, it adds Fences...
     */
    @Override
    public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox)
    {
        if (this.averageGroundLevel < 0)
        {
            this.averageGroundLevel = this.getAverageGroundLevel(par1World, par3StructureBoundingBox);

            if (this.averageGroundLevel < 0)
            {
                return true;
            }

            this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 4 - 1, 0);
        }

        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 0, 0, 2, 3, 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.setBlockState(par1World, Blocks.DARK_OAK_FENCE.getDefaultState(), 1, 0, 0, par3StructureBoundingBox);
        this.setBlockState(par1World, Blocks.DARK_OAK_FENCE.getDefaultState(), 1, 1, 0, par3StructureBoundingBox);
        this.setBlockState(par1World, Blocks.DARK_OAK_FENCE.getDefaultState(), 1, 2, 0, par3StructureBoundingBox);
        this.setBlockState(par1World, Blocks.WOOL.getStateFromMeta(15), 1, 3, 0, par3StructureBoundingBox);
        boolean flag = this.getCoordBaseMode() == EnumFacing.EAST || this.getCoordBaseMode() == EnumFacing.NORTH;
        this.setBlockState(par1World, GCBlocks.glowstoneTorch.getDefaultState().withProperty(BlockGlowstoneTorch.FACING, this.getCoordBaseMode().rotateY()), flag ? 2 : 0, 3, 0,
            par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.glowstoneTorch.getDefaultState().withProperty(BlockGlowstoneTorch.FACING, this.getCoordBaseMode()), 1, 3, 1, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.glowstoneTorch.getDefaultState().withProperty(BlockGlowstoneTorch.FACING, this.getCoordBaseMode().rotateYCCW()), flag ? 0 : 2, 3, 0,
            par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.glowstoneTorch.getDefaultState().withProperty(BlockGlowstoneTorch.FACING, this.getCoordBaseMode().getOpposite()), 1, 3, -1, par3StructureBoundingBox);
        return true;
    }
}
