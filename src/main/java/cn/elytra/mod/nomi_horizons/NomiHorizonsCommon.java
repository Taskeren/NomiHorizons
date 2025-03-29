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
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import xyz.vsngamer.elevator.init.Registry;

import static cn.elytra.mod.nomi_horizons.NomiHorizons.*;

public class NomiHorizonsCommon {

    public void preInit(FMLPreInitializationEvent event) {
        LOG.info("Nomi Horizons Installed");

        new NomiHorizonsConfigV2(new Configuration(event.getSuggestedConfigurationFile()));

        new NomiHorizonsMetaItem();
    }

    public void onInit(FMLInitializationEvent event) {
        CoverBehaviors.registerBehavior(new ResourceLocation(MOD_ID, "one_stack_filter"), NomiHorizonsMetaItem.ONE_STACK_FILTER,
                (def, tile, side) -> new CoverOneStackFilter(def, tile, side, NomiHorizonsTextures.ONE_STACK_FILTER_OVERLAY));
    }

    public void postInit(FMLPostInitializationEvent event) {
        NomiHorizonsMetaItem.registerRecipes();

        loadCrossModCompat();
    }

    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new NomiHorizonsCommand());
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
