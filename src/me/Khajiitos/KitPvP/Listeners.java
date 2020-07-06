package me.Khajiitos.KitPvP;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

public class Listeners implements Listener {
	
	public static ItemStack invsaveritem = new ItemStack(Material.ENCHANTED_BOOK);
	public static ItemMeta savermetaitem = invsaveritem.getItemMeta();
	
	private Plugin plugin = KitPvP.getPlugin(KitPvP.class);
	
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Location loc = plugin.getServer().getWorld("world").getSpawnLocation();
		loc.setX(loc.getX() + 0.5);
		loc.setZ(loc.getZ() + 0.5);
		event.getPlayer().teleport(loc);
		if (!event.getPlayer().hasPlayedBefore()) {
			plugin.getConfig().set("Players." + "preferences." + event.getPlayer().getUniqueId() + ".autokit", true);
			plugin.getConfig().set("Players." + "preferences." + event.getPlayer().getUniqueId() + ".food", 0);
			plugin.getConfig().set("Players." + event.getPlayer().getUniqueId() + ".balance", 10.0D);
			plugin.saveConfig();
			if (!event.getPlayer().getInventory().contains(Material.BOW)) {
				Kits.giveKitDefault(event.getPlayer());
			}
		}
		
		if (!event.getPlayer().isOp()) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (Globals.vanishedplayers.contains(p)) {
					event.getPlayer().hidePlayer(p);
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (Globals.frozenplayers.contains(event.getPlayer())) {
			event.setTo(event.getFrom());
		}
	}
	
	@EventHandler
	public void onPlayerSpawn(PlayerRespawnEvent event) {
		Location loc = plugin.getServer().getWorld("world").getSpawnLocation();
		loc.setX(loc.getX() + 0.5);
		loc.setZ(loc.getZ() + 0.5);
		event.getPlayer().teleport(loc);
		if (event.getPlayer().getInventory().contains(Material.BOW)) {
			return;
		}
		if (plugin.getConfig().getBoolean("Players." + "preferences." + event.getPlayer().getUniqueId() + ".autokit")) {
			Kits.giveKitDefault(event.getPlayer());
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		
		if (Duel.duelingplayers.containsKey(player)) {
			return;
		}
		
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
        	public void run() {
        		player.spigot().respawn();
        		player.setFireTicks(0);
            }
          },  1); 
        
		if (event.getDrops().contains(invsaveritem)) {
			Iterator<ItemStack> iterator = event.getDrops().iterator();
	        while (iterator.hasNext()) {
	             ItemStack stack = iterator.next();
	             if (stack.equals(invsaveritem)) {
	     			event.setKeepInventory(true);
	    			player.sendMessage(ChatColor.YELLOW + "Your inventory has been saved by the inventory saver.");
	            	stack = new ItemStack(Material.AIR);
	            	break;
	             }
	        }
		}
		
		if (event.getEntity().getKiller() == null) {
			event.setDeathMessage(ChatColor.DARK_RED + event.getEntity().getName() + ChatColor.RED + " has been killed");
		}
		
	    IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\":\"" + ChatColor.RED + "YOU DIED" + "\"}");
	    PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
	    PacketPlayOutTitle length = new PacketPlayOutTitle(5, 50, 5);

	    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
	    if (event.getEntity().getKiller() != null) {
	    	IChatBaseComponent subtitlemessage = ChatSerializer.a("{\"text\":\"" + ChatColor.RED + "Killed by "+ ChatColor.DARK_RED + event.getEntity().getKiller().getName() + "\"}");
	    	PacketPlayOutTitle subtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, subtitlemessage);
	    	((CraftPlayer) player).getHandle().playerConnection.sendPacket(subtitle);
	    }
	    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
	    
		if (event.getEntity().getKiller() != null) {
			double rangeMin = 5.0D;
			double rangeMax = 10.0D;
			Random r = new Random();
			double money = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
			
			double multiplier;
			if (Ranks.permissionLevel(player.getKiller()) >= 2) {
				multiplier = 1.5;
			}
			else if (Ranks.permissionLevel(player.getKiller()) >= 1) {
				multiplier = 1.25;
			}
			else {
				multiplier = 1;
			}
			money *= multiplier;
			double fixedmoney = (double) Math.round(money * 100) / 100;
			
			Player killer = event.getEntity().getKiller();
			if (killer != player) {
				Economy.addToBalance(killer, fixedmoney);
				PacketPlayOutChat actionbar = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + ChatColor.RED + "You killed " + ChatColor.DARK_RED + player.getName() + ChatColor.GRAY + " -" + ChatColor.GREEN + " +" + Double.toString(fixedmoney) + "$" + "\"}"), (byte) 2);
				killer.playSound(killer.getLocation(), Sound.ORB_PICKUP, 1, 2);
				((CraftPlayer) killer).getHandle().playerConnection.sendPacket(actionbar);
				killer.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
			}
		}
	    
	    
		if (player.getKiller() != null) {
			int killerkills = plugin.getConfig().getInt("Players.kills." + player.getKiller().getUniqueId());
			plugin.getConfig().set("Players.kills." + player.getKiller().getUniqueId(), killerkills + 1);
		}
		int playerdeaths = plugin.getConfig().getInt("Players.deaths." + player.getUniqueId());
		plugin.getConfig().set("Players.deaths." + player.getUniqueId(), playerdeaths + 1);
		plugin.saveConfig();
		
		
        Iterator<ItemStack> iterator = event.getDrops().iterator();
        
        while (iterator.hasNext()) {
             ItemStack stack = iterator.next();
             if (stack.getEnchantments().size() > 0) {
            	 continue;
             }
             switch (stack.getType()) {
             	case IRON_SWORD:
             	case IRON_HELMET:
             	case IRON_CHESTPLATE:
             	case IRON_LEGGINGS:
             	case IRON_BOOTS:
             	case BOW:
             	case ARROW:
             	case COOKED_BEEF:
             	case GRILLED_PORK:
             	case GOLDEN_CARROT:
             		iterator.remove();
                	break;
                default:
                  	break;
             }
        }
        
        if (Globals.combattaggedplayers.containsKey(player)) {
        	Globals.combattaggedplayers.remove(player);
        }
	}
	
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
    	if (Globals.vanishedplayers.contains(event.getPlayer())) {
    		Globals.vanishedplayers.remove(event.getPlayer());
    	}
    }
	
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
    	Player player = (Player) event.getWhoClicked();
    	ItemStack clicked = event.getCurrentItem();
    	Inventory inventory = event.getInventory();
    	if (clicked == null || inventory == null) {
    		return;
    	}
    	
    	if (inventory.getName().equals(ChatColor.RED + "Preferences")) { 
    		if (clicked.equals(KitPvP.autokittrue)) {
    			plugin.getConfig().set("Players." + "preferences." + player.getUniqueId() + ".autokit", false);
    			inventory.setItem(0, KitPvP.autokitfalse);
    			player.playSound(player.getLocation(), Sound.CLICK, 0.75F, 1.5F);
    		}
    		else if (clicked.equals(KitPvP.autokitfalse)) {
    			plugin.getConfig().set("Players." + "preferences." + player.getUniqueId() + ".autokit", true);
    			inventory.setItem(0, KitPvP.autokittrue);
    			player.playSound(player.getLocation(), Sound.CLICK, 0.75F, 1.75F);
    		}
    		
    		if (clicked.equals(KitPvP.steak)) {
    			plugin.getConfig().set("Players." + "preferences." + player.getUniqueId() + ".food", 1);
    			inventory.setItem(1, KitPvP.pork);
    			player.playSound(player.getLocation(), Sound.CLICK, 0.75F, 1.5F);
    		}
    		else if (clicked.equals(KitPvP.pork)) {
    			plugin.getConfig().set("Players." + "preferences." + player.getUniqueId() + ".food", 2);
    			inventory.setItem(1, KitPvP.carrot);
    			player.playSound(player.getLocation(), Sound.CLICK, 0.75F, 1.5F);
    		}
    		else if (clicked.equals(KitPvP.carrot)) {
    			plugin.getConfig().set("Players." + "preferences." + player.getUniqueId() + ".food", 0);
    			inventory.setItem(1, KitPvP.steak);
    			player.playSound(player.getLocation(), Sound.CLICK, 0.75F, 1.5F);
    		}
    		plugin.saveConfig();
    		event.setCancelled(true);
    	}
    	
    	if (inventory.getName().equals(ChatColor.LIGHT_PURPLE + "Shop")) {
    		if (clicked.equals(inventory.getItem(11))) {
    			if (Economy.getBalance(player) >= 100.0) {
    				if (player.getInventory().firstEmpty() != -1) {
	    				Economy.removeFromBalance(player, 100.0);
	    				player.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));
	    				player.sendMessage(ChatColor.GREEN + "You have successfully purchased: " + KitPvP.swordmeta.getDisplayName());
	    				player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1F, 1F);
	        			KitPvP.ingotmeta.setDisplayName(ChatColor.GOLD + ChatColor.BOLD.toString() + "Balance: " + ChatColor.AQUA + Economy.getBalance(player) + "$");
	        			KitPvP.balanceingot.setItemMeta(KitPvP.ingotmeta);
	    				inventory.setItem(0, KitPvP.balanceingot);
    				}
    				else {
    					player.sendMessage(ChatColor.RED + "Transaction failed, inventory full.");
    					player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    				}
    			}
    			else {
    				player.sendMessage(ChatColor.RED + "Transaction failed, insufficient balance.");
    				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    			}
    		}
    		
    		if (clicked.equals(inventory.getItem(13))) {
    			if (Economy.getBalance(player) >= 100.0) {
    				if (player.getInventory().firstEmpty() != -1) {
	    				Economy.removeFromBalance(player, 100.0);
	    				player.getInventory().addItem(new ItemStack(Material.FISHING_ROD));
	    				player.sendMessage(ChatColor.GREEN + "You have successfully purchased: " + KitPvP.rodmeta.getDisplayName());
	    				player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1F, 1F);
	        			KitPvP.ingotmeta.setDisplayName(ChatColor.GOLD + ChatColor.BOLD.toString() + "Balance: " + ChatColor.AQUA + Economy.getBalance(player) + "$");
	        			KitPvP.balanceingot.setItemMeta(KitPvP.ingotmeta);
	    				inventory.setItem(0, KitPvP.balanceingot);
    				}
    				else {
    					player.sendMessage(ChatColor.RED + "Transaction failed, inventory full.");
    					player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    				}
    			}
    			else {
    				player.sendMessage(ChatColor.RED + "Transaction failed, insufficient balance.");
    				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    			}
    		}
    		
    		if (clicked.equals(inventory.getItem(15))) {
    			if (Economy.getBalance(player) >= 1000.0) {
    				if (player.getInventory().firstEmpty() != -1) {
	    				Economy.removeFromBalance(player, 1000.0);
	    				player.getInventory().addItem(invsaveritem);
	    				player.sendMessage(ChatColor.GREEN + "You have successfully purchased: " + savermetaitem.getDisplayName());
	    				player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1F, 1F);
	        			KitPvP.ingotmeta.setDisplayName(ChatColor.GOLD + ChatColor.BOLD.toString() + "Balance: " + ChatColor.AQUA + Economy.getBalance(player) + "$");
	        			KitPvP.balanceingot.setItemMeta(KitPvP.ingotmeta);
	    				inventory.setItem(0, KitPvP.balanceingot);
    				}
    				else {
    					player.sendMessage(ChatColor.RED + "Transaction failed, inventory full.");
    					player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    				}
    			}
    			else {
    				player.sendMessage(ChatColor.RED + "Transaction failed, insufficient balance.");
    				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    			}
    		}
    		event.setCancelled(true);
    	}
    	
    	if (inventory.getName().startsWith(ChatColor.YELLOW + "Dueling:")) {
    		String cleanname = ChatColor.stripColor(inventory.getName());
    		Player target = Bukkit.getPlayer(cleanname.substring(9));
    		
    		if (clicked.equals(inventory.getItem(1))) {
    			
    			if (Arena.getFreeArenas().isEmpty()) {
    				player.sendMessage(ChatColor.RED + "There are no free arenas. Try again later.");
    				return;
    			}
    			
    	    	player.sendMessage(ChatColor.GREEN + "Invited player " + ChatColor.AQUA + target.getName() + ChatColor.GREEN + " to a " + ChatColor.AQUA + "classic " + ChatColor.GREEN + "duel! They have " + ChatColor.DARK_GREEN + "60 seconds " + ChatColor.DARK_GREEN + "to accept.");
    	    	target.sendMessage(ChatColor.GREEN + "You have been invited to a " + ChatColor.AQUA + "classic" + ChatColor.GREEN + " duel by "  + ChatColor.AQUA + player.getName() + ChatColor.GREEN + "! Type '/duel " + player.getName() + " accept' to accept. ");
    	    	Duel.pendinginvites.put(player, new DuelInvite(target, 1));
    			player.closeInventory();
    		}
    		if (clicked.equals(inventory.getItem(2))) {
    			
    			if (Arena.getFreeArenas().isEmpty()) {
    				player.sendMessage(ChatColor.RED + "There are no free arenas. Try again later.");
    				return;
    			}
    			
    	    	player.sendMessage(ChatColor.GREEN + "Invited player " + ChatColor.AQUA + target.getName() + ChatColor.GREEN + " to a " + ChatColor.AQUA + "NoDebuff " + ChatColor.GREEN + "duel! They have " + ChatColor.DARK_GREEN + "60 seconds " + ChatColor.DARK_GREEN + "to accept.");
    	    	target.sendMessage(ChatColor.GREEN + "You have been invited to a " + ChatColor.AQUA + "NoDebuff" + ChatColor.GREEN + " duel by "  + ChatColor.AQUA + player.getName() + ChatColor.GREEN + "! Type '/duel " + player.getName() + " accept' to accept. ");
    	    	Duel.pendinginvites.put(player, new DuelInvite(target, 2));
    			player.closeInventory();
    		}
    		event.setCancelled(true);
    	}
    	
    	if (inventory.getName().equals(ChatColor.BLUE + "Enchants")) {
    		ItemStack hold = player.getInventory().getItemInHand();
    		if (KitPvP.isSword(hold)) {
    			
        		int unbreakinglevel = hold.getEnchantmentLevel(Enchantment.DURABILITY);
        		int holdunbreakingworth;
        		
    			switch(unbreakinglevel) {
    			case 1: holdunbreakingworth = 50; break;
    			case 2: holdunbreakingworth = 100; break;
    			case 3: holdunbreakingworth = 200; break;
    			default: holdunbreakingworth = 0; break; 
    			}
    			
        		int sharplevel = hold.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
        		int holdsharpworth;
        		
    			switch(sharplevel) {
    			case 1: holdsharpworth = 100; break;
    			case 2: holdsharpworth = 200; break;
    			case 3: holdsharpworth = 400; break;
    			case 4: holdsharpworth = 800; break;
    			case 5: holdsharpworth = 1600; break;
    			default: holdsharpworth = 0; break; 
    			}
    			if (clicked.equals(inventory.getItem(11))) {
    				if (hold.getEnchantmentLevel(Enchantment.DAMAGE_ALL) < 1) {
  
    					int price = 100 - holdsharpworth;
    					if (Economy.getBalance(player) >= price) {
    						Economy.removeFromBalance(player, price);
    						hold.addEnchantment(Enchantment.DAMAGE_ALL, 1);
    						player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1F, 1F);
    						player.sendMessage(ChatColor.GREEN + "Your item has been successfully enchanted.");
    					}
    					else {
    	    				player.sendMessage(ChatColor.RED + "Transaction failed, insufficient balance.");
    	    				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    					}
    				}
    				else {
        				player.sendMessage(ChatColor.RED + "Transaction failed, you already have this or a stronger enchantment on this item.");
        				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    				}
    			}
    			if (clicked.equals(inventory.getItem(12))) {
    				if (hold.getEnchantmentLevel(Enchantment.DAMAGE_ALL) < 2) {
    					int price = 200 - holdsharpworth;
    					if (Economy.getBalance(player) >= price) {
    						Economy.removeFromBalance(player, price);
    						hold.addEnchantment(Enchantment.DAMAGE_ALL, 2);
    						player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1F, 1F);
    						player.sendMessage(ChatColor.GREEN + "Your item has been successfully enchanted.");
    					}
    					else {
    	    				player.sendMessage(ChatColor.RED + "Transaction failed, insufficient balance.");
    	    				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    					}
    				}
    				else {
        				player.sendMessage(ChatColor.RED + "Transaction failed, you already have this or a stronger enchantment on this item.");
        				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    				}
    			}
    			if (clicked.equals(inventory.getItem(13))) {
    				if (hold.getEnchantmentLevel(Enchantment.DAMAGE_ALL) < 3) {
    					int price = 400 - holdsharpworth;
    					if (Economy.getBalance(player) >= price) {
    						Economy.removeFromBalance(player, price);
    						hold.addEnchantment(Enchantment.DAMAGE_ALL, 3);
    						player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1F, 1F);
    						player.sendMessage(ChatColor.GREEN + "Your item has been successfully enchanted.");
    					}
    					else {
    	    				player.sendMessage(ChatColor.RED + "Transaction failed, insufficient balance.");
    	    				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    					}
    				}
    				else {
        				player.sendMessage(ChatColor.RED + "Transaction failed, you already have this or a stronger enchantment on this item.");
        				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    				}
    			}
    			if (clicked.equals(inventory.getItem(14))) {
    				if (hold.getEnchantmentLevel(Enchantment.DAMAGE_ALL) < 4) {
    					int price = 800 - holdsharpworth;
    					if (Economy.getBalance(player) >= price) {
    						Economy.removeFromBalance(player, price);
    						hold.addEnchantment(Enchantment.DAMAGE_ALL, 4);
    						player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1F, 1F);
    						player.sendMessage(ChatColor.GREEN + "Your item has been successfully enchanted.");
    					}
    					else {
    	    				player.sendMessage(ChatColor.RED + "Transaction failed, insufficient balance.");
    	    				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    					}
    				}
    				else {
        				player.sendMessage(ChatColor.RED + "Transaction failed, you already have this or a stronger enchantment on this item.");
        				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    				}
    			}
    			if (clicked.equals(inventory.getItem(15))) {
    				if (hold.getEnchantmentLevel(Enchantment.DAMAGE_ALL) < 5) {
    					int price = 1600 - holdsharpworth;
    					if (Economy.getBalance(player) >= price) {
    						Economy.removeFromBalance(player, price);
    						hold.addEnchantment(Enchantment.DAMAGE_ALL, 5);
    						player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1F, 1F);
    						player.sendMessage(ChatColor.GREEN + "Your item has been successfully enchanted.");
    					}
    					else {
    	    				player.sendMessage(ChatColor.RED + "Transaction failed, insufficient balance.");
    	    				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    					}
    				}
    				else {
        				player.sendMessage(ChatColor.RED + "Transaction failed, you already have this or a stronger enchantment on this item.");
        				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    				}
    			}
    			if (clicked.equals(inventory.getItem(21))) {
    				if (hold.getEnchantmentLevel(Enchantment.DURABILITY) < 1) {
    					int price = 50 - holdunbreakingworth;
    					if (Economy.getBalance(player) >= price) {
    						Economy.removeFromBalance(player, price);
    						hold.addEnchantment(Enchantment.DURABILITY, 1);
    						player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1F, 1F);
    						player.sendMessage(ChatColor.GREEN + "Your item has been successfully enchanted.");
    					}
    					else {
    	    				player.sendMessage(ChatColor.RED + "Transaction failed, insufficient balance.");
    	    				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    					}
    				}
    				else {
        				player.sendMessage(ChatColor.RED + "Transaction failed, you already have this or a stronger enchantment on this item.");
        				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    				}
    			}
    			if (clicked.equals(inventory.getItem(22))) {
    				if (hold.getEnchantmentLevel(Enchantment.DURABILITY) < 2) {
    					int price = 100 - holdunbreakingworth;
    					if (Economy.getBalance(player) >= price) {
    						Economy.removeFromBalance(player, price);
    						hold.addEnchantment(Enchantment.DURABILITY, 2);
    						player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1F, 1F);
    						player.sendMessage(ChatColor.GREEN + "Your item has been successfully enchanted.");
    					}
    					else {
    	    				player.sendMessage(ChatColor.RED + "Transaction failed, insufficient balance.");
    	    				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    					}
    				}
    				else {
        				player.sendMessage(ChatColor.RED + "Transaction failed, you already have this or a stronger enchantment on this item.");
        				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    				}
    			}
    			if (clicked.equals(inventory.getItem(23))) {
    				if (hold.getEnchantmentLevel(Enchantment.DURABILITY) < 3) {
    					int price = 200 - holdunbreakingworth;
    					if (Economy.getBalance(player) >= price) {
    						Economy.removeFromBalance(player, price);
    						hold.addEnchantment(Enchantment.DURABILITY, 3);
    						player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1F, 1F);
    						player.sendMessage(ChatColor.GREEN + "Your item has been successfully enchanted.");
    					}
    					else {
    	    				player.sendMessage(ChatColor.RED + "Transaction failed, insufficient balance.");
    	    				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    					}
    				}
    				else {
        				player.sendMessage(ChatColor.RED + "Transaction failed, you already have this or a stronger enchantment on this item.");
        				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    				}
    	    	}
    			
        		unbreakinglevel = hold.getEnchantmentLevel(Enchantment.DURABILITY);
        		
    			switch(unbreakinglevel) {
    			case 1: holdunbreakingworth = 50; break;
    			case 2: holdunbreakingworth = 100; break;
    			case 3: holdunbreakingworth = 200; break;
    			default: holdunbreakingworth = 0; break; 
    			}
    			
    			sharplevel = hold.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
        		
    			switch(sharplevel) {
    			case 1: holdsharpworth = 100; break;
    			case 2: holdsharpworth = 200; break;
    			case 3: holdsharpworth = 400; break;
    			case 4: holdsharpworth = 800; break;
    			case 5: holdsharpworth = 1600; break;
    			default: holdsharpworth = 0; break; 
    			}
    			
    			int holdfireworth;
    			int firelevel = hold.getEnchantmentLevel(Enchantment.FIRE_ASPECT);
    			switch(firelevel) {
    			case 1: holdfireworth = 500; break;
    			case 2: holdfireworth = 1000; break;
    			default: holdfireworth = 0; break; 
    			}
    			
    			
				ItemStack sharp1 = new ItemStack(Material.ENCHANTED_BOOK);
				ItemMeta sharp1meta = sharp1.getItemMeta();
				sharp1meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
				
				if (sharplevel < 1) {
					ArrayList<String> sharp1lore = new ArrayList<>();
					sharp1lore.add(ChatColor.DARK_GRAY + "Price: " + ChatColor.GOLD + "100$");
					sharp1meta.setLore(sharp1lore);
				}
				
				sharp1.setItemMeta(sharp1meta);
				
				ItemStack sharp2 = new ItemStack(Material.ENCHANTED_BOOK);
				ItemMeta sharp2meta = sharp2.getItemMeta();
				sharp2meta.addEnchant(Enchantment.DAMAGE_ALL, 2, true);
				
				if (sharplevel < 2) {
					ArrayList<String> sharp2lore = new ArrayList<>();
					int price = 200 - holdsharpworth;
					sharp2lore.add(ChatColor.DARK_GRAY + "Price: " + ChatColor.GOLD + price + "$");
					sharp2meta.setLore(sharp2lore);
				}
				
				sharp2.setItemMeta(sharp2meta);
				
				ItemStack sharp3 = new ItemStack(Material.ENCHANTED_BOOK);
				ItemMeta sharp3meta = sharp3.getItemMeta();
				sharp3meta.addEnchant(Enchantment.DAMAGE_ALL, 3, true);
				
				if (sharplevel < 3) {
					int price = 400 - holdsharpworth;
					ArrayList<String> sharp3lore = new ArrayList<>();
					sharp3lore.add(ChatColor.DARK_GRAY + "Price: " + ChatColor.GOLD + price + "$");
					sharp3meta.setLore(sharp3lore);
				}
				
				sharp3.setItemMeta(sharp3meta);
				
				ItemStack sharp4 = new ItemStack(Material.ENCHANTED_BOOK);
				ItemMeta sharp4meta = sharp4.getItemMeta();
				sharp4meta.addEnchant(Enchantment.DAMAGE_ALL, 4, true);
				
				if (sharplevel < 4) {
					int price = 800 - holdsharpworth;
					ArrayList<String> sharp4lore = new ArrayList<>();
					sharp4lore.add(ChatColor.DARK_GRAY + "Price: " + ChatColor.GOLD + price + "$");
					sharp4meta.setLore(sharp4lore);
				}
				
				sharp4.setItemMeta(sharp4meta);
				
				ItemStack sharp5 = new ItemStack(Material.ENCHANTED_BOOK);
				ItemMeta sharp5meta = sharp5.getItemMeta();
				sharp5meta.addEnchant(Enchantment.DAMAGE_ALL, 5, true);
				if (sharplevel < 5) {
					int price = 1600 - holdsharpworth;
					ArrayList<String> sharp5lore = new ArrayList<>();
					sharp5lore.add(ChatColor.DARK_GRAY + "Price: " + ChatColor.GOLD + price + "$");
					sharp5meta.setLore(sharp5lore);
				}
				
				
				sharp5.setItemMeta(sharp5meta);
				
				ItemStack unbreaking1 = new ItemStack(Material.ENCHANTED_BOOK);
				ItemMeta unbreaking1meta = unbreaking1.getItemMeta();
				unbreaking1meta.addEnchant(Enchantment.DURABILITY, 1, true);
				if (unbreakinglevel < 1) {
					int price = 50 - holdunbreakingworth;
					ArrayList<String> unbreaking1lore = new ArrayList<>();
					unbreaking1lore.add(ChatColor.DARK_GRAY + "Price: " + ChatColor.GOLD + price + "$");
					unbreaking1meta.setLore(unbreaking1lore);
				}
				unbreaking1.setItemMeta(unbreaking1meta);
				
				ItemStack unbreaking2 = new ItemStack(Material.ENCHANTED_BOOK);
				ItemMeta unbreaking2meta = unbreaking2.getItemMeta();
				unbreaking2meta.addEnchant(Enchantment.DURABILITY, 2, true);
				if (unbreakinglevel < 2) {
					int price = 100 - holdunbreakingworth;
					ArrayList<String> unbreaking2lore = new ArrayList<>();
					unbreaking2lore.add(ChatColor.DARK_GRAY + "Price: " + ChatColor.GOLD + price + "$");
					unbreaking2meta.setLore(unbreaking2lore);
				}
				unbreaking2.setItemMeta(unbreaking2meta);
				
				ItemStack unbreaking3 = new ItemStack(Material.ENCHANTED_BOOK);
				ItemMeta unbreaking3meta = unbreaking3.getItemMeta();
				unbreaking3meta.addEnchant(Enchantment.DURABILITY, 3, true);
				if (unbreakinglevel < 3) {
					int price = 200 - holdunbreakingworth;
					ArrayList<String> unbreaking3lore = new ArrayList<>();
					unbreaking3lore.add(ChatColor.DARK_GRAY + "Price: " + ChatColor.GOLD + price + "$");
					unbreaking3meta.setLore(unbreaking3lore);
				}
				unbreaking3.setItemMeta(unbreaking3meta);
				
				ItemStack fire1 = new ItemStack(Material.ENCHANTED_BOOK);
				ItemMeta fire1meta = fire1.getItemMeta();
				fire1meta.addEnchant(Enchantment.FIRE_ASPECT, 1, true);
				
				if (firelevel < 1) {
					ArrayList<String> fire1lore = new ArrayList<>();
					int price = 500 - holdfireworth;
					fire1lore.add(ChatColor.DARK_GRAY + "Price: " + ChatColor.GOLD + price + "$");
					fire1meta.setLore(fire1lore);
				}
				fire1.setItemMeta(fire1meta);
				
				ItemStack fire2 = new ItemStack(Material.ENCHANTED_BOOK);
				ItemMeta fire2meta = fire2.getItemMeta();
				fire2meta.addEnchant(Enchantment.FIRE_ASPECT, 2, true);
				
				if (firelevel < 2) {
					ArrayList<String> fire2lore = new ArrayList<>();
					int price = 1000 - holdfireworth;
					fire2lore.add(ChatColor.DARK_GRAY + "Price: " + ChatColor.GOLD + price + "$");
					fire2meta.setLore(fire2lore);
				}
				fire2.setItemMeta(fire2meta);
				
				inventory.setItem(11, sharp1);
				inventory.setItem(12, sharp2);
				inventory.setItem(13, sharp3);
				inventory.setItem(14, sharp4);
				inventory.setItem(15, sharp5);
				inventory.setItem(21, unbreaking1);
				inventory.setItem(22, unbreaking2);
				inventory.setItem(23, unbreaking3);
				inventory.setItem(30, fire1);
				inventory.setItem(32, fire2);
    			KitPvP.ingotmeta.setDisplayName(ChatColor.GOLD + ChatColor.BOLD.toString() + "Balance: " + ChatColor.AQUA + Economy.getBalance(player) + "$");
    			KitPvP.balanceingot.setItemMeta(KitPvP.ingotmeta);
				inventory.setItem(8, KitPvP.balanceingot);
    		}
    		
    		if (KitPvP.isArmor(hold)) {
    			
    			int protlevel = hold.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
        		int holdprotworth;
        		
    			switch(protlevel) {
    			case 1: holdprotworth = 150; break;
    			case 2: holdprotworth = 300; break;
    			case 3: holdprotworth = 600; break;
    			case 4: holdprotworth = 1200; break;
    			default: holdprotworth = 0; break; 
    			}
    			
    			int holdunbreakingworth;
    			int unbreakinglevel = hold.getEnchantmentLevel(Enchantment.DURABILITY);
    			switch(unbreakinglevel) {
    			case 1: holdunbreakingworth = 50; break;
    			case 2: holdunbreakingworth = 100; break;
    			case 3: holdunbreakingworth = 200; break;
    			default: holdunbreakingworth = 0; break; 
    			}
    			
    			if (clicked.equals(inventory.getItem(11))) {
    				if (protlevel < 1) {
  
    					int price = 150 - holdprotworth;
    					if (Economy.getBalance(player) >= price) {
    						Economy.removeFromBalance(player, price);
    						hold.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
    						player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1F, 1F);
    						player.sendMessage(ChatColor.GREEN + "Your item has been successfully enchanted.");
    					}
    					else {
    	    				player.sendMessage(ChatColor.RED + "Transaction failed, insufficient balance.");
    	    				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    					}
    				}
    				else {
        				player.sendMessage(ChatColor.RED + "Transaction failed, you already have this or a stronger enchantment on this item.");
        				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    				}
    			}
    			
    			if (clicked.equals(inventory.getItem(12))) {
    				if (protlevel < 2) {
  
    					int price = 300 - holdprotworth;
    					if (Economy.getBalance(player) >= price) {
    						Economy.removeFromBalance(player, price);
    						hold.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
    						player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1F, 1F);
    						player.sendMessage(ChatColor.GREEN + "Your item has been successfully enchanted.");
    					}
    					else {
    	    				player.sendMessage(ChatColor.RED + "Transaction failed, insufficient balance.");
    	    				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    					}
    				}
    				else {
        				player.sendMessage(ChatColor.RED + "Transaction failed, you already have this or a stronger enchantment on this item.");
        				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    				}
    			}
    			
    			if (clicked.equals(inventory.getItem(14))) {
    				if (protlevel < 3) {
  
    					int price = 600 - holdprotworth;
    					if (Economy.getBalance(player) >= price) {
    						Economy.removeFromBalance(player, price);
    						hold.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
    						player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1F, 1F);
    						player.sendMessage(ChatColor.GREEN + "Your item has been successfully enchanted.");
    					}
    					else {
    	    				player.sendMessage(ChatColor.RED + "Transaction failed, insufficient balance.");
    	    				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    					}
    				}
    				else {
        				player.sendMessage(ChatColor.RED + "Transaction failed, you already have this or a stronger enchantment on this item.");
        				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    				}
    			}
    			
    			if (clicked.equals(inventory.getItem(15))) {
    				if (protlevel < 4) {
  
    					int price = 1200 - holdprotworth;
    					if (Economy.getBalance(player) >= price) {
    						Economy.removeFromBalance(player, price);
    						hold.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
    						player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1F, 1F);
    						player.sendMessage(ChatColor.GREEN + "Your item has been successfully enchanted.");
    					}
    					else {
    	    				player.sendMessage(ChatColor.RED + "Transaction failed, insufficient balance.");
    	    				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    					}
    				}
    				else {
        				player.sendMessage(ChatColor.RED + "Transaction failed, you already have this or a stronger enchantment on this item.");
        				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    				}
    			}
    			
    			if (clicked.equals(inventory.getItem(21))) {
    				if (hold.getEnchantmentLevel(Enchantment.DURABILITY) < 1) {
    					int price = 50 - holdunbreakingworth;
    					if (Economy.getBalance(player) >= price) {
    						Economy.removeFromBalance(player, price);
    						hold.addEnchantment(Enchantment.DURABILITY, 1);
    						player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1F, 1F);
    						player.sendMessage(ChatColor.GREEN + "Your item has been successfully enchanted.");
    					}
    					else {
    	    				player.sendMessage(ChatColor.RED + "Transaction failed, insufficient balance.");
    	    				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    					}
    				}
    				else {
        				player.sendMessage(ChatColor.RED + "Transaction failed, you already have this or a stronger enchantment on this item.");
        				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    				}
    	    	}
    			if (clicked.equals(inventory.getItem(22))) {
    				if (hold.getEnchantmentLevel(Enchantment.DURABILITY) < 2) {
    					int price = 100 - holdunbreakingworth;
    					if (Economy.getBalance(player) >= price) {
    						Economy.removeFromBalance(player, price);
    						hold.addEnchantment(Enchantment.DURABILITY, 2);
    						player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1F, 1F);
    						player.sendMessage(ChatColor.GREEN + "Your item has been successfully enchanted.");
    					}
    					else {
    	    				player.sendMessage(ChatColor.RED + "Transaction failed, insufficient balance.");
    	    				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    					}
    				}
    				else {
        				player.sendMessage(ChatColor.RED + "Transaction failed, you already have this or a stronger enchantment on this item.");
        				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    				}
    	    	}
    			if (clicked.equals(inventory.getItem(23))) {
    				if (hold.getEnchantmentLevel(Enchantment.DURABILITY) < 3) {
    					int price = 200 - holdunbreakingworth;
    					if (Economy.getBalance(player) >= price) {
    						Economy.removeFromBalance(player, price);
    						hold.addEnchantment(Enchantment.DURABILITY, 3);
    						player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1F, 1F);
    						player.sendMessage(ChatColor.GREEN + "Your item has been successfully enchanted.");
    					}
    					else {
    	    				player.sendMessage(ChatColor.RED + "Transaction failed, insufficient balance.");
    	    				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    					}
    				}
    				else {
        				player.sendMessage(ChatColor.RED + "Transaction failed, you already have this or a stronger enchantment on this item.");
        				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
    				}
    	    	}
    			
        		unbreakinglevel = hold.getEnchantmentLevel(Enchantment.DURABILITY);
        		
    			switch(unbreakinglevel) {
    			case 1: holdunbreakingworth = 50; break;
    			case 2: holdunbreakingworth = 100; break;
    			case 3: holdunbreakingworth = 200; break;
    			default: holdunbreakingworth = 0; break; 
    			}
    			
    			protlevel = hold.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
        		
    			switch(protlevel) {
    			case 1: holdprotworth = 150; break;
    			case 2: holdprotworth = 300; break;
    			case 3: holdprotworth = 600; break;
    			case 4: holdprotworth = 1200; break;
    			default: holdprotworth = 0; break; 
    			}
    			
				ItemStack prot1 = new ItemStack(Material.ENCHANTED_BOOK);
				ItemMeta prot1meta = prot1.getItemMeta();
				prot1meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
				if (protlevel < 1) {
					int price = 150 - holdprotworth;
					ArrayList<String> prot1lore = new ArrayList<>();
					prot1lore.add(ChatColor.DARK_GRAY + "Price: " + ChatColor.GOLD + price + "$");
					prot1meta.setLore(prot1lore);
				}
				prot1.setItemMeta(prot1meta);
				
				ItemStack prot2 = new ItemStack(Material.ENCHANTED_BOOK);
				ItemMeta prot2meta = prot2.getItemMeta();
				prot2meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
				if (protlevel < 2) {
					int price = 300 - holdprotworth;
					ArrayList<String> prot2lore = new ArrayList<>();
					prot2lore.add(ChatColor.DARK_GRAY + "Price: " + ChatColor.GOLD + price + "$");
					prot2meta.setLore(prot2lore);
				}
				prot2.setItemMeta(prot2meta);
				
				ItemStack prot3 = new ItemStack(Material.ENCHANTED_BOOK);
				ItemMeta prot3meta = prot3.getItemMeta();
				prot3meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true);
				if (protlevel < 3) {
					int price = 600 - holdprotworth;
					ArrayList<String> prot3lore = new ArrayList<>();
					prot3lore.add(ChatColor.DARK_GRAY + "Price: " + ChatColor.GOLD + price + "$");
					prot3meta.setLore(prot3lore);
				}
				prot3.setItemMeta(prot3meta);
				
				ItemStack prot4 = new ItemStack(Material.ENCHANTED_BOOK);
				ItemMeta prot4meta = prot4.getItemMeta();
				prot4meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
				if (protlevel < 4) {
					int price = 1200 - holdprotworth;
					ArrayList<String> prot4lore = new ArrayList<>();
					prot4lore.add(ChatColor.DARK_GRAY + "Price: " + ChatColor.GOLD + price + "$");
					prot4meta.setLore(prot4lore);
				}
				prot4.setItemMeta(prot4meta);
				
				
				
				ItemStack unbreaking1 = new ItemStack(Material.ENCHANTED_BOOK);
				ItemMeta unbreaking1meta = unbreaking1.getItemMeta();
				unbreaking1meta.addEnchant(Enchantment.DURABILITY, 1, true);
				if (unbreakinglevel < 1) {
					int price = 50 - holdunbreakingworth;
					ArrayList<String> unbreaking1lore = new ArrayList<>();
					unbreaking1lore.add(ChatColor.DARK_GRAY + "Price: " + ChatColor.GOLD + price + "$");
					unbreaking1meta.setLore(unbreaking1lore);
				}
				unbreaking1.setItemMeta(unbreaking1meta);
				
				ItemStack unbreaking2 = new ItemStack(Material.ENCHANTED_BOOK);
				ItemMeta unbreaking2meta = unbreaking2.getItemMeta();
				unbreaking2meta.addEnchant(Enchantment.DURABILITY, 2, true);
				if (unbreakinglevel < 2) {
					int price = 100 - holdunbreakingworth;
					ArrayList<String> unbreaking2lore = new ArrayList<>();
					unbreaking2lore.add(ChatColor.DARK_GRAY + "Price: " + ChatColor.GOLD + price + "$");
					unbreaking2meta.setLore(unbreaking2lore);
				}
				unbreaking2.setItemMeta(unbreaking2meta);
				
				ItemStack unbreaking3 = new ItemStack(Material.ENCHANTED_BOOK);
				ItemMeta unbreaking3meta = unbreaking3.getItemMeta();
				unbreaking3meta.addEnchant(Enchantment.DURABILITY, 3, true);
				if (unbreakinglevel < 3) {
					int price = 200 - holdunbreakingworth;
					ArrayList<String> unbreaking3lore = new ArrayList<>();
					unbreaking3lore.add(ChatColor.DARK_GRAY + "Price: " + ChatColor.GOLD + price + "$");
					unbreaking3meta.setLore(unbreaking3lore);
				}
				unbreaking3.setItemMeta(unbreaking3meta);
				
				
    			
				inventory.setItem(11, prot1);
				inventory.setItem(12, prot2);
				inventory.setItem(14, prot3);
				inventory.setItem(15, prot4);
				inventory.setItem(21, unbreaking1);
				inventory.setItem(22, unbreaking2);
				inventory.setItem(23, unbreaking3);
    		}
    		
    		
    		event.setCancelled(true);
    		
			KitPvP.ingotmeta.setDisplayName(ChatColor.GOLD + ChatColor.BOLD.toString() + "Balance: " + ChatColor.AQUA + Economy.getBalance(player) + "$");
			KitPvP.balanceingot.setItemMeta(KitPvP.ingotmeta);
			inventory.setItem(8, KitPvP.balanceingot);
			if (player.getItemInHand() != null) {
				inventory.setItem(0, player.getItemInHand());
			}
    	}
    }
    
    @EventHandler
    public void weatherChange(WeatherChangeEvent e) {
    	e.setCancelled(true);
    }
    
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		if (plugin.getConfig().getLong("Players." + event.getPlayer().getUniqueId() + ".punishments.muteexpire") > System.currentTimeMillis()) {
			event.setCancelled(true);
			int secondsleft = (int) ((plugin.getConfig().getLong("Players." + event.getPlayer().getUniqueId() + ".punishments.muteexpire") - System.currentTimeMillis()) / 1000);
			event.getPlayer().sendMessage(ChatColor.RED + "You are muted for " + secondsleft + " more seconds.");
		}
	}
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
    	if (e.getEntity() instanceof Player && e.getCause().equals(DamageCause.FALL)) {
    		e.setCancelled(true);
    	}
    }
}
