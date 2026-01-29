package ch.ubique.backend.db.sqlite;

import javax.sql.DataSource;

public record SQLiteDB(DataSource dataSource, String dbPath) {}
