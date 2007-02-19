package org.vfny.geoserver.issues;

import java.io.IOException;

public class MementoWrapper {
    public static IMemento getMementoFromString(String mementoString){
        try {
            return XMLMemento.loadMementoFromString(mementoString);
        } catch (Exception e) {
            return null;
        }
        
    }
    public static final String ROOT_ID = "MementoData";
    public static String getStringFromMemento(IMemento memento){
        XMLMemento newMemento = XMLMemento.createWriteRoot(ROOT_ID);
        newMemento.putMemento(memento);
        try {
            return newMemento.saveToString();
        } catch (IOException e) {
            return "";
        }
    }

}
