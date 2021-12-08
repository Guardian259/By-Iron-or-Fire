package com.guardian.bif.util;

public interface LivingEntityAccessor {

    int[] getEntityVisibility();

    void setEntityVisibility(int slotId,int value);

    boolean getIsDetected();

    void setIsDetected(boolean status);

    int getFrozenTickDamage();

    void setFrozenTickDamage(int damage);

    float getMinEntityHealth();

    void setMinEntityHealth(float health);

    float minEntityHealth();

    int frozenTickDamage();

    boolean inIcyWater();

    boolean inIcyBiome();

    boolean inSnowstorm();

    int icyWaterTick();

    int icyBiomeTick();

    int snowstormTick();

    int snowfallMultiplier();

}
