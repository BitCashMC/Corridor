package gg.bitcash.corridor.components.inventory.playervault.database;

import gg.bitcash.corridor.components.inventory.playervault.VaultIdentity;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class VaultDataService {

    private final Map<UUID,Map<Integer, ItemStack[]>> vaultsMap;

    public VaultDataService() {
        vaultsMap = new HashMap<>();
    }

    public Inventory sendInventory(UUID uuid, int number) {

        if (!(vaultsMap.containsKey(uuid))) {
            vaultsMap.put(uuid,new HashMap<>());
        }

        if (!(vaultsMap.get(uuid).containsKey(number))) {
            vaultsMap.get(uuid).put(number,new ItemStack[0]);
        }

        ItemStack[] items = vaultsMap.get(uuid).get(number);
        Inventory inv = Bukkit.createInventory(new VaultIdentity(number),54);
        inv.setContents(items);

        return inv;
    }

    public boolean saveInventory(Inventory inventory, UUID uuid) {
        if (!(inventory.getHolder() instanceof VaultIdentity vaultIdentity)) return false;

        vaultsMap.get(uuid).put(vaultIdentity.getVaultNumber(), inventory.getContents());

        return true;
    }

}
