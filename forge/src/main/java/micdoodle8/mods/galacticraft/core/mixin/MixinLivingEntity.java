package micdoodle8.mods.galacticraft.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import micdoodle8.mods.galacticraft.core.TransformerHooks;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

@Mixin(LivingEntity.class)
public class MixinLivingEntity
{
    @Redirect(method = "travel(Lnet/minecraft/util/math/Vec3d;)V", at = @At(value = "INVOKE", target = "net/minecraft/entity/ai/attributes/IAttributeInstance.getValue()D", ordinal = 0))
    private double addGravityForEntity(AttributeInstance defaultGravity)
    {
        return TransformerHooks.getGravityForEntity((LivingEntity)(Object)this);
    }
}