package com.guardian.bif;

import com.guardian.bif.armor.*;
import com.guardian.bif.armor.material.*;
import com.guardian.bif.client.SmallBombEntityRenderer;
import com.guardian.bif.util.event.RenderEntityElytraCallback;
import com.guardian.bif.util.registries.ClientPacketsRegistry;
import com.guardian.bif.util.registries.EntityRegistry;
import com.guardian.bif.util.registries.ItemRegistry;
import com.guardian.bif.util.registries.VisibilityRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.caelus.api.event.RenderElytraCallback;

import java.util.HashMap;
import java.util.Map;


public class ByIronOrFire implements ModInitializer, ClientModInitializer {

    public static final String MODID = "bif";

    public static final ArmorMaterial CHAINED_NETHERITE = new ChainedNetherite();
    public static final ArmorMaterial CHAINED_DIAMOND = new ChainedDiamond();
    public static final ArmorMaterial CHAINED_IRON = new ChainedIron();
    public static final ArmorMaterial CHAINED_GOLD = new ChainedGold();
    public static final ArmorMaterial CHAINED_LEATHER = new ChainedLeather();
    public static final Map<ArmorMaterial, int[]> ARMORVISIBILITY = new HashMap<>();
    public static final Logger LOG = LogManager.getLogger(ByIronOrFire.MODID);

    public static Identifier id(String name) {
        return new Identifier(MODID, name);
    }

    @Override
    public void onInitialize() {

        LOG.info("By Iron or Fire Initializing");

        //Registry of Entity Renderers
        EntityRendererRegistry.INSTANCE.register(EntityRegistry.SMALL_BOMB, (dispatcher, context) -> new SmallBombEntityRenderer(dispatcher));

        ItemRegistry.init();
        EntityRegistry.init();
        ServerLifecycleEvents.SERVER_STARTING.register((server) -> VisibilityRegistry.init());

        LOG.info("By Iron or Fire Initializing Complete, Success!");
    }

    @Override
    public void onInitializeClient() {

        //Registering Client Packets
        ClientPacketsRegistry.init();

        //Registers all Dyeable Item's
        ColorProviderRegistry.ITEM.register((Stack, tintIndex) -> tintIndex > 0 ? -1 : ((DyeableItem) Stack.getItem()).getColor(Stack), ItemRegistry.CHAINED_LEATHER_HELMET, ItemRegistry.CHAINED_LEATHER_CHESTPLATE, ItemRegistry.CHAINED_LEATHER_LEGGINGS, ItemRegistry.CHAINED_LEATHER_BOOTS, ItemRegistry.CHAINED_ELYTRA_LEATHER_CHESTPATE, ItemRegistry.ELYTRA_LEATHER_CHESTPATE);

        //Renders the Elytra Model on Player Entities. Does not render on Armor Stands.
        RenderElytraCallback.EVENT.register((playerEntity, renderElytraInfo) -> {
            ItemStack itemStack = playerEntity.getEquippedStack(EquipmentSlot.CHEST);
            if (itemStack.getItem() instanceof ElytraLeatherArmor) {
                renderElytraInfo.activateRender();
                if (itemStack.hasGlint()) {
                    renderElytraInfo.activateGlow();
                }
            }
        });

        //Renders the Elytra Model on Armor Stand Entities. Does NOT currently function.
        RenderEntityElytraCallback.EVENT.register((armorStandEntity, renderElytraInfo) -> {
            ItemStack itemStack = armorStandEntity.getEquippedStack(EquipmentSlot.CHEST);
            if (itemStack.getItem() instanceof ElytraLeatherArmor) {
                renderElytraInfo.activateRender();
                if (itemStack.hasGlint()) {
                    renderElytraInfo.activateGlow();
                }
            }
        });
    }
}
