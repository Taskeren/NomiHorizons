package cn.elytra.mod.nomi_horizons.mixins.gt;

import cn.elytra.mod.nomi_horizons.xmod.gt.QuantumTankWidget;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.TankWidget;
import gregtech.common.metatileentities.storage.MetaTileEntityQuantumTank;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MetaTileEntityQuantumTank.class, remap = false)
public class ReplaceTankWidget_Tank_Mixin {

    @Inject(method = "createUI", at = @At(value = "INVOKE", target = "Lgregtech/api/gui/ModularUI;defaultBuilder()Lgregtech/api/gui/ModularUI$Builder;"))
    private void replace(EntityPlayer entityPlayer, CallbackInfoReturnable<ModularUI> cir, @Local LocalRef<TankWidget> widgetLocalRef) {
        var tank = widgetLocalRef.get();
        var newTank = QuantumTankWidget.create((MetaTileEntityQuantumTank) (Object) this, tank);
        widgetLocalRef.set(newTank);
    }
}
