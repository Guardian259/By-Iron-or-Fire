/**package com.guardian.bif.util;

import com.guardian.bif.util.compoundtags.ElytraTag;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import top.theillusivec4.caelus.api.CaelusApi;

import java.util.List;
import java.util.UUID;

public class ElytraHooks {

    public static final EntityAttributeModifier FLIGHT_MODIFIER = new EntityAttributeModifier(UUID.fromString("668bdbee-32b6-4c4b-bf6a-5a30f4d02e37"), "Flight modifier", 1.0d, EntityAttributeModifier.Operation.ADDITION);

    public static void updateColytra(PlayerEntity playerEntity) {
        ItemStack stack = playerEntity.getEquippedStack(EquipmentSlot.CHEST);
        EntityAttributeInstance attributeInstance = playerEntity.getAttributeInstance(CaelusApi.ELYTRA_FLIGHT);

        if (attributeInstance != null) {
            attributeInstance.removeModifier(FLIGHT_MODIFIER);
            if (ElytraTag.isUseable(ElytraTag.getElytra(stack))) {
                attributeInstance.addTemporaryModifier(FLIGHT_MODIFIER);
                ItemStack elytraStack = ElytraTag.getElytra(stack);
                int ticksFlying = playerEntity.getRoll();
                if ((ticksFlying + 1) % 20 != 0) {
                    return;
                }
                ElytraTag.damageElytra(playerEntity, stack, elytraStack, 1);
            }
        }
    }

    public static void appendAttachedElytraTooltip(ItemStack itemStack, List<Text> tooltip) {
        if (!ElytraTag.hasUpgrade(itemStack)) {
            return;
        }
        ItemStack elytraStack = ElytraTag.getElytra(itemStack);

        if (elytraStack.isEmpty()) {
            return;
        }
        tooltip.add(new LiteralText(""));

        /*Elytra display name*
        if (elytraStack.hasCustomName()) {
            Text display = elytraStack.getName();
            if (display instanceof MutableText) {
                ((MutableText) display).formatted(Formatting.AQUA, Formatting.ITALIC);
            }
            tooltip.add(display);
        } else {
            tooltip.add(new TranslatableText("item.minecraft.elytra").formatted(Formatting.AQUA));
        }

        if (ElytraTag.isUseable(elytraStack)) {
            tooltip.add(new LiteralText(" ").append(new TranslatableText("item.durability", elytraStack.getMaxDamage() - elytraStack.getDamage(), elytraStack.getMaxDamage())));
        } else {
            tooltip.add(new LiteralText(" ").append(new TranslatableText("tooltip.elytra.broken").formatted(Formatting.RED)));
        }

    }

}*/
