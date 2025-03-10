package cn.elytra.mod.nomi_horizons.xmod.gt;

import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.map.AbstractMapIngredient;
import gregtech.api.util.GTUtility;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

/**
 * The interface describe the Mixin-injected methods to {@link RecipeMap RecipeMaps} by NewHorizons.
 * Every instance of RecipeMap should implement INewHorizonsRecipeMap when the Mixin is applied.
 * But you'd better to check with {@code instanceof} before using these methods, just in case.
 * The names are prefixed with {@code nh$} because of the recommendation of Mixin-injected methods.
 * <p>
 * It is implemented in Mixin {@link cn.elytra.mod.nomi_horizons.mixins.gt.better_recipe.BetterRecipeMap_Mixin},
 * and the implementation of searching the matching recipes are referred from <a href="https://github.com/GregTechCEu/GregTech/pull/2372">GregTech#2372</a>.
 */
public interface INewHorizonsRecipeMap {

    /**
     * Finds a Recipe matching the Fluid and/or ItemStack Inputs with additional conditions.
     *
     * @param voltage         Voltage of the Machine or Long.MAX_VALUE if it has no Voltage
     * @param inputs          the Item Inputs
     * @param fluidInputs     the Fluid Inputs
     * @param exactVoltage    should require exact voltage matching on recipe.
     * @param addedConditions the additional conditions.
     * @return the Recipe it has found or null for no matching Recipe.
     */
    @Nullable
    Recipe nh$findRecipe(long voltage, List<ItemStack> inputs, List<FluidStack> fluidInputs, boolean exactVoltage, Predicate<Recipe> addedConditions);

    /**
     * Finds a Recipe matching the Fluid and/or ItemStack Inputs with additional conditions.
     *
     * @param voltage         Voltage of the Machine or Long.MAX_VALUE if it has no Voltage
     * @param inputs          the Item Inputs
     * @param fluidInputs     the Fluid Inputs
     * @param exactVoltage    should require exact voltage matching on recipe.
     * @param addedConditions the additional conditions.
     * @return the Recipe it has found or null for no matching Recipe.
     */
    @Nullable
    default Recipe nh$findRecipe(long voltage, IItemHandlerModifiable inputs, IMultipleTankHandler fluidInputs, boolean exactVoltage, Predicate<Recipe> addedConditions) {
        return nh$findRecipe(voltage, GTUtility.itemHandlerToList(inputs), GTUtility.fluidHandlerToList(fluidInputs), exactVoltage, addedConditions);
    }

    /**
     * Finds all the Recipes matching the Fluid and/or ItemStack Inputs with additional conditions.
     *
     * @param voltage         Voltage of the Machine or Long.MAX_VALUE if it has no Voltage
     * @param inputs          the Item Inputs
     * @param fluidInputs     the Fluid Inputs
     * @param exactVoltage    should require exact voltage matching on recipe.
     * @param addedConditions the additional conditions.
     * @return the Iterator of the Recipes it has found or empty Iterator for no matching Recipe.
     */
    @NotNull
    Iterator<Recipe> nh$findAllRecipe(long voltage, List<ItemStack> inputs, List<FluidStack> fluidInputs, boolean exactVoltage, Predicate<Recipe> addedConditions);

    /**
     * Finds all the Recipes matching the Fluid and/or ItemStack Inputs with additional conditions.
     *
     * @param voltage         Voltage of the Machine or Long.MAX_VALUE if it has no Voltage
     * @param inputs          the Item Inputs
     * @param fluidInputs     the Fluid Inputs
     * @param exactVoltage    should require exact voltage matching on recipe.
     * @param addedConditions the additional conditions.
     * @return the Iterator of the Recipes it has found or empty Iterator for no matching Recipe.
     */
    @NotNull
    default Iterator<Recipe> nh$findAllRecipe(long voltage, IItemHandlerModifiable inputs, IMultipleTankHandler fluidInputs, boolean exactVoltage, Predicate<Recipe> addedConditions) {
        return nh$findAllRecipe(voltage, GTUtility.itemHandlerToList(inputs), GTUtility.fluidHandlerToList(fluidInputs), exactVoltage, addedConditions);
    }
}
