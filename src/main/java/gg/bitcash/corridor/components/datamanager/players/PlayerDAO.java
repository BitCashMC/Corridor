package gg.bitcash.corridor.components.datamanager.players;

import gg.bitcash.corridor.CorridorDAO;
import gg.bitcash.corridor.CorridorDataSource;

import java.sql.*;
import java.util.*;

public class PlayerDAO implements CorridorDAO {

    private final CorridorDataSource dataSource;
    private final String table;

    public PlayerDAO(CorridorDataSource corridorDataSource) {
        this.dataSource = corridorDataSource;
        this.table = this.dataSource.getTablesMap().get(PlayerDAO.class);
    }

    public boolean putPlayer(UUID uuid, String username) {
        try (PreparedStatement stmt = this.dataSource.getDatabase_connection().prepareStatement("INSERT INTO " + this.table + " VALUES(?,?) ON DUPLICATE KEY UPDATE username = ?")){
            stmt.setString(1,uuid.toString());
            stmt.setString(2,username);
            stmt.setString(3,username);
            return stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Optional<String> fetchUsername(UUID uuid) {
        try (PreparedStatement stmt = this.dataSource.getDatabase_connection().prepareStatement("SELECT username FROM " + this.table + " WHERE uuid = ?")) {
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
    }

    // For the DataService Cache Layer
    public Map<UUID,String> fetchAll() {
        Map<UUID,String> uuidUsernamePairs = new HashMap<>();
        try (PreparedStatement stmt = this.dataSource.getDatabase_connection().prepareStatement("SELECT uuid,username FROM " + this.table)){
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                String username = resultSet.getString("username");
                uuidUsernamePairs.put(uuid,username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return uuidUsernamePairs;
    }

    public boolean putAll(Map<UUID,String> playerMap) {
        boolean success = true;
        for (Map.Entry<UUID,String> row : playerMap.entrySet()) {
            success &= putPlayer(row.getKey(),row.getValue());
        }
        return success;
    }
}
