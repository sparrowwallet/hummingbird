package com.sparrowwallet.hummingbird.registry;

import co.nstant.in.cbor.model.*;

import java.util.ArrayList;
import java.util.List;

public class CryptoBip39 {
    public static final long WORDS = 1;
    public static final long LANG = 2;

    private final List<String> words;
    private final String language;

    public CryptoBip39(List<String> words, String language) {
        this.words = words;
        this.language = language;
    }

    public List<String> getWords() {
        return words;
    }

    public String getLanguage() {
        return language;
    }

    public static CryptoBip39 fromCbor(DataItem item) {
        List<String> words = new ArrayList<>();
        String language = "en";

        Map map = (Map)item;
        for(DataItem key : map.getKeys()) {
            UnsignedInteger uintKey = (UnsignedInteger)key;
            int intKey = uintKey.getValue().intValue();
            if(intKey == WORDS) {
                Array wordsArray = (Array)map.get(key);
                for(DataItem wordItem : wordsArray.getDataItems()) {
                    words.add(((UnicodeString)wordItem).getString());
                }
            } else if(intKey == LANG) {
                language = ((UnicodeString)map.get(key)).getString();
            }
        }

        if(words.isEmpty()) {
            throw new IllegalStateException("No BIP39 words");
        }

        return new CryptoBip39(words, language);
    }
}
