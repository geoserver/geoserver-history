package org.vfny.geoserver.xml.internal.featureType;

import javax.xml.bind.IllegalEnumerationValueException;


public final class metadataTypeList {

    private String _metadataTypeList;
    public final static metadataTypeList TC_211 = new metadataTypeList("TC211");
    public final static metadataTypeList FGDC = new metadataTypeList("FGDC");

    private metadataTypeList(String s) {
        this._metadataTypeList = s;
    }

    public static metadataTypeList parse(String s) {
        if (s.equals("FGDC")) {
            return FGDC;
        }
        if (s.equals("TC211")) {
            return TC_211;
        }
        throw new IllegalEnumerationValueException(s);
    }

    public String toString() {
        return _metadataTypeList;
    }

}
