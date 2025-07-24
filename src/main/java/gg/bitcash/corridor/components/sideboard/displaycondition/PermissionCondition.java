package gg.bitcash.corridor.components.sideboard.displaycondition;

import org.bukkit.entity.Player;

public class PermissionCondition implements DisplayCondition {

    private String permission;

    public PermissionCondition(String permission) {
        this.permission = permission;
    }

    @Override
    public boolean shouldDisplay(Player player) {
        return player.hasPermission(permission);
    }

    @Override
    public void setValue(String permission) {
        this.permission = permission;
    }
}
