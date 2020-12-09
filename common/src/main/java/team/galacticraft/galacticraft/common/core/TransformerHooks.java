package team.galacticraft.galacticraft.common.core;

import team.galacticraft.galacticraft.common.api.entity.IAntiGrav;
import team.galacticraft.galacticraft.common.api.entity.ICameraZoomEntity;
import team.galacticraft.galacticraft.common.api.item.IArmorGravity;
import team.galacticraft.galacticraft.common.api.prefab.entity.EntitySpaceshipBase;
import team.galacticraft.galacticraft.common.api.world.IGalacticraftDimension;
import team.galacticraft.galacticraft.common.api.world.IOrbitDimension;
import team.galacticraft.galacticraft.common.api.world.IWeatherProvider;
import team.galacticraft.galacticraft.common.api.world.IZeroGDimension;
import team.galacticraft.galacticraft.core.client.FootprintRenderer;
import team.galacticraft.galacticraft.core.dimension.DimensionMoon;
import team.galacticraft.galacticraft.core.entities.player.EnumGravity;
import team.galacticraft.galacticraft.common.api.entity.GCPlayerStats;
import team.galacticraft.galacticraft.common.api.entity.GCPlayerStatsClient;
import team.galacticraft.galacticraft.core.proxy.ClientProxyCore;
import team.galacticraft.galacticraft.core.util.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static team.galacticraft.galacticraft.core.proxy.ClientProxyCore.PLAYER_Y_OFFSET;
import static team.galacticraft.galacticraft.core.proxy.ClientProxyCore.submergedTextures;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;

/**
 * These methods are called from vanilla minecraft through bytecode injection done in MicdoodleCore
 * <p>
 * See https://github.com/micdoodle8/MicdoodleCore
 */
public class TransformerHooks
{
//    private static final List<IWorldGenerator> otherModGeneratorsWhitelist = new LinkedList<>(); TODO Overworld gen
//    private static final IWorldGenerator generatorTCAuraNodes = null;
    private static final Method generateTCAuraNodes = null;
    private static final boolean generatorsInitialised = false;
    public static List<Block> spawnListAE2_GC = new LinkedList<>();
    public static ThreadLocal<BufferBuilder> renderBuilder = new ThreadLocal<>();
    private static int rainSoundCounter = 0;
    private static final Random random = new Random();

    public static double getGravityForEntity(Entity entity)
    {
        if (entity.level.getDimension() instanceof IGalacticraftDimension)
        {
            if (entity instanceof Chicken && !OxygenUtil.isAABBInBreathableAirBlock(entity.level, entity.getBoundingBox()))
            {
                return 0.08D;
            }

            final IGalacticraftDimension customProvider = (IGalacticraftDimension) entity.level.getDimension();
            if (entity instanceof Player)
            {
                Player player = (Player) entity;
                if (player.inventory != null)
                {
                    int armorModLowGrav = 100;
                    int armorModHighGrav = 100;
                    for (ItemStack armorPiece : player.getArmorSlots())
                    {
                        if (armorPiece != null && armorPiece.getItem() instanceof IArmorGravity)
                        {
                            armorModLowGrav -= ((IArmorGravity) armorPiece.getItem()).gravityOverrideIfLow(player);
                            armorModHighGrav -= ((IArmorGravity) armorPiece.getItem()).gravityOverrideIfHigh(player);
                        }
                    }
                    if (armorModLowGrav > 100)
                    {
                        armorModLowGrav = 100;
                    }
                    if (armorModHighGrav > 100)
                    {
                        armorModHighGrav = 100;
                    }
                    if (armorModLowGrav < 0)
                    {
                        armorModLowGrav = 0;
                    }
                    if (armorModHighGrav < 0)
                    {
                        armorModHighGrav = 0;
                    }
                    if (customProvider.getGravity() > 0)
                    {
                        return 0.08D - (customProvider.getGravity() * armorModLowGrav) / 100;
                    }
                    return 0.08D - (customProvider.getGravity() * armorModHighGrav) / 100;
                }
            }
            return 0.08D - customProvider.getGravity();
        }
        else if (entity instanceof IAntiGrav)
        {
            return 0;
        }
        else
        {
            return 0.08D;
        }
    }

    public static double getItemGravity(ItemEntity e)
    {
        if (e.level.getDimension() instanceof IGalacticraftDimension)
        {
            final IGalacticraftDimension customProvider = (IGalacticraftDimension) e.level.getDimension();
            return Math.max(0.002D, 0.03999999910593033D - (customProvider instanceof IOrbitDimension ? 0.05999999910593033D : customProvider.getGravity()) / 1.75D);
        }
        else
        {
            return 0.03999999910593033D;
        }
    }

    public static float getArrowGravity(AbstractArrow e)
    {
        if (e.level.getDimension() instanceof IGalacticraftDimension)
        {
            return ((IGalacticraftDimension) e.level.getDimension()).getArrowGravity();
        }
        else
        {
            return 0.05F;
        }
    }

    public static float getRainStrength(Level world, float partialTicks)
    {
        if (world.isClientSide)
        {
//            if (world.getDimension().getSkyRenderer() instanceof SkyProviderOverworld)
//            {
//                return 0.0F;
//            }  TODO Sky rendering
        }

        return world.oRainLevel + (world.rainLevel - world.oRainLevel) * partialTicks;
    }

    public static void otherModGenerate(int chunkX, int chunkZ, Level world, ChunkGenerator chunkGenerator, ChunkSource chunkProvider)
    {
//        if (world.getDimension() instanceof DimensionSpaceStation) TODO Other mod gen in space dimensions
//        {
//            return;
//        }
//
//        if (!(world.getDimension() instanceof IGalacticraftWorldProvider) || ConfigManagerCore.enableOtherModsFeatures.get())
//        {
//            try {
//                net.minecraftforge.fml.common.registry.GameRegistry.generateWorld(chunkX, chunkZ, world, chunkGenerator, chunkProvider);
//            } catch (Exception e)
//            {
//                GCLog.severe("Error in another mod's worldgen.  This is *NOT* a Galacticraft bug, report it to the other mod please.");
//                GCLog.severe("Details:- Dimension:" + GCCoreUtil.getDimensionID(world) + "  Chunk cx,cz:" + chunkX + "," + chunkZ + "  Seed:" + world.getSeed());
//                e.printStackTrace();
//            }
//            return;
//        }
//
//        if (!generatorsInitialised)
//        {
//            generatorsInitialised = true;
//
//            if (ConfigManagerCore.whitelistCoFHCoreGen.get())
//            {
//                addWorldGenForName("CoFHCore custom oregen", "cofh.cofhworld.init.WorldHandler");
//            }
//            addWorldGenForName("GalacticGreg oregen", "bloodasp.galacticgreg.GT_Worldgenerator_Space");
//            addWorldGenForName("Dense Ores oregen", "com.rwtema.denseores.WorldGenOres");
//            addWorldGenForName("AE2 meteorites worldgen", "appeng.worldgen.MeteoriteWorldGen");
//
//            try
//            {
//                Class genThaumCraft = Class.forName("thaumcraft.common.lib.world.ThaumcraftWorldGenerator");
//                if (genThaumCraft != null && ConfigManagerCore.enableThaumCraftNodes.get())
//                {
//                    final Field regField = GameRegistry.class.getDeclaredField("worldGenerators");
//                    regField.setAccessible(true);
//                    Set<IWorldGenerator> registeredGenerators = (Set<IWorldGenerator>) regField.get(null);
//                    for (IWorldGenerator gen : registeredGenerators)
//                    {
//                        if (genThaumCraft.isInstance(gen))
//                        {
//                            generatorTCAuraNodes = gen;
//                            break;
//                        }
//                    }
//                    if (generatorTCAuraNodes != null)
//                    {
//                        generateTCAuraNodes = genThaumCraft.getDeclaredMethod("generateWildNodes", World.class, Random.class, int.class, int.class, boolean.class, boolean.class);
//                        generateTCAuraNodes.setAccessible(true);
//                        GCLog.info("Whitelisting ThaumCraft aura node generation on planets.");
//                    }
//                }
//            }
//            catch (Exception e)
//            {
//            }
//        }
//
//        if (otherModGeneratorsWhitelist.size() > 0 || generateTCAuraNodes != null)
//        {
//            try
//            {
//                long worldSeed = world.getSeed();
//                Random fmlRandom = new Random(worldSeed);
//                long xSeed = fmlRandom.nextLong() >> 2 + 1L;
//                long zSeed = fmlRandom.nextLong() >> 2 + 1L;
//                long chunkSeed = (xSeed * chunkX + zSeed * chunkZ) ^ worldSeed;
//                fmlRandom.setSeed(chunkSeed);
//
//                for (IWorldGenerator gen : otherModGeneratorsWhitelist)
//                {
//                    gen.generate(fmlRandom, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
//                }
//                if (generateTCAuraNodes != null)
//                {
//                    generateTCAuraNodes.invoke(generatorTCAuraNodes, world, fmlRandom, chunkX, chunkZ, false, true);
//                }
//            }
//            catch (Exception e)
//            {
//                GCLog.severe("Error in another mod's worldgen.  This is *NOT* a Galacticraft bug, report it to the other mod please.");
//                e.printStackTrace();
//            }
//        }
    }

    private static void addWorldGenForName(String logString, String name)
    {
        try
        {
            Class target = Class.forName(name);
            if (target != null)
            {
                final Field regField = GameRegistry.class.getDeclaredField("worldGenerators");
                regField.setAccessible(true);
//                Set<IWorldGenerator> registeredGenerators = (Set<IWorldGenerator>) regField.get(null);
//                for (IWorldGenerator gen : registeredGenerators)
//                {
//                    if (target.isInstance(gen))
//                    {
//                        otherModGeneratorsWhitelist.add(gen);
//                        GCLog.info("Whitelisting " + logString + " on planets.");
//                        return;
//                    }
//                } TODO Overworld gen
            }
        }
        catch (Exception e)
        {
        }
    }

    /*
     * Used to supplement the hard-coded blocklist in AE2's MeteoritePlacer class
     */
    public static boolean addAE2MeteorSpawn(Object o, Block b)
    {
        if (o instanceof Collection<?>)
        {
            ((Collection<Block>) o).add(b);
            ((Collection<Block>) o).addAll(spawnListAE2_GC);
            return true;
        }
        return false;
    }

    @Environment(EnvType.CLIENT)
    public static float getWorldBrightness(ClientLevel world)
    {
        if (world.getDimension() instanceof DimensionMoon)
        {
            float f1 = world.getTimeOfDay(1.0F);
            float f2 = 1.0F - (Mth.cos(f1 * Constants.TWO_PI) * 2.0F + 0.2F);

            if (f2 < 0.0F)
            {
                f2 = 0.0F;
            }

            if (f2 > 1.0F)
            {
                f2 = 1.0F;
            }

            f2 = 1.0F - f2;
            return f2 * 0.8F;
        }

        return world.getSkyDarken(1.0F);
    }

    @Environment(EnvType.CLIENT)
    public static float getColorRed(Level world)
    {
        return WorldUtil.getWorldColor(world).x;
    }

    @Environment(EnvType.CLIENT)
    public static float getColorGreen(Level world)
    {
        return WorldUtil.getWorldColor(world).y;
    }

    @Environment(EnvType.CLIENT)
    public static float getColorBlue(Level world)
    {
        return WorldUtil.getWorldColor(world).z;
    }

    @Environment(EnvType.CLIENT)
    public static Vec3 getFogColorHook(ClientLevel world)
    {
        LocalPlayer player = Minecraft.getInstance().player;
//        if (world.getDimension().getSkyRenderer() instanceof SkyProviderOverworld)
//        {
//            float var20 = ((float) (player.getPosY()) - Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT) / 1000.0F;
//            var20 = MathHelper.sqrt(var20);
//            final float var21 = Math.max(1.0F - var20 * 40.0F, 0.0F);
//
//            Vec3d vec = world.getFogColor(1.0F);
//
//            return new Vec3d(vec.x * Math.max(1.0F - var20 * 1.29F, 0.0F), vec.y * Math.max(1.0F - var20 * 1.29F, 0.0F), vec.z * Math.max(1.0F - var20 * 1.29F, 0.0F));
//        } TODO Sky rendering

        return world.getFogColor(1.0F);
    }

    @Environment(EnvType.CLIENT)
    public static Vec3 getSkyColorHook(ClientLevel world)
    {
        LocalPlayer player = Minecraft.getInstance().player;
//        if (world.getDimension().getSkyRenderer() instanceof SkyProviderOverworld || (player != null && player.getPosY() > Constants.OVERWORLD_CLOUD_HEIGHT && player.getRidingEntity() instanceof EntitySpaceshipBase))
//        {
//            float f1 = world.getCelestialAngle(1.0F);
//            float f2 = MathHelper.cos(f1 * Constants.TWO_PI) * 2.0F + 0.5F;
//
//            if (f2 < 0.0F)
//            {
//                f2 = 0.0F;
//            }
//
//            if (f2 > 1.0F)
//            {
//                f2 = 1.0F;
//            }
//
//            int i = MathHelper.floor(player.getPosX());
//            int j = MathHelper.floor(player.getPosY());
//            int k = MathHelper.floor(player.getPosZ());
//            BlockPos pos = new BlockPos(i, j, k);
//            int l = ForgeHooksClient.getSkyBlendColour(world, pos);
//            float f4 = (float) (l >> 16 & 255) / 255.0F;
//            float f5 = (float) (l >> 8 & 255) / 255.0F;
//            float f6 = (float) (l & 255) / 255.0F;
//            f4 *= f2;
//            f5 *= f2;
//            f6 *= f2;
//
//            if (player.getPosY() <= Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT)
//            {
//                Vec3d vec = world.getSkyColor(Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getBlockPos(), 1.0F);
//                double blend = (player.getPosY() - Constants.OVERWORLD_CLOUD_HEIGHT) / (Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT - Constants.OVERWORLD_CLOUD_HEIGHT);
//                double ablend = 1 - blend;
//                return new Vec3d(f4 * blend + vec.x * ablend, f5 * blend + vec.y * ablend, f6 * blend + vec.z * ablend);
//            }
//            else
//            {
//                double blend = Math.min(1.0D, (player.getPosY() - Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT) / 300.0D);
//                double ablend = 1.0D - blend;
//                blend /= 255.0D;
//                return new Vec3d(f4 * ablend + blend * 31.0D, f5 * ablend + blend * 8.0D, f6 * ablend + blend * 99.0D);
//            }
//        } TODO Sky rendering

        return world.getSkyColor(Minecraft.getInstance().gameRenderer.getMainCamera().getBlockPosition(), 1.0F);
    }

    @Environment(EnvType.CLIENT)
    public static double getRenderPosY(Entity viewEntity, double regular)
    {
        if (viewEntity.getY() >= 256)
        {
            return 255.0F;
        }
        else
        {
            return regular + viewEntity.getEyeHeight();
        }
    }

    @Environment(EnvType.CLIENT)
    public static boolean shouldRenderFire(Entity entity)
    {
        if (entity.level == null || !(entity.level.getDimension() instanceof IGalacticraftDimension))
        {
            return entity.isOnFire();
        }

        if (!(entity instanceof LivingEntity) && !(entity instanceof AbstractArrow))
        {
            return entity.isOnFire();
        }

        if (entity.isOnFire())
        {
            if (OxygenUtil.noAtmosphericCombustion(entity.level.getDimension()))
            {
                return OxygenUtil.isAABBInBreathableAirBlock(entity.level, entity.getBoundingBox());
            }
            else
            {
                return true;
            }
            //Disable fire on Galacticraft worlds with no oxygen
        }

        return false;
    }

    @Environment(EnvType.CLIENT)
    public static void orientCamera(float partialTicks)
    {
        LocalPlayer player = ClientProxyCore.mc.player;
        GCPlayerStatsClient stats = GCPlayerStatsClient.get(player);

        Entity viewEntity = ClientProxyCore.mc.getCameraEntity();

        if (player.getVehicle() instanceof ICameraZoomEntity && ClientProxyCore.mc.options.thirdPersonView == 0)
        {
            Entity entity = player.getVehicle();
            float offset = ((ICameraZoomEntity) entity).getRotateOffset();
            if (offset > -10F)
            {
                offset += PLAYER_Y_OFFSET;
                GL11.glTranslatef(0, -offset, 0);
                float anglePitch = entity.xRotO + (entity.xRot - entity.xRotO) * partialTicks;
                float angleYaw = entity.yRotO + (entity.yRot - entity.yRotO) * partialTicks;
                GL11.glRotatef(-anglePitch, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(angleYaw, 0.0F, 1.0F, 0.0F);

                GL11.glTranslatef(0, offset, 0);
            }
        }

        if (viewEntity instanceof LivingEntity && viewEntity.level.getDimension() instanceof IZeroGDimension && !((LivingEntity) viewEntity).isSleeping())
        {
            float pitch = viewEntity.xRotO + (viewEntity.xRot - viewEntity.xRotO) * partialTicks;
            float yaw = viewEntity.yRotO + (viewEntity.yRot - viewEntity.yRotO) * partialTicks + 180.0F;
            float eyeHeightChange = viewEntity.getBbWidth() / 2.0F;

//            GL11.glTranslatef(0.0F, -f1, 0.0F);
            GL11.glRotatef(-yaw, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-pitch, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(0.0F, 0.0F, 0.1F);

            EnumGravity gDir = stats.getGdir();
            GL11.glRotatef(180.0F * gDir.getThetaX(), 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(180.0F * gDir.getThetaZ(), 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(pitch * gDir.getPitchGravityX(), 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(pitch * gDir.getPitchGravityY(), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(yaw * gDir.getYawGravityX(), 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(yaw * gDir.getYawGravityY(), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(yaw * gDir.getYawGravityZ(), 0.0F, 0.0F, 1.0F);

//        	GL11.glTranslatef(sneakY * gDir.getSneakVecX(), sneakY * gDir.getSneakVecY(), sneakY * gDir.getSneakVecZ());

            GL11.glTranslatef(eyeHeightChange * gDir.getEyeVecX(), eyeHeightChange * gDir.getEyeVecY(), eyeHeightChange * gDir.getEyeVecZ());

            if (stats.getGravityTurnRate() < 1.0F)
            {
                GL11.glRotatef(90.0F * (stats.getGravityTurnRatePrev() + (stats.getGravityTurnRate() - stats.getGravityTurnRatePrev()) * partialTicks), stats.getGravityTurnVecX(), stats.getGravityTurnVecY(), stats.getGravityTurnVecZ());
            }
        }

        //omit this for interesting 3P views
//        GL11.glTranslatef(0.0F, 0.0F, -0.1F);
//        GL11.glRotatef(pitch, 1.0F, 0.0F, 0.0F);
//        GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
//        GL11.glTranslatef(0.0F, f1, 0.0F);
    }

    @Environment(EnvType.CLIENT)
    public static void renderLiquidOverlays(float partialTicks)
    {
        boolean within = false;
        for (Map.Entry<Fluid, ResourceLocation> entry : submergedTextures.entrySet())
        {
            if (FluidUtil.isInsideOfFluid(ClientProxyCore.mc.player, entry.getKey()))
            {
                within = true;
                ClientProxyCore.mc.getTextureManager().bind(entry.getValue());
                break;
            }
        }

        if (!within)
        {
            return;
        }

        Tesselator tessellator = Tesselator.getInstance();
        float f1 = ClientProxyCore.mc.player.getBrightness() / 3.0F;
        GL11.glColor4f(f1, f1, f1, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glPushMatrix();
        float f2 = 4.0F;
        float f3 = -1.1F;
        float f4 = 1.1F;
        float f5 = -1.1F;
        float f6 = 1.1F;
        float f7 = -0.25F;
        float f8 = -ClientProxyCore.mc.player.yRot / 64.0F;
        float f9 = ClientProxyCore.mc.player.xRot / 64.0F;
        BufferBuilder worldRenderer = tessellator.getBuilder();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION_TEX);
        worldRenderer.vertex(f3, f5, f7).uv(f2 + f8, f2 + f9).endVertex();
        worldRenderer.vertex(f4, f5, f7).uv(0.0F + f8, f2 + f9).endVertex();
        worldRenderer.vertex(f4, f6, f7).uv(0.0F + f8, 0.0F + f9).endVertex();
        worldRenderer.vertex(f3, f6, f7).uv(f2 + f8, 0.0F + f9).endVertex();
        tessellator.end();
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Environment(EnvType.CLIENT)
    public static void renderFootprints(float partialTicks)
    {
        FootprintRenderer.renderFootprints(ClientProxyCore.mc.player, partialTicks);
        MinecraftForge.EVENT_BUS.post(new ClientProxyCore.EventSpecialRender(partialTicks));
//        BubbleRenderer.renderBubbles(ClientProxyCore.mc.player, partialTicks); TODO Bubble rendering
    }

    @Environment(EnvType.CLIENT)
    public static double getCameraZoom(double previous)
    {
        if (ConfigManagerCore.disableVehicleCameraChanges.get())
        {
            return previous;
        }

        Player player = Minecraft.getInstance().player;
        if (player.getVehicle() != null && player.getVehicle() instanceof ICameraZoomEntity)
        {
            return ((ICameraZoomEntity) player.getVehicle()).getCameraZoom();
        }

        return previous;
    }

    public static double armorDamageHook(LivingEntity entity)
    {
        if (entity instanceof Player && GalacticraftCore.isPlanetsLoaded)
        {
            GCPlayerStats stats = GCPlayerStats.get(entity);
            if (stats != null)
            {
                ItemStack shield = stats.getShieldControllerInSlot();
//                if (shield != null && !shield.isEmpty() && shield.getItem() == VenusItems.basicItem && shield.getDamage() == 0)
//                {
//                    return 0D;
//                } TODO Planets
            }
        }
        return 1D;
    }

    public static void setCurrentBuffer(BufferBuilder buffer)
    {
        renderBuilder.set(buffer);
    }

    public static boolean isGrating(boolean orig, Block b)
    {
        return false; // TODO Grating
//        return orig || (b instanceof BlockGrating && b != GCBlocks.grating && MinecraftForgeClient.getRenderLayer() == BlockRenderLayer.TRANSLUCENT);
    }

    public static float armorDamageHookF(LivingEntity entity)
    {
        return (float) armorDamageHook(entity);
    }

    @Environment(EnvType.CLIENT)
    public static int addRainParticles(int result, int rendererUpdateCount)
    {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel world = mc.level;
        if (result == 0 || !(world.getDimension() instanceof IWeatherProvider))
        {
            // Either no rain or it's a vanilla dimension
            return result;
        }
        IWeatherProvider moddedProvider = ((IWeatherProvider) world.getDimension());
        float f = mc.level.getRainLevel(1.0F);
        if (!mc.options.fancyGraphics)
        {
            f /= 2.0F;
        }

        if (f != 0.0F)
        {
            random.setSeed((long) rendererUpdateCount * 312987231L);
            LevelReader iworldreader = mc.level;
            BlockPos blockpos = new BlockPos(mc.gameRenderer.getMainCamera().getPosition());
            int i = 10;
            double xx = 0.0D;
            double yy = 0.0D;
            double zz = 0.0D;
            double x = 0.0D;
            double y = 0.0D;
            double z = 0.0D;
            int j = 0;
            int k = (int) (100.0F * f * f);
            if (mc.options.particles == ParticleStatus.DECREASED)
            {
                k >>= 1;
            }
            else if (mc.options.particles == ParticleStatus.MINIMAL)
            {
                k = 0;
            }

            for (int l = 0; l < k; ++l)
            {
                BlockPos blockpos1 = iworldreader.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockpos.offset(random.nextInt(10) - random.nextInt(10), 0, random.nextInt(10) - random.nextInt(10)));
                Biome biome = iworldreader.getBiome(blockpos1);
                BlockPos blockpos2 = blockpos1.below();
                if (blockpos1.getY() <= blockpos.getY() + 10 && blockpos1.getY() >= blockpos.getY() - 10 && biome.getPrecipitation() == Biome.Precipitation.RAIN && biome.getTemperature(blockpos1) >= 0.15F)
                {
                    double d3 = random.nextDouble();
                    double d4 = random.nextDouble();
                    BlockState blockstate = iworldreader.getBlockState(blockpos2);
                    FluidState ifluidstate = iworldreader.getFluidState(blockpos1);
                    VoxelShape voxelshape = blockstate.getCollisionShape(iworldreader, blockpos2);
                    double d7 = voxelshape.max(Direction.Axis.Y, d3, d4);
                    double d8 = ifluidstate.getHeight(iworldreader, blockpos1);
                    double d5;
                    double d6;
                    if (d7 >= d8)
                    {
                        d5 = d7;
                        d6 = voxelshape.min(Direction.Axis.Y, d3, d4);
                    }
                    else
                    {
                        d5 = 0.0D;
                        d6 = 0.0D;
                    }

                    if (d5 > -Double.MAX_VALUE)
                    {
                        if (!ifluidstate.is(FluidTags.LAVA) && blockstate.getBlock() != Blocks.MAGMA_BLOCK && (blockstate.getBlock() != Blocks.CAMPFIRE || !blockstate.getValue(CampfireBlock.LIT)))
                        {
                            ++j;
                            x = (double) blockpos2.getX() + d3;
                            y = (double) ((float) blockpos2.getY() + 0.1F) + d5;
                            z = (double) blockpos2.getZ() + d4;
                            if (random.nextInt(j) == 0)
                            {
                                xx = x + d3;
                                yy = y - 1.0D;
                                zz = z + d4;
                            }

                            mc.level.addParticle(moddedProvider.getParticle(mc.level, x, y, z), x, y, z, 0.0D, 0.0D, 0.0D);
                        }
                        else
                        {
                            mc.level.addParticle(ParticleTypes.SMOKE, (double) blockpos1.getX() + d3, (double) ((float) blockpos1.getY() + 0.1F) - d6, (double) blockpos1.getZ() + d4, 0.0D, 0.0D, 0.0D);
                        }
                    }
                }
            }

            if (j > 0 && random.nextInt(moddedProvider.getSoundInterval(f)) < rainSoundCounter++)
            {
                rainSoundCounter = 0;

                moddedProvider.weatherSounds(j, mc, world, blockpos, xx, yy, zz, random);
            }
        }

        // Bypass vanilla code after returning from this
        return 0;
    }
}
