package gg.bitcash.corridor.components.inventory.playervault.eventlisteners;

import gg.bitcash.corridor.Corridor;
import gg.bitcash.corridor.components.inventory.playervault.VaultIdentity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class VaultCloseListener extends VaultListener implements Listener {

    public VaultCloseListener(Corridor instance) {
        super(instance);
    }

    @EventHandler
    public void onVaultClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        if (!(inventory.getHolder() instanceof VaultIdentity vaultIdentity)) return;

        instance.getDataService().saveInventory(inventory,event.getPlayer().getUniqueId());
    }
}
