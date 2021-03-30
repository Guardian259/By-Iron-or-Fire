package com.guardian.bif.util.registries.server;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.registry.Registry;

import java.util.*;

import static com.guardian.bif.ByIronOrFire.*;

public class VisibilityRegistry {
    protected static final List<ArmorMaterial> materialList = new ArrayList<>();
    protected static final Map<String, float[]> VISIBILITY_MULTIPLIERS = new HashMap<>();

    //TODO: This Needs Testing, currently does not generate the correct values. my math is wrong somewhere
    /**Takes in a given ArmorMaterial and generates a corresponding array of visibility values based on the
     * ArmorMaterial's durabilityMultiplier, preset list of VISIBILITY_MULTIPLIERS, and the Armor Material's protectionAmounts
     * @param material The material used to generate visibility values
     */
    private static int[] generateValues(ArmorMaterial material) {

        float[] visibilityMulitipliers;
        int[] values = new int[4];
        //int[] protectionAmount = new int[4];
        //returns the durabilityMultiplier by undoing the multiplication done by getDurability
        int durability = (material.getDurability(EquipmentSlot.fromTypeIndex(EquipmentSlot.Type.ARMOR, 0)) / 13);

        /*pulls the corresponding multipliers based off of the material durability.
        Its not perfect but should adequately approximate for undefined visibilities*/
        if (durability >= 33) {
            visibilityMulitipliers = VISIBILITY_MULTIPLIERS.get("visible");
        } else if(durability >= 15) {
            visibilityMulitipliers = VISIBILITY_MULTIPLIERS.get("normal");
        } else{
            visibilityMulitipliers = VISIBILITY_MULTIPLIERS.get("sneaky");
        }

        /*Currently does not generate the appropriate estimations*/
        for (int i = 0; i < visibilityMulitipliers.length; i++) {
            int protectionAmount = material.getProtectionAmount(EquipmentSlot.fromTypeIndex(EquipmentSlot.Type.ARMOR, i));
            values[i] = (int)(((visibilityMulitipliers[i]/100) * protectionAmount) * durability);
        }

        return values;
    }

    /**Loops through all identified ArmorMaterials, at SERVER_STARTING and checks for any existing keys,
     * if no preexisting key is present a new ARMORVISIBILITY HashMap is registered & stored
     * @param materialList the list of ArmorMaterials identified at server startup
     * @see VisibilityRegistry#register(net.minecraft.item.ArmorMaterial, int[])
     * @see VisibilityRegistry#generateValues(net.minecraft.item.ArmorMaterial)
     */
    private static void setVisibilityValues(List<ArmorMaterial> materialList) {
        for (ArmorMaterial currentMaterial :
                materialList) {
            if (!ARMORVISIBILITY.containsKey(currentMaterial)) {
                register(currentMaterial, generateValues(currentMaterial));
            }
        }
    }

    /**Registers Armor Visibility Values in the ARMORVISIBILITY HashMap. This is used to determine
     * the players nameplate visibility, as well as, the players visibility to mobs
     * @param armorMaterial the armorMaterial to be associated with the visibility array
     * @param array the int[] array containing the four armor visibility values
     */
    public static void register(ArmorMaterial armorMaterial, int[] array) {
        ARMORVISIBILITY.put(armorMaterial, array);
    }

    /**Runs at ServerLifecycleEvents.SERVER_STARTING; first places preset VISIBILITY_MULTIPLIERS
     * then iterates through all loaded items, identifying instances of ArmorItem,
     * and storing all ArmorMaterials in an ArrayList; Will then run setVisibilityValues for said ArrayList to determine
     * whether or not to generate approximations
     * @see VisibilityRegistry#setVisibilityValues(java.util.List)
     */
    public static void init() {
        //Multipliers used in array gen of generateValues
        VISIBILITY_MULTIPLIERS.put("visible", new float[]{17.86F, 32.00F, 38.00F, 12.50F});
        VISIBILITY_MULTIPLIERS.put("normal", new float[]{18.75F, 31.00F, 38.00F, 12.50F});
        VISIBILITY_MULTIPLIERS.put("sneaky", new float[]{12.50F,25.00F, 50.00F, 12.50F});

        LOG.info("Generating Armor Visibility Values");
        Registry.ITEM.forEach(item -> {
            if (item instanceof ArmorItem) {
                materialList.add(((ArmorItem) item).getMaterial());
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
