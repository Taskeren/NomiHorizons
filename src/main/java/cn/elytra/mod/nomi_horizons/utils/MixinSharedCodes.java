package cn.elytra.mod.nomi_horizons.utils;

import gregtech.api.recipes.Recipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.function.Predicate;

/**
 * This class holds the shared codes of Mixins.
 * <p>
 * I personally don't like to add too many Unique methods in Mixins, so the extractable methods will be moved to this class.
 */
@ApiStatus.Internal
public class MixinSharedCodes {

    public static Predicate<Recipe> createPrimaryConditions(long voltage, List<ItemStack> inputs, List<FluidStack> fluidInputs, boolean exactVoltage) {
        return (recipe) -> {
            if(exactVoltage && recipe.getEUt() != voltage) {
                // if exact voltage is required, the recipe is not considered valid
                return false;
            }
            if(recipe.getEUt() > voltage) {
                // there is not enough voltage to consider the recipe valid
                return false;
            }
            return recipe.matches(false, inputs, fluidInputs);
        };
    }
}
