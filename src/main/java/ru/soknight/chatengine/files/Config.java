package ru.soknight.chatengine.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import ru.soknight.chatengine.ChatEngine;
import ru.soknight.chatengine.utils.Logger;

public class Config {

	public static FileConfiguration config;
	
	public static String GLOBAL_FORMAT, LOCAL_FORMAT;
	
	public static void refresh() {
		ChatEngine instance = ChatEngine.getInstance();
		File datafolder = instance.getDataFolder();
		if(!datafolder.isDirectory()) datafolder.mkdirs();
		File file = new File(instance.getDataFolder(), "config.yml");
		if(!file.exists()) {
			try {
				Files.copy(instance.getResource("config.yml"), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
				Logger.info("Generated new config file.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		config = YamlConfiguration.loadConfiguration(file);
		
		GLOBAL_FORMAT = getString("chat.global.format").replace("%message%", "%2$s").replace("%sender%", "%1$s");
		LOCAL_FORMAT = getString("chat.local.format").replace("%message%", "%2$s").replace("%sender%", "%1$s");
	}
	
	public static String getString(String section) {
		return config.getString(section, "").replace("&", "\u00A7");
	}
	
	public static int getInt(String section) {
		return config.getInt(section, -1);
	}
	
	public static boolean getBoolean(String section) {
		return config.getBoolean(section, true);
	}
	
	public static String getMessage(String section) {
		String path = "messages." + section;
		if(config.isSet(path)) return config.getString(path).replace("&", "\u00A7");
		Logger.error("Couldn't get message '" + section + "' from config.");
		return "";
	}
	
}
