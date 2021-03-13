package com.guardian.bif.mixin;

import com.guardian.bif.ByIronOrFire;
import com.guardian.bif.util.EntityVisibility;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements EntityVisibility {


    @Inject(method = "method_30125", at = @At("TAIL"))
    private void equipmentVisibility(EquipmentSlot equipmentSlot, CallbackInfoReturnable<ItemStack> cir){
        Item currentItem = cir.getReturnValue().getItem();
        int[] visibilityRanges;
        int itemVisibility;
        int currentSlotId = equipmentSlot.getEntitySlotId();

        if(currentItem instanceof ArmorItem) {
            /*Compares the armor materials name with a corresponding vanilla int[] array; Will throw an error for modded armor materials*/
            String key = ((ArmorItem) currentItem).getMaterial().getName();
            visibilityRanges = ByIronOrFire.ARMORVISIBILITY.get(key);
            itemVisibility = visibilityRanges[currentSlotId];
            this.entityVisibility[currentSlotId] = itemVisibility;
        }else{
            /*For default case, as well as, all VANILLA unknowns; This will happily function for all mob types, including mobs that do not wear armor*/
            visibilityRanges = ByIronOrFire.ARMORVISIBILITY.get("iron");
            itemVisibility = visibilityRanges[currentSlotId];
            this.entityVisibility[currentSlotId] = itemVisibility;
        }
    }

}
