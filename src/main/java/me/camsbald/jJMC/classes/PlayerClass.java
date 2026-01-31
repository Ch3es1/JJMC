package me.camsbald.jJMC.classes;

import me.camsbald.jJMC.abilities.Ability;

import java.util.Arrays;
import java.util.List;

public class PlayerClass {

    private final String id;
    private final String displayName;

    private final Ability primary;
    private final Ability secondary;
    private final Ability ultimate;

    private final List<Ability> passives;

    public PlayerClass(String id, String displayName, Ability primary, Ability secondary, Ability ultimate, List<Ability> passives) {
        this.id = id;
        this.displayName = displayName;
        this.primary = primary;
        this.secondary = secondary;
        this.ultimate = ultimate;
        this.passives = passives;
    }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }

    public Ability getPrimary() { return primary; }
    public Ability getSecondary() { return secondary; }
    public Ability getUltimate() { return ultimate; }

    public List<Ability> getPassives() { return passives; }

    // ✅ NEW: returns all active abilities
    public List<Ability> getAbilities() { return Arrays.asList(primary, secondary, ultimate); }
}
