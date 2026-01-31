package me.camsbald.jJMC.items.abilities;

import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class TNTSwordAbility implements Ability {

    @Override
    public String getId() { return "tnt_sword"; }

    @Override
    public String getName() { return "TNT Sword"; }

    @Override
    public void use(Player player) {
        try {
            Location target = player.getTargetBlockExact(50).getLocation();

            for (int y = 1; y <= 5; y++) {
                target.add(0, 1, 0); // raise the TNT a bit
                player.getWorld().spawnEntity(target, EntityType.TNT);
            }
        } catch (Exception exception) {
            return;
        }
    }
}
