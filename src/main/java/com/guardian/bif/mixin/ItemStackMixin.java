package com.guardian.bif.mixin;

import com.guardian.bif.util.ElytraHooks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "getTooltip", at = @At("RETURN"), cancellable = true)
    public void getTooltip(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir) {
        ElytraHooks.appendAttachedElytraTooltip((ItemStack) (Object) this, cir.getReturnValue());
    }

}
