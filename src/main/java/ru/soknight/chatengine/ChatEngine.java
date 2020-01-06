package ru.soknight.chatengine;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.clip.placeholderapi.PlaceholderAPI;
import ru.soknight.chatengine.commands.CommandBroadcast;
import ru.soknight.chatengine.commands.CommandCereload;
import ru.soknight.chatengine.commands.CommandGme;
import ru.soknight.chatengine.commands.CommandMe;
import ru.soknight.chatengine.commands.CommandMsg;
import ru.soknight.chatengine.files.Config;
import ru.soknight.chatengine.files.Groups;
import ru.soknight.chatengine.handlers.CommandRedirect;
import ru.soknight.chatengine.handlers.DeathMessageHandler;
import ru.soknight.chatengine.handlers.MainHandler;
import ru.soknight.chatengine.tablist.Group;
import ru.soknight.chatengine.tablist.TablistHandler;
import ru.soknight.chatengine.utils.Logger;

public class ChatEngine extends JavaPlugin {

	private static ChatEngine instance;
	public static boolean use_supervanish = false;
    
    @Override
	public void onEnable() {
    	instance = this;
        Config.refresh();
        Groups.refresh();
        
        if(Bukkit.getPluginManager().isPluginEnabled("SuperVanish")) use_supervanish = true;
        else Logger.info("SuperVanish is not detected, ignoring it.");
        
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new MainHandler(), this);
        if(!Config.getBoolean("server-messages.show-death")) 
        	pm.registerEvents(new DeathMessageHandler(), this);
        if(Config.getBoolean("command-redirect.enabled")) 
        	pm.registerEvents(new CommandRedirect(), this);
        
        getCommand("cereload").setExecutor(new CommandCereload());
        getCommand("broadcast").setExecutor(new CommandBroadcast());
        getCommand("msg").setExecutor(new CommandMsg());
        getCommand("gme").setExecutor(new CommandGme());
        getCommand("me").setExecutor(new CommandMe());
        
        launchUpdateTask();
        Logger.info("Enabled!");
    }
    
    @Override
	public void onDisable() {
    	Bukkit.getScheduler().cancelTasks(this);
    }
    
    public static ChatEngine getInstance() {
        return instance;
    }
    
    private static void launchUpdateTask() {
		int interval = Config.getInt("tablist.update-frequency") * 20;

		Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, () -> {
			boolean useheader = Config.getBoolean("tablist.header-use");
			boolean usefooter = Config.getBoolean("tablist.footer-use");
			
			String header = Config.getString("tablist.header");
			String footer = Config.getString("tablist.footer");
			
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(Groups.hasGroup(p)) {
					Group group = Groups.getGroup(p);
					TablistHandler.setGroup(p, group);
				}
				String ph = header;
				String pf = footer;
				if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
					ph = PlaceholderAPI.setPlaceholders(p, ph);
					pf = PlaceholderAPI.setPlaceholders(p, pf);
				}
				TablistHandler.refreshTablist(p, ph, pf, useheader, usefooter);
			}
		}, interval, interval);

		Logger.info("Tablist update task started.");
	}
	
}
