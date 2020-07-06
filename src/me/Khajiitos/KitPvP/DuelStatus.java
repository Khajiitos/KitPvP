package me.Khajiitos.KitPvP;

import org.bukkit.entity.Player;

public class DuelStatus {
	
	Player player;
	int arena;
	int mode;
	boolean gracePeriod;
	
	public DuelStatus(Player player, int arena, int mode) {
		this.player = player;
		this.arena = arena;
		this.mode = mode;
	}
	
	public int getArena() {
		return arena;
	}
	
	public int getMode() {
		return mode;
	}
	
	public Player getPlayer() {	
		return player;
	}
	
	public boolean isGracePeriod() {
		return gracePeriod;
	}
}
