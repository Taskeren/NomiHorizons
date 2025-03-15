package cn.elytra.mod.nomi_horizons.config;

import cn.elytra.mod.nomi_horizons.NomiHorizons;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class NomiHorizonsConfigV2 {

    // region static

    private static NomiHorizonsConfigV2 instance;

    public static NomiHorizonsConfigV2 get() {
        return instance;
    }

    public static void save() {
        get().config.save();
    }

    public static void load() {
        get().config.load();
        get().init(); // re-create the properties to update the changed values
    }

    // endregion static

    private final Configuration config;

    public Property propPardonCleanroom;
    public Property propPardonSterileCleanroom;
    public Property propUseAdvancedRecipeLogic;

    public NomiHorizonsConfigV2(Configuration config) {
        instance = this;
        this.config = config;
        init();
        save();
    }

    private void init() {
        propPardonCleanroom = this.config.get(Configuration.CATEGORY_GENERAL, "pardonCleanroom", true);
        propPardonCleanroom.setLanguageKey("nomi_horizons.config.pardon_cleanroom");
        propPardonCleanroom.setComment("True to bypass the Cleanroom check for multiblocks.");

        propPardonSterileCleanroom = this.config.get(Configuration.CATEGORY_GENERAL, "pardonSterileCleanroom", true);
        propPardonSterileCleanroom.setLanguageKey("nomi_horizons.config.pardon_sterile_cleanroom");
        propPardonSterileCleanroom.setComment("True to bypass the Sterile Cleanroom check for multiblocks.");

        propUseAdvancedRecipeLogic = this.config.get(Configuration.CATEGORY_GENERAL, "useAdvancedRecipeLogic", true);
        propUseAdvancedRecipeLogic.setLanguageKey("nomi_horizons.config.use_advanced_recipe_logic");
        propUseAdvancedRecipeLogic.setComment("[EXPERIMENTAL] True to enable New Horizons' Advanced Recipe Logic.");
    }

    public List<Property> getAllProperties() {
        var list = new ArrayList<Property>();

        try {
            for(Field declaredField : getClass().getDeclaredFields()) {
                // find static Property fields
                if(declaredField.getType() == Property.class && !Modifier.isStatic(declaredField.getModifiers())) {
                    var prop = declaredField.get(this);
                    if(prop instanceof Property) { // the check, but not necessary maybe
                        list.add((Property) prop);
                    }
                }
            }
        } catch(Exception e) {
            NomiHorizons.LOG.warn("Ignored exception", e);
        }

        return list;
    }

}
