package team.galacticraft.galacticraft.common.core.advancement.criterion;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import team.galacticraft.galacticraft.common.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GenericTrigger implements CriterionTrigger
{
    private final ResourceLocation id;
    private final Map<PlayerAdvancements, Listeners> listeners = Maps.newHashMap();

    public GenericTrigger(String id)
    {
        super();
        this.id = new ResourceLocation(Constants.MOD_ID_CORE, id);
    }

    @Override
    public ResourceLocation getId()
    {
        return id;
    }

    @Override
    public void addPlayerListener(PlayerAdvancements playerAdvancements, Listener listener)
    {
        Listeners listeners = this.listeners.get(playerAdvancements);

        if (listeners == null)
        {
            listeners = new Listeners(playerAdvancements);
            this.listeners.put(playerAdvancements, listeners);
        }

        listeners.add(listener);
    }

    @Override
    public void removePlayerListener(PlayerAdvancements advancements, Listener listener)
    {
        Listeners listeners = this.listeners.get(advancements);

        if (listeners != null)
        {
            listeners.remove(listener);

            if (listeners.isEmpty())
            {
                this.listeners.remove(advancements);
            }
        }
    }

    @Override
    public void removePlayerListeners(PlayerAdvancements advancements)
    {
        listeners.remove(advancements);
    }

    /**
     * Trigger.
     *
     * @param playerMP the player
     */
    public void trigger(ServerPlayer playerMP)
    {
        Listeners listeners = this.listeners.get(playerMP.getAdvancements());

        if (listeners != null)
        {
            listeners.trigger(playerMP);
        }
    }

    public static abstract class Instance extends AbstractCriterionTriggerInstance
    {
        public Instance(ResourceLocation resourceLocation)
        {
            super(resourceLocation);
        }

        public abstract boolean test(ServerPlayer player);
    }

    static class Listeners
    {
        private final PlayerAdvancements playerAdvancements;
        private final Set<Listener<?>> listeners = Sets.newHashSet();

        public Listeners(PlayerAdvancements advancements)
        {
            playerAdvancements = advancements;
        }

        public boolean isEmpty()
        {
            return listeners.isEmpty();
        }

        public void add(Listener<?> listener)
        {
            listeners.add(listener);
        }

        public void remove(Listener<?> listener)
        {
            listeners.remove(listener);
        }

        public void trigger(ServerPlayer player)
        {
            List<Listener<?>> list = null;

            for (Listener<?> listener : listeners)
            {
                if (listener.getTriggerInstance() instanceof Instance)
                {
                    if (((Instance) listener.getTriggerInstance()).test(player))
                    {
                        if (list == null)
                        {
                            list = new ArrayList<>(1);
                        }

                        list.add(listener);
                    }
                }
            }

            if (list != null)
            {
                for (Listener<?> listener1 : list)
                {
                    listener1.run(playerAdvancements);
                }
            }
        }
    }
}