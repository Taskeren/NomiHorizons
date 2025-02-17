package cn.elytra.mod.nomi_horizons.mixins.aa;

import cn.elytra.mod.nomi_horizons.utils.Constants;
import de.ellpeck.actuallyadditions.mod.blocks.BlockGiantChest;
import de.ellpeck.actuallyadditions.mod.blocks.base.BlockContainerBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = BlockGiantChest.class, remap = false)
public abstract class ShrinkCrateBoundingBox_Mixin extends BlockContainerBase {

    public ShrinkCrateBoundingBox_Mixin(Material material, String name) {
        super(material, name);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull AxisAlignedBB getBoundingBox(@NotNull IBlockState state, @NotNull IBlockAccess source, @NotNull BlockPos pos) {
        // slightly shrink the bounding box
        return Constants.SLIGHTLY_SHRUNK_BLOCK;
    }

}
