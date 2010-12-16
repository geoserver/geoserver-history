package org.geoserver.inspire.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.model.IModel;

public class AllLanguagesModel implements IModel<List<String>> {

    List<String> langs;
    
    public void setObject(List<String> object) {
        this.langs = object;
    }
    
    public List<String> getObject() {
        if (langs == null) {
            langs = getAvailableLanguages();
        }
        return langs;
    }
    
    public void detach() {
        langs = null;
    }
    
    List<String> getAvailableLanguages() {
        LinkedHashSet<String> langs = new LinkedHashSet<String>(); 
        for (Locale l : Locale.getAvailableLocales()) {
            langs.add(l.getDisplayLanguage().toLowerCase().substring(0, 3));
        }
        
        return new ArrayList(langs);
    }
}
