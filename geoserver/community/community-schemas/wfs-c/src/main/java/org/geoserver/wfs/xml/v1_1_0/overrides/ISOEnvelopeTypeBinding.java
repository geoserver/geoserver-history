/**
 * 
 */
package org.geoserver.wfs.xml.v1_1_0.overrides;

import java.net.URI;
import java.util.Map;

import javax.xml.namespace.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.geotools.feature.Name;
import org.geotools.feature.iso.UserData;
import org.geotools.gml3.bindings.EnvelopeTypeBinding;
import org.geotools.gml3.bindings.GML;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.Attribute;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.xml.sax.Attributes;

import com.vividsolutions.jts.geom.Envelope;

/**
 * @author Peter Warren
 *
 */
public class ISOEnvelopeTypeBinding extends EnvelopeTypeBinding {
	 
	public Object getProperty(Object object, QName name){
	        Attribute attribute = (Attribute) object;
	
	        
	        try
	        {
	        	Map clientProperties = (Map) ((UserData) attribute).getUserData(Attributes.class);
	        	if (clientProperties != null) {
	                Object prop =  clientProperties.get(new Name(name.getLocalPart()));
	                if (prop != null) {
	                    return prop;
	                }
	            }
	        }
	        catch (Exception ex)
	        {
	        	return null;
	        }
	        return null;
	}

    public Element encode(Object object, Document document, Element value)
    throws Exception {
    	
    if ( object instanceof Envelope)
    {
    	Envelope envelope = (Envelope) object;
    

    	if (envelope.isNull()) {
    		value.appendChild(document.createElementNS(GML.NAMESPACE, GML.Null.getLocalPart()));
    	}
    	else
    	{
    		Element lowerCorner = document.createElementNS(GML.NAMESPACE,"lowerCorner");
    		lowerCorner.appendChild(document.createTextNode(envelope.getMinX()+" "+envelope.getMinY()));
    		value.appendChild(lowerCorner);
    		Element upperCorner = document.createElementNS(GML.NAMESPACE,"upperCorner");
    		upperCorner.appendChild(document.createTextNode(envelope.getMaxX()+" "+envelope.getMaxY()));
    		value.appendChild(upperCorner);
    	}
 
    }
    else 
    {
    	// we will be creating internal object from individual attribute values.
    	Attribute envelope = (Attribute) object;
    	Object lc = getProperty(envelope, new QName (GML.NAMESPACE, "lowerCorner"));
    	if ( lc != null )
    	{
    		Element lowerCorner = document.createElementNS(GML.NAMESPACE,"lowerCorner");
    		lowerCorner.appendChild(document.createTextNode( lc.toString()));
    		value.appendChild(lowerCorner);
    	}

      	Object uc = getProperty(envelope, new QName (GML.NAMESPACE, "upperCorner"));
    	if ( uc != null )
    	{
    		Element upperCorner = document.createElementNS(GML.NAMESPACE,"upperCorner");
    		upperCorner.appendChild(document.createTextNode( uc.toString()));
    		value.appendChild(upperCorner);
    	}

    }
    
    return value;
    } 
	
}
