package cn.elytra.mod.nomi_horizons.mixins.gt;

import cn.elytra.mod.nomi_horizons.xmod.gt.QuantumTankWidget;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.TankWidget;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityFluidHatch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.IFluidTank;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MetaTileEntityFluidHatch.class, remap = false)
public class ReplaceTankWidget_Hatch_Mixin {

    @Inject(method = "createTankUI", at = @At(value = "INVOKE", target = "Lgregtech/api/gui/ModularUI$Builder;image(IIIILgregtech/api/gui/resources/IGuiTexture;)Lgregtech/api/gui/ModularUI$Builder;"))
    private void replace(IFluidTank fluidTank, String title, EntityPlayer entityPlayer, CallbackInfoReturnable<ModularUI.Builder> cir, @Local LocalRef<TankWidget> widgetLocalRef) {
        var tank = widgetLocalRef.get();
        var newTank = QuantumTankWidget.create((MetaTileEntityFluidHatch) (Object) this, tank);
        widgetLocalRef.set(newTank);
    }

}
