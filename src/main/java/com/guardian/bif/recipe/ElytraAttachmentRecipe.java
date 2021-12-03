package com.guardian.bif.recipe;

import com.guardian.bif.util.compoundtags.ElytraTag;
import com.guardian.bif.util.registries.server.ItemRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class ElytraAttachmentRecipe extends SpecialCraftingRecipe {

    public static final SpecialRecipeSerializer<ElytraAttachmentRecipe> CRAFTING_ATTACHED_ELYTRA = new SpecialRecipeSerializer<>(ElytraAttachmentRecipe::new);

    public ElytraAttachmentRecipe(Identifier id) {
        super(id);
    }

    private static void mergeEnchantments(ItemStack source, ItemStack destination) {
        Map<Enchantment, Integer> mappedSource = EnchantmentHelper.get(source);
        Map<Enchantment, Integer> mappedDestination = EnchantmentHelper.get(destination);

        for (Enchantment enchantment : mappedSource.keySet()) {
            if (enchantment == null || !enchantment.isAcceptableItem(destination)) {
                continue;
            }
            int destinationLevel = mappedDestination.getOrDefault(enchantment, 0);
            int sourceLevel = mappedSource.get(enchantment);
            sourceLevel = destinationLevel == sourceLevel ? sourceLevel + 1 : Math.max(sourceLevel, destinationLevel);

            for (Enchantment destinationEnchantment : mappedDestination.keySet()) {
                if (enchantment != destinationEnchantment && !destinationEnchantment.canCombine(enchantment)) {
                    return;
                }
            }

            if (sourceLevel > enchantment.getMaxLevel()) {
                sourceLevel = enchantment.getMaxLevel();
            }
            mappedDestination.put(enchantment, sourceLevel);
        }
        EnchantmentHelper.set(mappedDestination, destination);
        EnchantmentHelper.set(new HashMap<>(), source);
    }

    @Override
    public boolean matches(CraftingInventory inv, World world) {
        ItemStack itemstack = ItemStack.EMPTY;
        ItemStack elytra = ItemStack.EMPTY;

        for (int i = 0; i < inv.size(); ++i) {
            ItemStack currentStack = inv.getStack(i);

            if (currentStack.isEmpty()) {
                continue;
            }

            if (isValid(currentStack)) {
                /*Checks for if the item exist, does it already have an elytra attached, and is it a leather armor set*/
                if (!itemstack.isEmpty() || ElytraTag.hasUpgrade(currentStack) || !(((ArmorItem) currentStack.getItem()).getMaterial() == ArmorMaterials.LEATHER || (((ArmorItem) currentStack.getItem()).getMaterial()) == ItemRegistry.CHAINED_LEATHER)) {
                    return false;
                }
                itemstack = currentStack;
            } else {
                if (!elytra.isEmpty() || !(currentStack.getItem() instanceof ElytraItem)) {
                    return false;
                }
                elytra = currentStack;
            }
        }
        return !itemstack.isEmpty() && !elytra.isEmpty();
    }

    @Override
    public ItemStack craft(CraftingInventory inv) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack elytra = ItemStack.EMPTY;

        /*Crafting Slot Check Loop*/
        for (int i = 0; i < inv.size(); i++) {
            ItemStack currentStack = inv.getStack(i);

            if (currentStack.isEmpty()) {
                continue;
            }

            if (isValid(currentStack)) {
                if (!itemStack.isEmpty()) {
                    return ItemStack.EMPTY;
                }
                itemStack = currentStack.copy();
                itemStack.setCount(1);
            } else {
                if (!(currentStack.getItem() instanceof ElytraItem)) {
                    return ItemStack.EMPTY;
                }
                elytra = currentStack.copy();
            }
        }

        /*Enchantment Merger, Repair Cost Set, & Custom Elytra Name Preserved*/
        if (!itemStack.isEmpty() && !elytra.isEmpty()) {

            mergeEnchantments(elytra, itemStack);
            itemStack.setRepairCost(elytra.getRepairCost() + itemStack.getRepairCost());
            Text name = elytra.getName();
            boolean hasCustomName = elytra.hasCustomName();

            if (hasCustomName) {
                elytra.setCustomName(name);
            }
            /*Stores Elytra as CompoundTag*/
            itemStack.getOrCreateNbt().put(ElytraTag.ELYTRA_TAG, elytra.writeNbt(new NbtCompound()));
            return itemStack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CRAFTING_ATTACHED_ELYTRA;
    }

    private static boolean isValid(ItemStack stack) {
        return MobEntity.getPreferredEquipmentSlot(stack) == EquipmentSlot.CHEST && !(stack.getItem() instanceof ElytraItem);
    }
}
