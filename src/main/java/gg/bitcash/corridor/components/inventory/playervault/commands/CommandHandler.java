package gg.bitcash.corridor.components.inventory.playervault.commands;

import org.bukkit.command.CommandSender;

public interface CommandHandler {

    boolean handle(CommandSender sender, String[] args);
}
