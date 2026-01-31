package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import me.camsbald.jJMC.manager.ClassManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class MaceUltimate implements Ability {

    private final JJMC plugin;

    public MaceUltimate(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "§bFreeze"; }

    @Override
    public String getId() { return "mace_ultimate"; }

    @Override
    public void use(Player player) {
        World world = player.getWorld();
        Location center = player.getLocation();
        double radius = 15;

        for (Entity entity : world.getNearbyEntities(center, radius, radius, radius)) {
            if (entity instanceof Player target && !target.equals(player)) {
                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 15*20, 9, false, false, true));
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Entity entity : world.getNearbyEntities(center, radius, radius, radius)) {
                    if (entity instanceof Player target && !target.equals(player)) {
                        ClassManager.applyPassives(target); // restores attributes + walk speed
                    }
                }
            }
        }.runTaskLater(plugin, 15*20L); // 15 seconds
    }

    public String getDescription() {
        return "Slows entities around you for 15 seconds";
    }
}
