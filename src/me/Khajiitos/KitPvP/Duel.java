package me.Khajiitos.KitPvP;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

public class Duel implements Listener {
	
	public static HashMap<Player, DuelInvite> pendinginvites = new HashMap<Player, DuelInvite>();
	public static HashMap<Player, DuelStatus> duelingplayers = new HashMap<Player, DuelStatus>();
	public static HashMap<Player, ItemStack[]> oldinvs = new HashMap<Player, ItemStack[]>();
	public static HashMap<Player, ItemStack[]> oldarmors = new HashMap<Player, ItemStack[]>();
	
	public static void startDuel(Player player1, Player player2, int arena, int mode) {
		
		PlayerInventory player1inv = player1.getInventory();
		PlayerInventory player2inv = player2.getInventory();
		
		oldinvs.put(player1, player1inv.getContents());
		oldinvs.put(player2, player2inv.getContents());
		
		oldarmors.put(player1, player1inv.getArmorContents());
		oldarmors.put(player2, player2inv.getArmorContents());
		
		player1.getInventory().clear();
		player2.getInventory().clear();
		player1.getInventory().setArmorContents(null);
		player2.getInventory().setArmorContents(null);
		
		Location p1spawn = Arena.getSpawnLoc(1, arena);
		Location p2spawn = Arena.getSpawnLoc(2, arena);
		
		player1.teleport(p1spawn);
		player2.teleport(p2spawn);
		
		duelingplayers.put(player1, new DuelStatus(player2, arena, mode));
		duelingplayers.put(player2, new DuelStatus(player1, arena, mode));
		startCountdown(player1, player2, arena, mode);
		
		player1.setGameMode(GameMode.SURVIVAL);
		player2.setGameMode(GameMode.SURVIVAL);
		
		player1.setHealth(20);
		player1.setFoodLevel(20);
		player1.setSaturation(5);
		player1.getActivePotionEffects().clear();
		
		player2.setHealth(20);
		player2.setFoodLevel(20);
		player2.setSaturation(5);
		player2.getActivePotionEffects().clear();
		
		player1.closeInventory();
		player2.closeInventory();
	}
	
	
	public static void startCountdown(Player p1, Player p2, int arena, int mode) {
		new BukkitRunnable() {
			int countdown = 5;
	           @Override
	           public void run() {
	        	    String number;
	        	    switch (countdown) {
	        	    case 1: number = "①"; break;
	        	    case 2: number = "②"; break;
	        	    case 3: number = "③"; break;
	        	    case 4: number = "④"; break;
	        	    case 5: number = "⑤"; break;
	        	    default: number = "①"; break;
	        	    }
	        	   if (countdown < 1) {
	        		    cancel();
	        			Location p1spawn = Arena.getSpawnLoc(1, arena);
	        			Location p2spawn = Arena.getSpawnLoc(2, arena);
	        			p1.teleport(p1spawn);
	        			p2.teleport(p2spawn);
	        			
	        			switch (mode) {
	        			case 1:	Kits.giveKitDefaultLite(p1); Kits.giveKitDefaultLite(p2); break;
	        			case 2: Kits.giveKitNoDebuff(p1); Kits.giveKitNoDebuff(p2); break;
	        			default: Kits.giveKitDefaultLite(p1); Kits.giveKitDefaultLite(p2); break;
	        			}
	        			
	        		    IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\":\"" + ChatColor.YELLOW + "GO!" + "\"}");
	        		    PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
	        		    PacketPlayOutTitle length = new PacketPlayOutTitle(5, 60, 5);
	        		    ((CraftPlayer) p1).getHandle().playerConnection.sendPacket(title);
	        		    ((CraftPlayer) p1).getHandle().playerConnection.sendPacket(length);
	        		    ((CraftPlayer) p2).getHandle().playerConnection.sendPacket(title);
	        		    ((CraftPlayer) p2).getHandle().playerConnection.sendPacket(length);
	        		    p2.playSound(p2.getLocation(), Sound.FIREWORK_BLAST, 1, 1);
	        		    
	        			p1.setHealth(20);
	        			p1.setFoodLevel(20);
	        			p1.setSaturation(5);
	        			p1.getActivePotionEffects().clear();
	        			
	        			p2.setHealth(20);
	        			p2.setFoodLevel(20);
	        			p2.setSaturation(5);
	        			p2.getActivePotionEffects().clear();
	        			
	        	   } else {
	        		    IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\":\"" + ChatColor.YELLOW + number + "\"}");
	        		    PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
	        		    PacketPlayOutTitle length = new PacketPlayOutTitle(1, 60, 1);
	        		    ((CraftPlayer) p1).getHandle().playerConnection.sendPacket(title);
	        		    ((CraftPlayer) p1).getHandle().playerConnection.sendPacket(length);
	        		    ((CraftPlayer) p2).getHandle().playerConnection.sendPacket(title);
	        		    ((CraftPlayer) p2).getHandle().playerConnection.sendPacket(length);
	        		    p2.playSound(p2.getLocation(), Sound.NOTE_PIANO, 1, 1);
		        	   countdown--;
	        	   }
	           }
	       }.runTaskTimer(Globals.plugin, 20, 20);
	   }
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		
        if (Duel.duelingplayers.containsKey(player)) {
        	Duel.duelingplayers.remove(Duel.duelingplayers.get(player).getPlayer());
        	Duel.duelingplayers.remove(player);
        	event.setKeepInventory(true);
        	player.getInventory().clear();
        	player.setHealth(20);
        	player.setGameMode(GameMode.SPECTATOR);
        	
    	    IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\":\"" + ChatColor.AQUA + player.getKiller().getName() + ChatColor.GREEN + " won the duel!" + "\"}");
    	    PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
    	    PacketPlayOutTitle length = new PacketPlayOutTitle(3, 50, 3);

    	    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
    	    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
    	    
    	    ((CraftPlayer) player.getKiller()).getHandle().playerConnection.sendPacket(title);
    	    ((CraftPlayer) player.getKiller()).getHandle().playerConnection.sendPacket(length);
    	    
    	    player.playSound(player.getLocation(), Sound.FIREWORK_LARGE_BLAST, 1, 1);
    	    player.getKiller().playSound(player.getKiller().getLocation(), Sound.FIREWORK_LARGE_BLAST, 1, 1);
        	
            Bukkit.getScheduler().scheduleSyncDelayedTask(Globals.plugin, new Runnable() {
            	public void run() {
            		player.setGameMode(GameMode.SURVIVAL);
            		Location loc = Globals.plugin.getServer().getWorld("world").getSpawnLocation();
            		loc.setX(loc.getX() + 0.5);
            		loc.setZ(loc.getZ() + 0.5);
            		player.teleport(loc);
            		player.setHealth(20);
            		player.getKiller().setHealth(20);
          
            		player.getInventory().clear();
            		player.getInventory().setArmorContents(null);
            		player.getInventory().setContents(Duel.oldinvs.get(player));
            		player.getInventory().setArmorContents(Duel.oldarmors.get(player));
            		
            		player.getKiller().getInventory().clear();
            		player.getKiller().getInventory().setArmorContents(null);
            		player.getKiller().teleport(loc);
            		player.getKiller().getInventory().setContents(Duel.oldinvs.get(player.getKiller()));
            		player.getKiller().getInventory().setArmorContents(Duel.oldarmors.get(player.getKiller()));
            		
            		player.getKiller().setHealth(20);
            		player.getKiller().setFoodLevel(20);
            		player.getKiller().setSaturation(5);
            		player.getKiller().getActivePotionEffects().clear();
                }
              }, 60); 
        	return;
        }
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
        if (Duel.duelingplayers.containsKey(event.getPlayer())) {
    		Player leaver = event.getPlayer();
    		Player winner = Duel.duelingplayers.get(event.getPlayer()).getPlayer();
    		
        	
    	    IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\":\"" + ChatColor.AQUA + winner.getName() + ChatColor.GREEN + " won the duel!" + "\"}");
    	    PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
    	    PacketPlayOutTitle length = new PacketPlayOutTitle(3, 50, 3);

    	    ((CraftPlayer) winner).getHandle().playerConnection.sendPacket(title);
    	    ((CraftPlayer) winner).getHandle().playerConnection.sendPacket(length);
    	    
    	    
    	    winner.playSound(winner.getLocation(), Sound.FIREWORK_LARGE_BLAST, 1, 1);
        	
    	    
            Bukkit.getScheduler().scheduleSyncDelayedTask(Globals.plugin, new Runnable() {
            	public void run() {
            		Location loc = Globals.plugin.getServer().getWorld("world").getSpawnLocation();
            		loc.setX(loc.getX() + 0.5);
            		loc.setZ(loc.getZ() + 0.5);
            		
            		winner.getInventory().clear();
            		winner.getInventory().setArmorContents(null);
            		winner.teleport(loc);
            		winner.getInventory().setContents(Duel.oldinvs.get(winner));
            		winner.getInventory().setArmorContents(Duel.oldarmors.get(winner));
                }
              }, 60); 
            
    		leaver.getInventory().setContents(Duel.oldinvs.get(leaver));
    		leaver.getInventory().setArmorContents(Duel.oldarmors.get(leaver));
    		Duel.duelingplayers.remove(leaver);
        	Duel.duelingplayers.remove(winner);
        }
	}
}
