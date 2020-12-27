package micdoodle8.mods.galacticraft.core.recipe;

import com.google.gson.JsonObject;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.venus.items.VenusItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.common.crafting.IIngredientSerializer;

import java.util.stream.Stream;

public class IngredientAdvancedMetalSerializer implements IIngredientSerializer<Ingredient>
{
    public static final IngredientAdvancedMetalSerializer INSTANCE = new IngredientAdvancedMetalSerializer();

    public Ingredient parse(PacketBuffer buffer)
    {
        return Ingredient.fromItemListStream(Stream.generate(() -> new Ingredient.SingleItemList(buffer.readItemStack())).limit(buffer.readVarInt()));
    }

    public Ingredient parse(JsonObject json)
    {
        String metal = JSONUtils.getString(json, "metal");
        if (metal.equals("meteoric_iron_ingot"))
        {
            return Ingredient.fromStacks(new ItemStack(GCItems.METEORIC_IRON_INGOT, 1));// : new OreIngredient("ingotMeteoricIron");
        }
        if (metal.equals("meteoric_iron_plate"))
        {
            return Ingredient.fromStacks(new ItemStack(GCItems.COMPRESSED_METEORIC_IRON, 1));// : new OreIngredient("compressedMeteoricIron");
        }
        if (metal.equals("desh_ingot"))
        {
            return GalacticraftCore.isPlanetsLoaded ? Ingredient.fromStacks(new ItemStack(MarsItems.DESH_INGOT, 1)) : Ingredient.fromItems(GCItems.TIER_1_HEAVY_DUTY_PLATE);
        }
        if (metal.equals("desh_plate"))
        {
            return Ingredient.fromStacks(new ItemStack(MarsItems.COMPRESSED_DESH, 1));// : new OreIngredient("compressedDesh");
        }
        if (metal.equals("titanium_ingot"))
        {
            return Ingredient.fromStacks(new ItemStack(AsteroidsItems.ingotTitanium, 1));// : new OreIngredient("ingotTitanium");
        }
        if (metal.equals("titanium_plate"))
        {
            return Ingredient.fromStacks(new ItemStack(AsteroidsItems.compressedTitanium, 1));// : new OreIngredient("compressedTitanium");
        }
        if (metal.equals("lead_ingot"))
        {
            return Ingredient.fromStacks(new ItemStack(VenusItems.ingotLead, 1));// : new OreIngredient("ingotLead");
        }
        return Ingredient.fromItems(GCItems.INFINITE_BATTERY);
    }

    public void write(PacketBuffer buffer, Ingredient ingredient)
    {
        ItemStack[] items = ingredient.getMatchingStacks();
        buffer.writeVarInt(items.length);

        for (ItemStack stack : items)
            buffer.writeItemStack(stack);
    }
}
