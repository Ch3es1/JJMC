package me.camsbald.jJMC.manager;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import me.camsbald.jJMC.abilities.impl.MinerUltimate;
import me.camsbald.jJMC.classes.PlayerClass;
import me.camsbald.jJMC.abilities.impl.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class ClassManager {

    public static final Map<String, PlayerClass> classRegistry = new HashMap<>();
    private static final Map<UUID, PlayerClass> playerClasses = new HashMap<>();
    public static PlayerDataManager dataManager;

    // Initialize with plugin
    public static void init(JJMC plugin) {
        dataManager = new PlayerDataManager(plugin);
        dataManager.startAutoSave(); // periodic save
        registerDefaults(plugin);
    }

    public static void registerDefaults(JJMC plugin) {
        classRegistry.put("runner", new PlayerClass(
                "runner",
                "§aRunner",
                new DashAbility(),
                new BlinkAbility(),
                new SprintAbility(plugin),
                List.of(new RunnerPassive(plugin))
        ));
        classRegistry.put("sukuna", new PlayerClass(
                "sukuna",
                "§cSukuna",
                new SukunaPrimary(),
                new SukunaSecondary(),
                new SukunaUltimate(plugin),
                List.of(new SukunaPassive(plugin))
        ));
        classRegistry.put("builder", new PlayerClass(
                "builder",
                "§aBuilder",
                new BuilderPrimary(plugin),
                new BuilderSecondary(),
                new BuilderUltimate(),
                List.of(new BuilderPassive(plugin))
        ));
        classRegistry.put("gojo", new PlayerClass(
                "gojo",
                "§dSatoru Gojo",
                new GojoPrimary(plugin),
                new GojoSecondary(plugin),
                new GojoUltimate(plugin),
                List.of(new GojoPassive(plugin))
        ));
        classRegistry.put("mace", new PlayerClass(
                "mace",
                "§dMace Master",
                new MacePrimary(plugin),
                new MaceSecondary(plugin),
                new MaceUltimate(plugin),
                List.of(new MacePassive(plugin))
        ));
        classRegistry.put("miner", new PlayerClass(
                "miner",
                "§aLiL Dwarf ('Miner')",
                new MinerPrimary(plugin),
                new MinerSecondary(plugin),
                new MinerUltimate(plugin),
                List.of(new MinerPassive(plugin))
        ));
        classRegistry.put("villager", new PlayerClass(
                "villager",
                "§2Villager",
                new VillagerPrimary(plugin),
                new VillagerSecondary(plugin),
                new VillagerUltimate(plugin),
                List.of()
        ));
        classRegistry.put("swordmaster", new PlayerClass(
                "swordmaster",
                "§l§aSwordMaster",
                new SwordMasterPrimary(plugin),
                new SwordMasterSecondary(plugin),
                new SwordMasterUltimate(plugin),
                List.of(new SwordMasterPassive(plugin))
        ));
        classRegistry.put("berserker", new PlayerClass(
                "berserker",
                "§l§aBerserker",
                new BerserkerPrimary(),
                new BerserkerSecondary(plugin),
                new BerserkerUltimate(),
                List.of(new BerserkerPassive())
        ));
        classRegistry.put("duelist", new PlayerClass(
                "duelist",
                "§l§bDuelist",
                new DuelistPrimary(),
                new DuelistSecondary(),
                new DuelistUltimate(),
                List.of(new DuelistPassive())
        ));
        classRegistry.put("demon", new PlayerClass(
                "demon",
                "§cDemon",
                new DemonPrimary(plugin),
                new DemonSecondary(plugin),
                new DemonUltimate(plugin),
                List.of(new DemonPassive(plugin))
        ));
        classRegistry.put("dragonborn", new PlayerClass(
                "dragonborn",
                "§e§lLegendary §o§dDragonBorn§r",
                new DragonBornPrimary(plugin),
                new DragonBornSecondary(plugin),
                new DragonBornUltimate(plugin),
                List.of(new DragonBornPassive(plugin))
        ));


    }


    // --- Player Classes ---

    public static void applyPassives(Player player) {
        PlayerClass pc = getPlayerClass(player.getUniqueId());

        for (Attribute attr : Attribute.values()) {
            AttributeInstance inst = player.getAttribute(attr);
            if (inst != null) {
                inst.setBaseValue(inst.getDefaultValue());
            }
        }

        player.setWalkSpeed(0.2f);
        player.setFlySpeed(0.1f);

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        if (pc == null) return;

        for (Ability passive : pc.getPassives()) {
            passive.use(player);
        }
    }

    private static void resetAttribute(Player p, Attribute attr) {
        AttributeInstance inst = p.getAttribute(attr);
        if (inst != null) inst.setBaseValue(inst.getDefaultValue());
    }

    private static void remove(Player p, PotionEffectType type) {
        p.removePotionEffect(type);
    }

    public static PlayerClass getPlayerClass(UUID uuid) {
        if (playerClasses.containsKey(uuid)) return playerClasses.get(uuid);

        // load from file
        if (dataManager.hasClass(uuid)) {
            String classId = dataManager.getPlayerClass(uuid);
            PlayerClass pc = classRegistry.get(classId.toLowerCase());
            if (pc != null) {
                playerClasses.put(uuid, pc);
                return pc;
            }
        }
        return null;
    }

    public static void setPlayerClass(UUID uuid, PlayerClass pc) {
        playerClasses.put(uuid, pc);
        dataManager.setPlayerClass(uuid, pc.getId());
    }

    public static Collection<PlayerClass> getAllClasses() { return classRegistry.values(); }
    public static PlayerClass getClass(String id) { return classRegistry.get(id.toLowerCase()); }
    public static PlayerClass rollRandomClass(Player player, JJMC plugin) {
        // 🔹 Default to "default" pool if player not listed
        String pool = plugin.getConfig().getString("player-pools." + player.getName(), "default");

        // 🔹 Get allowed class IDs from that pool
        List<String> allowedIds = plugin.getConfig().getStringList("class-pools." + pool);

        // 🔹 If pool doesn't exist or is empty → fallback to default
        if (allowedIds == null || allowedIds.isEmpty()) {
            pool = "default";
            allowedIds = plugin.getConfig().getStringList("class-pools.default");
        }

        if (allowedIds == null || allowedIds.isEmpty()) {
            throw new IllegalStateException("No classes defined in class-pools.default!");
        }

        // 🔹 Convert IDs → PlayerClass objects
        List<PlayerClass> allowed = new ArrayList<>();
        for (String id : allowedIds) {
            PlayerClass pc = classRegistry.get(id.toLowerCase());
            if (pc != null) {
                allowed.add(pc);
            }
        }

        if (allowed.isEmpty()) {
            throw new IllegalStateException("No valid PlayerClass found in pool: " + pool);
        }

        // 🎲 Roll from allowed only
        return allowed.get(new Random().nextInt(allowed.size()));
    }


    public static void saveAll() {
        // Save all player classes
        for (Map.Entry<UUID, PlayerClass> entry : playerClasses.entrySet()) {
            dataManager.setPlayerClass(entry.getKey(), entry.getValue().getId());
        }
        dataManager.save();
    }

    private static final Set<UUID> recentlyDead = new HashSet<>();

    public static Set<UUID> getRecentlyDead() {
        return recentlyDead;
    }

    public static void markDead(Player player) {
        recentlyDead.add(player.getUniqueId());
    }

    // Remove from cache when their passive is applied on respawn
    public static void unmarkDead(Player player) {
        recentlyDead.remove(player.getUniqueId());
    }
}
