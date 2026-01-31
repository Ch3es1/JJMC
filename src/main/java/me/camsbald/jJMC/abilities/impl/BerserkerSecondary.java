package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

public class BerserkerSecondary implements Ability {

    private final JJMC plugin;

    public BerserkerSecondary(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "§eLeap Smash"; }

    @Override
    public String getId() { return "berserker_secondary"; }

    @Override
    public void use(Player player) {
        Location loc = player.getLocation();
        World world = player.getWorld();
        for (int i = 0; i<10; i++) {
            world.spawnParticle(Particle.SOUL_FIRE_FLAME, 1, 1, 1, 10);
        }

        for (Player p : loc.getWorld().getPlayers()) {
            if (p.getLocation().distanceSquared(loc) <= 100) {
                p.playSound(loc, Sound.BLOCK_BEACON_ACTIVATE, 1f, 2f);
            }
        }

        player.setVelocity(player.getLocation().getDirection().multiply(1.4).setY(0.9));

        Bukkit.getScheduler().runTaskLater(
                me.camsbald.jJMC.JJMC.getInstance(), () -> {
                    for (Entity e : player.getNearbyEntities(4, 3, 4)) {
                        if (e instanceof LivingEntity target && !target.equals(player)) {
                            target.damage(10, player);
                            target.setVelocity(new Vector(0, 1, 0));
                            target.getWorld().spawnParticle(Particle.END_ROD, 1, 1, 1, 10);
                        }
                    }
                }, 10L
        );
    }

    public String getDescription() {
        return "Leap forward and slam the ground, launching enemies upward.";
    }
}
