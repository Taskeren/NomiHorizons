package cn.elytra.mod.nomi_horizons.mixins.gt;

import cn.elytra.mod.nomi_horizons.NomiHorizons;
import cn.elytra.mod.nomi_horizons.NomiHorizonsAPI;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = RecipeMapMultiblockController.class, remap = false)
public class CleanroomPardonInfo_Mixin {

    @Inject(method = "getDataInfo", at = @At("RETURN"))
    private void nh$addCleanroomPardonInfo(CallbackInfoReturnable<List<ITextComponent>> cir) {
        List<ITextComponent> list = cir.getReturnValue();

        NomiHorizonsAPI api = NomiHorizons.API;
        String KEY_CLEANROOM_NAME = "gregtech.recipe.cleanroom.display_name";
        String KEY_STERILE_CLEANROOM_NAME = "gregtech.recipe.cleanroom_sterile.display_name";

        list.add(new TextComponentTranslation("nomi_horizons.gregtech.multiblock_info_cleanroom_pardon"));
        if(api.isCleanroomPardoned()) {
            TextComponentTranslation tct = new TextComponentTranslation(KEY_CLEANROOM_NAME);
            tct.getStyle().setColor(TextFormatting.GREEN);
            list.add(tct);
        }
        if(api.isSterileCleanroomPardoned()) {
            TextComponentTranslation tct = new TextComponentTranslation(KEY_STERILE_CLEANROOM_NAME);
            tct.getStyle().setColor(TextFormatting.GREEN);
            list.add(tct);
        }
    }

}
