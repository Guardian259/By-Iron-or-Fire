package com.guardian.bif.util.registries.client;

import com.guardian.bif.ByIronOrFire;
import com.guardian.bif.entity.BombEntity;
import com.guardian.bif.util.registries.server.EntityRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.impl.networking.ClientSidePacketRegistryImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class ClientPacketsRegistry {

    /*Components for the Large Bomb are present but left out due to the absence of the Large Bomb Entity will be added in the future*/
    public static final Identifier SMALL_BOMB_PACKET = ByIronOrFire.id("spawn_small_bomb");
    //public static final Identifier LARGE_BOMB_PACKET = ByIronOrFire.id("spawn_large_bomb");

    private static void spawnExplosiveEntity(PacketContext context, PacketByteBuf packet, EntityType<BombEntity> entityType){
        ClientWorld world = MinecraftClient.getInstance().world;


        BombEntity entity = new BombEntity(entityType, world);

        double x = packet.readDouble();
        double y = packet.readDouble();
        double z = packet.readDouble();

        float thisPitch = packet.readFloat();
        float thisYaw = packet.readFloat();

        int ownerID = packet.readInt();
        int entityID = packet.readInt();
        UUID entityUUID = packet.readUuid();

        //Settting info
        Entity owner = world.getEntityById(ownerID);
        entity.setOwner(owner);

        entity.setEntityId(entityID);
        entity.setUuid(entityUUID);

        entity.updatePosition(x, y, z);

        entity.pitch = thisPitch;
        entity.yaw = thisYaw;

        context.getTaskQueue().execute(() -> MinecraftClient.getInstance().world.addEntity(entityID, entity));
    }

    public static void init() {
        ClientSidePacketRegistryImpl.INSTANCE.register(SMALL_BOMB_PACKET, ((context, packet) -> spawnExplosiveEntity(context, packet, EntityRegistry.SMALL_BOMB_ENTITY)));
        //ClientSidePacketRegistryImpl.INSTANCE.register(LARGE_BOMB_PACKET, ((context, packet) -> spawnExplosiveEntity(context, packet, EntityRegistry.LARGE_BOMB_ENTITY)));

    }

}
