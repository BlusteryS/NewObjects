package ru.newplugin.newobjects;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import ru.newplugin.newobjects.commands.ObjectCommand;
import ru.newplugin.newobjects.utils.ObjectManager;

public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		final PluginCommand bukkitCommand = getCommand("object");
		final ObjectCommand command = new ObjectCommand(new ObjectManager(getConfig()));

		bukkitCommand.setExecutor(command);
		bukkitCommand.setTabCompleter(command);
	}

	@Override
	public void onDisable() {
		saveConfig();
	}
}
