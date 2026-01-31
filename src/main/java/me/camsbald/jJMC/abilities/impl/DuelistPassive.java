package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.abilities.Ability;
import me.camsbald.jJMC.classes.PlayerClass;
import me.camsbald.jJMC.manager.ClassManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.*;

public class DuelistPassive implements Ability, Listener {

    @Override
    public String getName() { return "Duelist Passive"; }

    @Override
    public String getId() { return "duelist_passive"; }

    @Override
    public void use(Player player) {

    }

    private static final Map<UUID, DuelistPassive> DATA = new HashMap<>();

    public static DuelistPassive get(Player p) {
        return DATA.computeIfAbsent(p.getUniqueId(), k -> new DuelistPassive());
    }

    private UUID lastTarget;
    private int stacks;
    private long lastHitTime;
    private static final int MAX_STACKS = 10;
    private static final long STACK_TIMEOUT = 4000; // 4 seconds

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player duelist)) return;

        // Get the player's class using your system
        PlayerClass playerClass = ClassManager.getClass(duelist.getUniqueId().toString());
        if (playerClass == null) return; // No class selected
        if (!playerClass.getId().equalsIgnoreCase("duelist")) return; // Not a Duelist

        DuelistPassive data = get(duelist);
        data.handleHit(e.getEntity());

        // Apply bonus damage
        e.setDamage(e.getDamage() * data.getDamageMultiplier());
    }

    private void handleHit(Entity target) {
        long now = System.currentTimeMillis();

        if (lastTarget != null &&
                lastTarget.equals(target.getUniqueId()) &&
                now - lastHitTime < STACK_TIMEOUT) {

            stacks = Math.min(stacks + 1, MAX_STACKS);

        } else {
            stacks = 1;
        }

        lastTarget = target.getUniqueId();
        lastHitTime = now;

        // Inform the duelist of their current stack
        if (target instanceof Player duelist) {
            // If hitting themselves (unlikely), skip
            return;
        }
        // The Duelist is the one who owns this instance
        Player owner = getOwner();
        if (owner != null && owner.isOnline()) {
            owner.sendActionBar("§6Duelist Stacks: §e" + stacks + "/" + MAX_STACKS);
        }
    }

    private double getDamageMultiplier() {
        return 1.0 + (stacks * 0.1); // +10% per stack
    }

    public String getDescription() {
        return "The more attacks you get on a single player/entity, +10% damager per stack";
    }

    private Player getOwner() {
        for (UUID id : DATA.keySet()) {
            if (DATA.get(id) == this) {
                return org.bukkit.Bukkit.getPlayer(id);
            }
        }
        return null;
    }
}
