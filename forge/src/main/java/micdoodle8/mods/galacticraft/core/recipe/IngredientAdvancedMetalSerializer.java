package micdoodle8.mods.galacticraft.core.recipe;

import com.google.gson.JsonObject;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.venus.items.VenusItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;

import java.util.stream.Stream;

public class IngredientAdvancedMetalSerializer implements IIngredientSerializer<Ingredient>
{
    public static final IngredientAdvancedMetalSerializer INSTANCE = new IngredientAdvancedMetalSerializer();

    public Ingredient parse(FriendlyByteBuf buffer)
    {
        return Ingredient.fromValues(Stream.generate(() -> new Ingredient.ItemValue(buffer.readItem())).limit(buffer.readVarInt()));
    }

    public Ingredient parse(JsonObject json)
    {
        String metal = GsonHelper.getAsString(json, "metal");
        if (metal.equals("meteoric_iron_ingot"))
        {
            return Ingredient.of(new ItemStack(GCItems.METEORIC_IRON_INGOT, 1));// : new OreIngredient("ingotMeteoricIron");
        }
        if (metal.equals("meteoric_iron_plate"))
        {
            return Ingredient.of(new ItemStack(GCItems.COMPRESSED_METEORIC_IRON, 1));// : new OreIngredient("compressedMeteoricIron");
        }
        if (metal.equals("desh_ingot"))
        {
            return GalacticraftCore.isPlanetsLoaded ? Ingredient.of(new ItemStack(MarsItems.DESH_INGOT, 1)) : Ingredient.of(GCItems.TIER_1_HEAVY_DUTY_PLATE);
        }
        if (metal.equals("desh_plate"))
        {
            return Ingredient.of(new ItemStack(MarsItems.COMPRESSED_DESH, 1));// : new OreIngredient("compressedDesh");
        }
        if (metal.equals("titanium_ingot"))
        {
            return Ingredient.of(new ItemStack(AsteroidsItems.TITANIUM_INGOT, 1));// : new OreIngredient("ingotTitanium");
        }
        if (metal.equals("titanium_plate"))
        {
            return Ingredient.of(new ItemStack(AsteroidsItems.COMPRESSED_TITANIUM, 1));// : new OreIngredient("compressedTitanium");
        }
        if (metal.equals("lead_ingot"))
        {
            return Ingredient.of(new ItemStack(VenusItems.LEAD_INGOT, 1));// : new OreIngredient("ingotLead");
        }
        return Ingredient.of(GCItems.INFINITE_BATTERY);
    }

    public void write(FriendlyByteBuf buffer, Ingredient ingredient)
    {
        ItemStack[] items = ingredient.getItems();
        buffer.writeVarInt(items.length);

        for (ItemStack stack : items)
            buffer.writeItem(stack);
    }
}
