package cn.elytra.mod.nomi_horizons.xmod.gt;

import cn.elytra.mod.nomi_horizons.mixins.gt.QuantumTank_Accessor;
import gregtech.api.gui.widgets.TankWidget;
import gregtech.common.metatileentities.storage.MetaTileEntityQuantumTank;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * A replacement of {@link TankWidget} in {@link MetaTileEntityQuantumTank}, where allows for direct interaction to
 * the fluid tank widget, while keeping the fluid lock functionality.
 * <p>
 * Make sure that both this and {@link QuantumTank_Accessor} are injected for accessing the private fields.
 */
public class QuantumTankWidget extends TankWidget {

    protected final MetaTileEntityQuantumTank quantumTank;

    /**
     * @throws IllegalArgumentException if the given fluidTank doesn't implement {@link IFluidHandler}.
     */
    protected QuantumTankWidget(MetaTileEntityQuantumTank quantumTank, IFluidTank fluidTank, int x, int y, int width, int height) {
        super(fluidTank, x, y, width, height);

        this.quantumTank = quantumTank;

        setAlwaysShowFull(true);
        setDrawHoveringText(false);
        setContainerClicking(true, true);
    }

    public QuantumTankWidget(MetaTileEntityQuantumTank quantumTank, TankWidget widget) {
        this(quantumTank, new QuantumTankHandler(widget.fluidTank, ((QuantumTank_Accessor) quantumTank)::getLockedFluid), widget.getSelfPosition().x, widget.getSelfPosition().y, widget.getSize().width, widget.getSize().height);
    }

    private static class QuantumTankHandler implements IFluidTank, IFluidHandler {

        /**
         * The delegate to the actual {@link IFluidTank}, while it has to also implemented {@link IFluidHandler}.
         */
        protected final IFluidTank delegate;

        /**
         * The getter to the locked fluid; if it returns {@code null}, it means not locked.
         */
        protected final Supplier<@Nullable FluidStack> lockedFluid;

        public QuantumTankHandler(IFluidTank delegate, Supplier<@Nullable FluidStack> lockedFluid) {
            this.delegate = delegate;
            this.lockedFluid = lockedFluid;
            if(!(delegate instanceof IFluidHandler)) {
                throw new IllegalArgumentException("delegate must implement IFluidHandler");
            }
        }

        @Nullable
        @Override
        public FluidStack getFluid() {
            var fluid = delegate.getFluid();
            if(fluid != null) return fluid;

            var locked = lockedFluid.get();
            if(locked != null) return new FluidStack(locked, 0);

            return null;
        }

        @Override
        public int getFluidAmount() {
            return delegate.getFluidAmount();
        }

        @Override
        public int getCapacity() {
            return delegate.getCapacity();
        }

        @Override
        public FluidTankInfo getInfo() {
            return delegate.getInfo();
        }

        @Override
        public int fill(FluidStack resource, boolean doFill) {
            var locked = lockedFluid.get();
            if(locked != null && !locked.isFluidEqual(resource)) return 0;

            return delegate.fill(resource, doFill);
        }

        @Nullable
        @Override
        public FluidStack drain(int maxDrain, boolean doDrain) {
            return delegate.drain(maxDrain, doDrain);
        }

        @Override
        public IFluidTankProperties[] getTankProperties() {
            return ((IFluidHandler) delegate).getTankProperties();
        }

        @Nullable
        @Override
        public FluidStack drain(FluidStack resource, boolean doDrain) {
            return ((IFluidHandler) delegate).drain(resource, doDrain);
        }
    }
}
