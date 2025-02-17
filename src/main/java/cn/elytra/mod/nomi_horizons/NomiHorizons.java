package cn.elytra.mod.nomi_horizons;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION)
public class NomiHorizons {

    public static final Logger LOG = LogManager.getLogger(Tags.MOD_NAME);

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOG.info("Nomi Horizons Installed");
    }

}
