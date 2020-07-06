package me.Khajiitos.KitPvP;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Ranks implements Listener {
	private static Plugin plugin = KitPvP.getPlugin(KitPvP.class);
	
	public static Team defaultrank;
	public static Team viprank;
	public static Team ownerrank;
	public static Team twitchrank;
	public static Team builderrank;
	
	public static void setup() {
		defaultrank.setPrefix(ChatColor.GRAY.toString());	
		
		viprank.setPrefix(ChatColor.GREEN.toString());	
		
		ownerrank.setPrefix(ChatColor.RED + ChatColor.BOLD.toString());	
		
		twitchrank.setPrefix(ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString());	
		
		builderrank.setPrefix(ChatColor.GREEN + ChatColor.BOLD.toString());	
	}
	
	public static void reverse() {
		/*
		defaultrank = null;
		viprank = null;
		ownerrank = null;
		twitchrank = null;
		builderrank = null;
		*/
		//defaultrank.unregister();
		KitPvP.sb.getTeam("VIP").unregister();
		KitPvP.sb.getTeam("Owner").unregister();
		KitPvP.sb.getTeam("Twitch").unregister();
		KitPvP.sb.getTeam("Builder").unregister();
	}
	
	public static String getPrefix(Player player) {
		switch (getRank(player)) {
		case "Owner":
			String Ownerprefix = ChatColor.DARK_RED.toString() + ChatColor.BOLD + "[" + ChatColor.RED + ChatColor.BOLD + "Owner" + ChatColor.DARK_RED + ChatColor.BOLD + "] " + ChatColor.RED;
			return Ownerprefix;
		case "default":
			String defaultprefix = ChatColor.GRAY + "";
			return defaultprefix;
		case "Builder":
			String builderprefix = ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "[" + ChatColor.GREEN + ChatColor.BOLD + "Builder" + ChatColor.DARK_GREEN + ChatColor.BOLD + "] " + ChatColor.GREEN;
			return builderprefix;
		case "VIP":
			String vipprefix = ChatColor.DARK_GREEN.toString() + "[" + ChatColor.GREEN + "VIP" + ChatColor.DARK_GREEN + "] " + ChatColor.GREEN;
			return vipprefix;
		case "Twitch":
			String twitchprefix = ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + "[" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Twitch" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "] " + ChatColor.RESET + ChatColor.LIGHT_PURPLE;
			return twitchprefix;
		default: return ChatColor.GRAY + "";
		}
	}
	
	public static String getPrefixFromRank(String rank) {
		switch (rank) {
		case "Owner":
			String Ownerprefix = ChatColor.DARK_RED.toString() + ChatColor.BOLD + "[" + ChatColor.RED + ChatColor.BOLD + "Owner" + ChatColor.DARK_RED + ChatColor.BOLD + "] " + ChatColor.RED;
			return Ownerprefix;
		case "default":
			String defaultprefix = ChatColor.GRAY + "";
			return defaultprefix;
		case "Builder":
			String builderprefix = ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "[" + ChatColor.GREEN + ChatColor.BOLD + "Builder" + ChatColor.DARK_GREEN + ChatColor.BOLD + "] " + ChatColor.GREEN;
			return builderprefix;
		case "VIP":
			String vipprefix = ChatColor.DARK_GREEN.toString() + "[" + ChatColor.GREEN + "VIP" + ChatColor.DARK_GREEN + "] " + ChatColor.GREEN;
			return vipprefix;
		case "Twitch":
			String twitchprefix = ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + "[" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Twitch" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "] " + ChatColor.RESET + ChatColor.LIGHT_PURPLE;
			return twitchprefix;
		default: return ChatColor.GRAY + "";
		}
	}
	
	public static String getRank(Player player) {
		
		if (plugin.getConfig().getString("Players." + player.getUniqueId() + ".rank") != null) {
			return plugin.getConfig().getString("Players." + player.getUniqueId() + ".rank");
		}
		else {
			setRank(player, "default");
			return plugin.getConfig().getString("Players." + player.getUniqueId() + ".rank");
		}
	}
	
	public static int permissionLevel(Player player) {
		switch (getRank(player)) {
		case "Owner": return 5;
		case "Admin" : return 4;
		case "Builder": return 3;
		case "Twitch": return 2;
		case "VIP": return 1;
		case "default": return 0;
		default: return 0;
		}
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		event.setFormat(getPrefix(event.getPlayer()) + event.getPlayer().getName() + ChatColor.RESET + ": " + event.getMessage());
	}
	
	public static void setRank(Player player, String rank) {
		plugin.getConfig().set("Players." + player.getUniqueId() + ".rank", rank);
		plugin.saveConfig();
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (!event.getPlayer().hasPlayedBefore()) {
			setRank(event.getPlayer(), "default");
		}
		/*
		switch (getRank(event.getPlayer())) {
		case "Owner":
			ownerrank.addEntry(event.getPlayer().getName()); break;
		case "Builder":
			builderrank.addEntry(event.getPlayer().getName()); break;
		case "Twitch":
			twitchrank.addEntry(event.getPlayer().getName()); break;
		case "VIP":
			viprank.addEntry(event.getPlayer().getName()); break;
		case "default":
			defaultrank.addEntry(event.getPlayer().getName()); break;
		}
		*/
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {	
		/*
		Player player = event.getPlayer();
		String playername = player.getName();
		
		if (defaultrank.getEntries().contains(playername))
			defaultrank.removeEntry(playername);
		
		if (viprank.getEntries().contains(playername))
			viprank.removeEntry(playername);
		
		if (builderrank.getEntries().contains(playername))
			builderrank.removeEntry(playername);
		
		if (twitchrank.getEntries().contains(playername))
			twitchrank.removeEntry(playername);
		
		if (ownerrank.getEntries().contains(playername))
			ownerrank.removeEntry(playername);
	*/
	}
}
