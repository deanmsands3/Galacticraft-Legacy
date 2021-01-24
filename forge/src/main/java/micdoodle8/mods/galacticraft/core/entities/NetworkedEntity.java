package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.network.PacketDistributor;

public abstract class NetworkedEntity extends Entity implements IPacketReceiver
{
    public NetworkedEntity(EntityType<?> type, Level world)
    {
        super(type, world);

        if (world != null && world.isClientSide)
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketDynamic(this));
        }
    }

    @Override
    public void tick()
    {
        super.tick();

        PacketDynamic packet = new PacketDynamic(this);
        if (this.networkedDataChanged())
        {
            if (!this.level.isClientSide)
            {
                GalacticraftCore.packetPipeline.sendToAllAround(packet, new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), this.getPacketRange(), GCCoreUtil.getDimensionType(this.level)));
            }
            else
            {
                GalacticraftCore.packetPipeline.sendToServer(packet);
            }
        }
    }

    public abstract boolean networkedDataChanged();

    public abstract double getPacketRange();
}
