package com.bukkit.systexpro.mcauction;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import com.iConomy.*;
import com.iConomy.system.Account;
import com.iConomy.system.Holdings;

public class MCAuction extends JavaPlugin {

	public AuctionConfig ac;
	public String[] itemsA = {"Gold", "Torch"};
	public iConomy iConomy = null;
	public File AuctionCon = new File("plugins/Auction/config.yml");
	public File AuctionItems = new File("plugins/Auction/auction.items");
	private final Logger log = Logger.getLogger("Minecraft");
	public boolean globalMessage = false;
	public boolean useMysql = true;
	public String mysqlHost;
	public String mysqlUsername;
	public String mysqlPassword;
	public String mysqlDatabase;

	/**
	 * Disable MCAuction
	 */
	public void onDisable() {
		log.info("[MCAuction] Unloaded.");

	}

	/**
	 * Load MCAuction
	 */
	public void onEnable() {
		log.info("[MCAuction] Loaded.");
		File AuctionDir = new File("plugins/Auction");
		if(!AuctionDir.exists()) {
			AuctionDir.mkdir();
		}

		//Check for Money Jar
		this.loadConfig();
		this.loadMoneyJar();
		this.loadConfig();
		if(this.useMysql == true) {
			log.info("[MCAuction] Preparing Mysql for MCAuction");
		} else {
			log.info("[MCAuction] Preparing FlatFile for MCAuction");
		}
	}

	/**
	 * Load up Config File
	 */
	private void loadConfig() {
		Configuration config  = new Configuration(AuctionCon);
		config.load();
		this.globalMessage = config.getBoolean("use-global-messages", false);
		this.useMysql = config.getBoolean("use-mysql", true);
		this.mysqlHost = config.getString("MysqlHost", "localhost:3306/");
		this.mysqlUsername = config.getString("MysqlUsername", "root");
		this.mysqlPassword = config.getString("MysqlPassword", "");
		this.mysqlDatabase = config.getString("MysqlDatabase", "mcauction");
		config.save();

	}

	/**
	 * Command Handler
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(cmd.getName().equals("auction")) {
			if (args.length >= 1 && sender instanceof Player) {
				return mainCommand(sender,args);
			} else {
				return this.help(sender);
			}

		} 
		return super.onCommand(sender, cmd, commandLabel, args);


	}

	/** 
	 * Save Auction Items File
	public void saveAuctionItems() {
		Configuration config  = new Configuration(this.AuctionItems);
		config.setProperty("items", "torch, gold");
		config.save();
	}

	/**
	 * Load Auction Files

	public void loadAuctionItems() {
		Configuration config  = new Configuration(this.AuctionItems);
		Object items = config.getProperty("items");
		String[] items2 = ((String) items).split(", ");
		for(int x =0; x < items2.length; x++) {
			System.out.println("[MCAuction] Loaded Item: " + items2[x]);
		}
		config.load();
	}
	 */
	private boolean mainCommand(CommandSender sender, String[] args) {
		String command = args[0];
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if(command.equalsIgnoreCase("list")) {
				this.sendText(player, "===Auctioned Items===");
				for(int x = 0; x < itemsA.length; x++) {
					this.sendText(player, "(Item) " + itemsA[x]);
				}
				return true;
			} else if(command.equalsIgnoreCase("money")) {
				if(this.hasBankAccount(player)) {
					Holdings balance = iConomy.getAccount(player.getDisplayName()).getHoldings();
					this.sendText(player, Double.toString(balance.balance()));
					return true;
				} else {
					this.sendText(player, "You need a Bank Account");
					return true;
				}
			} else if(command.equalsIgnoreCase("buy"))  {
				String item = args[1];
				Material itemM = Material.matchMaterial(item);
				if(player.getInventory().contains(itemM)) {
					
				}
				int amount = Integer.parseInt(args[2]);
				this.getServer().broadcastMessage(player.getDisplayName() + " is buying " + item + " for " + amount);
				return true;
			} else if(command.equalsIgnoreCase("help"))  {
				return this.help(player);
			} else {
				return false;
			}
		}
		sender.sendMessage(Integer.toString(args.length));
		return true;
	}


	private boolean help(CommandSender sender) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			player.sendMessage(ChatColor.GREEN + "MCAuction (" + ChatColor.WHITE + "1.0" + ChatColor.GREEN + ")");
			player.sendMessage("Required " + ChatColor.RED + "[] " + ChatColor.WHITE + "Optional " + ChatColor.GREEN + "<>");
			player.sendRawMessage(ChatColor.YELLOW + "/auction money " + ChatColor.GREEN + "- " + ChatColor.WHITE + "Views how much money you have.");
			player.sendRawMessage(ChatColor.YELLOW + "/auction buy " + ChatColor.RED + "[" + ChatColor.WHITE + "item" + ChatColor.RED + "] " +  ChatColor.RED + "[" + ChatColor.WHITE + "amount" + ChatColor.RED + "] " + ChatColor.GREEN + "- " + ChatColor.WHITE + "Auction a Item at a certain amount.");
			player.sendRawMessage(ChatColor.YELLOW + "/auction list " + ChatColor.GREEN + "- " + ChatColor.WHITE + "Shows items currently for auction.");
			return true;
		}
		return true;
	}

	/**
	 * Checks if player has a bank account
	 * @param p
	 * @return
	 */
	public boolean hasBankAccount(Player p) {
		if(!iConomy.hasAccount(p.getDisplayName())) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Hook into iConomy API System
	 */
	public void loadMoneyJar() {
		if (this.iConomy == null) {
			Plugin iConomy = this.getServer().getPluginManager().getPlugin("iConomy");

			if (iConomy != null) {
				if (iConomy.isEnabled() && iConomy.getClass().getName().equals("com.iConomy.iConomy")) {
					this.iConomy = (iConomy)iConomy;
					System.out.println("[MCAuction] Found iCononmy. Hooked into API System.");
				}
			}
		}
	}

	public void sendText(Player p, String t) {
		p.sendRawMessage(ChatColor.GOLD + "[Auction] " + ChatColor.BLUE + t);
	}



}
