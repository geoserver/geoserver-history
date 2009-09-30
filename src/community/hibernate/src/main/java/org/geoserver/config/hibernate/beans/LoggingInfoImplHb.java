/*
 */

package org.geoserver.config.hibernate.beans;

import org.geoserver.config.LoggingInfo;
import org.geoserver.config.impl.LoggingInfoImpl;
import org.geoserver.hibernate.Hibernable;

/**
 * 
 * @author ETj <etj at geo-solutions.it>
 */
public class LoggingInfoImplHb extends LoggingInfoImpl implements LoggingInfo, Hibernable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7285995102018807585L;

    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void copyFrom(LoggingInfo loggingInfo) {
        setLevel(loggingInfo.getLevel());
        setLocation(loggingInfo.getLocation());
        setStdOutLogging(loggingInfo.isStdOutLogging());
    }
}
