package com.guardian.bif.armor;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Rarity;

public class DyeableArmor extends DyeableArmorItem {

    public DyeableArmor(ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot) {
        super(armorMaterial, equipmentSlot, new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.UNCOMMON));
    }

}
