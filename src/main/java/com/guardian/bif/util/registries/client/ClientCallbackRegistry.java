package com.guardian.bif.util.registries.client;

import com.guardian.bif.util.compoundtags.ElytraTag;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import top.theillusivec4.caelus.api.event.RenderElytraCallback;

public class ClientCallbackRegistry {

    private static void renderElytraRegistry() {
        //Renders the Elytra Model on Player Entities. Does not render on Armor Stands.
        RenderElytraCallback.EVENT.register((playerEntity, renderElytraInfo) -> {
            ItemStack itemStack = playerEntity.getEquippedStack(EquipmentSlot.CHEST);
            if (ElytraTag.hasUpgrade(itemStack)) {
                renderElytraInfo.activateRender();
                if (itemStack.hasGlint()) {
                    renderElytraInfo.activateGlow();
                }
            }
        });
    }

    public static void init() {
        renderElytraRegistry();
    }
}
