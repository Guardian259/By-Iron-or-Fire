package com.guardian.bif.util.registries.server;

import com.guardian.bif.ByIronOrFire;
import com.guardian.bif.entity.ExplosiveBombEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;

public class EntityRegistry {

    public static final EntityType<ExplosiveBombEntity> SMALL_BOMB = register(
            "small_bomb",
            FabricEntityTypeBuilder.<ExplosiveBombEntity>create(SpawnGroup.MISC, ExplosiveBombEntity::new).dimensions(EntityDimensions.fixed(.25f,.25f)).build());

    private static <T extends Entity> EntityType<T> register(String name, EntityType<T> entity){
        return Registry.register(Registry.ENTITY_TYPE, ByIronOrFire.id(name), entity);
    }

    public static void init(){

    }

}
