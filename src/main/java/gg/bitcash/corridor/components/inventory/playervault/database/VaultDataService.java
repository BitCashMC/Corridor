package gg.bitcash.corridor.components.inventory.playervault.database;

import gg.bitcash.corridor.components.inventory.playervault.VaultIdentity;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VaultDataService {

    private final Map<UUID,Map<Integer, ItemStack[]>> vaultsMap;

    public VaultDataService() {
        vaultsMap = new HashMap<>();
    }

    public Inventory sendInventory(UUID uuid, int number) {
        ItemStack[] items = vaultsMap.getOrDefault(uuid,new HashMap<>()).getOrDefault(number,new ItemStack[0]);
        Inventory inv = Bukkit.createInventory(new VaultIdentity(number),54);
        inv.addItem(items);

        return inv;
    }

    public boolean saveInventory(Inventory inventory, UUID uuid) {
        if (!(inventory instanceof VaultIdentity vaultIdentity)) return false;

        vaultsMap.get(uuid).put(vaultIdentity.getVaultNumber(), inventory.getContents());
        return true;
    }

}
