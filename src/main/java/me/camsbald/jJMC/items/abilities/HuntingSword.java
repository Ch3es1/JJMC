package me.camsbald.jJMC.items.abilities;

import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import java.util.Comparator;

public class HuntingSword implements Ability {

    @Override
    public String getId() { return "hunting_sword"; }

    @Override
    public String getName() { return "§cHunting §aSword"; }

    @Override
    public void use(Player player) {
        Player nearest = player.getWorld().getPlayers().stream()
                .filter(p -> !p.equals(player)) // exclude self
                .filter(p -> p.getLocation().distanceSquared(player.getLocation()) <= 100 * 100) // within 100 blocks
                .min(Comparator.comparingDouble(p -> p.getLocation().distanceSquared(player.getLocation())))
                .orElse(null);

        if (nearest != null) {
            Location casterLoc = player.getLocation();
            Location targetLoc = nearest.getLocation();

            // Calculate direction vector towards the nearest player
            Vector direction = targetLoc.toVector().subtract(casterLoc.toVector()).normalize();

            // Optional: make the player face the target
            casterLoc.setDirection(direction);
            player.teleport(casterLoc); // rotates the player to face nearest target

            // Example effect: just a message for now
            player.sendMessage("You are now facing " + nearest.getName());

        } else {
            // Fallback: no players nearby
            player.sendMessage("No players found within 100 blocks!");
        }
    }
}
