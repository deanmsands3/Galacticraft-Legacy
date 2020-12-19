package team.galacticraft.galacticraft.common.core.tile;

import team.galacticraft.galacticraft.common.core.GCBlockNames;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.GCBlocks;
import team.galacticraft.galacticraft.common.core.blocks.BlockMulti;
import team.galacticraft.galacticraft.common.core.blocks.BlockMulti.EnumBlockMultiType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
//import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.List;

public class TileEntityNasaWorkbench extends TileEntityFake implements IMultiBlock
{
//    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.nasaWorkbench)
    public static BlockEntityType<TileEntityNasaWorkbench> TYPE;

    private boolean initialised;

    public TileEntityNasaWorkbench()
    {
        super(TYPE);
    }

    @Override
    public InteractionResult onActivated(Player entityPlayer)
    {
//        entityPlayer.openGui(GalacticraftCore.instance, GuiIdsCore.NASA_WORKBENCH_ROCKET, this.world, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()); TODO guis
        return InteractionResult.SUCCESS;
    }

    @Override
    public void tick()
    {
        if (!this.initialised)
        {
            this.initialised = this.initialiseMultiTiles(this.getBlockPos(), this.level);
        }
    }

    @Override
    public void onCreate(Level world, BlockPos placedPosition)
    {
        this.mainBlockPosition = placedPosition;
        this.setChanged();
        List<BlockPos> positions = new ArrayList<>();
        this.getPositions(placedPosition, positions);
        ((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(world, positions, placedPosition, this.getMultiType());
    }

    @Override
    public EnumBlockMultiType getMultiType()
    {
        return EnumBlockMultiType.NASA_WORKBENCH;
    }

    @Override
    public void getPositions(BlockPos placedPosition, List<BlockPos> positions)
    {
        int buildHeight = this.level.getMaxBuildHeight() - 1;

        for (int y = 1; y < 3; y++)
        {
            if (placedPosition.getY() + y > buildHeight)
            {
                return;
            }

            for (int x = -1; x < 2; x++)
            {
                for (int z = -1; z < 2; z++)
                {
                    if (Math.abs(x) != 1 || Math.abs(z) != 1)
                    {
                        positions.add(new BlockPos(placedPosition.getX() + x, placedPosition.getY() + y, placedPosition.getZ() + z));
                    }
                }
            }
        }

        if (placedPosition.getY() + 3 <= buildHeight)
        {
            positions.add(new BlockPos(placedPosition.getX(), placedPosition.getY() + 3, placedPosition.getZ()));
        }
    }

    @Override
    public void onDestroy(BlockEntity callingBlock)
    {
        final BlockPos thisBlock = getBlockPos();
        List<BlockPos> positions = new ArrayList<>();
        this.getPositions(thisBlock, positions);

        for (BlockPos pos : positions)
        {
            BlockState stateAt = this.level.getBlockState(pos);

            if (stateAt.getBlock() == GCBlocks.fakeBlock && stateAt.getValue(BlockMulti.MULTI_TYPE) == EnumBlockMultiType.NASA_WORKBENCH)
            {
                if (this.level.isClientSide && this.level.random.nextDouble() < 0.05D)
                {
                    Minecraft.getInstance().particleEngine.destroy(pos, this.level.getBlockState(thisBlock));
                }
                this.level.removeBlock(pos, false);
            }
        }
        this.level.destroyBlock(thisBlock, true);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public AABB getRenderBoundingBox()
    {
        return new AABB(getBlockPos().getX() - 1, getBlockPos().getY() - 1, getBlockPos().getZ() - 1, getBlockPos().getX() + 2, getBlockPos().getY() + 5, getBlockPos().getZ() + 2);
    }
}
