package cn.elytra.mod.nomi_horizons.xmod.gt;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Matrix4;
import gregtech.api.capability.impl.ItemHandlerDelegate;
import gregtech.api.cover.CoverBase;
import gregtech.api.cover.CoverDefinition;
import gregtech.api.cover.CoverableView;
import gregtech.client.renderer.texture.cube.SimpleOverlayRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CoverOneStackFilter extends CoverBase {

    protected final SimpleOverlayRenderer texture;

    protected ItemHandlerOneStackLimited itemHandler;

    public CoverOneStackFilter(@NotNull CoverDefinition definition, @NotNull CoverableView coverableView, @NotNull EnumFacing attachedSide, SimpleOverlayRenderer texture) {
        super(definition, coverableView, attachedSide);
        this.texture = texture;
    }

    @Override
    public boolean canAttach(@NotNull CoverableView coverable, @NotNull EnumFacing side) {
        return coverable.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getAttachedSide()) != null;
    }

    @Override
    public void renderCover(@NotNull CCRenderState renderState, @NotNull Matrix4 translation, @NotNull IVertexOperation[] pipeline, @NotNull Cuboid6 plateBox, @NotNull BlockRenderLayer layer) {
        texture.renderSided(getAttachedSide(), plateBox, renderState, pipeline, translation);
    }

    @Override
    public <T> @Nullable T getCapability(@NotNull Capability<T> capability, @Nullable T defaultValue) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if(defaultValue == null) {
                return null;
            }
            IItemHandler delegate = (IItemHandler) defaultValue;
            if(itemHandler == null || itemHandler.delegate != delegate) {
                itemHandler = new ItemHandlerOneStackLimited(delegate);
            }
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemHandler);
        }
        return defaultValue;
    }

    protected static class ItemHandlerOneStackLimited extends ItemHandlerDelegate {

        public ItemHandlerOneStackLimited(IItemHandler delegate) {
            super(delegate);
        }

        @NotNull
        @Override
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            for(int i = 0; i < getSlots(); i++) {
                if(areItemStacksEqualIgnoreAmount(stack, getStackInSlot(i)) && i != slot) {
                    return stack;
                }
            }
            return super.insertItem(slot, stack, simulate);
        }
    }

    private static boolean areItemStacksEqualIgnoreAmount(ItemStack s1, ItemStack s2) {
        var s1c = s1.copy();
        var s2c = s2.copy();
        s1c.setCount(1);
        s2c.setCount(1);
        return ItemStack.areItemStacksEqual(s1c, s2c);
    }
}
