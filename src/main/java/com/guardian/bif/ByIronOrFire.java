package com.guardian.bif;

import com.guardian.bif.armor.BaseArmor;
import com.guardian.bif.armor.DyeableArmor;
import com.guardian.bif.armor.NetheriteArmor;
import com.guardian.bif.armor.material.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class ByIronOrFire implements ModInitializer {

	public static final String MODID = "bif";

	public static final ArmorMaterial CHAINED_NETHERITE = new ChainedNetherite();
	public static final ArmorMaterial CHAINED_DIAMOND = new ChainedDiamond();
	public static final ArmorMaterial CHAINED_IRON = new ChainedIron();
	public static final ArmorMaterial CHAINED_GOLD = new ChainedGold();
	public static final ArmorMaterial CHAINED_LEATHER = new ChainedLeather();


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		System.out.println("Initializing Items");

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

		System.out.println("ITem Initializing Complete, Success!");
	}

	public static class Items {

		public static final Item CHAINED_LEATHER_HELMET = new DyeableArmor(CHAINED_LEATHER, EquipmentSlot.HEAD);
		public static final Item CHAINED_LEATHER_CHESTPLATE = new DyeableArmor(CHAINED_LEATHER, EquipmentSlot.CHEST);
		public static final Item CHAINED_LEATHER_LEGGINGS = new DyeableArmor(CHAINED_LEATHER, EquipmentSlot.LEGS);
		public static final Item CHAINED_LEATHER_BOOTS = new DyeableArmor(CHAINED_LEATHER, EquipmentSlot.FEET);
	}


}
