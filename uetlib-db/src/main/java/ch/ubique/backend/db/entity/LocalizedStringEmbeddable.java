package ch.ubique.backend.db.entity;

import ch.ubique.backend.core.localized.LocalizedValue;
import ch.ubique.backend.core.localized.LocalizedValueBuilder;
import ch.ubique.backend.core.localized.NoValidValuesException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Optional;

@Embeddable
public class LocalizedStringEmbeddable {

    @Column(name = "de")
    private String de;

    @Column(name = "fr")
    private String fr;

    @Column(name = "it")
    private String it;

    @Column(name = "en")
    private String en;

    public LocalizedStringEmbeddable(
            final String de, final String fr, final String it, final String en) {
        this.de = de;
        this.fr = fr;
        this.it = it;
        this.en = en;
    }

    public LocalizedStringEmbeddable() {}

    public static LocalizedStringEmbeddable from(final String s) {
        return new LocalizedStringEmbeddable(s, s, s, s);
    }

    public String getDe() {
        return de;
    }

    public void setDe(final String de) {
        this.de = de;
    }

    public String getFr() {
        return fr;
    }

    public void setFr(final String fr) {
        this.fr = fr;
    }

    public String getIt() {
        return it;
    }

    public void setIt(final String it) {
        this.it = it;
    }

    public String getEn() {
        return en;
    }

    public void setEn(final String en) {
        this.en = en;
    }

    /**
     * @throws NoValidValuesException if all entries are {@code null} or empty.
     *     <p>Use {@link #toLocalizedValueNullable()} or {@link #toLocalizedValueOptional()} if this
     *     is not desired.
     */
    public LocalizedValue<String> toLocalizedValue() {
        return toLocalizedValueBuilder().build();
    }

    /**
     * @return {@code null} if all entries are {@code null} or empty
     *     <p>Use {@link #toLocalizedValue()} to throw {@link NoValidValuesException} or {@link
     *     #toLocalizedValueOptional()} to return an {@link Optional}.
     */
    public LocalizedValue<String> toLocalizedValueNullable() {
        return toLocalizedValueBuilder().buildNullable();
    }

    /**
     * @return {@code null} if all entries are {@code null} or empty.
     *     <p>Use {@link #toLocalizedValue()} to throw {@link NoValidValuesException} or {@link
     *     #toLocalizedValueNullable()} to return {@code null}.
     */
    public Optional<LocalizedValue<String>> toLocalizedValueOptional() {
        return toLocalizedValueBuilder().buildOptional();
    }

    private LocalizedValueBuilder<String> toLocalizedValueBuilder() {
        return LocalizedValue.<String>builder().de(getDe()).fr(getFr()).it(getIt()).en(getEn());
    }
}
