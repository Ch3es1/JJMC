package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import me.camsbald.jJMC.manager.ClassManager;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

import static java.nio.file.Files.getAttribute;
import static org.bukkit.attribute.AttributeModifier.*;

public class MacePassive implements Ability {
    private final JJMC plugin;

    public MacePassive(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getId() { return "mace_passive"; }

    @Override
    public String getName() { return "Mace Passive"; }

    @Override
    public void use(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 1, false, false, true));
    }

    public String getDescription() {
        return "Always have Strength(II)";
    }
}
