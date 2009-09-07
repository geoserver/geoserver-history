package org.geoserver.services.hibernate.beans;


import java.util.List;
import org.geoserver.hibernate.Hibernable;
import org.geoserver.wfs.WFSInfo;
import org.geoserver.wfs.WFSInfoImpl;

public class WFSInfoImplHb 
        extends WFSInfoImpl
        implements WFSInfo, Hibernable {

}
