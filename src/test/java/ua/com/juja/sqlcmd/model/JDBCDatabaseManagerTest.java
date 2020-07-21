package ua.com.juja.sqlcmd.model;

public class JDBCDatabaseManagerTest extends DatabaseManagerTest {
    @Override
    protected DatabaseManager getDatabaseManager() {
        return new JDBCDatabaseManager();
    }
}
