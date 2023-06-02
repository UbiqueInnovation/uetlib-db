package ch.ubique.backend.db.sqlite;

import ch.ubique.backend.core.file.temp.TempFileManager;

import org.apache.commons.io.IOUtils;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.sqlite.SQLiteDataSource;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import javax.sql.DataSource;

public class SQLiteHelper {

    private static final Logger logger = LoggerFactory.getLogger(SQLiteHelper.class);

    private SQLiteHelper() {}

    /**
     * A SQLite datasource created in memory with location string ":memory:" instead of a file name
     * is written to a temp file with the given name
     *
     * @throws SQLException
     * @throws IOException
     */
    public static void writeSQLiteMemoryDBToTempFile(
            final DataSource sqliteDataSource, final String dbName)
            throws SQLException, IOException {
        final var conn = sqliteDataSource.getConnection();
        try (final Statement stmt = conn.createStatement()) {
            final var dbPath =
                    Path.of(TempFileManager.createTempDir().toAbsolutePath().toString(), dbName)
                            .toString();
            logger.info("SQLiteDB written to {}", dbPath);
            stmt.executeUpdate("backup to " + dbPath);
        }
    }

    public static SQLiteDB createSQLiteDbInTempDir(
            final String dbName, final String pathToSchema, final int schemaVersion)
            throws IOException, SQLException, ClassNotFoundException {
        final var dbPath =
                Path.of(TempFileManager.createTempDir().toAbsolutePath().toString(), dbName)
                        .toString();
        return createSQLiteDb(dbPath, pathToSchema, schemaVersion);
    }

    /**
     * Creates the SQLite db and returns the datasource. To improve performance, autocommit must be
     * disabled. For this we wrap the datasource and always set autocommit to false when returning a
     * connection. Unfortunately in combination with Springs {@link JdbcTemplate} writes do not get
     * committed anymore. Therefore, we also wrap the returned Connections, so that we can commit
     * the connection first, before we close it.
     *
     * @param schemaVersion is written in to the defined 23-bit integer at offset 60 of the SQLite
     *     DB
     * @return a DataSource object for this SQLite DB
     * @throws SQLException, ClassNotFoundException, IOException
     */
    public static SQLiteDB createSQLiteDb(
            final String dbPath, final String pathToSchema, final int schemaVersion)
            throws SQLException, ClassNotFoundException, IOException {
        logger.info("Init SQLite DB: {}", dbPath);
        Files.deleteIfExists(Paths.get(dbPath));
        Class.forName("org.sqlite.JDBC");
        final SQLiteDataSource datasource = new SQLiteDataSource();
        datasource.setUserVersion(schemaVersion);
        datasource.setUrl("jdbc:sqlite:" + dbPath);
        final var conn = datasource.getConnection();
        final var res = new ClassPathResource(pathToSchema);
        final var sqlInit = IOUtils.toString(res.getInputStream(), StandardCharsets.UTF_8);
        // This allows us to read the file as a whole. Split and execute single statements.
        final var statements = sqlInit.split(";");
        try (final var stat = conn.createStatement()) {
            for (final String statement : statements) {
                logger.trace("Exec sql statement: {}", statement);
                stat.executeUpdate(statement);
            }
        }
        return new SQLiteDB(new DisabledAutoCommitDataSource(datasource), dbPath);
    }

    private static class DisabledAutoCommitDataSource implements DataSource {

        private final DataSource ds;

        public DisabledAutoCommitDataSource(final DataSource datasource) {
            this.ds = datasource;
        }

        @Override
        public PrintWriter getLogWriter() throws SQLException {
            return ds.getLogWriter();
        }

        @Override
        public void setLogWriter(final PrintWriter out) throws SQLException {
            ds.setLogWriter(out);
        }

        @Override
        public int getLoginTimeout() throws SQLException {
            return ds.getLoginTimeout();
        }

        @Override
        public void setLoginTimeout(final int seconds) throws SQLException {
            ds.setLoginTimeout(seconds);
        }

        @Override
        public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return ds.getParentLogger();
        }

        @Override
        public <T> T unwrap(final Class<T> iface) throws SQLException {
            return ds.unwrap(iface);
        }

        @Override
        public boolean isWrapperFor(final Class<?> iface) throws SQLException {
            return ds.isWrapperFor(iface);
        }

        @Override
        @SuppressWarnings("java:S2095")
        public Connection getConnection() throws SQLException {
            final var con = ds.getConnection();
            con.setAutoCommit(false);
            return new CommitOnCloseConnection(con);
        }

        @Override
        @SuppressWarnings("java:S2095")
        public Connection getConnection(final String username, final String password)
                throws SQLException {
            final var con = ds.getConnection(username, password);
            con.setAutoCommit(false);
            return new CommitOnCloseConnection(con);
        }
    }

    @SuppressWarnings("ClassCanBeRecord")
    private static class CommitOnCloseConnection implements Connection {

        private final Connection c;

        public CommitOnCloseConnection(final Connection c) {
            this.c = c;
        }

        public <T> T unwrap(final Class<T> iface) throws SQLException {
            return c.unwrap(iface);
        }

        public boolean isWrapperFor(final Class<?> iface) throws SQLException {
            return c.isWrapperFor(iface);
        }

        public Statement createStatement() throws SQLException {
            return c.createStatement();
        }

        public PreparedStatement prepareStatement(final String sql) throws SQLException {
            return c.prepareStatement(sql);
        }

        public CallableStatement prepareCall(final String sql) throws SQLException {
            return c.prepareCall(sql);
        }

        public String nativeSQL(final String sql) throws SQLException {
            return c.nativeSQL(sql);
        }

        public boolean getAutoCommit() throws SQLException {
            return c.getAutoCommit();
        }

        public void setAutoCommit(final boolean autoCommit) throws SQLException {
            c.setAutoCommit(autoCommit);
        }

        public void commit() throws SQLException {
            c.commit();
        }

        public void rollback() throws SQLException {
            c.rollback();
        }

        public void close() throws SQLException {
            c.commit();
            c.close();
        }

        public boolean isClosed() throws SQLException {
            return c.isClosed();
        }

        public DatabaseMetaData getMetaData() throws SQLException {
            return c.getMetaData();
        }

        public boolean isReadOnly() throws SQLException {
            return c.isReadOnly();
        }

        public void setReadOnly(final boolean readOnly) throws SQLException {
            c.setReadOnly(readOnly);
        }

        public String getCatalog() throws SQLException {
            return c.getCatalog();
        }

        public void setCatalog(final String catalog) throws SQLException {
            c.setCatalog(catalog);
        }

        public int getTransactionIsolation() throws SQLException {
            return c.getTransactionIsolation();
        }

        public void setTransactionIsolation(final int level) throws SQLException {
            c.setTransactionIsolation(level);
        }

        public SQLWarning getWarnings() throws SQLException {
            return c.getWarnings();
        }

        public void clearWarnings() throws SQLException {
            c.clearWarnings();
        }

        public Statement createStatement(final int resultSetType, final int resultSetConcurrency)
                throws SQLException {
            return c.createStatement(resultSetType, resultSetConcurrency);
        }

        public PreparedStatement prepareStatement(
                final String sql, final int resultSetType, final int resultSetConcurrency)
                throws SQLException {
            return c.prepareStatement(sql, resultSetType, resultSetConcurrency);
        }

        public CallableStatement prepareCall(
                final String sql, final int resultSetType, final int resultSetConcurrency)
                throws SQLException {
            return c.prepareCall(sql, resultSetType, resultSetConcurrency);
        }

        public Map<String, Class<?>> getTypeMap() throws SQLException {
            return c.getTypeMap();
        }

        public void setTypeMap(final Map<String, Class<?>> map) throws SQLException {
            c.setTypeMap(map);
        }

        public int getHoldability() throws SQLException {
            return c.getHoldability();
        }

        public void setHoldability(final int holdability) throws SQLException {
            c.setHoldability(holdability);
        }

        public Savepoint setSavepoint() throws SQLException {
            return c.setSavepoint();
        }

        public Savepoint setSavepoint(final String name) throws SQLException {
            return c.setSavepoint(name);
        }

        public void rollback(final Savepoint savepoint) throws SQLException {
            c.rollback(savepoint);
        }

        public void releaseSavepoint(final Savepoint savepoint) throws SQLException {
            c.releaseSavepoint(savepoint);
        }

        public Statement createStatement(
                final int resultSetType,
                final int resultSetConcurrency,
                final int resultSetHoldability)
                throws SQLException {
            return c.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        public PreparedStatement prepareStatement(
                final String sql,
                final int resultSetType,
                final int resultSetConcurrency,
                final int resultSetHoldability)
                throws SQLException {
            return c.prepareStatement(
                    sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        public CallableStatement prepareCall(
                final String sql,
                final int resultSetType,
                final int resultSetConcurrency,
                final int resultSetHoldability)
                throws SQLException {
            return c.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        public PreparedStatement prepareStatement(final String sql, final int autoGeneratedKeys)
                throws SQLException {
            return c.prepareStatement(sql, autoGeneratedKeys);
        }

        public PreparedStatement prepareStatement(final String sql, final int[] columnIndexes)
                throws SQLException {
            return c.prepareStatement(sql, columnIndexes);
        }

        public PreparedStatement prepareStatement(final String sql, final String[] columnNames)
                throws SQLException {
            return c.prepareStatement(sql, columnNames);
        }

        public Clob createClob() throws SQLException {
            return c.createClob();
        }

        public Blob createBlob() throws SQLException {
            return c.createBlob();
        }

        public NClob createNClob() throws SQLException {
            return c.createNClob();
        }

        public SQLXML createSQLXML() throws SQLException {
            return c.createSQLXML();
        }

        public boolean isValid(final int timeout) throws SQLException {
            return c.isValid(timeout);
        }

        public void setClientInfo(final String name, final String value)
                throws SQLClientInfoException {
            c.setClientInfo(name, value);
        }

        public String getClientInfo(final String name) throws SQLException {
            return c.getClientInfo(name);
        }

        public Properties getClientInfo() throws SQLException {
            return c.getClientInfo();
        }

        public void setClientInfo(final Properties properties) throws SQLClientInfoException {
            c.setClientInfo(properties);
        }

        public Array createArrayOf(final String typeName, final Object[] elements)
                throws SQLException {
            return c.createArrayOf(typeName, elements);
        }

        public Struct createStruct(final String typeName, final Object[] attributes)
                throws SQLException {
            return c.createStruct(typeName, attributes);
        }

        public String getSchema() throws SQLException {
            return c.getSchema();
        }

        public void setSchema(final String schema) throws SQLException {
            c.setSchema(schema);
        }

        public void abort(final Executor executor) throws SQLException {
            c.abort(executor);
        }

        public void setNetworkTimeout(final Executor executor, final int milliseconds)
                throws SQLException {
            c.setNetworkTimeout(executor, milliseconds);
        }

        public int getNetworkTimeout() throws SQLException {
            return c.getNetworkTimeout();
        }
    }
}
