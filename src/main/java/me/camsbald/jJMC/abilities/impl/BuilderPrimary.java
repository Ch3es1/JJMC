package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class BuilderPrimary implements Ability {

    private final JJMC plugin;

    public BuilderPrimary(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "§aCast Wall"; }

    @Override
    public String getId() { return "builder_primary"; }

    @Override
    public void use(Player player) {
        World world = player.getWorld();
        Location origin = player.getLocation();

        int radius = 10;
        int height = 6;
        int arcDegrees = 120; // width of the curve
        int segments = 30;    // smoothness

        Vector forward = origin.getDirection().setY(0).normalize();
        Vector right = forward.clone().crossProduct(new Vector(0, 1, 0)).normalize();

        // Create the map outside loops
        HashMap<Block, BlockData> originalBlocks = new HashMap<>();

        for (int i = -segments; i <= segments; i++) {
            double angle = Math.toRadians(i * (arcDegrees / (double) segments));

            Vector dir = forward.clone()
                    .multiply(Math.cos(angle))
                    .add(right.clone().multiply(Math.sin(angle)))
                    .normalize();

            for (int y = 0; y < height; y++) {
                Location loc = origin.clone()
                        .add(dir.clone().multiply(radius))
                        .add(0, y, 0);

                Block block = world.getBlockAt(loc);

                if (!originalBlocks.containsKey(block)) {
                    originalBlocks.put(block, block.getBlockData());
                }

                block.setType(Material.STONE, false);
            }
        }

        int durationTicks = 200;

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<Block, BlockData> entry : originalBlocks.entrySet()) {
                    entry.getKey().setBlockData(entry.getValue(), false);
                }
            }
        }.runTaskLater(plugin, durationTicks);
    }

    public String getDescription() {
        return "Make a temporary stone wall in front of you";
    }
}
