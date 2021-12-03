package com.guardian.bif.mixin;

import com.guardian.bif.ByIronOrFire;
import com.guardian.bif.util.LivingEntityAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements LivingEntityAccessor {

    @Unique
    int[] entityVisibility = new int[4];

    @Unique
    boolean isDetected = false;


    @Override
    public int[] getEntityVisibility() {
        return this.entityVisibility;
    }

    @Override
    public void setEntityVisibility(int slotId, int value) {
        this.entityVisibility[slotId] = value;
    }

    @Override
    public boolean getIsDetected() {
        return this.isDetected;
    }

    @Override
    public void setIsDetected(boolean status) {
        this.isDetected = status;
    }

    @Inject(method = "getSyncedArmorStack", at = @At("TAIL"))
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

    @Inject(method = "getAttackDistanceScalingFactor", at = @At("TAIL"), cancellable = true)
    public void attackDistanceMultiplier(@Nullable Entity entity, CallbackInfoReturnable<Double> cir) {
        int currentVisibility = Arrays.stream(this.entityVisibility).sum();
        double detected = getIsDetected() ? ((double) (currentVisibility / 2)) : 0;
        double d = cir.getReturnValue();
        d *= (((currentVisibility * 2) + detected) / 64d);
        cir.setReturnValue(d);
    }


}
