package ch.ubique.backend.db.sqlite;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SQLiteHelperTest {

    @Test
    void testCreateSQLiteDb() {
        Assertions.assertDoesNotThrow(
                () ->
                        SQLiteHelper.createSQLiteDbInTempDir(
                                "test.sql", "db/migration/sqlite/test.sql", 1));
    }
}
