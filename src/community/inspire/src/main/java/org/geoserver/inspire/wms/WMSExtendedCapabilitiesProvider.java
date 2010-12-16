package org.geoserver.inspire.wms;

import java.io.IOException;

import org.geoserver.config.ContactInfo;
import org.geoserver.wms.ExtendedCapabilitiesProvider;
import org.geoserver.wms.WMSInfo;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.NamespaceSupport;

import static org.geoserver.inspire.wms.InspireMetadata.*;

public class WMSExtendedCapabilitiesProvider implements ExtendedCapabilitiesProvider {

    public static final String NAMESPACE =  "http://inspire.europa.eu/networkservice/view/1.0";
    
    public String[] getSchemaLocations() {
        return new String[]{NAMESPACE, "www/inspire/inspire_vs.xsd"};
    }

    public void registerNamespaces(NamespaceSupport namespaces) {
        namespaces.declarePrefix("inspire_vs", NAMESPACE);
        namespaces.declarePrefix("gml", "http://schemas.opengis.net/gml");
        namespaces.declarePrefix("gmd", "http://schemas.opengis.net/iso/19139/20060504/gmd/gmd.xsd");
        namespaces.declarePrefix("gco", "http://schemas.opengis.net/iso/19139/20060504/gco/gco.xsd");
        namespaces.declarePrefix("srv", "http://schemas.opengis.net/iso/19139/20060504/srv/srv.xsd");
    }

    public void encode(Translator tx, WMSInfo wms) throws IOException {
        tx.start("inspire_vs:ExtendedCapabilities");
    
        tx.start("inspire_vs:ResourceLocator");
        tx.start("gmd:linkage");
        tx.start("gmd:URL");
        tx.chars("http://inspire.europa.eu/info</gmd:URL");
        tx.end("gmd:URL");
        tx.end("gmd:linkage");
        tx.end("inspire_vs:ResourceLocator");
        
        String metadataURL = (String) wms.getMetadata().get(METADATA_URL.key);
        if (metadataURL != null) {
            tx.start("inspire_vs:MetadataUrl");
            tx.start("gmd:linkage");
            tx.start("gmd:URL");
            tx.chars(metadataURL);
            tx.end("gmd:URL");
            tx.end("gmd:linkage");
            tx.end("inspire_vs:MetadataUrl");
        }
        
        tx.start("inspire_vs:ResourceType");
        tx.start("gmd:MD_ScopeCode", atts("codeList", "http://standards.iso.org/ittf/" +
            "PubliclyAvailableStandards/ISO_19139_Schemas/resources/Codelist/" +
            "gmxCodelists.xml#MD_ScopeCode", "codeListValue", "dataset"));
        tx.end("gmd:MD_ScopeCod");
        tx.end("inspire_vs:ResourceType");
        
        tx.start("inspire_vs:TemporalReference");
        tx.start("gmd:EX_Extent");
        tx.start("gmd:temporalElement");
        tx.start("gmd:EX_TemporalExtent");
        tx.start("gmd:extent");
        tx.start("gml:TimePeriod", atts("gml:id", "IDd2febbb4-e66f-4ac8-ba76- 8fd9bc7c8be6"));
        tx.start("gml:beginPosition");
        tx.chars("2005-03-10T11:45:30");
        tx.end("gml:beginPosition");
        tx.start("gml:endPosition");
        tx.chars("2010-06-15T09:10:00");
        tx.end("gml:endPosition");
        tx.end("gml:TimePeriod");
        tx.end("gmd:extent");
        tx.end("gmd:EX_TemporalExtent");
        tx.end("gmd:temporalElement");
        tx.end("gmd:EX_Extent"); 
        tx.end("inspire_vs:TemporalReference");
        
        tx.start("inspire_vs:Conformity");
        tx.start("gmd:DQ_ConformanceResult");
        tx.start("gmd:specification");
        tx.start("gmd:CI_Citation");
        tx.start("gmd:title");
        tx.start("gco:CharacterString");
        tx.chars("Implementing Directive 2007/2/EC of the European Parliament and of the Council as regards the Network Services");
        tx.end("gco:CharacterString");
        tx.end("gmd:title");
        tx.start("gmd:date");
        tx.start("gmd:CI_Date");
        tx.start("gmd:date");
        tx.start("gco:Date");
        tx.chars("2008-11-28");
        tx.end("gco:Date");
        tx.end("gmd:date");
        tx.start("gmd:dateType");
        tx.start("gmd:CI_DateTypeCode", atts("codeList", "http://standards.iso.org/ittf/" +
            "PubliclyAvailableStandards/ISO_19139_Schemas/resources/Codelist/" +
            "ML_gmxCodelists.xml#CI_DateTypeCode", "codeListValue", "publication"));
        tx.chars("publication");
        tx.end("gmd:CI_DateTypeCode");
        tx.end("gmd:dateType");
        tx.end("gmd:CI_Date");
        tx.end("gmd:date");
        tx.end("gmd:CI_Citation");
        tx.end("gmd:specification");
        tx.start("gmd:explanation");
        tx.start("gco:CharacterString");
        tx.chars("See the referenced specification");
        tx.end("gco:CharacterString");
        tx.end("gmd:explanation");
        tx.start("gmd:pass");
        tx.start("gco:Boolean");
        tx.chars("true");
        tx.end("gco:Boolean");
        tx.end("gmd:pass");
        tx.end("gmd:DQ_ConformanceResult");
        tx.end("inspire_vs:Conformity");
        
        ContactInfo contact = wms.getGeoServer().getGlobal().getContact();
        tx.start("inspire_vs:MetadataPointOfContact");
        tx.start("gmd:CI_ResponsibleParty");
        if (contact.getContactOrganization() != null) {
            tx.start("gmd:organisationName");
            tx.start("gco:CharacterString");
            tx.chars(contact.getContactOrganization());
            tx.end("gco:CharacterString");
            tx.end("gmd:organisationName");
        }
        if (contact.getContactEmail() != null) {
            tx.start("gmd:contactInfo");
            tx.start("gmd:CI_Contact");
            tx.start("gmd:address");
            tx.start("gmd:CI_Address");
            tx.start("gmd:electronicMailAddress");
            tx.start("gco:CharacterString");
            tx.chars(contact.getContactEmail());
            tx.end("gco:CharacterString");
            tx.end("gmd:electronicMailAddress");
            tx.end("gmd:CI_Address");
            tx.end("gmd:address");
            tx.end("gmd:CI_Contact");
            tx.end("gmd:contactInfo");
        }
         tx.start("gmd:role");
          tx.start("gmd:CI_RoleCode", atts("codeList", "http://standards.iso.org/ittf/" +
              "PubliclyAvailableStandards/ISO_19139_Schemas/resources/Codelist/" +
              "gmxCodelists.xml#CI_RoleCode", "codeListValue", "pointOfContact"));
          tx.chars("pointOfContact");
          tx.end("gmd:CI_RoleCode");
         tx.end("gmd:role");
        tx.end("gmd:CI_ResponsibleParty");
       tx.end("inspire_vs:MetadataPointOfContact");

       tx.start("inspire_vs:MetadataDate");
       tx.start("gco:Date");
       tx.chars("2005-04-18");
       tx.end("gco:Date");
       tx.end("inspire_vs:MetadataDate");
       
       tx.start("inspire_vs:SpatialDataServiceType");
       tx.start("srv:serviceType");
       tx.start("gco:LocalName");
       tx.chars("view");
       tx.end("gco:LocalName");
       tx.end("srv:serviceType");
       tx.end("inspire_vs:SpatialDataServiceType");
       
       tx.start("inspire_vs:InspireKeywords");
       for(String kw : wms.getKeywords()) {
           tx.start("keyword");
           tx.start("gco:CharacterString");
           tx.chars(kw);
           tx.end("gco:CharacterString");
           tx.end("keyword");
       }
       tx.end("inspire_vs:InspireKeywords");
       
       String language = (String) wms.getMetadata().get(LANGUAGE.key);
       language = language != null ? language : "eng";
      
       tx.start("inspire_vs:Languages");
       tx.start("inspire_vs:Language", atts("default", "true"));
       tx.chars(language);
       tx.end("inspire_vs:Language");
       tx.start("inspire_vs:CurrentLanguage");
       tx.chars(language);
       tx.end("inspire_vs:CurrentLanguage");
       tx.end("inspire_vs:Languages");

       tx.end("inspire_vs:ExtendedCapabilities");
    }
    
    Attributes atts(String... atts) {
        AttributesImpl attributes = new AttributesImpl();
        for (int i = 0; i < atts.length; i += 2) {
            attributes.addAttribute(null, atts[i], atts[i], null, atts[i+1]);
        }
        return attributes;
    }

}
