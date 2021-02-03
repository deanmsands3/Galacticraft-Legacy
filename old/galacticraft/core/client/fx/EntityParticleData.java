package micdoodle8.mods.galacticraft.core.client.fx;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Locale;
import java.util.UUID;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

public class EntityParticleData implements ParticleOptions
{
    public static final ParticleOptions.Deserializer<EntityParticleData> DESERIALIZER = new ParticleOptions.Deserializer<EntityParticleData>()
    {
        @Override
        public EntityParticleData fromCommand(ParticleType<EntityParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException
        {
            reader.expect(' ');
            return new EntityParticleData(particleTypeIn, UUID.fromString(reader.readString()));
        }

        @Override
        public EntityParticleData fromNetwork(ParticleType<EntityParticleData> particleTypeIn, FriendlyByteBuf buffer)
        {
            return new EntityParticleData(particleTypeIn, buffer.readUUID());
        }
    };

    private final ParticleType<EntityParticleData> particleType;
    private final UUID entityUUID;

    public EntityParticleData(ParticleType<EntityParticleData> particleType, UUID entityUUID)
    {
        this.particleType = particleType;
        this.entityUUID = entityUUID;
    }

    @Override
    public ParticleType<?> getType()
    {
        return particleType;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer)
    {
        buffer.writeBoolean(this.entityUUID != null);
        if (this.entityUUID != null)
        {
            buffer.writeUUID(this.entityUUID);
        }
    }

    @Override
    public String writeToString()
    {
        return String.format(Locale.ROOT, "%s %s", Registry.PARTICLE_TYPE.getKey(this.getType()), this.entityUUID.toString());
    }

    public UUID getEntityUUID()
    {
        return entityUUID;
    }
}
