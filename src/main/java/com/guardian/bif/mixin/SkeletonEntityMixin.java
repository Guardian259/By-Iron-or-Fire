package com.guardian.bif.mixin;

import com.guardian.bif.util.LivingEntityAccessor;
import net.minecraft.entity.mob.SkeletonEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkeletonEntity.class)
public abstract class SkeletonEntityMixin implements LivingEntityAccessor {

    @Shadow
    private int inPowderSnowTime;

    @Shadow
    boolean inIcyWater;

    @Shadow
    boolean inIcyBiome;

    @Shadow
    boolean inSnowstorm;

    @Shadow
    private void setConversionTime(int time) {

    }


    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/AbstractSkeletonEntity;tick()V"))
    public void freezeConvert(CallbackInfo ci){

        //Depreciate this
        SkeletonEntity skeletonEntity = (SkeletonEntity) (Object) this;

        if (!skeletonEntity.world.isClient && skeletonEntity.isAlive() && !skeletonEntity.isAiDisabled()) {
            if(this.inIcyWater){
                ++this.inPowderSnowTime;
                ++this.inPowderSnowTime;
                if (this.inPowderSnowTime >= 140) {
                    this.setConversionTime(150);
                }
            }else if(this.inIcyBiome){
                ++this.inPowderSnowTime;
                ++this.inPowderSnowTime;
                if (this.inPowderSnowTime >= 140) {
                    this.setConversionTime(450);
                }
            }else if(this.inSnowstorm){
                ++this.inPowderSnowTime;
                ++this.inPowderSnowTime;
                if (this.inPowderSnowTime >= 140) {
                    this.setConversionTime(600);
                }
            }
        }
    }

}
