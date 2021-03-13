package com.guardian.bif.armor;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Rarity;

public class NetheriteArmor extends ArmorItem {
    public NetheriteArmor(ArmorMaterial material, EquipmentSlot slot) {
        super(material, slot, new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.UNCOMMON).fireproof());
    }
}
