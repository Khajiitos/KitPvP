package me.Khajiitos.KitPvP;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class EloSystem implements Listener {
	private Plugin plugin = KitPvP.getPlugin(KitPvP.class);
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (!event.getPlayer().hasPlayedBefore()) {
			plugin.getConfig().set("Elo." + event.getPlayer().getUniqueId(), 1000);
			plugin.saveConfig();
		}
	}
	
	@EventHandler
	public void onPlayerKill(PlayerDeathEvent event) {
		
		Player victim = event.getEntity();
		Player killer = event.getEntity().getKiller();
		
		if (Duel.duelingplayers.containsKey(victim) || Duel.duelingplayers.containsKey(killer)) {
			event.setDeathMessage("");
			return;
		}
		
		if (!(killer instanceof Player)) {
			return;
		}
		
		if (killer == victim) {
			event.setDeathMessage(ChatColor.DARK_RED + victim.getName() + ChatColor.RED + " has been killed");
			return;
		}
		
		int killerelo = plugin.getConfig().getInt("Elo." + killer.getUniqueId());
		int victimelo = plugin.getConfig().getInt("Elo." + victim.getUniqueId());
		float fkillerelo = (float) killerelo;
		float fvictimelo = (float) victimelo;
		
		float multiplier = fvictimelo / fkillerelo;
		
		float dif = 15 * multiplier;
		
		float fnewkillerelo = fkillerelo + dif;
		float fnewvictimelo = fvictimelo - (dif * 0.75f);
		int newkillerelo = Math.round(fnewkillerelo);
		int newvictimelo = Math.round(fnewvictimelo);
		
		int difkiller = newkillerelo - killerelo;
		int difvictim = victimelo - newvictimelo;
		
		plugin.getConfig().set("Elo." + killer.getUniqueId(), newkillerelo);
		plugin.getConfig().set("Elo." + victim.getUniqueId(), newvictimelo);
		plugin.saveConfig();
		
		killer.sendMessage(ChatColor.GREEN + "You have gained " + ChatColor.AQUA + difkiller + ChatColor.GREEN + " ELO points for killing " + ChatColor.RED + ChatColor.BOLD.toString() + victim.getName() + ChatColor.DARK_GREEN + ".");
		
		event.setDeathMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD.toString() + killer.getName() + ChatColor.RESET.toString() + ChatColor.GRAY + " (" + ChatColor.GREEN + "+" + difkiller + ChatColor.GRAY + ") " + ChatColor.RED + "has killed " + ChatColor.DARK_RED + ChatColor.BOLD.toString() + victim.getName() + ChatColor.RESET + ChatColor.GRAY + " (" + ChatColor.RED + "-" + difvictim + ChatColor.GRAY + ")" );
	}
}
