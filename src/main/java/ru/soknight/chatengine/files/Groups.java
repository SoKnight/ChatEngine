package ru.soknight.chatengine.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import ru.soknight.chatengine.ChatEngine;
import ru.soknight.chatengine.tablist.Group;
import ru.soknight.chatengine.utils.Logger;

public class Groups {

	public static FileConfiguration config;
	private static Map<String, Group> groups;
	private static String permission;
	
	public static void refresh() {
		ChatEngine instance = ChatEngine.getInstance();
		File datafolder = instance.getDataFolder();
		if(!datafolder.isDirectory()) datafolder.mkdirs();
		File file = new File(instance.getDataFolder(), "groups.yml");
		if(!file.exists()) {
			try {
				Files.copy(instance.getResource("groups.yml"), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
				Logger.info("Generated new groups file.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		config = YamlConfiguration.loadConfiguration(file);
		permission = Config.getString("tablist.permission");
		int loaded = load();
		Logger.info("Loaded parameters for " + loaded + " groups.");
	}
	
	private static int load() {
		List<String> keys = config.getStringList("group-sort");
		if(keys.isEmpty()) return 0;
		
		ConfigurationSection section = config.getConfigurationSection("groups");
		groups = new HashMap<>();
		for(String key : keys) {
			String prefix = section.getString(key + ".prefix", "").replace("&", "\u00A7");
			String suffix = section.getString(key + ".suffix", "").replace("&", "\u00A7");
			String color = section.getString(key + ".name-color", "");
			Group group = new Group(key, prefix, suffix, ChatColor.valueOf(color));
			groups.put(key, group);
		}
		return groups.size();
	}
	
	public static boolean contains(String name) {
		return groups.containsKey(name);
	}
	
	public static Group getGroup(String name) {
		if(groups.containsKey(name)) return groups.get(name);
		else return null;
	}
	
	public static boolean hasGroup(Player p) {
		for(String group : groups.keySet())
			if(p.hasPermission(permission.replace("%group%", group))) return true;
		return false;
	}
	
	public static Group getGroup(Player p) {
		for(String group : config.getStringList("group-sort"))
			if(p.hasPermission(permission.replace("%group%", group))) {
				return groups.get(group);
			}
		return null;
	}
	
}
