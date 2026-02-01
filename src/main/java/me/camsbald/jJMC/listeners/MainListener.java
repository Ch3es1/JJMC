package me.camsbald.jJMC.listeners;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.classes.PlayerClass;
import me.camsbald.jJMC.items.CustomItem;
import me.camsbald.jJMC.items.CustomItemManager;
import me.camsbald.jJMC.manager.ClassManager;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;

import static me.camsbald.jJMC.manager.ClassManager.*;

public class MainListener implements Listener {
    private final JJMC plugin;

    public MainListener(JJMC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (getPlayerClass(player.getUniqueId()) == null) {
            PlayerClass pc = rollRandomClass(player, plugin);
            setPlayerClass(player.getUniqueId(), pc);
            player.sendMessage("§eYou were assigned the class: " + pc.getDisplayName());
            player.sendMessage("§eDo /classinfo to get abilities & passive(s)");
        }

        applyPassives(player); // ✅ apply passives every join
        welcomeMessage(player);

        if (ClassManager.getPlayerClass(player.getUniqueId())
                .getPassives().stream()
                .anyMatch(p -> p.getId().equals("gojo_passive"))) {
            CustomItem item = CustomItemManager.getById("hollow_purple");
            ItemStack playerItem = item.getItem();
            playerItem.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
            player.getInventory().addItem(playerItem);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        ClassManager.markDead(player);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        // Apply passives once they respawn
        ClassManager.applyPassives(player);
        ClassManager.unmarkDead(player);
    }

    public void welcomeMessage(Player player) {
        player.sendMessage("Welcome to JJMC, Do /classinfo for clas info");
        player.sendMessage("Go Here For Plugin Credits, & Source Code: ");
        Component message = Component.text("Github Link", NamedTextColor.GREEN)
                .decorate(TextDecoration.BOLD)
                .hoverEvent(HoverEvent.showText(Component.text("Made by camsbald")))
                .clickEvent(ClickEvent.openUrl("https://github.com/Ch3es1/JJMC"));

        player.sendMessage(message);
    }
}

