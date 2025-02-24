package cn.elytra.mod.nomi_horizons.mixins.gt;

import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityMultiblockNotifiablePart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = MetaTileEntityMultiblockNotifiablePart.class, remap = false)
public interface MultiblockNotifiablePart_Accessor {

    @Accessor("isExportHatch")
    boolean isExportHatch();

}
