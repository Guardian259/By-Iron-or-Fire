package com.guardian.bif.mixin;

import com.guardian.bif.armor.ArmorVisibility;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;

//TODO: Delete class; The entirety of this class is @Deprecated; replaced and refined in LivingEntityMixin & LivingEntityRendererMixin

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityRenderer.class)
public class NameplateMixin {


    @Inject(method = "renderLabelIfPresent", at = @At("HEAD"), cancellable = true)
    public void render(AbstractClientPlayerEntity abstractClientPlayerEntity, Text text, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo info){
        int[] visibility = {8,24,20,12};

        ClientPlayerEntity p = MinecraftClient.getInstance().player;

        if(abstractClientPlayerEntity != null) {
            List<ItemStack> equipedArmor = abstractClientPlayerEntity.inventory.armor;

            for (int j = 0; j < 4; ++j) {
                ItemStack thisItem = equipedArmor.get(j);

                if (!(thisItem.getItem() instanceof ArmorItem)) {
                    visibility[j] = ArmorVisibility.IRON.getMaterialVisibility(j);
                } else {
                    String material = ((ArmorItem) thisItem.getItem()).getMaterial().getName().toUpperCase();
                    int thisArmorVisibility = ArmorVisibility.valueOf(material).getMaterialVisibility(j);
                    visibility[j] = thisArmorVisibility;
                }
            }
        }else{

            for (int k = 0; k < 4; ++k) {
                visibility[k] = ArmorVisibility.IRON.getMaterialVisibility(k);
            }

        }

        if (abstractClientPlayerEntity != null && p != null && !p.getBlockPos().isWithinDistance(abstractClientPlayerEntity.getBlockPos(), Arrays.stream(visibility).sum())) {
            info.cancel();
        }
    }
}
