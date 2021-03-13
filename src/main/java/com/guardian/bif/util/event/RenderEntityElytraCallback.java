package com.guardian.bif.util.event;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.decoration.ArmorStandEntity;
import top.theillusivec4.caelus.api.RenderElytraInfo;


public interface RenderEntityElytraCallback {

    //copied from CaelusAPI in an attempt to render elytras on armorstands. currently unsuccessful
    Event<RenderEntityElytraCallback> EVENT = EventFactory
            .createArrayBacked(RenderEntityElytraCallback.class, (listeners) -> (armorStandEntity, info) -> {

                for (RenderEntityElytraCallback listener : listeners) {
                    listener.process(armorStandEntity, info);
                }
            });

    void process(ArmorStandEntity armorStandEntity, RenderElytraInfo info);
}
