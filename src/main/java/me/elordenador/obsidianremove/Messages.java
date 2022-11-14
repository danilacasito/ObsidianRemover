package me.elordenador.obsidianremove;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Messages {
    String only_obsidian_allowed;
    JavaPlugin plugin;
    public Messages(JavaPlugin plugin) {
        List<String> list;
        this.plugin = plugin;
        this.only_obsidian_allowed = this.plugin.getConfig().getString("only_obsidian_allowed");
    }
}
