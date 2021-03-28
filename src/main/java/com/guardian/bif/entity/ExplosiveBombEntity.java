package com.guardian.bif.entity;

import com.google.common.collect.Sets;
import com.guardian.bif.ByIronOrFire;
import com.guardian.bif.util.registries.server.EntityRegistry;
import com.guardian.bif.util.registries.server.ItemRegistry;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.networking.ServerSidePacketRegistryImpl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import static net.minecraft.entity.projectile.ArrowEntity.getCustomPotionColor;

public class ExplosiveBombEntity extends PersistentProjectileEntity {

    private static TrackedData<Integer> COLOR;
    private final boolean isUnlit = false;
    private final boolean isPrimed = false;
    private Potion potion;
    private final Set<StatusEffectInstance> effects;
    private boolean colorSet;

    public static final Identifier SPAWN_PACKET = ByIronOrFire.id("small_bomb_packet");

    public ExplosiveBombEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.potion = Potions.EMPTY;
        this.effects = Sets.newHashSet();
    }

    @Environment(EnvType.CLIENT)
    public ExplosiveBombEntity(World world, double x, double y, double z, int id, UUID uuid) {
        super(EntityRegistry.SMALL_BOMB, world);
        updatePosition(x, y, z);
        updateTrackedPosition(x, y, z);
        setEntityId(id);
        setUuid(uuid);
        this.potion = Potions.EMPTY;
        this.effects = Sets.newHashSet();
    }

    public ExplosiveBombEntity(World world, LivingEntity owner) {
        super(EntityType.ARROW, owner, world);
        this.potion = Potions.EMPTY;
        this.effects = Sets.newHashSet();
    }

    /*This doesn't get called*/
    @Override
    public void tick() {
        ByIronOrFire.LOG.info(this.getX() + " "  + this.getY() + " " + this.getZ());
/*        super.tick();
        boolean bl = this.isNoClip();
        Vec3d vec3d = this.getVelocity();
        if (this.prevPitch == 0.0F && this.prevYaw == 0.0F) {
            float f = MathHelper.sqrt(squaredHorizontalLength(vec3d));
            this.yaw = (float)(MathHelper.atan2(vec3d.x, vec3d.z) * 57.2957763671875D);
            this.pitch = (float)(MathHelper.atan2(vec3d.y, (double)f) * 57.2957763671875D);
            this.prevYaw = this.yaw;
            this.prevPitch = this.pitch;
        }
        //fuse sound
        world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_CREEPER_PRIMED, SoundCategory.NEUTRAL, 0.5F, 0);

        vec3d = this.getVelocity();
        double d = vec3d.x;
        double e = vec3d.y;
        double g = vec3d.z;
        while (!this.inGround) {
            for(int i = 0; i < 4; ++i) {
                this.world.addParticle(ParticleTypes.FIREWORK, this.getX() + d * (double)i / 4.0D, this.getY() + e * (double)i / 4.0D, this.getZ() + g * (double)i / 4.0D, -d, -e + 0.2D, -g);
            }
        }*/
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        entity.damage(DamageSource.thrownProjectile(this, this.getOwner()),3.0F);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        Vec3d blockPos = blockHitResult.getPos().subtract(this.getX(), this.getY(), this.getZ());
        this.setVelocity(blockPos);
        Vec3d vec3d = blockPos.normalize().multiply(0.05000000074505806D);
        this.setPos(this.getX() - vec3d.x, this.getY() - vec3d.y, this.getZ() - vec3d.z);
        this.inGround = true;
        this.setCritical(false);
    }

    @Override
    protected ItemStack asItemStack() {
        return null;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if(!this.world.isClient){

        }
    }

    public void initFromStack(ItemStack stack) {
        if (stack.getItem() == Items.TIPPED_ARROW) {
            this.potion = PotionUtil.getPotion(stack);
            Collection<StatusEffectInstance> collection = PotionUtil.getCustomPotionEffects(stack);
            if (!collection.isEmpty()) {
                Iterator var3 = collection.iterator();

                while(var3.hasNext()) {
                    StatusEffectInstance statusEffectInstance = (StatusEffectInstance)var3.next();
                    this.effects.add(new StatusEffectInstance(statusEffectInstance));
                }
            }

            int i = getCustomPotionColor(stack);
            if (i == -1) {
                this.initColor();
            } else {
                this.setColor(i);
            }
        } else if (stack.getItem() == ItemRegistry.MORTAR_BOMB) {
            this.potion = Potions.EMPTY;
            this.effects.clear();
            this.dataTracker.set(COLOR, -1);
        }

    }

    private void initColor() {
        this.colorSet = false;
        if (this.potion == Potions.EMPTY && this.effects.isEmpty()) {
            this.dataTracker.set(COLOR, -1);
        } else {
            this.dataTracker.set(COLOR, PotionUtil.getColor((Collection)PotionUtil.getPotionEffects(this.potion, this.effects)));
        }

    }

    public int getColor() {
        return (Integer)this.dataTracker.get(COLOR);
    }

    private void setColor(int color) {
        this.colorSet = true;
        this.dataTracker.set(COLOR, color);
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    public Packet<?> createSpawnPacket() {
        PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());

        //entity pos
        packet.writeDouble(getX());
        packet.writeDouble(getY());
        packet.writeDouble(getZ());

        //entity id
        packet.writeInt(getEntityId());
        packet.writeUuid(getUuid());

        return ServerSidePacketRegistryImpl.INSTANCE.toPacket(SPAWN_PACKET, packet);
    }
}
