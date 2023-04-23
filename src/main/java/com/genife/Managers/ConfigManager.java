package com.genife.Managers;

import com.genife.InfiniteSculk;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    public static String ONLY_IN_GAME_MESSAGE;
    public static String NO_PERMISSION_MESSAGE;
    public static String INCORRECT_TYPING_MESSAGE;
    public static String START_MESSAGE;
    public static String STOP_MESSAGE;
    public static Integer CATALYST_DISTANCE;
    public static Integer LINE_DISTANCE;
    public static Integer SPREAD_DELAY;
    private final InfiniteSculk plugin;

    public ConfigManager(InfiniteSculk infiniteSculk) {
        this.plugin = infiniteSculk;
    }

    public void SetupConfig() {
        // Настраиваем конфиг
        FileConfiguration config = plugin.getConfig();

        config.addDefault("OnlyInGameMessage", "§7[§eInfiniteSculk§7] §fКоманду следует использовать в игре.");
        config.addDefault("NoPermissionMessage", "§7[§eInfiniteSculk§7] §cУ тебя нет прав на использование этой команды!");
        config.addDefault("IncorrectTypingMessage", "§7[§eInfiniteSculk§7] §aНеправильный ввод: §6/isculk [start/stop] [кол-во (если start)]");
        config.addDefault("StartMessage", "§7[§eInfiniteSculk§7] §7Распространение успешно запущено!");
        config.addDefault("StopMessage", "§7[§eInfiniteSculk§7] §7Все распространения завершены!");

        config.addDefault("CatalystDistance", 8);
        config.addDefault("LineDistance", 15);
        config.addDefault("SpreadDelay", 110);

        config.options().copyDefaults(true);
        plugin.saveConfig();

        ONLY_IN_GAME_MESSAGE = config.getString("OnlyInGameMessage");
        NO_PERMISSION_MESSAGE = config.getString("NoPermissionMessage");
        INCORRECT_TYPING_MESSAGE = config.getString("IncorrectTypingMessage");
        START_MESSAGE = config.getString("StartMessage");
        STOP_MESSAGE = config.getString("StopMessage");
        CATALYST_DISTANCE = config.getInt("CatalystDistance");
        LINE_DISTANCE = config.getInt("LineDistance");
        SPREAD_DELAY = config.getInt("SpreadDelay");
    }
}
