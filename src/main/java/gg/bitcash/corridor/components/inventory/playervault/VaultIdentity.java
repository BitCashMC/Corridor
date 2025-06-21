package gg.bitcash.corridor.components.inventory.playervault;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class VaultIdentity implements InventoryHolder {

    private final VaultMeta vaultMetadata;

    public VaultIdentity(VaultMeta vaultMetadata) {
        this.vaultMetadata = vaultMetadata;
    }

    public VaultMeta getVaultMeta() {
        return vaultMetadata;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
