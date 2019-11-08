package com.eurodyn.qlack.fuse.lexicon.util;

/**
 * A Util enum which provides languages name and locale
 *
 * @author European Dynamics SA
 */
public enum LanguagesEnum {

    EN("English"),
    EL("Greek"),
    FR("French"),
    DE("German"),
    DA("Danish"),
    IT("Italian"),
    ES("Spanish"),
    PT("Portuguese"),
    SV("Swedish"),
    HR("Croatian");

    /**
     *  the language name
     */
    private String languageName;
    /**
     *  Constructor
     */
    LanguagesEnum(String languageName) {
        this.languageName = languageName;
    }

    public String getLanguageName(){
        return languageName;
    }
}
