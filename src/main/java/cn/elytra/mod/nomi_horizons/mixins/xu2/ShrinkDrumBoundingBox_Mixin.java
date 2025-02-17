package cn.elytra.mod.nomi_horizons.mixins.xu2;

import com.rwtema.extrautils2.backend.XUBlockStatic;
import com.rwtema.extrautils2.blocks.BlockDrum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = BlockDrum.class, remap = false)
public abstract class ShrinkDrumBoundingBox_Mixin extends XUBlockStatic {

    @Override
    public @NotNull AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        // slightly shrink the bounding box
        return new AxisAlignedBB(0.075, 0.0, 0.075, 0.925, 1.0, 0.925);
    }

    @Override
    public @NotNull AxisAlignedBB getSelectedBoundingBox(IBlockState state, @NotNull World world, @NotNull BlockPos pos) {
        // revert the overridden code
        return getBoundingBox(state, world, pos).offset(pos);
    }

    @Override
    public @Nullable AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, @NotNull IBlockAccess worldIn, @NotNull BlockPos pos) {
        // revert the overridden code
        return getBoundingBox(blockState, worldIn, pos);
    }
}
