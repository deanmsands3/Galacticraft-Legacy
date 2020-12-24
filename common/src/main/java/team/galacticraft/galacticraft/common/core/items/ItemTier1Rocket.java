package team.galacticraft.galacticraft.common.core.items;

import team.galacticraft.galacticraft.common.api.entity.IRocketType.EnumRocketType;
import team.galacticraft.galacticraft.common.api.item.IHoldableItem;
import team.galacticraft.galacticraft.common.core.GCBlocks;
import team.galacticraft.galacticraft.common.core.entities.EntityTier1Rocket;
import team.galacticraft.galacticraft.common.core.entities.GCEntities;
import team.galacticraft.galacticraft.common.core.fluid.GCFluids;
import team.galacticraft.galacticraft.common.core.proxy.ClientProxyCore;
import team.galacticraft.galacticraft.common.core.tile.TileEntityLandingPad;
import team.galacticraft.galacticraft.common.core.util.EnumColor;
import team.galacticraft.galacticraft.common.core.util.EnumSortCategory;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
import me.shedaniel.architectury.fluid.FluidStack;
import team.galacticraft.galacticraft.common.compat.fluid.ActionType;

import org.jetbrains.annotations.Nullable;
import java.util.List;

public class ItemTier1Rocket extends Item implements IHoldableItem, ISortable
{
    public ItemTier1Rocket(Properties properties)
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
            return InteractionResult.SUCCESS;
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

        final EntityTier1Rocket spaceship = GCEntities.ROCKET_T1.create(worldIn);
        spaceship.setPos(centerX, centerY, centerZ);
        spaceship.rocketType = EntityTier1Rocket.getTypeFromItem(stack.getItem());

        spaceship.setPos(spaceship.getX(), spaceship.getY() + spaceship.getOnPadYOffset(), spaceship.getZ());
        worldIn.addFreshEntity(spaceship);

        if (spaceship.rocketType.getPreFueled())
        {
            spaceship.fuelTank.fill(FluidStack.create(GCFluids.FUEL.getFluid(), spaceship.getMaxFuel()), ActionType.PERFORM);
        }
        else if (stack.hasTag() && stack.getTag().contains("RocketFuel"))
        {
            spaceship.fuelTank.fill(FluidStack.create(GCFluids.FUEL.getFluid(), stack.getTag().getInt("RocketFuel")), ActionType.PERFORM);
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
    @Environment(EnvType.CLIENT)
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
            tooltip.add(new TextComponent(EnumColor.RED + "\u00a7o" + I18n.get("gui.creative_only.desc")));
        }

        if (stack.hasTag() && stack.getTag().contains("RocketFuel"))
        {
            tooltip.add(new TranslatableComponent(("gui.message.fuel") + ": " + stack.getTag().getInt("RocketFuel") + " / " + EntityTier1Rocket.FUEL_CAPACITY));
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
