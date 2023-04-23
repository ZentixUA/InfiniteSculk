package com.genife;

import com.genife.Commands.Command;
import com.genife.Managers.ConfigManager;
import com.genife.Managers.SculkManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class InfiniteSculk extends JavaPlugin {
    public static InfiniteSculk instance;
    private SculkManager sculkManager;

    public static InfiniteSculk getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        new ConfigManager(this).SetupConfig();

        sculkManager = new SculkManager();

        //noinspection DataFlowIssue
        this.getCommand("isculk").setExecutor(new Command());
    }

    public SculkManager getSculkManager() {
        return sculkManager;
    }
}
