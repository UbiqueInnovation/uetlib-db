package ch.ubique.backend.db.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ch.ubique.backend.core.localized.LocalizedValue;
import ch.ubique.backend.core.localized.NoValidValuesException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

@SuppressWarnings("DataFlowIssue")
class LocalizedStringDeFrItRmEmbeddableTest {

    @Test
    @SuppressWarnings("java:S5778")
    void toLocalizedValue() {
        final var localizedStringEmbeddable = new LocalizedStringDeFrItRmEmbeddable();
        assertThrows(NoValidValuesException.class, localizedStringEmbeddable::toLocalizedValue);
        localizedStringEmbeddable.setDe("");
        assertThrows(NoValidValuesException.class, localizedStringEmbeddable::toLocalizedValue);
        localizedStringEmbeddable.setDe("   ");
        assertThrows(NoValidValuesException.class, localizedStringEmbeddable::toLocalizedValue);
        localizedStringEmbeddable.setDe("DEUTSCH");
        assertEquals(
                LocalizedValue.de("DEUTSCH").toMapWithLocaleKeys(),
                localizedStringEmbeddable.toLocalizedValue().toMapWithLocaleKeys());
        localizedStringEmbeddable.setRm("ROMANSH");
        assertEquals(
                LocalizedValue.stringBuilder()
                        .de("DEUTSCH")
                        .romansh("ROMANSH")
                        .build()
                        .toMapWithLocaleKeys(),
                localizedStringEmbeddable.toLocalizedValue().toMapWithLocaleKeys());
    }

    @Test
    void toLocalizedValueNullable() {
        final var localizedStringEmbeddable = new LocalizedStringDeFrItRmEmbeddable();
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
        localizedStringEmbeddable.setRm("ROMANSH");
        assertEquals(
                LocalizedValue.stringBuilder()
                        .de("DEUTSCH")
                        .romansh("ROMANSH")
                        .build()
                        .toMapWithLocaleKeys(),
                localizedStringEmbeddable.toLocalizedValueNullable().toMapWithLocaleKeys());
    }

    @Test
    void toLocalizedValueOptional() {
        final var localizedStringEmbeddable = new LocalizedStringDeFrItRmEmbeddable();
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
        localizedStringEmbeddable.setRm("ROMANSH");
        assertEquals(
                LocalizedValue.stringBuilder()
                        .de("DEUTSCH")
                        .romansh("ROMANSH")
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
                LocalizedStringDeFrItRmEmbeddable.from((String) null).toLocalizedValueOptional());
        assertEquals(
                Optional.empty(),
                LocalizedStringDeFrItRmEmbeddable.from("").toLocalizedValueOptional());
        assertEquals(
                Optional.empty(),
                LocalizedStringDeFrItRmEmbeddable.from("  ").toLocalizedValueOptional());
        assertEquals(
                LocalizedValue.stringBuilder()
                        .de("TEXT")
                        .fr("TEXT")
                        .it("TEXT")
                        .romansh("TEXT")
                        .build()
                        .toMapWithLocaleKeys(),
                LocalizedStringDeFrItRmEmbeddable.from("TEXT")
                        .toLocalizedValueOptional()
                        .orElseThrow()
                        .toMapWithLocaleKeys());
    }

    @Test
    void fromLocalizedValue() {
        assertThrows(
                NullPointerException.class,
                () -> LocalizedStringDeFrItRmEmbeddable.from((LocalizedValue<String>) null));
        assertThrows(
                NoValidValuesException.class, () -> LocalizedValue.stringBuilder().de("").build());
        assertThrows(
                NoValidValuesException.class,
                () -> LocalizedValue.stringBuilder().de("  ").build());
        assertEquals(
                LocalizedValue.stringBuilder().de("DE").build(),
                LocalizedStringDeFrItRmEmbeddable.from(
                                LocalizedValue.stringBuilder().de("DE").build())
                        .toLocalizedValue());
        assertEquals(
                LocalizedValue.stringBuilder().de("DE").fr("FR").it("IT").romansh("RM").build(),
                LocalizedStringDeFrItRmEmbeddable.from(
                                LocalizedValue.stringBuilder()
                                        .de("DE")
                                        .fr("FR")
                                        .it("IT")
                                        .romansh("RM")
                                        .en("EN")
                                        .build())
                        .toLocalizedValue());
    }
}
