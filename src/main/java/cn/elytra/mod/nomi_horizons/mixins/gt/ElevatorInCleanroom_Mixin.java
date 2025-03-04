package cn.elytra.mod.nomi_horizons.mixins.gt;

import cn.elytra.mod.nomi_horizons.NomiHorizons;
import cn.elytra.mod.nomi_horizons.NomiHorizonsAPI;
import gregtech.api.pattern.TraceabilityPredicate;
import gregtech.common.metatileentities.multi.electric.MetaTileEntityCleanroom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = MetaTileEntityCleanroom.class, remap = false)
public class ElevatorInCleanroom_Mixin {

    @Unique
    private static TraceabilityPredicate nh$elevatorPredicate() {
        NomiHorizonsAPI api = NomiHorizons.API;
        return api.getElevatorPredicate().setMaxGlobalLimited(api.getMaxElevatorCount());
    }

    @Redirect(method = "createStructurePattern", at = @At(value = "INVOKE", target = "Lgregtech/api/pattern/TraceabilityPredicate;or(Lgregtech/api/pattern/TraceabilityPredicate;)Lgregtech/api/pattern/TraceabilityPredicate;", ordinal = 2))
    private TraceabilityPredicate nh$injectElevatorPredicate(TraceabilityPredicate instance, TraceabilityPredicate traceabilityPredicate) {
        return instance.or(nh$elevatorPredicate()).or(traceabilityPredicate);
    }

}
