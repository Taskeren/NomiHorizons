package cn.elytra.mod.nomi_horizons.config;

import cn.elytra.mod.nomi_horizons.Tags;
import net.minecraftforge.common.config.Config;

@Config(modid = Tags.MOD_ID)
public class NomiHorizonsConfig {

    @Config.LangKey("nomi_horizons.config.pardon_cleanroom")
    @Config.Comment("True to bypass the Cleanroom check for multiblocks.")
    public static boolean pardonCleanroom = true;

    @Config.LangKey("nomi_horizons.config.pardon_sterile_cleanroom")
    @Config.Comment("True to bypass the Sterile Cleanroom check for multiblocks.")
    public static boolean pardonSterileCleanroom = false;

    @Config.LangKey("nomi_horizons.config.use_advanced_recipe_logic")
    @Config.Comment("[EXPERIMENTAL] True to enable New Horizons' Advanced Recipe Logic.")
    public static boolean useAdvancedRecipeLogic = false;

}
