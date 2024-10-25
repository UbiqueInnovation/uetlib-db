package ch.ubique.backend.db.jdbc;

import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;

import java.sql.DatabaseMetaData;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

/**
 * Extracted and adapted from {@link
 * org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration.LocationResolver}
 */
public record VendorLocationResolver(DataSource dataSource) {

    private static final String VENDOR_PLACEHOLDER = "{vendor}";

    public List<String> resolveLocations(final List<String> locations) {
        if (usesVendorLocation(locations)) {
            final var databaseDriver = getDatabaseDriver();
            return replaceVendorLocations(locations, databaseDriver);
        }
        return locations;
    }

    private List<String> replaceVendorLocations(
            final List<String> locations, final DatabaseDriver databaseDriver) {
        if (databaseDriver == DatabaseDriver.UNKNOWN) {
            return locations;
        }
        final var vendor = databaseDriver.getId();
        return locations.stream()
                .map(location -> location.replace(VENDOR_PLACEHOLDER, vendor))
                .toList();
    }

    private DatabaseDriver getDatabaseDriver() {
        try {
            final var url = JdbcUtils.extractDatabaseMetaData(dataSource, DatabaseMetaData::getURL);
            return DatabaseDriver.fromJdbcUrl(url);
        } catch (final MetaDataAccessException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private boolean usesVendorLocation(final Collection<String> locations) {
        return locations.stream().anyMatch(location -> location.contains(VENDOR_PLACEHOLDER));
    }
}
