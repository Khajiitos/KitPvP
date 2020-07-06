package me.Khajiitos.KitPvP;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Announcer {

	private static Plugin plugin = KitPvP.getPlugin(KitPvP.class);

	private static int number = 1;

	public static void start() {
		BukkitScheduler scheduler = plugin.getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				if (plugin.getServer().getOnlinePlayers().size() > 0) {
					message(number);
					number++;
					if (number >= 4) {
						number = 1;
					}
				}
			}
		}, 6000L, 6000L);
	}

	private static void message(int i) {
		switch (i) {
		case 1:
			plugin.getServer().broadcastMessage("");
			plugin.getServer().broadcastMessage(ChatColor.GRAY + "You can join our discord server! Link: "
					+ ChatColor.AQUA + "https://discord.gg/qxby8X");
			plugin.getServer().broadcastMessage("");
			break;
		case 2:
			plugin.getServer().broadcastMessage("");
			plugin.getServer().broadcastMessage(ChatColor.GRAY + "See a bug? Report it on our discord! Link: "
					+ ChatColor.AQUA + "https://discord.gg/qxby8X");
			plugin.getServer().broadcastMessage("");
			break;
		case 3:
			plugin.getServer().broadcastMessage("");
			plugin.getServer().broadcastMessage(ChatColor.GRAY + "Do you have a suggestion? We have a channel on our discord server for that! Link: "
					+ ChatColor.AQUA + "https://discord.gg/qxby8X");
			plugin.getServer().broadcastMessage("");
			break;
		}
	}
}
