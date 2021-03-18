package com.guardian.bif;

import com.guardian.bif.armor.*;
import com.guardian.bif.armor.material.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;


public class ByIronOrFire implements ModInitializer {

	public static final String MODID = "bif";

	public static final ArmorMaterial CHAINED_NETHERITE = new ChainedNetherite();
	public static final ArmorMaterial CHAINED_DIAMOND = new ChainedDiamond();
	public static final ArmorMaterial CHAINED_IRON = new ChainedIron();
	public static final ArmorMaterial CHAINED_GOLD = new ChainedGold();
	public static final ArmorMaterial CHAINED_LEATHER = new ChainedLeather();
	public static final Map <ArmorMaterial, int[]> ARMORVISIBILITY = new HashMap<>();


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		System.out.println("By Iron or Fire Initializing");

		Registry.register(Registry.ITEM, new Identifier(MODID, "chained_netherite_helmet"), new NetheriteArmor(CHAINED_NETHERITE, EquipmentSlot.HEAD));
		Registry.register(Registry.ITEM, new Identifier(MODID, "chained_netherite_chestplate"), new NetheriteArmor(CHAINED_NETHERITE, EquipmentSlot.CHEST));
		Registry.register(Registry.ITEM, new Identifier(MODID, "chained_netherite_leggings"), new NetheriteArmor(CHAINED_NETHERITE, EquipmentSlot.LEGS));
		Registry.register(Registry.ITEM, new Identifier(MODID, "chained_netherite_boots"), new NetheriteArmor(CHAINED_NETHERITE, EquipmentSlot.FEET));

		Registry.register(Registry.ITEM, new Identifier(MODID, "chained_diamond_helmet"),  new BaseArmor(CHAINED_DIAMOND, EquipmentSlot.HEAD));
		Registry.register(Registry.ITEM, new Identifier(MODID, "chained_diamond_chestplate"), new BaseArmor(CHAINED_DIAMOND, EquipmentSlot.CHEST));
		Registry.register(Registry.ITEM, new Identifier(MODID, "chained_diamond_leggings"), new BaseArmor(CHAINED_DIAMOND, EquipmentSlot.LEGS));
		Registry.register(Registry.ITEM, new Identifier(MODID, "chained_diamond_boots"), new BaseArmor(CHAINED_DIAMOND, EquipmentSlot.FEET));

		Registry.register(Registry.ITEM, new Identifier(MODID, "chained_iron_helmet"), new BaseArmor(CHAINED_IRON, EquipmentSlot.HEAD));
		Registry.register(Registry.ITEM, new Identifier(MODID, "chained_iron_chestplate"), new BaseArmor(CHAINED_IRON, EquipmentSlot.CHEST));
		Registry.register(Registry.ITEM, new Identifier(MODID, "chained_iron_leggings"), new BaseArmor(CHAINED_IRON, EquipmentSlot.LEGS));
		Registry.register(Registry.ITEM, new Identifier(MODID, "chained_iron_boots"), new BaseArmor(CHAINED_IRON, EquipmentSlot.FEET));

		Registry.register(Registry.ITEM, new Identifier(MODID, "chained_gold_helmet"), new BaseArmor(CHAINED_GOLD, EquipmentSlot.HEAD));
		Registry.register(Registry.ITEM, new Identifier(MODID, "chained_gold_chestplate"), new BaseArmor(CHAINED_GOLD, EquipmentSlot.CHEST));
		Registry.register(Registry.ITEM, new Identifier(MODID, "chained_gold_leggings"), new BaseArmor(CHAINED_GOLD, EquipmentSlot.LEGS));
		Registry.register(Registry.ITEM, new Identifier(MODID, "chained_gold_boots"), new BaseArmor(CHAINED_GOLD, EquipmentSlot.FEET));

		Registry.register(Registry.ITEM, new Identifier(MODID, "chained_leather_helmet"), Items.CHAINED_LEATHER_HELMET);
		Registry.register(Registry.ITEM, new Identifier(MODID, "chained_leather_chestplate"), Items.CHAINED_LEATHER_CHESTPLATE);
		Registry.register(Registry.ITEM, new Identifier(MODID, "chained_leather_leggings"), Items.CHAINED_LEATHER_LEGGINGS);
		Registry.register(Registry.ITEM, new Identifier(MODID, "chained_leather_boots"), Items.CHAINED_LEATHER_BOOTS);

		Registry.register(Registry.ITEM, new Identifier(MODID, "elytra_chained_leather_chestplate"), Items.CHAINED_ELYTRA_LEATHER_CHESTPATE);
		Registry.register(Registry.ITEM, new Identifier(MODID, "elytra_leather_chestplate"), Items.ELYTRA_LEATHER_CHESTPATE);

		/*Array Values for Armor Visibility
		*TODO: Abstract & Automate this for Modded Armors so they will function with By Iron or Fire
		*WARNING DO NOT LOAD WITH OTHER ARMOR MODS ACTIVE THEIR MATERIALS WILL CAUSE A HARD CRASH
		*/
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


		System.out.println("By Iron or Fire Initializing Complete, Success!");
	}

	public static class Items {

		public static final Item CHAINED_LEATHER_HELMET = new DyeableArmor(CHAINED_LEATHER, EquipmentSlot.HEAD);
		public static final Item CHAINED_LEATHER_CHESTPLATE = new DyeableArmor(CHAINED_LEATHER, EquipmentSlot.CHEST);
		public static final Item CHAINED_LEATHER_LEGGINGS = new DyeableArmor(CHAINED_LEATHER, EquipmentSlot.LEGS);
		public static final Item CHAINED_LEATHER_BOOTS = new DyeableArmor(CHAINED_LEATHER, EquipmentSlot.FEET);

		public static final Item CHAINED_ELYTRA_LEATHER_CHESTPATE = new ElytraLeatherArmor(CHAINED_LEATHER, EquipmentSlot.CHEST);

		public static final Item ELYTRA_LEATHER_CHESTPATE = new ElytraLeatherArmor(ArmorMaterials.LEATHER, EquipmentSlot.CHEST);
	}


}
