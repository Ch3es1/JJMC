package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class DemonSecondary implements Ability {

    private final JJMC plugin;

    public DemonSecondary(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "§cSpread Wings"; }

    @Override
    public String getId() { return "demon_secondary"; }

    @Override
    public void use(Player player) {
        player.setAllowFlight(true);
        player.setFlying(true); // start flying immediately

        int durationTicksOverWorld = 10 * 20;
        int durationTicksNether = 30 * 20;

        // Choose duration based on dimension
        int durationTicks = player.getWorld().getEnvironment() == World.Environment.NETHER
                ? durationTicksNether
                : durationTicksOverWorld;

        // Track elapsed ticks
        final int[] elapsed = {0};

        // Create a repeating task that runs every tick
        Bukkit.getScheduler().runTaskTimer(plugin, task -> {

            // Constantly enforce flight while the duration hasn't expired
            if (!player.isOnline()) {
                task.cancel(); // stop if player logs out
                return;
            }

            player.setAllowFlight(true);
            if (!player.isFlying()) player.setFlying(true);

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


    public String getDescription() {
        return "Grants temporary flight for 10 seconds in the overworld/end and 30 seconds in the nether";
    }
}
