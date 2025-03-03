package cn.elytra.mod.nomi_horizons;

import cn.elytra.mod.nomi_horizons.utils.PredicateUtils;
import net.minecraft.block.state.IBlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class NomiHorizonsAPI {

    @NotNull
    protected Predicate<IBlockState> elevatorPredicate = PredicateUtils.alwaysFalse();

    public void addElevator(@NotNull Predicate<IBlockState> predicate) {
        elevatorPredicate = elevatorPredicate.or(predicate);
    }

    @NotNull
    public Predicate<IBlockState> getElevatorPredicate() {
        return elevatorPredicate;
    }

}
