package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.api.block.IDetectableResource;
import micdoodle8.mods.galacticraft.api.block.IPlantableBlock;
import micdoodle8.mods.galacticraft.api.block.ITerraformableBlock;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.client.GCParticles;
import micdoodle8.mods.galacticraft.core.client.fx.BlockPosParticleData;
import micdoodle8.mods.galacticraft.core.client.fx.EntityParticleData;
import micdoodle8.mods.galacticraft.core.client.sounds.GCSounds;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.client.fx.MarsParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

public class BlockBasicMars extends Block implements IDetectableResource, IPlantableBlock, ITerraformableBlock, ISortable
{
//    public enum EnumBlockBasic implements IStringSerializable
//    {
//        ORE_COPPER(0, "ore_copper_mars"),
//        ORE_TIN(1, "ore_tin_mars"),
//        ORE_DESH(2, "ore_desh_mars"),
//        ORE_IRON(3, "ore_iron_mars"),
//        COBBLESTONE(4, "cobblestone"),
//        SURFACE(5, "mars_surface"),
//        MIDDLE(6, "mars_middle"),
//        DUNGEON_BRICK(7, "dungeon_brick"),
//        DESH_BLOCK(8, "desh_block"),
//        MARS_STONE(9, "mars_stone");
//
//        private final int meta;
//        private final String name;
//
//        private EnumBlockBasic(int meta, String name)
//        {
//            this.meta = meta;
//            this.name = name;
//        }
//
//        public int getMeta()
//        {
//            return this.meta;
//        }
//
//        private final static EnumBlockBasic[] values = values();
//        public static EnumBlockBasic byMetadata(int meta)
//        {
//            return values[meta % values.length];
//        }
//
//        @Override
//        public String getName()
//        {
//            return this.name;
//        }
//    }

    public BlockBasicMars(Properties builder)
    {
        super(builder);
    }

    @Override
    public MaterialColor getMapColor(BlockState state, BlockGetter worldIn, BlockPos pos)
    {
        if (this == MarsBlocks.dungeonBrick)
        {
            return MaterialColor.COLOR_GREEN;
        }
        else if (this == MarsBlocks.rockSurface)
        {
            return MaterialColor.DIRT;
        }

        return MaterialColor.COLOR_RED;
    }

//    @Override
//    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
//    {
//        EnumBlockBasic type = (EnumBlockBasic) world.getBlockState(pos).getValue(BASIC_TYPE);
//
//        if (type == EnumBlockBasic.DUNGEON_BRICK)
//        {
//            return 40.0F;
//        }
//        else if (type == EnumBlockBasic.DESH_BLOCK)
//        {
//            return 60.0F;
//        }
//
//        return super.getExplosionResistance(world, pos, exploder, explosion);
//    }
//
////    @OnlyIn(Dist.CLIENT)
////    @Override
////    public ItemGroup getCreativeTabToDisplayOn()
////    {
////        return GalacticraftCore.galacticraftBlocksTab;
////    }
//
//    @Override
//    public float getBlockHardness(BlockState blockState, World worldIn, BlockPos pos)
//    {
//        BlockState state = worldIn.getBlockState(pos);
//
//        if (state.get(BASIC_TYPE) == EnumBlockBasic.DUNGEON_BRICK)
//        {
//            return 4.0F;
//        }
//
//        return this.blockHardness;
//    }

//    @Override
//    public Item getItemDropped(BlockState state, Random rand, int fortune)
//    {
//        if (state.get(BASIC_TYPE) == EnumBlockBasic.ORE_DESH)
//        {
//            return MarsItems.marsItemBasic;
//        }
//
//        return Item.getItemFromBlock(this);
//    }
//
//    @Override
//    public int damageDropped(BlockState state)
//    {
//        int meta = state.getBlock().getMetaFromState(state);
//        if (state.get(BASIC_TYPE) == EnumBlockBasic.MARS_STONE)
//        {
//            return 4;
//        }
//        else if (state.get(BASIC_TYPE) == EnumBlockBasic.ORE_DESH)
//        {
//            return 0;
//        }
//        else
//        {
//            return meta;
//        }
//    }

//    @Override
//    public int quantityDropped(BlockState state, int fortune, Random random)
//    {
//        if (state.get(BASIC_TYPE) == EnumBlockBasic.ORE_DESH && fortune >= 1)
//        {
//            return (random.nextFloat() < fortune * 0.29F - 0.25F) ? 2 : 1;
//        }
//
//        return 1;
//    } TODO Block item drops
//
//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public void getSubBlocks(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        for (EnumBlockBasic blockBasic : EnumBlockBasic.values())
//        {
//            list.add(new ItemStack(this, 1, blockBasic.getMeta()));
//        }
//    }

    @Override
    public boolean isValueable(BlockState state)
    {
        return this == MarsBlocks.oreCopper || this == MarsBlocks.oreDesh || this == MarsBlocks.oreIron || this == MarsBlocks.oreTin;
//        switch (this.getMetaFromState(state))
//        {
//        case 0:
//        case 1:
//        case 2:
//        case 3:
//            return true;
//        default:
//            return false;
//        }
    }

    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable)
    {
        return false;
    }

    //    @Override
//    public boolean canSustainPlant(BlockState state, IBlockAccess world, BlockPos pos, Direction direction, IPlantable plantable)
//    {
//        return false;
//    }

    @Override
    public int requiredLiquidBlocksNearby()
    {
        return 4;
    }

    @Override
    public boolean isPlantable(BlockState state)
    {
        return false;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void animateTick(BlockState state, Level worldIn, BlockPos pos, Random rand)
    {
        if (rand.nextInt(10) == 0)
        {
            if (this == MarsBlocks.dungeonBrick)
            {
                worldIn.addParticle(MarsParticles.DRIP, pos.getX() + rand.nextFloat(), pos.getY(), pos.getZ() + rand.nextFloat(), 0, 0, 0);

                if (rand.nextInt(100) == 0)
                {
                    worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), GCSounds.singleDrip, SoundSource.AMBIENT, 1, 0.8F + rand.nextFloat() / 5.0F);
                }
            }
        }
    }

    @Override
    public boolean isTerraformable(Level world, BlockPos pos)
    {
        BlockState state = world.getBlockState(pos);
        BlockState stateAbove = world.getBlockState(pos.above());
        return this == MarsBlocks.rockSurface && stateAbove.getShape(world, pos.above()) != Shapes.block();
    }

    @Override
    public ItemStack getPickBlock(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player)
    {
        return new ItemStack(Item.byBlock(this), 1);
    }

    @Override
    public boolean isReplaceableOreGen(BlockState state, LevelReader world, BlockPos pos, java.util.function.Predicate<BlockState> target)
    {
        return this == MarsBlocks.rockMiddle || this == MarsBlocks.stone;
    }

//    @Override
//    public boolean hasTileEntity(BlockState state)
//    {
//        return state.getBlock().getMetaFromState(state) == 10;
//    }

//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        return this.getDefaultState().with(BASIC_TYPE, EnumBlockBasic.byMetadata(meta));
//    }

//    @Override
//    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
//    {
//        builder.add(BASIC_TYPE);
//    }

    @Override
    public EnumSortCategory getCategory()
    {
        if (this == MarsBlocks.oreDesh || this == MarsBlocks.oreTin || this == MarsBlocks.oreIron || this == MarsBlocks.oreCopper)
        {
            return EnumSortCategory.ORE;
        }
        else if (this == MarsBlocks.dungeonBrick)
        {
            return EnumSortCategory.BRICKS;
        }
        else if (this == MarsBlocks.deshBlock)
        {
            return EnumSortCategory.INGOT_BLOCK;
        }
        return EnumSortCategory.GENERAL;
    }


    @Override
    public int getExpDrop(BlockState state, LevelReader world, BlockPos pos, int fortune, int silktouch)
    {
        if (state.getBlock() != this)
        {
            return 0;
        }

        if (this == MarsBlocks.oreDesh)
        {
            Random rand = world instanceof Level ? ((Level) world).random : new Random();
            return Mth.nextInt(rand, 2, 5);
        }

        return 0;
    }
}
