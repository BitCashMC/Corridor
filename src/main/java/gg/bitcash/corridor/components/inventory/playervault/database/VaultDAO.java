package gg.bitcash.corridor.components.inventory.playervault.database;

import gg.bitcash.corridor.DAO;
import gg.bitcash.corridor.DataSource;
import gg.bitcash.corridor.State;
import gg.bitcash.corridor.ThreadService;
import gg.bitcash.corridor.components.inventory.playervault.VaultMeta;
import gg.bitcash.corridor.components.inventory.playervault.VaultUtils;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class VaultDAO implements DAO {

    private final String tableName;
    private boolean initialized = false;
    private final DataSource dataSource;

    @Override
    public void initialize() throws SQLException {
        if (!dataSource.getPlayerDAO().isInitialized()) {
            throw new SQLException("Attempted to initialize vaults table before the player table");
        }
        String playerTableName = dataSource.getPlayerDAO().getTableName();
        String sqlQuery = "CREATE TABLE IF NOT EXISTS " + tableName + "(owner_uuid CHAR(36), vault_number INT, serialized_vault_contents BLOB NOT NULL, FOREIGN KEY(owner_uuid) REFERENCES " + playerTableName + "(uuid), PRIMARY KEY(owner_uuid,vault_number))";
        try (
            Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlQuery)
        ) {
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initialized = true;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    public VaultDAO(DataSource dataSource, String tableName) {
        this.dataSource = dataSource;
        this.tableName = tableName;
    }

    public Future<State> putVault(State mode, VaultMeta vaultMeta) {
        Runnable op = () -> {
            try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO " + tableName + " VALUES(?,?,?) ON DUPLICATE KEY UPDATE serialized_vault_contents = ?")) {
                byte[] serializedItems = VaultUtils.serializeInventory(vaultMeta.itemStacks());
                stmt.setString(1,vaultMeta.uuid().toString());
                stmt.setInt(2,vaultMeta.number());
                stmt.setBytes(3,serializedItems);
                stmt.setBytes(4,serializedItems);
                stmt.execute();

            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        };
        ThreadService service = dataSource.getInstance().getThreadService();
        return mode == State.ASYNC ? service.runAsync(op) : service.run(op);
    }

    /**
     * Executes a query to fetch and assemble a VaultMeta instance by inputted UUID and int number. It will also fallback to inserting a new vault if no vault was found with the specified credentials.
     *
     * @param uuid
     * @param number
     * @return
     */
    public Future<VaultMeta> fetchVault(UUID uuid, int number) {
        Callable<VaultMeta> op = () -> {
            try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM "+ tableName +" WHERE owner_uuid = ? AND vault_number = ?")) {
                stmt.setString(1,uuid.toString());
                stmt.setInt(2,number);
                ResultSet rs = stmt.executeQuery();
                // In the event that no entry is found from the query, a fallback operation will occur in which a new, empty vault will be inserted with the aforementioned uuid and number.
                if (!rs.next()) {
                    VaultMeta vaultMeta = new VaultMeta(uuid,number,new ItemStack[54]);
                    //Storing the computation as a Future; ThreadService will return null if the computation did not complete.
                    Future<State> fallback = putVault(State.SYNC,vaultMeta);
                    //Block the thread until the computation has finished. Assign the ResultSet to null if the computation failed, otherwise re-execute the query.
                    rs = fallback.get() != null ? stmt.executeQuery() : null;

                    if (rs == null || !rs.next()) {
                        throw new SQLException("Expected vault to exist after insertion, but no result returned.");
                    }
                }
                // Proceed as usual from here:
                UUID fetchedUUID = UUID.fromString(rs.getString("owner_uuid"));
                int fetchedNumber = rs.getInt("vault_number");
                ItemStack[] fetchedDeserializedContents = VaultUtils.deserializeInventory(rs.getBytes("serialized_vault_contents"));
                // Return the packaged vaultmeta
                return new VaultMeta(fetchedUUID,fetchedNumber,fetchedDeserializedContents);
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                return null;
            }
        };
        return dataSource.getInstance().getThreadService().runAsync(op);
    }
}
