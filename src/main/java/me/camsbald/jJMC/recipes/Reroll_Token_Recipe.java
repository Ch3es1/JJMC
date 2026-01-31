package me.camsbald.jJMC.recipes;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.items.CustomItemManager;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class Reroll_Token_Recipe {

    private final JJMC plugin;

    public Reroll_Token_Recipe(JJMC plugin) {
        this.plugin = plugin;
    }

    public void registerRecipes() {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();

// Display name & lore (optional)
        meta.setDisplayName("§dReroll Token");
        meta.setLore(List.of("§7Use to reroll your class"));

// ⚡ Add PersistentDataContainer entry
        NamespacedKey key = new NamespacedKey(JJMC.getInstance(), "reroll_token");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "reroll_token");

        item.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("GRG",
                "DND",
                "GDG");

        recipe.setIngredient('D', Material.DIAMOND_BLOCK);
        recipe.setIngredient('N', Material.NETHER_STAR);
        recipe.setIngredient('G', Material.GOLD_BLOCK);
        recipe.setIngredient('R', Material.REDSTONE_BLOCK);

        Bukkit.addRecipe(recipe);
    }
}
