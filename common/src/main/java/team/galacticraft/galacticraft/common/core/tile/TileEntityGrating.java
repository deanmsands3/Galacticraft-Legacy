package team.galacticraft.galacticraft.common.core.tile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityGrating extends BlockEntity
{
    private BlockState substate = Blocks.AIR.defaultBlockState();

    public TileEntityGrating(BlockEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public void load(CompoundTag compound)
    {
        super.load(compound);

        CompoundTag sub = compound.getCompound("substate");

        this.substate = NbtUtils.readBlockState(sub);
    }

    @Override
    public CompoundTag save(CompoundTag compound)
    {
        CompoundTag subTag = NbtUtils.writeBlockState(substate);

        compound.put("substate", subTag);

        return super.save(compound);

    }
}
