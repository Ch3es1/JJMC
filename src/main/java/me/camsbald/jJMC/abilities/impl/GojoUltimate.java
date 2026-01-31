package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import me.camsbald.jJMC.manager.GojoDomainManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static me.camsbald.jJMC.manager.ClassManager.applyPassives;
import static me.camsbald.jJMC.manager.GojoDomainManager.endDomain;

public class GojoUltimate implements Ability {

    private final JJMC plugin;

    public GojoUltimate(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "§dInfinite Void"; }

    @Override
    public String getId() { return "gojo_ultimate"; }

    @Override
    public void use(Player caster) {
        World world = caster.getWorld();
        Location center = caster.getLocation();


        double radius = 30;
        int durationSeconds = 15;

        Collection<Player> affected = new ArrayList<>();
        for (Entity e : world.getNearbyEntities(center, radius, radius, radius)) {
            if (e instanceof Player p && !p.equals(caster)) {
                affected.add(p);
            }
        }
        GojoDomainManager.startDomain(caster, affected);

        // 🔊 Domain sounds
        world.playSound(center, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 3f, 0.5f);
        world.playSound(center, Sound.ENTITY_WITHER_SPAWN, 2f, 0.6f);

        // 🧱 Save replaced blocks
        Map<Block, BlockData> replacedBlocks = new HashMap<>();

        // 🧱 Build bedrock shell
        int r = (int) radius;
        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    double dist = Math.sqrt(x*x + y*y + z*z);
                    if (dist >= radius - 1 && dist <= radius + 0.5) {
                        Location loc = center.clone().add(x, y, z);
                        Block block = world.getBlockAt(loc);
                        if (!replacedBlocks.containsKey(block)) {
                            replacedBlocks.put(block, block.getBlockData());
                            block.setType(Material.BEDROCK, false);
                        }
                    }
                }
            }
        }

        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                ticks++;

                for (Entity e : world.getNearbyEntities(center, radius, radius, radius)) {
                    if (!(e instanceof LivingEntity living)) continue;
                    if (living.equals(caster)) continue; // 👑 caster immune

                    // ❄ Freeze everything else
                    living.setVelocity(new Vector(0, 0, 0));

                    // ✨ Particles
                    Location head = living.getLocation().add(0, 1.6, 0);
                    world.spawnParticle(Particle.ENCHANT, head, 25, 0.3, 0.3, 0.3, 0.01);
                }

                if (ticks >= durationSeconds * 20) {
                    cancel();
                    endDomain(caster);

                    // 🔁 Restore blocks
                    for (Map.Entry<Block, BlockData> entry : replacedBlocks.entrySet()) {
                        entry.getKey().setBlockData(entry.getValue(), false);
                    }

                    // 🔓 Restore players (not caster needed, but safe)
                    for (Entity e : world.getNearbyEntities(center, radius, radius, radius)) {
                        if (e instanceof Player p && !p.equals(caster)) {
                            applyPassives(p);
                            endDomain(caster);
                            p.sendMessage("§aYou are released from the Domain.");
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    public String getDescription() {
        return "Infinite Void Domain Expansion";
    }
}
