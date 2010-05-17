/*
 */

package org.geoserver.services.hibernate.types;

import org.geoserver.hibernate.types.EnumUserType;
import org.geoserver.wfs.WFSInfo;

/**
 *
 * @author ETj <etj at geo-solutions.it>
 */
public class WFSVersionType
    extends EnumUserType<WFSInfo.Version>  {

    public WFSVersionType() {
        super(WFSInfo.Version.class);
    }
}
