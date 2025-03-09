package cn.elytra.mod.nomi_horizons.xmod.gt;

import appeng.api.util.AEColor;
import appeng.tile.networking.TileCableBus;
import cn.elytra.mod.nomi_horizons.client.ModText;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.ClickButtonWidget;
import gregtech.api.items.gui.ItemUIFactory;
import gregtech.api.items.gui.PlayerInventoryHolder;
import gregtech.api.items.metaitem.stats.IItemBehaviour;
import gregtech.api.items.metaitem.stats.IItemNameProvider;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.pipenet.tile.IPipeTile;
import gregtech.api.util.LocalizationUtils;
import gregtech.api.util.Mods;
import gregtech.common.items.behaviors.ColorSprayBehaviour;
import gregtech.core.sound.GTSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * This behaviour is copied and modified from {@link ColorSprayBehaviour}.
 * It is unbreakable, or of unlimited count of usages.
 * You can open an GUI by right-clicking at the air, and select a wanted color.
 * <p>
 * The selected color data is stored in the ItemStack, as an NBT byte keyed by "Color".
 * NBT unset or {@code null} are considered as "solvent".
 *
 * @see ColorSprayBehaviour
 */
public class CreativeColorSprayBehaviour implements IItemBehaviour, ItemUIFactory, IItemNameProvider {

    public static final String KEY_COLOR = "Color";
    public static final @Nullable EnumDyeColor[] COLORS;

    static {
        COLORS = new EnumDyeColor[EnumDyeColor.values().length + 1];
        COLORS[0] = null;
        System.arraycopy(EnumDyeColor.values(), 0, COLORS, 1, EnumDyeColor.values().length);
    }

    public CreativeColorSprayBehaviour() {
    }

    @Nullable
    public static EnumDyeColor getColor(ItemStack stack) {
        var tags = stack.getTagCompound();
        if(tags != null && tags.hasKey(KEY_COLOR)) {
            var ordinal = tags.getByte(KEY_COLOR);
            return EnumDyeColor.byMetadata(ordinal);
        }
        return null;
    }

    public static void setColor(ItemStack stack, @Nullable EnumDyeColor color) {
        var tags = stack.getTagCompound();
        if(tags == null) tags = new NBTTagCompound();
        if(color == null) {
            tags.removeTag(KEY_COLOR);
        } else {
            tags.setByte(KEY_COLOR, (byte) color.getMetadata());
        }
        stack.setTagCompound(tags);
    }

    @Override
    public ActionResult<ItemStack> onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        var stack = player.getHeldItem(hand);
        if(!player.canPlayerEdit(pos, facing, stack)) {
            return ActionResult.newResult(EnumActionResult.FAIL, player.getHeldItem(hand));
        }
        if(!tryPaintBlock(stack, player, world, pos, facing)) {
            return ActionResult.newResult(EnumActionResult.PASS, player.getHeldItem(hand));
        }
        world.playSound(null, player.posX, player.posY, player.posZ, GTSoundEvents.SPRAY_CAN_TOOL, SoundCategory.PLAYERS, 1.0F, 1.0F);
        return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    private boolean tryPaintBlock(ItemStack spray, EntityPlayer player, World world, BlockPos pos, EnumFacing side) {
        var color = getColor(spray);
        var blockState = world.getBlockState(pos);
        var block = blockState.getBlock();
        if(color == null) {
            return tryStripBlockColor(player, world, pos, block, side);
        } else {
            return block.recolorBlock(world, pos, side, color) || tryPaintSpecialBlock(spray, player, world, pos, block);
        }
    }

    @SuppressWarnings("DataFlowIssue")
    private boolean tryPaintSpecialBlock(ItemStack spray, EntityPlayer player, World world, BlockPos pos, Block block) {
        var color = getColor(spray);
        if(block == Blocks.GLASS) {
            IBlockState newBlockState = Blocks.STAINED_GLASS.getDefaultState()
                    .withProperty(BlockStainedGlass.COLOR, color);
            world.setBlockState(pos, newBlockState);
            return true;
        }
        if(block == Blocks.GLASS_PANE) {
            IBlockState newBlockState = Blocks.STAINED_GLASS_PANE.getDefaultState()
                    .withProperty(BlockStainedGlassPane.COLOR, color);
            world.setBlockState(pos, newBlockState);
            return true;
        }
        if(block == Blocks.HARDENED_CLAY) {
            IBlockState newBlockState = Blocks.STAINED_HARDENED_CLAY.getDefaultState()
                    .withProperty(BlockColored.COLOR, color);
            world.setBlockState(pos, newBlockState);
            return true;
        }
        if(Mods.AppliedEnergistics2.isModLoaded()) {
            TileEntity te = world.getTileEntity(pos);
            if(te instanceof TileCableBus cable) {
                // do not try to recolor if it already is this color
                if(cable.getColor().ordinal() != color.ordinal()) {
                    cable.recolourBlock(null, AEColor.values()[color.ordinal()], player);
                    return true;
                }
            }
        }
        return false;
    }

    @SuppressWarnings("DataFlowIssue")
    private boolean tryStripBlockColor(EntityPlayer player, World world, BlockPos pos, Block block, EnumFacing side) {
        // MC special cases
        if(block == Blocks.STAINED_GLASS) {
            world.setBlockState(pos, Blocks.GLASS.getDefaultState());
            return true;
        }
        if(block == Blocks.STAINED_GLASS_PANE) {
            world.setBlockState(pos, Blocks.GLASS_PANE.getDefaultState());
            return true;
        }
        if(block == Blocks.STAINED_HARDENED_CLAY) {
            world.setBlockState(pos, Blocks.HARDENED_CLAY.getDefaultState());
            return true;
        }

        // MTE special case
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof IGregTechTileEntity) {
            MetaTileEntity mte = ((IGregTechTileEntity) te).getMetaTileEntity();
            if(mte != null) {
                if(mte.isPainted()) {
                    mte.setPaintingColor(-1);
                    return true;
                } else return false;
            }
        }

        // TileEntityPipeBase special case
        if(te instanceof IPipeTile<?, ?> pipe) {
            if(pipe.isPainted()) {
                pipe.setPaintingColor(-1);
                return true;
            } else return false;
        }

        // AE2 cable special case
        if(Mods.AppliedEnergistics2.isModLoaded()) {
            if(te instanceof TileCableBus cable) {
                // do not try to strip color if it is already colorless
                if(cable.getColor() != AEColor.TRANSPARENT) {
                    cable.recolourBlock(null, AEColor.TRANSPARENT, player);
                    return true;
                } else return false;
            }
        }

        // General case
        IBlockState state = world.getBlockState(pos);
        for(IProperty<?> prop : state.getProperties().keySet()) {
            if(prop.getName().equals("color") && prop.getValueClass() == EnumDyeColor.class) {
                IBlockState defaultState = block.getDefaultState();
                EnumDyeColor defaultColor = EnumDyeColor.WHITE;
                try {
                    // try to read the default color value from the default state instead of just
                    // blindly setting it to default state, and potentially resetting other values
                    defaultColor = (EnumDyeColor) defaultState.getValue(prop);
                } catch(IllegalArgumentException ignored) {
                    // no default color, we may have to fallback to WHITE here
                    // other mods that have custom behavior can be done as
                    // special cases above on a case-by-case basis
                }
                block.recolorBlock(world, pos, side, defaultColor);
                return true;
            }
        }

        return false;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if(!world.isRemote && (hand == EnumHand.MAIN_HAND || (hand == EnumHand.OFF_HAND && player.getHeldItemMainhand().isEmpty()))) {
            var holder = new PlayerInventoryHolder(player, hand);
            holder.openUI();
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    @Override
    public void addInformation(ItemStack stack, List<String> lines) {
        var color = getColor(stack);
        if(color != null) {
            lines.add(I18n.format("behaviour.paintspray." + color.getTranslationKey() + ".tooltip"));
        } else {
            lines.add(I18n.format("behaviour.paintspray.solvent.tooltip"));
        }
        lines.add(I18n.format("nomi_horizons.spray.creative.tooltip"));
        lines.add(ModText.getDynamicPresentedByNomiHorizons());
    }


    @Override
    public ModularUI createUI(PlayerInventoryHolder holder, EntityPlayer player) {
        var builder = ModularUI.builder(GuiTextures.BACKGROUND, 280, 150)
                .label(10, 8, "nomi_horizons.spray.creative.gui");
        createGrid:
        for(int col = 0; col < COLORS.length; col++) {
            for(int row = 0; row < 4; row++) {
                var index = col * 4 + row;

                // check array boundary
                if(index >= COLORS.length) break createGrid;

                // create the button widget
                var color = COLORS[index];
                var displayText = color != null
                        ? "nomi_horizons.color." + color.getTranslationKey()
                        : "nomi_horizons.color.solvent";
                var textColor = color != null
                        ? color.colorValue
                        : EnumDyeColor.WHITE.colorValue;
                builder.widget(new ClickButtonWidget(row * 65 + 10, col * 20 + 25, 60, 20,
                        displayText, data -> {
                    // update the color info of the spray
                    setColor(holder.getCurrentItem(), color);
                    // play the spray sound and close the screen
                    player.world.playSound(null, player.posX, player.posY, player.posZ, GTSoundEvents.SPRAY_CAN_TOOL, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    player.closeScreen();
                }).setTextColor(textColor));
            }
        }

        return builder.build(holder, player);
    }


    @Override
    public String getItemStackDisplayName(ItemStack itemStack, String unlocalizedName) {
        var color = getColor(itemStack);
        var gtSprayUnlocalizedKey = color != null
                ? "metaitem.spray.can.dyes." + color.getName() + ".name"
                : "metaitem.spray.solvent.name";
        return LocalizationUtils.format("nomi_horizons.spray.creative.name_template", LocalizationUtils.format(gtSprayUnlocalizedKey));
    }
}
