package team.galacticraft.galacticraft.common.core.entities.player;

import com.mojang.authlib.GameProfile;
import team.galacticraft.galacticraft.common.api.entity.ICameraZoomEntity;
import team.galacticraft.galacticraft.common.api.item.IHoldableItem;
import team.galacticraft.galacticraft.common.api.prefab.entity.EntitySpaceshipBase;
import team.galacticraft.galacticraft.common.api.world.IZeroGDimension;
import team.galacticraft.galacticraft.common.core.client.EventHandlerClient;
import team.galacticraft.galacticraft.common.core.proxy.ClientProxyCore;
import team.galacticraft.galacticraft.common.core.util.ConfigManagerCore;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.resources.ResourceLocation;

public class GCEntityOtherPlayerMP extends RemotePlayer
{
    private boolean checkedCape = false;
    private ResourceLocation galacticraftCape = null;

    public GCEntityOtherPlayerMP(ClientLevel world, GameProfile profile)
    {
        super(world, profile);
    }

    @Override
    public ResourceLocation getCloakTextureLocation()
    {
        if (this.getVehicle() instanceof EntitySpaceshipBase)
        {
            // Don't draw any cape if riding a rocket (the cape renders outside the rocket model!)
            return null;
        }

        ResourceLocation vanillaCape = super.getCloakTextureLocation();

        if (!this.checkedCape)
        {
            PlayerInfo networkplayerinfo = this.getPlayerInfo();
            this.galacticraftCape = ClientProxyCore.capeMap.get(networkplayerinfo.getProfile().getId().toString().replace("-", ""));
            this.checkedCape = true;
        }

        if ((ConfigManagerCore.overrideCapes.get() || vanillaCape == null) && galacticraftCape != null)
        {
            return galacticraftCape;
        }

        return vanillaCape;
    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public int getBrightnessForRender()
//    {
//        double height = this.posY + (double) this.getEyeHeight();
//        if (height > 255D)
//        {
//            height = 255D;
//        }
//        BlockPos blockpos = new BlockPos(this.posX, height, this.posZ);
//        return this.world.isBlockLoaded(blockpos) ? this.world.getCombinedLight(blockpos, 0) : 0;
//    }

    @Override
    public boolean isShiftKeyDown()
    {
        if (EventHandlerClient.sneakRenderOverride && !(this.level.getDimension() instanceof IZeroGDimension))
        {
            if (this.onGround && this.inventory.getSelected() != null && this.inventory.getSelected().getItem() instanceof IHoldableItem && !(this.getVehicle() instanceof ICameraZoomEntity))
            {
                IHoldableItem holdableItem = (IHoldableItem) this.inventory.getSelected().getItem();

                if (holdableItem.shouldCrouch(this))
                {
                    return true;
                }
            }
        }
        return super.isShiftKeyDown();
    }
}
