package ch.uetlib.db.sqlite;

import javax.sql.DataSource;

public record SQLiteDB(DataSource dataSource, String dbPath) {}
