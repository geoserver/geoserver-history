package org.geoserver.wfs.xml.wfs;


import org.geotools.xml.BindingConfiguration;
import org.picocontainer.MutablePicoContainer;

/**
 * Binding configuration for the http://www.opengis.net/wfs schema.
 *
 * @generated
 */
public final class WFSBindingConfiguration
	implements BindingConfiguration {


	/**
	 * @generated modifiable
	 */
	public void configure(MutablePicoContainer container) {
	
		//Types
		container.registerComponentImplementation(WFS.DESCRIBEFEATURETYPETYPE,DescribeFeatureTypeTypeBinding.class);
		//container.registerComponentImplementation(WFS.FEATURECOLLECTIONTYPE,FeatureCollectionTypeBinding.class);
		container.registerComponentImplementation(WFS.GETCAPABILITIESTYPE,GetCapabilitiesTypeBinding.class);
		container.registerComponentImplementation(WFS.GETFEATURETYPE,GetFeatureTypeBinding.class);
		container.registerComponentImplementation(WFS.QUERYTYPE,QueryTypeBinding.class);
		
		//Elements
//		container.registerComponentImplementation(WFS.DESCRIBEFEATURETYPE,DescribeFeatureTypeBinding.class);
//		container.registerComponentImplementation(WFS.FEATURECOLLECTION,FeatureCollectionBinding.class);
//		container.registerComponentImplementation(WFS.GETCAPABILITIES,GetCapabilitiesBinding.class);
//		container.registerComponentImplementation(WFS.GETFEATURE,GetFeatureBinding.class);
//		container.registerComponentImplementation(WFS.QUERY,QueryBinding.class);

	}

}