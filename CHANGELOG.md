# CHANGELOG

## 3.0.0 (unreleased)

### Breaking changes

- Move Maven coordinates from `ch.ubique:uetlib-*` to `ch.ubique.uetlib:uetlib-*`.
- Align uetlib parent, BOM and library references with the migration releases.

## 2.3.1

- latest uetlib parent
- version updates

## 2.3.0

- New methods `FlywayConfigurer.configureFromFlywayProperties/configureAndMigrateFromFlywayProperties` that accept a
  `FluentConfiguration` instead of a `DataSource` to allow Flyway configuration/migration wihtout a `DataSource`, e.g.
  for MongoDB native connector migrations.
- Latest `uetlib` dependencies

## 2.2.1

- updated actions to not run tests twice for sonar
- update uetlib version

## 2.2.0

### Improvements

- Add `LocalizedStringDeFrItEnEmbeddable::from(LocalizedValue)` and
  `LocalizedStringDeFrItRmEmbeddable::from(LocalizedValue)`

## 2.1.0

### Improvements

- Null-marked it all
- Updated `uetlib-core`, `uetlib-spring-boot`, `uetlib-parent` and `uetlib-test` to their latest versions.

## 2.0.0

### Improvements

- Support for Spring Boot 4.0

## 1.1.0

### Improvements

- Latest uetlib dependencies with Spring Boot 3.5 support

## 1.0.0

First release. The code was moved
from [UbiqueInnovation/backend-jvm-lib](https://github.com/UbiqueInnovation/backend-jvm-lib).

