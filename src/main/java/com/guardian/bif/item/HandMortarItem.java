package com.guardian.bif.item;

import com.google.common.collect.Lists;
import com.guardian.bif.util.registries.server.ItemRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class HandMortarItem extends Item {

    private boolean wound = false;
    private boolean loaded = false;

    public HandMortarItem(Settings settings) {
        super(settings);
    }


    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {

        ItemStack itemstack = playerEntity.getStackInHand(hand);

        if(isLoaded(itemstack) && isWound(itemstack)){
            shootMortar(world, playerEntity, hand, itemstack,3.15F,0);
            setWound(itemstack, false);
            setLoaded(itemstack,false);
            world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.NEUTRAL, 0.5F, 0 / (RANDOM.nextFloat() * 0.4f + 0.8f));
            return TypedActionResult.consume(itemstack);
        }else if(isLoaded(itemstack) && !isWound(itemstack)){

            return TypedActionResult.consume(itemstack);
        }else if(!isLoaded(itemstack) && !isWound(itemstack)){
            return TypedActionResult.consume(itemstack);
        }else{
            return TypedActionResult.fail(itemstack);
        }
    }


    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        int i = this.getMaxUseTime(stack) - remainingUseTicks;
        float f = getLoadProgress(i, stack);
        if(f >= 1.0F && !isLoaded(stack)){
            setLoaded(stack,true);
            SoundCategory soundCategory = user instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.HOSTILE;
            world.playSound((PlayerEntity) null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_GRASS_HIT, soundCategory, 1.0F, 1.0F / (RANDOM.nextFloat() * 0.5F + 1.0F) + 0.2F);
        }
        if(f >= 1.0F && isLoaded(stack) && !isWound(stack)){
            setWound(stack, true);
            SoundCategory soundCategory = user instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.HOSTILE;
            world.playSound((PlayerEntity) null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_GRASS_HIT, soundCategory, 1.0F, 1.0F / (RANDOM.nextFloat() * 0.5F + 1.0F) + 0.2F);

        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return getLoadTime(stack) + 3;
    }

    private static int getLoadTime(ItemStack stack){
        int i = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
        return i == 0 ? 25 : 25 - 5 * i;
    }

    private static float getLoadProgress(int useTicks, ItemStack stack){
        float f = (float) useTicks / getLoadTime(stack);
        if(f > 1.0F){
            f = 1.0F;
        }
        return f;
    }

    public void setWound(ItemStack stack, boolean wound) {
        CompoundTag compoundTag = stack.getOrCreateTag();
        compoundTag.putBoolean("Wound", wound);
    }

    public static boolean isWound(ItemStack stack){
        CompoundTag compoundTag = stack.getTag();
        return compoundTag != null && compoundTag.getBoolean("Wound");
    }

    public void setLoaded(ItemStack stack, boolean loaded) {
        CompoundTag compoundTag = stack.getOrCreateTag();
        compoundTag.putBoolean("Loaded", loaded);
    }

    public static boolean isLoaded(ItemStack stack){
        CompoundTag compoundTag = stack.getTag();
        return compoundTag != null && compoundTag.getBoolean("Loaded");
    }

    public static void putProjectile(ItemStack mortar, ItemStack projectile){
        CompoundTag compoundTag = mortar.getOrCreateTag();
        ListTag listTag2;
        if(compoundTag.contains("LoadedProjectile",9)){
            listTag2 = compoundTag.getList("LoadedProjectile",10);
        }else{
            listTag2 = new ListTag();
        }
        CompoundTag loadedProjectile = new CompoundTag();
        projectile.toTag(loadedProjectile);
        listTag2.add(loadedProjectile);
        compoundTag.put("LoadedProjectile", listTag2);
    }

    private static List<ItemStack> getProjectile(ItemStack mortar){
        List<ItemStack> list = Lists.newArrayList();
        CompoundTag compoundTag = mortar.getTag();
        if (compoundTag != null && compoundTag.contains("LoadedProjectile", 9)) {
            ListTag listTag = compoundTag.getList("LoadedProjectile", 10);
            if (listTag != null) {
                for(int i = 0; i < listTag.size(); ++i) {
                    CompoundTag compoundTag2 = listTag.getCompound(i);
                    list.add(ItemStack.fromTag(compoundTag2));
                }
            }
        }

        return list;
    }

    private static void clearProjectiles(ItemStack crossbow) {
        CompoundTag compoundTag = crossbow.getTag();
        if (compoundTag != null) {
            ListTag listTag = compoundTag.getList("LoadedProjectile", 9);
            listTag.clear();
            compoundTag.put("LoadedProjectile", listTag);
        }

    }

    private static void shoot(World world, LivingEntity shooter, Hand hand, ItemStack mortar, ItemStack projectile, float soundPitch, boolean creative, float speed, float divergence, float simulated){
        if (!world.isClient) {
            Object projectileEntity;
            projectileEntity = createBomb(world, shooter, mortar, projectile);
            if (creative || simulated != 0.0F) {
                ((PersistentProjectileEntity)projectileEntity).pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
            }
            world.spawnEntity((Entity)projectileEntity);
            world.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.PLAYERS, 1.0F, soundPitch);
        }
    }

    private static PersistentProjectileEntity createBomb(World world, LivingEntity entity, ItemStack mortar, ItemStack bomb){
        SmallBombItem bombItem = (SmallBombItem)(bomb.getItem() != null ? bomb.getItem() : ItemRegistry.MORTAR_BOMB);
        PersistentProjectileEntity persistentProjectileEntity = bombItem.createBomb(world, bomb, entity);
        if(entity instanceof PlayerEntity){
            persistentProjectileEntity.setCritical(true);
        }

        persistentProjectileEntity.setSound(SoundEvents.ITEM_CROSSBOW_HIT);
        persistentProjectileEntity.setShotFromCrossbow(true);
        int i = EnchantmentHelper.getLevel(Enchantments.PIERCING, mortar);
        if (i > 0) {
            persistentProjectileEntity.setPierceLevel((byte)i);
        }

        return persistentProjectileEntity;
    }

    public static void shootMortar(World world, LivingEntity entity, Hand hand, ItemStack mortar, float speed, float divergence){
        List<ItemStack> list = getProjectile(mortar);
        float[] fs = getSoundPitches(entity.getRandom());
        for(int i = 0; i < list.size(); ++i) {
            ItemStack bomb = list.get(i);
            boolean bl = entity instanceof PlayerEntity && ((PlayerEntity) entity).abilities.creativeMode;
            shoot(world, entity, hand, mortar, bomb, fs[i], bl, speed, divergence, 10.0F);
        }
        postShoot(world, entity, mortar);
    }

    private static void postShoot(World world, LivingEntity entity, ItemStack stack) {
        if (entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)entity;
/*            if (!world.isClient) {
                Criteria.SHOT_CROSSBOW.trigger(serverPlayerEntity, stack);
            }*/
            serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
        }

        clearProjectiles(stack);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if(!world.isClient){
            int i = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
            SoundEvent soundEvent = i == 0 ? SoundEvents.ITEM_CROSSBOW_LOADING_MIDDLE : null;
            float f = (float)(stack.getMaxUseTime() - remainingUseTicks) / (float)getLoadTime(stack);

            if(f>= 0.2F && !this.loaded && !this.wound){
                this.loaded = true;
                world.playSound((PlayerEntity)null, user.getX(), user.getY(), user.getZ(), soundEvent, SoundCategory.PLAYERS, 0.5F, 1.0F);
            }

            if(f >= 0.2F && this.loaded && !this.wound){
                this.wound = true;
                world.playSound((PlayerEntity)null, user.getX(), user.getY(), user.getZ(), soundEvent, SoundCategory.PLAYERS, 0.5F, 1.0F);
            }

        }
    }

    private static float[] getSoundPitches(Random random) {
        boolean bl = random.nextBoolean();
        return new float[]{1.0F, getSoundPitch(bl), getSoundPitch(!bl)};
    }

    private static float getSoundPitch(boolean flag) {
        float f = flag ? 0.63F : 0.43F;
        return 1.0F / (RANDOM.nextFloat() * 0.5F + 1.8F) + f;
    }
}
