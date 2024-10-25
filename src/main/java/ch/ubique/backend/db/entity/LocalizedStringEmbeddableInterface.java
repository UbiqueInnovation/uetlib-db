package ch.ubique.backend.db.entity;

import ch.ubique.backend.core.localized.LocalizedValue;
import ch.ubique.backend.core.localized.LocalizedValueBuilder;
import ch.ubique.backend.core.localized.NoValidValuesException;

import java.util.Optional;

public interface LocalizedStringEmbeddableInterface {

    /**
     * @throws NoValidValuesException if all entries are {@code null} or empty.
     *     <p>Use {@link #toLocalizedValueNullable()} or {@link #toLocalizedValueOptional()} if this
     *     is not desired.
     */
    default LocalizedValue<String> toLocalizedValue() {
        return toLocalizedValueBuilder().build();
    }

    /**
     * @return {@code null} if all entries are {@code null} or empty
     *     <p>Use {@link #toLocalizedValue()} to throw {@link NoValidValuesException} or {@link
     *     #toLocalizedValueOptional()} to return an {@link Optional}.
     */
    default LocalizedValue<String> toLocalizedValueNullable() {
        return toLocalizedValueBuilder().buildNullable();
    }

    /**
     * @return {@code null} if all entries are {@code null} or empty.
     *     <p>Use {@link #toLocalizedValue()} to throw {@link NoValidValuesException} or {@link
     *     #toLocalizedValueNullable()} to return {@code null}.
     */
    default Optional<LocalizedValue<String>> toLocalizedValueOptional() {
        return toLocalizedValueBuilder().buildOptional();
    }

    LocalizedValueBuilder<String> toLocalizedValueBuilder();
}
