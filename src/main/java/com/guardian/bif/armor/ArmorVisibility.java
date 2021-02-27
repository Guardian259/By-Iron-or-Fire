package com.guardian.bif.armor;

import com.guardian.bif.armor.material.MaterialVisibility;
import net.minecraft.entity.EquipmentSlot;

public enum ArmorVisibility implements MaterialVisibility {

    //(Head, Chest, Legs, Feet)
    CHAINED_NETHERITE_ARMOR(new int[]{24,56,48,32}),
    NETHERITE_ARMOR(new int[]{20,52,44,28}),
    CHAINED_DIAMOND_ARMOR(new int[]{16,48,40,24}),
    DIAMOND_ARMOR(new int[]{14,42,36,20}),
    CHAINED_IRON_ARMOR(new int[]{8,24,20,12}),
    IRON_ARMOR(new int[]{8,24,20,12}),
    CHAINED_GOLD_ARMOR(new int[]{6,17,15,10}),
    GOLD_ARMOR(new int[]{4,12,10,6}),
    CHAINED_LEATHER_ARMOR(new int[]{2,6,5,3}),
    LEATHER_ARMOR(new int[]{1,4,2,1}),
    ;

    private final int[] visibilityAmounts;

    ArmorVisibility(int[] visibilityAmounts) {
        this.visibilityAmounts = visibilityAmounts;
    }

    public int getMaterialVisibility(EquipmentSlot slot) {
        return this.visibilityAmounts[slot.getEntitySlotId()];
    }
}
