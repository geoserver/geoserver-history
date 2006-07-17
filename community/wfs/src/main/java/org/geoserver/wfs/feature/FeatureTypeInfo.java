/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.feature;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Icon;

import org.geoserver.feature.FeatureSourceUtils;
import org.geotools.catalog.GeoResource;
import org.geotools.catalog.GeoResourceInfo;
import org.geotools.catalog.Resolve;
import org.geotools.catalog.ResolveChangeEvent;
import org.geotools.catalog.ResolveChangeListener;
import org.geotools.catalog.Service;
import org.geotools.catalog.Resolve.Status;
import org.geotools.catalog.defaults.DefaultGeoResourceInfo;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.factory.FactoryConfigurationError;
import org.geotools.feature.AttributeType;
import org.geotools.feature.FeatureType;
import org.geotools.feature.FeatureTypeFactory;
import org.geotools.feature.SchemaException;
import org.geotools.feature.type.GeometricAttributeType;
import org.geotools.filter.Filter;
import org.geotools.referencing.CRS;
import org.geotools.styling.Style;
import org.geotools.util.ProgressListener;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Represents a FeatureTypeInfo, its user config and autodefined information.
 * <p>
 * This class implements {@link org.geotools.catalog.Service} interface as a 
 * link to a catalog.
 * </p>
 * @author Gabriel Rold?n
 * @author Chris Holmes
 * @author dzwiers
 * @author Charles Kolbowicz
 * 
 * @version $Id: FeatureTypeInfo.java,v 1.41 2004/06/26 19:51:24 jive Exp $
 */
public class FeatureTypeInfo {
	
	private static Logger LOGGER = Logger.getLogger( "org.geoserver.feature");
	
	/** hash table that takes a epsg# to its definition**/
	private static Hashtable SRSLookup = new Hashtable();
	
    /** Default constant */
    private static final int DEFAULT_NUM_DECIMALS = 8;
   
    /**
     * Bounding box in Lat Long of the extent of this FeatureType.
     * <p>
     * Note reprojection may be required to derive this value.
     * </p>
     */
    private Envelope latLongBBox;
    /**
     * SRS number used to locate Coordidate Reference Systems
     * <p>
     * This will be used for reprojection and such like.
     * </p>
     */
    private int srs;
    
    /**
     * List of AttributeTypeInfo representing the schema.xml information.
     * <p>
     * Used to define the order and manditoryness of FeatureType attributes
     * during query (re)construction.
     * </p> 
     */
    private List schema = new ArrayList();
    
    /** Name of elment that is an instance of schemaBase */
    private String schemaName;
    
    /** Base schema (usually NullType) defining manditory attribtues */
    private String schemaBase;
    
    /**
     * prefix
     */
    private String namespacePrefix;
    
    /** typeName as defined by gt2 DataStore */
    private String typeName;
    
    /**
     * Directory where featureType is loaded from.
     * 
     * This may contain metadata files.
     */
    private String dirName;
    /**
     * Abstract used to describe FeatureType
     */
    private String _abstract;
    /**
     * List of keywords for Web Register Services
     */
    private List keywords;
    /**
     * Number of decimals used in GML output.
     */
    private int numDecimals;
    /**
     * Magic query used to limit scope of this FeatureType.
     */
    private Filter definitionQuery = null;
    /**
     * Default style used to render this FeatureType with WMS
     */
    private Style defaultStyle;
    /**
     * Title of this FeatureType as presented to End-Users.
     * <p>
     * Think of this as the display name on the off chance that typeName
     * is considered ugly.
     * </p>
     */
    private String title;

    /**
     * MetaData used by apps to squirel information away for a rainy day.
     */
    private Map meta;

    /**
     * AttributeTypeInfo by attribute name.
     * 
     * <p>
     * This will be null unless populated by schema or DTO.
     * Even if the DTO provides one this list will be lazily
     * created - so use the accessors.
     * </p>
     */
    private String xmlSchemaFrag;

    /**
     * The real geotools2 featureType cached for sanity checks.
     * <p>
     * This will be lazily created so use the accessors
     * </p>
     */
    private FeatureType ft;

    // Modif C. Kolbowicz - 07/10/2004
    /**
     * Holds value of property legendURL.
     */
    private LegendURL legendURL;
    //-- Modif C. Kolbowicz - 07/10/2004

    /** Holds the location of the file that contains schema information. */
    private File schemaFile;
    
    /**
     * Catalog handle
     */
    private GeoResource handle;
    
  
    
    /**
     * This value is added the headers of generated maps, marking them as being both
     * "cache-able" and designating the time for which they are to remain valid.
     *  The specific header added is "Cache-Control: max-age="
     */
    private String cacheMaxAge;
    
    /**
     * Should we be adding the CacheControl: max-age header to outgoing maps which include this layer?
     */
    private boolean cachingEnabled;
    

    public FeatureTypeInfo( GeoResource handle ) {
    		this.handle = handle;
    }
    
    /**
     * getNumDecimals purpose.
     * 
     * <p>
     * The default number of decimals allowed in the data.
     * </p>
     *
     * @return int the default number of decimals allowed in the data.
     */
    public int getNumDecimals() {
        return numDecimals;
    }

    public void setNumDecimals(int numDecimals) {
		this.numDecimals = numDecimals;
	}
    
    /**
     * Convenience method for returning the datastore info object associated 
     * with the containing datastore of the underlying feature type.
     * 
     * @return The DataStoreInfo for the unldering datastore.
     *
     * @throws IOException
     */
    public DataStoreInfo dataStoreInfo() throws IOException {
    		return (DataStoreInfo) handle.parent( null ).resolve( DataStoreInfo.class, null );
    }

    /**
     * By now just return the default style to be able to declare it in
     * WMS capabilities, but all this stuff needs to be revisited since it seems
     * currently there is no way of retrieving all the styles declared for
     * a given FeatureType.
     * 
     * @return the default Style for the FeatureType
     */
    public Style getDefaultStyle(){
    		return defaultStyle;
    }
    
    public void setDefaultStyle( Style defaultStyle ) {
    		this.defaultStyle = defaultStyle;
    }
    
    public String getNamespacePrefix() {
		return namespacePrefix;
	}
    
    public void setNamespacePrefix(String namespacePrefix) {
		this.namespacePrefix = namespacePrefix;
	}
    
    /**
     * Complete xml name (namespace:element) for this FeatureType.
     * 
     * This is the full type name with namespace prefix.
     *
     * @return String the FeatureTypeInfo name - should be unique for the
     *         parent Data instance.
     */
    public String name() {        
        return getNamespacePrefix() + ":" + typeName;
    }    

    /**
     * Determines if the feature type is enabled by delegating to the 
     * datastore info object.
     */
    public boolean enabled() {
    		try {
			return dataStoreInfo().isEnabled();
		} 
    		catch (IOException e) {
    			LOGGER.log( Level.SEVERE, null, e );
    			return false;
		}
    }
   
    public FeatureSource featureSource() throws IOException {
		if ( !enabled() || dataStoreInfo().dataStore() == null ) {
			String msg = "featureType: " + name()
                + " does not have a properly configured datastore";
			throw new IOException ( msg );
		}
		
		FeatureSource realSource = 
			(FeatureSource) handle.resolve( FeatureSource.class, null );
		
		//if not custom schema return real source
		if ( schema == null || schema.isEmpty() )
			return realSource;
		
		return GeoServerFeatureLocking.create(realSource,
                getFeatureType(realSource), getDefinitionQuery());
		
    }
   
    /**
     * getBoundingBox purpose.
     * 
     * <p>
     * The feature source bounds.
     * </p>
     *
     * @return Envelope the feature source bounds.
     *
     * @throws IOException when an error occurs
     */
    public Envelope boundingBox() throws IOException {
    		return FeatureSourceUtils.getBoundingBoxEnvelope( featureSource() );
    }
    
    /**
     * getDefinitionQuery purpose.
     * 
     * <p>
     * Returns the definition query for this feature source
     * </p>
     *
     * @return Filter the definition query
     */
    public Filter getDefinitionQuery() {
        return definitionQuery;
    }

    public void setDefinitionQuery(Filter definitionQuery) {
		this.definitionQuery = definitionQuery;
	}
    
    public Envelope latLongBoundingBox() throws Exception {
    		if (latLongBBox == null) {
            return boundingBox();
        }

        return latLongBBox;
    }
    
    /**
     * getLatLongBoundingBox purpose.
     * 
     * <p>
     * The feature source lat/long bounds.
     * </p>
     *
     * @return Envelope the feature source lat/long bounds.
     *
     * @throws IOException when an error occurs
     * 
     * JD: this method used to do what {@link #latLongBoundingBox()} does, 
     * hunt down calls and change.
     */
    public Envelope getLatLongBoundingBox() throws IOException {
        return latLongBBox;
    }

    public void getLatLongBoundingBox(Envelope latLongBBox) {
		this.latLongBBox = latLongBBox;
	}
    
    /**
     * getSRS purpose.
     * 
     * <p>
     * Proprietary identifier number
     * </p>
     *
     * @return int the SRS number.
     */
    public int getSRS() {
        return srs;
    }
    
    public void setSRS( int srs ) {
		this.srs = srs;
    }

    /**
     * Convenience method for returning a coordinate reference system.
     * <p>
     * This method never returns <code>null</code>. First it will try to return
     * the crs given by the code {@link #getSRS()}. If that fails, it then 
     * returns hte CRS for 4326 ( lat lon ).
     * </p>
     * @return A cooridnate reference system object.
     */
    public CoordinateReferenceSystem crs() {
    		CoordinateReferenceSystem crs = crs( getSRS() );
    		if ( crs != null ) {
    			return crs;
    		}
    		
    		return crs( 4326 );
    	}
    
    /**
     * getAttribute purpose.
     * 
     * <p>
     * XLM helper method.
     * </p>
     *
     * @param elem The element to work on.
     * @param attName The attribute name to find
     * @param mandatory true is an exception is be thrown when the attr is not
     *        found.
     *
     * @return String the Attr value
     *
     * @throws ConfigurationException thrown when an error occurs.
     */
    //JD: this method belongs somewhere else
//    protected String getAttribute(Element elem, String attName,
//        boolean mandatory) throws ConfigurationException {
//        Attr att = elem.getAttributeNode(attName);
//
//        String value = null;
//
//        if (att != null) {
//            value = att.getValue();
//        }
//
//        if (mandatory) {
//            if (att == null) {
//                throw new ConfigurationException("element "
//                    + elem.getNodeName()
//                    + " does not contains an attribute named " + attName);
//            } else if ("".equals(value)) {
//                throw new ConfigurationException("attribute " + attName
//                    + "in element " + elem.getNodeName() + " is empty");
//            }
//        }
//
//        return value;
//    }

    /**
     * Get abstract (description) of FeatureType.
     *
     * @return Short description of FeatureType
     */
    public String getAbstract() {
        return _abstract;
    }

    public void setAbstract( String _abstract ) {
    		this._abstract = _abstract;
    }
    
    /**
     * Keywords describing content of FeatureType.
     * 
     * <p>
     * Keywords are often used by Search engines or Catalog services.
     * </p>
     *
     * @return List the FeatureTypeInfo keywords
     */
    public List getKeywords() {
        return keywords;
    }

    public void setKeywords(List keywords) {
		this.keywords = keywords;
	}
    
    /**
     * getTitle purpose.
     * 
     * <p>
     * returns the FeatureTypeInfo title
     * </p>
     *
     * @return String the FeatureTypeInfo title
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
		this.title = title;
	}
    
    /**
     * Convenience method for always ensuring a name is returned.
     * 
     * 
     */
    public String schemaName() {
    	 	if( schemaName == null ){
             return typeName + "_Type";
         }
         return schemaName;	
    }
    
    /**
     * A valid schema name for this FeatureType. 
     * <p>
     * You should probably use {@link #schemaName()}.
     * </p>
     * 
     * @return schemaName if provided or typeName+"_Type"
     * 
     * JD: this method used to do what {@link #latLongBoundingBox()} does, 
     * hunt down calls and change.
     */
    public String getSchemaName() {
       return schemaName;
    }

    /**
     * setSchemaName purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param string
     */
    public void setSchemaName(String string) {
        schemaName = string;
    }

    /**
     * getSchemaName purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public String getSchemaBase() {
        return schemaBase;
    }

    /**
     * setSchemaName purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param string
     */
    public void setSchemaBase(String string) {
        schemaBase = string;
    }

    //
    // FeatureTypeMetaData Interface
    //
    /**
     * Access the name of this FeatureType.
     * <p>
     * This is the typeName as provided by the real gt2 DataStore.
     * </p>
     *
     * @return String getName()
     * @see org.geotools.data.FeatureTypeMetaData#getTypeName()
     */
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
    
    /**
     * Access real geotools2 FeatureType.
     *
     * @return Schema information.
     *
     * @throws IOException
     *
     * @see org.geotools.data.FeatureTypeMetaData#getFeatureType()
     */
    public FeatureType getFeatureType() throws IOException {
    	    return getFeatureType(featureSource());
    	}

    private FeatureType getFeatureType(FeatureSource fs)
        throws IOException {
        if (ft == null) {
            int count = 0;
            ft = fs.getSchema();
            URI namespace = ft.getNamespace();  

            //JD: checking the implementation, it always returnes empty 
            // array of strings.
//            String[] baseNames = DataTransferObjectFactory
//                .getRequiredBaseAttributes(schemaBase);
            String[] baseNames = new String[]{};
            AttributeType[] attributes = new AttributeType[schema.size()
                + baseNames.length];

            if (attributes.length > 0) {
                int errors = 0;

                for (; count < baseNames.length; count++) {
                    attributes[count - errors] = ft.getAttributeType(baseNames[count]);

                    if (attributes[count - errors] == null) {
                        // desired base attr is not availiable
                        errors++;
                    }
                }

                if (errors != 0) {
                    //resize array;
                    AttributeType[] tmp = new AttributeType[attributes.length
                        - errors];
                    count = count - errors;

                    for (int i = 0; i < count; i++) {
                        tmp[i] = attributes[i];
                    }

                    attributes = tmp;
                }

                for (Iterator i = schema.iterator(); i.hasNext();) {
                    AttributeTypeInfo ati = (AttributeTypeInfo) i.next();
                    String attName = ati.getName();
                    attributes[count] = ft.getAttributeType(attName);
                    
                    //DJB: added this to set SRS
                    if (Geometry.class.isAssignableFrom(attributes[count].getType())) {
                    	GeometricAttributeType old = (GeometricAttributeType) attributes[count];
                    	try {
                    		attributes[count] = new GeometricAttributeType(old,crs(srs)) ;
                    	}
                    	catch (Exception e) {
                		e.printStackTrace(); //DJB: this is okay to ignore since (a) it should never happen (b) we'll use the default one (crs=null)
					}
                    }

                    if (attributes[count] == null) {
                        throw new IOException("the FeatureType " + name()
                            + " does not contains the configured attribute "
                            + attName + ". Check your schema configuration");
                    }

                    count++;
                }

                try {
                    ft = FeatureTypeFactory.newFeatureType(attributes, typeName, namespace);
                } catch (SchemaException ex) {
                } catch (FactoryConfigurationError ex) {
                }
            }
        }

        return ft;
    }

    /**
     * Implement getDataStoreMetaData.
     *
     * @return
     *
     * @see org.geotools.data.FeatureTypeMetaData#getDataStoreMetaData()
     */
    public DataStoreInfo getDataStoreMetaData() {
        try {
			return dataStoreInfo();
		} 
        catch (IOException e) {
        		LOGGER.log( Level.SEVERE, null, e );
        		return null;
		}
    }

    /**
     * FeatureType attributes names as a List.
     * 
     * <p>
     * Convience method for accessing attribute names as a Collection. You may
     * use the names for AttributeTypeMetaData lookup or with the schema for
     * XPATH queries.
     * </p>
     *
     * @return List of attribute names
     *
     * @task REVISIT: This method sucks.  It didn't do the same thing as
     *       getAttributes, which it should have.  I fixed the root problem of
     *       why attribs.size() would equal 0.  So the second half of this
     *       method should probably be eliminated, as it should never be
     *       called. But I don't want to break code right before a release -
     *       ch.
     *
     * @see org.geotools.data.FeatureTypeMetaData#getAttributeNames()
     */
    public List getAttributeNames() {
        List attribs = schema;

        if (attribs.size() != 0) {
            List list = new ArrayList(attribs.size());

            for (Iterator i = attribs.iterator(); i.hasNext();) {
                AttributeTypeInfo at = (AttributeTypeInfo) i.next();
                list.add(at.getName());
            }

            return list;
        }

        List list = new ArrayList();

        try {
            FeatureType ftype = getFeatureType();
            AttributeType[] types = ftype.getAttributeTypes();
            list = new ArrayList(types.length);

            for (int i = 0; i < types.length; i++) {
                list.add(types[i].getName());
            }
        } catch (IOException e) {
        }

        return list;
    }

    /**
     * Returns a list of the attributeTypeInfo objects that make up this
     * FeatureType.
     *
     * @return list of attributeTypeInfo objects.
     */
    public List getAttributes() {
        return schema;
    }

    /**
     * Implement containsMetaData.
     *
     * @param key
     *
     * @return
     *
     * @see org.geotools.data.MetaData#containsMetaData(java.lang.String)
     */
    public boolean containsMetaData(String key) {
        return meta.containsKey(key);
    }

    /**
     * Implement putMetaData.
     *
     * @param key
     * @param value
     *
     * @see org.geotools.data.MetaData#putMetaData(java.lang.String,
     *      java.lang.Object)
     */
    public void putMetaData(String key, Object value) {
        meta.put(key, value);
    }

    /**
     * Implement getMetaData.
     *
     * @param key
     *
     * @return
     *
     * @see org.geotools.data.MetaData#getMetaData(java.lang.String)
     */
    public Object getMetaData(String key) {
        return meta.get(key);
    }
    
    /**
     * getLegendURL purpose.
     * 
     * <p>
     * returns the FeatureTypeInfo legendURL
     * </p>
     *
     * @return String the FeatureTypeInfo legendURL
     */
    // Modif C. Kolbowicz - 07/10/2004
    public LegendURL getLegendURL() {
        return this.legendURL;
    }        
    //-- Modif C. Kolbowicz - 07/10/2004

    /**
     * Gets the schema.xml file associated with this FeatureType.  This is set
     * during the reading of configuration, it is not persisted as an element
     * of the FeatureTypeInfoDTO, since it is just whether the schema.xml file
     * was persisted, and its location.  If there is no schema.xml file then
     * this method will return a File object with the location where the schema
     * file would be located, but the file will return false for exists().
     */
    public File getSchemaFile() {
	return this.schemaFile;
    }
    
    /**
     *  simple way of getting epsg #.
     *  We cache them so that we dont have to keep reading the DB or the epsg.properties file.
     *   I cannot image a system with more than a dozen CRSs in it...
     * 
     * @param epsg
     * @return
     */
    private CoordinateReferenceSystem crs(int epsg) {
    		CoordinateReferenceSystem result = 
    			(CoordinateReferenceSystem) SRSLookup.get(  new Integer(epsg) );
   		if (result == null) 	{
	    		//make and add to hash
	    		try {
					result = CRS.decode("EPSG:"+epsg);
					SRSLookup.put( new Integer(epsg)  , result);
			} 
	    		catch (NoSuchAuthorityCodeException e) {
				String msg = "Error looking up SRS for EPSG: " + epsg + 
					":" + e.getLocalizedMessage();
	    			LOGGER.warning( msg );
			}
	    		
	    	}
	    	return result;
    }

    /**
     * This value is added the headers of generated maps, marking them as being both
     * "cache-able" and designating the time for which they are to remain valid.
     *  The specific header added is "Cache-Control: max-age="
     * @return a string representing the number of seconds to be added to the "Cache-Control: max-age=" header
     */
	public String getCacheMaxAge() {
		return cacheMaxAge;
	}
    /**
     * 
     * @param cacheMaxAge a string representing the number of seconds to be added to the "Cache-Control: max-age=" header
     */
	public void setCacheMaxAge(String cacheMaxAge) {
		this.cacheMaxAge = cacheMaxAge;
	}
	
	/**
	 * Should we add the cache-control: max-age header to maps containing this layer?
	 * @return true if we should, false if we should omit the header
	 */
	public boolean isCachingEnabled() {
		return cachingEnabled;
	}
	/**
	 * Sets whether we should add the cache-control: max-age header to maps containing this layer
	 * @param cachingEnabled true if we should add the header, false if we should omit the header
	 */
	public void setCachingEnabled(boolean cachingEnabled) {
		this.cachingEnabled = cachingEnabled;
	}
}
 
