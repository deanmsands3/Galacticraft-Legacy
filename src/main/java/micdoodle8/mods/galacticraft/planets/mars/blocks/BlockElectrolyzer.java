package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachineBase;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityElectrolyzer;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityGasLiquefier;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityMethaneSynthesizer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class BlockElectrolyzer extends BlockMachineBase
{
    public static final EnumProperty<EnumMachineType> TYPE = EnumProperty.create("type", EnumMachineType.class);

    public enum EnumMachineType implements EnumMachineBase, StringRepresentable
    {
        GAS_LIQUEFIER(0, "gas_liquefier", TileEntityGasLiquefier::new, "tile.gas_liquefier.description", "tile.mars_machine.4"),
        METHANE_SYNTHESIZER(4, "methane_synthesizer", TileEntityMethaneSynthesizer::new, "tile.methane_synthesizer.description", "tile.mars_machine.5"),
        ELECTROLYZER(8, "electrolyzer", TileEntityElectrolyzer::new, "tile.electrolyzer.description", "tile.mars_machine.6");

        private final int meta;
        private final String name;
        private final TileConstructor tile;
        private final String shiftDescriptionKey;
        private final String blockName;

        EnumMachineType(int meta, String name, TileConstructor tile, String key, String blockName)
        {
            this.meta = meta;
            this.name = name;
            this.tile = tile;
            this.shiftDescriptionKey = key;
            this.blockName = blockName;
        }

        @Override
        public int getMetadata()
        {
            return this.meta;
        }

        private final static EnumMachineType[] values = values();

        @Override
        public EnumMachineType fromMetadata(int meta)
        {
            return values[(meta / 4) % values.length];
        }

        @Override
        public String getSerializedName()
        {
            return this.name;
        }

        @Override
        public BlockEntity tileConstructor()
        {
            return this.tile.create();
        }

        @FunctionalInterface
        private interface TileConstructor
        {
            BlockEntity create();
        }

        @Override
        public String getShiftDescriptionKey()
        {
            return this.shiftDescriptionKey;
        }

        @Override
        public String getTranslationKey()
        {
            return this.blockName;
        }
    }

    public BlockElectrolyzer(Properties builder)
    {
        super(builder);
    }

    @Override
    public InteractionResult onMachineActivated(Level world, BlockPos pos, BlockState state, Player entityPlayer, InteractionHand hand, ItemStack heldItem, BlockHitResult hit)
    {
//        playerIn.openGui(GalacticraftPlanets.instance, GuiIdsPlanets.MACHINE_MARS, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, TYPE);
    }
}
