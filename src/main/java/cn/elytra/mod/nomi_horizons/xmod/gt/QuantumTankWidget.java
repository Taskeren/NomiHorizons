package cn.elytra.mod.nomi_horizons.xmod.gt;

import cn.elytra.mod.nomi_horizons.mixins.gt.FluidHatch_Accessor;
import cn.elytra.mod.nomi_horizons.mixins.gt.QuantumTank_Accessor;
import gregtech.api.gui.Widget;
import gregtech.api.gui.widgets.TankWidget;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityFluidHatch;
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

    /**
     * @throws IllegalArgumentException if the given fluidTank doesn't implement {@link IFluidHandler}.
     */
    protected QuantumTankWidget(IFluidTank fluidTank, int x, int y, int width, int height) {
        super(fluidTank, x, y, width, height);

        setAlwaysShowFull(true);
        setDrawHoveringText(false);
        setContainerClicking(true, true);
    }

    protected QuantumTankWidget(IFluidTank fluidTank, Widget inherit) {
        this(fluidTank, inherit.getSelfPosition().x, inherit.getSelfPosition().y, inherit.getSize().width, inherit.getSize().height);
    }

    public static QuantumTankWidget create(MetaTileEntityQuantumTank quantumTank, TankWidget widget) {
        var tank = (QuantumTank_Accessor) quantumTank;
        return new QuantumTankWidget(new QuantumTankHandler(widget.fluidTank, tank::getLockedFluid), widget);
    }

    public static QuantumTankWidget create(MetaTileEntityFluidHatch fluidHatch, TankWidget widget) {
        var hatch = (FluidHatch_Accessor) fluidHatch;
        return new QuantumTankWidget(new QuantumTankHandler(widget.fluidTank, hatch.isExportHatch() ? hatch::getLockedFluid : null), widget);
    }

    private static class QuantumTankHandler implements IFluidTank, IFluidHandler {

        /**
         * The delegate to the actual {@link IFluidTank}, while it has to also implemented {@link IFluidHandler}.
         */
        protected final IFluidTank delegate;

        /**
         * The getter to the locked fluid; if it returns {@code null}, it means not locked.
         */
        @Nullable
        protected final Supplier<@Nullable FluidStack> lockedFluid;

        @Nullable
        protected FluidStack getLockedFluid() {
            if(lockedFluid != null) {
                return lockedFluid.get();
            } else {
                return null;
            }
        }

        public QuantumTankHandler(IFluidTank delegate) {
            this(delegate, null);
        }

        public QuantumTankHandler(IFluidTank delegate, @Nullable Supplier<@Nullable FluidStack> lockedFluid) {
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

            var locked = getLockedFluid();
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
            var locked = getLockedFluid();
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
