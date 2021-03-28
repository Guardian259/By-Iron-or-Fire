package com.guardian.bif.util.registries.client;

import com.guardian.bif.armor.ElytraLeatherArmor;
import com.guardian.bif.util.event.RenderEntityElytraCallback;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import top.theillusivec4.caelus.api.event.RenderElytraCallback;

public class ClientCallbackRegistry {

    private static void renderElytraRegistry(){
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

    public static void init(){
        renderElytraRegistry();
    }
}
