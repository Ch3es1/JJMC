package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwordMasterPrimary implements Ability {

    private final JJMC plugin;

    public SwordMasterPrimary(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "§cSword Slash"; }

    @Override
    public String getId() { return "swordmaster_primary"; }

    @Override
    public void use(Player player) {
        World world = player.getWorld();
        Location origin = player.getEyeLocation();
        Vector forward = origin.getDirection().normalize();

// Determine proper up reference
        Vector upRef = Math.abs(forward.getY()) > 0.98 ? new Vector(1, 0, 0) : new Vector(0, 1, 0);

// Base right vector
        Vector right = forward.clone().crossProduct(upRef).normalize();

// Play sound for nearby players (squared distance check)
        double radiusSq = 20 * 20;
        Location playerLoc = player.getLocation();
        for (Player p : playerLoc.getWorld().getPlayers()) {
            if (p.getLocation().distanceSquared(playerLoc) <= radiusSq) {
                p.playSound(playerLoc, Sound.ENTITY_SKELETON_HORSE_HURT, 0.5f, 0.1f);
            }
        }

// Slash configuration
        double range = 100;
        double halfLength = 3.5;
        double step = 0.5;
        double rotationDegrees = 25;

// Prepare two rotated slashes (precomputed)
        Vector[] slashRotations = new Vector[]{
                rotateAroundAxis(right, forward, Math.toRadians(rotationDegrees)),
                rotateAroundAxis(right, forward, Math.toRadians(-rotationDegrees))
        };

// Precompute all offsets for each slash (reused)
        List<Vector[]> slashOffsetsList = new ArrayList<>();
        for (Vector baseSlash : slashRotations) {
            int pointsCount = (int) Math.round(halfLength * 2 / 0.2) + 1; // make sure pointsCount is correct
            Vector[] offsets = new Vector[pointsCount];
            int idx = 0;
            for (int stepIdx = 0; stepIdx < pointsCount; stepIdx++) {
                double offset = -halfLength + stepIdx * 0.2;
                Vector point = baseSlash.clone().multiply(offset);

                // Apply depth-wise curve toward forward
                double curve = -Math.pow(offset / halfLength, 2) + 1;
                point.add(forward.clone().multiply(curve * 4));

                offsets[idx++] = point; // guaranteed to fill every slot
            }
            slashOffsetsList.add(offsets);
        }

// Run slash over time
        for (Vector[] offsets : slashOffsetsList) {
            // Track traveled distance for each point
            double[] traveled = new double[offsets.length];

            new BukkitRunnable() {
                @Override
                public void run() {
                    boolean anyActive = false;

                    for (int i = 0; i < offsets.length; i++) {
                        if (traveled[i] >= range) continue;

                        // Move point forward
                        offsets[i].add(forward.clone().multiply(step));
                        traveled[i] += step;
                        anyActive = true;

                        Location loc = origin.clone().add(offsets[i]);
                        world.spawnParticle(Particle.SWEEP_ATTACK, loc, 1, 0, 0, 0, 0);

                        if (cut(world, player, loc, forward) || traveled[i] >= range) {
                            traveled[i] = range; // mark as finished so this point stops moving
                        }
                    }

                    if (!anyActive) cancel(); // All points finished
                }
            }.runTaskTimer(plugin, 0L, 1L);
        }
    }

    private Vector rotateAroundAxis(Vector v, Vector axis, double angleRad) {
        return v.clone().rotateAroundAxis(axis, angleRad);
    }


    private boolean cut(World world, Player player, Location loc, Vector knockDir) {
        boolean removedSomething = false;

        if (!loc.isGenerated()) return false;

        Material blockType = loc.getBlock().getType();
        if (blockType != Material.AIR && blockType != Material.BEDROCK) {
            loc.getBlock().setType(Material.AIR, false);
            removedSomething = true;
        }

        for (Entity entity : world.getNearbyEntities(loc, 0.7, 0.7, 0.7)) {
            if (entity instanceof LivingEntity target && !target.equals(player)) {
                target.damage(800, player);
                target.setVelocity(knockDir.clone().multiply(3));
                removedSomething = true;
            }
        }

        return removedSomething;
    }

    public String getDescription() {
        return "";
    }
}
