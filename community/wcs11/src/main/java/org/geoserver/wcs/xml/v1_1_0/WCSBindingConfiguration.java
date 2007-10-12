package org.geoserver.wcs.xml.v1_1_0;


import org.geotools.xml.BindingConfiguration;
import org.picocontainer.MutablePicoContainer;

/**
 * Binding configuration for the http://www.opengis.net/wcs/1.1 schema.
 *
 * @generated
 */
public final class WCSBindingConfiguration
	implements BindingConfiguration {


	/**
	 * @generated modifiable
	 */
	public void configure(MutablePicoContainer container) {
	
		//Types
		container.registerComponentImplementation(WCS.AxisType,AxisTypeBinding.class);
		container.registerComponentImplementation(WCS.CoverageDescriptionType,CoverageDescriptionTypeBinding.class);
		container.registerComponentImplementation(WCS.CoverageDomainType,CoverageDomainTypeBinding.class);
		container.registerComponentImplementation(WCS.CoverageSummaryType,CoverageSummaryTypeBinding.class);
		container.registerComponentImplementation(WCS.DomainSubsetType,DomainSubsetTypeBinding.class);
		container.registerComponentImplementation(WCS.FieldType,FieldTypeBinding.class);
		container.registerComponentImplementation(WCS.GridCrsType,GridCrsTypeBinding.class);
		container.registerComponentImplementation(WCS.IdentifierType,IdentifierTypeBinding.class);
		container.registerComponentImplementation(WCS.ImageCRSRefType,ImageCRSRefTypeBinding.class);
		container.registerComponentImplementation(WCS.OutputType,OutputTypeBinding.class);
		container.registerComponentImplementation(WCS.RangeSubsetType,RangeSubsetTypeBinding.class);
		container.registerComponentImplementation(WCS.RangeType,RangeTypeBinding.class);
		container.registerComponentImplementation(WCS.RequestBaseType,RequestBaseTypeBinding.class);
		container.registerComponentImplementation(WCS.SpatialDomainType,SpatialDomainTypeBinding.class);
		container.registerComponentImplementation(WCS.TimeDurationType,TimeDurationTypeBinding.class);
		container.registerComponentImplementation(WCS.TimePeriodType,TimePeriodTypeBinding.class);
		container.registerComponentImplementation(WCS.TimeSequenceType,TimeSequenceTypeBinding.class);
		container.registerComponentImplementation(WCS._AvailableKeys,_AvailableKeysBinding.class);
		container.registerComponentImplementation(WCS._AxisSubset,_AxisSubsetBinding.class);
		container.registerComponentImplementation(WCS._Capabilities,_CapabilitiesBinding.class);
		container.registerComponentImplementation(WCS._Contents,_ContentsBinding.class);
		container.registerComponentImplementation(WCS._CoverageDescriptions,_CoverageDescriptionsBinding.class);
		container.registerComponentImplementation(WCS._DescribeCoverage,_DescribeCoverageBinding.class);
		container.registerComponentImplementation(WCS._GetCapabilities,_GetCapabilitiesBinding.class);
		container.registerComponentImplementation(WCS._GetCoverage,_GetCoverageBinding.class);
		container.registerComponentImplementation(WCS.RangeSubsetType_FieldSubset,RangeSubsetType_FieldSubsetBinding.class);


	}

}