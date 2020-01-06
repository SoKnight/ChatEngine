package ru.soknight.chatengine.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.soknight.chatengine.files.Config;
import ru.soknight.chatengine.utils.Checks;

public class CommandBroadcast implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if(!Checks.hasPermission(sender, "chatengine.broadcast")) return true;
        if(Checks.isInvalidUsage(sender, args, 1, "/bc <сообщение>")) return true;
        
        String raw = args[0];
        if(args.length > 1) for(int i = 1; i < args.length; i++) raw += " " + args[i];
        String format = Config.getString("broadcast.format");
        String message = format.replace("%message%", raw).replace("&", "\u00A7");
        
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(message));
        if(!(sender instanceof Player) && Config.getBoolean("broadcast.return"))
        	sender.sendMessage(message);
        return true;
    }
	
}
