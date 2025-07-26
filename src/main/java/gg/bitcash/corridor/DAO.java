package gg.bitcash.corridor;

import java.sql.SQLException;
/**
 * Abstracts the DAOs across the application
 */
public interface DAO {

    void initialize() throws SQLException;

    String getTableName();

    boolean isInitialized();

}
