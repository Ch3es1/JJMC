package me.camsbald.jJMC;

import me.camsbald.jJMC.abilities.Ability;
import me.camsbald.jJMC.abilities.impl.DuelistPassive;
import me.camsbald.jJMC.abilities.impl.DuelistSecondary;
import me.camsbald.jJMC.abilities.impl.SukunaPassive;
import me.camsbald.jJMC.classes.PlayerClass;
import me.camsbald.jJMC.commands.*;

import me.camsbald.jJMC.items.CustomItem;
import me.camsbald.jJMC.items.CustomItemListener;
import me.camsbald.jJMC.items.CustomItemManager;
import me.camsbald.jJMC.items.abilities.HollowPurpleItem;
import me.camsbald.jJMC.items.abilities.HuntingSword;
import me.camsbald.jJMC.items.abilities.RerollTokenItem;
import me.camsbald.jJMC.items.abilities.TNTSwordAbility;
import me.camsbald.jJMC.listeners.GojoDomainControlListener;
import me.camsbald.jJMC.listeners.MainListener;
import me.camsbald.jJMC.manager.ClassManager;
import me.camsbald.jJMC.manager.CooldownManager;
import me.camsbald.jJMC.manager.RecipeManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.UUID;

public class JJMC extends JavaPlugin {

    private final ClassManager classManager = new ClassManager();

    public ClassManager getClassManager() {
        return classManager;
    }

    @Override
    public void onEnable() {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Only apply passives to recently dead players
                for (UUID uuid : new HashSet<>(ClassManager.getRecentlyDead())) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player != null && player.isOnline()) {
                        ClassManager.applyPassives(player);
                        ClassManager.unmarkDead(player); // remove from "recently dead" set
                    }
                }
            }
        }.runTaskTimer(this, 0L, 20L * 10); // every 10 seconds


        saveDefaultConfig();
        instance = this;

        getServer().getPluginManager().registerEvents(new MainListener(this), this);
        getServer().getPluginManager().registerEvents(new SukunaPassive(this), this);
        getServer().getPluginManager().registerEvents(new CustomItemListener(), this);
        getServer().getPluginManager().registerEvents(new GojoDomainControlListener(), this);
        Bukkit.getPluginManager().registerEvents(new DuelistPassive(), this);
        Bukkit.getPluginManager().registerEvents(new DuelistSecondary(), this);

        ClassManager.init(this); // registers classes & loads data
        CooldownManager.init(this); // load cooldowns

        getCommand("ability").setExecutor(new AbilityCommand(this));
        getCommand("setclass").setExecutor(new SetClassCommand());
        getCommand("classes").setExecutor(new ClassesCommand());
        getCommand("reroll").setExecutor(new RerollCommand(this));
        getCommand("giveitem").setExecutor(new GiveCustomItemCommand());
        getCommand("classinfo").setExecutor(new ClassInfoCommand());

        new RecipeManager(this).registerRecipes();

        // Save all data on shutdown
        Bukkit.getScheduler().runTaskLater(this, () -> {
            Runtime.getRuntime().addShutdownHook(new Thread(ClassManager::saveAll));
        }, 1L);

        CustomItem tntSword = new CustomItem(
                "tnt_sword",
                Material.DIAMOND_SWORD,
                "§cTNT Sword",
                new TNTSwordAbility(), // <-- ability that runs when used
                this
        );
        CustomItemManager.register(tntSword);

        CustomItem rerollToken = new CustomItem(
                "reroll_token",
                Material.NETHER_STAR,
                "§6Class Reroll Token",
                new RerollTokenItem(this), // <-- ability that runs when used
                this
        );
        CustomItemManager.register(rerollToken);

        CustomItem huntingSword = new CustomItem(
                "hunting_sword",
                Material.IRON_SWORD,
                "§cHunting §aSword",
                new HuntingSword(), // <-- ability that runs when used
                this
        );
        CustomItemManager.register(huntingSword);

        CustomItem hollowPurple = new CustomItem(
                "hollow_purple",
                Material.PURPLE_DYE,
                "§5Hollow Purple",
                new HollowPurpleItem(),
                this
        );
        CustomItemManager.register(hollowPurple);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    PlayerClass pc = ClassManager.getPlayerClass(player.getUniqueId());
                    if (pc == null) continue;

                    for (Ability passive : pc.getPassives()) {
                        passive.use(player);
                    }
                }
            }
        }.runTaskTimer(this, 0L, 10L); // every 10 ticks (0.5s)
    }

    private static JJMC instance;

    public static JJMC getInstance() {
        return instance;
    }
}
