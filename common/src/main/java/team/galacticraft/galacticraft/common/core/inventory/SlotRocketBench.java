package team.galacticraft.galacticraft.common.core.inventory;

import java.util.List;

import team.galacticraft.galacticraft.common.api.GalacticraftRegistry;
import team.galacticraft.galacticraft.common.api.recipe.INasaWorkbenchRecipe;
import team.galacticraft.galacticraft.core.network.PacketSimple;
import team.galacticraft.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import team.galacticraft.galacticraft.core.util.GCCoreUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SlotRocketBench extends Slot
{
    private final int index;
    //    private final BlockPos pos;
    private final Player player;

    public SlotRocketBench(Container par2IInventory, int par3, int par4, int par5, Player player)
    {
        super(par2IInventory, par3, par4, par5);
        this.index = par3;
//        this.pos = pos;
        this.player = player;
    }

    @Override
    public void setChanged()
    {
        if (this.player instanceof ServerPlayer)
        {
//            DimensionType dimID = GCCoreUtil.getDimensionID(this.player.world);
//            GCCoreUtil.sendToAllAround(new PacketSimple(EnumSimplePacket.C_SPAWN_SPARK_PARTICLES, dimID, new Object[] { this.pos }), this.player.world, dimID, this.pos, 20); TODO Spark particles
        }
    }

    @Override
    public boolean mayPlace(ItemStack par1ItemStack)
    {
        if (par1ItemStack == null)
        {
            return false;
        }

        List<INasaWorkbenchRecipe> recipes = GalacticraftRegistry.getRocketT1Recipes();
        for (INasaWorkbenchRecipe recipe : recipes)
        {
            if (ItemStack.isSame(par1ItemStack, recipe.getRecipeInput().get(this.index)))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the maximum stack size for a given slot (usually the same as
     * getInventoryStackLimit(), but 1 in the case of armor slots)
     */
    @Override
    public int getMaxStackSize()
    {
        return 1;
    }
}
