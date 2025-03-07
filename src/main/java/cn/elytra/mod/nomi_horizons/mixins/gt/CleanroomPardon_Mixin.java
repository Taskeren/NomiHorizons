package cn.elytra.mod.nomi_horizons.mixins.gt;

import cn.elytra.mod.nomi_horizons.NomiHorizons;
import gregtech.api.capability.impl.AbstractRecipeLogic;
import gregtech.api.capability.impl.MultiblockRecipeLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.multiblock.CleanroomType;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.recipeproperties.CleanroomProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = MultiblockRecipeLogic.class, remap = false)
public abstract class CleanroomPardon_Mixin extends AbstractRecipeLogic {

    public CleanroomPardon_Mixin(MetaTileEntity tileEntity, RecipeMap<?> recipeMap) {
        super(tileEntity, recipeMap);
    }

    @Override
    protected boolean checkCleanroomRequirement(@NotNull Recipe recipe) {
        // get the required cleanroom type, and check if it is pardoned.
        @Nullable CleanroomType requiredType = recipe.getProperty(CleanroomProperty.getInstance(), null);
        if(requiredType == CleanroomType.CLEANROOM && NomiHorizons.API.isCleanroomPardoned()) {
            return true;
        }
        if(requiredType == CleanroomType.STERILE_CLEANROOM && NomiHorizons.API.isSterileCleanroomPardoned()) {
            return true;
        }
        // if the required type is not pardoned (null), we check it by super method.
        return super.checkCleanroomRequirement(recipe);
    }

}
