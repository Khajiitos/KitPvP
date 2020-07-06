package me.Khajiitos.KitPvP;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Economy {
	
	private static Plugin plugin = KitPvP.getPlugin(KitPvP.class);
	
	public static double getBalance(Player player) {
		return plugin.getConfig().getDouble("Players." + player.getUniqueId() + ".balance");
	}
	
	public static void setBalance(Player player, double amount) {
		plugin.getConfig().set("Players." + player.getUniqueId() + ".balance", amount);
		plugin.saveConfig();
	}
	
	public static void addToBalance(Player player, double amount) {
		double oldBalance = plugin.getConfig().getDouble("Players." + player.getUniqueId() + ".balance");
		plugin.getConfig().set("Players." + player.getUniqueId() + ".balance", oldBalance + amount);
		plugin.saveConfig();
	}
	
	public static void removeFromBalance(Player player, double amount) {
		double oldBalance = plugin.getConfig().getDouble("Players." + player.getUniqueId() + ".balance");
		plugin.getConfig().set("Players." + player.getUniqueId() + ".balance", oldBalance - amount);
		plugin.saveConfig();
	}
}
