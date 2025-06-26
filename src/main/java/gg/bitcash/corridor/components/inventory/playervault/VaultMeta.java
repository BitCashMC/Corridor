package gg.bitcash.corridor.components.inventory.playervault;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class VaultMeta {

    private final UUID uuid;
    private final int number;
    private ItemStack[] itemStacks;

    public VaultMeta(UUID uuid, int number, ItemStack[] itemStacks) {
        this.uuid = uuid;
        this.number = number;
        this.itemStacks = itemStacks;
    }

    public UUID uuid() {
        return this.uuid;
    }

    public int number() {
        return this.number;
    }

    public ItemStack[] itemStacks() {
        return this.itemStacks;
    }

    public void setItemStacks(ItemStack[] itemStacks) {
        this.itemStacks = itemStacks;
    }

}
