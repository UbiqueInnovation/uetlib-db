package ch.ubique.backend.db.entity;

import ch.ubique.backend.core.localized.LocalizedValue;

public interface LocalizedStringInterface {

    default LocalizedValue<String> toLocalizedValue() {
        return LocalizedValue.<String>builder()
                .de(getDe())
                .fr(getFr())
                .it(getIt())
                .en(getEn())
                .build();
    }

    String getDe();

    String getFr();

    String getIt();

    String getEn();
}
