package me.Khajiitos.KitPvP;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class StaffChat implements Listener {
	
	private boolean isForStaff(String msg) {
		return msg.startsWith("#");
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		if (event.getPlayer().isOp()) {
			if (isForStaff(event.getMessage())) {
				String newmsg = ChatColor.DARK_RED + "[StaffChat] " + ChatColor.YELLOW + event.getPlayer().getName() + ": " + ChatColor.DARK_AQUA + event.getMessage().substring(1);
				if (newmsg.startsWith(" ")) {
					newmsg.substring(1);
				}
				event.setCancelled(true);
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (player.isOp()) {
						player.sendMessage(newmsg);
					}
				}
			}
		}
	}
}
