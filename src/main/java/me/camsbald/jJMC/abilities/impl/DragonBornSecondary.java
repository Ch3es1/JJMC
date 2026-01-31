package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class DragonBornSecondary implements Ability {

    private final JJMC plugin;

    public DragonBornSecondary(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "§e§l§oRoar§r"; }

    @Override
    public String getId() { return "dragonborn_secondary"; }

    @Override
    public void use(Player player) {
        double sradius = 20;

        Location playerLoc = player.getLocation();
        double radiusSq = sradius * sradius;
        for (Player p : playerLoc.getWorld().getPlayers()) {
            if (p.getLocation().distanceSquared(playerLoc) <= radiusSq) {
                p.playSound(playerLoc, Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 2f);
            }
        }

        new BukkitRunnable() {
            int ticks = 0;
            int durationTicks = 30*20;
            int radius = 50;

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

                // 💥 Damage nearby players except caster
                for (Entity e : player.getWorld().getNearbyEntities(center, radius, 2, radius)) {
                    if (e instanceof LivingEntity target && target != player) {
                        target.damage(2.0, player);
                        target.setFireTicks(20);
                        target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20, 1, false, false, true));
                        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20, 1, false, false, true));
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    public String getDescription() {
        return "Scares entites around you and damages them, as well as applies Weakness II & Slowness II ";
    }
}
