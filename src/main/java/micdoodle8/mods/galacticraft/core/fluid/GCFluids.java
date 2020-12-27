package micdoodle8.mods.galacticraft.core.fluid;

import java.util.function.UnaryOperator;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BucketItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid.Flowing;
import net.minecraftforge.fluids.ForgeFlowingFluid.Source;

public class GCFluids
{
    public static final GCFluidRegistry FLUIDS = new GCFluidRegistry();

    public static final FluidRegistrationEntry<Source, Flowing, FlowingFluidBlock, BucketItem> OIL = registerLiquid("oil", "oil_still", "oil_flow", fluidAttributes -> fluidAttributes.color(0xFF111111).density(800).viscosity(1500));
    public static final FluidRegistrationEntry<Source, Flowing, FlowingFluidBlock, BucketItem> FUEL = registerLiquid("fuel", "fuel_still", "fuel_flow", fluidAttributes -> fluidAttributes.color(0xFFDBDF16).density(400).viscosity(900));
    public static final FluidRegistrationEntry<Source, Flowing, FlowingFluidBlock, BucketItem> OXYGEN = registerLiquid("oxygen", "oxygen_gas", fluidAttributes -> fluidAttributes.color(0xFF6CE2FF).temperature(1).density(13).viscosity(295).gaseous());
    public static final FluidRegistrationEntry<Source, Flowing, FlowingFluidBlock, BucketItem> HYDROGEN = registerLiquid("hydrogen", "hydrogen_gas", fluidAttributes -> fluidAttributes.color(0xFFFFFFFF).temperature(1).density(1).viscosity(295).gaseous());

    private static FluidRegistrationEntry<Source, Flowing, FlowingFluidBlock, BucketItem> registerLiquid(String name, String texture, UnaryOperator<FluidAttributes.Builder> fluidAttributes)
    {
        return registerLiquid(name, texture, texture, fluidAttributes);
    }

    private static FluidRegistrationEntry<Source, Flowing, FlowingFluidBlock, BucketItem> registerLiquid(String name, String stillTexture, String flowTexture, UnaryOperator<FluidAttributes.Builder> fluidAttributes)
    {
        return FLUIDS.register(name, fluidAttributes.apply(FluidAttributes.builder(new ResourceLocation(Constants.MOD_ID_CORE, "block/" + stillTexture), new ResourceLocation(Constants.MOD_ID_CORE, "block/" + flowTexture))), Material.WATER);
    }
}