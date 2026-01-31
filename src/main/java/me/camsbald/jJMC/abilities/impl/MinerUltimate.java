package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class MinerUltimate implements Ability {

    private final JJMC plugin;

    public MinerUltimate(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "§bX-Ray";
    }

    @Override
    public String getId() {
        return "stone_to_glass";
    }

    @Override
    public void use(Player player) {
        World world = player.getWorld();
        Location center = player.getLocation();
        int radius = 25; // how far it affects
        int durationSeconds = 5; // how long glass lasts

        Map<Block, Material> originalBlocks = new HashMap<>();

        // Loop through a sphere
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    // Only include blocks within a spherical radius
                    if (x * x + y * y + z * z > radius * radius) continue;

                    Block block = world.getBlockAt(center.clone().add(x, y, z));
                    switch (block.getType()) {
                        case Material.STONE, Material.DEEPSLATE, Material.TUFF, Material.ANDESITE, Material.DIORITE, Material.NETHERRACK, Material.DIRT, Material.GRAVEL -> {
                                originalBlocks.put(block, block.getType());
                                block.setType(Material.GLASS, false);
                        }
                    }
                }
            }
        }

        // Schedule reversion
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<Block, Material> entry : originalBlocks.entrySet()) {
                    entry.getKey().setType(entry.getValue(), false);
                }
            }
        }.runTaskLater(plugin, durationSeconds * 20L);
    }

    public String getDescription() {
        return "Gives you temporary X-Ray";
    }
}
