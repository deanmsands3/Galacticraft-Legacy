package team.galacticraft.galacticraft.common.core.util;

import team.galacticraft.galacticraft.common.api.block.IPartialSealableBlock;
import team.galacticraft.galacticraft.common.api.item.IBreathableArmor;
import team.galacticraft.galacticraft.common.api.item.IBreathableArmor.EnumGearType;
import team.galacticraft.galacticraft.common.api.transmission.NetworkType;
import team.galacticraft.galacticraft.common.api.transmission.tile.IConnector;
import team.galacticraft.galacticraft.common.api.vector.BlockVec3;
import team.galacticraft.galacticraft.common.api.vector.BlockVec3Dim;
import team.galacticraft.galacticraft.common.api.world.EnumAtmosphericGas;
import team.galacticraft.galacticraft.common.api.world.IGalacticraftDimension;
import team.galacticraft.galacticraft.common.core.GCBlocks;
import team.galacticraft.galacticraft.common.core.blocks.BlockThermalAir;
import team.galacticraft.galacticraft.common.core.energy.EnergyConfigHandler;
import team.galacticraft.galacticraft.common.api.entity.GCPlayerStats;
import team.galacticraft.galacticraft.common.core.fluid.OxygenPressureProtocol;
import team.galacticraft.galacticraft.common.core.items.ItemCanisterOxygenInfinite;
import team.galacticraft.galacticraft.common.core.items.ItemOxygenGear;
import team.galacticraft.galacticraft.common.core.items.ItemOxygenMask;
import team.galacticraft.galacticraft.common.core.items.ItemOxygenTank;
import team.galacticraft.galacticraft.common.core.tile.TileEntityOxygenDistributor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EnchantmentTableBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.GravelBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SpongeBlock;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.dimension.Dimension;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import java.util.HashSet;

public class OxygenUtil
{
    public enum BreathableAirResult
    {
        NONE,
        BREATHABLE,
        THERMAL_BREATHABLE;

        public boolean isBreathable()
        {
            return this != NONE;
        }

        public boolean isThermal()
        {
            return this == THERMAL_BREATHABLE;
        }
    }

    private static HashSet<BlockPos> checked;

    @Environment(EnvType.CLIENT)
    public static boolean shouldDisplayTankGui(Screen gui)
    {
        if (Minecraft.getInstance().options.hideGui)
        {
            return false;
        }

        if (gui == null)
        {
            return true;
        }

        if (gui instanceof InventoryScreen)
        {
            return false;
        }

        return gui instanceof ChatScreen;

    }

    public static boolean isAABBInBreathableAirBlock(LivingEntity entity)
    {
        return isAABBInBreathableAirBlock(entity, false);
    }

    public static boolean isAABBInBreathableAirBlock(LivingEntity entity, boolean testThermal)
    {
        double y = entity.getY() + entity.getEyeHeight();
        double x = entity.getX();
        double z = entity.getZ();

        double sx = entity.getBoundingBox().maxX - entity.getBoundingBox().minX;
        double sy = entity.getBoundingBox().maxY - entity.getBoundingBox().minY;
        double sz = entity.getBoundingBox().maxZ - entity.getBoundingBox().minZ;

        //A good first estimate of head size is that it's the smallest of the entity's 3 dimensions (e.g. front to back, for Steve)
        double smin = Math.min(sx, Math.min(sy, sz)) / 2;
        double offsetXZ = 0.0;

        // If entity is within air lock wall, check adjacent blocks for oxygen
        // The value is equal to the max distance from an adjacent oxygen block to the edge of a sealer wall
        if (entity.level.getBlockState(entity.getCommandSenderBlockPosition()).getBlock() == GCBlocks.airLockSeal)
        {
            offsetXZ = 0.75F;
        }

        return OxygenUtil.isAABBInBreathableAirBlock(entity.level, new AABB(x - smin - offsetXZ, y - smin, z - smin - offsetXZ, x + smin + offsetXZ, y + smin, z + smin + offsetXZ), testThermal);
    }

    public static boolean isAABBInBreathableAirBlock(Level world, AABB bb)
    {
        return isAABBInBreathableAirBlock(world, bb, false);
    }

    public static boolean isAABBInBreathableAirBlock(Level world, AABB bb, boolean testThermal)
    {
        final double avgX = (bb.minX + bb.maxX) / 2.0D;
        final double avgY = (bb.minY + bb.maxY) / 2.0D;
        final double avgZ = (bb.minZ + bb.maxZ) / 2.0D;

        if (testThermal)
        {
            return OxygenUtil.isInOxygenAndThermalBlock(world, bb);
        }

        if (OxygenUtil.inOxygenBubble(world, avgX, avgY, avgZ))
        {
            return true;
        }

        return OxygenUtil.isInOxygenBlock(world, bb);
    }

    public static boolean isInOxygenBlock(Level world, AABB bb)
    {
        int xm = Mth.floor(bb.minX + 0.001D);
        int xx = Mth.floor(bb.maxX - 0.001D);
        int ym = Mth.floor(bb.minY + 0.001D);
        int yy = Mth.floor(bb.maxY - 0.001D);
        int zm = Mth.floor(bb.minZ + 0.001D);
        int zz = Mth.floor(bb.maxZ - 0.001D);

        OxygenUtil.checked = new HashSet<>();
        if (world.hasChunksAt(new BlockPos(xm, ym, zm), new BlockPos(xx, yy, zz)))
        {
            for (int x = xm; x <= xx; ++x)
            {
                for (int z = zm; z <= zz; ++z)
                {
                    for (int y = ym; y <= yy; ++y)
                    {
                        BlockPos pos = new BlockPos(x, y, z);
                        Block block = world.getBlockState(pos).getBlock();
                        if (OxygenUtil.testContactWithBreathableAir(world, block, pos, 0).isBreathable())
                        {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public static boolean isInOxygenAndThermalBlock(Level world, AABB bb)
    {
        int i = Mth.floor(bb.minX + 0.001D);
        int j = Mth.floor(bb.maxX - 0.001D);
        int k = Mth.floor(bb.minY + 0.001D);
        int l = Mth.floor(bb.maxY - 0.001D);
        int i1 = Mth.floor(bb.minZ + 0.001D);
        int j1 = Mth.floor(bb.maxZ - 0.001D);

        OxygenUtil.checked = new HashSet<>();
        if (world.hasChunksAt(new BlockPos(i, k, i1), new BlockPos(j, l, j1)))
        {
            for (int x = i; x <= j; ++x)
            {
                for (int y = k; y <= l; ++y)
                {
                    for (int z = i1; z <= j1; ++z)
                    {
                        BlockPos pos = new BlockPos(x, y, z);
                        Block block = world.getBlockState(pos).getBlock();
                        if (OxygenUtil.testContactWithBreathableAir(world, block, pos, 0).isThermal()) // Thermal air has metadata 1
                        {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /*
     * A simplified version of the breathable air check which checks
     * all 6 sides of the given block (because a torch can pass air on all sides)
     * Used in BlockUnlitTorch.
     */
    public static boolean checkTorchHasOxygen(Level world, BlockPos pos)
    {
        if (OxygenUtil.inOxygenBubble(world, pos.getX() + 0.5D, pos.getY() + 0.6D, pos.getZ() + 0.5D))
        {
            return true;
        }
        OxygenUtil.checked = new HashSet<>();
        BlockVec3 vec = new BlockVec3(pos);
        for (int side = 0; side < 6; side++)
        {
            BlockVec3 sidevec = vec.newVecSide(side);
            BlockState state = sidevec.getBlockState_noChunkLoad(world);
            if (OxygenUtil.testContactWithBreathableAir(world, state.getBlock(), sidevec.toBlockPos(), 1).isBreathable())
            {
                return true;
            }
        }
        return false;
    }

    /*
     * Test whether the given block at (x,y,z) coordinates is either:
     * - breathable air (returns true)
     * - solid, or air which is not breathable (returns false)
     * - an air-permeable block, for example a torch, in which case test the surrounding
     * air-reachable blocks (up to 5 blocks away) and return true if breathable air is found
     * in one of them, or false if not.
     */
    private static synchronized BreathableAirResult testContactWithBreathableAir(Level world, Block block, BlockPos pos, int limitCount)
    {
        checked.add(pos);
        if (block == GCBlocks.breatheableAir || block == GCBlocks.brightBreatheableAir)
        {
            BlockState state = world.getBlockState(pos);
            return state.getValue(BlockThermalAir.THERMAL) ? BreathableAirResult.THERMAL_BREATHABLE : BreathableAirResult.BREATHABLE;
        }

        BlockState state = world.getBlockState(pos);
        if (block == null || block.getMaterial(state) == Material.AIR)
        {
            return BreathableAirResult.NONE;
        }

        //Test for non-sided permeable or solid blocks first
        boolean permeableFlag = false;
        if (!(block instanceof LeavesBlock))
        {
            if (state.isSolidRender(world, pos))
            {
                if (block instanceof GravelBlock || block.getMaterial(state) == Material.WOOL || block instanceof SpongeBlock)
                {
                    permeableFlag = true;
                }
                else
                {
                    return BreathableAirResult.NONE;
                }
            }
            else if (block instanceof GlassBlock || block instanceof StainedGlassBlock)
            {
                return BreathableAirResult.NONE;
            }
            else if (block instanceof LiquidBlock)
            {
                return BreathableAirResult.NONE;
            }
            else if (OxygenPressureProtocol.nonPermeableBlocks.contains(block))
            {
//                ArrayList<Integer> metaList = OxygenPressureProtocol.nonPermeableBlocks.get(block);
//                if (metaList.contains(-1) || metaList.contains(state.getBlock().getMetaFromState(state)))
//                {
//                    return -1;
//                }
                return BreathableAirResult.NONE;
            }
        }
        else
        {
            permeableFlag = true;
        }

        //Testing a non-air, permeable block (for example a torch or a ladder)
        if (limitCount < 5)
        {
            for (Direction side : Direction.values())
            {
                if (permeableFlag || OxygenUtil.canBlockPassAirOnSide(world, block, pos, side))
                {
                    BlockPos sidevec = pos.offset(side.getStepX(), side.getStepY(), side.getStepZ());
                    if (!checked.contains(sidevec))
                    {
                        Block newblock = world.getBlockState(sidevec).getBlock();
                        BreathableAirResult adjResult = OxygenUtil.testContactWithBreathableAir(world, newblock, sidevec, limitCount + 1);
                        if (adjResult != BreathableAirResult.NONE)
                        {
                            return adjResult;
                        }
                    }
                }
            }
        }

        return BreathableAirResult.NONE;
    }
    //TODO - performance, could add a 'safe' version of this code (inside world borders)

    //TODO - add more performance increase, these sided checks could be done once only
    private static boolean canBlockPassAirOnSide(Level world, Block block, BlockPos vec, Direction side)
    {
        if (block instanceof IPartialSealableBlock)
        {
            return !((IPartialSealableBlock) block).isSealed(world, vec, side);
        }

        //Half slab seals on the top side or the bottom side according to its metadata
        if (block instanceof SlabBlock)
        {
            BlockState state = world.getBlockState(vec);
            SlabType slabType = state.getValue(SlabBlock.TYPE);
            return !(side == Direction.DOWN && slabType == SlabType.TOP || side == Direction.UP && slabType == SlabType.BOTTOM);
        }

        //Farmland etc only seals on the solid underside
        if (block instanceof FarmBlock || block instanceof EnchantmentTableBlock || block instanceof LiquidBlock)
        {
            return side != Direction.UP;
        }

        if (block instanceof PistonBaseBlock)
        {
            BlockState state = world.getBlockState(vec);
            if (state.getValue(PistonBaseBlock.EXTENDED))
            {
                Direction direction = state.getValue(PistonBaseBlock.FACING);
                return side != direction;
            }
            return false;
        }

        return Block.isFaceFull(world.getBlockState(vec).getCollisionShape(world, vec, CollisionContext.empty()), side.getOpposite());
//        return !block.isSideSolid(world.getBlockState(vec), world, vec, Direction.byIndex(side.getIndex() ^ 1));
    }

    public static int getDrainSpacing(ItemStack tank, ItemStack tank2)
    {
        boolean tank1Valid = !tank.isEmpty() && tank.getItem() instanceof ItemOxygenTank && tank.getMaxDamage() - tank.getDamageValue() > 0;
        boolean tank2Valid = !tank2.isEmpty() && tank2.getItem() instanceof ItemOxygenTank && tank2.getMaxDamage() - tank2.getDamageValue() > 0;

        if (!tank1Valid && !tank2Valid)
        {
            return 0;
        }

        return 9;
    }

    public static boolean hasValidOxygenSetup(ServerPlayer player)
    {
        boolean missingComponent = false;

        GCPlayerStats stats = GCPlayerStats.get(player);

        if (!OxygenUtil.isItemValidForPlayerTankInv(0, stats.getExtendedInventory().getSingleSlot(0).get()))
        {
            boolean handled = false;

            for (final ItemStack armorStack : player.inventory.armor)
            {
                if (!armorStack.isEmpty() && armorStack.getItem() instanceof IBreathableArmor)
                {
                    final IBreathableArmor breathableArmor = (IBreathableArmor) armorStack.getItem();

                    if (breathableArmor.handleGearType(EnumGearType.HELMET))
                    {
                        if (breathableArmor.canBreathe(armorStack, player, EnumGearType.HELMET))
                        {
                            handled = true;
                        }
                    }
                }
            }

            if (!handled)
            {
                missingComponent = true;
            }
        }

        if (!OxygenUtil.isItemValidForPlayerTankInv(1, stats.getExtendedInventory().getSingleSlot(1).get()))
        {
            boolean handled = false;

            for (final ItemStack armorStack : player.inventory.armor)
            {
                if (!armorStack.isEmpty() && armorStack.getItem() instanceof IBreathableArmor)
                {
                    final IBreathableArmor breathableArmor = (IBreathableArmor) armorStack.getItem();

                    if (breathableArmor.handleGearType(EnumGearType.GEAR))
                    {
                        if (breathableArmor.canBreathe(armorStack, player, EnumGearType.GEAR))
                        {
                            handled = true;
                        }
                    }
                }
            }

            if (!handled)
            {
                missingComponent = true;
            }
        }

        if (!OxygenUtil.isItemValidForPlayerTankInv(2, stats.getExtendedInventory().getSingleSlot(2).get()) && !OxygenUtil.isItemValidForPlayerTankInv(3, stats.getExtendedInventory().getItem(3)))
        {
            boolean handled = false;

            for (final ItemStack armorStack : player.inventory.armor)
            {
                if (!armorStack.isEmpty() && armorStack.getItem() instanceof IBreathableArmor)
                {
                    final IBreathableArmor breathableArmor = (IBreathableArmor) armorStack.getItem();

                    if (breathableArmor.handleGearType(EnumGearType.TANK1))
                    {
                        if (breathableArmor.canBreathe(armorStack, player, EnumGearType.TANK1))
                        {
                            handled = true;
                        }
                    }

                    if (breathableArmor.handleGearType(EnumGearType.TANK2))
                    {
                        if (breathableArmor.canBreathe(armorStack, player, EnumGearType.TANK2))
                        {
                            handled = true;
                        }
                    }
                }
            }

            if (!handled)
            {
                missingComponent = true;
            }
        }

        return !missingComponent;
    }

    public static boolean isItemValidForPlayerTankInv(int slotIndex, ItemStack stack)
    {
        switch (slotIndex)
        {
        case 0:
            return stack.getItem() instanceof ItemOxygenMask;
        case 1:
            return stack.getItem() instanceof ItemOxygenGear;
        case 2:
        case 3:
            return stack.getItem() instanceof ItemOxygenTank || stack.getItem() instanceof ItemCanisterOxygenInfinite;
        }

        return false;
    }

    public static BlockEntity[] getAdjacentFluidConnections(BlockEntity tile)
    {
        return getAdjacentFluidConnections(tile, false);
    }

    public static BlockEntity[] getAdjacentFluidConnections(BlockEntity tile, boolean ignoreConnect)
    {
        if (tile == null)
        {
            return new BlockEntity[Direction.values().length];
        }

        return getAdjacentFluidConnections(new BlockVec3(tile), tile.getLevel(), ignoreConnect);
    }

    public static BlockEntity[] getAdjacentFluidConnections(BlockVec3 pos, Level world, boolean ignoreConnect)
    {
        BlockEntity[] adjacentConnections = new BlockEntity[Direction.values().length];

        boolean isMekLoaded = EnergyConfigHandler.isMekanismLoaded();

        for (Direction direction : Direction.values())
        {
            BlockEntity tileEntity = pos.getTileEntityOnSide(world, direction);

            boolean connectable = false;
            if (tileEntity instanceof IConnector)
            {
                connectable = ignoreConnect || ((IConnector) tileEntity).canConnect(direction.getOpposite(), NetworkType.FLUID);
            }
            else if (tileEntity != null)
            {
                connectable = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction.getOpposite()) != null;
            }

            if (connectable)
            {
                adjacentConnections[direction.ordinal()] = tileEntity;
            }
        }

        return adjacentConnections;
    }

    public static boolean noAtmosphericCombustion(Dimension dimension)
    {
        if (dimension instanceof IGalacticraftDimension)
        {
            return (!((IGalacticraftDimension) dimension).isGasPresent(EnumAtmosphericGas.OXYGEN) && !((IGalacticraftDimension) dimension).hasBreathableAtmosphere());
        }

        return false;
    }

    public static boolean inOxygenBubble(Level worldObj, double avgX, double avgY, double avgZ)
    {
        DimensionType dimID = GCCoreUtil.getDimensionType(worldObj);
        for (final BlockVec3Dim blockVec : TileEntityOxygenDistributor.loadedTiles)
        {
            if (blockVec != null && blockVec.dim == dimID)
            {
                BlockEntity tile = blockVec.getTileEntity();
                if (tile instanceof TileEntityOxygenDistributor)
                {
                    if (((TileEntityOxygenDistributor) tile).inBubble(avgX, avgY, avgZ))
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
