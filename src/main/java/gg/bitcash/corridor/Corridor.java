package gg.bitcash.corridor;

import gg.bitcash.corridor.components.datamanager.players.eventlisteners.PlayerUsernameChangeListener;
import gg.bitcash.corridor.components.inventory.playervault.commands.VaultCommandExecutor;
import gg.bitcash.corridor.components.inventory.playervault.eventlisteners.VaultCloseListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 * The driver class of the plugin. Its running Corridor instance serves as an access point for various data sources and other utils.
 */
public class Corridor extends JavaPlugin {

    private CorridorDataSource connector = null;

    public CorridorDataSource getDataSource() {
        return connector;
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
        connector = new CorridorDataSource(host,port,name,username,password);

        this.getCommand("vault").setExecutor(new VaultCommandExecutor(this));

        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new VaultCloseListener(this),this);
        pluginManager.registerEvents(new PlayerUsernameChangeListener(this),this);
    }

    @Override
    public void onDisable() {
        this.getLogger().log(Level.INFO,"Saving the current Player UUID cache layer to Database..");
        this.connector.closeConnection();
    }

}
