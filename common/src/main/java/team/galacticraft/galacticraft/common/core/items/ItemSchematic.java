package team.galacticraft.galacticraft.common.core.items;

import team.galacticraft.galacticraft.common.api.recipe.ISchematicItem;
import team.galacticraft.galacticraft.common.api.recipe.SchematicRegistry;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.GCItems;
import team.galacticraft.galacticraft.common.core.GalacticraftCore;
import team.galacticraft.galacticraft.common.core.entities.EntityHangingSchematic;
import team.galacticraft.galacticraft.common.core.entities.GCEntities;
import team.galacticraft.galacticraft.common.core.proxy.ClientProxyCore;
import team.galacticraft.galacticraft.common.core.util.EnumColor;
import team.galacticraft.galacticraft.common.core.util.EnumSortCategory;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.item.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.*;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.List;

import org.jetbrains.annotations.Nullable;

public class ItemSchematic extends Item implements ISchematicItem, ISortable
{
    public ItemSchematic(Properties builder)
    {
        super(builder);
//        super(EntityHangingSchematic.class);
//        this.setMaxDamage(0);
//        this.setHasSubtypes(true);
//        this.setMaxStackSize(1);
//        this.setUnlocalizedName(assetName);
    }

//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }

//    @Override
//    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        if (tab == GalacticraftCore.galacticraftItemsTab || tab == ItemGroup.SEARCH)
//        {
//            for (int i = 0; i < 2; i++)
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

//    @Override
//    public int getMetadata(int par1)
//    {
//        return par1;
//    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        if (stack.getItem() == GCItems.schematicBuggy)
        {
            tooltip.add(new TranslatableComponent(("schematic.moonbuggy")));
        }
        else if (stack.getItem() == GCItems.schematicRocketT2)
        {
            tooltip.add(new TranslatableComponent(("schematic.rocket_t2")));

            if (!GalacticraftCore.isPlanetsLoaded)
            {
                tooltip.add(new TextComponent(EnumColor.DARK_AQUA + "\"Galacticraft: Planets\" Not Installed!"));
            }
        }
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.SCHEMATIC;
    }

    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        ItemStack stack = context.getPlayer().getItemInHand(context.getHand());
        BlockPos blockpos = context.getClickedPos().relative(context.getClickedFace());
        Direction facing = context.getClickedFace();

        if (facing != Direction.DOWN && facing != Direction.UP && context.getPlayer().mayUseItemAt(blockpos, facing, stack))
        {
            EntityHangingSchematic entityhanging = this.createEntity(context.getLevel(), blockpos, facing, this.getIndex(stack.getDamageValue()));

            if (entityhanging != null && entityhanging.survives())
            {
                if (!context.getLevel().isClientSide)
                {
                    entityhanging.playPlacementSound();
                    context.getLevel().addFreshEntity(entityhanging);
                    entityhanging.sendToClient(context.getLevel(), blockpos);
                }

                stack.shrink(1);
            }

            return InteractionResult.SUCCESS;
        }
        else
        {
            return InteractionResult.FAIL;
        }
    }

    private EntityHangingSchematic createEntity(Level worldIn, BlockPos pos, Direction clickedSide, int index)
    {
        return new EntityHangingSchematic(GCEntities.HANGING_SCHEMATIC, worldIn, pos, clickedSide, index);
    }

    /**
     * Higher tiers should override - see ItemSchematicTier2 for example
     **/
    protected int getIndex(int damage)
    {
        return damage;
    }

    /**
     * Make sure the number of these will match the index values
     */
    public static void registerSchematicItems()
    {
        SchematicRegistry.registerSchematicItem(new ItemStack(GCItems.schematicBuggy, 1));
        SchematicRegistry.registerSchematicItem(new ItemStack(GCItems.schematicRocketT2, 1));
    }

    /**
     * Make sure the order of these will match the index values
     */
    @Environment(EnvType.CLIENT)
    public static void registerTextures()
    {
        SchematicRegistry.registerTexture(new ResourceLocation(Constants.MOD_ID_CORE, "textures/items/schematic_buggy.png"));
        SchematicRegistry.registerTexture(new ResourceLocation(Constants.MOD_ID_CORE, "textures/items/schematic_rocket_t2.png"));
    }

    @Override
    public String getDescriptionId(ItemStack stack)
    {
        return "item.galacticraftcore.schematic";
    }
}
