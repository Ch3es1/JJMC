package me.camsbald.jJMC.items;

import org.bukkit.entity.Player;

public class AbilityUser {
    private final Player player;

    public AbilityUser(Player player) {
        this.player = player;
    }

    public Player getPlayer() { return player; }
}
