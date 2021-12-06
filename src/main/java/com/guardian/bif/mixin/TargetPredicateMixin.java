package com.guardian.bif.mixin;

import com.guardian.bif.util.LivingEntityAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TargetPredicate.class)
public class TargetPredicateMixin {

    /*Checks whether an entity is actually detected. If true said entities visibility range is increased*/
    @Inject(method = "test", at = @At("TAIL"))
    private void entityDetected(LivingEntity baseEntity, LivingEntity targetEntity, CallbackInfoReturnable<Boolean> cir){
        ((LivingEntityAccessor) targetEntity).setIsDetected(cir.getReturnValue());
    }

}
