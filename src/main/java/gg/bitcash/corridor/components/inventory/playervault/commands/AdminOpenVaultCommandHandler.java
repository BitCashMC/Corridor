package gg.bitcash.corridor.components.inventory.playervault.commands;

import gg.bitcash.corridor.Corridor;
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

        String targetUsername = args[1];
        Optional<UUID> usernameMatch = instance.getPlayerDataService().getUUID(targetUsername);

        if (usernameMatch.isEmpty()) {
            return false;
        }
        UUID uuid = usernameMatch.get();

        int num;
        try {
            num = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            return false;
        }

        Optional<VaultMeta> vaultMetaOpt = instance.getVaultDataService().fetchVaultMeta(uuid,num);
        if (vaultMetaOpt.isEmpty()) {
            return false;
        }

        Inventory inventory = VaultUtils.buildInventory(instance,vaultMetaOpt.get());
        player.openInventory(inventory);

        return true;
    }
}
