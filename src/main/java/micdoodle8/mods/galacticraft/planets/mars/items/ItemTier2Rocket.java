package micdoodle8.mods.galacticraft.planets.mars.items;

import micdoodle8.mods.galacticraft.api.entity.IRocketType;
import micdoodle8.mods.galacticraft.api.entity.IRocketType.EnumRocketType;
import micdoodle8.mods.galacticraft.api.item.IHoldableItem;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.fluid.GCFluids;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityLandingPad;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityCargoRocket;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityTier2Rocket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTier2Rocket extends Item implements IHoldableItem, ISortable
{
    public ItemTier2Rocket(Item.Properties properties)
    {
        super(properties);
//        this.setMaxDamage(0);
//        this.setHasSubtypes(true);
//        this.setMaxStackSize(1);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }


    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context)
    {
        boolean padFound = false;
        BlockEntity tile = null;

        if (context.getLevel().isClientSide)
        {
            return InteractionResult.PASS;
        }
        else
        {
            float centerX = -1;
            float centerY = -1;
            float centerZ = -1;

            for (int i = -1; i < 2; i++)
            {
                for (int j = -1; j < 2; j++)
                {
                    BlockPos pos1 = context.getClickedPos().offset(i, 0, j);
                    BlockState state = context.getLevel().getBlockState(pos1);

                    if (state.getBlock() == GCBlocks.landingPadFull)
                    {
                        padFound = true;
                        tile = context.getLevel().getBlockEntity(pos1);

                        centerX = context.getClickedPos().getX() + i + 0.5F;
                        centerY = context.getClickedPos().getY() + 0.4F;
                        centerZ = context.getClickedPos().getZ() + j + 0.5F;

                        break;
                    }
                }

                if (padFound)
                {
                    break;
                }
            }

            if (padFound)
            {
                if (!placeRocketOnPad(stack, context.getLevel(), tile, centerX, centerY, centerZ))
                {
                    return InteractionResult.FAIL;
                }

                if (!context.getPlayer().abilities.instabuild)
                {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
            else
            {
                return InteractionResult.PASS;
            }
        }
    }

    public boolean placeRocketOnPad(ItemStack stack, Level world, BlockEntity tile, float centerX, float centerY, float centerZ)
    {
        //Check whether there is already a rocket on the pad
        if (tile instanceof TileEntityLandingPad)
        {
            if (((TileEntityLandingPad) tile).getDockedEntity() != null)
            {
                return false;
            }
        }
        else
        {
            return false;
        }

        EntityAutoRocket rocket;

        if (this == MarsItems.rocketTierTwo || this == MarsItems.rocketTierTwoCargo1 || this == MarsItems.rocketTierTwoCargo2 || this == MarsItems.rocketTierTwoCargo3 || this == MarsItems.rocketTierTwoCreative)
        {
            rocket = EntityTier2Rocket.createEntityTier2Rocket(world, centerX, centerY, centerZ, EntityTier2Rocket.getTypeFromItem(this));
        }
        else
        {
            rocket = EntityCargoRocket.createEntityCargoRocket(world, centerX, centerY, centerZ, EntityCargoRocket.getTypeFromItem(this));
        }

        rocket.setPos(rocket.getX(), rocket.getY() + rocket.getOnPadYOffset(), rocket.getZ());
        world.addFreshEntity(rocket);

        if (((IRocketType) rocket).getRocketType().getPreFueled())
        {
            if (rocket instanceof EntityTieredRocket)
            {
                ((EntityTieredRocket) rocket).fuelTank.fill(new FluidStack(GCFluids.FUEL.getFluid(), rocket.getMaxFuel()), IFluidHandler.FluidAction.EXECUTE);
            }
            else
            {
                ((EntityCargoRocket) rocket).fuelTank.fill(new FluidStack(GCFluids.FUEL.getFluid(), rocket.getMaxFuel()), IFluidHandler.FluidAction.EXECUTE);
            }
        }
        else if (stack.hasTag() && stack.getTag().contains("RocketFuel"))
        {
            rocket.fuelTank.fill(new FluidStack(GCFluids.FUEL.getFluid(), stack.getTag().getInt("RocketFuel")), IFluidHandler.FluidAction.EXECUTE);
        }

        return true;
    }

//    @Override
//    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        if (tab == GalacticraftCore.galacticraftItemsTab || tab == ItemGroup.SEARCH)
//        {
//            for (int i = 0; i < EnumRocketType.values().length; i++)
//            {
//                list.add(new ItemStack(this, 1, i));
//            }
//
//            for (int i = 11; i < 10 + EnumRocketType.values().length; i++)
//            {
//                list.add(new ItemStack(this, 1, i));
//            }
//        }
//    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendHoverText(ItemStack par1ItemStack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        EnumRocketType type;

        if (par1ItemStack.getDamageValue() < 10)
        {
            type = EnumRocketType.values()[par1ItemStack.getDamageValue()];
        }
        else
        {
            type = EnumRocketType.values()[par1ItemStack.getDamageValue() - 10];
        }

        if (!type.getTooltip().getColoredString().isEmpty())
        {
            tooltip.add(type.getTooltip());
        }

        if (type.getPreFueled())
        {
            tooltip.add(new TextComponent(EnumColor.RED + "\u00a7o" + GCCoreUtil.translate("gui.creative_only.desc")));
        }

        if (par1ItemStack.hasTag() && par1ItemStack.getTag().contains("RocketFuel"))
        {
            EntityAutoRocket rocket;

            if (par1ItemStack.getDamageValue() < 10)
            {
                rocket = EntityTier2Rocket.createEntityTier2Rocket(Minecraft.getInstance().level, 0, 0, 0, EnumRocketType.values()[par1ItemStack.getDamageValue()]);
            }
            else
            {
                rocket = EntityCargoRocket.createEntityCargoRocket(Minecraft.getInstance().level, 0, 0, 0, EnumRocketType.values()[par1ItemStack.getDamageValue() - 10]);
            }

            tooltip.add(new TextComponent(GCCoreUtil.translate("gui.message.fuel") + ": " + par1ItemStack.getTag().getInt("RocketFuel") + " / " + rocket.fuelTank.getCapacity()));
        }

        if (par1ItemStack.getDamageValue() >= 10)
        {
            tooltip.add(new TextComponent(EnumColor.AQUA + GCCoreUtil.translate("gui.requires_controller.desc")));
        }
    }

//    @Override
//    public String getUnlocalizedName(ItemStack par1ItemStack)
//    {
//        return super.getUnlocalizedName(par1ItemStack) + (par1ItemStack.getDamage() < 10 ? ".t2Rocket" : ".cargo_rocket");
//    }

    @Override
    public boolean shouldHoldLeftHandUp(Player player)
    {
        return true;
    }

    @Override
    public boolean shouldHoldRightHandUp(Player player)
    {
        return true;
    }

    @Override
    public boolean shouldCrouch(Player player)
    {
        return true;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.ROCKET;
    }
}
