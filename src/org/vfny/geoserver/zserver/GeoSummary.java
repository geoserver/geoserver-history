/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */

package org.vfny.geoserver.zserver;

import java.util.Properties;
import org.w3c.dom.*;
// Imported JAVA API for XML Parsing 1.0 classes
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.logging.Logger;

/**
 * Helper class that represents a summary response.  It can return itself
 * as text, html or xml.  To use it simply set the values of the fields
 * with the accessor methods.  A convenience constructor is also provided, 
 * which takes a lucene document and the attribute map used to generate it and
 * fills in the fields that it can.
  *
 *@author Chris Holmes, TOPP
 *@version $VERSION$
 */
public class GeoSummary {

    
    /** Standard logging instance for class */
    private static final Logger LOGGER = 
        Logger.getLogger("org.vfny.geoserver.zserver");

    private String title;
    private String onlink;
    private String westbc;
    private String eastbc;
    private String northbc;
    private String southbc;
    private String extent;
    private String pubDate;
    private String begDate;
    private String endDate;
    private String browseName;
    private String browseDesc;
    private String browseType;
    private String entTypeLabel;
    private String attrLabel;
    private String dsgpoly;
    //REVISIT:  These are the values of the official  geo profile, but the fgdc
    //seems to have different information.  They return the bounding coordinates, 
    //the title, and the beg and end dates, but then they do the edition, the 
    //update, the geoform, and the browsen.  So need to figure out which to return.
    private String edition;
    private String update;
    private String geoform;
    private String indspref;


    /**
     * Convenience constructor.  Takes a lucene document and a mapping of the
     * use attributes.  The method relies on the GeoProfile class to access the 
     * attribute numbers of the fields, using the passed in attrMap to map to 
     * the fields of the lucene document.
     * 
     * @param doc The lucene document used to fill the summary fields.
     * @param attrMap The mapping of use attribute numbers to their lucene field names
     * 
     */
    public GeoSummary(org.apache.lucene.document.Document doc, Properties attrMap) {
	//just doing same as iSite for now.  To add the others put them
	//in GeoProfile.Attribute, and make sure they're in the attrMap
	title = doc.get(attrMap.getProperty(GeoProfile.Attribute.TITLE));
	onlink = doc.get(attrMap.getProperty(GeoProfile.Attribute.ONLINK));
	begDate = doc.get(attrMap.getProperty(GeoProfile.Attribute.BEGDATE));
	endDate = doc.get(attrMap.getProperty(GeoProfile.Attribute.ENDDATE));
	edition = doc.get(attrMap.getProperty(GeoProfile.Attribute.EDITION));
	update = doc.get(attrMap.getProperty(GeoProfile.Attribute.UPDATE));
	geoform = doc.get(attrMap.getProperty(GeoProfile.Attribute.GEOFORM));
	browseName = doc.get(attrMap.getProperty(GeoProfile.Attribute.BROWSEN));
	indspref = doc.get(attrMap.getProperty(GeoProfile.Attribute.INDSPREF));
	westbc = doc.get(attrMap.getProperty(GeoProfile.Attribute.WESTBC));
	westbc = NumericField.stringToNumber(westbc).toString();
	eastbc = doc.get(attrMap.getProperty(GeoProfile.Attribute.EASTBC));
	eastbc = NumericField.stringToNumber(eastbc).toString();
	northbc = doc.get(attrMap.getProperty(GeoProfile.Attribute.NORTHBC));
	northbc = NumericField.stringToNumber(northbc).toString();
	southbc = doc.get(attrMap.getProperty(GeoProfile.Attribute.SOUTHBC));
	southbc = NumericField.stringToNumber(southbc).toString();
	extent = GeoProfile.computeExtent(eastbc, westbc,
					  northbc, southbc).toString();
    }

    /**
     * No argument constructor.  Fields must be filled with setter functions
     * before this class can do anything meaningful.
     */
    public GeoSummary() {
    }

    /**
     * Sets the three fields needed for a browse graphic.
     *
     * @param fileName the name of the browse graphic file.
     * @param fileDesc the file's description
     * @param fileType the type of the browse file.
     */
    public void setBrowseGraphic(String fileName, String fileDesc, String fileType) {
	browseName = fileName;
	browseDesc = fileDesc;
	browseType = fileType;
    }

    /**
     * Sets the title of the summary.
     *
     * @param title, the title to be set.
     */
    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * Sets the online linkage field of the summary.
     *
     * @param onlink, the string representation of the online linkage field.
     */
    public void setOnlineLinkage(String onlink) {
	this.onlink = onlink;
    }


    /**
     * Sets the bounding coordinates of the summary.
     *
     * @param westbc the west bounding coordinate.
     * @param eastbc the east bounding coordinate.
     * @param northbc the north bounding coordinate.
     * @param southbc the south bounding coordinate.
     */
    public void setBounds(String westbc, String eastbc, 
			  String northbc, String southbc){
	this.westbc = westbc;
	this.eastbc = eastbc;
	this.northbc = northbc;
	this.southbc = southbc;
	this.extent = GeoProfile.computeExtent(eastbc, westbc,
					       northbc, southbc).toString();
    }

    /**
     * Sets the extent field of the summary.
     *
     * @param extent, the area covered by the bounding coords.
     */
    public void setExtent(String extent) {
	this.extent = extent;
    }

    /**
     * Sets the publication date of the summary.
     *
     * @param pubDate, the publication date to be set.
     */
    public void setPublicationDate(String pubDate) {
	this.pubDate = pubDate;
    }

    /**
     * Sets the beginning date of the summary.
     *
     * @param begDate, the beginning date to be set.
     */
    public void setBeginningDate(String begDate) {
	this.begDate = begDate;
    }

    /**
     * Sets the end date of the summary.
     *
     * @param endDate, the ending date to be set.
     */
    public void setEndingDate(String endDate) {
	this.endDate = endDate;
    }

    /**
     * Sets the entity type label of the summary.
     *
     * @param entTypeLabel, the type label of the entity.
     */
    public void setEntityTypeLabel(String entTypeLabel) {
	this.entTypeLabel = entTypeLabel;
    }

    /**
     * Sets the attribute label of the summary.
     *
     * @param attrLabel, the attribute label to be set.
     */
    public void setAttributeLabel(String attrLabel){
	this.attrLabel = attrLabel;
    }

    /**
     * Sets the Data Set G-polygon of the summary.
     *
     * @param dsgpoly, the string representation of the g-polygon.
     */
    public void setDSGPolygon(String dsgpoly) {
	this.dsgpoly = dsgpoly;
    }

    /**
     * Sets the edition field of the summary.
     *
     * @param edition, the string representation of the g-polygon.
     */
    public void setEdition(String edition) {
	this.edition = edition;
    }

    /**
     * Sets the geoform field of the summary.
     *
     * @param geoform, the string representation of the g-polygon.
     */
    public void setGeoform(String geoform) {
	this.geoform = geoform;
    }

    /**
     * Sets the update field of the summary.
     *
     * @param update, the string representation of the g-polygon.
     */
    public void setUpdate(String update) {
	this.update = update;
    }

        /**
     * Sets the indspref field of the summary.
     *
     * @param indspref, the string representation of the g-polygon.
     */
    public void setIndspref(String indspref) {
	this.indspref = indspref;
    }


    /**
     * Creates a text field for the return summary.
     *
     * @param fieldName the name of the field.
     * @param fieldVal what the field equals.
     * @return a string of the fieldname, an equals sign, the fieldVal,
     * and a newline char.
     */
    public String addTextField(String fieldName, String fieldVal) {
	if (fieldVal == null || fieldVal.equals("")) {
	    return "";
	} else {
	    return fieldName + "=" + fieldVal + "\n";
	}
    }

    /**
     * Creates a html field for the return summary.
     *
     * @param fieldName the name of the field.
     * @param fieldVal what the field equals.
     * @return a string of a P tag, the fieldname, an equals sign, the fieldVal,
     * and a closing P tag, and a newline char.
     */
    public String addHtmlField(String fieldName, String fieldVal) {
	if(notEmpty(fieldVal)) {
	    return "<P>" + fieldName + "=" + fieldVal + "</P>\n";
	} else {
	    return "";
	}
    
				

	/*if (fieldVal == null || fieldVal.equals("")) {
	    return "";
	} else {
	    return "<P>" + fieldName + "=" + fieldVal + "</P>\n";
	    }*/
    }	


    /**
     * Creates a string representation of a geo summary in html format.  Currently
     * uses the same return values as the Isite reference implementation, since this
     * is for the fgdc.  The Geo Profile specifies different values, but this is
     * easily modified to use those.
     *
     * @return a string of the relevant fields, with various html tags.
     */
    public String getHtmlSummary() {
	String retString = "";
	if (title != null && !title.equals("")){
	    retString += "<P><B>TITLE=" + title + "</B></P>\n";
	}
	    retString += addHtmlField("EDITION", edition) +
	    addHtmlField("GEOFORM", geoform) +
	    addHtmlField("BEGDATE", begDate) +
	    addHtmlField("ENDDATE", endDate) +
	    addHtmlField("UPDATE", update) +
	    addHtmlField("WESTBC", westbc) +
	    addHtmlField("EASTBC", eastbc) +
	    addHtmlField("NORTHBC", northbc) + 
	    addHtmlField("SOUTHBC", southbc) +
	    addHtmlField("ONLINK", onlink) +
	    addHtmlField("INDSPREF", indspref) + 
	    addHtmlField("BROWSEN", browseName);
	return retString;
    }

    /**
     * Creates a string representation of a geo summary in a text format.  Currently
     * uses the same return values as the Isite reference implementation, since this
     * is for the fgdc.  The Geo Profile specifies different values, but this is
     * easily modified to use those.
     *
     * @return a string of the relevant fields.
     */
    public String getTextSummary() {
	String retString = addTextField("TITLE", title) +
	    addTextField("EDITION", edition) +
	    addTextField("GEOFORM", geoform) +
	    addTextField("BEGDATE", begDate) +
	    addTextField("ENDDATE", endDate) +
	    addTextField("UPDATE", update) +
	    addTextField("WESTBC", westbc) +
	    addTextField("EASTBC", eastbc) +
	    addTextField("NORTHBC", northbc) + 
	    addTextField("SOUTHBC", southbc) +
	    addTextField("ONLINK", onlink) +
	    addTextField("INDSPREF", indspref) + 
	    //addTextField("EXTENT", extent) + 
	    addTextField("BROWSEN", browseName);
	
	//these are for a summary more closely inline with that specified by 
	//the Geo Profile.
	/*  addTextField("EXTENT", extent) +
	    addTextField("PUBDATE", pubDate) +
	    addTextField("BROWSEN", browseName) +
	    addTextField("BROWSED", browseDesc) +
	    addTextField("BROWSET", browseType) +	    
	    addTextField("ENTTYPL", entTypeLabel) +
	    addTextField("ATTRLABL", attrLabel) +
	    addTextField("DSGPOLY", dsgpoly); */
	return retString;
    }

    /**
     * Helper function for xml, creates a new text element with the given parent
     * and value.
     *
     * @param d the Document to be used to create the element.
     * @param parent the element to serve as parent to this new element.
     * @param elem_name the name of this new element.
     * @param value the text that this new element will hold.
     */
    private void addTextElement(Document d, Element parent, String elem_name, Object value)
    {
    if ( value != null )
	{
	    Element new_element = d.createElement(elem_name);
	    parent.appendChild(new_element);
	    new_element.appendChild(d.createTextNode(value.toString()));
	}
    }

    /** 
     * Helper function for xml, creates a new child with the given parent.
     *
     * @param d the Document to be used to create the element.
     * @param parent the element to serve as parent to this new element.
     * @param childName the name of this new element.
     * @return the newly created element.
     */
    private Element newChild(Document d, Element parent, String childName) {
	
	Element new_element = d.createElement(childName);
	parent.appendChild(new_element);		
	return new_element;
    }

    private boolean notEmpty(String test) {
	return (test != null && !test.equals(""));
    }

    /**
     * Creates an xml summary.   Currently
     * uses the same return values as the Isite reference implementation, since this
     * is for the fgdc.  The Geo Profile specifies different values, but changing this
     * to use those won't be trivial.
     *
     */
    public Document getXmlSummary() {
	/*REVISIT: This is lots of bad hardcoding, needs to be redone.  It would be
	 * nice if we could just take an xml document and filter out all the elements
	 * we don't want, and return that new document.  I'm pretty sure this could be
	 * done, but I don't know xml that well.  Possibly a tree walker where the filter
	 * takes a list of xpaths and checks each node to see if its name matches any
	 * of the parts of the xpath.  But I had no time to figure it all out and get
	 * it working. */
	    Document doc = null;
	    try {
		DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
		dfactory.setNamespaceAware(true);
		doc = dfactory.newDocumentBuilder().newDocument();		
		//retval = new org.apache.xerces.dom.DocumentImpl();
		Element root = doc.createElement("metadata");
		doc.appendChild( root );
		//Element root = newChild(doc, doc, "metadata");
		//Element id = doc.createElement("idinfo");
		//root.appendChild(id);
		Element id = newChild(doc, root, "idinfo");
		//Element citation = doc.createElement("citdation");
		//id.appendChild(citation);
		if (notEmpty(title) || notEmpty(edition) || notEmpty(geoform)
		    || notEmpty(onlink)) {
		    Element citation = newChild(doc, id, "citation");
		    Element citeinfo = newChild(doc, citation, "citeinfo");
		    // Element citeinfo = doc.createElement("citeinfo");
		    //citation.appendChild(citeinfo);
		    //HACK: change this hardcode
		    addTextElement(doc, citeinfo, "title", title);
		    addTextElement(doc, citeinfo, "edition", edition);
		    addTextElement(doc, citeinfo, "geoform", geoform);
		    addTextElement(doc, citeinfo, "onlink", onlink);
		}
		if (notEmpty(begDate) || notEmpty(endDate)) {
		    Element timeperd = newChild(doc, id, "timeperd");
		    Element timeinfo = newChild(doc, timeperd, "timeinfo");
		    Element rngdates = newChild(doc, timeinfo, "rngdates");
		    addTextElement(doc, rngdates, "begdate", begDate);
		    addTextElement(doc, rngdates, "enddate", endDate);
		}
		if (notEmpty(update)) {
		    Element status = newChild(doc, id, "status");
		    addTextElement(doc, status, "update", update);
		}
		if (notEmpty(northbc) || notEmpty(eastbc) 
		    ||notEmpty(westbc) || notEmpty(southbc)) { 
		    Element spdom = newChild(doc, id, "spdom");
		    Element bounding = newChild(doc, spdom, "bounding");
		    addTextElement(doc, bounding, "westbc", westbc);
		    addTextElement(doc, bounding, "eastbc", eastbc);
		    addTextElement(doc, bounding, "northbc", northbc);
		    addTextElement(doc, bounding, "southbc", southbc);
		}
		if (notEmpty(indspref)) {
		    Element spdoinfo = newChild(doc, root, "spdoinfo");
		    addTextElement(doc, spdoinfo, "indspref", indspref);
		}
		//System.out.println("Doc is " + doc);


	    } catch (ParserConfigurationException e) {
		LOGGER.severe("exception with the parser, check the xerces jars: " + e.getMessage());
      }
	  return doc;
    }


}
