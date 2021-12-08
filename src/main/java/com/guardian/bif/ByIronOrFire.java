package com.guardian.bif;

import com.guardian.bif.util.registries.server.FreezingRegistry;
import com.guardian.bif.util.registries.server.ItemRegistry;
import com.guardian.bif.util.registries.server.VisibilityRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;


public class ByIronOrFire implements ModInitializer, ClientModInitializer {

    public static final String MODID = "bif";
    public static final Map<ArmorMaterial, int[]> ARMORVISIBILITY = new HashMap<>();
    public static final Map<ArmorMaterial, int[]> ARMORFREEZINGABILITY = new HashMap<>();
    public static final Logger LOG = LogManager.getLogger(ByIronOrFire.MODID);

    public static Identifier id(String name) {
        return new Identifier(MODID, name);
    }

    @Override
    public void onInitialize() {

        LOG.info("By Iron or Fire Initializing");

        //Registering of Items
        ItemRegistry.init();
        /*Generating & Registering Armor Freezing Values. Will loop through all armor items, get there materials,
         and generates Freezing values for all non-existent sets. Will skip over Freezing Values added by other mods*/
        ServerLifecycleEvents.SERVER_STARTING.register((server) -> FreezingRegistry.init());
        /*Generating & Registering Armor Visibility Values. Will loop through all armor items, get there materials,
         and generates visibility values for all non-existent sets. Will skip over Visibility Values added by other mods*/
        ServerLifecycleEvents.SERVER_STARTING.register((server) -> VisibilityRegistry.init());

        LOG.info("By Iron or Fire Initializing Complete, Success!");
    }

    @Override
    public void onInitializeClient() {

        //Registers all Dyeable Item's
        ColorProviderRegistry.ITEM.register((Stack, tintIndex) -> tintIndex > 0 ? -1 : ((DyeableItem) Stack.getItem()).getColor(Stack), ItemRegistry.CHAINED_LEATHER_HELMET, ItemRegistry.CHAINED_LEATHER_CHESTPLATE, ItemRegistry.CHAINED_LEATHER_LEGGINGS, ItemRegistry.CHAINED_LEATHER_BOOTS);

    }
}
