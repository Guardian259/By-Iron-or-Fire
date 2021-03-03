package com.guardian.bif;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.item.DyeableItem;

public class ByIronOrFireClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ColorProviderRegistry.ITEM.register((Stack, tintIndex) -> tintIndex > 0 ? -1 : ((DyeableItem) Stack.getItem()).getColor(Stack), ByIronOrFire.Items.CHAINED_LEATHER_HELMET, ByIronOrFire.Items.CHAINED_LEATHER_CHESTPLATE, ByIronOrFire.Items.CHAINED_LEATHER_LEGGINGS, ByIronOrFire.Items.CHAINED_LEATHER_BOOTS);
    }
    
}
