package team.galacticraft.galacticraft.common.core.entities;

public interface ITumblable
{
    void setTumbling(float value);

    float getTumbleAngle(float partial);

    float getTumbleAxisX();

    float getTumbleAxisZ();
}
