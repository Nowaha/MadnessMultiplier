package me.nowaha.madnessmultiplier;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockExpEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MadnessMultiplier extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerExpChange(PlayerExpChangeEvent e) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 200; i++) {
                    if (e.getPlayer().hasPermission("xpmultiplier." + i)) {
                        e.setAmount(e.getAmount() * (1 + (i / 100)));
                       return;
                    }
                }
            }
        });
    }

    Map<Location, Material> blocksOre = new HashMap<>();
    @EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onBlockDamage(BlockDamageEvent e) {
        if (e.getBlock().getType().name().contains("ORE")) {
            blocksOre.put(e.getBlock().getLocation(), e.getBlock().getType());
        } else if (e.getBlock().getType().name().contains("STONE")) {
            if (blocksOre.containsKey(e.getBlock().getLocation())) {
                int exp = 0;

                if (blocksOre.get(e.getBlock().getLocation()) == Material.COAL_ORE) {
                    exp = 1;
                } else if (blocksOre.get(e.getBlock().getLocation()) == Material.IRON_ORE) {
                    exp = 3;
                } else if (blocksOre.get(e.getBlock().getLocation()) == Material.LAPIS_ORE) {
                    exp = 7;
                } else if (blocksOre.get(e.getBlock().getLocation()) == Material.REDSTONE_ORE) {
                    exp = 10;
                } else if (blocksOre.get(e.getBlock().getLocation()) == Material.DIAMOND_ORE) {
                    exp = 13;
                } else if (blocksOre.get(e.getBlock().getLocation()) == Material.EMERALD_ORE) {
                    exp = 15;
                } else if (blocksOre.get(e.getBlock().getLocation()) == Material.GOLD_ORE) {
                    exp = 15;
                }
                blocksOre.remove(e.getBlock().getLocation());
                int finalExp = exp;
                Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 1000; i >= 0; i--) {
                            if (e.getPlayer().hasPermission("xpmultiplier." + (long)i)) {
                                e.getPlayer().giveExp(finalExp * (i/100));
                                return;
                            }
                        }
                    }
                });
            }
        }
    }

}
