package com.genife;

import com.genife.Commands.Command;
import com.genife.Managers.SculkManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class InfiniteSculk extends JavaPlugin {
    public static InfiniteSculk instance;
    private SculkManager sculkManager;
    public static String START_MESSAGE;
    public static String STOP_MESSAGE;

    public static InfiniteSculk getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        sculkManager = new SculkManager();

        // Настраиваем конфиг
        FileConfiguration config = getConfig();

        config.addDefault("StartMessage", "§7[§eInfiniteSculk§7] §7Распространение успешно запущено!");
        config.addDefault("StopMessage", "§7[§eInfiniteSculk§7] §7Все распространения завершены!");
        config.options().copyDefaults(true);
        saveConfig();

        START_MESSAGE = config.getString("StartMessage");
        STOP_MESSAGE = config.getString("StopMessage");

        //noinspection DataFlowIssue
        this.getCommand("isculk").setExecutor(new Command());
    }

    public SculkManager getSculkManager() {
        return sculkManager;
    }
}
