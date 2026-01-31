package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class DragonBornPrimary implements Ability {

    private final JJMC plugin;

    public DragonBornPrimary(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "§e§l§oFire Field§r"; }

    @Override
    public String getId() { return "dragonborn_primary"; }

    @Override
    public void use(Player player) {
        double sradius = 20;

        Location playerLoc = player.getLocation();
        double radiusSq = sradius * sradius;
        for (Player p : playerLoc.getWorld().getPlayers()) {
            if (p.getLocation().distanceSquared(playerLoc) <= radiusSq) {
                p.playSound(playerLoc, Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 0.1f);
            }
        }

        new BukkitRunnable() {
            int ticks = 0;
            int durationTicks = 40;
            int radius = 20;

            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }

                ticks++;
                if (ticks >= durationTicks) {
                    cancel();
                    return;
                }

                Location center = player.getLocation();

                // 🔥 Set fire around the player
                for (int x = -radius; x <= radius; x++) {
                    for (int y = -1; y <= 2; y++) {
                        for (int z = -radius; z <= radius; z++) {
                            Location loc = center.clone().add(x, y, z);

                            if (loc.getBlock().getType().isAir()) {
                                Location below = loc.clone().add(0, -1, 0);

                                if (!below.getBlock().getType().isAir()) {
                                    loc.getBlock().setType(Material.FIRE);
                                }
                            }
                        }
                    }
                }

                // 💥 Damage nearby players except caster
                for (Entity e : player.getWorld().getNearbyEntities(center, radius, 2, radius)) {
                    if (e instanceof LivingEntity target && !target.equals(player)) {
                        target.damage(2.0, player); // 1 heart per tick
                        target.setFireTicks(40);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    public String getDescription() {
        return "Sets ablaze a radius of 20 blocks";
    }
}
