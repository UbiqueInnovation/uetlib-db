package ch.ubique.backend.db.sqlite;

import javax.sql.DataSource;

public class SQLiteDB {
    private final DataSource dataSource;
    private final String dbPath;

    public SQLiteDB(DataSource dataSource, String dbPath) {
        this.dataSource = dataSource;
        this.dbPath = dbPath;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public String getDbPath() {
        return dbPath;
    }
}
