package gg.bitcash.corridor.components.inventory.playervault.commands;

import gg.bitcash.corridor.Corridor;
import gg.bitcash.corridor.CorridorUtils;
import gg.bitcash.corridor.components.inventory.playervault.VaultMeta;
import gg.bitcash.corridor.components.inventory.playervault.VaultUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class AdminOpenVaultCommandHandler implements CommandHandler {

    private final Corridor instance;

    public AdminOpenVaultCommandHandler(Corridor instance) {
        this.instance = instance;
    }

    @Override
    public boolean handle(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player player)) {
            return false;
        }

        int num;
        if (!CorridorUtils.isInteger(args[2])) {
            return false;
        }
        num = Integer.parseInt(args[2]);

        final String targetUsername = args[1];
        Bukkit.getScheduler().runTaskAsynchronously(instance,()->{
            try {
                Optional<UUID> usernameMatch = instance.getDataSource().getPlayerDAO().fetchUUID(targetUsername).get();
                if (usernameMatch.isEmpty()) {
                    Bukkit.getScheduler().runTask(instance,()->player.sendMessage("Error: No player '" + targetUsername + "' has joined the server. Verify inputted username and try again."));
                    return;
                }
                UUID uuid = usernameMatch.get();
                VaultMeta vaultMeta = instance.getDataSource().getVaultDAO().fetchVault(uuid,num).get();
                Inventory inventory = VaultUtils.buildInventory(vaultMeta,targetUsername);
                Bukkit.getScheduler().runTask(instance,()->player.openInventory(inventory));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        return true;
    }
}
