package micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon;

import com.google.common.collect.Lists;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import java.util.List;
import java.util.Random;

import static micdoodle8.mods.galacticraft.planets.venus.world.gen.VenusFeatures.CVENUS_DUNGEON_START;

public class DungeonStartVenus extends EntranceCraterVenus
{
    public List<StructurePiece> attachedComponents = Lists.newArrayList();
    public List<BoundingBox> componentBounds = Lists.newArrayList();

    public DungeonStartVenus(StructureManager templateManager, CompoundTag nbt)
    {
        super(CVENUS_DUNGEON_START, nbt);
    }

    public DungeonStartVenus(Level world, DungeonConfigurationVenus configuration, Random rand, int blockPosX, int blockPosZ)
    {
        super(CVENUS_DUNGEON_START, configuration, rand, blockPosX, blockPosZ);
    }

    @Override
    public void addChildren(StructurePiece componentIn, List<StructurePiece> listIn, Random rand)
    {
        boolean validAttempt = false;
        final int maxAttempts = 10;
        int attempts = 0;
        while (!validAttempt && attempts < maxAttempts)
        {
            attachedComponents.clear();
            componentBounds.clear();
            componentBounds.add(this.boundingBox);
            listIn.clear();
            listIn.add(this);
            PieceVenus next = getNextPiece(this, rand);
            while (next != null)
            {
                listIn.add(next);
                attachedComponents.add(next);
                componentBounds.add(next.getBoundingBox());

                next = next.getNextPiece(this, rand);
            }
            if (attachedComponents.size() >= 3 && attachedComponents.get(attachedComponents.size() - 1) instanceof RoomTreasureVenus &&
                    attachedComponents.get(attachedComponents.size() - 3) instanceof RoomBossVenus)
            {
                validAttempt = true;
            }
            attempts++;
        }

        GCLog.debug("Dungeon generation took " + attempts + " attempt(s)");

        if (!validAttempt)
        {
            int xPos = this.boundingBox.x0 + (this.boundingBox.x1 - this.boundingBox.x0) / 2;
            int zPos = this.boundingBox.z0 + (this.boundingBox.z1 - this.boundingBox.z0) / 2;
            System.err.println("Could not find valid venus dungeon layout! This is a bug, please report it, including your world seed (/seed) and dungeon location (" + xPos + ", " + zPos + ")");
        }
    }

    public boolean checkIntersection(int blockX, int blockZ, int sizeX, int sizeZ)
    {
        return this.checkIntersection(new BoundingBox(blockX, blockZ, blockX + sizeX, blockZ + sizeZ));
    }

    public boolean checkIntersection(BoundingBox bounds)
    {
        for (int i = 0; i < componentBounds.size() - 1; ++i)
        {
            BoundingBox boundingBox = componentBounds.get(i);
            if (boundingBox.intersects(bounds))
            {
                return true;
            }
        }

        return false;
    }
}