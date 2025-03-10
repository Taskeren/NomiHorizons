package cn.elytra.mod.nomi_horizons.mixins.gt.better_recipe;

import cn.elytra.mod.nomi_horizons.utils.MixinSharedCodes;
import cn.elytra.mod.nomi_horizons.xmod.gt.INewHorizonsRecipeMap;
import com.google.common.collect.AbstractIterator;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.map.AbstractMapIngredient;
import gregtech.api.recipes.map.Branch;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Mixin(value = RecipeMap.class, remap = false)
public abstract class BetterRecipeMap_Mixin implements INewHorizonsRecipeMap {

    @Shadow
    @Final
    private Branch lookup;

    @Shadow
    @Nullable
    public abstract Recipe find(@NotNull Collection<ItemStack> items, @NotNull Collection<FluidStack> fluids, @NotNull Predicate<Recipe> canHandle);

    @Shadow
    @Nullable
    protected abstract List<List<AbstractMapIngredient>> prepareRecipeFind(@NotNull Collection<ItemStack> items, @NotNull Collection<FluidStack> fluids);

    @Shadow
    @Nullable
    protected abstract Recipe recurseIngredientTreeFindRecipe(@NotNull List<List<AbstractMapIngredient>> ingredients, @NotNull Branch branchMap, @NotNull Predicate<Recipe> canHandle, int index, int count, long skip);

    @Unique
    @Override
    public Recipe nh$findRecipe(long voltage, List<ItemStack> inputs, List<FluidStack> fluidInputs, boolean exactVoltage, Predicate<Recipe> addedConditions) {
        List<ItemStack> items = inputs.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
        List<FluidStack> fluids = fluidInputs.stream().filter(f -> f != null && f.amount != 0).collect(Collectors.toList());

        var primaryConditions = MixinSharedCodes.createPrimaryConditions(voltage, inputs, fluids, exactVoltage);
        var canHandle = addedConditions == null ? primaryConditions : primaryConditions.and(addedConditions);
        return find(items, fluids, canHandle);
    }

    @Unique
    @Override
    @NotNull
    public Iterator<Recipe> nh$findAllRecipe(long voltage, List<ItemStack> inputs, List<FluidStack> fluidInputs, boolean exactVoltage, Predicate<Recipe> addedConditions) {
        List<ItemStack> items = inputs.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
        List<FluidStack> fluids = fluidInputs.stream().filter(f -> f != null && f.amount != 0).collect(Collectors.toList());

        List<List<AbstractMapIngredient>> list = prepareRecipeFind(items, fluids);
        if(list == null) return Collections.emptyIterator();

        var primaryConditions = MixinSharedCodes.createPrimaryConditions(voltage, inputs, fluids, exactVoltage);
        var canHandle = addedConditions == null ? primaryConditions : primaryConditions.and(addedConditions);
        return nh$findAllRecipeImpl(list, canHandle);
    }

    @Unique
    @Nullable
    public Recipe nh$recurseIngredientTreeFindRecipe(@NotNull List<List<AbstractMapIngredient>> ingredients, @NotNull Branch branchMap, @NotNull Predicate<Recipe> canHandle, int index, int count, long skip) {
        // used to delegate the method call inside the AbstractIterator below
        return recurseIngredientTreeFindRecipe(ingredients, lookup, canHandle, index, 0, (1L << index));
    }

    @Unique
    @NotNull
    private Iterator<Recipe> nh$findAllRecipeImpl(List<List<AbstractMapIngredient>> ingredients, Predicate<Recipe> canHandle) {
        return new AbstractIterator<>() {
            private int index;

            @Override
            protected Recipe computeNext() {
                if(ingredients == null) {
                    return endOfData();
                }

                while(index < ingredients.size()) {
                    Recipe r = nh$recurseIngredientTreeFindRecipe(ingredients, lookup, canHandle, index, 0, (1L << index));
                    index++;
                    if(r != null) {
                        return r;
                    }
                }

                return endOfData();
            }
        };
    }
}
