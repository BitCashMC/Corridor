package gg.bitcash.corridor.components.sideboard.displaycondition;

import org.bukkit.entity.Player;

public class WorldCondition implements DisplayCondition {

    private String world;

    public WorldCondition(String world) {
        this.world = world;
    }

    @Override
    public boolean shouldDisplay(Player player) {
        return player.getWorld().getName().equalsIgnoreCase(world);
    }

    @Override
    public void setValue(String world) {
        this.world = world;
    }

}
