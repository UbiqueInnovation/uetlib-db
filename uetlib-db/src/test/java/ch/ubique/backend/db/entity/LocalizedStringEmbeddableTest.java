package ch.ubique.backend.db.entity;

import ch.ubique.backend.core.localized.LocalizedValue;
import ch.ubique.backend.core.localized.NoValidValuesException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LocalizedStringEmbeddableTest {

    @Test
    @SuppressWarnings("java:S5778")
    void toLocalizedValue() {
        final var localizedStringEmbeddable = new LocalizedStringEmbeddable();
        Assertions.assertThrows(
                NoValidValuesException.class, localizedStringEmbeddable::toLocalizedValue);
        localizedStringEmbeddable.setDe(" ");
        Assertions.assertThrows(
                NoValidValuesException.class, localizedStringEmbeddable::toLocalizedValue);
        localizedStringEmbeddable.setDe("DEUTSCH");
        Assertions.assertEquals(
                LocalizedValue.de("DEUTSCH").values(),
                localizedStringEmbeddable.toLocalizedValue().values());
        localizedStringEmbeddable.setIt("ITALIAN");
        Assertions.assertEquals(
                LocalizedValue.stringBuilder().de("DEUTSCH").it("ITALIAN").build().values(),
                localizedStringEmbeddable.toLocalizedValue().values());
    }

    @Test
    void toLocalizedValueNullable() {
        final var localizedStringEmbeddable = new LocalizedStringEmbeddable();
        Assertions.assertNull(localizedStringEmbeddable.toLocalizedValueNullable());
        localizedStringEmbeddable.setDe(" ");
        Assertions.assertNull(localizedStringEmbeddable.toLocalizedValueNullable());
        localizedStringEmbeddable.setDe("DEUTSCH");
        Assertions.assertEquals(
                LocalizedValue.de("DEUTSCH").values(),
                localizedStringEmbeddable.toLocalizedValueNullable().values());
        localizedStringEmbeddable.setIt("ITALIAN");
        Assertions.assertEquals(
                LocalizedValue.stringBuilder().de("DEUTSCH").it("ITALIAN").build().values(),
                localizedStringEmbeddable.toLocalizedValueNullable().values());
    }
}
