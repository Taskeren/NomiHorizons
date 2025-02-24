package cn.elytra.mod.nomi_horizons.mixins.gt;

import cn.elytra.mod.nomi_horizons.client.ModText;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityFluidHatch;
import gregtech.common.metatileentities.storage.MetaTileEntityQuantumTank;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = {MetaTileEntityQuantumTank.class, MetaTileEntityFluidHatch.class}, remap = false)
public class ReplaceTankWidget_Mixin_Client {

    @Inject(method = "addInformation", at = @At("RETURN"))
    private void addInfo(ItemStack stack, @Nullable World player, List<String> tooltip, boolean advanced, CallbackInfo ci) {
        tooltip.add(ModText.getDynamicPresentedByNomiHorizons());
    }
}
