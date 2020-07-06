package me.Khajiitos.KitPvP;

import org.bukkit.entity.Player;

public class DuelInvite {
	
	Player player;
	int mode;
	
	public DuelInvite(Player player, int mode) {
		this.mode = mode;
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public int getMode() {
		return mode;
	}
}
