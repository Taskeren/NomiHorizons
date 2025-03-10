package cn.elytra.mod.nomi_horizons;

import cn.elytra.mod.nomi_horizons.config.NomiHorizonsConfig;
import codechicken.lib.util.ArrayUtils;
import com.google.common.collect.Lists;
import gregtech.api.metatileentity.multiblock.CleanroomType;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.api.pattern.TraceabilityPredicate;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NomiHorizonsAPI {

    /**
     * A {@link TraceabilityPredicate} where always return false, and don't provide any candidates.
     * <p>
     * It should be used as a placeholder.
     */
    protected static final TraceabilityPredicate ALWAYS_FALSE = new TraceabilityPredicate((state) -> false);

    /**
     * The list storing all the {@link TraceabilityPredicate} of an elevator, from whatever what the mod is.
     * They are being combined by {@link TraceabilityPredicate#or(TraceabilityPredicate)} in {@link #getElevatorPredicate()}.
     */
    @NotNull
    protected final List<TraceabilityPredicate> elevators = Lists.newArrayList();

    protected int maxElevatorCount = 1;

    /**
     * Register {@link IBlockState BlockStates} as elevator blocks, to make them available in Cleanroom structures.
     *
     * @param state the given blocks
     */
    public void addElevator(@NotNull IBlockState... state) {
        var predicate = MultiblockControllerBase.states(state);
        addElevatorPredicate(predicate);
    }

    /**
     * Register a {@link TraceabilityPredicate} to be used in elevator block testing in Cleanroom structures.
     *
     * @param predicate the given predicate
     */
    public void addElevatorPredicate(@NotNull TraceabilityPredicate predicate) {
        elevators.add(predicate);
    }

    @NotNull
    @ApiStatus.Internal
    public TraceabilityPredicate getElevatorPredicate() {
        if(elevators.isEmpty()) return ALWAYS_FALSE;
        return elevators.stream().reduce(TraceabilityPredicate::or).get();
    }

    public void setMaxElevatorCount(int maxElevatorCount) {
        this.maxElevatorCount = maxElevatorCount;
    }

    @ApiStatus.Internal
    public int getMaxElevatorCount() {
        return maxElevatorCount;
    }

    @ApiStatus.Internal
    public boolean isCleanroomPardoned() {
        return NomiHorizonsConfig.pardonCleanroom;
    }

    @ApiStatus.Internal
    public boolean isSterileCleanroomPardoned() {
        return NomiHorizonsConfig.pardonSterileCleanroom;
    }

    @ApiStatus.Internal
    public boolean useAdvancedRecipeLogic() {
        return NomiHorizonsConfig.useAdvancedRecipeLogic;
    }

}
