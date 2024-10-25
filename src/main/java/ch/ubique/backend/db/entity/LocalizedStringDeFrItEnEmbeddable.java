package ch.ubique.backend.db.entity;

import ch.ubique.backend.core.localized.LocalizedValue;
import ch.ubique.backend.core.localized.LocalizedValueBuilder;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class LocalizedStringDeFrItEnEmbeddable implements LocalizedStringEmbeddableInterface {

    @Column(name = "de")
    private String de;

    @Column(name = "fr")
    private String fr;

    @Column(name = "it")
    private String it;

    @Column(name = "en")
    private String en;

    public LocalizedStringDeFrItEnEmbeddable(
            final String de, final String fr, final String it, final String en) {
        this.de = de;
        this.fr = fr;
        this.it = it;
        this.en = en;
    }

    public LocalizedStringDeFrItEnEmbeddable() {}

    public static LocalizedStringDeFrItEnEmbeddable from(final String s) {
        return new LocalizedStringDeFrItEnEmbeddable(s, s, s, s);
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

    @Override
    public LocalizedValueBuilder<String> toLocalizedValueBuilder() {
        return LocalizedValue.<String>builder().de(getDe()).fr(getFr()).it(getIt()).en(getEn());
    }
}
