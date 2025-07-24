package gg.bitcash.corridor.components.datamanager.players;

import gg.bitcash.corridor.CorridorDAO;
import gg.bitcash.corridor.CorridorDataSource;

import java.sql.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class PlayerDAO implements CorridorDAO {

    private final CorridorDataSource dataSource;
    private String tableName = null;
    private boolean initialized;

    public PlayerDAO(CorridorDataSource corridorDataSource, String tableName) {
        this.dataSource = corridorDataSource;
        this.tableName = tableName;
    }

    @Override
    public void initialize() {
        try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement("CREATE TABLE IF NOT EXISTS "+ tableName + " (uuid CHAR(36) PRIMARY KEY, username VARCHAR(36) UNIQUE)")) {
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

    public void putPlayer(UUID uuid, String username) {
        Runnable op = () -> {
            try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO " + this.tableName + " VALUES(?,?) ON DUPLICATE KEY UPDATE username = ?")){
                stmt.setString(1,uuid.toString());
                stmt.setString(2,username);
                stmt.setString(3,username);
                stmt.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
        dataSource.getInstance().getThreadService().getThreadPool().submit(op);
    }

    public Future<Optional<String>> fetchUsername(UUID uuid) {
        Callable<Optional<String>> op = () -> {
            try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT username FROM " + this.tableName + " WHERE uuid = ?")) {
                stmt.setString(1,uuid.toString());
                ResultSet resultSet = stmt.executeQuery();
                if (resultSet.next()) {
                    String username = resultSet.getString("username");
                    return Optional.of(username);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return Optional.empty();
        };
        return dataSource.getInstance().getThreadService().getThreadPool().submit(op);
    }

    public Future<Optional<UUID>> fetchUUID(String username) {
        Callable<Optional<UUID>> op = () -> {
            try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT uuid FROM " + this.tableName + " WHERE username = ?")) {
                stmt.setString(1,username);
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(UUID.fromString(rs.getString("uuid")));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return Optional.empty();
        };
        return dataSource.getInstance().getThreadService().getThreadPool().submit(op);
    }
}
