package gg.bitcash.corridor;

import gg.bitcash.corridor.components.inventory.playervault.commands.OpenVault;
import gg.bitcash.corridor.components.inventory.playervault.database.VaultDataService;
import gg.bitcash.corridor.components.inventory.playervault.eventlisteners.VaultCloseListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Corridor extends JavaPlugin {

    private VaultDataService dataService = null;

    public VaultDataService getDataService() {
        return dataService;
    }

    @Override
    public void onEnable() {
        dataService = new VaultDataService();

        this.getCommand("vault").setExecutor(new OpenVault(this));
        this.getServer().getPluginManager().registerEvents(new VaultCloseListener(this),this);
    }

}
