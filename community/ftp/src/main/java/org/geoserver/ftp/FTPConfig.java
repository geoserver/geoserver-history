/* Copyright (c) 2001 - 2010 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.ftp;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Configuration bean for the GeoServer FTP Service.
 * 
 * @author groldan
 * @see FTPConfigLoader
 */
class FTPConfig {

    private static final Integer DEFAULT_FTP_PORT = 8021;

    private boolean enabled;

    private int ftpPort;

    public FTPConfig() {
        setDefaults();
    }

    void setDefaults() {
        enabled = true;
        ftpPort = DEFAULT_FTP_PORT;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getFtpPort() {
        return ftpPort;
    }

    public void setFtpPort(Integer ftpPort) {
        this.ftpPort = ftpPort;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
