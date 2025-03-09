package cn.elytra.mod.nomi_horizons.mixins.gt;

import cn.elytra.mod.nomi_horizons.xmod.gt.NomiHorizonsMetaItem;
import gregtech.api.block.machines.BlockMachine;
import gregtech.api.pipenet.block.BlockPipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {BlockMachine.class, BlockPipe.class}, remap = false)
public class CreativeSprayOffhand_Mixin {

    @Inject(method = "onBlockPlacedBy", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;getHeldItemOffhand()Lnet/minecraft/item/ItemStack;", shift = At.Shift.AFTER))
    private void nh$hookCreativeSpray(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack, CallbackInfo ci) {
        var offhand = placer.getHeldItemOffhand();
        if(offhand.isItemEqual(NomiHorizonsMetaItem.CREATIVE_SPRAY.getStackForm())) {
            NomiHorizonsMetaItem.CREATIVE_SPRAY.getBehaviours().get(0).onItemUse((EntityPlayer) placer, worldIn, pos, EnumHand.OFF_HAND, EnumFacing.UP, 0, 0, 0);
        }
    }

}
