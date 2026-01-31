package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DragonBornUltimate implements Ability {

    private final JJMC plugin;

    public DragonBornUltimate(JJMC plugin) {
        this.plugin = plugin;
    }

    private static final int RADIUS = 50;
    private static final int DENSITY = 25;
    private static final float POWER = 20f;

    private static final Random random = new Random();

    @Override
    public String getName() { return "§e§l§oEmmit Aura§r"; }

    @Override
    public String getId() { return "dragonborn_ultimate"; }

    @Override
    public void use(Player player) {
        World world = player.getWorld();
        Location center = player.getLocation();

        for (int i = 0; i < DENSITY; i++) {
            double offsetX = random.nextDouble() * RADIUS * 2 - RADIUS;
            double offsetZ = random.nextDouble() * RADIUS * 2 - RADIUS;
            double offsetY = random.nextDouble() * 10;

            Location explosionLoc = center.clone().add(offsetX, offsetY, offsetZ);
            world.createExplosion(
                    explosionLoc.getX(),
                    explosionLoc.getY(),
                    explosionLoc.getZ(),
                    POWER,
                    false,
                    false
            );

            int durationTicks = 15 * 20;
            final int[] elapsed = {0};

            Bukkit.getScheduler().runTaskTimer(plugin, task -> {

                // Constantly enforce flight while the duration hasn't expired
                if (!player.isOnline()) {
                    task.cancel(); // stop if player logs out
                    return;
                }

                player.setAllowFlight(true);
                if (!player.isFlying()) player.setFlying(true);
                player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 20, 4, false, false, true));
                player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 20, 9, false, false, true));

                elapsed[0]++;

                if (elapsed[0] >= durationTicks) {
                    // Time's up: remove flight
                    player.setFlying(false);
                    player.setAllowFlight(false);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 100, 0, false, false, true));
                    task.cancel();
                }

            }, 0L, 1L); // run every tick
        }
    }

    public String getDescription() {
        return "Explodes around the player, and grants temporary flight as well as Strength V & makes you invincible for 15 seconds";
    }
}
