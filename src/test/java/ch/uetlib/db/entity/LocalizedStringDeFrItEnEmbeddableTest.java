package ch.uetlib.db.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ch.uetlib.core.localized.LocalizedValue;
import ch.uetlib.core.localized.NoValidValuesException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

@SuppressWarnings("DataFlowIssue")
class LocalizedStringDeFrItEnEmbeddableTest {

    @Test
    @SuppressWarnings("java:S5778")
    void toLocalizedValue() {
        final var localizedStringEmbeddable = new LocalizedStringDeFrItEnEmbeddable();
        assertThrows(NoValidValuesException.class, localizedStringEmbeddable::toLocalizedValue);
        localizedStringEmbeddable.setDe("");
        assertThrows(NoValidValuesException.class, localizedStringEmbeddable::toLocalizedValue);
        localizedStringEmbeddable.setDe("   ");
        assertThrows(NoValidValuesException.class, localizedStringEmbeddable::toLocalizedValue);
        localizedStringEmbeddable.setDe("DEUTSCH");
        assertEquals(
                LocalizedValue.de("DEUTSCH").toMapWithLocaleKeys(),
                localizedStringEmbeddable.toLocalizedValue().toMapWithLocaleKeys());
        localizedStringEmbeddable.setEn("ENGLISH");
        assertEquals(
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
        assertEquals(
                LocalizedValue.de("DEUTSCH").toMapWithLocaleKeys(),
                localizedStringEmbeddable.toLocalizedValueNullable().toMapWithLocaleKeys());
        localizedStringEmbeddable.setEn("ENGLISH");
        assertEquals(
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
        assertEquals(Optional.empty(), localizedStringEmbeddable.toLocalizedValueOptional());
        localizedStringEmbeddable.setDe(" ");
        assertEquals(Optional.empty(), localizedStringEmbeddable.toLocalizedValueOptional());
        localizedStringEmbeddable.setDe("DEUTSCH");
        assertEquals(
                LocalizedValue.de("DEUTSCH").toMapWithLocaleKeys(),
                localizedStringEmbeddable
                        .toLocalizedValueOptional()
                        .orElseThrow()
                        .toMapWithLocaleKeys());
        localizedStringEmbeddable.setEn("ENGLISH");
        assertEquals(
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
    void fromString() {
        assertEquals(
                Optional.empty(),
                LocalizedStringDeFrItEnEmbeddable.from((String) null).toLocalizedValueOptional());
        assertEquals(
                Optional.empty(),
                LocalizedStringDeFrItEnEmbeddable.from("").toLocalizedValueOptional());
        assertEquals(
                Optional.empty(),
                LocalizedStringDeFrItEnEmbeddable.from("  ").toLocalizedValueOptional());
        assertEquals(
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

    @Test
    void fromLocalizedValue() {
        assertThrows(
                NullPointerException.class,
                () -> LocalizedStringDeFrItEnEmbeddable.from((LocalizedValue<String>) null));
        assertThrows(
                NoValidValuesException.class, () -> LocalizedValue.stringBuilder().de("").build());
        assertThrows(
                NoValidValuesException.class,
                () -> LocalizedValue.stringBuilder().de("  ").build());
        assertEquals(
                LocalizedValue.stringBuilder().de("DE").build(),
                LocalizedStringDeFrItEnEmbeddable.from(
                                LocalizedValue.stringBuilder().de("DE").build())
                        .toLocalizedValue());
        assertEquals(
                LocalizedValue.stringBuilder().de("DE").fr("FR").it("IT").en("EN").build(),
                LocalizedStringDeFrItEnEmbeddable.from(
                                LocalizedValue.stringBuilder()
                                        .de("DE")
                                        .fr("FR")
                                        .it("IT")
                                        .en("EN")
                                        .romansh("RM")
                                        .build())
                        .toLocalizedValue());
    }
}
