package com.guardian.bif.armor;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Rarity;

public class BaseArmor extends ArmorItem {

    public BaseArmor(ArmorMaterial material, EquipmentSlot slot) {
        super(material, slot, new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.UNCOMMON));
    }
}
