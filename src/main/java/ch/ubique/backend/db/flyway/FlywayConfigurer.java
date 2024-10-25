package ch.ubique.backend.db.flyway;

import ch.ubique.backend.db.jdbc.VendorLocationResolver;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Duration;

import javax.sql.DataSource;

/**
 * Spring Boot does not some provide a mechanism to create a {@link Flyway} given {@link
 * FlywayProperties} when not using {@link
 * org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration}. This class executes
 * {@link Flyway#configure()} and applies all the given {@link FlywayProperties} to it, as-well as
 * optionally executing the migrations.
 */
@SuppressWarnings("unused")
public class FlywayConfigurer {

    private FlywayConfigurer() {}

    public static FluentConfiguration configureFromFlywayProperties(
            final DataSource dataSource, final FlywayProperties flywayProperties) {
        final var fluentConfiguration = Flyway.configure().dataSource(dataSource);
        configureProperties(fluentConfiguration, flywayProperties);
        return fluentConfiguration;
    }

    public static Flyway configureAndMigrateFromFlywayProperties(
            final DataSource dataSource, final FlywayProperties flywayProperties) {
        final var flyway = configureFromFlywayProperties(dataSource, flywayProperties).load();
        flyway.migrate();
        return flyway;
    }

    /**
     * Extracted and adapted from {@link
     * org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration.FlywayConfiguration#configureProperties}
     */
    private static void configureProperties(
            final FluentConfiguration fluentConfiguration,
            final FlywayProperties flywayProperties) {
        final var map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        final var locations =
                new VendorLocationResolver(fluentConfiguration.getDataSource())
                        .resolveLocations(flywayProperties.getLocations())
                        .toArray(new String[0]);
        map.from(flywayProperties.isFailOnMissingLocations())
                .to(fluentConfiguration::failOnMissingLocations);
        map.from(locations).to(fluentConfiguration::locations);
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
        map.from(flywayProperties.isCleanOnValidationError())
                .to(fluentConfiguration::cleanOnValidationError);
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
    }
}
