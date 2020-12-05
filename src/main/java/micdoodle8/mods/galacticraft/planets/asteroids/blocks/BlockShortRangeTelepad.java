package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import micdoodle8.mods.galacticraft.core.blocks.BlockTileGC;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.client.fx.BlockPosParticleData;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.client.fx.AsteroidParticles;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.ShortRangeTelepadHandler;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityShortRangeTelepad;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class BlockShortRangeTelepad extends BlockTileGC implements IShiftDescription, ISortable
{
    protected static final VoxelShape AABB_TELEPAD = Shapes.box(0.0F, 0.0F, 0.0F, 1.0F, 0.45F, 1.0F);

    protected BlockShortRangeTelepad(Properties builder)
    {
        super(builder);
    }

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

    @Override
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.INVISIBLE;
    }

//    @Override
//    public boolean isOpaqueCube(BlockState state)
//    {
//        return false;
//    }

//    @Override
//    public boolean isFullCube(BlockState state)
//    {
//        return false;
//    }

//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return BlockFaceShape.UNDEFINED;
//    }

    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world)
    {
        return new TileEntityShortRangeTelepad();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return AABB_TELEPAD;
    }

//    @Override
//    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
//    {
//        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.45F, 1.0F);
//    }

//    @Override
//    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
//    {
//        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.45F, 1.0F);
//        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        super.setPlacedBy(worldIn, pos, state, placer, stack);

        BlockEntity tile = worldIn.getBlockEntity(pos);

        boolean validSpot = true;

        for (int x = -1; x <= 1; x++)
        {
            for (int y = 0; y < 3; y += 2)
            {
                for (int z = -1; z <= 1; z++)
                {
                    if (!(x == 0 && y == 0 && z == 0))
                    {
                        BlockState stateAt = worldIn.getBlockState(pos.offset(x, y, z));

                        if (!stateAt.getMaterial().isReplaceable())
                        {
                            validSpot = false;
                        }
                    }
                }
            }
        }

        if (!validSpot)
        {
            worldIn.removeBlock(pos, false);

            if (placer instanceof Player)
            {
                if (!worldIn.isClientSide)
                {
                    placer.sendMessage(new TextComponent(EnumColor.RED + GCCoreUtil.translate("gui.warning.noroom")));
                }
                ((Player) placer).inventory.add(new ItemStack(Item.byBlock(this), 1));
            }

            return;
        }

        if (tile instanceof TileEntityShortRangeTelepad)
        {
            ((TileEntityShortRangeTelepad) tile).onCreate(worldIn, pos);
            ((TileEntityShortRangeTelepad) tile).setOwner(PlayerUtil.getName(((Player) placer)));
        }
    }

    @Override
    public InteractionResult onMachineActivated(Level worldIn, BlockPos pos, BlockState state, Player playerIn, InteractionHand hand, ItemStack heldItem, BlockHitResult hit)
    {
        return ((IMultiBlock) worldIn.getBlockEntity(pos)).onActivated(playerIn);
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        final BlockEntity tileAt = worldIn.getBlockEntity(pos);

        int fakeBlockCount = 0;

        for (int x = -1; x <= 1; x++)
        {
            for (int y = 0; y < 3; y += 2)
            {
                for (int z = -1; z <= 1; z++)
                {
                    if (!(x == 0 && y == 0 && z == 0))
                    {
                        if (worldIn.getBlockState(pos.offset(x, y, z)).getBlock() == AsteroidBlocks.fakeTelepad)
                        {
                            fakeBlockCount++;
                        }
                    }
                }
            }
        }

        if (tileAt instanceof TileEntityShortRangeTelepad)
        {
            if (fakeBlockCount > 0)
            {
                ((TileEntityShortRangeTelepad) tileAt).onDestroy(tileAt);
            }
            ShortRangeTelepadHandler.removeShortRangeTeleporter((TileEntityShortRangeTelepad) tileAt);
        }

        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand)
    {
        final BlockEntity tileAt = worldIn.getBlockEntity(pos);

        if (tileAt instanceof TileEntityShortRangeTelepad)
        {
            TileEntityShortRangeTelepad telepad = (TileEntityShortRangeTelepad) tileAt;

            for (int i = 0; i < 6; i++)
            {
                BlockPosParticleData data = new BlockPosParticleData(AsteroidParticles.TELEPAD_UP, pos);
                for (int j = 0; j < 4; j++)
                {
                    worldIn.addParticle(data, pos.getX() + 0.2 + rand.nextDouble() * 0.6, pos.getY() + 0.1, pos.getZ() + 0.2 + rand.nextDouble() * 0.6, 0.0, 1.4, 0.0);
//                    GalacticraftPlanets.addParticle("portalBlue", new Vector3(pos.getX() + 0.2 + rand.nextDouble() * 0.6, pos.getY() + 0.1, pos.getZ() + 0.2 + rand.nextDouble() * 0.6), new Vector3(0.0, 1.4, 0.0), telepad, false);
                }

                data = new BlockPosParticleData(AsteroidParticles.TELEPAD_DOWN, pos);
                worldIn.addParticle(data, pos.getX() + 0.0 + rand.nextDouble() * 0.2, pos.getY() + 2.9, pos.getZ() + rand.nextDouble(), 0.0, -2.95, 0.0);
                worldIn.addParticle(data, pos.getX() + 0.8 + rand.nextDouble() * 0.2, pos.getY() + 2.9, pos.getZ() + rand.nextDouble(), 0.0, -2.95, 0.0);
                worldIn.addParticle(data, pos.getX() + rand.nextDouble(), pos.getY() + 2.9, pos.getZ() + 0.2 + rand.nextDouble() * 0.2, 0.0, -2.95, 0.0);
                worldIn.addParticle(data, pos.getX() + rand.nextDouble(), pos.getY() + 2.9, pos.getZ() + 0.8 + rand.nextDouble() * 0.2, 0.0, -2.95, 0.0);
            }
        }
    }

    @Override
    public String getShiftDescription(ItemStack stack)
    {
        return GCCoreUtil.translate(this.getDescriptionId() + ".description");
    }

    @Override
    public boolean showDescription(ItemStack stack)
    {
        return true;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.MACHINE;
    }
}
