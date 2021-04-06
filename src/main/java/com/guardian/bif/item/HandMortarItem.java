package com.guardian.bif.item;


import com.guardian.bif.entity.ExplosiveBombEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.function.Predicate;


public class HandMortarItem extends RangedWeaponItem {


    public HandMortarItem(Settings settings) {
        super(settings);
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return null;
    }

    @Override
    public int getRange() {
        return 0;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        ItemStack itemStack = user.getStackInHand(hand);
        ItemStack bombItem = ItemStack.EMPTY;
        if(!world.isClient()){
/*            for (int i = 0; i < user.inventory.size(); i++) {
                ItemStack stack = user.inventory.getStack(i);
                if(stack.getItem() instanceof SmallBombItem){
                    bombItem = stack;
                }
            }*/

            ExplosiveBombEntity explosiveBombEntity = new ExplosiveBombEntity(world, user);
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.NEUTRAL, 0.5F, 0 / (RANDOM.nextFloat() * 0.4f + 0.8f));
            explosiveBombEntity.setProperties(user, user.pitch, user.yaw, 0.0F, 2.0F, 7.5F);
            world.spawnEntity(explosiveBombEntity);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if(!user.abilities.creativeMode /*&& bombItem == ItemStack.EMPTY*/){
            bombItem.decrement(1);
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }
}
