package gg.bitcash.corridor.components.inventory.playervault.eventlisteners;

import gg.bitcash.corridor.Corridor;
import gg.bitcash.corridor.components.inventory.playervault.VaultIdentity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class VaultCloseListener implements Listener {

    private final Corridor instance;

    public VaultCloseListener(Corridor instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onVaultClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        if (!(inventory.getHolder() instanceof VaultIdentity vaultIdentity)) return;

        vaultIdentity.getVaultMeta().setItemStacks(inventory.getContents());
        instance.getDataSource().getVaultDAO().putVault(vaultIdentity.getVaultMeta());
    }
}
