# ChatEngine
Just another minecraft chat engine with tablist customizer and prefixes & suffixes support. 
Verified support MC 1.14.4, other versions are not tested.

### Features
- Global chat: Customizable format, message prefix (! or other) and messages cooldown.
- Local chat: Customizable format, radius and messages cooldown.
- You can edit or disable join/leave messages in chat and disable deaths messages.
- Private messages: Clickable messages for fast reply, customizable format and sending message from console.
- Tablist editor: Customizable header, footer and update frequency.
- PlaceholdersAPI supported in tablist.
- Global /me (/gme): Customizable format, cooldown and showing in console.
- Local /me (vanilla hook): Customizable format, cooldown, radius and showing in console.
- Vanilla command hooker: Disable use commands prefixes (ex.`minecraft:`,`bukkit:`) and redirect it to plugins command executors.
- Customizable messages and /cereload command for fast reloading configuration.

### Building
Plugin will be at SpigotMC soon, but now you can build it with Maven: `mvn compile`
