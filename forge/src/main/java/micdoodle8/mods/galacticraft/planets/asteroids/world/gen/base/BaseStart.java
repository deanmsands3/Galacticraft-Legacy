package micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import static micdoodle8.mods.galacticraft.planets.asteroids.world.gen.AsteroidFeatures.CBASE_START;

public class BaseStart extends BaseDeck
{
    public List<StructurePiece> attachedComponents = Lists.newArrayList();
    public List<BoundingBox> componentBounds = Lists.newArrayList();

    public BaseStart(StructureManager templateManager, CompoundTag nbt)
    {
        super(CBASE_START, nbt);
    }

    public BaseStart(BaseConfiguration configuration, Random rand, int blockPosX, int blockPosZ, Direction direction)
    {
        super(CBASE_START, configuration, rand, blockPosX, configuration.getYPosition(), blockPosZ, 1, direction);
    }

    @Override
    public void addChildren(StructurePiece componentIn, List<StructurePiece> listIn, Random rand)
    {
        attachedComponents.clear();
        componentBounds.clear();
        componentBounds.add(this.boundingBox);
        listIn.clear();
        listIn.add(this);
        List<Piece> rooms = getRooms(0, this, rand);
        for (Piece next : rooms)
        {
            listIn.add(next);
            attachedComponents.add(next);
            componentBounds.add(next.getBoundingBox());
        }

        //TODO  applyAsteroidDamage();

        super.addChildren(componentIn, listIn, rand);
    }
}