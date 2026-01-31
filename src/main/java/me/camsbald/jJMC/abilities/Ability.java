package me.camsbald.jJMC.abilities;

import org.bukkit.entity.Player;

public interface Ability {

    String getName();
    String getId();
    void use(Player player);

    default String getDescription() { return null; }
}