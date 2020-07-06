package me.Khajiitos.KitPvP;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public class Arena {
	
	private static Plugin plugin = KitPvP.getPlugin(KitPvP.class);
	
	public static Location getSpawnLoc(int player, int arena) {
		return ((Location) plugin.getConfig().get("Server.duelarenas.arena" + arena + ".spawn" + player));
	}
	
	public static boolean exists(int arena) {
		if (plugin.getConfig().get("Server.duelarenas.arena" + arena) != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public static ArrayList<Integer> getArenas() {
		int arena = 1;
		ArrayList<Integer> arenas = new ArrayList<Integer>();
		while (exists(arena)) {
			arenas.add(arena);
			arena++;
		}
		return arenas;
	}
	
	public static ArrayList<Integer> getFreeArenas() {
		ArrayList<Integer> arenas = new ArrayList<Integer>();
		for (int a : getArenas()) {
			if (isFree(a)) {
				arenas.add(a);
			}
		}
		return arenas;
	}
	
	public static boolean isFree(int arena) {
		
		if (!exists(arena)) {
			return false;
		}
		
		for (DuelStatus ds : Duel.duelingplayers.values()) {
			if (ds.getArena() == arena) {
				return false;
			}
		}
		return true;
	}
}
