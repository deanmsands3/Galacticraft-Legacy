package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTelemetry;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import java.util.UUID;

public class BlockTelemetry extends BlockAdvancedTile implements IShiftDescription, ISortable
{
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    //Metadata: 0-3 = orientation;  bits 2,3 = reserved for future use
    public BlockTelemetry(Properties builder)
    {
        super(builder);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
//        int angle = MathHelper.floor(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
//        int change = 0;
//
//        switch (angle)
//        {
//        case 0:
//            change = 3;
//            break;
//        case 1:
//            change = 4;
//            break;
//        case 2:
//            change = 2;
//            break;
//        case 3:
//            change = 5;
//            break;
//        }

//        worldIn.setBlockState(pos, state.with(FACING, placer.getHorizontalFacing()), 3);
    }

    @Override
    public InteractionResult onUseWrench(Level world, BlockPos pos, Player entityPlayer, InteractionHand hand, ItemStack heldItem, BlockHitResult hit)
    {
        Direction facing = world.getBlockState(pos).getValue(FACING);

        int change = 0;

        switch (facing)
        {
        case DOWN:
            change = 1;
            break;
        case UP:
            change = 3;
            break;
        case NORTH:
            change = 5;
            break;
        case SOUTH:
            change = 4;
            break;
        case WEST:
            change = 2;
            break;
        case EAST:
            change = 0;
            break;
        }
//        change += (12 & metadata);
        world.setBlock(pos, this.defaultBlockState().setValue(FACING, Direction.from3DDataValue(change)), 2);

        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world)
    {
        return new TileEntityTelemetry();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

    @Override
    public InteractionResult onMachineActivated(Level world, BlockPos pos, BlockState state, Player entityPlayer, InteractionHand hand, ItemStack heldItem, BlockHitResult hit)
    {
        if (!world.isClientSide)
        {
            BlockEntity tile = world.getBlockEntity(pos);
            if (tile instanceof TileEntityTelemetry)
            {
                ItemStack held = entityPlayer.inventory.getSelected();
                //Look for Frequency Module
                if (!held.isEmpty() && held.getItem() == GCItems.frequencyModule)
                {
                    CompoundTag fmData = held.getTag();
                    if (fmData != null && fmData.contains("linkedUUIDMost") && fmData.contains("linkedUUIDLeast"))
                    {
                        UUID uuid = new UUID(fmData.getLong("linkedUUIDMost"), fmData.getLong("linkedUUIDLeast"));
                        ((TileEntityTelemetry) tile).addTrackedEntity(uuid);
                        entityPlayer.sendMessage(new TextComponent(GCCoreUtil.translate("gui.telemetry_succeed.message")));
                    }
                    else
                    {
                        entityPlayer.sendMessage(new TextComponent(GCCoreUtil.translate("gui.telemetry_fail.message")));

                        if (fmData == null)
                        {
                            fmData = new CompoundTag();
                            held.setTag(fmData);
                        }
                    }
                    fmData.putInt("teCoordX", pos.getX());
                    fmData.putInt("teCoordY", pos.getY());
                    fmData.putInt("teCoordZ", pos.getZ());
                    fmData.putString("teDim", GCCoreUtil.getDimensionType(world).getRegistryName().toString());
                    return InteractionResult.SUCCESS;
                }

                ItemStack wearing = GCPlayerStats.get(entityPlayer).getFrequencyModuleInSlot();
                if (wearing != null)
                {
                    if (wearing.hasTag() && wearing.getTag().contains("teDim"))
                    {
                        return InteractionResult.PASS;
                    }
                    entityPlayer.sendMessage(new TextComponent(GCCoreUtil.translate("gui.telemetry_fail_wearing_it.message")));
                }
                else
                {
                    entityPlayer.sendMessage(new TextComponent(GCCoreUtil.translate("gui.telemetry_fail_no_frequency_module.message")));
                }
            }
        }
        return InteractionResult.PASS;
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
