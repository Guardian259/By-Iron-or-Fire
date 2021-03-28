package com.guardian.bif.util.registries.server;

import net.minecraft.item.*;

import java.util.HashMap;
import java.util.Map;

import static com.guardian.bif.ByIronOrFire.*;

public class VisibilityRegistry {

    protected static final Map<String, int[]> VISIBILITY_MULTIPLIERS = new HashMap<>();

/*    private void getArmorItems(Items items){
        for (Item item:
                Items) {
            if(item.asItem() instanceof ArmorItem){
                ((ArmorItem) item.asItem()).getMaterial();
            }
        }
    }*/

    private static int[] register(ArmorMaterial armorMaterial, int[] array){
        return ARMORVISIBILITY.put(armorMaterial, array);
    }

    /*Array Values for Armor Visibility
     *TODO: Abstract & Automate this for Modded Armors so they will function with By Iron or Fire
     *WARNING DO NOT LOAD WITH OTHER ARMOR MODS ACTIVE THEIR MATERIALS WILL CAUSE A HARD CRASH
     */
    public static void init(){
        LOG.info("Generating Armor Visibility Values");
        ARMORVISIBILITY.put(ItemRegistry.CHAINED_NETHERITE, new int[]{24,56,48,32});
        ARMORVISIBILITY.put(ArmorMaterials.NETHERITE, new int[]{20,52,44,28});
        ARMORVISIBILITY.put(ItemRegistry.CHAINED_DIAMOND, new int[]{16,48,40,24});
        ARMORVISIBILITY.put(ArmorMaterials.DIAMOND, new int[]{14,42,36,20});
        ARMORVISIBILITY.put(ItemRegistry.CHAINED_IRON, new int[]{8,24,20,12});
        ARMORVISIBILITY.put(ArmorMaterials.CHAIN, new int[]{8,24,20,12});
        ARMORVISIBILITY.put(ArmorMaterials.IRON, new int[]{8,24,20,12});
        ARMORVISIBILITY.put(ArmorMaterials.TURTLE, new int[]{8,24,20,12});
        ARMORVISIBILITY.put(ItemRegistry.CHAINED_GOLD, new int[]{6,17,15,10});
        ARMORVISIBILITY.put(ArmorMaterials.GOLD, new int[]{4,12,10,6});
        ARMORVISIBILITY.put(ItemRegistry.CHAINED_LEATHER, new int[]{2,6,5,3});
        ARMORVISIBILITY.put(ArmorMaterials.LEATHER, new int[]{1,4,2,1});
        LOG.info("Generation Success");
    }
}
