package cn.elytra.mod.nomi_horizons.xmod.gt;

import cn.elytra.mod.nomi_horizons.NomiHorizons;
import gregtech.api.GregTechAPI;
import gregtech.api.items.metaitem.MetaItem;
import gregtech.api.items.metaitem.StandardMetaItem;
import net.minecraft.util.ResourceLocation;

public class NomiHorizonsMetaItem extends StandardMetaItem {

    private static NomiHorizonsMetaItem instance;

    public static MetaItem<?>.MetaValueItem CREATIVE_SPRAY;

    public NomiHorizonsMetaItem() {
        instance = this;
        this.setRegistryName("meta_item");
    }

    public static NomiHorizonsMetaItem getInstance() {
        return instance;
    }

    @Override
    public void registerSubItems() {
        CREATIVE_SPRAY = addItem(0, "spray.creative")
                .setMaxStackSize(1)
                .addComponents(new CreativeColorSprayBehaviour())
                .setCreativeTabs(GregTechAPI.TAB_GREGTECH_TOOLS);
    }

    @Override
    public ResourceLocation createItemModelPath(MetaItem<?>.MetaValueItem metaValueItem, String postfix) {
        return new ResourceLocation(NomiHorizons.MOD_ID, formatModelPath(metaValueItem) + postfix);
    }
}
