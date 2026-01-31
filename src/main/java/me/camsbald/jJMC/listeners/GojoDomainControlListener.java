package me.camsbald.jJMC.listeners;

import me.camsbald.jJMC.manager.GojoDomainManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class GojoDomainControlListener implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if (GojoDomainManager.isFrozen(p)) {
            e.setCancelled(true);
            p.sendMessage("§cYou are trapped in a Domain!");
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (GojoDomainManager.isFrozen(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player p && GojoDomainManager.isFrozen(p)) {
            e.setCancelled(true);
        }
        if (e.getDamager() instanceof Player p && GojoDomainManager.isFrozen(p)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent e) {
        if (GojoDomainManager.isFrozen(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (GojoDomainManager.isFrozen(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (GojoDomainManager.isFrozen(e.getPlayer())) e.setCancelled(true);
    }
}
