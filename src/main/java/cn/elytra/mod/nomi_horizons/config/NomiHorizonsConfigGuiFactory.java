package cn.elytra.mod.nomi_horizons.config;

import cn.elytra.mod.nomi_horizons.NomiHorizons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NomiHorizonsConfigGuiFactory implements IModGuiFactory {

    @Override
    public void initialize(Minecraft minecraftInstance) {
    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    public List<IConfigElement> getConfigElements() {
        var config = NomiHorizonsConfigV2.get();
        return config != null ? config.getAllProperties().stream().map(ConfigElement::new).collect(Collectors.toList()) : Collections.emptyList();
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new GuiConfig(parentScreen, getConfigElements(), NomiHorizons.MOD_ID, false, false, I18n.format("nomi_horizons.nomi_horizons"));
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return Collections.emptySet();
    }
}
