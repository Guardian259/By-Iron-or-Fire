package com.guardian.bif.util.registries.server;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.guardian.bif.ByIronOrFire.*;

public class VisibilityRegistry {
    protected static final List<ArmorMaterial> materialList = new ArrayList<>();
    protected static final Map<String, float[]> VISIBILITY_MULTIPLIERS = new HashMap<>();

    //TODO: This Needs Testing, currently does not generate the correct values. my math is wrong somewhere
    private static int[] generateValues(ArmorMaterial material) {

        float[] visibilityMulitipliers;
        int[] values = new int[4];
        //returns the durabilityMultiplier by undoing the multiplication done by getDurability
        int durability = (material.getDurability(EquipmentSlot.fromTypeIndex(EquipmentSlot.Type.ARMOR, 1)) / 13);

        /*pulls the corresponding multipliers based off of the material durability.
        Its not perfect but should adequately approximate for undefined visibilities*/
        if (durability >= 33) {
            visibilityMulitipliers = VISIBILITY_MULTIPLIERS.get("visible");
        } else if(durability >= 15) {
            visibilityMulitipliers = VISIBILITY_MULTIPLIERS.get("normal");
        } else{
            visibilityMulitipliers = VISIBILITY_MULTIPLIERS.get("sneaky");
        }

        for (int i = 0; i < visibilityMulitipliers.length; i++) {
            int protectionAmount = material.getProtectionAmount(EquipmentSlot.fromTypeIndex(EquipmentSlot.Type.ARMOR, i));
            values[i] = (int)((visibilityMulitipliers[i]/100) * protectionAmount);
        }

        return values;
    }

    private static void setVisibilityValues(List<ArmorMaterial> materialList) {
        for (ArmorMaterial currentMaterial :
                materialList) {
            if (!ARMORVISIBILITY.containsKey(currentMaterial)) {
                register(currentMaterial, generateValues(currentMaterial));
            }
        }
    }

    public static void register(ArmorMaterial armorMaterial, int[] array) {
        ARMORVISIBILITY.put(armorMaterial, array);
    }

    /*Array Values for Armor Visibility
     *TODO: Abstract & Automate this for Modded Armors so they will function with By Iron or Fire
     *WARNING DO NOT LOAD WITH OTHER ARMOR MODS ACTIVE THEIR MATERIALS WILL CAUSE A HARD CRASH
     */
    public static void init() {
        VISIBILITY_MULTIPLIERS.put("visible", new float[]{17.86F, 32.00F, 38.00F, 12.50F});
        VISIBILITY_MULTIPLIERS.put("normal", new float[]{18.75F, 31.00F, 38.00F, 12.50F});
        VISIBILITY_MULTIPLIERS.put("sneaky", new float[]{12.50F,25.00F, 50.00F, 12.50F});

        LOG.info("Generating Armor Visibility Values");
        Registry.ITEM.forEach(item -> {
            if (item.asItem() instanceof ArmorItem) {
                materialList.add(((ArmorItem) item.asItem()).getMaterial());
            }
        });
        //Preset values for vanilla & core armors may be removed in favor of generating them
        ARMORVISIBILITY.put(ItemRegistry.CHAINED_NETHERITE, new int[]{24, 56, 48, 32});
        ARMORVISIBILITY.put(ArmorMaterials.NETHERITE, new int[]{20, 52, 44, 28});
        ARMORVISIBILITY.put(ItemRegistry.CHAINED_DIAMOND, new int[]{16, 48, 40, 24});
        ARMORVISIBILITY.put(ArmorMaterials.DIAMOND, new int[]{14, 42, 36, 20});
        ARMORVISIBILITY.put(ItemRegistry.CHAINED_IRON, new int[]{8, 24, 20, 12});
        ARMORVISIBILITY.put(ArmorMaterials.CHAIN, new int[]{8, 24, 20, 12});
        ARMORVISIBILITY.put(ArmorMaterials.IRON, new int[]{8, 24, 20, 12});
        ARMORVISIBILITY.put(ArmorMaterials.TURTLE, new int[]{8, 24, 20, 12});
        ARMORVISIBILITY.put(ItemRegistry.CHAINED_GOLD, new int[]{6, 17, 15, 10});
        ARMORVISIBILITY.put(ArmorMaterials.GOLD, new int[]{4, 12, 10, 6});
        ARMORVISIBILITY.put(ItemRegistry.CHAINED_LEATHER, new int[]{2, 6, 5, 3});
        ARMORVISIBILITY.put(ArmorMaterials.LEATHER, new int[]{1, 4, 2, 1});
        //will generate approximations based on all ArmorMaterials if no visibilityValues are present
        setVisibilityValues(materialList);

        LOG.info("Generation Success");
    }
}
