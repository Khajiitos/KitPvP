package me.Khajiitos.KitPvP;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class HealthIndicator {

	public static void register() {
		if (KitPvP.sb.getObjective("health") == null) {
			Objective o = KitPvP.sb.registerNewObjective("health", "health");
			o.setDisplayName(ChatColor.RED + "❤");
			o.setDisplaySlot(DisplaySlot.BELOW_NAME);
		}
	}
}
