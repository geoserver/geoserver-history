package org.vfny.geoserver.xml.internal.featureType;

import javax.xml.bind.IllegalEnumerationValueException;


public final class metadataFormatList {

    private String _metadataFormatList;
    public final static metadataFormatList XML = new metadataFormatList("XML");
    public final static metadataFormatList SGML = new metadataFormatList("SGML");
    public final static metadataFormatList TXT = new metadataFormatList("TXT");

    private metadataFormatList(String s) {
        this._metadataFormatList = s;
    }

    public static metadataFormatList parse(String s) {
        if (s.equals("SGML")) {
            return SGML;
        }
        if (s.equals("TXT")) {
            return TXT;
        }
        if (s.equals("XML")) {
            return XML;
        }
        throw new IllegalEnumerationValueException(s);
    }

    public String toString() {
        return _metadataFormatList;
    }

}
