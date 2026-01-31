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

public class GojoPassive implements Ability {
    private final JJMC plugin;

    public GojoPassive(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getId() { return "gojo_passive"; }

    @Override
    public String getName() { return "Gojo Passive"; }

    @Override
    public void use(Player player) {
        player.setWalkSpeed(0.235f);
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 2, false, false, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, Integer.MAX_VALUE, 1, false, false, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 0, false, false, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 4, false, false, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false, true));

        player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, Integer.MAX_VALUE, 9, false, false, true));
    }

    public String getDescription() {
        return "Makes you faster, gives you the following effects: Strength(III), Haste(II), Saturation(I), Regeneration(V), Fire Resistance(I)";
    }
}
