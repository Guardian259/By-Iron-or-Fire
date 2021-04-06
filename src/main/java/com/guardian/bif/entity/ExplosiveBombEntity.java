package com.guardian.bif.entity;

import com.guardian.bif.ByIronOrFire;
import com.guardian.bif.util.registries.server.EntityRegistry;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.networking.ServerSidePacketRegistryImpl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.UUID;

public class ExplosiveBombEntity extends PersistentProjectileEntity {

    private final boolean isUnlit = false;
    private final boolean isPrimed = false;

    public static final Identifier SPAWN_PACKET = ByIronOrFire.id("small_bomb_packet");

    public ExplosiveBombEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.pickupType = PickupPermission.DISALLOWED;
    }

    @Environment(EnvType.CLIENT)
    public ExplosiveBombEntity(World world, double x, double y, double z, int id, UUID uuid) {
        super(EntityRegistry.SMALL_BOMB, world);
        updatePosition(x, y, z);
        updateTrackedPosition(x, y, z);
        setEntityId(id);
        setUuid(uuid);
    }

    public ExplosiveBombEntity(World world, LivingEntity owner) {
        super(EntityRegistry.SMALL_BOMB, owner, world);
        this.pickupType = PickupPermission.DISALLOWED;
    }


    @Override
    public void tick() {
        Vec3d vec3d = this.getVelocity();
        if (this.prevPitch == 0.0F && this.prevYaw == 0.0F) {
            float f = MathHelper.sqrt(squaredHorizontalLength(vec3d));
            this.yaw = (float)(MathHelper.atan2(vec3d.x, vec3d.z) * 57.2957763671875D);
            this.pitch = (float)(MathHelper.atan2(vec3d.y, (double)f) * 57.2957763671875D);
            this.prevYaw = this.yaw;
            this.prevPitch = this.pitch;
        }
        //fuse sound
        //world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_CREEPER_PRIMED, SoundCategory.NEUTRAL, 0.5F, 0);
        /*These two lines cause a Null Pointer Exception*/
        //HitResult result = ProjectileUtil.getCollision(this, (entity -> !entity.isSpectator() && entity.isAlive() && entity.collides()));
        //if(result.getType() == HitResult.Type.BLOCK) onBlockHit((BlockHitResult) result);

        double d = this.getX() + vec3d.x;
        double e = this.getY() + vec3d.y;
        double f = this.getZ() + vec3d.z;
        if (!this.inGround) {
            for(int i = 0; i < 4; ++i) {
                this.world.addParticle(ParticleTypes.FIREWORK, d - vec3d.x * 0.25D, e - vec3d.y * 0.25D, f - vec3d.z * 0.25D, vec3d.x, vec3d.y, vec3d.z);
                this.world.addParticle(ParticleTypes.POOF, d - vec3d.x * 2D, e - vec3d.y * 2D, f - vec3d.z * 2D, vec3d.x, vec3d.y, vec3d.z);
            }
        }

        this.setVelocity(vec3d.multiply((double)0.99F));
        if (!this.hasNoGravity()) {
            Vec3d vec3d2 = this.getVelocity();
            this.setVelocity(vec3d2.x, vec3d2.y - (double)this.getGravity(), vec3d2.z);
        }

        this.updatePosition(d, e, f);
        this.checkBlockCollision();
    }

    protected float getGravity() {
        return 0.03F;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        entity.damage(DamageSource.mobProjectile(this, (LivingEntity) this.getOwner()),3.0F);
    }


    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
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
