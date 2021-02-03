package micdoodle8.mods.galacticraft.core;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class GCMixinConnector implements IMixinConnector
{
    @Override
    public void connect()
    {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.galacticraftcore.json");
    }
}