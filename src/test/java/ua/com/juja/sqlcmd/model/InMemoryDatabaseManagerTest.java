package ua.com.juja.sqlcmd.model;

public class InMemoryDatabaseManagerTest extends DatabaseManagerTest {
    @Override
    protected DatabaseManager getDatabaseManager() {
        return new InMemoryDatabaseManager();
    }
}
