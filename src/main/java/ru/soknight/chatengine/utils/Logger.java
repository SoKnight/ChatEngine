package ru.soknight.chatengine.utils;

import ru.soknight.chatengine.ChatEngine;

public class Logger {
	
	public static void infoWithoutPrefix(String info) {
		java.util.logging.Logger.getLogger("Minecraft").info(info);
		return;
	}
	
    public static void info(String info) {
        ChatEngine.getInstance().getLogger().info(info);
        return;
    }
    
    public static void warning(String warn) {
        ChatEngine.getInstance().getLogger().warning(warn);
        return;
    }
    
    public static void error(String error) {
        ChatEngine.getInstance().getLogger().severe(error);
        return;
    }
    
}
