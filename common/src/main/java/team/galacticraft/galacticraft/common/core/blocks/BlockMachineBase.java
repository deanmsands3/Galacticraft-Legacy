package team.galacticraft.galacticraft.common.core.blocks;

import net.minecraft.client.resources.language.I18n;
import team.galacticraft.galacticraft.common.compat.PlatformSpecific;
import team.galacticraft.galacticraft.common.core.items.IShiftDescription;
import team.galacticraft.galacticraft.common.core.items.ISortable;
import team.galacticraft.galacticraft.common.core.tile.IMachineSides;
import team.galacticraft.galacticraft.common.core.util.EnumSortCategory;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import team.galacticraft.galacticraft.common.compat.PlatformSpecific;

import org.jetbrains.annotations.Nullable;

public abstract class BlockMachineBase extends BlockTileGC implements IShiftDescription, ISortable
{
    public static final int METADATA_MASK = 0x0c; //Used to select the machine type from metadata
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
//    protected EnumMachineBase[] types;
//    protected EnumMachineBase typeBase;

    public BlockMachineBase(Properties builder)
    {
        super(builder);
//        this.initialiseTypes();
    }

//    protected abstract void initialiseTypes();

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public InteractionResult onMachineActivated(Level worldIn, BlockPos pos, BlockState state, Player playerIn, InteractionHand hand, ItemStack heldItem, BlockHitResult hit)
    {
        if (!worldIn.isClientSide)
        {
            PlatformSpecific.openContainer((ServerPlayer) playerIn, getMenuProvider(state, worldIn, pos), buf -> buf.writeBlockPos(pos));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    @Nullable
    public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos)
    {
        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        return tileentity instanceof MenuProvider ? (MenuProvider) tileentity : null;
    }

    @Override
    public InteractionResult onSneakUseWrench(Level world, BlockPos pos, Player entityPlayer, InteractionHand hand, ItemStack heldItem, BlockHitResult hit)
    {
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof IMachineSides)
        {
            ((IMachineSides) tile).nextSideConfiguration(tile);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

//    @Nullable
//    @Override
//    public TileEntity newBlockEntity(IBlockReader world)
//    {
//        int meta = getMetaFromState(state);
//        EnumMachineBase type = typeBase.fromMetadata(meta);
//        return type.tileConstructor();
//    }

//    @Override
//    public int damageDropped(BlockState state)
//    {
//        return getMetaFromState(state) & BlockMachineBase.METADATA_MASK;
//    }

//    public String getTranslationKey(int meta)
//    {
//        EnumMachineBase type = typeBase.fromMetadata(meta);
//        return type.getTranslationKey();
//    }
//
//    @Override
//    public String getShiftDescription(ItemStack stack)
//    {
//        EnumMachineBase type = typeBase.fromMetadata(meta);
//        return I18n.get(type.getShiftDescriptionKey());
//    }

    @Override
    public String getShiftDescription(ItemStack stack)
    {
        return I18n.get(this.getDescriptionId() + ".description");
    }

    @Override
    public boolean showDescription(ItemStack stack)
    {
        return true;
    }

    public static Direction getFront(BlockState state)
    {
        if (state.getBlock() instanceof BlockMachineBase)
        {
            return (state.getValue(BlockMachineBase.FACING));
        }
        return Direction.NORTH;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.MACHINE;
    }

//    @Override
//    public void getSubBlocks(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        for (EnumMachineBase type : types)
//            list.add(new ItemStack(this, 1, type.getMetadata()));
//    }

    public interface EnumMachineBase<T extends Enum<T> & StringRepresentable>
    {
        int getMetadata();

        EnumMachineBase fromMetadata(int meta);

        String getShiftDescriptionKey();

        String getTranslationKey();

        BlockEntity tileConstructor();
    }
}
