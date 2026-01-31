package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.abilities.Ability;
import me.camsbald.jJMC.manager.ClassManager;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashSet;
import java.util.Set;

public class SukunaPrimary implements Ability {

    @Override
    public String getName() { return "§cSlash"; }

    @Override
    public String getId() { return "sukuna_primary"; }

    @Override
    public void use(Player player) {
        World world = player.getWorld();
        Location origin = player.getEyeLocation();

        Vector forward = origin.getDirection().normalize();

        Vector upRef = Math.abs(forward.getY()) > 0.98 ? new Vector(1, 0, 0) : new Vector(0, 1, 0);
        Vector right = forward.clone().crossProduct(upRef).normalize();
        Vector up = right.clone().crossProduct(forward).normalize();

        double range = 750;
        double halfLength = 200;
        double step = 0.5; // larger step for speed
        double start = 1.5;

        int SLASH_COUNT = 1;
        double ROTATION_DEGREES = 1;

        double radius = 20;

        // Precompute rotated vectors
        Vector[] rotatedRights = new Vector[SLASH_COUNT];
        Vector[] rotatedUps = new Vector[SLASH_COUNT];
        for (int i = 0; i < SLASH_COUNT; i++) {
            double angle = Math.toRadians(i * ROTATION_DEGREES);
            rotatedRights[i] = rotateAroundAxis(right, forward, angle);
            rotatedUps[i] = rotateAroundAxis(up, forward, angle);
        }

        // Spawn initial particles near player (reduced frequency)
        Location particleLoc = origin.clone().add(forward.clone().multiply(start)).add(right);
        world.spawnParticle(Particle.SWEEP_ATTACK, particleLoc, 5, 0, 0, 0, 0);

        // Play sound to nearby players
        Location playerLoc = player.getLocation();
        double radiusSq = radius * radius;
        for (Player p : playerLoc.getWorld().getPlayers()) {
            if (p.getLocation().distanceSquared(playerLoc) <= radiusSq) {
                p.playSound(playerLoc, Sound.ENTITY_WARDEN_SONIC_BOOM, 0.5f, 2f);
            }
        }

        // Track entities already hit to avoid repeated damage
        Set<LivingEntity> hitEntities = new HashSet<>();

        // Main slash loop
        for (double f = start; f < range + start; f += step) {
            Vector forwardOffset = forward.clone().multiply(f);
            Location center = origin.clone().add(forwardOffset);

            for (int i = 0; i < SLASH_COUNT; i++) {
                Vector r = rotatedRights[i];
                Vector u = rotatedUps[i];

                // X-axis slice
                for (double x = -halfLength; x <= halfLength; x += step) {
                    Vector temp = r.clone().multiply(x);
                    Location loc = center.clone().add(temp);
                     if (!cut(world, player, loc, forward, hitEntities)) {
                         break;
                     }
                }

                // Y-axis slice
                for (double y = -halfLength; y <= halfLength; y += step) {
                    Vector temp = u.clone().multiply(y);
                    Location loc = center.clone().add(temp);
                    if (!cut(world, player, loc, forward, hitEntities)) {
                        break;
                    }
                }
            }
        }
    }

    // Optimized cut method with entity batching
    private boolean cut(World world, Player player, Location loc, Vector knockDir, Set<LivingEntity> hitEntities) {
        if (!loc.isGenerated()) return false;

        for (Entity entity : world.getNearbyEntities(loc, 0.7, 0.7, 0.7)) {
            if (entity instanceof LivingEntity target && !target.equals(player) && hitEntities.add(target)) {
                target.damage(20, player);
                target.setHealth(0.0);
                target.setVelocity(knockDir.clone().multiply(3));
            }
        }

        Material blockType = loc.getBlock().getType();
        if (blockType != Material.AIR && blockType != Material.BEDROCK) {
            loc.getBlock().setType(Material.AIR, false);
            return true;
        }

        return true;
    }

    private Vector rotateAroundAxis(Vector v, Vector axis, double angleRad) {
        return v.clone().rotateAroundAxis(axis, angleRad);
    }

    @Override
    public String getDescription() {
        return "Slash fr";
    }
}