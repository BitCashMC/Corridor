package gg.bitcash.corridor.components.inventory.playervault;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class VaultIdentity implements InventoryHolder {

    private final int vaultNumber;

    public VaultIdentity(int vaultNumber) {
        this.vaultNumber = vaultNumber;
    }

    public int getVaultNumber() {
        return vaultNumber;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
