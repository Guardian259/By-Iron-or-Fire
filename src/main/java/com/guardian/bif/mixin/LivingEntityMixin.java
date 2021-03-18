package com.guardian.bif.mixin;

import com.guardian.bif.ByIronOrFire;
import com.guardian.bif.util.EntityVisibility;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements EntityVisibility {

    @Unique
    int[] entityVisibility = new int[4];


    @Override
    public int[] getEntityVisibility() {
        return this.entityVisibility;
    }

    @Override
    public void setEntityVisibility(int slotId, int value) {
        this.entityVisibility[slotId] = value;
    }

    @Inject(method = "method_30125", at = @At("TAIL"))
    private void equipmentVisibility(EquipmentSlot equipmentSlot, CallbackInfoReturnable<ItemStack> cir) {
        Item currentItem = cir.getReturnValue().getItem();
        int[] visibilityRanges;
        int itemVisibility;
        int currentSlotId = equipmentSlot.getEntitySlotId();

        if (currentItem instanceof ArmorItem) {
            /*Compares the armor material with a corresponding vanilla int[] array; Will currently throw an error for modded armor materials*/
            ArmorMaterial key = ((ArmorItem) currentItem).getMaterial();
            visibilityRanges = ByIronOrFire.ARMORVISIBILITY.get(key);
            itemVisibility = visibilityRanges[currentSlotId];
            /*sets the current visibility slot with the value in the corresponding array*/
            setEntityVisibility(currentSlotId, itemVisibility);
        } else {
            /*For default case, as well as, all VANILLA unknowns; This will happily function for all mob types, including mobs that do not wear armor*/
            visibilityRanges = ByIronOrFire.ARMORVISIBILITY.get(ArmorMaterials.IRON);
            itemVisibility = visibilityRanges[currentSlotId];
            /*sets the current visibility slot with either {8,24,20,12} for a sum of 64, the default minecraft visibility value */
            setEntityVisibility(currentSlotId, itemVisibility);
        }
    }


}
