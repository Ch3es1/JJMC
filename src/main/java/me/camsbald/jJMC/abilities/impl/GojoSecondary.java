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

public class GojoSecondary implements Ability {

    private final JJMC plugin;

    public GojoSecondary(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "§cReversal Red"; }

    @Override
    public String getId() { return "gojo_secondary"; }

    @Override
    public void use(Player player) {
        World world = player.getWorld();
        Location origin = player.getEyeLocation();
        Vector direction = origin.getDirection().normalize();

        // 🔊 Cannon sounds
        world.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 2f, 0.6f);
        world.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2f, 0.8f);

        new BukkitRunnable() {
            double step = 0;

            @Override
            public void run() {
                step += 1.2;
                if (step > 60) {
                    cancel();
                    return;
                }

                Location point = origin.clone().add(direction.clone().multiply(step));

                // 🔴 Red blast particles
                world.spawnParticle(Particle.FLAME, point, 25, 0.6, 0.6, 0.6, 0.02);
                world.spawnParticle(Particle.DUST, point, 15, 0.4, 0.4, 0.4,
                        new Particle.DustOptions(Color.RED, 2f));

                // 💥 Damage + Knockback
                for (Entity e : world.getNearbyEntities(point, 2.5, 2.5, 2.5)) {
                    if (!(e instanceof LivingEntity le)) continue;
                    if (le.equals(player)) continue;

                    le.damage(10, player);

                    Vector knock = le.getLocation().toVector()
                            .subtract(player.getLocation().toVector())
                            .normalize()
                            .multiply(2.5)
                            .setY(0.8);

                    le.setVelocity(knock);
                }

                // 🧱 Break blocks in path
                Block block = point.getBlock();
                if (!block.isPassable() && block.getType() != Material.BEDROCK) {
                    block.breakNaturally();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    public String getDescription() {
        return "Pushes entities in where your looking away from you and damages them";
    }
}
