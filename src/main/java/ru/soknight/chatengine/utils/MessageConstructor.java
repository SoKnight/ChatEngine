package ru.soknight.chatengine.utils;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class MessageConstructor {

	public static BaseComponent[] getMessage(String raw, String hovertext, String insertion) {
		TextComponent message = new TextComponent(raw);
		message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, insertion));
		message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(hovertext)));
		BaseComponent[] component = new BaseComponent[] { message };
		return component;
	}
	
}
