package team.galacticraft.galacticraft.common.core.entities;

public interface IBubbleProvider
{
    float getBubbleSize();

    void setBubbleVisible(boolean shouldRender);

    boolean getBubbleVisible();
}
