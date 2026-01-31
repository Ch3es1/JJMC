package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class BuilderUltimate implements Ability {
    @Override
    public String getName() { return "§aStrengthen"; }

    @Override
    public String getId() { return "builder_ultimate"; }

    @Override
    public void use(Player player) {
        player.sendMessage("You feel a sudden burst of strength!");
        int duration = 30;

        player.addPotionEffect(new PotionEffect(
                PotionEffectType.STRENGTH,
                duration*20,
                1,
                false,
                true,
                true)
        );
    }

    public String getDescription() {
        return "Gives you strength II, that's it";
    }
}
