package me.Khajiitos.KitPvP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Globals {
	
	public static Plugin plugin = KitPvP.getPlugin(KitPvP.class);
	
	public static HashMap<UUID, Long> kitdefaultcooldown = new HashMap<UUID, Long>();
	
	public static HashMap<Player, Player> lastmessagingplayers = new HashMap<Player, Player>();
	
	public static HashMap<Player, Integer> combattaggedplayers = new HashMap<Player, Integer>();
	
	public static ArrayList<Player> vanishedplayers = new ArrayList<Player>();
	
	public static ArrayList<Player> frozenplayers = new ArrayList<Player>();
	
	public static int currentsec;

	
	
	public static void everysecloop() {
		BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
            	currentsec = (int) System.currentTimeMillis() / 1000;
            	for (Player player : Bukkit.getOnlinePlayers()) {
            		if (combattaggedplayers.containsKey(player)) {
            			if (combattaggedplayers.get(player) < currentsec) {
            				combattaggedplayers.remove(player);
            				player.sendMessage(ChatColor.YELLOW + "You're no longer combat tagged.");
            			}
            		}
            		
            		if (plugin.getConfig().get("Players." + player.getUniqueId() + ".punishments.muteexpire") != null && plugin.getConfig().getLong("Players." + player.getUniqueId() + ".punishments.muteexpire") != 0) {
            			int expiresec = (int) plugin.getConfig().getLong("Players." + player.getUniqueId() + ".punishments.muteexpire") / 1000;
            			if (expiresec != 0) {
	            			if (expiresec < currentsec) {
	            				player.sendMessage(ChatColor.YELLOW + "Your mute has expired.");
	            				plugin.getConfig().set("Players." + player.getUniqueId() + ".punishments.muteexpire", 0);
	            				plugin.saveConfig();
	            			}
            			}
            		}
            	}
            	
            	for (Player player : Bukkit.getOnlinePlayers()) {
            		if (player.isOp()) {
            			for (Player p : Bukkit.getOnlinePlayers()) {
            				if (vanishedplayers.contains(p)) {
            					player.showPlayer(p);
            				}
            			}
            		} else {
            			if (vanishedplayers.contains(player)) {
            				for (Player p : Bukkit.getOnlinePlayers()) {
            					p.showPlayer(player);
            				}
            			}
            			for (Player p : Bukkit.getOnlinePlayers()) {
        					if (vanishedplayers.contains(p)) {
        						player.hidePlayer(p);
        					}
            			}
            		}
            	}
            }
        }, 20L, 20L);
	}
}
