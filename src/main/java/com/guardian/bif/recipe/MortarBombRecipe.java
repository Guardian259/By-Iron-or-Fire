package com.guardian.bif.recipe;

import com.google.common.collect.Lists;
import com.guardian.bif.util.registries.server.ItemRegistry;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

public class MortarBombRecipe extends SpecialCraftingRecipe {

    public static final SpecialRecipeSerializer<MortarBombRecipe> CRAFTING_MORTAR_BOMB = new SpecialRecipeSerializer<>(MortarBombRecipe :: new);

    private static final Ingredient DURRATION_MODIFIER;
    private static final Ingredient FIREWORKSTAR;
    private static final Ingredient STRING;
    private static final Ingredient HONEYCOMB;

    public MortarBombRecipe(Identifier id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingInventory inv, World world) {
        boolean bl = false;
        boolean bl2 = false;
        int durration = 0;
        Tag color;


        for (int i = 0; i < inv.size(); i++) {
            ItemStack itemStack = inv.getStack(i);
            if(!itemStack.isEmpty()){
                if(HONEYCOMB.test(itemStack)){
                    if(bl){
                        return false;
                    }
                    bl = true;
                }else if(STRING.test(itemStack)){
                    if(bl2){
                        return false;
                    }
                    bl2 = true;
                }else if(DURRATION_MODIFIER.test(itemStack)){
                    ++durration;
                    if(i > 3){
                        return false;
                    }
                }else if(FIREWORKSTAR.test(itemStack)){
                    color = itemStack.getTag().get("Colors");

                }else if(!FIREWORKSTAR.test(itemStack)){
                    return false;
                }
            }
        }

        return bl && bl2 && durration >= 1;
    }

    @Override
    public ItemStack craft(CraftingInventory inv) {
        ItemStack itemStack = new ItemStack(ItemRegistry.SMALL_BOMB);
        CompoundTag compoundTag = itemStack.getOrCreateSubTag("small_bomb");
        List<Integer> colorList = Lists.newArrayList();

        for (int j = 0; j < inv.size(); j++) {
            ItemStack currentStack = inv.getStack(j);
            if(!currentStack.isEmpty()){
               if(currentStack.getItem() instanceof DyeItem){
                   colorList.add(((DyeItem) currentStack.getItem()).getColor().getFireworkColor());
               }
            }
        }

        compoundTag.putBoolean("Trail", true);
        compoundTag.putIntArray("Colors", colorList);
        return itemStack;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    public ItemStack getOutput(){return new ItemStack(ItemRegistry.SMALL_BOMB);}

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    static {
        STRING = Ingredient.ofItems(Items.STRING);
        HONEYCOMB = Ingredient.ofItems(Items.HONEYCOMB);
        FIREWORKSTAR = Ingredient.ofItems(Items.FIREWORK_STAR);
        DURRATION_MODIFIER = Ingredient.ofItems(Items.GUNPOWDER);
    }
}
