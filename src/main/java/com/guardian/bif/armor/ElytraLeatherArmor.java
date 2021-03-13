package com.guardian.bif.armor;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/*Currently is Rendering but non Functioning; Does NOT render on armor stands*/
public class ElytraLeatherArmor extends DyeableArmorItem {

    public ElytraLeatherArmor(ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot) {
        super(armorMaterial, equipmentSlot, new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.UNCOMMON));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add( new TranslatableText("item.bif.elytra_chestplate.tooltip").formatted(Formatting.GRAY));
    }






}
