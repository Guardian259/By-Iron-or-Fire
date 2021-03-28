package com.guardian.bif.entity;

import com.guardian.bif.ByIronOrFire;
import com.guardian.bif.util.registries.EntityRegistry;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.networking.ServerSidePacketRegistryImpl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
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
    }

    @Environment(EnvType.CLIENT)
    public ExplosiveBombEntity(World world, double x, double y, double z, int id, UUID uuid) {
        super(EntityRegistry.SMALL_BOMB, world);
        updatePosition(x, y, z);
        updateTrackedPosition(x, y, z);
        setEntityId(id);
        setUuid(uuid);
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
