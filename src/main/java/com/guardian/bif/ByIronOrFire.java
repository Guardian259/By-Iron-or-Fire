package com.guardian.bif;

import com.guardian.bif.client.SmallBombEntityRenderer;
import com.guardian.bif.recipe.ElytraAttachmentRecipe;
import com.guardian.bif.recipe.MortarBombRecipe;
import com.guardian.bif.util.registries.client.ClientCallbackRegistry;
import com.guardian.bif.util.registries.client.ClientPacketsRegistry;
import com.guardian.bif.util.registries.server.EntityRegistry;
import com.guardian.bif.util.registries.server.ItemRegistry;
import com.guardian.bif.util.registries.server.VisibilityRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;


public class ByIronOrFire implements ModInitializer, ClientModInitializer {

    public static final String MODID = "bif";
    public static final Map<ArmorMaterial, int[]> ARMORVISIBILITY = new HashMap<>();
    public static final Logger LOG = LogManager.getLogger(ByIronOrFire.MODID);

    public static Identifier id(String name) {
        return new Identifier(MODID, name);
    }

    @Override
    public void onInitialize() {

        LOG.info("By Iron or Fire Initializing");

        //Registering of Items
        ItemRegistry.init();
        //Registering of Entities
        EntityRegistry.init();
        //Registering Recipes
        Registry.register(Registry.RECIPE_SERIALIZER, (MODID + ":elytra_attachment"), ElytraAttachmentRecipe.CRAFTING_ATTACHED_ELYTRA);
        Registry.register(Registry.RECIPE_SERIALIZER, (MODID + ":mortar_bomb"), MortarBombRecipe.CRAFTING_MORTAR_BOMB);

        /*Generating & Registering Armor Visibility Values. Will loop through all armor items, get there materials,
         and generates visibility values for all non existent sets. Will skip over Visibility Values added by other mods*/
        ServerLifecycleEvents.SERVER_STARTING.register((server) -> VisibilityRegistry.init());

        LOG.info("By Iron or Fire Initializing Complete, Success!");
    }

    @Override
    public void onInitializeClient() {

        //Registering Client Packets
        ClientPacketsRegistry.init();
        //Registering Client Callbacks
        ClientCallbackRegistry.init();
        //Registering of Entity Renderers
        EntityRendererRegistry.INSTANCE.register(EntityRegistry.SMALL_BOMB_ENTITY, (dispatcher, context) -> new SmallBombEntityRenderer(dispatcher));

        //Registers all Dyeable Item's
        ColorProviderRegistry.ITEM.register((Stack, tintIndex) -> tintIndex > 0 ? -1 : ((DyeableItem) Stack.getItem()).getColor(Stack), ItemRegistry.CHAINED_LEATHER_HELMET, ItemRegistry.CHAINED_LEATHER_CHESTPLATE, ItemRegistry.CHAINED_LEATHER_LEGGINGS, ItemRegistry.CHAINED_LEATHER_BOOTS);

    }
}
