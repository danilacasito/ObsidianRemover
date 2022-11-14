package me.elordenador.obsidianremove;

import org.bukkit.plugin.java.JavaPlugin;

public final class ObsidianRemove extends JavaPlugin {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(new ListenEvent(this), this);
        this.getCommand("obreload").setExecutor(new ReloadCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
