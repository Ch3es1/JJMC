package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

import static me.camsbald.jJMC.manager.ClassManager.applyPassives;

public class VillagerUltimate implements Ability {

    private final JJMC plugin;

    public VillagerUltimate(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "§dSummon Guardian"; }

    @Override
    public String getId() { return "villager_ultimate"; }

    @Override
    public void use(Player player) {
        Location loc = player.getLocation();
        World world = player.getWorld();

        // 🔊 Summon sound
        world.playSound(loc, Sound.BLOCK_ANVIL_PLACE, 1.5f, 0.6f);

        IronGolem golem = (IronGolem) world.spawnEntity(loc, EntityType.IRON_GOLEM);
        golem.setPlayerCreated(true);
        golem.setCustomName("§c§lGuardian of " + player.getName());
        golem.setCustomNameVisible(true);

        // 🔒 Tag the owner
        golem.getPersistentDataContainer().set(
                new org.bukkit.NamespacedKey(plugin, "golem_owner"),
                org.bukkit.persistence.PersistentDataType.STRING,
                player.getUniqueId().toString()
        );

        int durationSeconds = 20;

        // 🔁 AI Targeting Loop
        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (!golem.isValid()) {
                    cancel();
                    return;
                }

                ticks++;

                // Find targets
                for (Entity e : golem.getNearbyEntities(15, 10, 15)) {
                    if (!(e instanceof LivingEntity target)) continue;
                    if (e.equals(player)) continue;

                    // Never target owner
                    if (e instanceof Player p && p.getUniqueId().equals(player.getUniqueId())) continue;

                    // Attack hostile mobs + players
                    if (target instanceof Player || target instanceof Monster) {
                        golem.setTarget(target);
                        break;
                    }
                }

                // ⏳ Despawn
                if (ticks >= durationSeconds * 20) {
                    golem.getWorld().playSound(golem.getLocation(), Sound.ENTITY_IRON_GOLEM_DEATH, 1f, 0.8f);
                    golem.remove();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
        applyPassives(player);
    }

    public String getDescription() {
        return "Summons an iron golem to protect you";
    }
}
