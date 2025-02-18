package cn.elytra.mod.nomi_horizons.mixins.gt;

import gregtech.api.items.metaitem.FilteredFluidStats;
import gregtech.common.items.MetaItem1;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = MetaItem1.class, remap = false)
public class ModifyCellCapacity_Mixin {

    @Redirect(method = "registerSubItems", at = @At(value = "NEW", target = "(IIZZZZZ)Lgregtech/api/items/metaitem/FilteredFluidStats;", ordinal = 0))
    private FilteredFluidStats nh$modifyCellCapacity(int capacity, int maxFluidTemperature, boolean gasProof, boolean acidProof, boolean cryoProof, boolean plasmaProof, boolean allowPartialFill) {
        // set capacity to 144
        return new FilteredFluidStats(144, maxFluidTemperature, gasProof, acidProof, cryoProof, plasmaProof, allowPartialFill);
    }

}
