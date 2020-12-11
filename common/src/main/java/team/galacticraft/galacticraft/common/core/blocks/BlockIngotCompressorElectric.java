package team.galacticraft.galacticraft.common.core.blocks;

import team.galacticraft.galacticraft.common.core.tile.IMachineSidesProperties;
import team.galacticraft.galacticraft.common.core.tile.TileEntityElectricIngotCompressor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;

public class BlockIngotCompressorElectric extends BlockMachineBase
{
    //    public static final EnumProperty<EnumMachineExtendedType> TYPE = EnumProperty.create("type", EnumMachineExtendedType.class);
    public static IMachineSidesProperties MACHINESIDES_RENDERTYPE = IMachineSidesProperties.TWOFACES_HORIZ;
    public static final EnumProperty<IMachineSidesProperties.MachineSidesModel> SIDES = MACHINESIDES_RENDERTYPE.asProperty;

    public BlockIngotCompressorElectric(Properties builder)
    {
        super(builder);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockGetter world)
    {
        return new TileEntityElectricIngotCompressor.TileEntityElectricIngotCompressorT1();
    }

    @Override
    public boolean isEntityBlock()
    {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, SIDES);
    }
}
