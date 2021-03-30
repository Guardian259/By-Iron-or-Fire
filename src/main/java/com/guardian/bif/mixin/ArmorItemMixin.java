package com.guardian.bif.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorItem.class)
public class ArmorItemMixin {

    @Shadow @Final @Mutable protected ArmorMaterial type;
    @Shadow @Final @Mutable private int protection;
    @Shadow @Final @Mutable private float toughness;
    @Shadow @Final @Mutable protected float knockbackResistance;

    @Inject(method = "ArmorItem", at = @At("HEAD"))
    private void armorMaterial(ArmorMaterial material, EquipmentSlot slot, Item.Settings settings, CallbackInfo ci){

        this.type = material;
        this.protection = material.getProtectionAmount(slot);
        this.toughness = material.getToughness();
        this.knockbackResistance = material.getKnockbackResistance();
    }


}
