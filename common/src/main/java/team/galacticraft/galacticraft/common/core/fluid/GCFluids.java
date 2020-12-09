package team.galacticraft.galacticraft.common.core.fluid;

import team.galacticraft.galacticraft.core.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid.Flowing;
import net.minecraftforge.fluids.ForgeFlowingFluid.Source;
import java.util.function.UnaryOperator;

import static net.minecraftforge.fluids.ForgeFlowingFluid.Source;
import static net.minecraftforge.fluids.ForgeFlowingFluid.Flowing;

public class GCFluids
{
    public static final GCFluidRegistry FLUIDS = new GCFluidRegistry();

    public static final FluidRegistrationEntry<Source, Flowing, LiquidBlock, BucketItem> OIL = registerLiquid("oil", fluidAttributes -> fluidAttributes.color(0xFF111111).density(800).viscosity(1500));
    public static final FluidRegistrationEntry<Source, Flowing, LiquidBlock, BucketItem> FUEL = registerLiquid("fuel", fluidAttributes -> fluidAttributes.color(0xFFDBDF16).density(400).viscosity(900));
    public static final FluidRegistrationEntry<Source, Flowing, LiquidBlock, BucketItem> OXYGEN = registerLiquid("oxygen", fluidAttributes -> fluidAttributes.color(0xFF6CE2FF).temperature(1).density(13).viscosity(295).gaseous());
    public static final FluidRegistrationEntry<Source, Flowing, LiquidBlock, BucketItem> HYDROGEN = registerLiquid("hydrogen", fluidAttributes -> fluidAttributes.color(0xFFFFFFFF).temperature(1).density(1).viscosity(295).gaseous());

    private static FluidRegistrationEntry<Source, Flowing, LiquidBlock, BucketItem> registerLiquid(String name, UnaryOperator<FluidAttributes.Builder> fluidAttributes)
    {
        return FLUIDS.register(name, fluidAttributes.apply(FluidAttributes.builder(new ResourceLocation(Constants.MOD_ID_CORE, "block/liquid/liquid"),
                new ResourceLocation(Constants.MOD_ID_CORE, "block/liquid/liquid_flow"))), Material.WATER);
    }
}
