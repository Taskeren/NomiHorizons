package cn.elytra.mod.nomi_horizons;

import cn.elytra.mod.nomi_horizons.xmod.gt.NomiHorizonsTextures;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class NomiHorizonsClient extends NomiHorizonsCommon {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        NomiHorizonsTextures.init();
    }
}
