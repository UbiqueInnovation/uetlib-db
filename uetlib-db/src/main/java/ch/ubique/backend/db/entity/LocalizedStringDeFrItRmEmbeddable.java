package ch.ubique.backend.db.entity;

import ch.ubique.backend.core.localized.LocalizedValue;
import ch.ubique.backend.core.localized.LocalizedValueBuilder;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class LocalizedStringDeFrItRmEmbeddable implements LocalizedStringEmbeddableInterface {

    @Column(name = "de")
    private String de;

    @Column(name = "fr")
    private String fr;

    @Column(name = "it")
    private String it;

    @Column(name = "rm")
    private String rm;

    public LocalizedStringDeFrItRmEmbeddable(
            final String de, final String fr, final String it, final String rm) {
        this.de = de;
        this.fr = fr;
        this.it = it;
        this.rm = rm;
    }

    public LocalizedStringDeFrItRmEmbeddable() {}

    public static LocalizedStringDeFrItRmEmbeddable from(final String s) {
        return new LocalizedStringDeFrItRmEmbeddable(s, s, s, s);
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

    public String getRm() {
        return rm;
    }

    public void setRm(final String rm) {
        this.rm = rm;
    }

    @Override
    public LocalizedValueBuilder<String> toLocalizedValueBuilder() {
        return LocalizedValue.<String>builder().de(de).fr(fr).it(it).romansh(rm);
    }
}
