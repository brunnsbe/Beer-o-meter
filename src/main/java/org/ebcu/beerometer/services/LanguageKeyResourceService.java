package org.ebcu.beerometer.services;

import java.util.*;
import java.util.concurrent.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.ebcu.beerometer.domain.*;
import org.ebcu.beerometer.database.*;
import org.ebcu.beerometer.resource.*;
import org.ebcu.beerometer.util.*;
import org.sql2o.Connection;

public class LanguageKeyResourceService implements LanguageKeyResource {

    private static final String                   DEF_LANG               = "en";

    private static final String                   SQL_LOAD_LANGUAGE_KEYS = "SELECT "
                                                                                 + "Election_LanguageCode.Code, "
                                                                                 + "Election_Language.LanguageKey AS Name, "
                                                                                 + "Election_Language.Data "
                                                                                 + "FROM "
                                                                                 + "Election_LanguageCode "
                                                                                 + "INNER JOIN Election_Language ON Election_LanguageCode.Id = Election_Language.LanguageCodeId "
                                                                                 + "WHERE "
                                                                                 + "Election_Language.LanguageKey NOT LIKE 'Email.%' "
                                                                                 + "ORDER BY "
                                                                                 + "Election_LanguageCode.Code, "
                                                                                 + "Election_Language.LanguageKey;";

    private static Map<String, List<LanguageKey>> languageKeys           = new ConcurrentHashMap<String, List<LanguageKey>>();

    static {
        loadLanguageKeysFromDB();
    }

    @Override
    public String reloadDB() {
        loadLanguageKeysFromDB();
        return new String();
    }

    @Override
    public Response getLanguageKeys(String languageCode) {
        if (languageCode == null) {
            languageCode = DEF_LANG;
        }

        List<LanguageKey> langKeys = languageKeys.get(languageCode);
        if (langKeys != null) {
            return ResponseUtil.addCacheControl(langKeys);
        } else {
            throw new NotFoundException();
        }
    }

    private static void loadLanguageKeysFromDB() {

        List<LanguageKey> languageSet;
        try (Connection con = DBConnection.getConnection().open()) {
            languageSet = con.createQuery(SQL_LOAD_LANGUAGE_KEYS).executeAndFetch(LanguageKey.class);

            for (LanguageKey languageKey : languageSet) {
                List<LanguageKey> langs = languageKeys.get(languageKey.getCode());
                if (langs == null) {
                    langs = new ArrayList<LanguageKey>();
                    languageKeys.put(languageKey.getCode(), langs);
                }
                langs.add(languageKey);
            }
        }
    }
}
