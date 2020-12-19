package team.galacticraft.galacticraft.common.core.tile;

import io.netty.buffer.ByteBuf;
import team.galacticraft.galacticraft.common.api.item.IPaintable;
import team.galacticraft.galacticraft.common.api.tile.IDisableableMachine;
import team.galacticraft.galacticraft.common.api.vector.BlockVec3;
import team.galacticraft.galacticraft.common.core.GCBlockNames;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.GalacticraftCore;
import team.galacticraft.galacticraft.common.api.entity.GCPlayerStats;
import team.galacticraft.galacticraft.common.core.inventory.ContainerPainter;
import team.galacticraft.galacticraft.common.core.network.IPacketReceiver;
import team.galacticraft.galacticraft.common.core.network.PacketDynamic;
import team.galacticraft.galacticraft.common.core.util.ColorUtil;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
import team.galacticraft.galacticraft.common.core.util.PlayerUtil;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.MaterialColor;
//import net.minecraftforge.registries.ObjectHolder;

import java.util.*;

//import net.minecraft.item.EnumDyeColor;

public class TileEntityPainter extends TileEntityInventory implements IDisableableMachine, IPacketReceiver, MenuProvider
{
//    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.painter)
    public static BlockEntityType<TileEntityPainter> TYPE;

    private static final int RANGE_DEFAULT = 96;
    public static Map<DimensionType, Set<BlockVec3>> loadedTilesForDim = new HashMap<>();

    public int range = RANGE_DEFAULT;  //currently unused

    public int[] glassColor = new int[]{-1, -1, -1};  //Size of this array must match GlassType enum
    public final Set<Player> playersUsing = new HashSet<Player>();

    public int guiColor = 0xffffff;

    public TileEntityPainter()
    {
        super(TYPE);
        inventory = NonNullList.withSize(2, ItemStack.EMPTY);
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[0];
    }

    public void takeColorFromItem(ItemStack itemStack)
    {
        if (itemStack == null)
        {
            return;
        }

        Item item = itemStack.getItem();
        int color = -1;
        if (item instanceof DyeItem)
        {
            color = ((DyeItem) item).getDyeColor().getColorValue();
        }
        else if (item instanceof BlockItem)
        {
            Block b = ((BlockItem) item).getBlock();
            try
            {
                MaterialColor mc = b.defaultBlockState().getMapColor(level, null);
                color = mc.col;
            }
            catch (Exception e)
            {
            }
        }
        else
        {
            color = tryOtherModDyes(itemStack);
        }

        if (color >= 0)
        {
            color = ColorUtil.addColorsBasic(color, this.guiColor);
            this.guiColor = color;
        }
    }

    private void applyColorToItem(ItemStack itemStack, int color, Player player)
    {
        if (itemStack == null)
        {
            return;
        }

        Item item = itemStack.getItem();

        if (item instanceof IPaintable)
        {
            //TODO  e.g. flags, eggs, rockets???
        }
        else if (item instanceof BlockItem)
        {
            color = ColorUtil.lighten(color, 0.03F);
            Block b = ((BlockItem) item).getBlock();
            int result = 0;
            if (b instanceof IPaintable)
            {
                result = ((IPaintable) b).setColor(color, player);
            }
//            if (b instanceof BlockSpaceGlass)
//            {
//                int type = ((BlockSpaceGlass)b).type.ordinal();
//                this.glassColor[type] = color;
//                if (result > 0 && EnvType == EnvType.CLIENT)
//                {
//                    BlockSpaceGlass.updateClientRender();
//                }
//            } TODO Space glass
        }
    }

    private void setGlassColors(int color1, int color2, int color3)
    {
        boolean changes = false;
        if (this.glassColor[0] != color1)
        {
            changes = true;
            this.glassColor[0] = color1;
        }
        if (this.glassColor[1] != color2)
        {
            changes = true;
            this.glassColor[1] = color2;
        }
        if (this.glassColor[2] != color3)
        {
            changes = true;
            this.glassColor[2] = color3;
        }

//        if (changes)
//            ColorUtil.updateColorsForArea(this.world), this.pos, this.range, this.glassColor[0], this.glassColor[1], this.glassColor[2]);;
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        if (nbt.contains("guic"))
        {
            this.glassColor[0] = nbt.getInt("G1");
            this.glassColor[1] = nbt.getInt("G2");
            this.glassColor[2] = nbt.getInt("G3");
            this.range = nbt.getInt("rge");
            this.guiColor = nbt.getInt("guic");
        }
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);
        nbt.putInt("G1", this.glassColor[0]);
        nbt.putInt("G2", this.glassColor[1]);
        nbt.putInt("G3", this.glassColor[2]);
        nbt.putInt("guic", this.guiColor);
        nbt.putInt("rge", this.range);

        return nbt;
    }

    @Override
    public CompoundTag getUpdateTag()
    {
        return this.save(new CompoundTag());
    }

    private static Set<BlockVec3> getLoadedTiles(Level world)
    {
        DimensionType dimID = GCCoreUtil.getDimensionType(world);
        Set<BlockVec3> loaded = loadedTilesForDim.get(dimID);

        if (loaded == null)
        {
            loaded = new HashSet<BlockVec3>();
            loadedTilesForDim.put(dimID, loaded);
        }

        return loaded;
    }

    @Override
    public void onLoad()
    {
        if (this.level.isClientSide)
        {
            //Request any networked information from server on first client tick
            GalacticraftCore.packetPipeline.sendToServer(new PacketDynamic(this));
        }
        else
        {
            getLoadedTiles(this.level).add(new BlockVec3(this.worldPosition));
        }
    }

    @Override
    public void onChunkUnloaded()
    {
        getLoadedTiles(this.level).remove(new BlockVec3(this.worldPosition));
        super.onChunkUnloaded();
    }

    @Override
    public void setRemoved()
    {
        if (!this.level.isClientSide)
        {
            getLoadedTiles(this.level).remove(new BlockVec3(this.worldPosition));
        }
        super.setRemoved();
    }

    public static void onServerTick(Level world)
    {
        Set<BlockVec3> loaded = getLoadedTiles(world);
        DimensionType dimID = GCCoreUtil.getDimensionType(world);
        List<ServerPlayer> allPlayers = PlayerUtil.getPlayersOnline();
        for (final ServerPlayer player : allPlayers)
        {
            if (player.dimension != dimID)
            {
                continue;
            }

            BlockVec3 playerPos = new BlockVec3(player);
            BlockVec3 nearest = null;
            int shortestDistance = RANGE_DEFAULT * RANGE_DEFAULT;
            for (final BlockVec3 bv : loaded)
            {
                int distance = bv.distanceSquared(playerPos);
                if (distance < shortestDistance)
                {
                    shortestDistance = distance;
                    nearest = bv;
                }
            }

            if (nearest != null)
            {
                BlockEntity te = nearest.getTileEntity(world);
                if (te instanceof TileEntityPainter)
                {
                    ((TileEntityPainter) te).dominantToPlayer(player);
                }
            }

            //TODO
            //Make sure this works in a way so that the nearest Painter quickly takes priority, but there is no race condition...
            //Also maybe some hysteresis?
        }
    }

    private void dominantToPlayer(ServerPlayer player)
    {
        GCPlayerStats.get(player).setGlassColors(this.glassColor[0], this.glassColor[1], this.glassColor[2]);
    }

//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newSate)
//    {
//        return oldState.getBlock() != newSate.getBlock();
//    }

    @Override
    public void setDisabled(int index, boolean disabled)
    {
        //Used to do the painting!
        for (Player entityPlayer : playersUsing)
        {
            this.buttonPressed(index, entityPlayer);
        }
    }

    public void buttonPressed(int index, Player player)
    {
        switch (index)
        {
        case 0:  //Apply Paint
            this.applyColorToItem(this.getItem(1), this.guiColor, player);
            break;
        case 1:  //Mix Colors
            this.takeColorFromItem(this.getItem(0));
            break;
        case 2:  //Reset Colors
            this.guiColor = 0xffffff;
            break;
        }
    }

    @Override
    public boolean getDisabled(int index)
    {
        return false;
    }

    @Override
    public void getNetworkedData(ArrayList<Object> sendData)
    {
        if (this.level.isClientSide)
        {
            return;
        }

        sendData.add(this.guiColor);
    }

    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        if (this.level.isClientSide)
        {
            try
            {
                this.guiColor = buffer.readInt();
            }
            catch (Exception ignore)
            {
                ignore.printStackTrace();
            }
        }
    }

    //Any special cases go here, e.g. coloured dye or paint items added by other mods
    private static int tryOtherModDyes(ItemStack itemStack)
    {
        Item item = itemStack.getItem();

//        if (CompatibilityManager.isIc2Loaded())
//        {
//            ItemStack ic2paintbrush = IC2Items.getItem("painter");
//            if (ic2paintbrush != null && item == ic2paintbrush.getItem())
//            {
//                return DyeItem.DYE_COLORS[itemStack.getDamage()];
//            }
//        } TODO Ic2 support

//        if (CompatibilityManager.isBOPLoaded())
//        {
//            if (item == BOPItems.black_dye) return DyeItem.DYE_COLORS[DyeColor.BLACK.getDyeDamage()];
//            if (item == BOPItems.blue_dye) return DyeItem.DYE_COLORS[DyeColor.BLUE.getDyeDamage()];
//            if (item == BOPItems.brown_dye) return DyeItem.DYE_COLORS[DyeColor.BROWN.getDyeDamage()];
//            if (item == BOPItems.green_dye) return DyeItem.DYE_COLORS[DyeColor.GREEN.getDyeDamage()];
//            if (item == BOPItems.white_dye) return DyeItem.DYE_COLORS[DyeColor.WHITE.getDyeDamage()];
//        } TODO Biomes o plenty support

        return -1;
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player)
    {
        return new ContainerPainter(containerId, playerInv, this);
    }

    @Override
    public Component getDisplayName()
    {
        return new TranslatableComponent("container.painter");
    }
}
