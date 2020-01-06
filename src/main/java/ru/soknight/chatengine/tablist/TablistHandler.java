package ru.soknight.chatengine.tablist;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TablistHandler {

	public static void setGroup(Player p, Group group) {
		String prefix = group.getPrefix();
		String suffix = group.getSuffix();
		ChatColor color = group.getColor();
		
		String name = p.getName();
		
		Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
		Team team = board.getTeam(name) == null ? board.registerNewTeam(name) : board.getTeam(name);
		
		team.setPrefix(prefix.length() > 16 ? prefix.substring(0, 16) : prefix);
		team.setSuffix(suffix.length() > 16 ? suffix.substring(0, 16) : suffix);
		team.setColor(color);
		team.addEntry(name);
	}
	
	public static void refreshTablist(Player p, String header, String footer, boolean useheader, boolean usefooter) {
		if(useheader && usefooter) 	p.setPlayerListHeaderFooter(header, footer);
		else if(useheader)			p.setPlayerListHeader(header);
		else if(usefooter)			p.setPlayerListFooter(footer);
	}
	
}
