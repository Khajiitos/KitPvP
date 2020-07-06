package me.Khajiitos.KitPvP;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Protection implements Listener {
	
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if (!event.getPlayer().isOp()) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "You are not allowed to build.");
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if (!event.getPlayer().isOp()) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "You are not allowed to build.");
		}
	}
	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player && !event.getDamager().isOp()) {
			Entity damager = event.getDamager();
			if (event.getEntity().getLocation().getY() > 125) {
				
				if (damager instanceof Player) {
					damager.sendMessage(ChatColor.RED + "You can't PvP here.");
				}
				else if (damager instanceof Arrow) {
					Arrow arrow = (Arrow) damager;
					if (arrow.getShooter() instanceof Player) {
						Player player = (Player) arrow.getShooter();
						player.sendMessage(ChatColor.RED + "You can't PvP here.");
					}
				}
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onExplosion(ExplosionPrimeEvent event) {
		event.setRadius(0);
		event.setFire(false);
	}
	
	@EventHandler
	public void onSpread(BlockSpreadEvent event) {
		if (!event.getSource().isLiquid()) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockBurn(BlockBurnEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void combatTag(EntityDamageByEntityEvent event) {
        if (!Duel.duelingplayers.containsKey(event.getEntity())) {
			if (!event.isCancelled()) {
				if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) {
					return;
				}
				Player victim = (Player) event.getEntity();
				Player damager = (Player) event.getDamager();
				
				if (!Globals.combattaggedplayers.containsKey(victim)) {
					victim.sendMessage(ChatColor.RED + "You're now combat tagged. Do not log off!");
				}
				if (!Globals.combattaggedplayers.containsKey(damager)) {
					damager.sendMessage(ChatColor.RED + "You're now combat tagged. Do not log off!");
				}
				
				int currentsecond = (int) System.currentTimeMillis() / 1000;
				int scheduledend = currentsecond + 15;
				Globals.combattaggedplayers.put(victim, scheduledend);
				Globals.combattaggedplayers.put(damager, scheduledend);
			}
		}
	}
	
	@EventHandler
	public void noUproot(PlayerInteractEvent event) {
	    if(event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.SOIL) {
	        event.setCancelled(true);
	    }
	}
	
	@EventHandler
	public void antibreak(HangingBreakByEntityEvent event) {
		if (!event.getRemover().isOp()) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
        if (!Duel.duelingplayers.containsKey(event.getPlayer())) {
			if (Globals.combattaggedplayers.containsKey(event.getPlayer())) {
				event.getPlayer().setHealth(0);
				Globals.combattaggedplayers.remove(event.getPlayer());
			}
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (event.getPlayer().getHealth() == 0) {
			event.getPlayer().sendMessage(ChatColor.DARK_RED + "You logged off during combat and were killed.");
		}
	}
}
