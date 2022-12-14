package me.elordenador.obsidianremove;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.plugin.java.JavaPlugin;
import me.elordenador.Utilities.StringUtils;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListenEvent implements Listener {
    Messages msg;
    JavaPlugin plugin;
    StringUtils utils;
    private final Map<Block, Long> blockList = new HashMap<>();

    private void startRunTaskTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                blockList.entrySet().removeIf(entry -> {
                    if (entry.getValue() <= System.currentTimeMillis()) {
                        entry.getKey().setType(Material.AIR);

                        return true;
                    }
                    return false;
                });
            }
        }.runTaskTimer(this.plugin, 20, 20);
    }

    public ListenEvent(JavaPlugin plugin) {
        this.plugin = plugin;
        this.utils = new StringUtils();
        this.startRunTaskTimer();
        this.msg = new Messages(this.plugin);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        long time = this.plugin.getConfig().getLong("time");
        Block block = event.getBlockPlaced();
        List<String> list;
        list = (List<String>) this.plugin.getConfig().getList("worlds");
        List<String> removed_blocks;
        removed_blocks = (List<String>) this.plugin.getConfig().getList("disabled_blocks");

        assert list != null;
        if (this.utils.isPresent(player.getWorld().getName(), list)) {
            if (block.getBlockData().getMaterial() != Material.OBSIDIAN && block.getBlockData().getMaterial() != Material.END_CRYSTAL && block.getBlockData().getMaterial() != Material.RESPAWN_ANCHOR) {
                if (!player.hasPermission("Remover.exclude")) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',this.msg.only_obsidian_allowed));
                }
            }
        }
        if (block.getBlockData().getMaterial() == Material.OBSIDIAN) {
            if (!this.utils.isPresent("obsidian", removed_blocks)) {
                if (this.utils.isPresent(player.getWorld().getName(), list)) {
                    blockList.put(event.getBlock(), System.currentTimeMillis() + time);
                }
            }

        } else if (block.getBlockData().getMaterial() == Material.RESPAWN_ANCHOR) {
            if (!this.utils.isPresent("respawn_anchor", removed_blocks)) {
                if (this.utils.isPresent(player.getWorld().getName(), list)) {
                    blockList.put(event.getBlock(), System.currentTimeMillis() + time);
                }
            }
        } else if (block.getBlockData().getMaterial() == Material.END_CRYSTAL) {
            event.setCancelled(false);
        } else {
            if (!player.hasPermission("Remover.exclude")) {
                if (this.utils.isPresent(player.getWorld().getName(), list)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',this.msg.only_obsidian_allowed));
                    event.setCancelled(true);
                }
            }
        }
    }
    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        List<String> list;
        list = (List<String>) this.plugin.getConfig().getList("worlds");
        assert list != null;
        if (this.utils.isPresent(player.getWorld().getName(), list)) {
            if (!player.hasPermission("Remover.exclude")) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',this.msg.only_obsidian_allowed));
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
            double damage = event.getDamage();
            double percentage = this.plugin.getConfig().getDouble("ender_crystal_damage_percentage");
            int isEnabled = this.plugin.getConfig().getInt("crystal_damage_enabled");
            int isNegative = this.plugin.getConfig().getInt("crystal_damage_percentage_negative");
            if (isEnabled == 0) {event.setDamage(damage);} else {
                if (isNegative == 0) {
                    event.setDamage(damage + percentage);
                } else {
                    event.setDamage(damage - percentage);
                }
            }
        }
    }
}
