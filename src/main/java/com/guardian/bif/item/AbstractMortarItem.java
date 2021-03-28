package com.guardian.bif.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;

import java.util.function.Predicate;

public class AbstractMortarItem extends Item {

/*    public static final Predicate<ItemStack> MORTAR_PROJECTILES = (stack) -> {
        return stack.getItem().isIn((Tag) ItemTags.BOMBS);
    };
    public static final Predicate<ItemStack> MORTAR_HELD_PROJECTILES;*/

    public AbstractMortarItem(Settings settings) {
        super(settings);
    }



/*    static {
        MORTAR_HELD_PROJECTILES = MORTAR_PROJECTILES.or((stack) -> {
            return stack.getItem() == Items.FIREWORK_ROCKET;
        });
    }*/

}
