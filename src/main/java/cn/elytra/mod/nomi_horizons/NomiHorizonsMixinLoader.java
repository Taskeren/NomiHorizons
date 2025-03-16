package cn.elytra.mod.nomi_horizons;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import net.minecraftforge.fml.common.versioning.ComparableVersion;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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

        @SerializedName("__version__")
        public String version;

        public boolean setCellCapacityTo144 = true;
        public boolean useNonPhantomFluidTankWidgets = true;
        public boolean useCleanroomPardon = true;
        public boolean useBetterRecipe = true;
        public boolean useOneStackOnly = true;
    }

    private static Config config;

    static {
        var gson = new GsonBuilder().setPrettyPrinting().create();
        var path = new File("config/nomi_horizons.mixins.json").toPath();

        if(Files.notExists(path)) {
            config = newConfig();
            try {
                Files.write(path, gson.toJson(config).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
            } catch(IOException e) {
                NomiHorizons.LOG.error("Failed to create nomi_horizons.mixins.json", e);
            }
        } else {
            try {
                config = gson.fromJson(new String(Files.readAllBytes(path), StandardCharsets.UTF_8), Config.class);

                // check config version
                var modVersion = new ComparableVersion(NomiHorizons.VERSION);
                var configVersion = config.version != null ? new ComparableVersion(config.version) : null;
                if(configVersion == null || modVersion.compareTo(configVersion) > 0) {
                    NomiHorizons.LOG.warn("nomi_horizons.mixins.json is out-dated!");
                    try {
                        // update the config version
                        config.version = NomiHorizons.VERSION;
                        // write it to the file, and the default values are also included in.
                        Files.write(path, gson.toJson(config).getBytes(StandardCharsets.UTF_8), StandardOpenOption.WRITE);
                        NomiHorizons.LOG.info("Updated new configuration options in nomi_horizons.mixins.json, have a look at it!");
                    } catch(IOException e) {
                        NomiHorizons.LOG.error("Failed to update nomi_horizons.mixins.json", e);
                    }
                }
            } catch(IOException e) {
                NomiHorizons.LOG.error("Failed to read nomi_horizons.mixins.json, using default settings", e);
                config = newConfig();
            } catch(JsonSyntaxException e) {
                NomiHorizons.LOG.error("Failed to parse nomi_horizons.mixins.json, using default settings", e);
                config = newConfig();
                try {
                    Files.move(path, path.getParent().resolve("nomi_horizons.mixins.json.bak"), StandardCopyOption.REPLACE_EXISTING);
                    Files.write(path, gson.toJson(config).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
                    NomiHorizons.LOG.info("A brand new nomi_horizons.mixins.json is generated, have a look at it!");
                } catch(IOException ex) {
                    NomiHorizons.LOG.error("Failed to fix nomi_horizons.mixins.json, using default settings", ex);
                }
            }
        }
    }

    private static Config newConfig() {
        var config = new Config();
        config.version = NomiHorizons.VERSION;
        return config;
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

        if(config.useCleanroomPardon) {
            mixinConfigs.add("mixins.nomi_horizons.cleanroom_pardon.json");
        }

        if(config.useBetterRecipe) {
            mixinConfigs.add("mixins.nomi_horizons.better_recipe.json");
        }

        if(config.useOneStackOnly) {
            mixinConfigs.add("mixins.nomi_horizons.one_stack_only.json");
        }

        return mixinConfigs;
    }
}
