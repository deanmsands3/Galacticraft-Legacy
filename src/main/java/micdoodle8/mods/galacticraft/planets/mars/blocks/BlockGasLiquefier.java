package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachineBase;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityElectrolyzer;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityGasLiquefier;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityMethaneSynthesizer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class BlockGasLiquefier extends BlockMachineBase
{
    public BlockGasLiquefier(Properties builder)
    {
        super(builder);
    }

    /**
     * Called when the block is right clicked by the player
     */
    @Override
    public InteractionResult onMachineActivated(Level worldIn, BlockPos pos, BlockState state, Player playerIn, InteractionHand hand, ItemStack heldItem, BlockHitResult hit)
    {
//        playerIn.openGui(GalacticraftPlanets.instance, GuiIdsPlanets.MACHINE_MARS, worldIn, pos.getX(), pos.getY(), pos.getZ()); TODO guis
        return InteractionResult.SUCCESS;
    }

//    @Override
//    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IWorldReader world, BlockPos pos, PlayerEntity player)
//    {
//        int metadata = this.getMetaFromState(state) & BlockMachineBase.METADATA_MASK;
//        return new ItemStack(this, 1, metadata);
//    }

    @Override
    @Environment(EnvType.CLIENT)
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand)
    {
//        final TileEntityGasLiquefier tileEntity = (TileEntityGasLiquefier) worldIn.getTileEntity(pos);
//
//        if (tileEntity.processTicks > 0)
//        {
//            final float x = pos.getX() + 0.5F;
//            final float y = pos.getY() + 0.8F + 0.05F * rand.nextInt(3);
//            final float z = pos.getZ() + 0.5F;
//
//            for (float i = -0.41F + 0.16F * rand.nextFloat(); i < 0.5F; i += 0.167F)
//            {
//                if (rand.nextInt(3) == 0)
//                {
//                    worldIn.addParticle("whiteSmokeTiny", new Vector3(x + i, y, z - 0.41F), new Vector3(0.0D, -0.015D, -0.0015D), new Object[] {});
//                }
//                if (rand.nextInt(3) == 0)
//                {
//                    worldIn.addParticle("whiteSmokeTiny", new Vector3(x + i, y, z + 0.537F), new Vector3(0.0D, -0.015D, 0.0015D), new Object[] {});
//                }
//                if (rand.nextInt(3) == 0)
//                {
//                    worldIn.addParticle("whiteSmokeTiny", new Vector3(x - 0.41F, y, z + i), new Vector3(-0.0015D, -0.015D, 0.0D), new Object[] {});
//                }
//                if (rand.nextInt(3) == 0)
//                {
//                    worldIn.addParticle("whiteSmokeTiny", new Vector3(x + 0.537F, y, z + i), new Vector3(0.0015D, -0.015D, 0.0D), new Object[] {});
//                }
//            }
//        } TODO Particles
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }
}
