package cn.elytra.mod.nomi_horizons.mixins.gt;

import gregtech.api.items.itemhandlers.GTItemStackHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = GTItemStackHandler.class, remap = false)
public abstract class OneStackOnly_Mixin extends ItemStackHandler {

    @Override
    @NotNull
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        for(int i = 0; i < getSlots(); i++) {
            var existing = this.stacks.get(i);
            if(ItemStack.areItemStacksEqual(existing, stack)) {
                if(i == slot) {
                    return super.insertItem(slot, stack, simulate);
                } else {
                    return stack;
                }
            }
        }
        return super.insertItem(slot, stack, simulate);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        for(int i = 0; i < getSlots(); i++) {
            var existing = this.stacks.get(i);
            if(super.isItemValid(slot, stack) && ItemStack.areItemStacksEqual(existing, stack)) {
                return i == slot; // only allow the slot where there is an existing one
            }
        }
        return super.isItemValid(slot, stack);
    }
}
