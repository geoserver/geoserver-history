/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.NoSuchElementException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.geotools.data.FeatureSource;
import org.geotools.factory.FactoryConfigurationError;
import org.geotools.feature.AttributeType;
import org.geotools.feature.FeatureType;
import org.geotools.feature.FeatureTypeFactory;
import org.geotools.feature.SchemaException;
import org.geotools.filter.Filter;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.vfny.geoserver.global.dto.*;
import com.vividsolutions.jts.geom.Envelope;
import java.util.List;


/**
 * Represents a FeatureTypeInfo, its user config and autodefined information.
 *
 * @author Gabriel Roldán
 * @author Chris Holmes
 * @version $Id: FeatureTypeInfo.java,v 1.1.2.2 2004/01/05 23:26:25 dmzwiers Exp $
 */
public class FeatureTypeInfo extends Abstract {
    /** DOCUMENT ME! */
    private static final int DEFAULT_NUM_DECIMALS = 8;

	private FeatureTypeInfoDTO ftc;

    public FeatureTypeInfo(FeatureTypeInfoDTO config)throws ConfigurationException{
    	ftc = config;
    }

	Object getDTO(){
		return ftc;
	}

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumDecimals() {
        return ftc.getNumDecimals();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public FeatureType getSchema() {
    	try{
        	return getSchema(ftc.getSchema());
    	}catch(Exception e){
    		return null;
    	}
    }

    /**
     * gets the string of the path to the schema file.  This is set during
     * feature reading, the schema file should be in the same folder as the
     * feature type info, with the name schema.xml.  This function does not
     * guarantee that the schema file actually exists, it just gives the
     * location where it _should_ be located.
     *
     * @return The path to the schema file.
     */
   /*public String getSchemaFile() {
        return pathToSchemaFile;
    }*/

    /**
     * DOCUMENT ME!
     *
     * @param pathToSchema DOCUMENT ME!
     */
    /*public void setSchemaFile(String pathToSchema) {
        this.pathToSchemaFile = pathToSchema;
    }*/

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DataStoreInfo getDataStore() {
        return GeoServer.getInstance().getData().getDataStore(ftc.getDataStoreId());
    }

    /**
     * Indicates if this FeatureTypeInfo is enabled.  For now just gets whether the
     * backing datastore is enabled.
     *
     * @return <tt>true</tt> if this FeatureTypeInfo is enabled.
     *
     * @task REVISIT: Consider adding more fine grained control to config
     *       files, so users can indicate specifically if they want the
     *       featureTypes enabled, instead of just relying on if the datastore
     *       is.
     */
    public boolean isEnabled() {
        return (getDataStore() != null) && (getDataStore().isEnabled());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getPrefix() {
		return getDataStore().getNameSpace().getPrefix();
    }

    /**
     * Gets the namespace for this featureType.  This isn't _really_ necessary,
     * but I'm putting it in in case we change namespaces,  letting
     * FeatureTypes set their own namespaces instead of being dependant on
     * datasources.  This method will allow us to make that change more easily
     * in the future.
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalStateException DOCUMENT ME!
     */
    public NameSpace getNameSpace() {
        if (!isEnabled()) {
            throw new IllegalStateException("This featureType is not "
                + "enabled");
        }

        return getDataStore().getNameSpace();
    }

    /**
     * overrides getName to return full type name with namespace prefix
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return NameSpace.PREFIX_DELIMITER+ftc.getName();
    }

    /**
     * Convenience method for those who just want to report the name of the
     * featureType instead of requiring the full name for look up.  If
     * allowShort is true then just the localName, with no prefix, will be
     * returned if the dataStore is not enabled.  If allow short is false then
     * a full getName will be returned, with potentially bad results.
     *
     * @param allowShort DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName(boolean allowShort) {
        if (allowShort && (!isEnabled() || (getDataStore() == null))) {
            return getShortName();
        } else {
            return getName();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getShortName() {
        return ftc.getName();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public FeatureSource getFeatureSource() throws IOException {
        if (!isEnabled() || (getDataStore().getDataStore() == null)) {
            throw new IOException("featureType: " + getName(true)
                + " does not have a properly configured " + "datastore");
        }

        FeatureSource realSource = getRealFeatureSource();
        FeatureSource mappedSource = new DEFQueryFeatureLocking(realSource,
                getSchema(), ftc.getDefinitionQuery());

        return mappedSource;
    }

    private FeatureSource getRealFeatureSource()
        throws NoSuchElementException, IllegalStateException, IOException {
        FeatureSource realSource = getDataStore().getDataStore().getFeatureSource(ftc.getName());

        return realSource;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public Envelope getBoundingBox() throws IOException {
		FeatureSource source = getRealFeatureSource();
		return source.getBounds();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Filter getDefinitionQuery() {
        return ftc.getDefinitionQuery();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public Envelope getLatLongBoundingBox() throws IOException {
		if(ftc.getLatLongBBox() == null)
			return getBoundingBox();
        return ftc.getLatLongBBox();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSRS() {
        return ftc.getSRS()+"";
    }


    /**
     * creates a FeatureTypeInfo schema from the list of defined exposed
     * attributes, or the full schema if no exposed attributes were defined
     *
     * @param attsElem DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     *
     * @task TODO: if the default geometry attribute was not declared as
     *       exposed should we expose it anyway? I think yes.
     */
    private FeatureType getSchema(Element attsElem)
        throws ConfigurationException, IOException {
        NodeList exposedAttributes = null;
        FeatureType schema = getRealFeatureSource().getSchema();
        FeatureType filteredSchema = null;

        if (attsElem != null) {
            exposedAttributes = attsElem.getElementsByTagName("attribute");
        }

        if ((exposedAttributes == null) || (exposedAttributes.getLength() == 0)) {
            return schema;
        }

        int attCount = exposedAttributes.getLength();
        AttributeType[] attributes = new AttributeType[attCount];
        Element attElem;
        String attName;

        for (int i = 0; i < attCount; i++) {
            attElem = (Element) exposedAttributes.item(i);
            attName = getAttribute(attElem, "name", true);
            attributes[i] = schema.getAttributeType(attName);

            if (attributes[i] == null) {
                throw new ConfigurationException("the FeatureTypeConfig " + getName()
                    + " does not contains the configured attribute " + attName
                    + ". Check your catalog configuration");
            }
        }

        try {
            filteredSchema = FeatureTypeFactory.newFeatureType(attributes,
                    getName());
        } catch (SchemaException ex) {
        } catch (FactoryConfigurationError ex) {
        }

        return filteredSchema;
    }
    
	protected String getAttribute(Element elem, String attName,
		boolean mandatory) throws ConfigurationException {
		Attr att = elem.getAttributeNode(attName);

		String value = null;

		if (att != null) {
			value = att.getValue();
		}

		if (mandatory) {
			if (att == null) {
				throw new ConfigurationException("element "
					+ elem.getNodeName()
					+ " does not contains an attribute named " + attName);
			} else if ("".equals(value)) {
				throw new ConfigurationException("attribute " + attName
					+ "in element " + elem.getNodeName() + " is empty");
			}
		}

		return value;
	}
    
    private FeatureType getSchema(String schema) throws ConfigurationException{
    	try{
    		return getSchema(loadConfig(new StringReader(schema)));
    	}catch(IOException e){
    		throw new ConfigurationException("",e);
    	}
    }

	/**
	 * loadConfig purpose.
	 * <p>
	 * Parses the specified file into a DOM tree.
	 * </p>
	 * @param configFile The file to parse int a DOM tree.
	 * @return the resulting DOM tree
	 * @throws ConfigException
	 */
	public static Element loadConfig(Reader fis)
		throws ConfigurationException {
		try {
			InputSource in = new InputSource(fis);
			DocumentBuilderFactory dfactory = DocumentBuilderFactory
				.newInstance();
			//dfactory.setNamespaceAware(true);
			/*set as optimizations and hacks for geoserver schema config files
			 * @HACK should make documents ALL namespace friendly, and validated. Some documents are XML fragments.
			 * @TODO change the following config for the parser and modify config files to avoid XML fragmentation.
			 */
			dfactory.setNamespaceAware(false);
			dfactory.setValidating(false);
			dfactory.setIgnoringComments(true);
			dfactory.setCoalescing(true);
			dfactory.setIgnoringElementContentWhitespace(true);

			Document serviceDoc = dfactory.newDocumentBuilder().parse(in);
			Element configElem = serviceDoc.getDocumentElement();

			return configElem;
		} catch (IOException ioe) {
			String message = "problem reading file " + "due to: "
				+ ioe.getMessage();
			LOGGER.warning(message);
			throw new ConfigurationException(message, ioe);
		} catch (ParserConfigurationException pce) {
			String message = "trouble with parser to read org.vfny.geoserver.config.org.vfny.geoserver.config.xml, make sure class"
				+ "path is correct, reading file " ;
			LOGGER.warning(message);
			throw new ConfigurationException(message, pce);
		} catch (SAXException saxe) {
			String message = "trouble parsing XML "  + ": "
				+ saxe.getMessage();
			LOGGER.warning(message);
			throw new ConfigurationException(message, saxe);
		}
	}

    /**
     * here we must make the transformation. Crhis: do you know how to do it? I
     * don't know.  Ask martin or geotools devel.  This will be better when
     * our geometries actually have their srs objects.  And I think that we
     * may need some MS Access database, not sure, but I saw some stuff about
     * that on the list.  Hopefully they'll do it all in java soon.  I'm sorta
     * tempted to just have users define for now.
     *
     * @param fromSrId DOCUMENT ME!
     * @param bbox DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static Envelope getLatLongBBox(String fromSrId, Envelope bbox) {
        //Envelope latLongBBox = null;
        //return latLongBBox;
        return bbox;
    }

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getAbstract() {
		return ftc.getAbstract();
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public List getKeywords() {
		return ftc.getKeywords();
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getTitle() {
		return ftc.getTitle();
	}
}
