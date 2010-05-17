/* 
 */
package org.geoserver.config.hibernate.beans;

import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.JAIInfo;
import org.geoserver.config.impl.GeoServerInfoImpl;
import org.geoserver.hibernate.Hibernable;
import org.geotools.util.logging.Logging;

/**
 * 
 * @author ETj <etj at geo-solutions.it>
 */
public class GeoServerInfoImplHb extends GeoServerInfoImpl implements GeoServerInfo, Hibernable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -9107189216422234902L;

    public GeoServerInfoImplHb() {
    }

    public GeoServerInfoImplHb(GeoServer geoServer) {
        super(geoServer);
    }

    @Override
    public JAIInfo getJAI() {
        if (super.getJAI() == null)
            Logging.getLogger(getClass()).warning("JAI is null for geoserver id:" + getId());
        return super.getJAI();
    }

    // @Override
    // public void setContact(ContactInfo contactInfo) {
    // super.setContact(contactInfo);
    // }

    // public Map getMetadata() {
    // Map map = super.getMetadata();
    //
    // JAIInfo info = (JAIInfo)map.get(JAIInfo.KEY);
    //
    // // this is a testcase workaround, when the db-stored existing geoserver
    // // may be invalid
    // if(info == null) {
    // Logging.getLogger(this.getClass()).warning("Returning a geoserver metadata with no JAIInfo. Fixing...");
    // map.put(JAIInfo.KEY, HibDefaultsFactoryImpl.getDefaultJAIInfo());
    // }
    //
    // return map;
    // }

    /**
     * Copies all the fields (except Id) into the dst instance.
     */
    public void copyTo(GeoServerInfoImplHb dst) {
        Logging.getLogger(getClass()).severe("FIXME " + getClass().getSimpleName() + "::copyTo()");
        dst.setAdminPassword(adminPassword);
        dst.setAdminUsername(adminUsername);
        dst.setCharset(charset);
        dst.setClientProperties(clientProperties);
        dst.setContact(contact);
        // dst.setLoggingLevel(loggingLevel);
        // dst.setLoggingLocation(loggingLocation);
        // dst.setMaxFeatures(maxFeatures);
        dst.setMetadata(metadata);
        dst.setNumDecimals(numDecimals);
        dst.setOnlineResource(onlineResource);
        dst.setProxyBaseUrl(proxyBaseUrl);
        dst.setSchemaBaseUrl(schemaBaseUrl);
        // dst.setStdOutLogging(stdOutLogging);
        dst.setTitle(title);
        dst.setUpdateSequence(updateSequence);
        dst.setVerbose(verbose);
        dst.setVerboseExceptions(verboseExceptions);
    }

    private static String getDiff(String fieldName, String o1, String o2) {
        if (o1 == null) {
            if (o2 != null)
                return fieldName + ":null:" + o2;
        } else if (!o1.equals(o2))
            return fieldName + ":" + o1 + ":" + o2;

        return null;
    }

    public String getFirstDiff(Object obj) {
        if (this == obj)
            return null;
        if (obj == null)
            return "other is null";
        if (!(obj instanceof GeoServerInfo)) {
            return "bad class";
        }
        final GeoServerInfo other = (GeoServerInfo) obj;
        String err;
        err = getDiff("pw", adminPassword, other.getAdminPassword());
        if (err != null)
            return err;
        err = getDiff("admin", adminUsername, other.getAdminUsername());
        if (err != null)
            return err;
        err = getDiff("charset", charset, other.getCharset());
        if (err != null)
            return err;

        if (!contact.equals(other.getContact()))
            return "contactInfo::";

        err = getDiff("id", id, other.getId());
        if (err != null)
            return err;
        // err = getDiff("loglev", loggingLevel, other.getLoggingLevel());
        // if(err != null)
        // return err;
        // err = getDiff("logloc", loggingLocation, other.getLoggingLocation());
        // if(err != null)
        // return err;
        // err = getDiff("maxfeat", ""+maxFeatures, ""+other.getMaxFeatures());
        // if(err != null)
        // return err;
        err = getDiff("numdec", "" + numDecimals, "" + other.getNumDecimals());
        if (err != null)
            return err;
        err = getDiff("olres", onlineResource, other.getOnlineResource());
        if (err != null)
            return err;
        err = getDiff("proxybu", proxyBaseUrl, other.getProxyBaseUrl());
        if (err != null)
            return err;
        err = getDiff("schemabu", schemaBaseUrl, other.getSchemaBaseUrl());
        if (err != null)
            return err;
        // err = getDiff("stdout", ""+stdOutLogging, ""+other.isStdOutLogging());
        // if(err != null)
        // return err;
        err = getDiff("title", title, other.getTitle());
        if (err != null)
            return err;
        err = getDiff("updateSeq", "" + updateSequence, "" + other.getUpdateSequence());
        if (err != null)
            return err;
        err = getDiff("verb", "" + verbose, "" + other.isVerbose());
        if (err != null)
            return err;
        err = getDiff("verbex", "" + verboseExceptions, "" + other.isVerboseExceptions());

        return err;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + "id:" + getId() + " title:" + getTitle()
                + " olr:" + getOnlineResource() + " cinfo:"
                + (contact == null ? "null" : contact.getId() + "--" + contact.getAddress()) + "]";
    }

}
