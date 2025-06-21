package gg.bitcash.corridor.components.inventory.playervault;

import gg.bitcash.corridor.Corridor;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.Optional;

public class VaultUtils {

    private VaultUtils() {}

    public static Inventory buildInventory(Corridor instance, VaultMeta vaultMeta) {
        Optional<String> username = instance.getPlayerDataService().getUsername(vaultMeta.uuid());
        if (username.isEmpty()) throw new IllegalArgumentException("Failed to assemble an inventory for the given player with UUID " + vaultMeta.uuid() + ". UUID could not resolve to a username.");

        Inventory inventory = Bukkit.createInventory(new VaultIdentity(vaultMeta),54,username.get() + "'s Vault (#" + vaultMeta.number() + ")");
        inventory.setContents(vaultMeta.itemStacks());

        return inventory;
    }
}
