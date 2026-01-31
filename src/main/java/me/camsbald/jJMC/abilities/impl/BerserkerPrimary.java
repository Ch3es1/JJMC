package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

public class BerserkerPrimary implements Ability {

    @Override
    public String getName() { return "§6Heavy Swing"; }

    @Override
    public String getId() { return "berserker_primary"; }

    @Override
    public void use(Player player) {
        Location origin = player.getEyeLocation();
        Vector forward = origin.getDirection().normalize();
        Vector upRef = Math.abs(forward.getY()) > 0.98 ? new Vector(1, 0, 0) : new Vector(0, 1, 0);
        Vector right = forward.clone().crossProduct(upRef).normalize();
        Location particleLoc = origin.clone().add(forward.clone().multiply(1)).add(right);
        player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, particleLoc, 5, 0, 0, 0, 0);

        Location playerLoc = player.getLocation();
        for (Player p : playerLoc.getWorld().getPlayers()) {
            if (p.getLocation().distanceSquared(playerLoc) <= 100) {
                p.playSound(playerLoc, Sound.ENTITY_PLAYER_ATTACK_CRIT, 1f, 0.5f);
            }
        }
        for (Entity e : player.getNearbyEntities(3, 3, 3)) {
            if (e instanceof LivingEntity target && !target.equals(player)) {
                target.damage(8, player);
                Vector kb = target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(1.8);
                target.setVelocity(kb.setY(0.6));
            }
        }
    }

    public String getDescription() {
        return "A powerful melee swing that sends enemies flying.";
    }
}
