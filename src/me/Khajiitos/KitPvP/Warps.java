package me.Khajiitos.KitPvP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public class Warps {
	
	private static Plugin plugin = KitPvP.getPlugin(KitPvP.class);
	
	public static void createWarp(String name, Location loc) {
		if (!doesWarpExist(name)) {
			plugin.getConfig().set("Server.warps." + name, loc);
			plugin.saveConfig();
		}
	}
	
	public static boolean doesWarpExist(String name) {
		if (plugin.getConfig().get("Server.warps." + name) != null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static Location getLoc(String name) {
		if (doesWarpExist(name)) {
			return (Location) plugin.getConfig().get("Server.warps." + name);
		} else {
			return null;
		}
	}
	
	public static void removeWarp(String name) {
		if (doesWarpExist(name)) {
			plugin.getConfig().set("Server.warps." + name, null);
			plugin.saveConfig();
		}
	}
}
