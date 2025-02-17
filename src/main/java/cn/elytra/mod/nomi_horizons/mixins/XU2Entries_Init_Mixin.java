package cn.elytra.mod.nomi_horizons.mixins;

import cn.elytra.mod.nomi_horizons.NomiHorizons;
import cn.elytra.mod.nomi_horizons.xmod.xu2.DrumItemBlock;
import com.rwtema.extrautils2.backend.entries.BlockClassEntry;
import com.rwtema.extrautils2.backend.entries.XU2Entries;
import com.rwtema.extrautils2.blocks.BlockDrum;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = XU2Entries.class, remap = false)
public class XU2Entries_Init_Mixin {

    @Shadow
    public static BlockClassEntry<BlockDrum> drum;

    @Inject(method = "init", at = @At("RETURN"))
    private static void nh$injectAfter(CallbackInfo ci) {
        drum.setItemClass(DrumItemBlock.class);
        NomiHorizons.LOG.info("Replaced BlockItem class of {} with {}", BlockDrum.class, DrumItemBlock.class);
    }

}
