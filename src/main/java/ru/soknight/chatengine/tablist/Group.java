package ru.soknight.chatengine.tablist;

import org.bukkit.ChatColor;

import lombok.Getter;

@Getter
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
	
}
