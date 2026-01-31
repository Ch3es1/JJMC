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

public class GojoPrimary implements Ability {

    private final JJMC plugin;

    public GojoPrimary(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "§bLapse Blue"; }

    @Override
    public String getId() { return "gojo_primary"; }

    @Override
    public void use(Player player) {
        World world = player.getWorld();
        Location eye = player.getEyeLocation();
        Vector dir = eye.getDirection().normalize();

        // Point player is looking at (10 blocks out)
        Location targetPoint = eye.clone().add(dir.multiply(12));

        // Sounds at player
        world.playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1.5f, 0.6f);
        world.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 1.2f, 0.4f);

        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks++ > 40) {
                    cancel();
                    return;
                }

                // Blue particles at target point
                world.spawnParticle(Particle.PORTAL, targetPoint, 50, 1.5, 1.5, 1.5, 0.05);

                for (Entity e : world.getNearbyEntities(targetPoint, 6, 6, 6)) {
                    if (!(e instanceof LivingEntity le)) continue;
                    if (le.equals(player)) continue;

                    // Pull entity TOWARD the player
                    Vector pull = player.getLocation().toVector()
                            .subtract(le.getLocation().toVector())
                            .normalize()
                            .multiply(1.5);

                    le.setVelocity(pull);
                    le.damage(2.0, player);
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }

    public String getDescription() {
        return "Pulls the entities around where you're looking towards you";
    }
}
