package com.guardian.bif.entity;

import com.guardian.bif.ByIronOrFire;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.networking.ServerSidePacketRegistryImpl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.UUID;


public class BombEntity extends Entity {

    protected static final TrackedData<Boolean> FUSE_PRIMED = DataTracker.registerData(BombEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Integer> BOUNCE_COUNT = DataTracker.registerData(BombEntity.class, TrackedDataHandlerRegistry.INTEGER);
    protected static final TrackedData<Integer> DETONATION_AGE = DataTracker.registerData(BombEntity.class, TrackedDataHandlerRegistry.INTEGER);
    protected static final TrackedData<Byte> PIERCE_LEVEL = DataTracker.registerData(BombEntity.class, TrackedDataHandlerRegistry.BYTE);

    private final int bounceLimit;
    private final Identifier spawnPacketId;
    protected UUID ownerUUID;
    protected int ownerId;

    public int armingAge;
    public int shake;
    protected int PiercingLevel = 0;

    public BombEntity(EntityType<? extends BombEntity> type, World world, int bounceLimit) {
        super(type, world);

        String entityName = Registry.ENTITY_TYPE.getId(type).getPath();
        this.spawnPacketId = ByIronOrFire.id("spawn_" + entityName);
        ByIronOrFire.LOG.info(spawnPacketId.toString());
        this.bounceLimit = bounceLimit;
        //how long before the fuse has primed, bounces will cease past this point
        this.armingAge = 24;
        //how long till the bomb detonates due to age
        dataTracker.set(DETONATION_AGE, 128);
    }

    public BombEntity(EntityType<? extends BombEntity> type, World world) {
        this(type, world, 2);
    }

    //Defines the distance at which the Client should stop rendering the Entity. Pull directly from 'PersistentProjectileEntity' to ensure parity with vanilla mechanics
    @Environment(EnvType.CLIENT)
    public boolean shouldRender(double distance) {
        double d = this.getBoundingBox().getAverageSideLength() * 10.0D;
        if (Double.isNaN(d)) {
            d = 1.0D;
        }

        d *= 64.0D * getRenderDistanceMultiplier();
        return distance < d * d;
    }


    @Override
    public void tick() {
        boolean fuse_primed = this.dataTracker.get(FUSE_PRIMED);

        //auto-detonation of bomb entities past a certain age. still not satisfied with this
        if (fuse_primed && (this.age == this.dataTracker.get(DETONATION_AGE))) {
            detonate();
            return;
        }

        //disables entity bounce past a certain 'duration', once fuse is 'primed' the entity will embed itself within the next block it touches, or explode on entity collision
        if (this.age > (this.age % armingAge)) {
            this.dataTracker.set(FUSE_PRIMED, true);
        }

        //returns executes the appropriate hit method should a collision have been detected
        HitResult hitResult = ProjectileUtil.getCollision(this, (entity) -> !entity.isSpectator() && entity.isAlive() && entity.collides());
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            onBlockHit((BlockHitResult) hitResult);
        } else if (hitResult.getType() == HitResult.Type.ENTITY) {
            onEntityHit((EntityHitResult) hitResult);
        }
        //loading the velocity defined by the 'HandMortarItem' at entity spawn
        Vec3d velocity = this.getVelocity();
        this.setVelocity(this.getVelocity().subtract(0.0, 0.04 /*0.06*/, 0.0));

        Vec3d newPos = this.getPos().add(velocity);
        this.updatePositionAndAngles(newPos.x, newPos.y, newPos.z, yaw, pitch);
        //this is needed in 1.16.*, however this doesn't appear needed in 1.17.*-SNAPSHOTS
        this.updateTrackedPosition(newPos.x, newPos.y, newPos.z);
        this.checkBlockCollision();

        //simulates atmospheric drag
        this.setVelocity(this.getVelocity().multiply(0.99));


        //these are only for the particles
        double d = this.getX() + velocity.x;
        double e = this.getY() + velocity.y;
        double f = this.getZ() + velocity.z;


        //particle trail
        if(!fuse_primed){
            this.world.addParticle(ParticleTypes.FIREWORK, d - velocity.x * 0.25D, e - velocity.y * 0.25D, f - velocity.z * 0.25D, velocity.x, velocity.y, velocity.z);
        }else {
            this.world.addParticle(ParticleTypes.POOF, d - velocity.x * 2D, e - velocity.y * 2D, f - velocity.z * 2D, velocity.x, velocity.y, velocity.z);
        }

        super.tick();
    }

    //custom setVelocity, mimicking 'PersistentProjectileEntity', allowing for both speed and divergence to be defined. set within 'HandMortarItem'
    public void setVelocity(double x, double y, double z, float speed, float divergence) {
        super.setVelocityClient(x, y, z);
        Vec3d vec3d = (new Vec3d(x, y, z)).normalize().add(this.random.nextGaussian() * 0.007499999832361937D * (double) divergence, this.random.nextGaussian() * 0.007499999832361937D * (double) divergence, this.random.nextGaussian() * 0.007499999832361937D * (double) divergence).multiply((double) speed);
        this.setVelocity(vec3d);
        float f = MathHelper.sqrt(squaredHorizontalLength(vec3d));
        this.yaw = (float) (MathHelper.atan2(vec3d.x, vec3d.z) * 57.2957763671875D);
        this.pitch = (float) (MathHelper.atan2(vec3d.y, (double) f) * 57.2957763671875D);
        this.prevYaw = this.yaw;
        this.prevPitch = this.pitch;
    }

    //Called when this Entity collides with a Block
    protected void onBlockHit(BlockHitResult result) {
        //Will return the entity to the outside of a block it's collided with if it is within a block and can no longer pierce any more
        if (result.isInsideBlock() && !this.submergedInWater && (this.dataTracker.get(PIERCE_LEVEL) == 0)) {
            this.pushOutOfBlocks(this.getX(), this.getY(), this.getZ());
            return;
        }

        int bounceCount = this.dataTracker.get(BOUNCE_COUNT) + 1;
        if (!this.dataTracker.get(FUSE_PRIMED) && (bounceCount <= bounceLimit)) {
            this.dataTracker.set(BOUNCE_COUNT, bounceCount);
        }

        if ((bounceCount >= bounceLimit) || this.dataTracker.get(FUSE_PRIMED)) {
            Vec3d blockPos = result.getPos().subtract(this.getX(), this.getY(), this.getZ());
            this.setVelocity(blockPos);
            Vec3d vec3d = blockPos.normalize().multiply(0.05000000074505806D);
            this.setPos(this.getX() - vec3d.x, this.getY() - vec3d.y, this.getZ() - vec3d.z);
            //used by ProjectileEntityRenderer not currently used by bomb entity
            this.shake = 7;
        }

        if (bounceCount < bounceLimit) {
            Vec3d velocity = this.getVelocity();
            Vec3d surfaceNormal = Vec3d.of(result.getSide().getVector());
            double deflectDot = surfaceNormal.dotProduct(velocity) * 2;

            Vec3d projection = new Vec3d(
                    surfaceNormal.x * deflectDot,
                    surfaceNormal.y * deflectDot,
                    surfaceNormal.z * deflectDot);

            double multiplier = this.isTouchingWater() ? 0.5 : 0.7;
            Vec3d newVelocity = velocity.subtract(projection).multiply(multiplier);
            this.setVelocity(newVelocity);
        }
    }

    //Called when this Entity Collides with another Entity
    protected void onEntityHit(EntityHitResult result) {
        if (dataTracker.get(FUSE_PRIMED)) {
            detonate();
        } else {
            Entity entity = result.getEntity();
            entity.damage(DamageSource.thrownProjectile(this, this.getOwner()), 3.0F);
        }
    }


    //Called to Explode and Remove Entity
    protected void detonate() {
        world.playSound(
                null,
                this.getX(),
                this.getY(),
                this.getZ(),
                SoundEvents.ENTITY_GENERIC_EXPLODE,
                SoundCategory.NEUTRAL,
                1.5F,
                0 / (random.nextFloat() * 0.4f + 0.8f));
        this.world.sendEntityStatus(this, (byte) 3);
        this.destroy();
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(FUSE_PRIMED, false);
        this.dataTracker.startTracking(BOUNCE_COUNT, 0);
        this.dataTracker.startTracking(DETONATION_AGE, -1);
        this.dataTracker.startTracking(PIERCE_LEVEL, (byte)0);
    }

    //defines and stores owning entity, should always be the player, used when calculating damage dealt and death messages
    public void setOwner(Entity owner) {
        if (owner != null) {
            ownerId = owner.getEntityId();
            ownerUUID = owner.getUuid();
        }
    }

    //Returns the stored owner of this entity
    public Entity getOwner() {
        if (ownerUUID != null && this.world instanceof ServerWorld) {
            return ((ServerWorld) this.world).getEntity(ownerUUID);
        } else {
            return ownerId != 0 ? this.world.getEntityById(ownerId) : null;
        }
    }

    @Override
    protected void readCustomDataFromTag(CompoundTag tag) {

    }

    @Override
    protected void writeCustomDataToTag(CompoundTag tag) {

    }

    //Creates the Server-Sided Packet used in entity creation, this information can then be read by the Client and is defined within the 'ClientPacketsRegistry'
    @Override
    public Packet<?> createSpawnPacket() {
        PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());

        //entity pos
        packet.writeDouble(this.getX());
        packet.writeDouble(this.getY());
        packet.writeDouble(this.getZ());

        //entity pitch & yaw
        packet.writeFloat(this.pitch);
        packet.writeFloat(this.yaw);

        //owner id
        packet.writeInt(ownerId);

        //entity id
        packet.writeInt(this.getEntityId());
        packet.writeUuid(this.getUuid());

        return ServerSidePacketRegistryImpl.INSTANCE.toPacket(spawnPacketId, packet);
    }
}
