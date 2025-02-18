package cn.elytra.mod.nomi_horizons.mixins.xu2;

import com.llamalad7.mixinextras.sugar.Local;
import com.rwtema.extrautils2.tile.TileDrum;
import gregtech.api.util.GTUtility;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value = TileDrum.class, remap = false)
public class FixDrumFluidLocalization_Mixin {

    @ModifyArgs(method = "onBlockActivated", at = @At(value = "INVOKE", target = "Lcom/rwtema/extrautils2/utils/Lang;chat(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/util/text/TextComponentTranslation;", ordinal = 1))
    private void nh$replaceGTFluidUnlocalizedName(Args args, @Local FluidStack fluidStack) {
        var formatArgs = args.<Object[]>get(1); // get the Object[] used for formatting
        var fluidName = GTUtility.getFluidTranslation(fluidStack); // get the proper GT fluid translation text component
        formatArgs[0] = fluidName; // replace
    }

}
