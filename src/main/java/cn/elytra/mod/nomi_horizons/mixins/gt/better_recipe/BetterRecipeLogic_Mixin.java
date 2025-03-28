package cn.elytra.mod.nomi_horizons.mixins.gt.better_recipe;

import cn.elytra.mod.nomi_horizons.NomiHorizons;
import cn.elytra.mod.nomi_horizons.NomiHorizonsAPI;
import cn.elytra.mod.nomi_horizons.xmod.gt.INewHorizonsRecipeMap;
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.capability.impl.AbstractRecipeLogic;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AbstractRecipeLogic.class, remap = false)
public abstract class BetterRecipeLogic_Mixin {

    @Shadow
    public abstract long getMaxVoltage();

    @Shadow
    protected abstract IItemHandlerModifiable getInputInventory();

    @Shadow
    protected abstract IMultipleTankHandler getInputTank();

    @Shadow
    protected abstract boolean checkPreviousRecipe();

    @Shadow
    protected Recipe previousRecipe;

    @Shadow
    public abstract boolean checkRecipe(@NotNull Recipe recipe);

    @Shadow
    public abstract boolean prepareRecipe(Recipe recipe);

    @Shadow
    protected boolean isOutputsFull;

    @Shadow
    protected boolean invalidInputsForRecipes;

    @Shadow @Nullable public abstract RecipeMap<?> getRecipeMap();

    @Inject(method = "trySearchNewRecipe", at = @At("HEAD"), cancellable = true)
    private void nh$hookTrySearchNewRecipe(CallbackInfo ci) {
        if(NomiHorizons.API.useAdvancedRecipeLogic()) { // redirect into my logic, if enabled
            nh$trySearchNewRecipeAdvanced();
            ci.cancel();
        }
    }

    @SuppressWarnings("UnusedAssignment")
    @Unique
    protected void nh$trySearchNewRecipeAdvanced() {
        long maxVoltage = getMaxVoltage();
        IItemHandlerModifiable importInventory = getInputInventory();
        IMultipleTankHandler importFluids = getInputTank();

        // check the previous recipe, which its inputs are also existent.
        // we need also to check if it still meets the requirement, and if there is enough space to dump the outputs.
        if(checkPreviousRecipe()) {
            // If there is no active recipe, then we need to find one.
            var currentRecipe = this.previousRecipe;
            if(currentRecipe != null && checkRecipe(currentRecipe) && prepareRecipe(currentRecipe)) {
                this.previousRecipe = currentRecipe;
                return;
            }
        }

        var recipeMap = getRecipeMap();
        if(!(recipeMap instanceof INewHorizonsRecipeMap nRecipeMap)) {
            throw new RuntimeException("RecipeMaps are expected to be injected with INewHorizonsRecipeMap, but they are not!");
        }

        var iter = nRecipeMap.nh$findAllRecipe(maxVoltage, importInventory, importFluids, false, null);
        // 2 vars below are used store the states during the iteration.
        // the values in "this" will be reset to false every iteration,
        // and we need to fix their values at the end of the iteration,
        // so that any changes in buses/hatches will trigger the recipe re-scan.
        var mIsOutputsFull = false;
        var mInvalidInputsForRecipes = false;
        while(iter.hasNext()) {
            var recipe = iter.next();
            if(checkRecipe(recipe) && prepareRecipe(recipe)) {
                this.previousRecipe = recipe;
                return;
            } else {
                mIsOutputsFull = this.isOutputsFull;
                mInvalidInputsForRecipes = this.invalidInputsForRecipes;
                this.isOutputsFull = false;
                this.invalidInputsForRecipes = false;
            }
        }

        // try fallback to #findRecipe, in case of recipe maps that provides additional recipes
        // in #findRecipe.
        var defaultRecipe = recipeMap.findRecipe(maxVoltage, importInventory, importFluids);
        if(defaultRecipe != null) {
            if(checkRecipe(defaultRecipe) && prepareRecipe(defaultRecipe)) {
                this.previousRecipe = defaultRecipe;
                return;
            } else {
                mIsOutputsFull = this.isOutputsFull;
                mInvalidInputsForRecipes = this.invalidInputsForRecipes;
                this.isOutputsFull = false;
                this.invalidInputsForRecipes = false;
            }
        }

        this.isOutputsFull = mIsOutputsFull;
        this.invalidInputsForRecipes = mInvalidInputsForRecipes;
    }

}
