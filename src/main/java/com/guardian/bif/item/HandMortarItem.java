package com.guardian.bif.item;


import com.guardian.bif.ByIronOrFire;
import com.guardian.bif.entity.BombEntity;
import com.guardian.bif.util.registries.server.EntityRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;


public class HandMortarItem extends Item {

    private EntityType<? extends BombEntity> entityType;
    public static final String BOMB_TAG = ByIronOrFire.MODID + ":bomb";

    public HandMortarItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        ItemStack itemStack = user.getStackInHand(hand);

        entityType = EntityRegistry.SMALL_BOMB_ENTITY;
        shoot(world, entityType, user);
        return TypedActionResult.success(itemStack, world.isClient());

        //is the launcher loaded and wound?
/*        if(isLoaded(itemStack) && isWound(itemStack)){
            entityType = EntityRegistry.SMALL_BOMB_ENTITY;
            shoot(world, entityType, user);
            setWound(itemStack, false);
            setLoaded(itemStack, false);
            return TypedActionResult.success(itemStack, world.isClient());
        }else{
            return TypedActionResult.fail(itemStack);
        }*/
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        int i = this.getMaxUseTime(stack) - remainingUseTicks;
        float f = getLoadProgress(i, stack);
        if (f >= 1.0F && !isLoaded(stack) && loadProjectile(user, stack)){
            setLoaded(stack, true);
        }else if(f >= 1.0F && isLoaded(stack) && !isWound(stack) && loadProjectile(user, stack)){
            setWound(stack, true);
        }
    }

    private static void shoot(World world, EntityType entityType, PlayerEntity user){
        if(!world.isClient()){
            //entityType = user.isCreative() ? EntityRegistry.SMALL_BOMB_ENTITY : getProjectile(itemStack);
            BombEntity bombEntity = new BombEntity(entityType, world);
            bombEntity.setOwner(user);
            bombEntity.setPos(user.getX(), user.getEyeY(), user.getZ());
            bombEntity.updateTrackedPosition(user.getX(), user.getEyeY(), user.getZ());

            float deg2rad = (float) (Math.PI / 180);
            bombEntity.setVelocity(
                    -MathHelper.sin(user.headYaw * deg2rad) * MathHelper.cos(user.pitch * deg2rad),
                    -MathHelper.sin((user.pitch + user.getRoll()) * deg2rad),
                    MathHelper.cos(user.headYaw * deg2rad) * MathHelper.cos(user.pitch * deg2rad),
                    1.5F,
                    5
            );

            world.playSound(
                    null,
                    user.getX(),
                    user.getY(),
                    user.getZ(),
                    SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH,
                    SoundCategory.NEUTRAL,
                    0.5F,
                    0 / (RANDOM.nextFloat() * 0.4f + 0.8f));
            world.spawnEntity(bombEntity);
        }
    }

    private static boolean loadProjectile(LivingEntity shooter, ItemStack launcher) {
        ItemStack projectile = getProjectile(launcher);
        boolean creative = shooter instanceof PlayerEntity && ((PlayerEntity)shooter).abilities.creativeMode;
        if (projectile.isEmpty()) {
            return false;
        } else {
            boolean bl1 = creative && projectile.getItem() instanceof SmallBombItem;
            ItemStack itemStack2;
            if (!bl1 && !creative) {
                itemStack2 = projectile.split(1);
                if (projectile.isEmpty() && shooter instanceof PlayerEntity) {
                    ((PlayerEntity)shooter).inventory.removeOne(projectile);
                }
            } else {
                itemStack2 = projectile.copy();
            }

            putProjectile(launcher, itemStack2);
            return true;
        }
    }

    private static void putProjectile(ItemStack launcher, ItemStack projectile) {
        launcher.getOrCreateTag().put(BOMB_TAG, projectile.toTag(new CompoundTag()));
    }

    private static ItemStack getProjectile(ItemStack launcher){
            CompoundTag compoundTag = launcher.getSubTag(BOMB_TAG);
            ItemStack itemStack = compoundTag != null ? ItemStack.fromTag(compoundTag) : ItemStack.EMPTY;
            return itemStack;
    }

    public static boolean isLoaded(ItemStack stack) {
        CompoundTag compoundTag = stack.getTag();
        return compoundTag != null && compoundTag.getBoolean("loaded");
    }

    public static void setLoaded(ItemStack stack, boolean loaded) {
        CompoundTag compoundTag = stack.getOrCreateTag();
        compoundTag.putBoolean("loaded", loaded);
    }

    public static boolean isWound(ItemStack stack) {
        CompoundTag compoundTag = stack.getTag();
        return compoundTag != null && compoundTag.getBoolean("wound");
    }

    public static void setWound(ItemStack stack, boolean wound) {
        CompoundTag compoundTag = stack.getOrCreateTag();
        compoundTag.putBoolean("wound", wound);
    }

    private static float getLoadProgress(int useTicks, ItemStack stack) {
        float f = (float)useTicks / (float)getLoadTime(stack);
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    public static int getLoadTime(ItemStack stack) {
        int i = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
        return i == 0 ? 25 : 25 - 5 * i;
    }
}
