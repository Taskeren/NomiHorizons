package cn.elytra.mod.nomi_horizons.xmod.xu2;

import cn.elytra.mod.nomi_horizons.client.ModText;
import com.rwtema.extrautils2.backend.XUItemBlock;
import com.rwtema.extrautils2.blocks.BlockDrum;
import gregtech.api.capability.impl.GTFluidHandlerItemStack;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * The modified version of {@link XUItemBlock}, used for Drum blocks, adding
 * Fluid container support for extracting/draining fluids from/into GregTech machines.
 */
public class DrumItemBlock extends XUItemBlock {

    protected final BlockDrum drum;

    public DrumItemBlock(Block block) {
        super(block);

        // cache the reference to the drum
        if(block instanceof BlockDrum blockDrum) {
            this.drum = blockDrum;
        } else {
            throw new IllegalArgumentException("Block must be an instance of BlockDrum");
        }
    }

    @Override
    public void addItemColors(ItemColors itemColors, BlockColors blockColors) {
        super.addItemColors(itemColors, blockColors);
    }

    public int getCapacity(ItemStack stack) {
        return drum.xuBlockState
                .getStateFromDropMeta(stack.getMetadata())
                .getValue(BlockDrum.PROPERTY_CAPACITY)
                .capacity * 1000;
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(@NotNull ItemStack stack, @Nullable NBTTagCompound nbt) {
        var capacity = getCapacity(stack);
        return new GTFluidHandlerItemStack(stack, capacity);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null) != null;
    }

    @Override
    public @NotNull ItemStack getContainerItem(@NotNull ItemStack stack) {
        var handler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
        if(handler == null) return ItemStack.EMPTY;

        var drained = handler.drain(1000, true);
        if(drained == null || drained.amount != 1000) {
            return ItemStack.EMPTY;
        } else {
            return handler.getContainer().copy();
        }
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, @NotNull EntityPlayer playerIn, @NotNull List<String> tooltip, boolean advanced) {
        // logic copied from MetaTileEntityQuantumTank#addInformation
        int capacity = getCapacity(stack);
        tooltip.add(I18n.format("gregtech.universal.tooltip.fluid_storage_capacity", capacity));

        NBTTagCompound tagCompound = stack.getTagCompound();
        if(tagCompound != null && tagCompound.hasKey("Fluid", Constants.NBT.TAG_COMPOUND)) {
            FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(tagCompound.getCompoundTag("Fluid"));
            if(fluidStack != null) {
                tooltip.add(I18n.format("gregtech.universal.tooltip.fluid_stored",
                        fluidStack.getFluid().getLocalizedName(fluidStack), fluidStack.amount));
            }
        }
        // end

        tooltip.add(ModText.getDynamicPresentedByNomiHorizons());
    }
}
