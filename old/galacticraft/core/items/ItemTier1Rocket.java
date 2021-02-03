package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.api.entity.IRocketType.EnumRocketType;
import micdoodle8.mods.galacticraft.api.item.IHoldableItem;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.entities.Tier1RocketEntity;
import micdoodle8.mods.galacticraft.core.entities.GCEntities;
import micdoodle8.mods.galacticraft.core.fluid.GCFluids;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityLandingPad;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.player.LocalPlayer;
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

public class ItemTier1Rocket extends Item implements IHoldableItem, ISortable
{
    public ItemTier1Rocket(Item.Properties properties)
    {
        super(properties);
//        this.setMaxDamage(0);
//        this.setHasSubtypes(true);
//        this.setMaxStackSize(1);
        //this.setTextureName("arrow");
//        this.setUnlocalizedName(assetName);
    }

//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }


    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        ItemStack stack = context.getPlayer().getItemInHand(context.getHand());
        boolean padFound = false;
        BlockEntity tile = null;

        if (context.getLevel().isClientSide && context.getPlayer() instanceof LocalPlayer)
        {
            ClientProxyCore.playerClientHandler.onBuild(8, (LocalPlayer) context.getPlayer());
            return ActionResultType.SUCCESS;
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

                    if (state.getBlock() == GCBlocks.FULL_ROCKET_LAUNCH_PAD)
                    {
                        padFound = true;
                        tile = context.getLevel().getBlockEntity(context.getClickedPos().offset(i, 0, j));

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
                    return ActionResultType.FAIL;
                }

                if (!context.getPlayer().abilities.instabuild)
                {
                    stack.shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
            else
            {
                return ActionResultType.PASS;
            }
        }
    }

    public static boolean placeRocketOnPad(ItemStack stack, Level worldIn, BlockEntity tile, float centerX, float centerY, float centerZ)
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

        final Tier1RocketEntity spaceship = GCEntities.TIER_1_ROCKET.create(worldIn);
        spaceship.setPos(centerX, centerY, centerZ);
        spaceship.rocketType = Tier1RocketEntity.getTypeFromItem(stack.getItem());

        spaceship.setPos(spaceship.getX(), spaceship.getY() + spaceship.getOnPadYOffset(), spaceship.getZ());
        worldIn.addFreshEntity(spaceship);

        if (spaceship.rocketType.getPreFueled())
        {
            spaceship.fuelTank.fill(new FluidStack(GCFluids.FUEL.getFluid(), spaceship.getMaxFuel()), IFluidHandler.FluidAction.EXECUTE);
        }
        else if (stack.hasTag() && stack.getTag().contains("RocketFuel"))
        {
            spaceship.fuelTank.fill(new FluidStack(GCFluids.FUEL.getFluid(), stack.getTag().getInt("RocketFuel")), IFluidHandler.FluidAction.EXECUTE);
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
//        }
//    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        EnumRocketType type = EnumRocketType.values()[stack.getDamageValue()];

        if (!type.getTooltip().getColoredString().isEmpty())
        {
            tooltip.add(type.getTooltip());
        }

        if (type.getPreFueled())
        {
            tooltip.add(new TextComponent(EnumColor.RED + "\u00a7o" + GCCoreUtil.translate("gui.creative_only.desc")));
        }

        if (stack.hasTag() && stack.getTag().contains("RocketFuel"))
        {
            tooltip.add(new TextComponent(GCCoreUtil.translate("gui.message.fuel") + ": " + stack.getTag().getInt("RocketFuel") + " / " + Tier1RocketEntity.FUEL_CAPACITY));
        }
    }

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
