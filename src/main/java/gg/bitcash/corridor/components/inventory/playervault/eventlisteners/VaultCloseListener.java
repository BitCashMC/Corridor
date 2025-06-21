package gg.bitcash.corridor.components.inventory.playervault.eventlisteners;

import gg.bitcash.corridor.Corridor;
import gg.bitcash.corridor.components.inventory.playervault.VaultManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class VaultCloseListener extends VaultListener implements Listener {

    public VaultCloseListener(Corridor instance) {
        super(instance);
    }

    @EventHandler
    public void onVaultClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();

        if (!(inventory.getHolder() instanceof VaultManager.VaultIdentity vaultIdentity)) return;

        instance.getVaultDataService().saveVault(inventory,event.getPlayer().getUniqueId(), vaultIdentity.getVaultNumber());


    }
}
