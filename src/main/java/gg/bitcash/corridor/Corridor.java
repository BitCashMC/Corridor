package gg.bitcash.corridor;

import gg.bitcash.corridor.components.datamanager.players.eventlisteners.PlayerUsernameChangeListener;
import gg.bitcash.corridor.components.inventory.playervault.commands.VaultCommandExecutor;
import gg.bitcash.corridor.components.inventory.playervault.eventlisteners.VaultCloseListener;
import gg.bitcash.corridor.components.sideboard.SideboardConfiguration;
import gg.bitcash.corridor.components.sideboard.SideboardHandler;
import gg.bitcash.corridor.components.sideboard.displaycondition.DisplayCondition;
import gg.bitcash.corridor.components.sideboard.eventlisteners.ShowScoreboardOnJoinListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.logging.Level;

/**
 * The driver class of the plugin. Its running Corridor instance serves as an access point for various data sources and other utils.
 */
public class Corridor extends JavaPlugin {

    private CorridorThreadService threadService = null;
    private CorridorDataSource connector = null;
    private DisplayCondition.ConditionRegistry conditionRegistry = null;
    private SideboardHandler sideboardHandler = null;

    public CorridorDataSource getDataSource() {
        return connector;
    }
    public CorridorThreadService getThreadService() {
        return threadService;
    }

    @Override
    public void onEnable() {
        threadService = new CorridorThreadService(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2));
        this.saveDefaultConfig();

        FileConfiguration config = this.getConfig();

        //database credentials:
        String host,name,username,password;
        int port;
        host = config.getString("database.host");
        port = config.getInt("database.port");
        name = config.getString("database.name");
        username = config.getString("database.user");
        password = config.getString("database.password");

        this.getLogger().log(Level.INFO,"Attempting to establish connection with database : " + name);

        try {
            connector = new CorridorDataSource(this,host,port,name,username,password);
        } catch (SQLException e) {
            e.printStackTrace();
            this.getServer().getPluginManager().disablePlugin(this);
        }

        this.getCommand("vault").setExecutor(new VaultCommandExecutor(this));

        initializeSideboards();

        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new VaultCloseListener(this),this);
        pluginManager.registerEvents(new PlayerUsernameChangeListener(this),this);
        pluginManager.registerEvents(new ShowScoreboardOnJoinListener(sideboardHandler),this);
    }

    private void initializeSideboards() {
        File file = new File(getDataFolder(),"scoreboards.yml");
        if (!file.exists()) {
            this.saveResource("scoreboards.yml",false);
        }
        sideboardHandler = new SideboardHandler(new SideboardConfiguration(YamlConfiguration.loadConfiguration(file)));
    }

    @Override
    public void onDisable() {
        this.connector.closeConnection();
    }

}
