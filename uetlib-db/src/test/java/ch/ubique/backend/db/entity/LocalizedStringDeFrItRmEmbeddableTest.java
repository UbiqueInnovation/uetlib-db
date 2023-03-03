package ch.ubique.backend.db.entity;

import ch.ubique.backend.core.localized.LocalizedValue;
import ch.ubique.backend.core.localized.NoValidValuesException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LocalizedStringDeFrItRmEmbeddableTest {

    @Test
    @SuppressWarnings("java:S5778")
    void toLocalizedValue() {
        final var localizedStringEmbeddable = new LocalizedStringDeFrItRmEmbeddable();
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
                LocalizedValue.de("DEUTSCH").values(),
                localizedStringEmbeddable.toLocalizedValue().values());
        localizedStringEmbeddable.setRm("ROMANSH");
        Assertions.assertEquals(
                LocalizedValue.stringBuilder().de("DEUTSCH").romansh("ROMANSH").build().values(),
                localizedStringEmbeddable.toLocalizedValue().values());
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
        Assertions.assertEquals(
                LocalizedValue.de("DEUTSCH").values(),
                localizedStringEmbeddable.toLocalizedValueNullable().values());
        localizedStringEmbeddable.setRm("ROMANSH");
        Assertions.assertEquals(
                LocalizedValue.stringBuilder().de("DEUTSCH").romansh("ROMANSH").build().values(),
                localizedStringEmbeddable.toLocalizedValueNullable().values());
    }

    @Test
    void toLocalizedValueOptional() {
        final var localizedStringEmbeddable = new LocalizedStringDeFrItRmEmbeddable();
        Assertions.assertEquals(
                Optional.empty(), localizedStringEmbeddable.toLocalizedValueOptional());
        localizedStringEmbeddable.setDe(" ");
        Assertions.assertEquals(
                Optional.empty(), localizedStringEmbeddable.toLocalizedValueOptional());
        localizedStringEmbeddable.setDe("DEUTSCH");
        Assertions.assertEquals(
                LocalizedValue.de("DEUTSCH").values(),
                localizedStringEmbeddable.toLocalizedValueOptional().orElseThrow().values());
        localizedStringEmbeddable.setRm("ROMANSH");
        Assertions.assertEquals(
                LocalizedValue.stringBuilder().de("DEUTSCH").romansh("ROMANSH").build().values(),
                localizedStringEmbeddable.toLocalizedValueOptional().orElseThrow().values());
    }

    @Test
    void from() {
        Assertions.assertEquals(
                Optional.empty(),
                LocalizedStringDeFrItRmEmbeddable.from(null).toLocalizedValueOptional());
        Assertions.assertEquals(
                Optional.empty(),
                LocalizedStringDeFrItRmEmbeddable.from("").toLocalizedValueOptional());
        Assertions.assertEquals(
                Optional.empty(),
                LocalizedStringDeFrItRmEmbeddable.from("  ").toLocalizedValueOptional());
        Assertions.assertEquals(
                LocalizedValue.stringBuilder()
                        .de("TEXT")
                        .fr("TEXT")
                        .it("TEXT")
                        .romansh("TEXT")
                        .build()
                        .values(),
                LocalizedStringDeFrItRmEmbeddable.from("TEXT")
                        .toLocalizedValueOptional()
                        .orElseThrow()
                        .values());
    }
}
