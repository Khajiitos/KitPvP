package me.Khajiitos.KitPvP;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import net.md_5.bungee.api.ChatColor;

public class Kits {
	
	private static Plugin plugin = KitPvP.getPlugin(KitPvP.class);
	
	public static class defaultitems {
		public static ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
		
		public static ItemStack bow = new ItemStack(Material.BOW, 1);
		
		public static ItemStack arrows = new ItemStack(Material.ARROW, 64);
		
		public static ItemStack steak = new ItemStack(Material.COOKED_BEEF, 32);
		public static ItemStack pork = new ItemStack(Material.GRILLED_PORK, 32);
		public static ItemStack carrot = new ItemStack(Material.GOLDEN_CARROT, 32);
		
		public static ItemStack helmet = new ItemStack(Material.IRON_HELMET);
		
		public static ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
		
		public static ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
		
		public static ItemStack boots = new ItemStack(Material.IRON_BOOTS);
	}
	
	public static void giveKitDefault(Player player) {
		
		  ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
		
		  ItemStack bow = new ItemStack(Material.BOW, 1);
		
		  ItemStack arrows = new ItemStack(Material.ARROW, 64);
		
		  ItemStack steak = new ItemStack(Material.COOKED_BEEF, 32);
		  ItemStack pork = new ItemStack(Material.GRILLED_PORK, 32);
		  ItemStack carrot = new ItemStack(Material.GOLDEN_CARROT, 32);
		
		  ItemStack helmet = new ItemStack(Material.IRON_HELMET);
		
		 ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
		
		 ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
		
		 ItemStack boots = new ItemStack(Material.IRON_BOOTS);
		
		
		PlayerInventory inv = player.getInventory();
		inv.addItem(sword, bow, arrows);
		
		switch (plugin.getConfig().getInt("Players." + "preferences." + player.getUniqueId() + ".food")) {
		case 0: inv.addItem(steak); break;
		case 1: inv.addItem(pork); break;
		case 2: inv.addItem(carrot); break;
		default: inv.addItem(steak); break;
		}
		
		if (inv.getHelmet() == null) {
			inv.setHelmet(helmet);
		}
		else {
			inv.addItem(helmet);
		}
		
		if (inv.getChestplate() == null) {
			inv.setChestplate(chestplate);
		}
		else {
			inv.addItem(chestplate);
		}
		
		if (inv.getLeggings() == null) {
			inv.setLeggings(leggings);
		}
		else {
			inv.addItem(leggings);
		}
		
		if (inv.getBoots() == null) {
			inv.setBoots(boots);
		}
		else {
			inv.addItem(boots);
		}
		
		player.sendMessage(ChatColor.GREEN + "Received kit " + ChatColor.AQUA + "default" + ChatColor.GREEN + ".");
		
	}
	
	public static void giveKitArcher(Player player) {
		
		ItemStack sword = new ItemStack(Material.WOOD_SWORD, 1);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
			
		ItemStack bow = new ItemStack(Material.BOW, 1);
		bow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
		bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		bow.addEnchantment(Enchantment.DURABILITY, 3);
		 
		ItemStack arrows = new ItemStack(Material.ARROW, 1);
		
		ItemStack steak = new ItemStack(Material.COOKED_BEEF, 32);
		ItemStack pork = new ItemStack(Material.GRILLED_PORK, 32);
		ItemStack carrot = new ItemStack(Material.GOLDEN_CARROT, 32);
		
		ItemStack helmet = new ItemStack(Material.CHAINMAIL_HELMET);
		helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		
		ItemStack chestplate = new ItemStack(Material.GOLD_CHESTPLATE);
		
		ItemStack leggings = new ItemStack(Material.GOLD_LEGGINGS);
		
		ItemStack boots = new ItemStack(Material.GOLD_BOOTS);
		 
		PlayerInventory inv = player.getInventory();
		inv.addItem(sword, bow, arrows);
			
		switch (plugin.getConfig().getInt("Players." + "preferences." + player.getUniqueId() + ".food")) {
		case 0: inv.addItem(steak); break;
		case 1: inv.addItem(pork); break;
		case 2: inv.addItem(carrot); break;
		default: inv.addItem(steak); break;
		}
			
		if (inv.getHelmet() == null) {
			inv.setHelmet(helmet);
		}
		else {
			inv.addItem(helmet);
		}
			
		if (inv.getChestplate() == null) {
			inv.setChestplate(chestplate);
		}
		else {
			inv.addItem(chestplate);
		}
			
		if (inv.getLeggings() == null) {
			inv.setLeggings(leggings);
		}
		else {
			inv.addItem(leggings);
		}
			
		if (inv.getBoots() == null) {
			inv.setBoots(boots);
		}
		else {
			inv.addItem(boots);
		}
		
		player.sendMessage(ChatColor.GREEN + "Received kit " + ChatColor.AQUA + "archer" + ChatColor.GREEN + ".");
	}
	
	public static void giveKitDefaultLite(Player player) {
		
		ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
		
		ItemStack bow = new ItemStack(Material.BOW, 1);
		
		ItemStack arrows = new ItemStack(Material.ARROW, 5);
		
		ItemStack steak = new ItemStack(Material.COOKED_BEEF, 32);
		ItemStack pork = new ItemStack(Material.GRILLED_PORK, 32);
		ItemStack carrot = new ItemStack(Material.GOLDEN_CARROT, 32);
		
		ItemStack helmet = new ItemStack(Material.IRON_HELMET);
		
		ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
		
		ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
		
		ItemStack boots = new ItemStack(Material.IRON_BOOTS);
		
		
		PlayerInventory inv = player.getInventory();
		inv.addItem(sword, bow, arrows);
		
		switch (plugin.getConfig().getInt("Players." + "preferences." + player.getUniqueId() + ".food")) {
		case 0: inv.addItem(steak); break;
		case 1: inv.addItem(pork); break;
		case 2: inv.addItem(carrot); break;
		default: inv.addItem(steak); break;
		}
		
		player.getInventory().setHelmet(helmet);
		player.getInventory().setChestplate(chestplate);
		player.getInventory().setLeggings(leggings);
		player.getInventory().setBoots(boots);
	}
	
	public static void giveKitNoDebuff(Player player) {
		ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
		sword.addEnchantment(Enchantment.FIRE_ASPECT, 2);
		sword.addEnchantment(Enchantment.DURABILITY, 3);
		
		ItemStack pearls = new ItemStack(Material.ENDER_PEARL, 8);
		
		ItemStack steak = new ItemStack(Material.COOKED_BEEF, 32);
		ItemStack pork = new ItemStack(Material.GRILLED_PORK, 32);
		ItemStack carrot = new ItemStack(Material.GOLDEN_CARROT, 32);
		
		ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
		helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		helmet.addEnchantment(Enchantment.DURABILITY, 3);
		
		ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
		chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		chestplate.addEnchantment(Enchantment.DURABILITY, 3);
		
		ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
		leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		leggings.addEnchantment(Enchantment.DURABILITY, 3);
		
		ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		boots.addEnchantment(Enchantment.DURABILITY, 3);
		
		Potion pot = new Potion(PotionType.INSTANT_HEAL, 2);
		pot.setSplash(true);
		ItemStack potion = pot.toItemStack(1);
		
		
		PlayerInventory inv = player.getInventory();
		inv.addItem(sword, pearls);
		
		switch (plugin.getConfig().getInt("Players." + "preferences." + player.getUniqueId() + ".food")) {
		case 0: inv.addItem(steak); break;
		case 1: inv.addItem(pork); break;
		case 2: inv.addItem(carrot); break;
		default: inv.addItem(steak); break;
		}
		
		while (inv.firstEmpty() != -1) {
			inv.addItem(potion);
		}
		
		player.getInventory().setHelmet(helmet);
		player.getInventory().setChestplate(chestplate);
		player.getInventory().setLeggings(leggings);
		player.getInventory().setBoots(boots);
	}
}
