package me.camsbald.jJMC.items.abilities;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import me.camsbald.jJMC.classes.PlayerClass;
import me.camsbald.jJMC.items.CustomItem;
import me.camsbald.jJMC.manager.ClassManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

// Create the ability for the reroll item
public class RerollTokenItem implements Ability {

    private final JJMC plugin;

    public RerollTokenItem(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getId() { return "reroll_token_ability"; }

    @Override
    public String getName() { return "Class Reroll"; }

    @Override
    public void use(Player player) {
        // Roll a new class for this player
        PlayerClass pc = ClassManager.rollRandomClass(player, plugin);
        ClassManager.setPlayerClass(player.getUniqueId(), pc);

        // Notify player
        player.sendMessage("§eYour class has been rerolled to: " + pc.getDisplayName());
        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1.2f);

        // Remove 1 item from main hand
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item != null && item.getType() != Material.AIR) {
            item.setAmount(item.getAmount() - 1);
        }
    }
}
