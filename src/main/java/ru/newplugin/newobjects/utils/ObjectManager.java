package ru.newplugin.newobjects.utils;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ObjectManager {
	private final FileConfiguration config;

	public ObjectManager(final FileConfiguration config) {
		if (!config.contains("objects")) config.createSection("objects");
		this.config = config;
	}

	/**
	 * Получает названия всех зарегистрированных объектов
	 */
	public List<String> getObjects() {
		return List.copyOf(config.getConfigurationSection("objects").getKeys(false));
	}

	/**
	 * Добавляет правило в объект
	 *
	 * @param object название объекта
	 * @param rule правило для добавления
	 * @return true в случае успеха, false при неудаче
	 */
	public boolean addRule(final String object, final ObjectRule rule) {
		final List<ObjectRule> rules = getRules(object);

		if (rules.contains(rule)) return false;

		rules.add(rule);
		config.set("objects." + object, rules.stream().map(ObjectRule::name).toList());

		return true;
	}

	/**
	 * Удаляет правило из объекта
	 *
	 * @param object название объекта
	 * @param rule правило для удаления
	 * @return true в случае успеха
	 */
	public boolean removeRule(final String object, final ObjectRule rule) {
		final List<ObjectRule> rules = getRules(object);

		if (!rules.remove(rule)) return false;

		config.set("objects." + object, rules.stream().map(ObjectRule::name).toList());
		return true;
	}

	/**
	 * Создаёт объект
	 *
	 * @param object название объекта
	 */
	public void createObject(final String object) {
		config.set("objects." + object, Collections.emptyList());
	}

	/**
	 * Удаляет объект
	 *
	 * @param object название объекта
	 * @return true, если объект был удалён
	 */
	public boolean removeObject(final String object) {
		if (!getObjects().contains(object)) return false;

		config.set("objects." + object, null);
		return true;
	}

	/**
	 * Получает список правил для конкретного объекта
	 *
	 * @param name название объекта
	 */
	public List<ObjectRule> getRules(final String name) {
		final List<ObjectRule> rules = new ArrayList<>();

		for (final String rule : config.getStringList("objects." + name)) {
			rules.add(ObjectRule.valueOf(rule));
		}

		return rules;
	}

	/**
	 * Получает список правил в виде строк
	 *
	 * @param name название объекта
	 */
	public List<String> getRulesName(final String name) {
		return config.getStringList("objects." + name);
	}

	/**
	 * Получает список правил, которые можно
	 * добавить к объекту
	 *
	 * @param name название объекта
	 */
	public List<String> getAvailableRules(final String name) {
		final List<String> names = getRulesName(name);

		return Arrays.stream(ObjectManager.ObjectRule.values())
			.map(ObjectManager.ObjectRule::name)
			.filter(pred -> !names.contains(pred))
			.toList();
	}

	public enum ObjectRule {
		A, B, C, D, E
	}
}
