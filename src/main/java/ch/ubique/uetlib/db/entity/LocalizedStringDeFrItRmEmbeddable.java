package ch.ubique.uetlib.db.entity;

import ch.ubique.uetlib.core.localized.LocalizedValue;
import ch.ubique.uetlib.core.localized.LocalizedValueBuilder;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.jspecify.annotations.Nullable;

@Embeddable
public class LocalizedStringDeFrItRmEmbeddable implements LocalizedStringEmbeddableInterface {

    @Column(name = "de")
    private @Nullable String de;

    @Column(name = "fr")
    private @Nullable String fr;

    @Column(name = "it")
    private @Nullable String it;

    @Column(name = "rm")
    private @Nullable String rm;

    public LocalizedStringDeFrItRmEmbeddable(
            final @Nullable String de,
            final @Nullable String fr,
            final @Nullable String it,
            final @Nullable String rm) {
        this.de = de;
        this.fr = fr;
        this.it = it;
        this.rm = rm;
    }

    public LocalizedStringDeFrItRmEmbeddable() {}

    public static LocalizedStringDeFrItRmEmbeddable from(final @Nullable String s) {
        return new LocalizedStringDeFrItRmEmbeddable(s, s, s, s);
    }

    public static LocalizedStringDeFrItRmEmbeddable from(final LocalizedValue<String> lv) {
        return new LocalizedStringDeFrItRmEmbeddable(
                lv.deNullable(), lv.frNullable(), lv.itNullable(), lv.romanshNullable());
    }

    public @Nullable String getDe() {
        return de;
    }

    public void setDe(final @Nullable String de) {
        this.de = de;
    }

    public @Nullable String getFr() {
        return fr;
    }

    public void setFr(final @Nullable String fr) {
        this.fr = fr;
    }

    public @Nullable String getIt() {
        return it;
    }

    public void setIt(final @Nullable String it) {
        this.it = it;
    }

    public @Nullable String getRm() {
        return rm;
    }

    public void setRm(final @Nullable String rm) {
        this.rm = rm;
    }

    @Override
    public LocalizedValueBuilder<String> toLocalizedValueBuilder() {
        return LocalizedValue.<String>builder().de(de).fr(fr).it(it).romansh(rm);
    }
}
