package micdoodle8.mods.galacticraft.core.fluid;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Credit to pupnewfster from the Mekanism project
 */
@ParametersAreNonnullByDefault
public class FluidRegistrationEntry<STILL extends Fluid, FLOWING extends Fluid, BLOCK extends LiquidBlock, BUCKET extends Item>
{
    private RegistryObject<STILL> stillRO;
    private RegistryObject<FLOWING> flowingRO;
    private RegistryObject<BLOCK> blockRO;
    private RegistryObject<BUCKET> bucketRO;

    public FluidRegistrationEntry(String name)
    {
        this.stillRO = RegistryObject.of(new ResourceLocation(Constants.MOD_ID_CORE, name), ForgeRegistries.FLUIDS);
        this.flowingRO = RegistryObject.of(new ResourceLocation(Constants.MOD_ID_CORE, "flowing_" + name), ForgeRegistries.FLUIDS);
        this.blockRO = RegistryObject.of(new ResourceLocation(Constants.MOD_ID_CORE, name), ForgeRegistries.BLOCKS);
        this.bucketRO = RegistryObject.of(new ResourceLocation(Constants.MOD_ID_CORE, name + "_bucket"), ForgeRegistries.ITEMS);
    }

    public STILL getStillFluid()
    {
        return stillRO.get();
    }

    public FLOWING getFlowingFluid()
    {
        return flowingRO.get();
    }

    public BLOCK getBlock()
    {
        return blockRO.get();
    }

    public BUCKET getBucket()
    {
        return bucketRO.get();
    }

    //Make sure these update methods are package local as only the FluidDeferredRegister should be messing with them
    void updateStill(RegistryObject<STILL> stillRO)
    {
        this.stillRO = Objects.requireNonNull(stillRO);
    }

    void updateFlowing(RegistryObject<FLOWING> flowingRO)
    {
        this.flowingRO = Objects.requireNonNull(flowingRO);
    }

    void updateBlock(RegistryObject<BLOCK> blockRO)
    {
        this.blockRO = Objects.requireNonNull(blockRO);
    }

    void updateBucket(RegistryObject<BUCKET> bucketRO)
    {
        this.bucketRO = Objects.requireNonNull(bucketRO);
    }

    @Nonnull
    public STILL getFluid()
    {
        //Default our fluid to being the still variant
        return getStillFluid();
    }
}