package gg.bitcash.corridor.components.inventory.playervault.commands;

import gg.bitcash.corridor.Corridor;
import gg.bitcash.corridor.CorridorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class VaultCommandExecutor implements CommandExecutor {

    private final Corridor instance;
    private final OpenVaultCommandHandler openVaultCommandHandler;
    private final AdminOpenVaultCommandHandler adminOpenVaultCommandHandler;

    public VaultCommandExecutor(Corridor instance) {
        this.instance = instance;
        this.openVaultCommandHandler = new OpenVaultCommandHandler(instance);
        this.adminOpenVaultCommandHandler = new AdminOpenVaultCommandHandler(instance);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (CorridorUtils.isInteger(strings[0])) {
            return openVaultCommandHandler.handle(commandSender,strings);
        }
        if (strings[0].equalsIgnoreCase("adminopen")) {
            return adminOpenVaultCommandHandler.handle(commandSender,strings);
        }
        return false;
    }
}
