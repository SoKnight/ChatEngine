package ru.soknight.chatengine.tablist;

import org.bukkit.ChatColor;

public class Group {

	private String name, prefix = "", suffix = "";
	private ChatColor color;
	
	public Group(String name, String prefix, String suffix, ChatColor color) {
		this.name = name;
		this.prefix = prefix;
		this.suffix = suffix;
		if(color == null) this.color = ChatColor.RESET;
		else this.color = color;
	}

	public String getName() {
		return name;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public ChatColor getColor() {
		return color;
	}
	
}
