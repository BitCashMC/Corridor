package gg.bitcash.corridor.components.inventory.playervault.commands;

import gg.bitcash.corridor.Corridor;
import gg.bitcash.corridor.CorridorUtils;
import gg.bitcash.corridor.components.inventory.playervault.VaultMeta;
import gg.bitcash.corridor.components.inventory.playervault.VaultUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Optional;
import java.util.UUID;

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

        final String targetUsername = args[1];
        Optional<UUID> usernameMatch = instance.getDataSource().getPlayerDAO().fetchUUID(targetUsername);

        if (usernameMatch.isEmpty()) {
            player.sendMessage("Error: No player '" + targetUsername + "' has joined the server. Verify inputted username and try again.");
            return true;
        }
        UUID uuid = usernameMatch.get();

        int num;
        if (!CorridorUtils.isInteger(args[2])) {
            return false;
        }
        num = Integer.parseInt(args[2]);

        VaultMeta vaultMeta = instance.getDataSource().getVaultDAO().fetchVault(uuid,num);
        Inventory inventory = VaultUtils.buildInventory(vaultMeta,targetUsername);
        player.openInventory(inventory);

        return true;
    }
}
