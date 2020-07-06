package me.Khajiitos.KitPvP;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import org.bukkit.scoreboard.Scoreboard;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;


public class KitPvP extends JavaPlugin {
	// 02.06.2020 - 1266 lines
	// 03.06.2020 - 1571 lines
	// 04.06.2020 - 1938 lines
	// 05.06.2020 - 1983 lines
	// 07.06.2020 - 2202 lines
	// 09.06.2020 - 2751 lines
	private int cooldowntime = 30;
	
	public static Scoreboard sb;
	
	public static int sharp1price;
	public static int sharp2price;
	public static int sharp3price;
	public static int sharp4price;
	public static int sharp5price;
	
	private String prefix = ChatColor.DARK_GRAY + "≫ " + ChatColor.RESET;
	
	public static ItemStack duelclassic = new ItemStack(Material.IRON_SWORD);
	public static ItemMeta classicmeta = duelclassic.getItemMeta();
	
	public static Potion pot = new Potion(PotionType.INSTANT_HEAL);
	
	public static ItemStack duelnodebuff = pot.toItemStack(1);
	public static ItemMeta nodebuffmeta = duelnodebuff.getItemMeta();
	
	public static ItemStack invsaver = new ItemStack(Material.ENCHANTED_BOOK);
	public static ItemMeta savermeta = invsaver.getItemMeta();
	
	public static ItemStack rod = new ItemStack(Material.FISHING_ROD);
	public static ItemMeta rodmeta = rod.getItemMeta();
	
	public static ItemStack empty = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
	public static ItemMeta emptymeta = empty.getItemMeta();
	
	public static ItemStack balanceingot = new ItemStack(Material.GOLD_INGOT);
	public static ItemMeta ingotmeta = balanceingot.getItemMeta();
	
	public static ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
	public static ItemMeta swordmeta = sword.getItemMeta();
	
	public static ItemStack autokittrue = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
	public static ItemMeta aktmeta = autokittrue.getItemMeta();
	public static ItemStack autokitfalse = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
	public static ItemMeta akfmeta = autokitfalse.getItemMeta();
	
	public static ItemStack noenchants = new ItemStack(Material.PAPER, 1);
	public static ItemMeta noenchantsmeta = noenchants.getItemMeta();
	
	
	public static ItemStack steak = new ItemStack(Material.COOKED_BEEF, 1);
	public static ItemMeta smeta = steak.getItemMeta();
	public static ItemStack pork = new ItemStack(Material.GRILLED_PORK, 1);
	public static ItemMeta pmeta = pork.getItemMeta();
	public static ItemStack carrot = new ItemStack(Material.GOLDEN_CARROT, 1);
	public static ItemMeta cmeta = carrot.getItemMeta();
	
	@Override
	public void onEnable() {
		
		getServer().getPluginManager().registerEvents(new Listeners(), this);
		getServer().getPluginManager().registerEvents(new EloSystem(), this);
		getServer().getPluginManager().registerEvents(new Protection(), this);
		getServer().getPluginManager().registerEvents(new JoinMessage(), this);
		getServer().getPluginManager().registerEvents(new Ranks(), this);
		getServer().getPluginManager().registerEvents(new StaffChat(), this);
		getServer().getPluginManager().registerEvents(new Duel(), this);
		loadConfig();
		
		sb = Bukkit.getScoreboardManager().getMainScoreboard();
		
		noenchantsmeta.setDisplayName(ChatColor.RED + "No available enchants");
		ArrayList<String> noenchantslore = new ArrayList<>();
		noenchantslore.add(ChatColor.GRAY + "There are no available enchants for this item.");
		noenchantsmeta.setLore(noenchantslore);
		noenchants.setItemMeta(noenchantsmeta);
		
		aktmeta.setDisplayName(ChatColor.GRAY + ChatColor.BOLD.toString() + "AutoKit: " + ChatColor.GREEN + "Enabled");
		akfmeta.setDisplayName(ChatColor.GRAY + ChatColor.BOLD.toString() + "AutoKit: " + ChatColor.RED + "Disabled");
		autokittrue.setItemMeta(aktmeta);
		autokitfalse.setItemMeta(akfmeta);
		
		
		Listeners.savermetaitem.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "Inventory Saver");
		Listeners.invsaveritem.setItemMeta(Listeners.savermetaitem);
		
		smeta.setDisplayName(ChatColor.GRAY + ChatColor.BOLD.toString() + "Food: " + ChatColor.WHITE + "Steak");
		pmeta.setDisplayName(ChatColor.GRAY + ChatColor.BOLD.toString() + "Food: " + ChatColor.WHITE + "Cooked Porkchop");
		cmeta.setDisplayName(ChatColor.GRAY + ChatColor.BOLD.toString() + "Food: " + ChatColor.WHITE + "Golden Carrot");
		emptymeta.setDisplayName(" ");
		swordmeta.setDisplayName(ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "Diamond Sword");
		ArrayList<String> swordlore = new ArrayList<>();
		swordlore.add(ChatColor.DARK_GRAY + "Price: " + ChatColor.GOLD + "100$");
		swordmeta.setLore(swordlore);
		ArrayList<String> invsaverlore = new ArrayList<>();
		savermeta.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "Inventory Saver");
		invsaverlore.add(ChatColor.DARK_GRAY + "Price: " + ChatColor.GOLD + "1000$");
		savermeta.setLore(invsaverlore);
		
		ArrayList<String> rodlore = new ArrayList<>();
		rodmeta.setDisplayName(ChatColor.GRAY + ChatColor.BOLD.toString() + "Fishing Rod");
		rodlore.add(ChatColor.DARK_GRAY + "Price: " + ChatColor.GOLD + "100$");
		rodmeta.setLore(rodlore);
		
		pot.setSplash(true);
		nodebuffmeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		
		classicmeta.setDisplayName(ChatColor.GREEN + "Classic Duel");
		nodebuffmeta.setDisplayName(ChatColor.GREEN + "NoDebuff Duel");
		
		ingotmeta.setDisplayName(ChatColor.GOLD + ChatColor.BOLD.toString() + "Balance: " + ChatColor.AQUA + "Unknown");
		steak.setItemMeta(smeta);
		pork.setItemMeta(pmeta);
		carrot.setItemMeta(cmeta);
		empty.setItemMeta(emptymeta);
		sword.setItemMeta(swordmeta);
		balanceingot.setItemMeta(ingotmeta);
		invsaver.setItemMeta(savermeta);
		rod.setItemMeta(rodmeta);
		duelclassic.setItemMeta(classicmeta);
		duelnodebuff.setItemMeta(nodebuffmeta);
		
		sharp1price = 100;
		sharp2price = 200;
		sharp3price = 400;
		sharp4price = 800;
		sharp5price = 1600;
		
		HealthIndicator.register();
		Announcer.start();
		
		Globals.everysecloop();
		
		//Ranks.defaultrank = KitPvP.sb.registerNewTeam("default");
		if (KitPvP.sb.getTeam("VIP") == null)
			Ranks.viprank = KitPvP.sb.registerNewTeam("VIP");
		if (KitPvP.sb.getTeam("Twitch") == null)
			Ranks.twitchrank = KitPvP.sb.registerNewTeam("Twitch");
		if (KitPvP.sb.getTeam("Builder") == null)
			Ranks.builderrank = KitPvP.sb.registerNewTeam("Builder");
		if (KitPvP.sb.getTeam("Owner") == null)
			Ranks.ownerrank = KitPvP.sb.registerNewTeam("Owner");
			
		//Ranks.setup();
	}
	
	@Override
	public void onDisable() {
		//Ranks.reverse();
	}
	
	public void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	
	public static boolean isSword(ItemStack is) {
		return is.getType().name().contains("SWORD");
	}
	public static boolean isArmor(ItemStack is) {
		if (is.getType().name().contains("HELMET") || is.getType().name().contains("CHESTPLATE") || is.getType().name().contains("LEGGINGS") || is.getType().name().contains("BOOTS")) {
			return true;
		}
		else {
			return false;
		}
	}
	public static boolean isBow(ItemStack is) {
		return is.getType().name().contains("BOW");
	}
	
	
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	
    	if (command.getName().equalsIgnoreCase("crash")) {
    		if (sender.isOp()) {
	    		if (args.length > 0) {
	    			Player target = Bukkit.getPlayerExact(args[0]);
	    			if (target != null) {
	    				Location loc = target.getLocation();
	    				for (int i = 0; i < 25; i++) {
		    				PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.FLAME, true, (int) loc.getX(), (int) loc.getY(), (int) loc.getZ(), 10, 10, 10, 1, 1000000000);
		    				CraftPlayer crafttarget = (CraftPlayer) target;
		    				crafttarget.getHandle().playerConnection.sendPacket(packet);
	    				}
	    				sender.sendMessage(ChatColor.YELLOW + "You have crashed " + target.getName() + "'s game.");
	    			} else {
	    				sender.sendMessage(ChatColor.RED + "This player does not exist.");
	    			}
	    		} else {
	    			sender.sendMessage(ChatColor.RED + "Usage: /crash <username>");
	    		}
    		} else {
    			sender.sendMessage(ChatColor.RED + "No permission.");
    		}
    	}
    	
    	if (command.getName().equalsIgnoreCase("vanish")) {
    		if (sender instanceof Player) {
    			Player player = (Player) sender;
    			if (player.isOp()) {
    				if (!Globals.vanishedplayers.contains(player)) {
        				for (Player p : Bukkit.getOnlinePlayers()) {
        					if (!p.isOp()) {
        						p.hidePlayer(player);
        					}
        				}
        				Globals.vanishedplayers.add(player);
	    				player.sendMessage(ChatColor.GREEN + "You have successfully vanished.");
    				} else {
        				for (Player p : Bukkit.getOnlinePlayers()) {
        					p.showPlayer(player);
        				}
        				Globals.vanishedplayers.remove(player);
        				player.sendMessage(ChatColor.GREEN + "You have successfully unvanished.");
    				}
    			} else {
    				player.sendMessage(ChatColor.RED + "No permission.");
    			}
    		}
    	}
    	
    	if (command.getName().equalsIgnoreCase("elo")) {
    		if (sender instanceof Player) {
    			Player player = (Player) sender;
    			int playerelo = getConfig().getInt("Elo." + player.getUniqueId());
    			String elo = Integer.toString(playerelo);
    			player.sendMessage(prefix + ChatColor.GREEN + "Your ELO: " + ChatColor.AQUA + elo);
    		}
    		else {
    			sender.sendMessage(ChatColor.RED + "Only players can use this command!");
    		}
    	}
    	if (command.getName().equalsIgnoreCase("duel")) {
    		
    		Player player = (Player) sender;
    		
    		
    		if (Duel.duelingplayers.containsKey(player)) {
    			player.sendMessage(ChatColor.RED + "You are already in a duel!");
    			return false;
    		}
    		
    		if (args.length < 1) {
    			sender.sendMessage(ChatColor.DARK_RED + "Usage: /duel <username>");
    			return false;
    		}
    		if (!(sender instanceof Player)) {
    			sender.sendMessage(ChatColor.RED + "Only players can use this command.");
    			return false;
    		}
    		
    		Player target = Bukkit.getPlayerExact(args[0]);
    		
    		if (player == target) {
    			player.sendMessage(ChatColor.RED + "You can't duel yourself.");
    			return false;
    		}
    		
    		if (Arena.getFreeArenas().isEmpty()) {
    			player.sendMessage(ChatColor.RED + "There are no free arenas. Try again later.");
    			return false;
    		}
    		
    		if (target != null) {
    			
    			if (args.length == 1) {
    				
    				Inventory selection = Bukkit.createInventory(player, 9, ChatColor.YELLOW + "Dueling: " + ChatColor.AQUA + target.getName());
    				for (int i = 0; i < selection.getSize(); i++) {
    					selection.setItem(i, KitPvP.empty);
    				}
    				selection.setItem(1, KitPvP.duelclassic);
    				selection.setItem(2, duelnodebuff);
    				
    				player.openInventory(selection);
    				
    			} else {
        			if (args[1].equalsIgnoreCase("accept")) {
        				boolean isSenderInvited = false;
        				
        				for (DuelInvite di : Duel.pendinginvites.values()) {
        					if (di.getPlayer().equals(player)) {
        						isSenderInvited = true;
        						break;
        					}
        				}
        				
        				if (isSenderInvited) {
        					Duel.startDuel(player, target, 1, Duel.pendinginvites.get(target).getMode());
        					Duel.pendinginvites.remove(target);
        					Duel.pendinginvites.remove(player);
        				} else {
        					player.sendMessage(ChatColor.RED + "This person hasn't invited you to a duel.");
        				}
        			}
    			}
    		}
    	}
    	
    	if (command.getName().equalsIgnoreCase("kit")) {
    		
	        if (Duel.duelingplayers.containsKey((Player) sender)) {
	        	sender.sendMessage(ChatColor.RED + "You can't use this command in a duel.");
	        	return false;
	        }
    		
    		if (sender instanceof Player) {
    			Player player = (Player) sender;
    			if (args.length == 1) {
    				if (args[0].equalsIgnoreCase("default")) {
    					if (Globals.kitdefaultcooldown.containsKey(player.getUniqueId())) {
    						long secondsleft = ((Globals.kitdefaultcooldown.get(player.getUniqueId()) / 1000) + cooldowntime) - (System.currentTimeMillis() / 1000);
    						if (secondsleft > 0) {
    							player.sendMessage(ChatColor.RED + "You can't use this command for " + ChatColor.AQUA + secondsleft + ChatColor.RED + " more seconds.");
    						} else {
        						Kits.giveKitDefault(player);
        						Globals.kitdefaultcooldown.put(player.getUniqueId(), System.currentTimeMillis());
    						}
    					} else {
    						Kits.giveKitDefault(player);
    						Globals.kitdefaultcooldown.put(player.getUniqueId(), System.currentTimeMillis());
    					}
    				}
    				else if (args[0].equalsIgnoreCase("archer")) {
        					Kits.giveKitArcher(player);
        					Globals.kitdefaultcooldown.put(player.getUniqueId(), System.currentTimeMillis());
    				}
    				else {
    					player.sendMessage(ChatColor.RED + "Unknown kit name. Available kits: default, archer");
    				}
    			} else {
    				player.sendMessage(ChatColor.RED + "Available kits: default, archer");
    			}
    			
    		} else {
    			sender.sendMessage(ChatColor.RED + "You are not a player.");
    		}
    		
    	}
    	
    	if (command.getName().equalsIgnoreCase("invsee")) {
    		if (sender.isOp()) {
    			if (sender instanceof Player) {
    				Player player = (Player) sender;
    				if (args.length > 0) {
    					Player target = Bukkit.getPlayerExact(args[0]);
    					if (target == player) {
    						player.sendMessage(ChatColor.RED + "You can already see your inventory lol!");
    						return false;
    					}
    					if (target != null) {
    						player.openInventory(target.getInventory());
    					} else {
    						player.sendMessage(ChatColor.RED + "Usage: /invsee <username>");
    					}
    				} else {
    					player.sendMessage(ChatColor.RED + "Usage: /invsee <username>");
    				}
    			}
    		} else {
    			sender.sendMessage(ChatColor.RED + "No permission.");
    		}
    	}
    	
    	if (command.getName().equalsIgnoreCase("ecsee") || command.getName().equalsIgnoreCase("enderchestsee")) {
    		if (sender.isOp()) {
    			if (sender instanceof Player) {
    				Player player = (Player) sender;
    				if (args.length > 0) {
    					Player target = Bukkit.getPlayerExact(args[0]);
    					if (target == player) {
    						player.sendMessage(ChatColor.RED + "You can use /ec or /enderchest to see your enderchest!");
    						return false;
    					}
    					if (target != null) {
    						player.openInventory(target.getEnderChest());
    					} else {
    						player.sendMessage(ChatColor.RED + "Usage: /ecsee <username>");
    					}
    				} else {
    					player.sendMessage(ChatColor.RED + "Usage: /ecsee <username>");
    				}
    			}
    		} else {
    			sender.sendMessage(ChatColor.RED + "No permission.");
    		}
    	}
    	
    	if (command.getName().equalsIgnoreCase("walkspeed")) {
    		if (sender.isOp()) {
    			if (args.length > 0) {
    				int speed;
    				try {
    					speed = Integer.parseInt(args[0]);
    				} catch(Exception e) {
    					sender.sendMessage(ChatColor.RED + "You must input a valid number.");
    					return false;
    				}
    				if (sender instanceof Player) {
    					Player player = (Player) sender;
    					if (speed > 10) {
    						player.sendMessage(ChatColor.RED + "The speed can't be higher than 10.");
    						return false;
    					}
    					
    					float newspeed = ((float) speed) / 10;
    					player.setWalkSpeed(newspeed);
    					player.sendMessage(ChatColor.GREEN + "Set your walk speed to " + speed);
    				}
    			} else {
    				sender.sendMessage(ChatColor.RED + "Usage: /walkspeed <number>");
    			}
    		} else {
    			sender.sendMessage(ChatColor.RED + "No permission.");
    		}
    	}
    	
    	if (command.getName().equalsIgnoreCase("flyspeed")) {
    		if (sender.isOp()) {
    			if (args.length > 0) {
    				int speed;
    				try {
    					speed = Integer.parseInt(args[0]);
    				} catch(Exception e) {
    					sender.sendMessage(ChatColor.RED + "You must input a valid number.");
    					return false;
    				}
    				if (sender instanceof Player) {
    					Player player = (Player) sender;
    					if (speed > 10) {
    						player.sendMessage(ChatColor.RED + "The speed can't be higher than 10.");
    						return false;
    					}
    					
    					float newspeed = ((float) speed) / 10;
    					player.setFlySpeed(newspeed);
    					player.sendMessage(ChatColor.GREEN + "Set your flying speed to " + speed);
    				}
    			} else {
    				sender.sendMessage(ChatColor.RED + "Usage: /flyspeed <number>");
    			}
    		} else {
    			sender.sendMessage(ChatColor.RED + "No permission.");
    		}
    	}
    	
    	if (command.getName().equalsIgnoreCase("ec") || command.getName().equalsIgnoreCase("enderchest")) {
    		
	        if (Duel.duelingplayers.containsKey((Player) sender)) {
	        	sender.sendMessage(ChatColor.RED + "You can't use this command in a duel.");
	        	return false;
	        }
	        
    		if (sender instanceof Player) {
    			Player player = (Player) sender;
    	        if (Globals.combattaggedplayers.containsKey(player)) {
    	        	player.sendMessage(ChatColor.RED + "You can't use this command while combat tagged.");
    	        	return false;
    	        }
    			player.openInventory(player.getEnderChest());
    		} else {
    			sender.sendMessage(ChatColor.RED + "You are not a player.");
    		}
    		
    	}
    	
    	if (command.getName().equalsIgnoreCase("preferences")) {
    		if (sender instanceof Player) {
    			Player player = (Player) sender;
    			Inventory prefinv = Bukkit.createInventory(player, 9, ChatColor.RED + "Preferences");
    			ItemStack item0 = getConfig().getBoolean("Players." + "preferences." + player.getUniqueId() + ".autokit") ? autokittrue : autokitfalse;
    			ItemStack item1;
    			
    			switch (getConfig().getInt("Players." + "preferences." + player.getUniqueId() + ".food")) {
    			case 0: item1 = steak; break;
    			case 1: item1 = pork; break;
    			case 2: item1 = carrot; break;
    			default: item1 = steak; break;
    			}
    			
    			prefinv.setItem(0, item0);
    			prefinv.setItem(1, item1);
    			player.openInventory(prefinv);
    		}
    	}
    	if (command.getName().equalsIgnoreCase("gamemode") || command.getName().equalsIgnoreCase("gm")) {
    		if (sender instanceof Player) {
    			Player player = (Player) sender;
    			if (player.isOp()) {
	    			if (args.length == 1) {
	    				if (args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("s")) {
	    					player.setGameMode(GameMode.SURVIVAL);
	    					player.sendMessage(prefix + ChatColor.GREEN + "Set your gamemode to survival.");
	    				}
	    				else if (args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("c")) {
	    					player.setGameMode(GameMode.CREATIVE);
	    					player.sendMessage(prefix + ChatColor.GREEN + "Set your gamemode to creative.");
	    				}
	    				else if (args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("a")) {
	    					player.setGameMode(GameMode.ADVENTURE);
	    					player.sendMessage(prefix + ChatColor.GREEN + "Set your gamemode to adventure.");
	    				}
	    				else if (args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("sp")) {
	    					player.setGameMode(GameMode.SPECTATOR);
	    					player.sendMessage(prefix + ChatColor.GREEN + "Set your gamemode to spectator.");
	    				}
	    				else {
	    					player.sendMessage(ChatColor.RED + "Usage: /gamemode <0, 1, 2, 3>");
	    				}
	    		
	    			}
	    			else if (args.length == 2) {
	    				Player target = Bukkit.getPlayerExact(args[1]);
	    				if (target != null) {
		    				if (args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("s")) {
		    					target.setGameMode(GameMode.SURVIVAL);
		    					player.sendMessage(prefix + ChatColor.GREEN + "Set " + target.getName() + "'s gamemode to survival.");
		    				}
		    				else if (args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("c")) {
		    					target.setGameMode(GameMode.CREATIVE);
		    					player.sendMessage(prefix + ChatColor.GREEN + "Set " + target.getName() + "'s gamemode to creative.");
		    				}
		    				else if (args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("a")) {
		    					target.setGameMode(GameMode.ADVENTURE);
		    					player.sendMessage(prefix + ChatColor.GREEN + "Set " + target.getName() + "'s gamemode to adventure.");
		    				}
		    				else if (args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("sp")) {
		    					target.setGameMode(GameMode.SPECTATOR);
		    					player.sendMessage(prefix + ChatColor.GREEN + "Set " + target.getName() + "'s gamemode to spectator.");
		    				}
		    				else {
		    					player.sendMessage(ChatColor.RED + "Usage: /gamemode <0, 1, 2, 3> <username>");
		    				}
	    					
	    				} else {
	    					player.sendMessage(ChatColor.RED + "This player is offline.");
	    				}
	    			}
	    			else {
	    				player.sendMessage(ChatColor.RED + "Usage: /gamemode <0, 1, 2, 3> <username>");
	    			}
    			}
    			else {
    				player.sendMessage(ChatColor.RED + "No permission.");
    			}
    		}
    	}
    	
    	if (command.getName().equalsIgnoreCase("spawn")) {
    		
	        if (Duel.duelingplayers.containsKey((Player) sender)) {
	        	sender.sendMessage(ChatColor.RED + "You can't use this command in a duel.");
	        	return false;
	        }
	        
    		if (sender instanceof Player) {
    			Player player = (Player) sender;
        		if (Globals.combattaggedplayers.containsKey(player)) {
        			sender.sendMessage(ChatColor.RED + "You can't use this command while combat tagged.");
        			return false;
        		}
    			Location loc = getServer().getWorld("world").getSpawnLocation();
    			loc.setX(loc.getX() + 0.5);
    			loc.setZ(loc.getZ() + 0.5);
    			player.teleport(loc);
    			player.sendMessage(prefix + ChatColor.DARK_GREEN + "Teleporting to spawn.");
    		}
    	}
    	
    	if (command.getName().equalsIgnoreCase("shop")) {
    		if (sender instanceof Player) {
    	        if (Duel.duelingplayers.containsKey((Player) sender)) {
    	        	sender.sendMessage(ChatColor.RED + "You can't use this command in a duel.");
    	        	return false;
    	        }
    			Player player = (Player) sender;
    			ingotmeta.setDisplayName(ChatColor.GOLD + ChatColor.BOLD.toString() + "Balance: " + ChatColor.AQUA + Economy.getBalance(player) + "$");
    			balanceingot.setItemMeta(ingotmeta);
    			
    			Inventory shop = Bukkit.createInventory(player, 27, ChatColor.LIGHT_PURPLE + "Shop");
    			for (int i = 0; i < shop.getSize(); i++) {
    				if (i == 11 || i == 13 || i == 15 || i == 0) {
    					continue;
    				}
    				shop.setItem(i, empty);
    			}
    			
    			shop.setItem(11, sword);
    			shop.setItem(15, invsaver);
    			shop.setItem(13, rod);
    			shop.setItem(0, balanceingot);
    			player.openInventory(shop);
    		}
    	}
    	
    	if (command.getName().equalsIgnoreCase("setrank")) {
    		if (sender.isOp()) {
	    		if (args.length == 2) {
	    			Player target = Bukkit.getPlayerExact(args[0]);
	    			if (target != null) {
	    				if (args[1].equalsIgnoreCase("Owner")) {
	    					Ranks.setRank(target, "Owner");
	    					sender.sendMessage(ChatColor.GREEN + "Successfully set the rank of " + target.getName() + " to " + args[1] + ".");
	    				}
	    				else if (args[1].equalsIgnoreCase("Builder")) {
	    					Ranks.setRank(target, "Builder");
	    					sender.sendMessage(ChatColor.GREEN + "Successfully set the rank of " + target.getName() + " to " + args[1] + ".");
	    				}
	    				else if (args[1].equalsIgnoreCase("default")) {
	    					Ranks.setRank(target, "default");
	    					sender.sendMessage(ChatColor.GREEN + "Successfully set the rank of " + target.getName() + " to " + args[1] + ".");
	    				}
	    				else if (args[1].equalsIgnoreCase("VIP")) {
	    					Ranks.setRank(target, "VIP");
	    					sender.sendMessage(ChatColor.GREEN + "Successfully set the rank of " + target.getName() + " to " + args[1] + ".");
	    				}
	    				else if (args[1].equalsIgnoreCase("Twitch")) {
	    					Ranks.setRank(target, "Twitch");
	    					sender.sendMessage(ChatColor.GREEN + "Successfully set the rank of " + target.getName() + " to " + args[1] + ".");
	    				}
	    				else {
	    					sender.sendMessage(ChatColor.RED + "This rank does not exist.");
	    				}
	    			} else {
	    				sender.sendMessage(ChatColor.RED + "This player is invalid or offline.");
	    			}
	    		} else {
	    			sender.sendMessage(ChatColor.RED + "Usage: /setrank <username> <rank>");
	    		}
    		} else {
    			sender.sendMessage(ChatColor.RED + "No permission.");
    		}
    	}
    	if (command.getName().equalsIgnoreCase("bc") || command.getName().equalsIgnoreCase("broadcast")) {
    		if (sender.isOp()) {
    			String message = "";
    			for (String part : args) {
    			    if (message != "") message += " ";
    			    message += part;
    			}
    			Bukkit.broadcastMessage(ChatColor.GRAY + ChatColor.BOLD.toString() + "[" + ChatColor.GREEN + ChatColor.BOLD + "Broadcast" + ChatColor.GRAY + ChatColor.BOLD.toString() + "] " + ChatColor.RESET + message);
    		} else {
    			sender.sendMessage(ChatColor.RED + "No permission.");
    		}
    	}
    	
    	if (command.getName().equalsIgnoreCase("money") || command.getName().equalsIgnoreCase("m") || command.getName().equalsIgnoreCase("balance") || command.getName().equalsIgnoreCase("bal")) {
    		if (args.length == 0) {
        		if (sender instanceof Player) {
        			Player player = (Player) sender;
        			player.sendMessage(ChatColor.DARK_GREEN + "Your balance: " + ChatColor.GOLD + Economy.getBalance(player) + "$");
        		}
    		} else {
    			Player target = Bukkit.getPlayerExact(args[0]);
    			if (target != null) {
    				sender.sendMessage(ChatColor.DARK_GREEN + target.getName() + "'s balance: " + ChatColor.GOLD + Economy.getBalance(target) + "$");
    			} else {
    				sender.sendMessage(ChatColor.RED + "This player is offline.");
    			}
    		}
    	}
    	
    	if (command.getName().equalsIgnoreCase("setmoney")) {
    		if (sender.isOp()) {
    			if (args.length == 2) {
    				Player target = Bukkit.getPlayerExact(args[0]);
    				if (target != null) {
    					try {
    						double balance = Double.parseDouble(args[1]);
    						Economy.setBalance(target, balance);
    						sender.sendMessage(ChatColor.GREEN + "Successfully set the balance of " + target.getName() + " to " + balance + "$.");
    					} catch(Exception e) {
    						sender.sendMessage(ChatColor.RED + "Usage: /setmoney <player> <balance>");
    					}
    				}
    				else {
    					sender.sendMessage(ChatColor.RED + "Usage: /setmoney <player> <balance>");
    				}
    			}
    			else {
    				sender.sendMessage(ChatColor.RED + "Usage: /setmoney <player> <balance>");
    			}
    		}
    		else {
    			sender.sendMessage(ChatColor.RED + "No permission.");
    		}
    	}
    	
    	if (command.getName().equalsIgnoreCase("heal")) {
    		if (sender.isOp()) {
    			if (args.length > 0) {
    				Player target = Bukkit.getPlayerExact(args[0]);
    				if (target != null) {
    					target.setHealth(20);
    					target.setFireTicks(0);
    					target.setSaturation(10);
    					target.setFoodLevel(20);
    					target.sendMessage(ChatColor.GREEN + "You have been healed.");
    					sender.sendMessage(ChatColor.GREEN + "You have healed " + target.getName() + ".");
    				} else {
    					sender.sendMessage(ChatColor.RED + "This player is offline.");
    				}
    			} else {
    				if (sender instanceof Player) {
    					Player player = (Player) sender;
    					player.setHealth(20);
    					player.setFireTicks(0);
    					player.setSaturation(10);
    					player.setFoodLevel(20);
    					player.sendMessage(ChatColor.GREEN + "You have been healed.");
    				}
    			}
    		} else {
    			sender.sendMessage(ChatColor.RED + "No permission.");
    		}
    	}
    	
    	if (command.getName().equalsIgnoreCase("feed")) {
    		if (sender.isOp()) {
    			if (args.length > 0) {
    				Player target = Bukkit.getPlayerExact(args[0]);
    				if (target != null) {
    					target.setSaturation(10);
    					target.setFoodLevel(20);
    					target.sendMessage(ChatColor.GREEN + "You have been fed.");
    					sender.sendMessage(ChatColor.GREEN + "You have fed " + target.getName() + ".");
    				} else {
    					sender.sendMessage(ChatColor.RED + "This player is offline.");
    				}
    			} else {
    				if (sender instanceof Player) {
    					Player player = (Player) sender;
    					player.setSaturation(10);
    					player.setFoodLevel(20);
    					player.sendMessage(ChatColor.GREEN + "You have been fed.");
    				}
    			}
    		} else {
    			sender.sendMessage(ChatColor.RED + "No permission.");
    		}
    	}
    	
    	if (command.getName().equalsIgnoreCase("createwarp")) {
    		if (sender instanceof Player) {
    			if (sender.isOp()) {
	    			Player player = (Player) sender;
	    			if (args.length > 0) {
	    				if (!Warps.doesWarpExist(args[0])) {
	    					Warps.createWarp(args[0], player.getLocation());
	    					player.sendMessage(ChatColor.GREEN + "You have successfully created a warp named " + args[0] + ".");
	    				} else {
	    					player.sendMessage(ChatColor.RED + "This warp already exists.");
	    				}
	    			} else {
	    				player.sendMessage(ChatColor.RED + "Usage: /createwarp <name>");
	    			}
    			} else {
    				sender.sendMessage(ChatColor.RED + "No permission.");
    			}
    		}
    	}
    	
    	if (command.getName().equalsIgnoreCase("removewarp") || command.getName().equalsIgnoreCase("deletewarp")) {
    		if (sender instanceof Player) {
    			if (sender.isOp()) {
	    			Player player = (Player) sender;
	    			if (args.length > 0) {
	    				if (Warps.doesWarpExist(args[0])) {
	    					Warps.removeWarp(args[0]);
	    					player.sendMessage(ChatColor.GREEN + "You have successfully deleted a warp named " + args[0] + ".");
	    				} else {
	    					player.sendMessage(ChatColor.RED + "This warp doesn't exist.");
	    				}
	    			} else {
	    				player.sendMessage(ChatColor.RED + "Usage: /removewarp <name>");
	    			}
    			} else {
    				sender.sendMessage(ChatColor.RED + "No permission.");
    			}
    		}
    	}
    	
    	if (command.getName().equalsIgnoreCase("warp")) {
    		if (sender instanceof Player) {
    			
    	        if (Duel.duelingplayers.containsKey((Player) sender)) {
    	        	sender.sendMessage(ChatColor.RED + "You can't use this command in a duel.");
    	        	return false;
    	        }
    	        
	    		Player player = (Player) sender;
    			if (!Globals.combattaggedplayers.containsKey(player)) {
		    		if (args.length > 0) {
		    			if (Warps.doesWarpExist(args[0])) {
		    				player.teleport(Warps.getLoc(args[0]));
		    				player.sendMessage(prefix + ChatColor.GREEN + "Warping to " + ChatColor.AQUA + args[0] + ChatColor.GREEN + ".");
		    			} else {
		    				player.sendMessage(ChatColor.RED + "This warp doesn't exist.");
		    			}
		    		} else {
		    			player.sendMessage(ChatColor.RED + "Usage: /warp <name>");
		    		}
    			} else {
    				player.sendMessage(ChatColor.RED + "You can't use this command while combat tagged.");
    				return false;
    			}
    		}
    	}
    	
    	if (command.getName().equalsIgnoreCase("freeze")) {
    		if (sender.isOp()) {
    			if (args.length > 0) {
    				Player target = Bukkit.getPlayerExact(args[0]);
    				if (target != null) {
    					if (!Globals.frozenplayers.contains(target)) {
    						Globals.frozenplayers.add(target);
    						sender.sendMessage(ChatColor.GREEN + target.getName() + " has been successfully frozen.");
    					} else {
    						Globals.frozenplayers.remove(target);
    						sender.sendMessage(ChatColor.GREEN + target.getName() + " has been successfully unfrozen.");
    					}
    				} else {
    					sender.sendMessage(ChatColor.RED + "Usage: /freeze <username>");
    				}
    			} else {
    				sender.sendMessage(ChatColor.RED + "Usage: /freeze <username>");
    			}
    		} else {
    			sender.sendMessage(ChatColor.RED + "No permission.");
    		}
    	}
    	
    	if (command.getName().equalsIgnoreCase("tphere")) {
    		if (sender instanceof Player) {
        		Player player = (Player) sender;
        		if (player.isOp()) {
        			if (args.length > 0) {
        				Player target = Bukkit.getPlayerExact(args[0]);
        				if (target != null) {
        					if (target == player) {
        						player.sendMessage(ChatColor.RED + "You don't need to do that");
        						return false;
        					}
        					target.teleport(player.getLocation());
        					player.sendMessage(prefix + ChatColor.GREEN + "Teleported " + target.getName() + " to you.");
        				} else {
        					player.sendMessage(ChatColor.RED + "Usage: /tphere <username>");
        				}
        			} else {
        				player.sendMessage(ChatColor.RED + "Usage: /tphere <username>");
        			}
        		} else {
        			player.sendMessage(ChatColor.RED + "No permission.");
        		}
    		}
    	}
    	
    	if (command.getName().equalsIgnoreCase("warps")) {
    		String message = ChatColor.DARK_GREEN + "Available warps: ";
    		int i = 0;
    		for (String warpname : getConfig().getConfigurationSection("Server.warps").getKeys(false)) {
    			if (i == 0) {
        			message += ChatColor.AQUA + warpname;
    			} else {
    				message += ChatColor.GREEN + ", " + ChatColor.AQUA + warpname;
    			}
    			i++;
    		}
    		
    		sender.sendMessage(message);
    	}
    	
    	if (command.getName().equalsIgnoreCase("msg") || command.getName().equalsIgnoreCase("message")) {
    		Player player = (Player) sender;
    		String message = "";
    		
 			if (args.length == 0) {
				player.sendMessage(ChatColor.RED + "Usage: /msg <username> <message>");
				return false;
			}   	
 			
			for (int i = 1; i < args.length; i++) {
			    message += args[i] + " ";
			}

    		Player target = Bukkit.getPlayerExact(args[0]); 
    		if (target != null) {
    			if (target != player) {
    				if (args.length > 1) {
            			target.sendMessage(ChatColor.YELLOW + player.getName() + " -> You: " + ChatColor.RESET + message);
            			player.sendMessage(ChatColor.YELLOW + "You" + " -> " + target.getName() + ": "  + ChatColor.RESET + message);
            			Globals.lastmessagingplayers.put(target, player);
    				} else {
    					player.sendMessage(ChatColor.RED + "Usage: /msg <username> <message>");
    				}
    			} else {
    				player.sendMessage(ChatColor.RED + "You can't message yourself.");
    			}
    		} else {
    			player.sendMessage(ChatColor.RED + "This player is offline.");
    		}
    	}
    	
    	if (command.getName().equalsIgnoreCase("r") || command.getName().equalsIgnoreCase("respond")) {
    		Player player = (Player) sender;
    		String message = "";
    		
 			if (args.length == 0) {
				player.sendMessage(ChatColor.RED + "Usage: /r <message>");
				return false;
			}   	
 			
			for (int i = 0; i < args.length; i++) {
			    message += args[i] + " ";
			}
			
    		Player target = Globals.lastmessagingplayers.get(player);

    		if (target != null) {
    			if (target != player) {
    				if (args.length > 0) {
    					if (target.isOnline()) {
                			target.sendMessage(ChatColor.YELLOW + player.getName() + " -> You: " + ChatColor.RESET + message);
                			player.sendMessage(ChatColor.YELLOW + "You" + " -> " + target.getName() + ": "  + ChatColor.RESET + message);
    					} else {
    						player.sendMessage(ChatColor.RED + "This player is offline.");
    					}
    				} else {
    					player.sendMessage(ChatColor.RED + "Usage: /r <message>");
    				}
    			} else {
    				player.sendMessage(ChatColor.RED + "You can't message yourself.");
    			}
    		} else {
    			player.sendMessage(ChatColor.RED + "No one has messaged you recently.");
    		}
    	}
    	
    	if (command.getName().equalsIgnoreCase("discord")) {
    		sender.sendMessage(prefix + ChatColor.GRAY + "Our discord: " + ChatColor.AQUA + "https://discord.gg/qxby8X");
    	}
    		
    	
    	if (command.getName().equalsIgnoreCase("tempmute")) {
    		if (sender.isOp()) {
    			if (args.length >= 2) {
    				Player target = Bukkit.getPlayerExact(args[0]);
    				if (target != null) {
    					int time;
    					
    					try {
    						time = Integer.parseInt(args[1]);
    					} catch(Exception e) {
    						sender.sendMessage(ChatColor.RED + "Usage: /tempmute <username> <seconds>");
    						return false;
    					}
    					
    					long millis = time * 1000;
    					long endtime = System.currentTimeMillis() + millis;
    					getConfig().set("Players." + target.getUniqueId() + ".punishments.muteexpire", endtime);
    					saveConfig();
    					sender.sendMessage(ChatColor.GREEN + "Successfully muted player " + target.getName() + " for " + time + " seconds.");
    					target.sendMessage(ChatColor.DARK_RED + "You have been temporarily muted for " + time + " seconds.");
    					
    				} else {
    					sender.sendMessage(ChatColor.RED + "This player is offline.");
    				}
    			} else {
    				sender.sendMessage(ChatColor.RED + "Usage: /tempmute <username> <seconds>");
    			}
    		} else {
    			sender.sendMessage(ChatColor.RED + "No permission.");
    		}
    	}
    	
    	if (command.getName().equalsIgnoreCase("unmute")) {
    		if (sender.isOp()) {
    			if (args.length == 1) {
    				Player target = Bukkit.getPlayerExact(args[0]);
    				if (target != null) {
    					if (getConfig().getLong("Players." + target.getUniqueId() + ".punishments.muteexpire") > System.currentTimeMillis()) {
    						getConfig().set("Players." + target.getUniqueId() + ".punishments.muteexpire", 0);
    						saveConfig();
    						sender.sendMessage(ChatColor.GREEN + "Successfully unmuted " + target.getName() + ".");
    					} else {
    						sender.sendMessage(ChatColor.RED + "This player is not muted.");
    					}
    				} else {
    					sender.sendMessage(ChatColor.RED + "This player is offline.");
    				}
    			}
    		} else {
    			sender.sendMessage(ChatColor.RED + "No permission.");
    		}
    	}
    			
    	if (command.getName().equalsIgnoreCase("enchants")) {
    		
	        if (Duel.duelingplayers.containsKey((Player) sender)) {
	        	sender.sendMessage(ChatColor.RED + "You can't use this command in a duel.");
	        	return false;
	        }
	        
    		if (sender instanceof Player) {
    			Player player = (Player) sender;
    			Inventory inv = Bukkit.createInventory(player, 54, ChatColor.BLUE + "Enchants");
    			
    			for (int i = 0; i < inv.getSize(); i++) {
    				inv.setItem(i, empty);
    			}
    			
    			ItemStack hold = player.getInventory().getItemInHand();
    			
    			
    			
    					
    			if (isSword(hold)) {
    				
        			int holdsharpworth;
        			int sharplevel = hold.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
        			
        			switch(sharplevel) {
        			case 1: holdsharpworth = 100; break;
        			case 2: holdsharpworth = 200; break;
        			case 3: holdsharpworth = 400; break;
        			case 4: holdsharpworth = 800; break;
        			case 5: holdsharpworth = 1600; break;
        			default: holdsharpworth = 0; break;
        			}
        			
        			int holdunbreakingworth;
        			int unbreakinglevel = hold.getEnchantmentLevel(Enchantment.DURABILITY);
        			switch(unbreakinglevel) {
        			case 1: holdunbreakingworth = 50; break;
        			case 2: holdunbreakingworth = 100; break;
        			case 3: holdunbreakingworth = 200; break;
        			default: holdunbreakingworth = 0; break; 
        			}
        			
        			int holdfireworth;
        			int firelevel = hold.getEnchantmentLevel(Enchantment.FIRE_ASPECT);
        			switch(firelevel) {
        			case 1: holdfireworth = 500; break;
        			case 2: holdfireworth = 1000; break;
        			default: holdfireworth = 0; break; 
        			}
    				
    				// 11-15
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

    				inv.setItem(11, sharp1);
    				inv.setItem(12, sharp2);
    				inv.setItem(13, sharp3);
    				inv.setItem(14, sharp4);
    				inv.setItem(15, sharp5);
    				inv.setItem(21, unbreaking1);
    				inv.setItem(22, unbreaking2);
    				inv.setItem(23, unbreaking3);
    				inv.setItem(30, fire1);
    				inv.setItem(32, fire2);
        			ingotmeta.setDisplayName(ChatColor.GOLD + ChatColor.BOLD.toString() + "Balance: " + ChatColor.AQUA + Economy.getBalance(player) + "$");
        			balanceingot.setItemMeta(ingotmeta);
    				inv.setItem(8, balanceingot);
    			}
    			else if (isArmor(hold)) {
    				
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

    				
        			
    				inv.setItem(11, prot1);
    				inv.setItem(12, prot2);
    				inv.setItem(14, prot3);
    				inv.setItem(15, prot4);
    				inv.setItem(21, unbreaking1);
    				inv.setItem(22, unbreaking2);
    				inv.setItem(23, unbreaking3);
    			}
    			else if (isBow(hold)) {
    				
    			}
    			
    			else {
    				inv.setItem(22, KitPvP.noenchants);
    			}
    			
    			hold = player.getItemInHand();
    			if (hold != null) {
    				inv.setItem(0, hold);
    			}

    			ingotmeta.setDisplayName(ChatColor.GOLD + ChatColor.BOLD.toString() + "Balance: " + ChatColor.AQUA + Economy.getBalance(player) + "$");
    			balanceingot.setItemMeta(KitPvP.ingotmeta);
				inv.setItem(8, KitPvP.balanceingot);
    			player.openInventory(inv);
    		}
    	}
    	
    	return false;
    }
}
