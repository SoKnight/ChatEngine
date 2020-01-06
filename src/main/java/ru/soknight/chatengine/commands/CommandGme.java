package ru.soknight.chatengine.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.soknight.chatengine.files.Config;
import ru.soknight.chatengine.utils.Checks;
import ru.soknight.chatengine.utils.Logger;

public class CommandGme implements CommandExecutor {
	
	private static Map<Player, Long> cooldowns = new HashMap<>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!Checks.isPlayer(sender)) return true;
    	if(!Checks.hasPermission(sender, "chatengine.me")) return true;
        if(Checks.isInvalidUsage(sender, args, 1, "/me <сообщение>")) return true;
        
        Player p = (Player) sender;
        if(isCooldown(p)) return true;
        
        String msg = args[0];
        if(args.length > 1) for(int i = 1; i < args.length; i++) msg += " " + args[i];
        String format = Config.getString("roleplay.global.format");
        String message = format.replace("%sender%", p.getDisplayName()).replace("%message%", msg);
        
        if(!p.hasPermission("chatengine.gme.bypass") && (Config.getInt("roleplay.global.cooldown") != -1))
        	cooldowns.put(p, System.currentTimeMillis());
        Bukkit.getOnlinePlayers().forEach(e -> e.sendMessage(message));
        if(Config.getBoolean("roleplay.global.show-in-console")) Logger.infoWithoutPrefix(message);
        return true;
    }
	
	private boolean isCooldown(Player p) {
		if(cooldowns.containsKey(p)) {
			long current = System.currentTimeMillis();
			long cooldown = cooldowns.get(p);
			long min = Config.getInt("roleplay.global.cooldown") * 1000;
			long time = current - cooldown;
			if(time < min) {
				String remain = String.valueOf(((int) (min - time)) / 1000);
				p.sendMessage(Config.getMessage("error-cooldown-command").replace("%time%", remain));
				return true;
			} else cooldowns.remove(p);
			return false;
		} else return false;
	}
	
}
