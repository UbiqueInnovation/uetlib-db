package ch.ubique.backend.db.entity;

import ch.ubique.backend.core.localized.LocalizedValue;
import ch.ubique.backend.core.localized.NoValidValuesException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class LocalizedStringDeFrItEnEmbeddableTest {

    @Test
    @SuppressWarnings("java:S5778")
    void toLocalizedValue() {
        final var localizedStringEmbeddable = new LocalizedStringDeFrItEnEmbeddable();
        Assertions.assertThrows(
                NoValidValuesException.class, localizedStringEmbeddable::toLocalizedValue);
        localizedStringEmbeddable.setDe("");
        Assertions.assertThrows(
                NoValidValuesException.class, localizedStringEmbeddable::toLocalizedValue);
        localizedStringEmbeddable.setDe("   ");
        Assertions.assertThrows(
                NoValidValuesException.class, localizedStringEmbeddable::toLocalizedValue);
        localizedStringEmbeddable.setDe("DEUTSCH");
        Assertions.assertEquals(
                LocalizedValue.de("DEUTSCH").toMapWithLocaleKeys(),
                localizedStringEmbeddable.toLocalizedValue().toMapWithLocaleKeys());
        localizedStringEmbeddable.setEn("ENGLISH");
        Assertions.assertEquals(
                LocalizedValue.stringBuilder()
                        .de("DEUTSCH")
                        .en("ENGLISH")
                        .build()
                        .toMapWithLocaleKeys(),
                localizedStringEmbeddable.toLocalizedValue().toMapWithLocaleKeys());
    }

    @Test
    void toLocalizedValueNullable() {
        final var localizedStringEmbeddable = new LocalizedStringDeFrItEnEmbeddable();
        Assertions.assertNull(localizedStringEmbeddable.toLocalizedValueNullable());
        localizedStringEmbeddable.setDe(null);
        Assertions.assertNull(localizedStringEmbeddable.toLocalizedValueNullable());
        localizedStringEmbeddable.setDe("");
        Assertions.assertNull(localizedStringEmbeddable.toLocalizedValueNullable());
        localizedStringEmbeddable.setDe("   ");
        Assertions.assertNull(localizedStringEmbeddable.toLocalizedValueNullable());
        localizedStringEmbeddable.setDe("DEUTSCH");
        Assertions.assertEquals(
                LocalizedValue.de("DEUTSCH").toMapWithLocaleKeys(),
                localizedStringEmbeddable.toLocalizedValueNullable().toMapWithLocaleKeys());
        localizedStringEmbeddable.setEn("ENGLISH");
        Assertions.assertEquals(
                LocalizedValue.stringBuilder()
                        .de("DEUTSCH")
                        .en("ENGLISH")
                        .build()
                        .toMapWithLocaleKeys(),
                localizedStringEmbeddable.toLocalizedValueNullable().toMapWithLocaleKeys());
    }

    @Test
    void toLocalizedValueOptional() {
        final var localizedStringEmbeddable = new LocalizedStringDeFrItEnEmbeddable();
        Assertions.assertEquals(
                Optional.empty(), localizedStringEmbeddable.toLocalizedValueOptional());
        localizedStringEmbeddable.setDe(" ");
        Assertions.assertEquals(
                Optional.empty(), localizedStringEmbeddable.toLocalizedValueOptional());
        localizedStringEmbeddable.setDe("DEUTSCH");
        Assertions.assertEquals(
                LocalizedValue.de("DEUTSCH").toMapWithLocaleKeys(),
                localizedStringEmbeddable
                        .toLocalizedValueOptional()
                        .orElseThrow()
                        .toMapWithLocaleKeys());
        localizedStringEmbeddable.setEn("ENGLISH");
        Assertions.assertEquals(
                LocalizedValue.stringBuilder()
                        .de("DEUTSCH")
                        .en("ENGLISH")
                        .build()
                        .toMapWithLocaleKeys(),
                localizedStringEmbeddable
                        .toLocalizedValueOptional()
                        .orElseThrow()
                        .toMapWithLocaleKeys());
    }

    @Test
    void from() {
        Assertions.assertEquals(
                Optional.empty(),
                LocalizedStringDeFrItEnEmbeddable.from(null).toLocalizedValueOptional());
        Assertions.assertEquals(
                Optional.empty(),
                LocalizedStringDeFrItEnEmbeddable.from("").toLocalizedValueOptional());
        Assertions.assertEquals(
                Optional.empty(),
                LocalizedStringDeFrItEnEmbeddable.from("  ").toLocalizedValueOptional());
        Assertions.assertEquals(
                LocalizedValue.stringBuilder()
                        .de("TEXT")
                        .fr("TEXT")
                        .it("TEXT")
                        .en("TEXT")
                        .build()
                        .toMapWithLocaleKeys(),
                LocalizedStringDeFrItEnEmbeddable.from("TEXT")
                        .toLocalizedValueOptional()
                        .orElseThrow()
                        .toMapWithLocaleKeys());
    }
}
