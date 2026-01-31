package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class MaceSecondary implements Ability {

    private final JJMC plugin;

    public MaceSecondary(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "§bBounce"; }

    @Override
    public String getId() { return "mace_secondary"; }

    @Override
    public void use(Player player) {
        World world = player.getWorld();
        Location center = player.getLocation();
        double radius = 15;

        for (Entity entity : world.getNearbyEntities(center, radius, radius, radius)) {
            if (entity instanceof Player target) {
                target.setVelocity(new Vector(0, 1.5, 0));
            }
        }
    }

    public String getDescription() {
        return "Launches every entity around you up in the air";
    }
}
