package ru.soknight.chatengine.handlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import ru.soknight.chatengine.ChatEngine;
import ru.soknight.chatengine.files.Config;

public class CommandRedirect implements Listener {
	
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
    	String raw = e.getMessage().substring(1);
    	String[] parts = raw.split(" ");
    	String label = parts[0];
    	
    	String redirectby = "";
    	for(String s : Config.config.getStringList("command-redirect.prefixes"))
    		if(label.startsWith(s)) redirectby = s;
    	if(redirectby.equals("")) return;
    	
    	e.setCancelled(true);
    	label = label.replace(redirectby, "");
    	String[] args = new String[parts.length - 1];
    	if(parts.length > 1) for(int i = 1; i < parts.length; i++) args[i - 1] = parts[i];
    	ChatEngine.getInstance().getCommand(label).execute(e.getPlayer(), label, args);
        return;
    }
    
}
