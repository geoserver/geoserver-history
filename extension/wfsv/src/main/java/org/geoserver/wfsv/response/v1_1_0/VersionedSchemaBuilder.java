package org.geoserver.wfsv.response.v1_1_0;

import org.eclipse.xsd.XSDSchema;
import org.geoserver.catalog.Catalog;
import org.geoserver.config.GeoServer;
import org.geoserver.platform.GeoServerResourceLoader;
import org.geoserver.wfs.WFSInfo;
import org.geoserver.wfs.xml.FeatureTypeSchemaBuilder;
import org.geoserver.wfs.xml.GML3Profile;
import org.geoserver.wfsv.xml.v1_1_0.WFSVConfiguration;
import org.opengis.feature.type.AttributeDescriptor;

public class VersionedSchemaBuilder extends FeatureTypeSchemaBuilder {
    /**
     * Cached gml3 schema
     */
    private static XSDSchema gml3Schema;

    public VersionedSchemaBuilder(GeoServer gs, GeoServerResourceLoader resourceLoader, WFSVConfiguration configuration) {
        super(gs, resourceLoader);

        profiles.add(new GML3Profile());

        gmlNamespace = org.geoserver.wfsv.xml.v1_1_0.WFSV.NAMESPACE;
        gmlSchemaLocation = "wfs/1.1.0/wfsv.xsd";
        baseType = "AbstractVersionedFeatureType";
        substitutionGroup = "_VersionedFeature";
        describeFeatureTypeBase = "request=DescribeVersionedFeatureType&version=1.1.0&versioned=true";
        gmlPrefix = "wfsv";
        xmlConfiguration = new org.geotools.gml3.GMLConfiguration();
    }

    protected XSDSchema gmlSchema() {
        if (gml3Schema == null) {
            gml3Schema = xmlConfiguration.schema();
        }

        return gml3Schema;
    }
    
    protected boolean filterAttributeType( AttributeDescriptor attribute ) {
        return super.filterAttributeType( attribute ) || 
            "metaDataProperty".equals( attribute.getName() ) || 
            "location".equals( attribute.getName() );
    }
}
