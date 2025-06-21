package gg.bitcash.corridor;

import gg.bitcash.corridor.components.datamanager.players.PlayerDAO;
import gg.bitcash.corridor.components.datamanager.players.PlayerDataService;
import gg.bitcash.corridor.components.datamanager.players.eventlisteners.PlayerUsernameChangeListener;
import gg.bitcash.corridor.components.inventory.playervault.VaultManager;
import gg.bitcash.corridor.components.inventory.playervault.commands.OpenVault;
import gg.bitcash.corridor.components.inventory.playervault.database.VaultDataService;
import gg.bitcash.corridor.components.inventory.playervault.eventlisteners.VaultCloseListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Corridor extends JavaPlugin {

    private CorridorDataSource connector = null;

    private VaultDataService vaultDataService = null;
    private PlayerDataService playerDataService = null;

    private VaultManager vaultManager = null;

    public PlayerDataService getPlayerDataService() {
        return playerDataService;
    }

    public VaultDataService getVaultDataService() {
        return vaultDataService;
    }

    public VaultManager getVaultManager() {
        return vaultManager;
    }

    @Override
    public void onEnable() {

        this.saveDefaultConfig();

        FileConfiguration config = getConfig();

        //database credentials:
        String host,name,username,password;
        int port;
        host = config.getString("database.host");
        port = config.getInt("database.port");
        name = config.getString("database.name");
        username = config.getString("database.user");
        password = config.getString("database.password");

        this.getLogger().log(Level.INFO,"Attempting to establish connection with database : " + name);
        connector = CorridorDataSource.buildDataSource(host,port,name,username,password);
        vaultDataService = new VaultDataService(); //Pending DAO
        vaultManager = new VaultManager(vaultDataService);

        playerDataService = new PlayerDataService(connector.buildDAO(PlayerDAO.class));

        this.getCommand("vault").setExecutor(new OpenVault(this));

        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new VaultCloseListener(this),this);
        pluginManager.registerEvents(new PlayerUsernameChangeListener(this),this);
    }

    @Override
    public void onDisable() {
        this.getLogger().log(Level.INFO,"Saving the current Player UUID cache layer to Database..");
        this.playerDataService.saveToDatabase();
        this.connector.closeDatabase_connection();
    }

}
