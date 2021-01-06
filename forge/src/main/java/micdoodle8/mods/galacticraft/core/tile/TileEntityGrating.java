package micdoodle8.mods.galacticraft.core.tile;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class TileEntityGrating extends TileEntity
{
    private BlockState substate = Blocks.AIR.getDefaultState();

    public TileEntityGrating(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        super.read(compound);

        CompoundNBT sub = compound.getCompound("substate");

        this.substate = NBTUtil.readBlockState(sub);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        CompoundNBT subTag = NBTUtil.writeBlockState(substate);

        compound.put("substate", subTag);

        return super.write(compound);

    }
}
