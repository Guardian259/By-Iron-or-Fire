package com.guardian.bif;

import com.guardian.bif.armor.ElytraLeatherArmor;
import com.guardian.bif.util.event.RenderEntityElytraCallback;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;

import top.theillusivec4.caelus.api.event.RenderElytraCallback;

public class ByIronOrFireClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        //Registers all Dyeable Item's
        ColorProviderRegistry.ITEM.register((Stack, tintIndex) -> tintIndex > 0 ? -1 : ((DyeableItem) Stack.getItem()).getColor(Stack), ByIronOrFire.Items.CHAINED_LEATHER_HELMET, ByIronOrFire.Items.CHAINED_LEATHER_CHESTPLATE, ByIronOrFire.Items.CHAINED_LEATHER_LEGGINGS, ByIronOrFire.Items.CHAINED_LEATHER_BOOTS, ByIronOrFire.Items.CHAINED_ELYTRA_LEATHER_CHESTPATE, ByIronOrFire.Items.ELYTRA_LEATHER_CHESTPATE);

        //Renders the Elytra Model on Player Entities. Does not render on Armor Stands.
        RenderElytraCallback.EVENT.register((playerEntity, renderElytraInfo) -> {

            ItemStack itemStack = playerEntity.getEquippedStack(EquipmentSlot.CHEST);

            if(itemStack.getItem() instanceof ElytraLeatherArmor){

                renderElytraInfo.activateRender();

                if(itemStack.hasGlint()){

                    renderElytraInfo.activateGlow();

                }

            }

        });

        //Renders the Elytra Model on Armor Stand Entities. Does NOT currently function.
        RenderEntityElytraCallback.EVENT.register((armorStandEntity, renderElytraInfo) -> {

            ItemStack itemStack = armorStandEntity.getEquippedStack(EquipmentSlot.CHEST);

            if(itemStack.getItem() instanceof ElytraLeatherArmor){

                renderElytraInfo.activateRender();

                if(itemStack.hasGlint()){

                    renderElytraInfo.activateGlow();

                }

            }
        });

    }
}
