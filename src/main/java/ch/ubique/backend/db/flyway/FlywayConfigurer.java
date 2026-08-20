package ch.ubique.backend.db.flyway;

import ch.ubique.backend.db.jdbc.VendorLocationResolver;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.boot.flyway.autoconfigure.FlywayAutoConfiguration;
import org.springframework.boot.flyway.autoconfigure.FlywayProperties;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;

import javax.sql.DataSource;

/**
 * Spring Boot does not some provide a mechanism to create a {@link Flyway} given {@link
 * FlywayProperties} when not using {@link FlywayAutoConfiguration}. This class executes {@link
 * Flyway#configure()} and applies all the given {@link FlywayProperties} to it, as-well as
 * optionally executing the migrations.
 */
@SuppressWarnings("unused")
public final class FlywayConfigurer {

    private FlywayConfigurer() {}

    public static FluentConfiguration configureFromFlywayProperties(
            final FluentConfiguration fluentConfiguration,
            final FlywayProperties flywayProperties) {
        configureProperties(fluentConfiguration, flywayProperties);
        return fluentConfiguration;
    }

    public static Flyway configureAndMigrateFromFlywayProperties(
            final FluentConfiguration fluentConfiguration,
            final FlywayProperties flywayProperties) {
        final var flyway =
                configureFromFlywayProperties(fluentConfiguration, flywayProperties).load();
        flyway.migrate();
        return flyway;
    }

    public static FluentConfiguration configureFromFlywayProperties(
            final DataSource dataSource, final FlywayProperties flywayProperties) {
        return configureFromFlywayProperties(
                Flyway.configure().dataSource(dataSource), flywayProperties);
    }

    public static Flyway configureAndMigrateFromFlywayProperties(
            final DataSource dataSource, final FlywayProperties flywayProperties) {
        return configureAndMigrateFromFlywayProperties(
                Flyway.configure().dataSource(dataSource), flywayProperties);
    }

    /**
     * Extracted and adapted from {@link
     * FlywayAutoConfiguration.FlywayConfiguration#configureProperties}
     */
    private static void configureProperties(
            final FluentConfiguration fluentConfiguration,
            final FlywayProperties flywayProperties) {
        final var map = PropertyMapper.get();
        final var locations =
                new VendorLocationResolver(fluentConfiguration.getDataSource())
                        .resolveLocations(flywayProperties.getLocations())
                        .toArray(String[]::new);
        fluentConfiguration.locations(locations);
        map.from(flywayProperties.getCallbackLocations())
                .when(callbackLocations -> !ObjectUtils.isEmpty(callbackLocations))
                .to(
                        callbackLocations ->
                                fluentConfiguration.callbackLocations(
                                        new VendorLocationResolver(
                                                        fluentConfiguration.getDataSource())
                                                .resolveLocations(callbackLocations)
                                                .toArray(String[]::new)));
        map.from(flywayProperties.isFailOnMissingLocations())
                .to(fluentConfiguration::failOnMissingLocations);
        map.from(flywayProperties.getEncoding()).to(fluentConfiguration::encoding);
        map.from(flywayProperties.getConnectRetries()).to(fluentConfiguration::connectRetries);
        map.from(flywayProperties.getConnectRetriesInterval())
                .as(Duration::getSeconds)
                .as(Long::intValue)
                .to(fluentConfiguration::connectRetriesInterval);
        map.from(flywayProperties.getLockRetryCount()).to(fluentConfiguration::lockRetryCount);
        map.from(flywayProperties.getDefaultSchema()).to(fluentConfiguration::defaultSchema);
        map.from(flywayProperties.getSchemas())
                .as(StringUtils::toStringArray)
                .to(fluentConfiguration::schemas);
        map.from(flywayProperties.isCreateSchemas()).to(fluentConfiguration::createSchemas);
        map.from(flywayProperties.getTable()).to(fluentConfiguration::table);
        map.from(flywayProperties.getTablespace()).to(fluentConfiguration::tablespace);
        map.from(flywayProperties.getBaselineDescription())
                .to(fluentConfiguration::baselineDescription);
        map.from(flywayProperties.getBaselineVersion()).to(fluentConfiguration::baselineVersion);
        map.from(flywayProperties.getInstalledBy()).to(fluentConfiguration::installedBy);
        map.from(flywayProperties.getPlaceholders()).to(fluentConfiguration::placeholders);
        map.from(flywayProperties.getPlaceholderPrefix())
                .to(fluentConfiguration::placeholderPrefix);
        map.from(flywayProperties.getPlaceholderSuffix())
                .to(fluentConfiguration::placeholderSuffix);
        map.from(flywayProperties.getPlaceholderSeparator())
                .to(fluentConfiguration::placeholderSeparator);
        map.from(flywayProperties.isPlaceholderReplacement())
                .to(fluentConfiguration::placeholderReplacement);
        map.from(flywayProperties.getSqlMigrationPrefix())
                .to(fluentConfiguration::sqlMigrationPrefix);
        map.from(flywayProperties.getSqlMigrationSuffixes())
                .as(StringUtils::toStringArray)
                .to(fluentConfiguration::sqlMigrationSuffixes);
        map.from(flywayProperties.getSqlMigrationSeparator())
                .to(fluentConfiguration::sqlMigrationSeparator);
        map.from(flywayProperties.getRepeatableSqlMigrationPrefix())
                .to(fluentConfiguration::repeatableSqlMigrationPrefix);
        map.from(flywayProperties.getTarget()).to(fluentConfiguration::target);
        map.from(flywayProperties.isBaselineOnMigrate()).to(fluentConfiguration::baselineOnMigrate);
        map.from(flywayProperties.isCleanDisabled()).to(fluentConfiguration::cleanDisabled);
        map.from(flywayProperties.isGroup()).to(fluentConfiguration::group);
        map.from(flywayProperties.isMixed()).to(fluentConfiguration::mixed);
        map.from(flywayProperties.isOutOfOrder()).to(fluentConfiguration::outOfOrder);
        map.from(flywayProperties.isSkipDefaultCallbacks())
                .to(fluentConfiguration::skipDefaultCallbacks);
        map.from(flywayProperties.isSkipDefaultResolvers())
                .to(fluentConfiguration::skipDefaultResolvers);
        map.from(flywayProperties.isValidateMigrationNaming())
                .to(fluentConfiguration::validateMigrationNaming);
        map.from(flywayProperties.isValidateOnMigrate()).to(fluentConfiguration::validateOnMigrate);
        map.from(flywayProperties.getInitSqls())
                .whenNot(CollectionUtils::isEmpty)
                .as(initSQLs -> StringUtils.collectionToDelimitedString(initSQLs, "\n"))
                .to(fluentConfiguration::initSql);
        map.from(flywayProperties.getScriptPlaceholderPrefix())
                .to(fluentConfiguration::scriptPlaceholderPrefix);
        map.from(flywayProperties.getScriptPlaceholderSuffix())
                .to(fluentConfiguration::scriptPlaceholderSuffix);
        map.from(flywayProperties.getPowershellExecutable())
                .to(fluentConfiguration::powershellExecutable);
        configureExecuteInTransaction(fluentConfiguration, flywayProperties, map);
        Objects.requireNonNull(flywayProperties);
        map.from(flywayProperties::getLoggers).to(fluentConfiguration::loggers);
        Objects.requireNonNull(flywayProperties);
        map.from(flywayProperties::getCommunityDbSupportEnabled)
                .to(fluentConfiguration::communityDBSupportEnabled);
        map.from(flywayProperties.getBatch()).to(fluentConfiguration::batch);
        map.from(flywayProperties.getDryRunOutput()).to(fluentConfiguration::dryRunOutput);
        map.from(flywayProperties.getErrorOverrides()).to(fluentConfiguration::errorOverrides);
        map.from(flywayProperties.getStream()).to(fluentConfiguration::stream);
        map.from(flywayProperties.getJdbcProperties())
                .whenNot(Map::isEmpty)
                .to(fluentConfiguration::jdbcProperties);
        map.from(flywayProperties.getKerberosConfigFile())
                .to(fluentConfiguration::kerberosConfigFile);
        map.from(flywayProperties.getOutputQueryResults())
                .to(fluentConfiguration::outputQueryResults);
        map.from(flywayProperties.getSkipExecutingMigrations())
                .to(fluentConfiguration::skipExecutingMigrations);
        map.from(flywayProperties.getIgnoreMigrationPatterns())
                .to(
                        ignoreMigrationPatterns ->
                                fluentConfiguration.ignoreMigrationPatterns(
                                        ignoreMigrationPatterns.toArray(String[]::new)));
        map.from(flywayProperties.getDetectEncoding()).to(fluentConfiguration::detectEncoding);
    }

    private static void configureExecuteInTransaction(
            final FluentConfiguration fluentConfiguration,
            final FlywayProperties flywayProperties,
            final PropertyMapper map) {
        try {
            map.from(flywayProperties.isExecuteInTransaction())
                    .to(fluentConfiguration::executeInTransaction);
        } catch (final NoSuchMethodError ex) {
            // Flyway < 9.14
        }
    }
}
