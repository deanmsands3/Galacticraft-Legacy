package micdoodle8.mods.galacticraft.core.client.obj;

import java.util.*;

import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;

public class GCModelCache extends BaseModelCache {

    public static final GCModelCache INSTANCE = new GCModelCache();
    private final Set<Runnable> callbacks = new HashSet<>();
    private static final IModelConfiguration contentsConfiguration = new ContentsModelConfiguration();

    private Map<ResourceLocation, OBJModel> loadedModelCache = new HashMap<>();
    private Map<VisibilityEntry, BakedModel> visibleModelCache = new HashMap<>();

    public GCModelCache() {
    }

    /**
     * @param visibleGroups A list of all visible groups from .obj file. If the provided list is empty,
     *                              all parts will be visible
     */
    public BakedModel getModel(ResourceLocation location, List<String> visibleGroups)
    {
        OBJModel model = loadedModelCache.computeIfAbsent(location, loc -> OBJLoader.INSTANCE.loadModel(new OBJModel.ModelSettings(location, false, true, true, true, null)));
        return visibleModelCache.computeIfAbsent(new VisibilityEntry(location, visibleGroups), visEntry -> {
            if (visibleGroups.isEmpty()) {
                return model.bake(new VisibleModelConfiguration(contentsConfiguration, visibleGroups), ModelLoader.instance(), material -> material.getSprite(), ModelRotation.X0_Y0, ItemOverrides.EMPTY, location);
            } else {
                return model.bake(new VisibleModelConfiguration(contentsConfiguration, visibleGroups),
                        ModelLoader.instance(), material -> material.getSprite(), ModelRotation.X0_Y0, ItemOverrides.EMPTY, location);
            }
        });
    }

    @Override
    public void onBake(ModelBakeEvent evt) {
        super.onBake(evt);
        callbacks.forEach(Runnable::run);
        loadedModelCache.clear();
        visibleModelCache.clear();
    }

    public void reloadCallback(Runnable callback) {
        callbacks.add(callback);
    }

    private class VisibilityEntry
    {
        ResourceLocation location;
        List<String> visible;

        public VisibilityEntry(ResourceLocation location, List<String> visible) {
            this.location = location;
            this.visible = visible;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            VisibilityEntry that = (VisibilityEntry) o;
            return Objects.equals(location, that.location) &&
                    Objects.equals(visible, that.visible);
        }

        @Override
        public int hashCode() {
            return Objects.hash(location, visible);
        }
    }
}