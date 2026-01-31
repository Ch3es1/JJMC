package me.camsbald.jJMC.manager;

import me.camsbald.jJMC.abilities.Ability;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AbilityManager {

    private static final Map<UUID, Ability> playerAbilities = new HashMap<>();

    public static void setAbility(UUID uuid, Ability ability) {
        playerAbilities.put(uuid, ability);
    }

    public static Ability getAbility(UUID uuid) {
        return playerAbilities.get(uuid);
    }
}
