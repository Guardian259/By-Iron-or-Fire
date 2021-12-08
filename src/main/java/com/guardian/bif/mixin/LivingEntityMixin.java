package com.guardian.bif.mixin;

import com.guardian.bif.ByIronOrFire;
import com.guardian.bif.util.EntityAccessor;
import com.guardian.bif.util.LivingEntityAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements LivingEntityAccessor, EntityAccessor {
    /*=================================================================================================================
     * An explanation of the intended function of the Freezing System
     *=================================================================================================================
     * The Freezing System is spread across three mixin classes, with the core of the systems being defined here within
     * LivingEntity. Both PlayerEntity & SkeletonEntity access and make use of inIcyWater, inIcyBiome, and inSnowstorm
     * Booleans to define there intended custom functionality. icyWaterTick, icyBiomeTick, snowstormTick, and
     * snowfallMultiplier are to be used to modulate the rate, duration, and maximum damage dealt to entities that are
     * effected by the Freezing effect.
     *
     * The System within LivingEntity is comprised of two mixins, one injection which occurs directly after the vanilla
     * powdered snow check, and one redirect which is meant to catch the vanilla damage call and add better modularity
     *=================================================================================================================*/

    /*=================================================================================================================
     * Fields used in the visibility system
     *=================================================================================================================*/

    @Unique
    int[] entityVisibility = new int[4];

    @Unique
    float minEntityHealth = 0.0F;

    @Unique
    int frozenTickDamage = 1;

    @Unique
    public boolean isDetected;

    /*=================================================================================================================
     * Fields used in the modified freezing mechanic
     *=================================================================================================================*/

    @Unique
    public boolean inIcyWater;

    @Unique
    public boolean inIcyBiome;

    @Unique
    public boolean inSnowstorm;

    @Unique
    int icyWaterTick = 3;

    @Unique
    int icyBiomeTick = 1;

    @Unique
    int snowstormTick = 1;

    @Unique
    int snowfallMultiplier = 1;

    /*=================================================================================================================
     * Methods used in the modified freezing mechanic
     *=================================================================================================================*/

    @Shadow
    public abstract boolean damage(DamageSource source, float amount);

    @Shadow
    public abstract boolean canFreeze();

    @Shadow
    public abstract boolean isDead();

    //needs fixing; may not even be needed
    //@Shadow
    //public abstract int getFrozenTicks();

    //needs fixing; may not even be needed
    //@Shadow
    //public abstract void setFrozenTicks(int tick);

    @Override
    public boolean isFreezing(){
        return this.getFrozenTicks() >= this.getMinFreezeDamageTicks();
    }

    @Override
    public abstract int getFrozenTickDamage();

    @Override
    public abstract void setFrozenTickDamage(int damage);

    @Override
    public float getMinEntityHealth(){
        return this.minEntityHealth;
    }

    @Override
    public void setMinEntityHealth(float health){
        this.minEntityHealth = health;
    }

    /*=================================================================================================================
     * Methods added to Living Entity used in the visibility system
     *=================================================================================================================*/

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

    /*=================================================================================================================
     * Freezing Mechanic Changes
     *=================================================================================================================*/

    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;removePowderSnowSlow()V"))
    private void tickMovement(CallbackInfo ci){

        LivingEntity livingEntity = (LivingEntity) (Object) this;

        int currentTicks;
        if (!livingEntity.world.isClient && !this.isDead()) {
            currentTicks = this.getFrozenTicks();
            //Additional checksums for the Vanilla Freezing Mechanic
            if ((this.inIcyBiome || this.inIcyWater || this.inSnowstorm) && this.canFreeze()) {
                //corrects for vanilla PowderSnow system and sets FrozensTicks dynamically
                this.setFrozenTicks(Math.min(this.getMinFreezeDamageTicks(), currentTicks + ((!livingEntity.inPowderSnow ? (currentTicks == 0 ? 1 : 2) : 0) + (icyWaterTick + icyBiomeTick + (snowfallMultiplier * snowstormTick)))));
            } else {
                //Restores FrozenTicks to 0 over time
                this.setFrozenTicks(Math.max(0, currentTicks - 2));
            }
        }
    }

    //Modifies the DamageSource call for freezing damage, which can accept variable damage rates and limits
    @Redirect( method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private boolean tickMovementDamage(@NotNull LivingEntity instance, DamageSource source, float amount){
        if (instance.getHealth() > minEntityHealth) {
            return this.damage(source, (float) frozenTickDamage);
        }
        return false;
    }

    /*=================================================================================================================
     * Armor Visibility Check & AI Visibility Check
     *=================================================================================================================*/

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
