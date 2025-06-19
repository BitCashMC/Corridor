package gg.bitcash.corridor.components.inventory.playervault;

import gg.bitcash.corridor.components.inventory.playervault.database.VaultDataService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class VaultManager {

    private final VaultDataService vaultDataService;

    public VaultManager(VaultDataService dataService) {
        this.vaultDataService = dataService;
    }

    public Inventory buildInventory(Player player, int number) {
        VaultDataService.VaultMeta vaultMeta = vaultDataService.fetchVaultMeta(player.getUniqueId(),number);
        Inventory inventory = Bukkit.createInventory(new VaultIdentity(number),54,"Viewing " + player.getName() + "'s Vault #" + number);
        inventory.setContents(vaultMeta.getItemStacks());

        return inventory;
    }

    public static class VaultIdentity implements InventoryHolder {

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
}
