package team.galacticraft.galacticraft.common.core.tile;

import team.galacticraft.galacticraft.common.api.item.IKeyable;
import team.galacticraft.galacticraft.common.core.Annotations;
import team.galacticraft.galacticraft.common.core.GCBlockNames;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.GalacticraftCore;
import team.galacticraft.galacticraft.common.core.network.PacketSimple;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
import team.galacticraft.galacticraft.common.core.util.GCLog;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ObjectHolder;

import org.jetbrains.annotations.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class TileEntityTreasureChest extends TileEntityAdvanced implements TickableBlockEntity, Container, IKeyable, WorldlyContainer, LidBlockEntity, MenuProvider
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.treasureChestTier1)
    public static BlockEntityType<TileEntityTreasureChest> TYPE;

    public boolean adjacentChestChecked;
    public float lidAngle;
    public float prevLidAngle;
    public int numPlayersUsing;
    private int ticksSinceSync;
    private AABB renderAABB;

    protected ResourceLocation lootTable;
    protected long lootTableSeed;

    @Annotations.NetworkedField(targetSide = EnvType.CLIENT)
    public boolean locked = true;
    @Annotations.NetworkedField(targetSide = EnvType.CLIENT)
    public int tier = 1;

    public TileEntityTreasureChest()
    {
        this(TYPE, 1);
    }

    public TileEntityTreasureChest(BlockEntityType<?> type, int tier)
    {
        super(type);
        this.tier = tier;
        inventory = NonNullList.withSize(27, ItemStack.EMPTY);
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        this.locked = nbt.getBoolean("isLocked");
        this.tier = nbt.getInt("tier");

        checkLootAndRead(nbt);
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);
        nbt.putBoolean("isLocked", this.locked);
        nbt.putInt("tier", this.tier);

        checkLootAndWrite(nbt);

        return nbt;
    }

    @Override
    public CompoundTag getUpdateTag()
    {
        return this.save(new CompoundTag());
    }

    @Override
    public void clearCache()
    {
        super.clearCache();
        this.adjacentChestChecked = false;
    }

    /**
     * Updates the JList with a new model.
     */
    @Override
    public void tick()
    {
        int i = this.worldPosition.getX();
        int j = this.worldPosition.getY();
        int k = this.worldPosition.getZ();
        ++this.ticksSinceSync;
        float f;

        if (this.locked)
        {
            this.numPlayersUsing = 0;
        }

        if (!this.level.isClientSide && this.numPlayersUsing != 0 && (this.ticksSinceSync + i + j + k) % 200 == 0)
        {
            this.numPlayersUsing = 0;
            f = 5.0F;
            List list = this.level.getEntitiesOfClass(Player.class, new AABB((float) i - f, (float) j - f, (float) k - f, (float) (i + 1) + f, (float) (j + 1) + f, (float) (k + 1) + f));
            Iterator iterator = list.iterator();

            while (iterator.hasNext())
            {
                Player entityplayer = (Player) iterator.next();

                if (entityplayer.containerMenu instanceof ChestMenu)
                {
                    Container iinventory = ((ChestMenu) entityplayer.containerMenu).getContainer();

                    if (iinventory == this || iinventory instanceof CompoundContainer && ((CompoundContainer) iinventory).contains(this))
                    {
                        ++this.numPlayersUsing;
                    }
                }
            }
        }

        this.prevLidAngle = this.lidAngle;
        f = 0.1F;
        double d2;

        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F)
        {
            double d1 = (double) i + 0.5D;
            d2 = (double) k + 0.5D;

            this.level.playSound(null, d1, (double) j + 0.5D, d2, SoundEvents.CHEST_OPEN, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
        }

        if (((this.numPlayersUsing == 0 || this.locked) && this.lidAngle > 0.0F) || this.numPlayersUsing > 0 && this.lidAngle < 1.0F)
        {
            float f1 = this.lidAngle;

            if (this.numPlayersUsing == 0 || this.locked)
            {
                this.lidAngle -= f;
            }
            else
            {
                this.lidAngle += f;
            }

            if (this.lidAngle > 1.0F)
            {
                this.lidAngle = 1.0F;
            }

            float f2 = 0.5F;

            if (this.lidAngle < f2 && f1 >= f2)
            {
                d2 = (double) i + 0.5D;
                double d0 = (double) k + 0.5D;

                this.level.playSound(null, d2, (double) j + 0.5D, d0, SoundEvents.CHEST_CLOSE, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
            }

            if (this.lidAngle < 0.0F)
            {
                this.lidAngle = 0.0F;
            }
        }

        super.tick();
    }

    @Override
    public boolean triggerEvent(int id, int type)
    {
        if (id == 1)
        {
            this.numPlayersUsing = type;
            return true;
        }
        else
        {
            return super.triggerEvent(id, type);
        }
    }

    @Override
    public void startOpen(Player player)
    {
        if (!player.isSpectator())
        {
            if (this.numPlayersUsing < 0)
            {
                this.numPlayersUsing = 0;
            }

            ++this.numPlayersUsing;
            this.level.blockEvent(this.worldPosition, this.getBlockState().getBlock(), 1, this.numPlayersUsing);
            this.level.updateNeighborsAt(this.worldPosition, this.getBlockState().getBlock());
            this.level.updateNeighborsAt(this.worldPosition.below(), this.getBlockState().getBlock());
        }
    }

    @Override
    public void stopOpen(Player player)
    {
        if (!player.isSpectator())
        {
//            --this.numPlayersUsing;
//            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
//            this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType());
//            this.world.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType());
        }
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    @Override
    public boolean canPlaceItem(int index, ItemStack stack)
    {
        return true;
    }

    /**
     * invalidates a tile entity
     */
    @Override
    public void setRemoved()
    {
        super.setRemoved();
        this.clearCache();
    }

    public String getGuiID()
    {
        return "minecraft:chest";
    }

//    public Container createContainer(PlayerInventory playerInventory, PlayerEntity playerIn)
//    {
//        return new ChestContainer(playerInventory, this, playerIn);
//    }

//    @Override
//    public int getField(int id)
//    {
//        return 0;
//    }
//
//    @Override
//    public void setField(int id, int value)
//    {
//    }

//    @Override
//    public int getFieldCount()
//    {
//        return 0;
//    }

    @Override
    public double getPacketRange()
    {
        return 20.0D;
    }

    @Override
    public int getPacketCooldown()
    {
        return 3;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return true;
    }

    @Override
    public int getTierOfKeyRequired()
    {
        return this.tier;
    }

    @Override
    public boolean onValidKeyActivated(Player player, ItemStack key, Direction face)
    {
        if (this.locked)
        {
            this.locked = false;

            if (this.level.isClientSide)
            {
                // player.playSound("galacticraft.player.unlockchest", 1.0F,
                // 1.0F);
            }
            else
            {
                if (!player.abilities.instabuild)
                {
                    player.inventory.getSelected().shrink(1);
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public boolean onActivatedWithoutKey(Player player, Direction face)
    {
        if (this.locked)
        {
            if (player.level.isClientSide)
            {
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_ON_FAILED_CHEST_UNLOCK, GCCoreUtil.getDimensionType(this.level), new Object[]{this.getTierOfKeyRequired()}));
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean canBreak()
    {
        return false;
    }

    public static TileEntityTreasureChest findClosest(Entity entity, int tier)
    {
        double distance = Double.MAX_VALUE;
        TileEntityTreasureChest chest = null;
        for (final BlockEntity tile : entity.level.blockEntityList)
        {
            if (tile instanceof TileEntityTreasureChest && ((TileEntityTreasureChest) tile).getTierOfKeyRequired() == tier)
            {
                double dist = entity.distanceToSqr(tile.getBlockPos().getX() + 0.5, tile.getBlockPos().getY() + 0.5, tile.getBlockPos().getZ() + 0.5);
                if (dist < distance)
                {
                    distance = dist;
                    chest = (TileEntityTreasureChest) tile;
                }
            }
        }

        if (chest != null)
        {
            GCLog.debug("Found chest to generate boss loot in: " + chest.worldPosition);
        }
        else
        {
            GCLog.debug("Could not find chest to generate boss loot in!");
        }

        return chest;
    }

//    protected boolean checkLootAndRead(CompoundNBT compound)
//    {
//        if (compound.contains("LootTable", 8))
//        {
//            this.lootTable = new ResourceLocation(compound.getString("LootTable"));
//            this.lootTableSeed = compound.getLong("LootTableSeed");
//            return true;
//        }
//        else
//        {
//            return false;
//        }
//    }

//    protected boolean checkLootAndWrite(CompoundNBT compound)
//    {
//        if (this.lootTable != null)
//        {
//            compound.putString("LootTable", this.lootTable.toString());
//
//            if (this.lootTableSeed != 0L)
//            {
//                compound.putLong("LootTableSeed", this.lootTableSeed);
//            }
//
//            return true;
//        }
//        else
//        {
//            return false;
//        }
//    }

//    public void fillWithLoot(PlayerEntity player)
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
//            LootContext.Builder builder = new LootContext.Builder((ServerWorld) this.world);
//
//            if (player != null)
//            {
//                builder.withLuck(player.getLuck());
//            }
//
//            loottable.fillInventory(this, random, builder.build());
//        }
//    }

    public ResourceLocation getLootTable()
    {
        return this.lootTable;
    }

//    public void setLootTable(ResourceLocation lootTable, long lootTableSeed)
//    {
//        this.lootTable = lootTable;
//        this.lootTableSeed = lootTableSeed;
//    }

    @Override
    @Environment(EnvType.CLIENT)
    public AABB getRenderBoundingBox()
    {
        if (this.renderAABB == null)
        {
            this.renderAABB = new AABB(worldPosition, worldPosition.offset(1, 2, 1));
        }
        return this.renderAABB;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public double getViewDistance()
    {
        return Constants.RENDERDISTANCE_MEDIUM;
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[0];
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction)
    {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction)
    {
        return false;
    }

    public static void setLootTable(BlockGetter reader, Random rand, BlockPos p_195479_2_, ResourceLocation lootTableIn)
    {
        BlockEntity tileentity = reader.getBlockEntity(p_195479_2_);
        if (tileentity instanceof RandomizableContainerBlockEntity)
        {
            ((RandomizableContainerBlockEntity) tileentity).setLootTable(lootTableIn, rand.nextLong());
        }

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
        if (this.lootTable == null)
        {
            return false;
        }
        else
        {
            compound.putString("LootTable", this.lootTable.toString());
            if (this.lootTableSeed != 0L)
            {
                compound.putLong("LootTableSeed", this.lootTableSeed);
            }

            return true;
        }
    }

    public void fillWithLoot(@Nullable Player player)
    {
        if (this.lootTable != null && this.level.getServer() != null)
        {
            LootTable loottable = this.level.getServer().getLootTables().get(this.lootTable);
            this.lootTable = null;
            LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerLevel) this.level)).withParameter(LootContextParams.BLOCK_POS, new BlockPos(this.worldPosition)).withOptionalRandomSeed(this.lootTableSeed);
            if (player != null)
            {
                lootcontext$builder.withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player);
            }

            loottable.fill(this, lootcontext$builder.create(LootContextParamSets.CHEST));
        }

    }

    public void setLootTable(ResourceLocation lootTableIn, long seedIn)
    {
        this.lootTable = lootTableIn;
        this.lootTableSeed = seedIn;
    }

    /**
     * Returns the stack in the given slot.
     */
    @Override
    public ItemStack getItem(int index)
    {
        this.fillWithLoot(null);
        return this.inventory.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    @Override
    public ItemStack removeItem(int index, int count)
    {
        this.fillWithLoot(null);
        ItemStack itemstack = ContainerHelper.removeItem(this.inventory, index, count);
        if (!itemstack.isEmpty())
        {
            this.setChanged();
        }

        return itemstack;
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    @Override
    public ItemStack removeItemNoUpdate(int index)
    {
        this.fillWithLoot(null);
        return ContainerHelper.takeItem(this.inventory, index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
    public void setItem(int index, ItemStack stack)
    {
        this.fillWithLoot(null);
        this.inventory.set(index, stack);
        if (stack.getCount() > this.getMaxStackSize())
        {
            stack.setCount(this.getMaxStackSize());
        }

        this.setChanged();
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    @Override
    public boolean stillValid(Player player)
    {
        if (this.level.getBlockEntity(this.worldPosition) != this)
        {
            return false;
        }
        else
        {
            return !(player.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) > 64.0D);
        }
    }

    @Override
    public void clearContent()
    {
        this.inventory.clear();
    }

    @Override
    public ChestMenu createMenu(int containerId, Inventory playerInv, Player player)
    {
        this.fillWithLoot(playerInv.player);
        return ChestMenu.threeRows(containerId, playerInv, this);
    }

    @Override
    public Component getDisplayName()
    {
        return new TranslatableComponent("container.treasure_chest");
    }

    @Override
    public float getOpenNess(float partialTicks)
    {
        return Mth.lerp(partialTicks, this.prevLidAngle, this.lidAngle);
    }
}