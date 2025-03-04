package cn.elytra.mod.nomi_horizons;

import crazypants.enderio.machines.init.MachineObject;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.vsngamer.elevator.init.Registry;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION, dependencies = NomiHorizons.DEPS)
public class NomiHorizons {

    public static final Logger LOG = LogManager.getLogger(Tags.MOD_NAME);

    public static final NomiHorizonsAPI API = new NomiHorizonsAPI();

    @SuppressWarnings("SpellCheckingInspection")
    public static final String DEPS = "required-after:gregtech;"
            + "required-after:extrautils2;"
            + "required-after:actuallyadditions;";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOG.info("Nomi Horizons Installed");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        loadCrossModCompat();
    }

    private void loadCrossModCompat() {
        loadModCompat("elevatorid", () -> {
            var blockStates = Registry.ELEVATOR_ITEMBLOCKS.values().stream()
                    .map(ItemBlock::getBlock)
                    .map(Block::getDefaultState)
                    .toArray(IBlockState[]::new);
            API.addElevator(blockStates);
        });
        loadModCompat("enderio", () -> {
            API.addElevator(MachineObject.block_travel_anchor.getBlockNN().getDefaultState());
        });
    }

    private static void loadModCompat(String modId, Runnable r) {
        if(Loader.isModLoaded(modId)) {
            try {
                LOG.info("Loading compat with {}", modId);
                r.run();
            } catch(Exception e) {
                LOG.error("Failed to compat with {}", modId, e);
            }
        }
    }

}
