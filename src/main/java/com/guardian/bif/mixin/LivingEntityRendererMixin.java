package com.guardian.bif.mixin;

import com.guardian.bif.util.EntityVisibility;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity>{

    private LivingEntity entityLiving;

    @Inject(method = "hasLabel", at = @At("HEAD"))
    private void getEntity(T livingEntity, CallbackInfoReturnable<Boolean> cir){
        entityLiving = livingEntity;
    }

    /*Replaces float f within hasLabel and sets (32.0F : 64.0F) to (currentVisibility/2 : currentVisibility) to allow for dynamic alteration of the entity nameplate*/
    @ModifyVariable(method = "hasLabel", at = @At(value = "STORE", ordinal = 0))
    private float nameplateVisibility(float f){
        int[] array = ((EntityVisibility) entityLiving).getEntityVisibility();
        int currentVisibility = Arrays.stream(array).sum();
        return entityLiving.isSneaky() ? ((float) currentVisibility)/2 : ((float) currentVisibility);
    }

}
