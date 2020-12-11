package team.galacticraft.galacticraft.common.core.tile;
//
//import io.netty.buffer.ByteBuf;
//
//import java.util.ArrayList;
//
//import team.galacticraft.galacticraft.common.core.BlockNames;
//import team.galacticraft.galacticraft.common.Constants;
//import team.galacticraft.galacticraft.common.core.GCBlocks;
//import team.galacticraft.galacticraft.common.core.GalacticraftCore;
//import team.galacticraft.galacticraft.common.core.blocks.BlockPanelLighting;
//import team.galacticraft.galacticraft.common.core.blocks.BlockPanelLighting.PanelType;
//import team.galacticraft.galacticraft.common.api.entity.GCPlayerStats;
//import team.galacticraft.galacticraft.common.core.network.IPacketReceiver;
//import team.galacticraft.galacticraft.common.core.network.NetworkUtil;
//import team.galacticraft.galacticraft.common.core.network.PacketDynamic;
//import team.galacticraft.galacticraft.common.core.util.RedstoneUtil;
//import team.galacticraft.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.Blocks;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.tileentity.TileEntityType;
//import net.minecraft.util.Direction;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.math.AxisAlignedBB;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.fml.common.network.ByteBufUtils;
//import net.minecraftforge.fml.EnvType;
//import net.minecraftforge.fml.relauncher.SideOnly;
//import net.minecraftforge.registries.ObjectHolder;
//
//public class TileEntityPanelLight extends TileEntity implements IPacketReceiver
//{
//    @ObjectHolder(Constants.MOD_ID_CORE + ":" + BlockNames.panelLighting)
//    public static TileEntityType<TileEntityPanelLight> TYPE;
//
//    public int meta;
//    private BlockState superState;
//    private static BlockState defaultLook = GalacticraftCore.isPlanetsLoaded ? AsteroidBlocks.blockBasic.getStateFromMeta(6) : GCBlocks.basicBlock.getStateFromMeta(4);
//    public int color = 0xf0f0e0;
//    @OnlyIn(Dist.CLIENT)
//    private AxisAlignedBB renderAABB;
//
//    public TileEntityPanelLight()
//    {
//        super(TYPE);
//    }
//
//    public void initialise(int type, Direction facing, PlayerEntity player, boolean isRemote, BlockState superStateClient)
//    {
//        this.meta = facing.ordinal();
//        if (isRemote)
//        {
//            this.superState = superStateClient;
//            this.color = BlockPanelLighting.color;
//        }
//        else
//        {
//            GCPlayerStats stats = GCPlayerStats.get(player);
//            this.superState = stats.getPanelLightingBases()[type];
//            this.color = stats.getPanelLightingColor();
//        }
//    }
//
//
//    public BlockState getBaseBlock()
//    {
//        if (this.superState != null && this.superState.getBlock() == Blocks.AIR)
//        {
//            this.superState = null;
//        }
//        return this.superState == null ? defaultLook : this.superState;
//    }
//
//    public BlockPanelLighting.PanelType getType()
//    {
//        if (this.world != null)
//        {
//            BlockState b = this.world.getBlockState(this.pos);
//            if (b.getBlock() instanceof BlockPanelLighting)
//            {
//                return (PanelType) b.getValue(BlockPanelLighting.TYPE);
//            }
//        }
//        return BlockPanelLighting.PanelType.SQUARE;
//    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public AxisAlignedBB getRenderBoundingBox()
//    {
//        if (this.renderAABB == null)
//        {
//            this.renderAABB = new AxisAlignedBB(pos, pos.add(1, 1, 1));
//        }
//        return this.renderAABB;
//    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public double getMaxRenderDistanceSquared()
//    {
//        return Constants.RENDERDISTANCE_LONG;
//    }
//
//    @Override
//    public void read(CompoundNBT nbt)
//    {
//        super.read(nbt);
//
//        this.meta = nbt.getInt("meta");
//        if (nbt.contains("col"))
//        {
//            this.color = nbt.getInt("col");
//        }
//        CompoundNBT tag = nbt.getCompound("sust");
//        if (!tag.isEmpty())
//        {
//            this.superState = readBlockState(tag);
//        }
//    }
//
//    @Override
//    public CompoundNBT write(CompoundNBT nbt)
//    {
//        super.write(nbt);
//        nbt.putInt("meta", this.meta);
//        nbt.putInt("col", this.color);
//        if (this.superState != null)
//        {
//            CompoundNBT tag = new CompoundNBT();
//            writeBlockState(tag, this.superState);
//            nbt.put("sust", tag);
//        }
//        return nbt;
//    }
//
//    @Override
//    public CompoundNBT getUpdateTag()
//    {
//        return this.write(new CompoundNBT());
//    }
//
//    /**
//     * Reads a blockstate from the given tag.  In MC1.10+ use NBTUtil instead!
//     */
//    public static BlockState readBlockState(CompoundNBT tag)
//    {
//        if (!tag.contains("Name", 8))
//        {
//            return Blocks.AIR.getDefaultState();
//        }
//        else
//        {
//            Block block = (Block)Block.REGISTRY.getObject(new ResourceLocation(tag.getString("Name")));
//
//            if (tag.contains("Meta"))
//            {
//                int meta = tag.getInt("Meta");
//                if (meta >= 0 && meta < 16)
//                {
//                    return block.getStateFromMeta(meta);
//                }
//            }
//
//            return block.getDefaultState();
//        }
//    }
//
//    /**
//     * Writes the given blockstate to the given tag.  In MC1.10+ use NBTUtil instead!
//     */
//    public static CompoundNBT writeBlockState(CompoundNBT tag, BlockState state)
//    {
//        tag.putString("Name", ((ResourceLocation)Block.REGISTRY.getNameForObject(state.getBlock())).toString());
//        tag.putInt("Meta", state.getBlock().getMetaFromState(state));
//        return tag;
//    }
//
//    public static BlockState readBlockState(String name, Integer meta)
//    {
//        Block block = (Block)Block.REGISTRY.getObject(new ResourceLocation(name));
//        if (block == null)
//        {
//            return Blocks.AIR.getDefaultState();
//        }
//        return block.getStateFromMeta(meta);
//    }
//
//    @Override
//    public void onLoad()
//    {
//        if (this.world.isRemote)
//        {
//            //Request any networked information from server on first client tick
//            GalacticraftCore.packetPipeline.sendToServer(new PacketDynamic(this));
//        }
//    }
//
//    @Override
//    public void getNetworkedData(ArrayList<Object> sendData)
//    {
//        if (this.world.isRemote)
//        {
//            return;
//        }
//
//        sendData.add((byte)this.meta);
//        sendData.add(this.color);
//        if (this.superState != null)
//        {
//            Block block = this.superState.getBlock();
//            if (block == Blocks.AIR)
//            {
//                this.superState = null;
//                return;
//            }
//
//            sendData.add(((ResourceLocation)Block.REGISTRY.getNameForObject(block)).toString());
//            sendData.add((byte) block.getMetaFromState(this.superState));
//        }
//    }
//
//    @Override
//    public void decodePacketdata(ByteBuf buffer)
//    {
//        if (this.world.isRemote)
//        {
//            try
//            {
//                this.meta = buffer.readByte();
//                this.color = buffer.readInt();
//                if (buffer.readableBytes() > 0)
//                {
//                    String name = NetworkUtil.readUTF8String(buffer);
//                    int otherMeta = buffer.readByte();
//                    this.superState = readBlockState(name, otherMeta);
//                    this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
//                }
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public boolean getEnabled()
//    {
//        return !RedstoneUtil.isBlockReceivingRedstone(this.world, this.getPos());
//    }
//}
