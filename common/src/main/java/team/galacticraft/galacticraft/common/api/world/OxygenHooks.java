package team.galacticraft.galacticraft.common.api.world;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.Dimension;
import net.minecraft.world.phys.AABB;

public class OxygenHooks
{
    private static Method combusionTestMethod;
    private static Method breathableAirBlockMethod;
    private static Method breathableAirBlockEntityMethod;
    private static Method torchHasOxygenMethod;
    private static Method oxygenBubbleMethod;
    private static Method validOxygenSetupMethod;

    static
    {
        try
        {
            Class<?> oxygenUtilClass = Class.forName("team.galacticraft.galacticraft.common.core.util.OxygenUtil");
            combusionTestMethod = oxygenUtilClass.getDeclaredMethod("noAtmosphericCombustion", Dimension.class);
            breathableAirBlockMethod = oxygenUtilClass.getDeclaredMethod("isAABBInBreathableAirBlock", Level.class, AABB.class);
            breathableAirBlockEntityMethod = oxygenUtilClass.getDeclaredMethod("isAABBInBreathableAirBlock", LivingEntity.class);
            torchHasOxygenMethod = oxygenUtilClass.getDeclaredMethod("checkTorchHasOxygen", Level.class, BlockPos.class);
            oxygenBubbleMethod = oxygenUtilClass.getDeclaredMethod("inOxygenBubble", Level.class, double.class, double.class, double.class);
            validOxygenSetupMethod = oxygenUtilClass.getDeclaredMethod("hasValidOxygenSetup", ServerPlayer.class);
        }
        catch (ClassNotFoundException | NoSuchMethodException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Test whether fire can burn in this world's atmosphere (outside any oxygen bubble).
     *
     * @param dimension The WorldProvider for this dimension
     * @return False if fire burns normally
     * True if fire cannot burn in this world
     */
    public static boolean noAtmosphericCombustion(Dimension dimension)
    {
        if (combusionTestMethod != null)
        {
            try
            {
                return (Boolean) combusionTestMethod.invoke(null, dimension);
            }
            catch (IllegalAccessException | InvocationTargetException e)
            {
                e.printStackTrace();
            }
        }

        return false;
    }


    /**
     * Test whether a bounding box (normally a block but it could be an entity)
     * is inside an oxygen bubble or oxygen sealed space, on an otherwise
     * oxygen-free world.  (Do not use this on the Overworld or other oxygen-rich
     * world,, it will return false negatives!!)
     * <p>
     * NOTE: In a complex build where this block is surrounded by air-permeable blocks
     * on all sides (for example torches, ladders, signs, wires, chests etc etc)
     * then it may have to look quite far to find whether it is in oxygen or not -
     * it will check up to 5 blocks in each direction.  This can impose a performance
     * load in the unlikely event there are permeable blocks in all directions.  It is
     * therefore advisable not to call this every tick: 1 tick in 5 should be plenty.
     *
     * @param world The World
     * @param bb    AxisAligned BB representing the block (e.g. a torch), or maybe the EnvType of a block
     * @return True if the bb is in oxygen, otherwise false.
     */
    public static boolean isAABBInBreathableAirBlock(Level world, AABB bb)
    {
        if (breathableAirBlockMethod != null)
        {
            try
            {
                return (Boolean) breathableAirBlockMethod.invoke(null, world, bb);
            }
            catch (IllegalAccessException | InvocationTargetException e)
            {
                e.printStackTrace();
            }
        }

        return false;
    }


    /**
     * Special version of the oxygen AABB check for living entities.
     * This is based on checking the oxygen contact of a small box centred
     * at the entity's eye height.  The small box has sides equal to half
     * the width of the entity.  This is a good approximation to head size and
     * position for most types of mobs.
     *
     * @param entity
     * @return True if the entity's head is in an oxygen bubble or block, false otherwise
     */
    public static boolean isAABBInBreathableAirBlock(LivingEntity entity)
    {
        if (breathableAirBlockEntityMethod != null)
        {
            try
            {
                return (Boolean) breathableAirBlockEntityMethod.invoke(null, entity);
            }
            catch (IllegalAccessException | InvocationTargetException e)
            {
                e.printStackTrace();
            }
        }

        return false;
    }


    /**
     * Simplified (better performance) version of the block oxygen check
     * for use with torch blocks and other oxygen-requiring blocks
     * which can access oxygen on any EnvType.
     * <p>
     * NOTE:  this does not run an inOxygenBubble() check, you will need to do
     * that also.
     *
     * @param world
     * @param block The block type of this torch being checked - currently unused
     * @param pos   The x, y, z position of the torch
     * @return True if there is a (sealed) oxygen block accessible, otherwise false.
     */
    public static boolean checkTorchHasOxygen(Level world, Block block, BlockPos pos)
    {
        if (torchHasOxygenMethod != null)
        {
            try
            {
                return (Boolean) torchHasOxygenMethod.invoke(null, world, pos);
            }
            catch (IllegalAccessException | InvocationTargetException e)
            {
                e.printStackTrace();
            }
        }

        return false;
    }


    /**
     * Test whether a location is inside an Oxygen Bubble from an Oxygen Distributor
     *
     * @param worldObj World
     * @param avgX     avg X, avgY, avgZ are the average co-ordinates of the location
     * @param avgY     (for example, the central point of a block being tested
     * @param avgZ     or the average position of the centre of a living entity)
     * @return True if it is in an oxygen bubble, otherwise false
     */
    public static boolean inOxygenBubble(Level worldObj, double avgX, double avgY, double avgZ)
    {
        if (oxygenBubbleMethod != null)
        {
            try
            {
                return (Boolean) oxygenBubbleMethod.invoke(null, worldObj, avgX, avgY, avgZ);
            }
            catch (IllegalAccessException | InvocationTargetException e)
            {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * Test whether a player is wearing a working set of Galacticraft
     * oxygen-breathing apparatus (mask, gear + tank)
     *
     * @param player
     * @return True if the setup is valid, otherwise false
     */
    public static boolean hasValidOxygenSetup(ServerPlayer player)
    {
        if (validOxygenSetupMethod != null)
        {
            try
            {
                return (Boolean) validOxygenSetupMethod.invoke(null, player);
            }
            catch (IllegalAccessException | InvocationTargetException e)
            {
                e.printStackTrace();
            }
        }

        return false;
    }
}
