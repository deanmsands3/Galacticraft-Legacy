package micdoodle8.mods.galacticraft.core.client.fx;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockPosParticleData implements ParticleOptions
{
    public static final ParticleOptions.Deserializer<BlockPosParticleData> DESERIALIZER = new ParticleOptions.Deserializer<BlockPosParticleData>()
    {
        @Override
        public BlockPosParticleData fromCommand(ParticleType<BlockPosParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException
        {
            reader.expect(' ');
            return new BlockPosParticleData(particleTypeIn, BlockPos.of(reader.readLong()));
        }

        @Override
        public BlockPosParticleData fromNetwork(ParticleType<BlockPosParticleData> particleTypeIn, FriendlyByteBuf buffer)
        {
            return new BlockPosParticleData(particleTypeIn, BlockPos.of(buffer.readLong()));
        }
    };
    private final ParticleType<BlockPosParticleData> particleType;
    private final BlockPos blockPos;

    public BlockPosParticleData(ParticleType<BlockPosParticleData> particleTypeIn, BlockPos blockPos)
    {
        this.particleType = particleTypeIn;
        this.blockPos = blockPos;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer)
    {
        buffer.writeLong(this.blockPos.asLong());
    }

    @Override
    public String writeToString()
    {
        return Registry.PARTICLE_TYPE.getKey(this.getType()) + " " + this.blockPos.toString();
    }

    @Override
    public ParticleType<BlockPosParticleData> getType()
    {
        return this.particleType;
    }

    @OnlyIn(Dist.CLIENT)
    public BlockPos getBlockPos()
    {
        return this.blockPos;
    }
}