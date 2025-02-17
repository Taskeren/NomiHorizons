package cn.elytra.mod.nomi_horizons;

import com.google.common.collect.Lists;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.List;

/**
 * The late Mixin loader for Nomi Horizons.
 */
public class NomiHorizonsMixinLoader implements ILateMixinLoader {

    @Override
    public List<String> getMixinConfigs() {
        return Lists.newArrayList("mixins.nomi_horizons.json");
    }
}
