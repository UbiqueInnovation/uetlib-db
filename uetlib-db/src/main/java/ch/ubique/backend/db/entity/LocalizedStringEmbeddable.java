package ch.ubique.backend.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class LocalizedStringEmbeddable implements LocalizedStringInterface {

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

    @Override
    public String getDe() {
        return de;
    }

    public void setDe(final String de) {
        this.de = de;
    }

    @Override
    public String getFr() {
        return fr;
    }

    public void setFr(final String fr) {
        this.fr = fr;
    }

    @Override
    public String getIt() {
        return it;
    }

    public void setIt(final String it) {
        this.it = it;
    }

    @Override
    public String getEn() {
        return en;
    }

    public void setEn(final String en) {
        this.en = en;
    }
}
