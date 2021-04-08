package com.guardian.bif.util.registries.server;

import com.guardian.bif.ByIronOrFire;
import com.guardian.bif.entity.BombEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;

public class EntityRegistry {

    public static final EntityType<BombEntity> SMALL_BOMB_ENTITY = registerExplosive("small_bomb", EntityDimensions.fixed(.25f,.25f));
    //public static final EntityType<BombEntity> LARGE_BOMB_ENTITY = registerExplosive("large_bomb", EntityDimensions.fixed(.25f,.25f));

    private static <T extends Entity> EntityType<T> register(String name, EntityType<T> entity) {
        return Registry.register(Registry.ENTITY_TYPE, ByIronOrFire.id(name), entity);
    }

    private static EntityType<BombEntity> registerExplosive(String name, EntityDimensions dimensions) {
        return register(name, FabricEntityTypeBuilder.<BombEntity>create(SpawnGroup.MISC, BombEntity::new)
                .dimensions(dimensions)
                .build());
    }

    public static void init() {

    }

}
