package me.camsbald.jJMC.manager;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GojoDomainManager {

    private static final Set<UUID> domainCasters = new HashSet<>();
    private static final Set<UUID> frozenPlayers = new HashSet<>();

    public static void startDomain(Player caster, Collection<? extends Player> affected) {
        domainCasters.add(caster.getUniqueId());
        for (Player p : affected) {
            if (!p.equals(caster)) {
                frozenPlayers.add(p.getUniqueId());
            }
        }
    }

    public static void endDomain(Player caster) {
        domainCasters.remove(caster.getUniqueId());
        frozenPlayers.clear();
    }

    public static boolean isFrozen(Player p) {
        return frozenPlayers.contains(p.getUniqueId());
    }

    public static boolean isCaster(Player p) {
        return domainCasters.contains(p.getUniqueId());
    }
}
