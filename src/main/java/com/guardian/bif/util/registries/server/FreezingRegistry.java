package com.guardian.bif.util.registries.server;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.guardian.bif.ByIronOrFire.*;

public class FreezingRegistry {
    protected static final float[] armorWarmthPercentage = new float[]{12.50F,25.00F, 50.00F, 12.50F};
    protected static final Map<Item, Float> MATERIAL_WARMTH_EFFECT = new HashMap<>();
    protected static final List<ArmorMaterial> materialFreezingList = new ArrayList<>();


    public static void init() {
        //these need to be rethought
        MATERIAL_WARMTH_EFFECT.put(Items.LEATHER, 0.0f);
        MATERIAL_WARMTH_EFFECT.put(Items.NETHERITE_INGOT, 145.0f);
        MATERIAL_WARMTH_EFFECT.put(Items.DIAMOND,150.0f);
        MATERIAL_WARMTH_EFFECT.put(Items.GOLD_INGOT, 180.0f);
        MATERIAL_WARMTH_EFFECT.put(Items.IRON_INGOT, 140.0f);

        LOG.info("Generating Armor Freezing Values");
        Registry.ITEM.forEach(item -> {
            if (item instanceof ArmorItem) {
                materialFreezingList.add(((ArmorItem) item).getMaterial());
            }
        });

        setFreezingValues(materialFreezingList);
        LOG.info("Generation Success");
    }


    public static void register(ArmorMaterial armorMaterial, int[] array) { ARMORFREEZINGABILITY.put(armorMaterial, array); }

    //TODO: Complete code for modular generation of Armor Freezing values
    private static int[] generateValues(ArmorMaterial material) {

        float[] visibilityMulitipliers;
        int[] values = new int[4];

        for (int i = 0; i < 4; i++) {

        }

        return values;
    }

    private static void setFreezingValues(List<ArmorMaterial> materialList) {
        for (ArmorMaterial currentMaterial :
                materialList) {
            if (!ARMORFREEZINGABILITY.containsKey(currentMaterial)) {
                register(currentMaterial, generateValues(currentMaterial));
            }
        }
    }


}
