package cn.elytra.mod.nomi_horizons.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModText {

    private static final int DYNAMIC_MILLISECONDS = 500;

    public static final String NOMI_HORIZONS = I18n.format("nomi_horizons.nomi_horizons");

    private static final String[] DYNAMIC_NOMI_HORIZONS = new String[]{
            TextFormatting.RED + NOMI_HORIZONS,
            TextFormatting.YELLOW + NOMI_HORIZONS,
            TextFormatting.GOLD + NOMI_HORIZONS,
    };

    public static String getDynamicNomiHorizons() {
        var i = (int) (Minecraft.getSystemTime() / DYNAMIC_MILLISECONDS);
        return DYNAMIC_NOMI_HORIZONS[i % DYNAMIC_NOMI_HORIZONS.length];
    }

    public static String getDynamicPresentedByNomiHorizons() {
        return I18n.format("nomi_horizons.text.presented_by", getDynamicNomiHorizons());
    }

}
