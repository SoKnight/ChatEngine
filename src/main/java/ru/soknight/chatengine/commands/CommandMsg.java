package ru.soknight.chatengine.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.myzelyam.api.vanish.VanishAPI;
import net.md_5.bungee.api.chat.BaseComponent;
import ru.soknight.chatengine.ChatEngine;
import ru.soknight.chatengine.files.Config;
import ru.soknight.chatengine.utils.Checks;
import ru.soknight.chatengine.utils.MessageConstructor;

public class CommandMsg implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if(!Checks.hasPermission(sender, "chatengine.msg")) return true;
        if(Checks.isInvalidUsage(sender, args, 2, "/w <получатель> <сообщение>")) return true;
        
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if(!target.isOnline()) {
        	sender.sendMessage(Config.getMessage("error-player-not-found").replace("%player%", args[0]));
        	return true; } 
        if(ChatEngine.use_supervanish)
        	if(VanishAPI.isInvisible(target.getPlayer()) && !sender.hasPermission("chatengine.msg.vanished")) {
        		sender.sendMessage(Config.getMessage("error-player-not-found").replace("%player%", args[0]));
        		return true; }
        
        Player receiver = target.getPlayer();
        String message = args[1];
        if(args.length > 2) for(int i = 2; i < args.length; i++) message += " " + args[i];
        
        if(Config.getBoolean("private-messages.clickable"))
        	sendMessages(sender, receiver, message);
        else {
        	String format = Config.getString("private-messages.format"), name = receiver.getName(), sn;
        	if(sender instanceof Player) sn = ((Player) sender).getName();
        	else sn = Config.getString("private-messages.name-replacing");
        	String raw = format.replace("%sender%", sn).replace("%receiver%", name).replace("%message%", message);
        	sender.sendMessage(raw);
            receiver.sendMessage(raw);
        }
        return true;
    }
	
	private void sendMessages(CommandSender sender, Player receiver, String message) {
		String format = Config.getString("private-messages.format"), name = receiver.getName();
		BaseComponent[] forsender, forreceiver;
        String insertion = Config.getString("private-messages.insertion");
        if(sender instanceof Player) {
        	String sn = ((Player) sender).getName();
        	String sht = Config.getString("private-messages.hover-text.sender");
        	String rht = Config.getString("private-messages.hover-text.receiver");
        	// Constructing
        	String raw = format.replace("%sender%", sn).replace("%receiver%", name).replace("%message%", message);
        	forsender = MessageConstructor.getMessage(raw, sht, insertion.replace("%player%", name));
            forreceiver = MessageConstructor.getMessage(raw, rht, insertion.replace("%player%", sn));
            // Sending
            sender.sendMessage(forsender);
            receiver.sendMessage(forreceiver);
        } else {
        	String cs = Config.getString("private-messages.name-replacing");
        	String cht = Config.getString("private-messages.hover-text.by-console");
        	// Constructing
        	String raw = format.replace("%sender%", cs).replace("%receiver%", name).replace("%message%", message);
        	forreceiver = MessageConstructor.getMessage(raw, cht, "");
        	// Sending
        	sender.sendMessage(raw);
            receiver.sendMessage(forreceiver);
        }
        return;
	}
	
}
