package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public class BerserkerPassive implements Ability {

    @Override
    public String getName() { return "§cBloodlust"; }

    @Override
    public String getId() { return "berserker_passive"; }

    @Override
    public void use(Player player) {
        double hpPercent = player.getHealth() / player.getMaxHealth();
        float speed = (float) (0.2 + (1 - hpPercent) * 0.15);
        player.setWalkSpeed(Math.min(speed, 0.25f));

        AttributeInstance attack = player.getAttribute(Attribute.ATTACK_DAMAGE);
        if (attack != null) {
            attack.setBaseValue(6 + ((1 - hpPercent) * 3));
        }
    }

    public String getDescription() {
        return "The lower your health, the stronger and faster you become.";
    }
}
