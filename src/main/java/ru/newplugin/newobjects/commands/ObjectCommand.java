package ru.newplugin.newobjects.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.newplugin.newobjects.utils.ObjectManager;

import java.util.Collections;
import java.util.List;

public class ObjectCommand implements CommandExecutor, TabCompleter {
	private final ObjectManager manager;

	public ObjectCommand(final ObjectManager manager) {
		this.manager = manager;
	}

	@Override
	public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
		if (args.length == 0) {
			sender.sendMessage("""
				Помощь по командам:
				/object info <name> - отобразить игроку список <rule> в объекте <name>
				/object create <name> - создать объект <name>
				/object delete <name> - удалить объект <name>
				/object add <name> <rule> - добавить в объект <name> правило <rule>
				/object remove <name> <rule> - удалить из объекта <name> правило <rule>"""
			);
			return true;
		}

		switch (args[0]) {
			case "info" -> {
				if (args.length != 2) {
					sender.sendMessage("Укажите название объекта");
					return true;
				}

				if (!manager.getObjects().contains(args[1])) {
					sender.sendMessage("Объект не найден");
					return true;
				}

				sender.sendMessage("Список правил для объекта " + args[1] + ": " + String.join(", ", manager.getRulesName(args[1])));
			}

			case "create" -> {
				if (args.length != 2) {
					sender.sendMessage("Укажите название объекта");
					return true;
				}

				if (manager.getObjects().contains(args[1])) {
					sender.sendMessage("Объект уже существует");
					return true;
				}

				manager.createObject(args[1]);

				sender.sendMessage("Объект успешно создан");
			}

			case "delete" -> {
				if (args.length != 2) {
					sender.sendMessage("Укажите название объекта");
					return true;
				}

				if (!manager.removeObject(args[1])) {
					sender.sendMessage("Объект не существует");
					return true;
				}

				sender.sendMessage("Объект успешно удалён");
			}

			case "add" -> {
				if (args.length != 3) {
					sender.sendMessage("Укажите название объекта и правило");
					return true;
				}

				if (!manager.getObjects().contains(args[1])) {
					sender.sendMessage("Объект не найден");
					return true;
				}

				final List<String> available = manager.getAvailableRules(args[1]);

				if (!available.contains(args[2])) {
					sender.sendMessage("Вы не можете добавить это правило");
					return true;
				}

				manager.addRule(args[1], ObjectManager.ObjectRule.valueOf(args[2]));
				sender.sendMessage("Правило успешно добавлено");
			}

			case "remove" -> {
				if (args.length != 3) {
					sender.sendMessage("Укажите название объекта и правило");
					return true;
				}

				if (!manager.getObjects().contains(args[1])) {
					sender.sendMessage("Объект не найден");
					return true;
				}

				final List<String> available = manager.getRulesName(args[1]);

				if (!available.contains(args[2].toUpperCase())) {
					sender.sendMessage("Вы не можете удалить это правило");
					return true;
				}

				manager.removeRule(args[1], ObjectManager.ObjectRule.valueOf(args[2]));
				sender.sendMessage("Правило успешно удалено");
			}
		}

		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
		if (args.length < 2) {
			return List.of("info", "create", "delete", "add", "remove");
		}

		switch (args[0]) {
			case "info", "delete" -> {
				if (args.length > 2) break;

				return manager.getObjects();
			}

			case "add" -> {
				if (args.length < 3) {
					return manager.getObjects();
				}

				if (args.length > 3) break;

				return manager.getAvailableRules(args[1]);
			}

			case "remove" -> {
				if (args.length < 3) {
					return manager.getObjects();
				}

				if (args.length > 3) break;

				return manager.getRulesName(args[1]);
			}
		}

		return Collections.emptyList();
	}
}
