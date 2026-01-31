package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class DuelistUltimate implements Ability {

    @Override
    public String getId() {
        return "duelist_ultimate";
    }

    @Override
    public String getName() {
        return "Duel Arena";
    }

    @Override
    public String getDescription() {
        return "Trap you and your target in a temporary 1v1 arena.";
    }

    @Override
    public void use(Player p) {
        Player target = p.getTargetEntity(10) instanceof Player t ? t : null;
        if (target == null) {
            p.sendMessage("§cNo target!");
            return;
        }

        Location center = p.getLocation().clone();
        double radius = 4;

        BukkitRunnable task = new BukkitRunnable() {
            int ticks = 0;

            public void run() {
                ticks++;
                if (ticks > 120) {
                    cancel();
                    return;
                }

                drawCircle(center, radius);
                pullBack(p, center, radius);
                pullBack(target, center, radius);
            }
        };

        task.runTaskTimer(JJMC.getInstance(), 0, 1);
    }

    private void pullBack(Player player, Location c, double r) {
        if (player.getLocation().distance(c) > r) {
            player.teleport(player.getLocation().toVector()
                    .subtract(player.getLocation().toVector().subtract(c.toVector()).normalize().multiply(0.5))
                    .toLocation(player.getWorld(), player.getLocation().getYaw(), player.getLocation().getPitch()));
        }
    }

    private void drawCircle(Location loc, double r) {
        World w = loc.getWorld();
        for (double t = 0; t < Math.PI * 2; t += Math.PI / 16) {
            double x = Math.cos(t) * r;
            double z = Math.sin(t) * r;
            w.spawnParticle(
                    Particle.DUST,
                    loc.clone().add(x, 0.1, z),
                    1,
                    new Particle.DustOptions(Color.RED, 1)
            );
        }
    }
}
