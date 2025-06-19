package gg.bitcash.corridor.components.inventory.playervault.database;

import gg.bitcash.corridor.components.inventory.playervault.VaultManager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class VaultDataService {

    private final Map<UUID,Map<Integer, ItemStack[]>> vaultsMap;

    public VaultDataService() {
        vaultsMap = new HashMap<>();
    }

    public VaultMeta fetchVaultMeta(UUID uuid, int number) {

        if (!(vaultsMap.containsKey(uuid))) {
            vaultsMap.put(uuid,new HashMap<>());
        }

        if (!(vaultsMap.get(uuid).containsKey(number))) {
            vaultsMap.get(uuid).put(number,new ItemStack[0]);
        }

        ItemStack[] items = vaultsMap.get(uuid).get(number);
        return new VaultMeta(uuid,items,number);
    }

    public void saveVault(Inventory inventory, UUID uuid, int number) {
        vaultsMap.get(uuid).put(number, inventory.getContents());
    }

    public static class VaultMeta {

        private final UUID uuid;
        private final ItemStack[] itemStacks;
        private final int number;

        public VaultMeta(UUID uuid, ItemStack[] itemStacks, int number) {
            this.uuid = uuid;
            this.itemStacks = itemStacks;
            this.number = number;
        }

        public int getNumber() {
            return number;
        }

        public ItemStack[] getItemStacks() {
            return itemStacks;
        }

        public UUID getUuid() {
            return uuid;
        }

    }
}
