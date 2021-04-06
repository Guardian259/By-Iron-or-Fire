package com.guardian.bif.mixin;

import com.guardian.bif.ByIronOrFire;
import com.guardian.bif.util.ElytraHooks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(PlayerEntity.class)
public class PlayerEntityMixin extends LivingEntityMixin {

    @Shadow
    @Final
    public PlayerInventory inventory;

    @Inject(method = "tick", at = @At("HEAD"))
    public void onTick(CallbackInfo ci) {
        PlayerEntity playerEntity = (PlayerEntity) (Object) this;
        ElytraHooks.updateColytra(playerEntity);

        DefaultedList<ItemStack> armorArray = inventory.armor;

        for (int currentSlotId = 0; currentSlotId < armorArray.size(); currentSlotId++) {
            Item currentItem = armorArray.get(currentSlotId).getItem();
            int[] visibilityRanges;
            int itemVisibility;

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

}
