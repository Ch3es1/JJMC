package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VillagerSecondary implements Ability {

    private final JJMC plugin;

    public VillagerSecondary(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "§dGo Home"; }

    @Override
    public String getId() { return "villager_secondary"; }

    @Override
    public void use(Player player) {
        Location respawn = player.getRespawnLocation();

        if (respawn == null) {
            // Fallback to world spawn if no bed/anchor set
            respawn = player.getWorld().getSpawnLocation();
            player.sendMessage("§eYou don't have a bed set — sending you to world spawn.");
        } else {
            player.sendMessage("§aReturning you to your respawn point...");
        }

        player.teleport(respawn);
        player.playSound(respawn, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
    }

    public String getDescription() {
        return "Brings you back to your respawn point/world spawn";
    }
}
