/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import org.w3c.dom.Element;
import java.util.Map;


/**
 * Represents a configuration of the WFS service.  Inherits most everything
 * from ServiceConfig.
 *
 * @author Gabriel Roldán
 * @author Chris Holmes
 * @version $Id: WFSConfig.java,v 1.3.2.2 2004/02/04 19:09:06 cholmesny Exp $
 */
public class WFSConfig extends ServiceConfig {
    public static final String WFS_FOLDER = "wfs/1.0.0/";
    public static final String WFS_BASIC_LOC = WFS_FOLDER + "WFS-basic.xsd";
    public static final String WFS_CAP_LOC = WFS_FOLDER
        + "WFS-capabilities.xsd";
    private GlobalConfig global = GlobalConfig.getInstance();
    private String describeUrl;
    
    /** Whether name, description, and bounded by should have gml: prepended. */
    private boolean gmlPrefixing = false;

	/** Whether transactions should be enabled. */
	private boolean transactionsEnabled = true;
	
    /**
     * Creates a new WFSConfig object.
     *
     * @param root DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
    public WFSConfig(Element root) throws ConfigurationException {
        super(root);
        URL = GlobalConfig.getInstance().getBaseUrl() + "wfs/";

        //String value = notNull(getElementText(root, "gmlPrefixing"));
        //getBooleanAttribute doesn't work right with non-mandatory atts.
        Element elem = getChildElement(root, "gmlPrefixing", false);
        LOGGER.info("gml element is " + elem + " root is " + root);

		String transType = getChildText(root, "serviceLevel", false);
		if (transType.equalsIgnoreCase("basic")){
			this.transactionsEnabled = false;
		}
        if (elem != null) {
            this.gmlPrefixing = getBooleanAttribute(elem, "value", false);
        }
    }

    public WFSConfig(Map config) {
        super(config);
        URL = GlobalConfig.getInstance().getBaseUrl() + "wfs/";
    }

    /**
     * Gets the base url of a describe request.
     *
     * @return DOCUMENT ME!
     *
     * @task REVISIT: consider using the /wfs? base, as it makes things a bit
     *       clearer.  Right now, however, I'm getting problems with the & in
     *       returned xml, having to put a &amp; in, and not sure if clients
     *       will process it correctly.
     */
    public String getDescribeBaseUrl() {
        if (this.describeUrl == null) {
            this.describeUrl = URL + "DescribeFeatureType?typeName=";
        }

        return describeUrl;
    }

    public String getDescribeUrl(String typeName) {
        return getDescribeBaseUrl() + typeName;
    }

    public String getWfsBasicLocation() {
        return global.getSchemaBaseUrl() + WFS_BASIC_LOC;
    }

    public String getWfsCapLocation() {
        return global.getSchemaBaseUrl() + WFS_CAP_LOC;
    }

	public boolean isTransactionsEnabled(){
		return transactionsEnabled;
	}

    /**
     * Returns whether the gml prefix should be appended to name, description
     * and boundedBy elements.
     *
     * @return <tt>true</tt> if gml elements should be prefixed appropriately.
     *
     * @task REVISIT: gml prefixing could be done more intelligently, right now
     *       it's a blunt approach that just puts gml: onto all name elements.
     *       But this logic should be done in geotools, in FeatureTransformer.
     *       For more information see that class.
     */
    public boolean isGmlPrefixing() {
        return gmlPrefixing;
    }
}
