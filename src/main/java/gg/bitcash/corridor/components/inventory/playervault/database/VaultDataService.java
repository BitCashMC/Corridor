package gg.bitcash.corridor.components.inventory.playervault.database;

import gg.bitcash.corridor.Corridor;
import gg.bitcash.corridor.components.inventory.playervault.VaultMeta;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class VaultDataService {

    private final Map<UUID,Map<Integer, ItemStack[]>> vaultsMap;
    private final Corridor instance;

    public VaultDataService(Corridor instance) {
        this.instance = instance;
        vaultsMap = new HashMap<>();
    }

    public Optional<VaultMeta> fetchVaultMeta(UUID uuid, int number) {

        if (instance.getPlayerDataService().playerExists(uuid)) {
            return Optional.empty();
        }
        if (!(vaultsMap.containsKey(uuid))) {
            vaultsMap.put(uuid,new HashMap<>());
        }

        if (!(vaultsMap.get(uuid).containsKey(number))) {
            vaultsMap.get(uuid).put(number,new ItemStack[0]);
        }

        ItemStack[] items = vaultsMap.get(uuid).get(number);
        return Optional.of(new VaultMeta(uuid,items,number));
    }

    public void saveVault(ItemStack[] contents, UUID uuid, int number) {
        vaultsMap.get(uuid).put(number, contents);
    }

}
