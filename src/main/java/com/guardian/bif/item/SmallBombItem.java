package com.guardian.bif.item;


import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundTag;

public class SmallBombItem extends Item {

    public static final boolean UNLIT = true;

    public SmallBombItem(Settings settings) {
        super(settings);
        CompoundTag compoundTag = this.getDefaultStack().getOrCreateTag();
        if(compoundTag.contains("unlit")){
            compoundTag.putBoolean("unlit", true);
        }
    }


}
