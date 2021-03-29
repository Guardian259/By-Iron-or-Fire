package com.guardian.bif.item;

import com.guardian.bif.entity.ExplosiveBombEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;

public class SmallBombItem extends Item {

    public static final boolean UNLIT = true;

    public SmallBombItem(Settings settings) {
        super(settings);
        CompoundTag compoundTag = this.getDefaultStack().getOrCreateTag();
        if(compoundTag.contains("unlit")){
            compoundTag.putBoolean("unlit", true);
        }
    }

    public PersistentProjectileEntity createBomb(World world, ItemStack stack, LivingEntity shooter) {
        ExplosiveBombEntity bombEntity = new ExplosiveBombEntity(world, shooter);
        bombEntity.initFromStack(stack);
        return bombEntity;
    }

}
