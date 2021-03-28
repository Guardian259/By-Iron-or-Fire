package com.guardian.bif.util.registries;

import com.guardian.bif.entity.ExplosiveBombEntity;
import net.fabricmc.fabric.impl.networking.ClientSidePacketRegistryImpl;
import net.minecraft.client.MinecraftClient;

import java.util.UUID;

public class ClientPacketsRegistry {

    public static void init() {
        ClientSidePacketRegistryImpl.INSTANCE.register(ExplosiveBombEntity.SPAWN_PACKET, ((context, packet) -> {
            double x = packet.readDouble();
            double y = packet.readDouble();
            double z = packet.readDouble();

            int entityID = packet.readInt();
            UUID entityUUID = packet.readUuid();

            context.getTaskQueue().execute(() -> {
                ExplosiveBombEntity bombEntity = new ExplosiveBombEntity(MinecraftClient.getInstance().world, x, y, z, entityID, entityUUID);
                MinecraftClient.getInstance().world.addEntity(entityID, bombEntity);
            });
        }));
    }

}
