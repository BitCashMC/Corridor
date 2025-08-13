package gg.bitcash.corridor;

import com.zaxxer.hikari.HikariDataSource;
import gg.bitcash.corridor.components.datamanager.players.PlayerDAO;
import gg.bitcash.corridor.components.inventory.playervault.database.VaultDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Handles the connections to the databases, as well as serving as an access point for the various DAOs across the application
 */
public class DataSource {

    private final HikariDataSource connectionPool;
    private final PlayerDAO playerDAO;
    private final VaultDAO vaultDAO;
    private final Corridor instance;

    public Corridor getInstance() {
        return instance;
    }
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
    public DataSource(Corridor instance, String host, int port, String name, String username, String password) throws SQLException {
        this.instance = instance;

        final String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + name;

        Future<HikariDataSource> connPoolFuture = instance.getThreadService().runAsync(() -> {
            HikariDataSource hikariDataSource = new HikariDataSource();
            hikariDataSource.setJdbcUrl(jdbcUrl);
            hikariDataSource.setUsername(username);
            hikariDataSource.setPassword(password);

            return hikariDataSource;
        });

        playerDAO = new PlayerDAO(this,"players");
        vaultDAO = new VaultDAO(this,"vaults");

        try {
            connectionPool = connPoolFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new SQLException("Failed to connect to the database!");
        }

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
