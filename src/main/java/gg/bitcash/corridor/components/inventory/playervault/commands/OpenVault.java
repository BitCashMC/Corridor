package gg.bitcash.corridor.components.inventory.playervault.commands;

import gg.bitcash.corridor.Corridor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.logging.Level;

public class OpenVault implements CommandExecutor {

    private final Corridor instance;

    public OpenVault(Corridor instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player player)) { return false; }

        int num = 1;
        try {
            num = Integer.parseInt(strings[0]);
        } catch (NumberFormatException e) {
            instance.getLogger().log(Level.FINE, "Vault number '" + strings[0] + "' not a valid argument. Please choose a natural number.");
        }

        Inventory inventory = instance.getDataService().sendInventory(player.getUniqueId(),num);
        player.openInventory(player.openInventory(inventory));

        return true;
    }

}
