/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */

package org.vfny.geoserver.config;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException; 

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.DocumentHandler;
import org.xml.sax.SAXException;

/**
 * This class represents a FeatureType element in a Capabilities document
 * along with additional information about the datasource backend.  
 * @author Chris Holmes, TOPP
 * @version $Revision: 1.4 $ $Date: 2003/05/30 22:07:56 $
 * @tasks REVISIT: consider merging this into TypeInfo.  This class replaces
 * the castor generated FeatureType, but it is now unclear if we _really_ 
 * need this internal class, or if a TypeInfo can just hold it all.
**/
class FeatureType {

    public static final String ROOT_TAG = "FeatureType";
   
    public static final String OLD_ROOT_TAG = "featureType";

    public static final String SRS_TAG = "SRS";

    public static final String LAT_BBOX_TAG = "LatLonBoundingBox";

    public static final String OLD_LAT_BBOX_TAG = "LatLonBoundingBox";

    public static final String PARAMS_TAG = "DatasourceParams";

    public static final String MINX_ATT = "minx";
    public static final String MINY_ATT = "miny";
    public static final String MAXX_ATT = "maxx";
    public static final String MAXY_ATT = "maxy";

    //we're using ServiceConfig's final String tags for abstract, title,
    //keywords and name, under the assumption that in future versions the 
    //elements of feature type and service sections will be the same.

       /** Standard logging instance for class */
    private static final Logger LOGGER = 
        Logger.getLogger("org.vfny.geoserver.config");

    /** The name of the feature type*/
    private String name;

    /** A human-readable title to briefly identify in menus*/
    private String title;

    /** a descriptive narrative for more information*/
    private String _abstract;

    /** short words to aid catalog searching*/
    private List keywords;

    /** indicates which spatial reference system should be used*/
    private String srs;

    /** Indicates the edges of the enclosing rectangle in latitude/longitude
     * decimal degrees.  */
    private LatLonBoundingBox latLongBBox;

    /** Holds the parameters for the datasource backend holding this feature*/
    //put in different place? - to differentiate between service info and data.
    private Map dsParams;


    /** 
     * Constructor with the two mandatory featureType elements.
     */
    public FeatureType(String name, String srs) {
        this.name = name;
	this.srs = srs;
    } 

      /**
     * static factory, reads a FeatureType from an xml file, using
     * the default root tag.
     *
     * @param configFile the path to the configuration file.
     * @return the FeatureType object constructed from the xml elements
     * of the file.
     */
    public static FeatureType getInstance(String featureTypeFile) 
    throws ConfigurationException {
	return getInstance(featureTypeFile, ROOT_TAG);
    }

    /**
     * static factory, reads a FeatureType from an xml file, using
     * the passed in root tag.
     *
     * @param configFile the path to the configuration file.
     * @param rootTag the tag of the element whose children are the appropriate
     * configuration elements.
     * @return the FeatureType object constructed from the xml elements
     * of the file.
     */
    public static FeatureType getInstance(String featureTypeFile, 
					  String rootTag) 
	throws ConfigurationException{
	FeatureType featureType = null;
	try {
	    //FileInputStream fis = new FileInputStream(featureTypeFile);
	    FileReader fis = new FileReader(featureTypeFile);
	    InputSource in = new InputSource(fis);
	    DocumentBuilderFactory dfactory = 
		DocumentBuilderFactory.newInstance();
	    dfactory.setNamespaceAware(true);
	    Document infoDoc = dfactory.newDocumentBuilder().parse(in);
	    Element featureElem = infoDoc.getDocumentElement();
	    String featureTag = featureElem.getTagName();
	    if (!featureTag.equals(rootTag) && 
		!featureTag.equals(OLD_ROOT_TAG)){
		featureElem = 
		    (Element)featureElem.getElementsByTagName(rootTag).item(0);
		if (featureElem == null) {
		    String message = "could not find root tag: " + rootTag + 
			" in file: " + featureTypeFile;
		    LOGGER.warning(message);
		    throw new ConfigurationException(message);
		}
	    }
		
	    String name = findTextFromTag(featureElem, ServiceConfig.NAME_TAG);
	    String srs = findTextFromTag(featureElem, SRS_TAG);
	    if (name.equals("") || srs.equals("")) {
		String message = "in " + featureTypeFile + 
		    ", <Name> and <SRS> are " +
		    "required for a valid feature type.";
		//REVISIT: if we don't throw exception then the user will just have
		//an invalid Capabilities document.  Is that what we want?
		//throw new FeatureurationException(message);
		LOGGER.warning(message);
	    }
	    featureType = new FeatureType(name, srs);
	    featureType.setAbstract(findTextFromTag
				    (featureElem, ServiceConfig.ABSTRACT_TAG));
	    featureType.setTitle(findTextFromTag
				(featureElem, ServiceConfig.TITLE_TAG));
	    featureType.setKeywords(ServiceConfig.getKeywords(featureElem));
	    featureType.setLatLonBoundingBox(LatLonBoundingBox.getBBox(featureElem));
	    featureType.setDataParams(getDataParams(featureElem, featureTypeFile));

	    fis.close();

	} catch (IOException ioe) {
	    String message = "problem reading file " + featureTypeFile  + "due to: "
		+ ioe.getMessage();
	    LOGGER.warning(message);
	    throw new ConfigurationException(message, ioe);
	} catch (ParserConfigurationException pce) {
	    String message = "trouble with parser to read xml, make sure class"
		+ "path is correct, reading file " + featureTypeFile;
	    LOGGER.warning(message);
	    throw new ConfigurationException(message, pce);
	} catch (SAXException saxe){
	    String message = "trouble parsing XML in " + featureTypeFile 
		+ ": " + saxe.getMessage();
	    LOGGER.warning(message);
	    throw new ConfigurationException(message, saxe);
	}
	return featureType;
    }
    
    //TODO: put in common utility.
    private static String findTextFromTag(Element root, String tag){
	return ServiceConfig.findTextFromTag(root, tag);
    }
	
    /**
     * This class generates the appropriate data source parameters.  It is
     * written for postgis, but should easily extend to use geotools
     * datasource factories to generate new datasources.  It is also
     * backwards compatible with the old format.
     *
     * @param root the element whose descendants should be searched.
     * @return the properties needed to construct a new datasource.
     * @tasks REVISIT: when file datasources are introduced we may look into
     * just having the filename as a config item and constructing the full
     * filename ourselves.
     */
     private static Map getDataParams(Element root, String featureTypeFile){
	Map params = new HashMap();
	//try to get text in Keywords element.
	Element paramElem = (Element)root.getElementsByTagName(PARAMS_TAG).item(0);
	if (paramElem == null) { //keywords are comma delimited in one field,
	    params.put("host", findTextFromTag(root, "Host"));
	    params.put("port", findTextFromTag(root, "Port"));
	    params.put("passwd", findTextFromTag(root, "Password"));
	    params.put("user", findTextFromTag(root, "User"));
	    params.put("database", findTextFromTag(root, "DatabaseName"));
	    params.put("table", findTextFromTag(root, "Name"));
	    params.put("dbtype", "postgis");
	} else { 
	    NodeList paramNodes = paramElem.getChildNodes();
	    LOGGER.finer("there are nodes: " + paramNodes.getLength());
	    for (int j = 0; j < paramNodes.getLength(); j++){
		Node param = paramNodes.item(j);
		String name = param.getLocalName();
		LOGGER.finer(param + " is the node and the name is " + name);
		Node text = param.getFirstChild();
		if (text instanceof org.w3c.dom.Text){
		    String value = ((Text)text).getData();
		    LOGGER.finer("setting property with " + value);
		    //all urls have a :, so check for that, if it is just the 
		    //file name it won't have a colon (unless user names it
		    //something with a colon).  Better delimiter?
		    if (name.equals("filename")) {
			File featureTypeDir = 
			    new File(featureTypeFile).getParentFile();
			try {
			    value = new File(featureTypeDir, value).toURL().toString();
			} catch (java.net.MalformedURLException murle) {
			    LOGGER.info("problem reading datasource info for " +
					featureTypeFile + ": " + murle);
			}
			name = "url";
		    }
		    
		    params.put(name.toLowerCase(), value);
		}
	    }
	}
	return params;
    }

   
    

    /**
    **/
    public String getAbstract()
    {
        return this._abstract;
    } 

    /**
    **/
    public String getDatabaseName()
    {
        return (String) dsParams.get("database");
    } 

    /**
    **/
    public String getHost()
    {
	return dsParams.get("host").toString();
    } 

    /**
    **/
    public List getKeywords()
    {
        return this.keywords;
    } 

    /**
    **/
    public LatLonBoundingBox getLatLonBoundingBox()
    {
        return this.latLongBBox;
    } 

    /**
    **/
    public String getName()
    {
        return this.name;
    } 

    /**
    **/
    public String getOperations()
    {
	//get this from the datasource?
        return null;
    } 

    /**
    **/
    public String getPassword()
    {
	return (String) dsParams.get("passwd");
    } 

    /**
    **/
    public String getPort()
    {
        return (String) dsParams.get("port");
    } 

    /**
    **/
    public String getSRS()
    {
        return this.srs;
    } 

    /**
    **/
    public String getTitle()
    {
        return this.title;
    } 

    /**
    **/
    public String getUser()
    {
	return (String) dsParams.get("user");
    } 

    public Map getDataParams() {
	return this.dsParams;
    }

    public void setDataParams(Map dsParams){
	this.dsParams = dsParams;
    }

    /**
     * 
     * @param _abstract
    **/
    public void setAbstract(String _abstract)
    {
        this._abstract = _abstract;
    }

    /**
     * 
     * @param keywords
    **/
    public void setKeywords(List keywords)
    {
        this.keywords = keywords;
    }

    /**
     * 
     * @param latLongBBox
    **/
    public void setLatLonBoundingBox(LatLonBoundingBox latLongBBox)
    {
        this.latLongBBox = latLongBBox;
    }


    /**
     * 
     * @param name
    **/
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * 
     * @param srs
    **/
    public void setSRS(String srs)
    {
        this.srs = srs;
    }

    /**
     * 
     * @param title
    **/
    public void setTitle(String title)
    {
        this.title = title;
    }

      /**
     * Override of toString method. */
    public String toString() {
        StringBuffer returnString = new StringBuffer("\nconfig.FeatureType:");
	returnString.append("\n   [name: " + name + "] ");
	returnString.append("\n   [title: " + title + "] ");
	returnString.append("\n   [abstract: " + _abstract + "] ");
	returnString.append("\n   [keywords: "); 
        Iterator i = keywords.iterator();
        while(i.hasNext()) {
            String keyword = (String) i.next();
            returnString.append(keyword + " ");
        }
	returnString.append("] ");
	returnString.append("\n   [srs: " + srs + "] ");
	returnString.append("\n   [params: " + dsParams + "] ");
	returnString.append("\n   [" + latLongBBox + "] ");
	//returnString.append("\n   [access constraints: " + accessConstraints + "] ");
	

        return returnString.toString();
    }

	
    /**
     * Represents the LatLon(g)BoundingBox capabilities element.
     */
    public static class LatLonBoundingBox {
	private String minx;
	private String miny;
	private String maxx;
	private String maxy;

	public LatLonBoundingBox(String minx, String miny, 
				  String maxx, String maxy){
	    this.minx = minx;
	    this.miny = miny;
	    this.maxx = maxx;
	    this.maxy = maxy;
	}
	
	/**
	 * Static constructor that generates the first bbox found from the 
	 * passed in root node.
	 *
	 * @param root the parent of the child element to search for the
	 * bbox tags.
	 * @return the first bbox found at the root.
	 */
	public static LatLonBoundingBox getBBox(Element root){
	    LatLonBoundingBox retBox = null;
	    Node firstElement = root.getElementsByTagName(LAT_BBOX_TAG).item(0);
	    if (firstElement == null) {
		firstElement = root.getElementsByTagName(OLD_LAT_BBOX_TAG).item(0);
	    }
	    if (firstElement instanceof org.w3c.dom.Element){
		Element bbox = (Element)firstElement;
		retBox = new LatLonBoundingBox(bbox.getAttribute(MINX_ATT),
						bbox.getAttribute(MINY_ATT),
						bbox.getAttribute(MAXX_ATT),
						bbox.getAttribute(MAXY_ATT));
	    }
	    return retBox;
	}
	
	public String getMaxx(){
	    return this.maxx;
	} 
	

	public String getMaxy(){
	    return this.maxy;
	} 
	
	public String getMinx(){
	    return this.minx;
	} 
	
	
	public String getMiny(){
	    return this.miny;
	} 


	public String toString(){
	    return "LatLonBoundingBox: minx=" + minx + " miny=" + miny + 
		" maxx=" + maxx + "maxy=" + maxy;
	}
	
	public String getXml(){
	    return "<LatLonBoundingBox: minx=" + minx + " miny=" + miny + 
		" maxx=" + maxx + " maxy=" + maxy + "/>";
	}
    }
    

}
