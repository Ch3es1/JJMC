package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class MinerPrimary implements Ability {

    private final JJMC plugin;

    public MinerPrimary(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "§bEnchant"; }

    @Override
    public String getId() { return "miner_primary"; }

    @Override
    public void use(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();

        // Check if the item is a pickaxe
        if (item.getType() == Material.WOODEN_PICKAXE ||
                item.getType() == Material.STONE_PICKAXE ||
                item.getType() == Material.IRON_PICKAXE ||
                item.getType() == Material.GOLDEN_PICKAXE ||
                item.getType() == Material.DIAMOND_PICKAXE ||
                item.getType() == Material.NETHERITE_PICKAXE) {

            // Add Efficiency 10
            item.addUnsafeEnchantment(Enchantment.EFFICIENCY, 10);
            item.addUnsafeEnchantment(Enchantment.UNBREAKING, 5);
            item.addUnsafeEnchantment(Enchantment.FORTUNE, 10);
            player.sendMessage("§aYour pickaxe has been enchanted!");
        } else if (item.getType() == Material.WOODEN_AXE ||
                item.getType() == Material.STONE_AXE ||
                item.getType() == Material.IRON_AXE ||
                item.getType() == Material.GOLDEN_AXE ||
                item.getType() == Material.DIAMOND_AXE ||
                item.getType() == Material.NETHERITE_AXE) {
            item.addUnsafeEnchantment(Enchantment.EFFICIENCY, 10);
            item.addUnsafeEnchantment(Enchantment.SHARPNESS, 5);
            item.addUnsafeEnchantment(Enchantment.UNBREAKING, 5);
            item.addUnsafeEnchantment(Enchantment.FORTUNE, 10);
            player.sendMessage("§aYour axe has been enchanted!");
        } else if (item.getType() == Material.WOODEN_SWORD ||
                item.getType() == Material.STONE_SWORD ||
                item.getType() == Material.IRON_SWORD ||
                item.getType() == Material.GOLDEN_SWORD ||
                item.getType() == Material.DIAMOND_SWORD ||
                item.getType() == Material.NETHERITE_SWORD) {
            item.addUnsafeEnchantment(Enchantment.SHARPNESS, 5);
            item.addUnsafeEnchantment(Enchantment.UNBREAKING, 5);
            item.addUnsafeEnchantment(Enchantment.LOOTING, 10);
            player.sendMessage("§aYour sword has been enchanted!");
        } else {
            player.sendMessage("§cYou must be holding a pickaxe/axe/sword to use this ability!");
        }
    }

    public String getDescription() {
        return "Enchant pickaxe with Efficiency X, Unbreaking V, and Fortune X";
    }
}
