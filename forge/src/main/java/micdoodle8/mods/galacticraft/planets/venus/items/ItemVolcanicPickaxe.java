package micdoodle8.mods.galacticraft.planets.venus.items;

import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.item.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CommandBlock;
import net.minecraft.world.level.block.StructureBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.List;

import javax.annotation.Nullable;

public class ItemVolcanicPickaxe extends PickaxeItem implements ISortable, IShiftDescription
{
    public ItemVolcanicPickaxe(Item.Properties builder)
    {
        super(EnumItemTierVenus.VOLCANIC_TOOL, 1, -2.8F, builder);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        if (this.showDescription(stack))
        {
            if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 340))
            {
                List<String> descString = Minecraft.getInstance().font.split(this.getShiftDescription(stack), 150);
                for (String string : descString)
                {
                    tooltip.add(new TextComponent(string));
                }
            }
            else
            {
                tooltip.add(new TextComponent(GCCoreUtil.translateWithFormat("item_desc.shift", Minecraft.getInstance().options.keyShift.getTranslatedKeyMessage())));
            }
        }
    }

//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.TOOLS;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving)
    {
        boolean ret = super.mineBlock(stack, worldIn, state, pos, entityLiving);

        if (!(entityLiving instanceof Player) || worldIn.isClientSide)
        {
            return ret;
        }

        Player player = (Player) entityLiving;
        Direction facing = entityLiving.getDirection();

        if (entityLiving.xRot < -45.0F)
        {
            facing = Direction.UP;
        }
        else if (entityLiving.xRot > 45.0F)
        {
            facing = Direction.DOWN;
        }

        boolean yAxis = facing.getAxis() == Direction.Axis.Y;
        boolean xAxis = facing.getAxis() == Direction.Axis.X;

        for (int i = -1; i <= 1; ++i)
        {
            for (int j = -1; j <= 1 && !stack.isEmpty(); ++j)
            {
                if (i == 0 && j == 0)
                {
                    continue;
                }

                BlockPos pos1;
                if (yAxis)
                {
                    pos1 = pos.offset(i, 0, j);
                }
                else if (xAxis)
                {
                    pos1 = pos.offset(0, i, j);
                }
                else
                {
                    pos1 = pos.offset(i, j, 0);
                }

                //:Replicate logic of PlayerInteractionManager.tryHarvestBlock(pos1)
                BlockState state1 = worldIn.getBlockState(pos1);
                float f = state1.getDestroySpeed(worldIn, pos1);
                if (f >= 0F)
                {
                    BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(worldIn, pos1, state1, player);
                    MinecraftForge.EVENT_BUS.post(event);
                    if (!event.isCanceled())
                    {
                        Block block = state1.getBlock();
                        if ((block instanceof CommandBlock || block instanceof StructureBlock) && !player.canUseGameMasterBlocks())
                        {
                            worldIn.sendBlockUpdated(pos1, state1, state1, 3);
                            continue;
                        }
                        BlockEntity tileentity = worldIn.getBlockEntity(pos1);
                        if (tileentity != null)
                        {
                            Packet<?> pkt = tileentity.getUpdatePacket();
                            if (pkt != null)
                            {
                                ((ServerPlayer) player).connection.send(pkt);
                            }
                        }

                        boolean canHarvest = block.canHarvestBlock(state1, worldIn, pos1, player);
                        boolean destroyed = block.removedByPlayer(state1, worldIn, pos1, player, canHarvest, worldIn.getFluidState(pos1));
                        if (destroyed)
                        {
                            block.destroy(worldIn, pos1, state1);
                        }
                        if (canHarvest && destroyed)
                        {
                            block.playerDestroy(worldIn, player, pos1, state1, tileentity, stack);
                            stack.hurtAndBreak(1, player, (e) ->
                            {
                            });
                        }
                    }
                }
            }
        }

        return ret;
    }

    @Override
    public String getShiftDescription(ItemStack stack)
    {
        return GCCoreUtil.translate(this.getDescriptionId() + ".description");
    }

    @Override
    public boolean showDescription(ItemStack stack)
    {
        return true;
    }
}
