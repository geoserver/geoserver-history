package org.vfny.geoserver.zserver;

//import org.apache.lucene.document.Document;
//import org.vfny.zServer.index.*;
import java.util.Properties;
//import org.apache.xerces.dom;
    import org.w3c.dom.*;
// Imported JAVA API for XML Parsing 1.0 classes
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class GeoSummary {
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
    //REVISIT:  These are the values of the official geo profile, but the fgdc seems to have
    //different information.  They return the bounding coordinates, the title, and the beg and
    //end dates, but then they do the edition, the update, the geoform, and the browsen.  So 
    //need to figure out which to return.
    private String edition;
    private String update;
    private String geoform;
    private String indspref;

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
    }

    public void setBrowseGraphic(String fileName, String fileDesc, String fileType) {
	browseName = fileName;
	browseDesc = fileDesc;
	browseType = fileType;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public void setOnlineLinkage(String onlink) {
	this.onlink = onlink;
    }

    public void setBounds(String westbc, String eastbc, 
			  String northbc, String southbc){
	this.westbc = westbc;
	this.eastbc = eastbc;
	this.northbc = northbc;
	this.southbc = southbc;
    }

    public void setExtent(String extent) {
	this.extent = extent;
    }

    public void setPublicationDate(String pubDate) {
	this.pubDate = pubDate;
    }

    public void setBeginningDate(String begDate) {
	this.begDate = begDate;
    }

    public void setEndingDate(String endDate) {
	this.endDate = endDate;
    }

    public void setEntityTypeLabel(String entTypeLabel) {
	this.entTypeLabel = entTypeLabel;
    }

    public void setAttributeLabel(String attrLabel){
	this.attrLabel = attrLabel;
    }

    public void setDSGPolygon(String dsgpoly) {
	this.dsgpoly = dsgpoly;
    }

    public String addTextField(String fieldName, String fieldVal) {
	if (fieldVal == null || fieldVal.equals("")) {
	    return "";
	} else {
	    return fieldName + "=" + fieldVal + "\n";
	}
    }

    public String addHtmlField(String fieldName, String fieldVal) {
	if (fieldVal == null || fieldVal.equals("")) {
	    return "";
	} else {
	    return "<P>" + fieldName + "=" + fieldVal + "</P>\n";
	}
    }	

    public String getHtmlSummary() {
	String retString = "<P><B>TITLE=" + title + "</B><P>\n" +
	    addHtmlField("EDITION", edition) +
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
	    addTextField("BROWSEN", browseName);
	

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

    private void addTextElement(Document d, Element parent, String elem_name, Object value)
    {
    if ( value != null )
	{
	    Element new_element = d.createElement(elem_name);
	    parent.appendChild(new_element);
	    new_element.appendChild(d.createTextNode(value.toString()));
	}
    }

    private Element newChild(Document d, Element parent, String childName) {
	
	Element new_element = d.createElement(childName);
	parent.appendChild(new_element);		
	return new_element;
    }

    public Document getXmlSummary() {
	
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
		Element citation = newChild(doc, id, "citation");
		Element citeinfo = newChild(doc, citation, "citeinfo");
		// Element citeinfo = doc.createElement("citeinfo");
		//citation.appendChild(citeinfo);
		//HACK: change this hardcode
		addTextElement(doc, citation, "title", title);
		addTextElement(doc, citation, "edition", edition);
		addTextElement(doc, citation, "geoform", geoform);
		addTextElement(doc, citation, "onlink", onlink);
		Element timeperd = newChild(doc, id, "timeperd");
		Element timeinfo = newChild(doc, timeperd, "timeinfo");
		Element rngdates = newChild(doc, timeinfo, "rngdates");
		addTextElement(doc, rngdates, "begdate", begDate);
		addTextElement(doc, rngdates, "enddate", endDate);
		Element status = newChild(doc, id, "status");
		addTextElement(doc, status, "update", update);
		Element spdom = newChild(doc, id, "spdom");
		Element bounding = newChild(doc, spdom, "bounding");
		addTextElement(doc, bounding, "westbc", westbc);
		addTextElement(doc, bounding, "eastbc", westbc);
		addTextElement(doc, bounding, "northbc", westbc);
		addTextElement(doc, bounding, "southbc", westbc);
		Element spdoinfo = newChild(doc, root, "spdoinfo");
		addTextElement(doc, spdoinfo, "indspref", indspref);
		//System.out.println("Doc is " + doc);


	    } catch (ParserConfigurationException e) {
	  //TODO: catch each exception...should be 4.  Deal with gracefully
	  System.out.println("exception: " + e.getMessage());
      }
	  return doc;
    }


}
