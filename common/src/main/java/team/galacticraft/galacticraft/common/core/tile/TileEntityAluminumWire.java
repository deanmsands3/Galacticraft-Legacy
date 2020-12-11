package team.galacticraft.galacticraft.common.core.tile;

import team.galacticraft.galacticraft.common.core.GCBlockNames;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.energy.tile.TileBaseUniversalConductor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class TileEntityAluminumWire extends TileBaseUniversalConductor
{
    public static class TileEntityAluminumWireT1 extends TileEntityAluminumWire
    {
        @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.aluminumWire)
        public static BlockEntityType<TileEntityAluminumWireT1> TYPE;

        public TileEntityAluminumWireT1()
        {
            super(TYPE, 1);
        }
    }

    public static class TileEntityAluminumWireT2 extends TileEntityAluminumWire
    {
        @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.aluminumWireHeavy)
        public static BlockEntityType<TileEntityAluminumWireT2> TYPE;

        public TileEntityAluminumWireT2()
        {
            super(TYPE, 2);
        }
    }

    public int tier;

    public TileEntityAluminumWire(BlockEntityType<?> type, int tier)
    {
        super(type);
        this.tier = tier;
    }

//    public TileEntityAluminumWire()
//    {
//        this(1);
//    }
//
//    public TileEntityAluminumWire(int theTier)
//    {
//        this.tier = theTier;
//    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        this.tier = nbt.getInt("tier");
        //For legacy worlds (e.g. converted from 1.6.4)
        if (this.tier == 0)
        {
            this.tier = 1;
        }
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);
        nbt.putInt("tier", this.tier);
        return nbt;
    }

    @Override
    public CompoundTag getUpdateTag()
    {
        return this.save(new CompoundTag());
    }

    @Override
    public int getTierGC()
    {
        return this.tier;
    }
}
