package micdoodle8.mods.galacticraft.core.blocks;

import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public abstract class BlockThermalAir extends AirBlock
{
    public static final BooleanProperty THERMAL = BooleanProperty.create("thermal");

    public BlockThermalAir(Properties builder)
    {
        super(builder);
    }
}
