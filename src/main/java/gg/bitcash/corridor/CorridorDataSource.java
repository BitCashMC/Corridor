package gg.bitcash.corridor;

import gg.bitcash.corridor.components.datamanager.players.PlayerDAO;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CorridorDataSource {

    private Connection database_connection;
    // Map of all the tables pertaining to every DAO. This may need revision if/when individual DAOs use more tables (not hard).
    private Map<Class<? extends CorridorDAO>,String> tablesMap;

    public final Connection getDatabase_connection() {
        return database_connection;
    }

    private CorridorDataSource() {}

    public static CorridorDataSource buildDataSource(String host, int port, String name, String username, String password) {
        CorridorDataSource dataSource = new CorridorDataSource();
        String url = "jdbc:mysql://" + host + ":" + port + "/" + name;
        try {
            dataSource.database_connection = attemptConnection(url,username,password);
            initializeTables(dataSource.database_connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dataSource.tablesMap = buildTablesMap();

        return dataSource;
    }

    private static Connection attemptConnection(String url,String username, String password) throws SQLException {
        return DriverManager.getConnection(url,username,password);
    }

    private static void initializeTables(Connection conn) {
        try {
            conn.prepareStatement("CREATE TABLE IF NOT EXISTS players (uuid CHAR(36) PRIMARY KEY, username VARCHAR(36) UNIQUE)").execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Map<Class<? extends CorridorDAO>,String> buildTablesMap() {
        Map<Class<? extends CorridorDAO>,String> tablesMap = new HashMap<>();
        tablesMap.put(PlayerDAO.class,"players");

        return tablesMap;
    }

    public Map<Class<? extends CorridorDAO>, String> getTablesMap() {
        return tablesMap;
    }

    public void closeDatabase_connection() {
        new Thread(() -> {
            try {
                Thread.sleep(4500);
                database_connection.close();
            } catch (SQLException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public <T extends CorridorDAO> T buildDAO(Class<T> clazz) {
        T instance;
        try {
            instance = clazz.getConstructor(CorridorDataSource.class).newInstance(this);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return instance;
    }
}
