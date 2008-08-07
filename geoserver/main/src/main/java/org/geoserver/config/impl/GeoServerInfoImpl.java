package org.geoserver.config.impl;

import java.util.HashMap;
import java.util.Map;

import org.geoserver.catalog.Catalog;
import org.geoserver.config.ContactInfo;
import org.geoserver.config.GeoServerInfo;

public class GeoServerInfoImpl implements GeoServerInfo {

    String id;

    ContactInfo contact = new ContactInfoImpl();

    // Charset charSet = Charset.forName("UTF-8");
    String charset = "UTF-8";

    String title;

    int numDecimals = 4;

    String onlineResource;

    String schemaBaseUrl;

    String proxyBaseUrl;

    boolean verbose = true;

    boolean verboseExceptions = false;

    Map metadata = new HashMap();

    Map clientProperties = new HashMap();

    String loggingLevel;

    String loggingLocation;

    boolean stdOutLogging;

    int updateSequence;
    
    String adminUsername;
    String adminPassword;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public Catalog getCatalog() {
//        return catalog;
//    }
//
//    public void setCatalog(Catalog catalog) {
//        this.catalog = catalog;
//    }

    public void setContact(ContactInfo contactInfo) {
        this.contact = contactInfo;
    }

    public ContactInfo getContact() {
        return contact;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public int getNumDecimals() {
        return numDecimals;
    }

    public void setNumDecimals(int numDecimals) {
        this.numDecimals = numDecimals;
    }

    public String getOnlineResource() {
        return onlineResource;
    }

    public void setOnlineResource(String onlineResource) {
        this.onlineResource = onlineResource;
    }

    public String getProxyBaseUrl() {
        return proxyBaseUrl;
    }

    public void setProxyBaseUrl(String proxyBaseUrl) {
        this.proxyBaseUrl = proxyBaseUrl;
    }

    public String getSchemaBaseUrl() {
        return schemaBaseUrl;
    }

    public void setSchemaBaseUrl(String schemaBaseUrl) {
        this.schemaBaseUrl = schemaBaseUrl;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public boolean isVerboseExceptions() {
        return verboseExceptions;
    }

    public void setVerboseExceptions(boolean verboseExceptions) {
        this.verboseExceptions = verboseExceptions;
    }
    
    public String getLoggingLevel() {
        return loggingLevel;
    }
    
    public void setLoggingLevel( String loggingLevel ) {
        this.loggingLevel = loggingLevel;
    }
    
    public String getLoggingLocation() {
        return loggingLocation;
    }
    
    public void setLoggingLocation( String loggingLocation ) {
        this.loggingLocation = loggingLocation;
    }
    
    /**
     * Flag indicating if GeoServer logs to stdout.
     */
    public boolean isStdOutLogging() {
        return stdOutLogging;
    }
    
   
    public void setStdOutLogging( boolean stdOutLogging ) {
        this.stdOutLogging = stdOutLogging;
    }
    
    public int getUpdateSequence() {
        return updateSequence;
    }
    
    public void setUpdateSequence( int updateSequence ) {
        this.updateSequence = updateSequence;
    }

    public String getAdminPassword() {
        return adminPassword;
    }
    
    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }
    
    public String getAdminUsername() {
        return adminUsername;
    }
    
    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }
    
    public Map getMetadata() {
        return metadata;
    }

    public void setMetadata(Map metadata) {
        this.metadata = metadata;
    }

    public Map getClientProperties() {
        return clientProperties;
    }
    
    public void dispose() {
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((adminPassword == null) ? 0 : adminPassword.hashCode());
        result = prime * result
                + ((adminUsername == null) ? 0 : adminUsername.hashCode());
        result = prime * result + ((charset == null) ? 0 : charset.hashCode());
        result = prime
                * result
                + ((clientProperties == null) ? 0 : clientProperties.hashCode());
        result = prime * result
                + ((contact == null) ? 0 : contact.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result
                + ((loggingLevel == null) ? 0 : loggingLevel.hashCode());
        result = prime * result
                + ((loggingLocation == null) ? 0 : loggingLocation.hashCode());
        result = prime * result
                + ((metadata == null) ? 0 : metadata.hashCode());
        result = prime * result + numDecimals;
        result = prime * result
                + ((onlineResource == null) ? 0 : onlineResource.hashCode());
        result = prime * result
                + ((proxyBaseUrl == null) ? 0 : proxyBaseUrl.hashCode());
        result = prime * result
                + ((schemaBaseUrl == null) ? 0 : schemaBaseUrl.hashCode());
        result = prime * result + (stdOutLogging ? 1231 : 1237);
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + updateSequence;
        result = prime * result + (verbose ? 1231 : 1237);
        result = prime * result + (verboseExceptions ? 1231 : 1237);
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!( obj instanceof GeoServerInfo ) ) {
            return false;
        }
        final GeoServerInfo other = (GeoServerInfo) obj;
        if (adminPassword == null) {
            if (other.getAdminPassword() != null)
                return false;
        } else if (!adminPassword.equals(other.getAdminPassword()))
            return false;
        if (adminUsername == null) {
            if (other.getAdminUsername() != null)
                return false;
        } else if (!adminUsername.equals(other.getAdminUsername()))
            return false;
        if (charset == null) {
            if (other.getCharset() != null)
                return false;
        } else if (!charset.equals(other.getCharset()))
            return false;
        if (contact == null) {
            if (other.getContact() != null)
                return false;
        } else if (!contact.equals(other.getContact()))
            return false;
        if (id == null) {
            if (other.getId() != null)
                return false;
        } else if (!id.equals(other.getId()))
            return false;
        if (loggingLevel == null) {
            if (other.getLoggingLevel() != null)
                return false;
        } else if (!loggingLevel.equals(other.getLoggingLevel()))
            return false;
        if (loggingLocation == null) {
            if (other.getLoggingLocation() != null)
                return false;
        } else if (!loggingLocation.equals(other.getLoggingLocation()))
            return false;
        if (numDecimals != other.getNumDecimals())
            return false;
        if (onlineResource == null) {
            if (other.getOnlineResource() != null)
                return false;
        } else if (!onlineResource.equals(other.getOnlineResource()))
            return false;
        if (proxyBaseUrl == null) {
            if (other.getProxyBaseUrl() != null)
                return false;
        } else if (!proxyBaseUrl.equals(other.getProxyBaseUrl()))
            return false;
        if (schemaBaseUrl == null) {
            if (other.getSchemaBaseUrl() != null)
                return false;
        } else if (!schemaBaseUrl.equals(other.getSchemaBaseUrl()))
            return false;
        if (stdOutLogging != other.isStdOutLogging())
            return false;
        if (title == null) {
            if (other.getTitle() != null)
                return false;
        } else if (!title.equals(other.getTitle()))
            return false;
        if (updateSequence != other.getUpdateSequence())
            return false;
        if (verbose != other.isVerbose())
            return false;
        if (verboseExceptions != other.isVerboseExceptions())
            return false;
        return true;
    }
}
