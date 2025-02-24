package cn.elytra.mod.nomi_horizons.mixins.gt;

import gregtech.common.metatileentities.storage.MetaTileEntityQuantumTank;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = MetaTileEntityQuantumTank.class, remap = false)
public interface QuantumTank_Accessor {

    @Accessor("lockedFluid")
    FluidStack getLockedFluid();

}
