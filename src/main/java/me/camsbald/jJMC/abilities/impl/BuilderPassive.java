package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public class BuilderPassive implements Ability {

    private final JJMC plugin;

    public BuilderPassive(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getId() { return "builder_passive"; }

    @Override
    public String getName() { return "§aBuilder Passive"; }

    @Override
    public void use(Player player) {
        player.setWalkSpeed(0.175f); // increase base walk speed

        AttributeInstance attr = player.getAttribute(Attribute.BLOCK_INTERACTION_RANGE);
        if (attr != null) {
            attr.setBaseValue(10);
        }
    }

    public String getDescription() {
        return "Increase block interaction range to 10, makes you walk slightly faster";
    }
}

