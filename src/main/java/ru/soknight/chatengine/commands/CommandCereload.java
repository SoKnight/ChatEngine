package ru.soknight.chatengine.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ru.soknight.chatengine.files.Config;
import ru.soknight.chatengine.files.Groups;
import ru.soknight.chatengine.utils.Checks;

public class CommandCereload implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!Checks.hasPermission(sender, "chatengine.reload")) return true;
        Config.refresh();
        Groups.refresh();
        
        sender.sendMessage(Config.getMessage("reload-success"));
		return true;
	}
    
}
