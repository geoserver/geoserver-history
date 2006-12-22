package org.geoserver.wfs.xml.filter.v1_1;

import org.geoserver.data.GeoServerCatalog;
import org.geoserver.wfs.WFSException;
import org.geotools.filter.v1_0.OGCPropertyNameTypeBinding;
import org.geotools.gml3.bindings.GML;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.PropertyName;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * A binding for ogc:PropertyName which does a special case check for an empty 
 * property name.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class PropertyNameTypeBinding extends OGCPropertyNameTypeBinding {

	/** the geoserver catalog */
	GeoServerCatalog catalog;
	/** parser namespace mappings */
	NamespaceSupport namespaceSupport;
	
	public PropertyNameTypeBinding(
		FilterFactory filterFactory, NamespaceSupport namespaceSupport, GeoServerCatalog catalog ) {
		super(filterFactory);
		this.namespaceSupport = namespaceSupport;
		this.catalog = catalog;
	}
	
	public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
		PropertyName propertyName = (PropertyName) super.parse( instance, node, value );	

		//JD: temporary hack, this should be carried out at evaluation time
		String name = propertyName.getPropertyName();
		if ( name.matches( "\\w+:\\w+" ) ) {
			//namespace qualified name, ensure the prefix is valid
			String prefix = name.substring( 0, name.indexOf( ':' ) );
			String namespaceURI = namespaceSupport.getURI( prefix );
			
			//only accept if its an application schema namespace, or gml
			
			if (!GML.NAMESPACE.equals( namespaceURI ) 
					&& catalog.getNamespaceSupport().getPrefix( namespaceURI) == null ) {
				throw new WFSException( "Illegal attribute namespace: " + namespaceURI );
			}
		}
		
		return propertyName;
	}
}
