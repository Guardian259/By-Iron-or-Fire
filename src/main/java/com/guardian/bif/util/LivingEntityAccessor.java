package com.guardian.bif.util;

public interface LivingEntityAccessor {

    int[] getEntityVisibility();

    void setEntityVisibility(int slotId,int value);

    boolean getIsDetected();

    void setIsDetected(boolean status);


}
