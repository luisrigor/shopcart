package com.gsc.shopcart.constants;

import lombok.Getter;

@Getter
public enum AppProfile {

    TOYOTA_LEXUS_PRF_TCAP(217, 10142),
    TOYOTA_LEXUS_PRF_MANAGER_CA(218, 10143),
    TOYOTA_LEXUS_PRF_CALLCENTER(243, 10164),
    TOYOTA_LEXUS_PRF_MANAGER_DEALER(219, 10144),
    TOYOTA_PRF_MANAGER_INSTALLATION(220, -2),
    TOYOTA_PRF_IMPORT_EXPORT(223, 10147),
    TOYOTA_PRF_TPA_BO_MANAGER_TPA(345, 10213),
    ROLE_VIEW_ALL_DEALERS(-1, -2),
    ROLE_VIEW_CA_DEALERS(-1, -2),
    ROLE_VIEW_CALL_CENTER_DEALERS(-1, -2),
    ROLE_VIEW_DEALER_ALL_INSTALLATION(-1, -2),
    ROLE_VIEW_DEALER_OWN_INSTALLATION(-1, -2),
    ROLE_IMPORT_EXPORT(-1, -2),
    ROLE_TPA_BO(-1, -2);

    private final Integer idToyota;
    private final Integer idLexus;

    AppProfile(Integer idToyota, Integer idLexus) {
        this.idToyota = idToyota;
        this.idLexus = idLexus;
    }

    public static Boolean compareId(Integer id, AppProfile profile) {
        return profile.getIdToyota().equals(id) || profile.getIdLexus().equals(id);
    }

}
