package cn.elytra.mod.nomi_horizons.mixins.gt;

import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityFluidHatch;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = MetaTileEntityFluidHatch.class, remap = false)
public interface FluidHatch_Accessor extends MultiblockNotifiablePart_Accessor {

    @Accessor("lockedFluid")
    FluidStack getLockedFluid();

}
