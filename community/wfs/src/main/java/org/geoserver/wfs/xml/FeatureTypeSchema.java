package org.geoserver.wfs.xml;

import java.io.IOException;

import org.eclipse.xsd.XSDSchema;
import org.geoserver.GeoServerResourceLoader;
import org.geoserver.data.GeoServerCatalog;
import org.geoserver.data.feature.FeatureTypeInfo;
import org.geoserver.wfs.WFS;

/**
 * An xml schema describing a wfs feature type.
 * <p>
 * This 
 * </p>
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public abstract class FeatureTypeSchema {

	/**
	 * The feature type metadata object.
	 */
	protected FeatureTypeInfo featureType;
	
	/**
	 * The xsd schema builder.
	 */
	protected FeatureTypeSchemaBuilder builder;
	
	protected FeatureTypeSchema ( FeatureTypeInfo featureType ) {
		this.featureType = featureType;
	}
	
	/**
	 * @return The feautre type info.
	 */
	FeatureTypeInfo getFeatureType() {
		return featureType;
	}
	
	/**
	 * @return The {@link XSDSchema} representation of the schema.
	 */
	public XSDSchema schema() throws IOException {
		return builder.build( new FeatureTypeInfo[] { featureType } );
	}
	
	/**
	 * GML2 based wfs feature type schema.
	 * 
	 * @author Justin Deoliveira, The Open Planning Project
	 */
	public static final class GML2 extends FeatureTypeSchema {

		public GML2( FeatureTypeInfo featureType, WFS wfs, GeoServerCatalog catalog, GeoServerResourceLoader resourceLoader ) {
			super(featureType);
			builder = new FeatureTypeSchemaBuilder.GML2( wfs, catalog, resourceLoader );
		}

	}
	
	/**
	 * GML3 based wfs feature type schema.
	 * 
	 * @author Justin Deoliveira, The Open Planning Project
	 */
	public static final class GML3 extends FeatureTypeSchema {

		protected GML3( FeatureTypeInfo featureType, WFS wfs, GeoServerCatalog catalog, GeoServerResourceLoader resourceLoader ) {
			super(featureType);
			builder = new FeatureTypeSchemaBuilder.GML3( wfs, catalog, resourceLoader );
		}
		
		
	}
}