package com.guardian.bif.item;

import com.guardian.bif.entity.ExplosiveBombEntity;
import com.guardian.bif.util.registries.EntityRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class HandMortarItem extends Item {

    private boolean isCharged = false;
    private boolean isLoaded = false;

    public HandMortarItem(Settings settings) {
        super(settings);
    }


    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {

        ItemStack itemstack = playerEntity.getStackInHand(hand);

        if(isLoaded && isCharged){

            //setCharged(itemstack, false);
            //setLoaded(itemstack,false);
            return TypedActionResult.consume(itemstack);
        }
        //launch sound
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.NEUTRAL, 0.5F, 0 / (RANDOM.nextFloat() * 0.4f + 0.8f));

        if (!world.isClient) {
            // Spawn Projectile
            ExplosiveBombEntity bombEntity = new ExplosiveBombEntity(EntityRegistry.SMALL_BOMB, world);
            bombEntity.setPos(playerEntity.getX(), playerEntity.getY(), playerEntity.getZ());
            bombEntity.setOwner(playerEntity);
            bombEntity.setProperties(playerEntity, playerEntity.pitch, playerEntity.yaw, 0, 1, 0);
        }

        playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!playerEntity.abilities.creativeMode) {
            itemstack.decrement(1);
        }

        return TypedActionResult.success(itemstack, world.isClient());
    }


/*    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        int i = this.getMaxUseTime(stack) - remainingUseTicks;
        float f = getLoadProgress(i, stack);
        if(f >= 1.0F && !isLoaded){
            setLoaded(stack,true);
            SoundCategory soundCategory = user instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.HOSTILE;
            world.playSound((PlayerEntity) null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_CROSSBOW_LOADING_END, soundCategory, 1.0F, 1.0F / (RANDOM.nextFloat() * 0.5F + 1.0F) + 0.2F);
        }
        if(f >= 1.0F && !isCharged && isLoaded){
            setCharged(stack, true);
            SoundCategory soundCategory = user instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.HOSTILE;
            world.playSound((PlayerEntity) null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_CROSSBOW_LOADING_END, soundCategory, 1.0F, 1.0F / (RANDOM.nextFloat() * 0.5F + 1.0F) + 0.2F);

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

    public void setCharged(ItemStack stack, boolean charged) {
        isCharged = charged;
    }

    public void setLoaded(ItemStack stack, boolean loaded) {
        isLoaded = loaded;
    }

    private static void shoot(World world, LivingEntity shooter, Hand hand, ItemStack mortar, ItemStack projectile, float soundPitch, boolean creative, float speed, float divergence, float simulated){

    }

    private static PersistentProjectileEntity createBomb(World world, LivingEntity entity, ItemStack mortar, ItemStack bomb){
        SmallBombItem bombItem = (SmallBombItem)(bomb.getItem() != null ? bomb.getItem() : ByIronOrFire.Items.MORTAR_BOMB);
        PersistentProjectileEntity persistentProjectileEntity = bombItem.createBomb(world, bomb, entity);
        if(entity instanceof PlayerEntity){
            persistentProjectileEntity.setCritical(true);
        }
    }

    public static void shootMortar(World world, LivingEntity entity, Hand hand, ItemStack mortar, float speed, float divergence){
        float[] fs = getSoundPitches(entity.getRandom());

            boolean bl = entity instanceof PlayerEntity && ((PlayerEntity)entity).abilities.creativeMode;

                    shoot(world, entity, hand, mortar, bomb, fs, bl, speed, divergence, 10.0F);


        postShoot(world, entity, stack);
    }

    private static float[] getSoundPitches(Random random) {
        boolean bl = random.nextBoolean();
        return new float[]{1.0F, getSoundPitch(bl), getSoundPitch(!bl)};
    }

    private static float getSoundPitch(boolean flag) {
        float f = flag ? 0.63F : 0.43F;
        return 1.0F / (RANDOM.nextFloat() * 0.5F + 1.8F) + f;
    }*/
}
