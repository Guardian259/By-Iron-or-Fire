package com.guardian.bif.util.compoundtags;

import com.guardian.bif.ByIronOrFire;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class ElytraTag {

    public static final String ELYTRA_TAG = ByIronOrFire.MODID + ":ElytraUpgrade";

    public static boolean hasUpgrade(ItemStack stack){
        return stack.getSubNbt(ELYTRA_TAG) != null;
    }

    public static ItemStack getElytra(ItemStack stack){
        NbtCompound compoundTag = stack.getSubNbt(ELYTRA_TAG);
        return compoundTag != null ? ItemStack.fromNbt(compoundTag) : ItemStack.EMPTY;
    }

    public static void setElytra(ItemStack chestStack, ItemStack elytraStack){
        chestStack.getOrCreateNbt().put(ELYTRA_TAG, elytraStack.writeNbt(new NbtCompound()));
    }

    public static void damageElytra(LivingEntity livingEntity,ItemStack chestStack, ItemStack elytraStack, int amount){
        elytraStack.damage(amount, livingEntity, damager -> damager.sendEquipmentBreakStatus(EquipmentSlot.CHEST));
        setElytra(chestStack, elytraStack);
    }

    public static boolean isUseable( ItemStack elytraStack){
        if(elytraStack.isEmpty()){
            return false;
        }
        return elytraStack.getItem() instanceof ElytraItem && ElytraItem.isUsable(elytraStack);
    }

}
