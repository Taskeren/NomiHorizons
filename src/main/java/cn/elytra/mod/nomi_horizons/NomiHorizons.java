package cn.elytra.mod.nomi_horizons;

import cn.elytra.mod.nomi_horizons.command.NomiHorizonsCommand;
import cn.elytra.mod.nomi_horizons.config.NomiHorizonsConfigV2;
import cn.elytra.mod.nomi_horizons.xmod.gt.CoverOneStackFilter;
import cn.elytra.mod.nomi_horizons.xmod.gt.NomiHorizonsMetaItem;
import cn.elytra.mod.nomi_horizons.xmod.gt.NomiHorizonsTextures;
import crazypants.enderio.machines.init.MachineObject;
import gregtech.common.covers.CoverBehaviors;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.vsngamer.elevator.init.Registry;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION, dependencies = NomiHorizons.DEPS, guiFactory = "cn.elytra.mod.nomi_horizons.config.NomiHorizonsConfigGuiFactory")
@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class NomiHorizons {

    public static final Logger LOG = LogManager.getLogger(Tags.MOD_NAME);

    public static final String MOD_ID = Tags.MOD_ID;
    public static final String VERSION = Tags.VERSION;

    public static final NomiHorizonsAPI API = new NomiHorizonsAPI();

    @SuppressWarnings("SpellCheckingInspection")
    public static final String DEPS = "required-after:gregtech;"
            + "required-after:extrautils2;"
            + "required-after:actuallyadditions;";

    @SidedProxy(
            serverSide = "cn.elytra.mod.nomi_horizons.NomiHorizonsCommon",
            clientSide = "cn.elytra.mod.nomi_horizons.NomiHorizonsClient"
    )
    public static NomiHorizonsCommon proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void onInit(FMLInitializationEvent event) {
        proxy.onInit(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }

    @SubscribeEvent
    public static void onConfigSync(ConfigChangedEvent.OnConfigChangedEvent event) {
        if(event.getModID().equals(Tags.MOD_ID)) {
            try {
                NomiHorizonsConfigV2.save();
            } catch(Exception e) {
                NomiHorizons.LOG.error("Failed to save configurations", e);
            }
        }
    }

}
