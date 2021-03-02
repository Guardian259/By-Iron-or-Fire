package com.guardian.bif.armor;

import com.guardian.bif.armor.material.MaterialVisibility;
import net.minecraft.entity.EquipmentSlot;

public enum ArmorVisibility implements MaterialVisibility {

    //(Head, Chest, Legs, Feet)
    CHAINED_NETHERITE("chained_netherite", new int[]{24,56,48,32}),
    NETHERITE("netherite", new int[]{20,52,44,28}),
    CHAINED_DIAMOND("chained_diamond", new int[]{16,48,40,24}),
    DIAMOND("diamond", new int[]{14,42,36,20}),
    CHAINED_IRON("chained_iron", new int[]{8,24,20,12}),
    IRON("iron", new int[]{8,24,20,12}),
    CHAINED("chained_gold", new int[]{6,17,15,10}),
    GOLD("gold", new int[]{4,12,10,6}),
    CHAINED_LEATHER("chained_leather", new int[]{2,6,5,3}),
    LEATHER("leather",  new int[]{1,4,2,1});

    private final String name;
    private final int[] visibilityAmounts;

    ArmorVisibility(String name, int[] visibilityAmounts) {
        this.name = name();
        this.visibilityAmounts = visibilityAmounts;
    }


    public String getMaterialName(String name) {
        return this.name;
    }

    public int getMaterialVisibility(int slot) {
        return this.visibilityAmounts[slot];
    }
}
