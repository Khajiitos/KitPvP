package me.Khajiitos.KitPvP;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinMessage implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		event.setJoinMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.DARK_GRAY + "]" + ChatColor.YELLOW + " " + event.getPlayer().getName());
		message(event.getPlayer());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		event.setQuitMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.YELLOW + " " + event.getPlayer().getName());
	}
	
	public static void message(Player player) {
		if (!player.hasPlayedBefore()) {
			player.sendMessage(ChatColor.GOLD + "Hi " + ChatColor.DARK_AQUA + player.getName() + ChatColor.GOLD + ", welcome to our server!");
			player.sendMessage(ChatColor.GOLD + "It seems like you're here for the first time, use the command " + ChatColor.DARK_AQUA + "/help " + ChatColor.GOLD + "to get a list of commands.");
		}
		else {
			player.sendMessage(ChatColor.GOLD + "Hi " + ChatColor.DARK_AQUA + player.getName() + ChatColor.GOLD + ", welcome back to our server!");
		}
	}
}
