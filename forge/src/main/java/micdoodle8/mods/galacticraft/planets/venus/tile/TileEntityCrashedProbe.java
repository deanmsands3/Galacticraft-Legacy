package micdoodle8.mods.galacticraft.planets.venus.tile;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.inventory.ContainerElectricFurnace;
import micdoodle8.mods.galacticraft.core.tile.TileEntityInventory;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlockNames;
import micdoodle8.mods.galacticraft.planets.venus.inventory.ContainerCrashedProbe;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ObjectHolder;

import java.util.Random;

public class TileEntityCrashedProbe extends TileEntityInventory implements MenuProvider
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + VenusBlockNames.CRASHED_PROBE)
    public static BlockEntityType<TileEntityCrashedProbe> TYPE;

    private boolean hasCoreToDrop;
    protected ResourceLocation lootTable;
    protected long lootTableSeed;

    public TileEntityCrashedProbe()
    {
        super(TYPE);
        inventory = NonNullList.withSize(6, ItemStack.EMPTY);
    }

//    protected void fillWithLoot(PlayerEntity player)
//    {
//        if (this.lootTable != null)
//        {
//            LootTable loottable = this.world.getLootTableManager().getLootTableFromLocation(this.lootTable);
//            this.lootTable = null;
//            Random random;
//
//            if (this.lootTableSeed == 0L)
//            {
//                random = new Random();
//            }
//            else
//            {
//                random = new Random(this.lootTableSeed);
//            }
//
//            LootContext.Builder lootcontext$builder = new LootContext.Builder((ServerWorld) this.world);
//
//            if (player != null)
//            {
//                lootcontext$builder.withLuck(player.getLuck());
//            }
//
//            loottable.fillInventory(this, random, lootcontext$builder.build());
//        }
//    }

    @Override
    protected boolean handleInventory()
    {
        return false;
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        if (nbt.contains("ctd"))
        {
            this.hasCoreToDrop = nbt.getBoolean("ctd");
        }
        else
        {
            this.hasCoreToDrop = true;   //Legacy compatibility with worlds generated before this key used
        }

        if (!this.checkLootAndRead(nbt))
        {
            this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
            ContainerHelper.loadAllItems(nbt, this.getInventory());
        }
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);
        nbt.putBoolean("ctd", this.hasCoreToDrop);

        if (!this.checkLootAndWrite(nbt))
        {
            ContainerHelper.saveAllItems(nbt, this.getInventory());
        }
        return nbt;
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[0];
    }

    @Override
    public ItemStack getItem(int slot)
    {
//    	this.fillWithLoot(null);
        return super.getItem(slot);
    }

    @Override
    public ItemStack removeItem(int index, int count)
    {
//        this.fillWithLoot(null);
        return super.removeItem(index, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index)
    {
//        this.fillWithLoot(null);
        return super.removeItemNoUpdate(index);
    }

    @Override
    public boolean isEmpty()
    {
//        this.fillWithLoot(null);
        return super.isEmpty();
    }

    @Override
    public void clearContent()
    {
//        this.fillWithLoot(null);
    }

//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newSate)
//    {
//        return oldState.getBlock() != newSate.getBlock();
//    }

    public void setDropCore()
    {
        this.hasCoreToDrop = true;
    }

    public boolean getDropCore()
    {
        return this.hasCoreToDrop;
    }

    protected boolean checkLootAndRead(CompoundTag compound)
    {
        if (compound.contains("LootTable", 8))
        {
            this.lootTable = new ResourceLocation(compound.getString("LootTable"));
            this.lootTableSeed = compound.getLong("LootTableSeed");
            return true;
        }
        else
        {
            return false;
        }
    }

    protected boolean checkLootAndWrite(CompoundTag compound)
    {
        if (this.lootTable != null)
        {
            compound.putString("LootTable", this.lootTable.toString());

            if (this.lootTableSeed != 0L)
            {
                compound.putLong("LootTableSeed", this.lootTableSeed);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public void setLootTable(ResourceLocation lootTable, long lootTableSeed)
    {
        this.lootTable = lootTable;
        this.lootTableSeed = lootTableSeed;
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player)
    {
        return new ContainerCrashedProbe(containerId, playerInv, this);
    }

    @Override
    public Component getDisplayName()
    {
        return new TranslatableComponent("container.crashed_probe");
    }
}
