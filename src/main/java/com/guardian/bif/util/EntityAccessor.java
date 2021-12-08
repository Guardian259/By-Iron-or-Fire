package com.guardian.bif.util;

public interface EntityAccessor {

    int getFrozenTicks();

    void setFrozenTicks(int tick);

    int getMinFreezeDamageTicks();

    boolean isFreezing();

}
