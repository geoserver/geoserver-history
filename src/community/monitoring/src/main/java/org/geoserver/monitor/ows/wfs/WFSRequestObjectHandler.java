package org.geoserver.monitor.ows.wfs;

import javax.xml.namespace.QName;

import org.geoserver.monitor.ows.RequestObjectHandler;

public abstract class WFSRequestObjectHandler extends RequestObjectHandler {

    protected WFSRequestObjectHandler(String reqObjClassName) {
        super(reqObjClassName);
    }

    protected String toString(Object name) {
        if (name instanceof QName) {
            QName qName = (QName) name;
            return qName.getPrefix() + ":" + qName.getLocalPart();    
        }
        else {
            return name.toString();
        }
         
    }

}
