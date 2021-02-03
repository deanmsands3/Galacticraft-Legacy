package micdoodle8.mods.galacticraft.planets.asteroids.items;

import micdoodle8.mods.galacticraft.api.item.IHoldableItem;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.dimension.DimensionSpaceStation;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFake;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.ConfigManagerPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.AstroMinerEntity;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityMinerBase;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemAstroMiner extends Item implements IHoldableItem, ISortable
{
    public ItemAstroMiner(Item.Properties properties)
    {
        super(properties);
//        this.setMaxDamage(0);
//        this.setMaxStackSize(1);
//        this.setUnlocalizedName(assetName);
        //this.setTextureName("arrow");
    }

    @Override
    @OnlyIn(Dist.CLIENT)
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
        BlockEntity tile = null;
        Player playerIn = context.getPlayer();
        Level worldIn = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (playerIn == null)
        {
            return ActionResultType.PASS;
        }
        else
        {
            final Block id = worldIn.getBlockState(pos).getBlock();

            if (id == GCBlocks.MULTI_BLOCK)
            {
                tile = worldIn.getBlockEntity(pos);

                if (tile instanceof TileEntityFake)
                {
                    tile = ((TileEntityFake) tile).getMainBlockTile();
                }
            }

            if (id == AsteroidBlocks.FULL_ASTRO_MINER_BASE)
            {
                tile = worldIn.getBlockEntity(pos);
            }

            if (tile instanceof TileEntityMinerBase)
            {
                //Don't open GUI on client
                if (worldIn.isClientSide)
                {
                    return ActionResultType.FAIL;
                }

                if (worldIn.dimension instanceof DimensionSpaceStation)
                {
                    playerIn.sendMessage(new TextComponent(GCCoreUtil.translate("gui.message.astro_miner7.fail")));
                    return ActionResultType.FAIL;
                }

                if (((TileEntityMinerBase) tile).getLinkedMiner() != null)
                {
                    playerIn.sendMessage(new TextComponent(GCCoreUtil.translate("gui.message.astro_miner.fail")));
                    return ActionResultType.FAIL;
                }

                //Gives a chance for any loaded Astro Miner to link itself
                if (((TileEntityMinerBase) tile).ticks < 15)
                {
                    return ActionResultType.FAIL;
                }

                ServerPlayer playerMP = (ServerPlayer) playerIn;
                GCPlayerStats stats = GCPlayerStats.get(playerIn);

                int astroCount = stats.getAstroMinerCount();
                if (astroCount >= ConfigManagerPlanets.astroMinerMax.get() && (!playerIn.abilities.instabuild))
                {
                    playerIn.sendMessage(new TextComponent(GCCoreUtil.translate("gui.message.astro_miner2.fail")));
                    return ActionResultType.FAIL;
                }

                if (!((TileEntityMinerBase) tile).spawnMiner(playerMP))
                {
                    playerIn.sendMessage(new TextComponent(GCCoreUtil.translate("gui.message.astro_miner1.fail") + " " + GCCoreUtil.translate(AstroMinerEntity.blockingBlock.toString())));
                    return ActionResultType.FAIL;
                }

                if (!playerIn.abilities.instabuild)
                {
                    stats.setAstroMinerCount(stats.getAstroMinerCount() + 1);
                    playerIn.getItemInHand(context.getHand()).shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void addInformation(ItemStack par1ItemStack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
//    {
//        //TODO
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
        return EnumSortCategory.GENERAL;
    }
}
