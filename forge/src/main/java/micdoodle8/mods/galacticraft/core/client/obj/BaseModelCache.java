package micdoodle8.mods.galacticraft.core.client.obj;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.SimpleModelTransform;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel.ModelSettings;

public class BaseModelCache {

    private final Map<ResourceLocation, ModelData> modelMap = new Object2ObjectOpenHashMap<>();

    public void onBake(ModelBakeEvent evt) {
        modelMap.values().forEach(m -> m.reload(evt));
    }

    public void setup() {
        modelMap.values().forEach(ModelData::setup);
    }

    public OBJModelData registerOBJ(ResourceLocation rl) {
        OBJModelData data = new OBJModelData(rl);
        modelMap.put(rl, data);
        return data;
    }

    protected JSONModelData registerJSON(ResourceLocation rl) {
        JSONModelData data = new JSONModelData(rl);
        modelMap.put(rl, data);
        return data;
    }

    public static class ModelData {

        protected IModelGeometry<?> model;

        protected final ResourceLocation rl;
        private final Map<IModelConfiguration, BakedModel> bakedMap = new Object2ObjectOpenHashMap<>();

        protected ModelData(ResourceLocation rl) {
            this.rl = rl;
        }

        protected void reload(ModelBakeEvent evt) {
            bakedMap.clear();
        }

        protected void setup() {
        }

        public BakedModel bake(IModelConfiguration config) {
            return bakedMap.computeIfAbsent(config, c -> model.bake(c, ModelLoader.instance(), ModelLoader.defaultTextureGetter(), SimpleModelTransform.IDENTITY, ItemOverrideList.EMPTY, rl));
        }

        public IModelGeometry<?> getModel() {
            return model;
        }
    }

    public static class OBJModelData extends ModelData {

        private OBJModelData(ResourceLocation rl) {
            super(rl);
        }

        @Override
        protected void reload(ModelBakeEvent evt) {
            super.reload(evt);
            model = OBJLoader.INSTANCE.loadModel(new ModelSettings(rl, true, true, true, true, null));
        }
    }

    public static class JSONModelData extends ModelData {

        private BakedModel bakedModel;

        private JSONModelData(ResourceLocation rl) {
            super(rl);
        }

        @Override
        protected void reload(ModelBakeEvent evt) {
            super.reload(evt);
            bakedModel = evt.getModelRegistry().get(rl);
            UnbakedModel unbaked = evt.getModelLoader().getUnbakedModel(rl);
            if (unbaked instanceof BlockModel) {
                model = ((BlockModel) unbaked).customData.getCustomGeometry();
            }
        }

        @Override
        protected void setup() {
            ModelLoader.addSpecialModel(rl);
        }

        public BakedModel getBakedModel() {
            return bakedModel;
        }
    }
}