package org.geoserver.services.hibernate.types;

import org.geoserver.hibernate.types.EnumUserType;
import org.geoserver.wfs.WFSInfo;
import org.geoserver.wfs.WFSInfo.Version;

/**
 * Hibernate user type for {@link WFSInfo.ServiceLevel}.
 */
public class WFSServiceLevelType extends EnumUserType<WFSInfo.ServiceLevel> {

    public WFSServiceLevelType() {
        super(WFSInfo.ServiceLevel.class);
    }

}
