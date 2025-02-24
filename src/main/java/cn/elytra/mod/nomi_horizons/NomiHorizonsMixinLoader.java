package cn.elytra.mod.nomi_horizons;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * The late Mixin loader for Nomi Horizons.
 */
@SuppressWarnings("ReadWriteStringCanBeUsed")
public class NomiHorizonsMixinLoader implements ILateMixinLoader {

    private static class Config {
        @SuppressWarnings("unused")
        @SerializedName("__comment__")
        public String comment = "Hi! This is Nomi Horizons mixin configuration, which controls which hard-injecting features you are interested in. Set to false to disable certain mixins.";

        public boolean setCellCapacityTo144 = true;
        public boolean useNonPhantomFluidTankWidgets = true;
    }

    private static Config config;

    static {
        var gson = new GsonBuilder().setPrettyPrinting().create();
        var path = new File("config/nomi_horizons.mixins.json").toPath();

        if(Files.notExists(path)) {
            config = new Config();
            try {
                Files.write(path, gson.toJson(config).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
            } catch(IOException e) {
                NomiHorizons.LOG.error("Failed to create nomi_horizons.mixins.json", e);
            }
        } else {
            try {
                config = gson.fromJson(new String(Files.readAllBytes(path), StandardCharsets.UTF_8), Config.class);
            } catch(IOException e) {
                NomiHorizons.LOG.error("Failed to read nomi_horizons.mixins.json, using default settings", e);
                config = new Config();
            } catch(JsonSyntaxException e) {
                NomiHorizons.LOG.error("Failed to parse nomi_horizons.mixins.json, using default settings", e);
                config = new Config();
            }
        }
    }

    @Override
    public List<String> getMixinConfigs() {
        var mixinConfigs = Lists.newArrayList("mixins.nomi_horizons.json");

        if(config.setCellCapacityTo144) {
            mixinConfigs.add("mixins.nomi_horizons.144cell.json");
        }

        if(config.useNonPhantomFluidTankWidgets) {
            mixinConfigs.add("mixins.nomi_horizons.non_phantom.json");
        }

        return mixinConfigs;
    }
}
