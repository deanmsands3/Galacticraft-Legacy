package micdoodle8.mods.galacticraft.planets;

import java.util.function.UnaryOperator;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.fluid.FluidRegistrationEntry;
import micdoodle8.mods.galacticraft.core.fluid.GCFluids;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.item.BucketItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid.Flowing;
import net.minecraftforge.fluids.ForgeFlowingFluid.Source;

public class PlanetFluids
{
    public static final Material ACID_MATERIAL = new Material(MaterialColor.EMERALD, true, false, false, false, true, false, true, PushReaction.DESTROY);

    public static final FluidRegistrationEntry<Source, Flowing, FlowingFluidBlock, BucketItem> GAS_METHANE = registerLiquid("methane", fluidAttributes -> fluidAttributes.color(0xFF111111).density(1).viscosity(11).temperature(295).gaseous());
    public static final FluidRegistrationEntry<Source, Flowing, FlowingFluidBlock, BucketItem> GAS_ATMOSPHERIC = registerLiquid("atmospheric_gases", fluidAttributes -> fluidAttributes.color(0xFF111111).density(1).viscosity(13).temperature(295).gaseous());
    public static final FluidRegistrationEntry<Source, Flowing, FlowingFluidBlock, BucketItem> GAS_NITROGEN = registerLiquid("nitrogen", fluidAttributes -> fluidAttributes.color(0xFF111111).density(1).viscosity(12).temperature(295).gaseous());
    public static final FluidRegistrationEntry<Source, Flowing, FlowingFluidBlock, BucketItem> GAS_CARBON_DIOXIDE = registerLiquid("carbondioxide", fluidAttributes -> fluidAttributes.color(0xFF111111).density(2).viscosity(20).temperature(295).gaseous());
    public static final FluidRegistrationEntry<Source, Flowing, FlowingFluidBlock, BucketItem> GAS_ARGON = registerLiquid("argon", fluidAttributes -> fluidAttributes.color(0xFF111111).density(1).viscosity(4).temperature(295).gaseous());
    public static final FluidRegistrationEntry<Source, Flowing, FlowingFluidBlock, BucketItem> GAS_HELIUM = registerLiquid("helium", fluidAttributes -> fluidAttributes.color(0xFF111111).density(1).viscosity(1).temperature(295).gaseous());

    public static final FluidRegistrationEntry<Source, Flowing, FlowingFluidBlock, BucketItem> LIQUID_METHANE = registerLiquid("liquid_methane", fluidAttributes -> fluidAttributes.color(0xFF111111).density(450).viscosity(120).temperature(109));
    public static final FluidRegistrationEntry<Source, Flowing, FlowingFluidBlock, BucketItem> LIQUID_OXYGEN = registerLiquid("liquid_oxygen", fluidAttributes -> fluidAttributes.color(0xFF111111).density(1141).viscosity(140).temperature(90));
    public static final FluidRegistrationEntry<Source, Flowing, FlowingFluidBlock, BucketItem> LIQUID_NITROGEN = registerLiquid("liquid_nitrogen", fluidAttributes -> fluidAttributes.color(0xFF111111).density(808).viscosity(130).temperature(90));
    public static final FluidRegistrationEntry<Source, Flowing, FlowingFluidBlock, BucketItem> LIQUID_ARGON = registerLiquid("liquid_argon", fluidAttributes -> fluidAttributes.color(0xFF111111).density(900).viscosity(100).temperature(87));
    public static final FluidRegistrationEntry<Source, Flowing, FlowingFluidBlock, BucketItem> LIQUID_SULPHURIC_ACID = registerLiquid("sulphuric_acid", "sulphuric_acid_still", "sulphuric_acid_flow", fluidAttributes -> fluidAttributes.color(0xFF111111).density(6229).viscosity(1400));
    public static final FluidRegistrationEntry<Source, Flowing, FlowingFluidBlock, BucketItem> LIQUID_BACTERIAL_SLUDGE = registerLiquid("bacterial_sludge", "bacterial_sludge_still", "bacterial_sludge_flow", fluidAttributes -> fluidAttributes.color(0xFF111111).density(800).viscosity(1500), ACID_MATERIAL);

    private static FluidRegistrationEntry<Source, Flowing, FlowingFluidBlock, BucketItem> registerLiquid(String name, UnaryOperator<FluidAttributes.Builder> fluidAttributes)
    {
        return registerLiquid(name, name, name, fluidAttributes);
    }

    private static FluidRegistrationEntry<Source, Flowing, FlowingFluidBlock, BucketItem> registerLiquid(String name, String stillTexture, String flowTexture, UnaryOperator<FluidAttributes.Builder> fluidAttributes)
    {
        return GCFluids.FLUIDS.register(name, fluidAttributes.apply(FluidAttributes.builder(new ResourceLocation(Constants.MOD_ID_PLANETS, "block/" + stillTexture), new ResourceLocation(Constants.MOD_ID_PLANETS, "block/" + flowTexture))), Material.WATER);
    }

    private static FluidRegistrationEntry<Source, Flowing, FlowingFluidBlock, BucketItem> registerLiquid(String name, String stillTexture, String flowTexture, UnaryOperator<FluidAttributes.Builder> fluidAttributes, Material blockMaterial)
    {
        return GCFluids.FLUIDS.register(name, fluidAttributes.apply(FluidAttributes.builder(new ResourceLocation(Constants.MOD_ID_PLANETS, "block/" + stillTexture), new ResourceLocation(Constants.MOD_ID_PLANETS, "block/" + flowTexture))), blockMaterial);
    }
}