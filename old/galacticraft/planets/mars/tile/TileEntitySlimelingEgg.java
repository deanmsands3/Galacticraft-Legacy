package micdoodle8.mods.galacticraft.planets.mars.tile;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockSlimelingEgg;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlockNames;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.entities.SlimelingEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ObjectHolder;

import java.util.UUID;

public class TileEntitySlimelingEgg extends BlockEntity implements TickableBlockEntity
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + MarsBlockNames.SLIMELING_EGG)
    public static BlockEntityType<TileEntitySlimelingEgg> TYPE;

    public int timeToHatch = -1;
    public String lastTouchedPlayerUUID = "";

    public TileEntitySlimelingEgg()
    {
        super(TYPE);
    }

    @Override
    public void tick()
    {
        if (!this.level.isClientSide)
        {
            if (this.timeToHatch > 0)
            {
                this.timeToHatch--;
            }
            else if (this.timeToHatch == 0 && lastTouchedPlayerUUID != null && lastTouchedPlayerUUID.length() > 0)
            {
                BlockState state = this.level.getBlockState(this.getBlockPos());

                float colorRed = 0.0F;
                float colorGreen = 0.0F;
                float colorBlue = 0.0F;

                if (state.getBlock() == MarsBlocks.RED_SLIMELING_EGG)
                {
                    colorRed = 1.0F;
                }
                else if (state.getBlock() == MarsBlocks.BLUE_SLIMELING_EGG)
                {
                    colorBlue = 1.0F;
                }
                else if (state.getBlock() == MarsBlocks.YELLOW_SLIMELING_EGG)
                {
                    colorRed = 1.0F;
                    colorGreen = 1.0F;
                }

                SlimelingEntity slimeling = SlimelingEntity.createEntitySlimeling(this.level, colorRed, colorGreen, colorBlue);

                slimeling.setPos(this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 1.0, this.getBlockPos().getZ() + 0.5);
                slimeling.setOwnerUUID(UUID.fromString(this.lastTouchedPlayerUUID));

                if (!this.level.isClientSide)
                {
                    this.level.addFreshEntity(slimeling);
                }

                slimeling.setTame(true);
                slimeling.getNavigation().stop();
                slimeling.setTarget(null);
                slimeling.setHealth(20.0F);

                this.level.removeBlock(this.getBlockPos(), false);
            }
        }
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        this.timeToHatch = nbt.getInt("TimeToHatch");

        String uuid = nbt.getString("OwnerUUID");

        if (uuid.length() > 0)
        {
            lastTouchedPlayerUUID = uuid;
        }
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);
        nbt.putInt("TimeToHatch", this.timeToHatch);
        nbt.putString("OwnerUUID", this.lastTouchedPlayerUUID);
        return nbt;
    }

//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newSate)
//    {
//        return oldState.getBlock() != newSate.getBlock();
//    }
}
