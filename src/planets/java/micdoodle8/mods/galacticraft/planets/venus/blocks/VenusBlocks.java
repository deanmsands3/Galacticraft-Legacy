package micdoodle8.mods.galacticraft.planets.venus.blocks;

import micdoodle8.mods.galacticraft.init.GCBlocks;
import micdoodle8.mods.galacticraft.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.items.ItemBlockGC;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemBlockLaser;
import micdoodle8.mods.galacticraft.planets.venus.items.ItemBlockBasicVenus;
import micdoodle8.mods.galacticraft.planets.venus.items.ItemBlockTorchWeb;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.oredict.OreDictionary;

public class VenusBlocks
{
    public static Block venusBlock;
    public static Block spout;
    public static Block bossSpawner;
    public static Block treasureChestTier3;
    public static Block torchWeb;
    public static Block sulphuricAcid;
    public static Block geothermalGenerator;
    public static Block crashedProbe;
    public static Block scorchedRock;
    public static Block solarArrayModule;
    public static Block solarArrayController;
    public static Block laserTurret;

    public static void initBlocks()
    {
        venusBlock = new BlockBasicVenus("venus");
        spout = new BlockSpout("spout");
        bossSpawner = new BlockBossSpawnerVenus("boss_spawner_venus");
        treasureChestTier3 = new BlockTier3TreasureChest("treasure_t3");
        torchWeb = new BlockTorchWeb("web_torch");
        geothermalGenerator = new BlockGeothermalGenerator("geothermal_generator");
        crashedProbe = new BlockCrashedProbe("crashed_probe");
        scorchedRock = new BlockScorchedRock("venus_rock_scorched");
        solarArrayModule = new BlockSolarArrayModule("solar_array_module");
        solarArrayController = new BlockSolarArrayController("solar_array_controller");
        laserTurret = new BlockLaserTurret("laser_turret");

        GCBlocks.hiddenBlocks.add(bossSpawner);

        registerBlocks();
        setHarvestLevels();
    }

    private static void setHarvestLevel(Block block, String toolClass, int level, int meta)
    {
        block.setHarvestLevel(toolClass, level, block.getStateFromMeta(meta));
    }

    private static void setHarvestLevel(Block block, String toolClass, int level)
    {
        block.setHarvestLevel(toolClass, level);
    }

    public static void setHarvestLevels()
    {
        setHarvestLevel(venusBlock, "pickaxe", 1, 0);
        setHarvestLevel(venusBlock, "pickaxe", 1, 1);
        setHarvestLevel(venusBlock, "pickaxe", 1, 2);
        setHarvestLevel(venusBlock, "pickaxe", 1, 3);
        setHarvestLevel(spout, "pickaxe", 1);
    }

    public static void registerBlock(Block block, Class<? extends ItemBlock> itemClass)
    {
        GCBlocks.registerBlock(block, itemClass);
    }

    public static void registerBlocks()
    {
        registerBlock(venusBlock, ItemBlockBasicVenus.class);
        registerBlock(spout, ItemBlockGC.class);
        registerBlock(bossSpawner, ItemBlockGC.class);
        registerBlock(treasureChestTier3, ItemBlockDesc.class);
        registerBlock(torchWeb, ItemBlockTorchWeb.class);
        registerBlock(geothermalGenerator, ItemBlockDesc.class);
        registerBlock(crashedProbe, ItemBlockDesc.class);
        registerBlock(scorchedRock, ItemBlockGC.class);
        registerBlock(solarArrayModule, ItemBlockDesc.class);
        registerBlock(solarArrayController, ItemBlockDesc.class);
        registerBlock(laserTurret, ItemBlockLaser.class);
    }

    public static void oreDictRegistration()
    {
        OreDictionary.registerOre("oreCopper", BlockBasicVenus.EnumBlockBasicVenus.ORE_COPPER.getItemStack());
        OreDictionary.registerOre("oreTin", BlockBasicVenus.EnumBlockBasicVenus.ORE_TIN.getItemStack());
        OreDictionary.registerOre("oreAluminum", BlockBasicVenus.EnumBlockBasicVenus.ORE_ALUMINUM.getItemStack());
        OreDictionary.registerOre("oreAluminium", BlockBasicVenus.EnumBlockBasicVenus.ORE_ALUMINUM.getItemStack());
        OreDictionary.registerOre("oreNaturalAluminum", BlockBasicVenus.EnumBlockBasicVenus.ORE_ALUMINUM.getItemStack());
        OreDictionary.registerOre("oreQuartz", BlockBasicVenus.EnumBlockBasicVenus.ORE_QUARTZ.getItemStack());
        OreDictionary.registerOre("oreLead", BlockBasicVenus.EnumBlockBasicVenus.ORE_GALENA.getItemStack());
        OreDictionary.registerOre("oreSilicon", BlockBasicVenus.EnumBlockBasicVenus.ORE_SILICON.getItemStack());
        OreDictionary.registerOre("oreSolar", BlockBasicVenus.EnumBlockBasicVenus.ORE_SOLAR_DUST.getItemStack());

        OreDictionary.registerOre("blockLead", BlockBasicVenus.EnumBlockBasicVenus.LEAD_BLOCK.getItemStack());
    }
}
