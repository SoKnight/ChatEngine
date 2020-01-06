package ru.soknight.chatengine.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.soknight.chatengine.files.Config;

public class Checks {

	public static boolean hasPermission(CommandSender sender, String permission) {
		if(!sender.hasPermission(permission)) {
			sender.sendMessage(Config.getMessage("error-not-permission"));
			return false;
		} else return true;
	}
	
	public static boolean isPlayer(CommandSender sender) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(Config.getMessage("error-only-for-players"));
			return false;
		} else return true;
	}
	
	public static boolean isInvalidUsage(CommandSender sender, String[] args, int neededargscount, String usage) {
		if(args.length < neededargscount) {
			sender.sendMessage(Config.getMessage("error-invalid-syntax").replace("%usage%", usage));
			return true;
		} else return false;
	}
	
}
