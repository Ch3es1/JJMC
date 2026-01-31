package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DuelistSecondary implements Ability, Listener {

    private final Set<UUID> parry = new HashSet<>();

    @Override
    public String getId() {
        return "duelist_secondary";
    }

    @Override
    public String getName() {
        return "Parry";
    }

    @Override
    public String getDescription() {
        return "Negate the next melee hit and knock the attacker back.";
    }

    @Override
    public void use(Player p) {
        parry.add(p.getUniqueId());
        p.sendMessage("§eParry ready!");

        Bukkit.getScheduler().runTaskLater(JJMC.getInstance(),
                () -> parry.remove(p.getUniqueId()), 40L);
    }

    @EventHandler
    public void onParry(EntityDamageByEntityEvent e) {
        if (!parry.contains(e.getEntity().getUniqueId())) return;

        e.setCancelled(true); // Cancel damage

        // Knockback if damager is a living entity
        if (e.getDamager() instanceof LivingEntity damager) {
            Vector kb = damager.getLocation().toVector()
                    .subtract(e.getEntity().getLocation().toVector())
                    .normalize().multiply(1.2);
            damager.setVelocity(kb);
        }

        if (e.getEntity() instanceof Player p) {
            p.playSound(p.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1f, 1.4f);
        }

        parry.remove(e.getEntity().getUniqueId());
    }
}
