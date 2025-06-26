package gg.bitcash.corridor.components.inventory.playervault.commands;

import gg.bitcash.corridor.Corridor;
import gg.bitcash.corridor.components.inventory.playervault.VaultMeta;
import gg.bitcash.corridor.components.inventory.playervault.VaultUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class OpenVaultCommandHandler implements CommandHandler {

    private final Corridor instance;

    public OpenVaultCommandHandler(Corridor instance) {
        this.instance = instance;
    }

    @Override
    public boolean handle(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player player)) {
            return false;
        }

        int num = Integer.parseInt(args[0]);
        VaultMeta vaultMeta = instance.getDataSource().getVaultDAO().fetchVault(player.getUniqueId(),num);
        Inventory inv = VaultUtils.buildInventory(vaultMeta,player.getName());
        player.openInventory(inv);

        return true;
    }
}
