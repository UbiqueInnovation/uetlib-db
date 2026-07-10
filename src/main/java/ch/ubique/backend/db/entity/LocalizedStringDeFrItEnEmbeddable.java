package ch.ubique.backend.db.entity;

import ch.ubique.backend.core.localized.LocalizedValue;
import ch.ubique.backend.core.localized.LocalizedValueBuilder;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import org.jspecify.annotations.Nullable;

@Embeddable
public class LocalizedStringDeFrItEnEmbeddable implements LocalizedStringEmbeddableInterface {

    @Column(name = "de")
    private @Nullable String de;

    @Column(name = "fr")
    private @Nullable String fr;

    @Column(name = "it")
    private @Nullable String it;

    @Column(name = "en")
    private @Nullable String en;

    public LocalizedStringDeFrItEnEmbeddable(
            final @Nullable String de,
            final @Nullable String fr,
            final @Nullable String it,
            final @Nullable String en) {
        this.de = de;
        this.fr = fr;
        this.it = it;
        this.en = en;
    }

    public LocalizedStringDeFrItEnEmbeddable() {}

    public static LocalizedStringDeFrItEnEmbeddable from(final @Nullable String s) {
        return new LocalizedStringDeFrItEnEmbeddable(s, s, s, s);
    }

    public static LocalizedStringDeFrItEnEmbeddable from(final LocalizedValue<String> lv) {
        return new LocalizedStringDeFrItEnEmbeddable(
                lv.deNullable(), lv.frNullable(), lv.itNullable(), lv.enNullable());
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

    public @Nullable String getEn() {
        return en;
    }

    public void setEn(final @Nullable String en) {
        this.en = en;
    }

    @Override
    public LocalizedValueBuilder<String> toLocalizedValueBuilder() {
        return LocalizedValue.<String>builder().de(getDe()).fr(getFr()).it(getIt()).en(getEn());
    }
}
