package micdoodle8.mods.galacticraft.planets.mars.tile;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.sounds.GCSounds;
import micdoodle8.mods.galacticraft.core.entities.EvolvedSkeletonEntity;
import micdoodle8.mods.galacticraft.core.entities.EvolvedSpiderEntity;
import micdoodle8.mods.galacticraft.core.entities.EvolvedZombieEntity;
import micdoodle8.mods.galacticraft.core.tile.TileEntityDungeonSpawner;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlockNames;
import micdoodle8.mods.galacticraft.planets.mars.entities.CreeperBossEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.List;

public class TileEntityDungeonSpawnerMars extends TileEntityDungeonSpawner<CreeperBossEntity>
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + MarsBlockNames.CRYOGENIC_CHAMBER)
    public static BlockEntityType<TileEntityDungeonSpawnerMars> TYPE;

    public TileEntityDungeonSpawnerMars()
    {
        super(TYPE, CreeperBossEntity.class);
    }

    @Override
    public List<Class<? extends Mob>> getDisabledCreatures()
    {
        List<Class<? extends Mob>> list = new ArrayList<Class<? extends Mob>>();
        list.add(EvolvedSkeletonEntity.class);
        list.add(EvolvedZombieEntity.class);
        list.add(EvolvedSpiderEntity.class);
        return list;
    }

    @Override
    public void playSpawnSound(Entity entity)
    {
        this.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), GCSounds.scaryScape, SoundCategory.AMBIENT, 9.0F, 1.4F);
    }
}
