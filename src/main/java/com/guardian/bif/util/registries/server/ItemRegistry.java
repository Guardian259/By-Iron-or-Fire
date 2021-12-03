package com.guardian.bif.util.registries.server;

import com.guardian.bif.ByIronOrFire;
import com.guardian.bif.armor.BaseArmor;
import com.guardian.bif.armor.DyeableArmor;
import com.guardian.bif.armor.NetheriteArmor;
import com.guardian.bif.armor.material.*;
//import com.guardian.bif.item.HandMortarItem;
//import com.guardian.bif.item.SmallBombItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

public class ItemRegistry {

    //Materials
    public static final ArmorMaterial CHAINED_NETHERITE = new ChainedNetherite();
    public static final ArmorMaterial CHAINED_DIAMOND = new ChainedDiamond();
    public static final ArmorMaterial CHAINED_IRON = new ChainedIron();
    public static final ArmorMaterial CHAINED_GOLD = new ChainedGold();
    public static final ArmorMaterial CHAINED_LEATHER = new ChainedLeather();

    //Armorsets
    public static final Item CHAINED_NETHERITE_HELMET = register("chained_netherite_helmet", new NetheriteArmor(CHAINED_NETHERITE, EquipmentSlot.HEAD));
    public static final Item CHAINED_NETHERITE_CHESTPLATE = register("chained_netherite_chestplate", new NetheriteArmor(CHAINED_NETHERITE, EquipmentSlot.CHEST));
    public static final Item CHAINED_NETHERITE_LEGGINGS = register("chained_netherite_leggings", new NetheriteArmor(CHAINED_NETHERITE, EquipmentSlot.LEGS));
    public static final Item CHAINED_NETHERITE_BOOTS = register("chained_netherite_boots", new NetheriteArmor(CHAINED_NETHERITE, EquipmentSlot.FEET));
    public static final Item CHAINED_DIAMOND_HELMET = register("chained_diamond_helmet",  new BaseArmor(CHAINED_DIAMOND, EquipmentSlot.HEAD));
    public static final Item CHAINED_DIAMOND_CHESTPLATE = register("chained_diamond_chestplate", new BaseArmor(CHAINED_DIAMOND, EquipmentSlot.CHEST));
    public static final Item CHAINED_DIAMOND_LEGGINGS = register("chained_diamond_leggings", new BaseArmor(CHAINED_DIAMOND, EquipmentSlot.LEGS));
    public static final Item CHAINED_DIAMOND_BOOTS = register("chained_diamond_boots", new BaseArmor(CHAINED_DIAMOND, EquipmentSlot.FEET));
    public static final Item CHAINED_IRON_HELMET = register("chained_iron_helmet", new BaseArmor(CHAINED_IRON, EquipmentSlot.HEAD));
    public static final Item CHAINED_IRON_CHESTPLATE = register("chained_iron_chestplate", new BaseArmor(CHAINED_IRON, EquipmentSlot.CHEST));
    public static final Item CHAINED_IRON_LEGGINGS = register("chained_iron_leggings", new BaseArmor(CHAINED_IRON, EquipmentSlot.LEGS));
    public static final Item CHAINED_IRON_BOOTS = register("chained_iron_boots", new BaseArmor(CHAINED_IRON, EquipmentSlot.FEET));
    public static final Item CHAINED_GOLD_HELMET = register("chained_gold_helmet", new BaseArmor(CHAINED_GOLD, EquipmentSlot.HEAD));
    public static final Item CHAINED_GOLD_CHESTPLATE = register("chained_gold_chestplate", new BaseArmor(CHAINED_GOLD, EquipmentSlot.CHEST));
    public static final Item CHAINED_GOLD_LEGGINGS = register("chained_gold_leggings", new BaseArmor(CHAINED_GOLD, EquipmentSlot.LEGS));
    public static final Item CHAINED_GOLD_BOOTS  = register( "chained_gold_boots", new BaseArmor(CHAINED_GOLD, EquipmentSlot.FEET));
    public static final Item CHAINED_LEATHER_HELMET = register("chained_leather_helmet" , new DyeableArmor(CHAINED_LEATHER, EquipmentSlot.HEAD));
    public static final Item CHAINED_LEATHER_CHESTPLATE = register("chained_leather_chestplate", new DyeableArmor(CHAINED_LEATHER, EquipmentSlot.CHEST));
    public static final Item CHAINED_LEATHER_LEGGINGS =register("chained_leather_leggings", new DyeableArmor(CHAINED_LEATHER, EquipmentSlot.LEGS));
    public static final Item CHAINED_LEATHER_BOOTS = register("chained_leather_boots", new DyeableArmor(CHAINED_LEATHER, EquipmentSlot.FEET));

    //Items
    //public static final Item HAND_MORTAR = register("hand_mortar", new HandMortarItem(new FabricItemSettings().group(ItemGroup.COMBAT)));
    //ublic static final Item SMALL_BOMB = register("small_bomb", new SmallBombItem(new FabricItemSettings().group(ItemGroup.COMBAT)));


    public static <T extends Item> T register(String name, T item){
        return Registry.register(Registry.ITEM, ByIronOrFire.id(name), item);
    }

    public static void init(){

    }

}
