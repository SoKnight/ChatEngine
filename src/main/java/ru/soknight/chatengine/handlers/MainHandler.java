package ru.soknight.chatengine.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import ru.soknight.chatengine.files.Config;
import ru.soknight.chatengine.files.Groups;
import ru.soknight.chatengine.tablist.Group;
import ru.soknight.chatengine.tablist.TablistHandler;

public class MainHandler implements Listener {

	private static Map<Player, Long> cd_local = new HashMap<>(), cd_global = new HashMap<>();
	
	@EventHandler()
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(!Config.getBoolean("server-messages.join.show")) {
			e.setJoinMessage(null); return; }
		String format = Config.getString("server-messages.join.format");
		e.setJoinMessage(format.replace("%player%", p.getDisplayName()));
		
		if(Groups.hasGroup(p)) {
			Group group = Groups.getGroup(p);
			TablistHandler.setGroup(p, group);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(!Config.getBoolean("server-messages.quit.show")) {
			e.setQuitMessage(null); return; }
		String format = Config.getString("server-messages.quit.format");
		e.setQuitMessage(format.replace("%player%", p.getDisplayName()));
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onDeath(PlayerDeathEvent e) {
		if(!Config.getBoolean("server-messages.show-deaths"))
			e.setDeathMessage(null);
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		String glprefix = Config.getString("chat.global.prefix");
		String message = e.getMessage();
		boolean global = false;
		
		if(message.startsWith(glprefix)) global = true;
		
		Player p = e.getPlayer();
		if(isCooldown(p, global)) { e.setCancelled(true); return; }
		
		String format;
		if(global) {
			if(!p.hasPermission("chatengine.chat.global.bypass") || (Config.getInt("chat.global.cooldown") != -1))
				cd_global.put(p, System.currentTimeMillis());
			format = Config.GLOBAL_FORMAT;
			e.setMessage(message.substring(1));
		} else {
			if(!p.hasPermission("chatengine.chat.local.bypass") && (Config.getInt("chat.local.cooldown") != -1))
				cd_local.put(p, System.currentTimeMillis());
			int radius = Config.getInt("chat.local.radius");
			format = Config.LOCAL_FORMAT;
			e.getRecipients().clear();
			e.getRecipients().addAll(getNearbyRecipients(p, radius));
		}
		
		String prefix = "", suffix = "";
		if(Groups.hasGroup(p)) {
			Group group = Groups.getGroup(p);
			prefix = group.getPrefix();
			suffix = group.getSuffix();
		}
		
		format = format.replace("%prefix%", prefix).replace("%suffix%", suffix);
		e.setFormat(format);
	}
	
	private List<Player> getNearbyRecipients(Player p, int radius) {
		Location ploc = p.getLocation();
		List<Player> players = new ArrayList<>();
		double squared = Math.pow(radius, 2);
		
		ploc.getWorld().getPlayers().parallelStream().forEach(pl -> {
			if(ploc.distanceSquared(pl.getLocation()) <= squared) 
				players.add(pl);
		});
		return players;
	}
	
	private boolean isCooldown(Player p, boolean global) {
		if(!cd_global.containsKey(p) && !cd_local.containsKey(p)) return false;
		
		long min, cooldown;
		
		if(cd_global.containsKey(p)) {
			min = Config.getInt("chat.global.cooldown") * 1000;
			cooldown = cd_global.get(p);
		} else {
			min = Config.getInt("chat.local.cooldown") * 1000;
			cooldown = cd_local.get(p);
		}
		
		if(min == -1) return false;
		
		long current = System.currentTimeMillis();
		long time = current - cooldown;
		if(time < min) {
			String remain = String.valueOf((int) ((min - time) / 1000));
			p.sendMessage(Config.getMessage("error-cooldown-chat").replace("%time%", remain));
			return true; } 
		else if(cd_global.containsKey(p)) cd_global.remove(p);
		else cd_local.remove(p);
		return false;
	}
	
}
