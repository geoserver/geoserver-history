package org.geoserver.wfs.xml;

import org.geotools.feature.Feature;
import org.geotools.gml.producer.FeatureTransformer;
import org.geotools.gml.producer.FeatureTransformer.FeatureTranslator;
import org.geotools.gml.producer.FeatureTransformer.FeatureTypeNamespaces;
import org.geotools.gml3.bindings.GML;
import org.geotools.xml.transform.Translator;
import org.geotools.xml.transform.TransformerBase.SchemaLocationSupport;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;

public class GML3FeatureTransformer extends FeatureTransformer {

	protected FeatureTranslator createTranslator(
		ContentHandler handler, String prefix, String ns, 
		FeatureTypeNamespaces featureTypeNamespaces, SchemaLocationSupport schemaLocationSupport
	) {
		return new GML3FeatureTranslator( handler, prefix, ns, featureTypeNamespaces, schemaLocationSupport );
	}
	
	
	public static class GML3FeatureTranslator extends FeatureTranslator {

		public GML3FeatureTranslator( 
			ContentHandler handler, String prefix, String ns, 
			FeatureTypeNamespaces featureTypeNamespaces, SchemaLocationSupport schemaLocationSupport	
		) {
			super( handler, prefix, ns, featureTypeNamespaces, schemaLocationSupport ); 
		}
		
		protected Attributes encodeFeatureId( Feature f ) {
			
			AttributesImpl atts = new AttributesImpl();
			if ( f.getID() != null ) {
				atts.addAttribute( GML.NAMESPACE, "id", "gml:id", null, f.getID() );
			}
			
			return atts;
		} 
		
	}
}
