package com.guardian.bif.util.registries;

import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Items;

import java.util.HashMap;
import java.util.Map;

import static com.guardian.bif.ByIronOrFire.*;

public class VisibilityRegistry {

    protected static final Map<String, int[]> VISIBILITY_MULTIPLIERS = new HashMap<>();

    private void getArmorItems(Items items){

    }

    private static int[] register(ArmorMaterial armorMaterial, int[] array){
        return ARMORVISIBILITY.put(armorMaterial, array);
    }

    /*Array Values for Armor Visibility
     *TODO: Abstract & Automate this for Modded Armors so they will function with By Iron or Fire
     *WARNING DO NOT LOAD WITH OTHER ARMOR MODS ACTIVE THEIR MATERIALS WILL CAUSE A HARD CRASH
     */
    public static void init(){
        ARMORVISIBILITY.put(CHAINED_NETHERITE, new int[]{24,56,48,32});
        ARMORVISIBILITY.put(ArmorMaterials.NETHERITE, new int[]{20,52,44,28});
        ARMORVISIBILITY.put(CHAINED_DIAMOND, new int[]{16,48,40,24});
        ARMORVISIBILITY.put(ArmorMaterials.DIAMOND, new int[]{14,42,36,20});
        ARMORVISIBILITY.put(CHAINED_IRON, new int[]{8,24,20,12});
        ARMORVISIBILITY.put(ArmorMaterials.CHAIN, new int[]{8,24,20,12});
        ARMORVISIBILITY.put(ArmorMaterials.IRON, new int[]{8,24,20,12});
        ARMORVISIBILITY.put(ArmorMaterials.TURTLE, new int[]{8,24,20,12});
        ARMORVISIBILITY.put(CHAINED_GOLD, new int[]{6,17,15,10});
        ARMORVISIBILITY.put(ArmorMaterials.GOLD, new int[]{4,12,10,6});
        ARMORVISIBILITY.put(CHAINED_LEATHER, new int[]{2,6,5,3});
        ARMORVISIBILITY.put(ArmorMaterials.LEATHER, new int[]{1,4,2,1});
    }
}
