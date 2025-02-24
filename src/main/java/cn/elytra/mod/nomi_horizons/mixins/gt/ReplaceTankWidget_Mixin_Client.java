package cn.elytra.mod.nomi_horizons.mixins.gt;

import cn.elytra.mod.nomi_horizons.client.ModText;
import cn.elytra.mod.nomi_horizons.xmod.gt.QuantumTankWidget;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.TankWidget;
import gregtech.common.metatileentities.storage.MetaTileEntityQuantumTank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = MetaTileEntityQuantumTank.class, remap = false)
public class ReplaceTankWidget_Mixin_Client {

    @Inject(method = "addInformation", at = @At("RETURN"))
    private void addInfo(ItemStack stack, @Nullable World player, List<String> tooltip, boolean advanced, CallbackInfo ci) {
        tooltip.add(ModText.getDynamicPresentedByNomiHorizons());
    }
}
