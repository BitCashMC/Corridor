package gg.bitcash.corridor.components.inventory.playervault;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public record VaultMeta(UUID uuid, ItemStack[] itemStacks, int number) {}
