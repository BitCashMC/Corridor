package gg.bitcash.corridor;

import com.zaxxer.hikari.HikariDataSource;
import gg.bitcash.corridor.components.datamanager.players.PlayerDAO;
import gg.bitcash.corridor.components.inventory.playervault.database.VaultDAO;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Handles the connections to the databases, as well as serving as an access point for the various DAOs across the application
 */
public class CorridorDataSource {

    private final HikariDataSource connectionPool;
    private PlayerDAO playerDAO;
    private VaultDAO vaultDAO;

    /**
     * Gets a Connection instance from the connection pool.
     * @return a connection fetched from the connection pool.
     * @throws SQLException
     */
    public final Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

    public PlayerDAO getPlayerDAO() {
        return playerDAO;
    }

    public VaultDAO getVaultDAO() {
        return vaultDAO;
    }

    /**
     * Upon invokation, this constructor will initialize the HikariCP pool of connections, and establish a connection to the database with the parametrically provided credentials.
     * @param host The host of the database
     * @param port the port of the database
     * @param name The name of the database
     * @param username The username used to log into the database
     * @param password The password used to log into the database
     */
    public CorridorDataSource(String host, int port, String name, String username, String password) {
        final String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + name;
        connectionPool = new HikariDataSource();
        connectionPool.setJdbcUrl(jdbcUrl);
        connectionPool.setUsername(username);
        connectionPool.setPassword(password);

        playerDAO = new PlayerDAO(this,"players");
        vaultDAO = new VaultDAO(this,"vaults");

        try {
            playerDAO.initialize();
            vaultDAO.initialize();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Shut off the entire connection pool -- ONLY to be used on plugin disable.
     */
    public void closeConnection() {
        connectionPool.close();
    }
}
