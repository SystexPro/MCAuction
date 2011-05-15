package com.bukkit.systexpro.mcauction;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import com.iConomy.*;
import com.iConomy.system.Account;
import com.iConomy.system.Holdings;

public class MCAuction extends JavaPlugin {

	public iConomy iConomy = null;
	private final Logger log = Logger.getLogger("Minecraft");

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

		//Check for Money Jar
		this.loadMoneyJar();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(cmd.getName().equals("auction"))
		{
			return mainCommand(sender,args);
		}

		return super.onCommand(sender, cmd, commandLabel, args);
	}


	private boolean mainCommand(CommandSender sender, String[] args) {
		String command = args[0];
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if(args.length > 2) {
				return false;
			}
			if(command.equalsIgnoreCase("list")) {
				return true;
			}
			if(command.equalsIgnoreCase("money")) {
				if(this.hasBankAccount(player)) {
					Holdings balance = iConomy.getAccount(player.getDisplayName()).getHoldings();
					this.sendText(player, Double.toString(balance.balance()));
					return true;
				} else {
					this.sendText(player, "You need a Bank Account");
					return true;
				}	
			}
			if(command.equalsIgnoreCase("buy"))  {
				String item = args[1];
				int amount = Integer.parseInt(args[2]);
			}
		}
		return false;
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
