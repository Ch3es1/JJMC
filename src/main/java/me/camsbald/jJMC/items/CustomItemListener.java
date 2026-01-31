package me.camsbald.jJMC.items;

import me.camsbald.jJMC.items.CustomItem;
import me.camsbald.jJMC.items.CustomItemManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.entity.Player;

public class CustomItemListener implements Listener {

    // Trigger ability when right-clicking main hand or offhand
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        // Only right-clicks
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        CustomItem item = CustomItemManager.getByItemStack(player.getInventory().getItemInMainHand());
        if (item == null) return;

        // Activate ability
        if (item.getAbility() != null) {
            item.getAbility().use(player);
            event.setCancelled(true); // prevent normal right-click action if desired
        }
    }
}
