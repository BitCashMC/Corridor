package gg.bitcash.corridor.components.inventory.playervault.database;

import gg.bitcash.corridor.CorridorDAO;
import gg.bitcash.corridor.CorridorDataSource;
import gg.bitcash.corridor.components.inventory.playervault.VaultMeta;
import gg.bitcash.corridor.components.inventory.playervault.VaultUtils;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class VaultDAO implements CorridorDAO {

    private String tableName = null;
    private boolean initialized = false;
    private final CorridorDataSource dataSource;

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

    public VaultDAO(CorridorDataSource dataSource, String tableName) {
        this.dataSource = dataSource;
        this.tableName = tableName;
    }

    public void putVault(VaultMeta vaultMeta) {
        Runnable op = () -> {
            try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO " + tableName + " VALUES(?,?,?) ON DUPLICATE KEY UPDATE serialized_vault_contents = ?")) {
                byte[] serializedItems = VaultUtils.serializeInventory(vaultMeta.itemStacks());
                stmt.setString(1,vaultMeta.uuid().toString());
                stmt.setInt(2,vaultMeta.number());
                stmt.setBytes(3,serializedItems);
                stmt.setBytes(4,serializedItems);
                stmt.execute();

                synchronized(vaultMeta) {
                    vaultMeta.notifyAll();
                }

            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        };
        dataSource.getInstance().getThreadService().getThreadPool().submit(op);
    }

    public Future<VaultMeta> fetchVault(UUID uuid, int number) {
        Callable<VaultMeta> op = () -> {
            try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM "+ tableName +" WHERE owner_uuid = ? AND vault_number = ?")) {
                stmt.setString(1,uuid.toString());
                stmt.setInt(2,number);
                ResultSet rs = stmt.executeQuery();

                if (!rs.next()) {
                    VaultMeta vaultMeta = new VaultMeta(uuid,number,new ItemStack[54]);
                    putVault(vaultMeta);

                    synchronized (vaultMeta) {
                        vaultMeta.wait(5000L);
                    }
                    rs = stmt.executeQuery();

                    if (!rs.next()) {
                        throw new SQLException("Expected vault to exist after insertion, but no result returned.");
                    }
                }

                UUID fetchedUUID = UUID.fromString(rs.getString("owner_uuid"));
                int fetchedNumber = rs.getInt("vault_number");
                ItemStack[] fetchedDeserializedContents = VaultUtils.deserializeInventory(rs.getBytes("serialized_vault_contents"));
                return new VaultMeta(fetchedUUID,fetchedNumber,fetchedDeserializedContents);
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                return null;
            }
        };
        return dataSource.getInstance().getThreadService().getThreadPool().submit(op);
    }
}
