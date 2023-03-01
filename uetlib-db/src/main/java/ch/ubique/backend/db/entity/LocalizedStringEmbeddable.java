package ch.ubique.backend.db.entity;

import ch.ubique.backend.core.localized.LocalizedValue;
import ch.ubique.backend.core.localized.NoValidValuesException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

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
     * @throws NoValidValuesException if all entries are {@code null} or empty. Use {@link
     *     #toLocalizedValueNullable()} if {@code null} is a valid response.
     */
    public LocalizedValue<String> toLocalizedValue() {
        return LocalizedValue.<String>builder()
                .de(getDe())
                .fr(getFr())
                .it(getIt())
                .en(getEn())
                .build();
    }

    /**
     * @return {@code null} if all entries are {@code null} or empty. Use {@link
     *     #toLocalizedValue()} to throw {@link NoValidValuesException} in that case.
     */
    public LocalizedValue<String> toLocalizedValueNullable() {
        return LocalizedValue.<String>builder()
                .de(getDe())
                .fr(getFr())
                .it(getIt())
                .en(getEn())
                .buildNullable();
    }
}
