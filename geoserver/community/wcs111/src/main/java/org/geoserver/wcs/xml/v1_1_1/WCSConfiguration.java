package org.geoserver.wcs.xml.v1_1_1;

import java.util.Map;

import javax.xml.namespace.QName;

import net.opengis.ows.v1_1_0.Ows11Factory;
import net.opengis.wcs.v1_1_1.Wcs111Factory;

import org.eclipse.emf.ecore.EFactory;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.ows.v1_1.OWS;
import org.geotools.ows.v1_1.OWSConfiguration;
import org.geotools.xml.ComplexEMFBinding;
import org.geotools.xml.Configuration;
import org.picocontainer.MutablePicoContainer;

/**
 * Parser configuration for the http://www.opengis.net/wcs/1.1.1 schema.
 *
 * @generated
 */
public class WCSConfiguration extends Configuration {

    /**
     * Creates a new configuration.
     * 
     * @generated
     */     
    public WCSConfiguration() {
       super(WCS.getInstance());
       
       addDependency(new GMLConfiguration());
       addDependency(new OWSConfiguration());
    }
    
    /**
     * Registers the bindings for the configuration.
     *
     * @generated
     */
    protected final void registerBindings( MutablePicoContainer container ) {
        //Types
//        container.registerComponentImplementation(WCS.AxisType,AxisTypeBinding.class);
//        container.registerComponentImplementation(WCS.CoverageDescriptionType,CoverageDescriptionTypeBinding.class);
//        container.registerComponentImplementation(WCS.CoverageDomainType,CoverageDomainTypeBinding.class);
//        container.registerComponentImplementation(WCS.CoveragesType,CoveragesTypeBinding.class);
//        container.registerComponentImplementation(WCS.CoverageSummaryType,CoverageSummaryTypeBinding.class);
//        container.registerComponentImplementation(WCS.DomainSubsetType,DomainSubsetTypeBinding.class);
//        container.registerComponentImplementation(WCS.FieldType,FieldTypeBinding.class);
//        container.registerComponentImplementation(WCS.GridCrsType,GridCrsTypeBinding.class);
//        container.registerComponentImplementation(WCS.IdentifierType,IdentifierTypeBinding.class);
//        container.registerComponentImplementation(WCS.ImageCRSRefType,ImageCRSRefTypeBinding.class);
//        container.registerComponentImplementation(WCS.InterpolationMethodBaseType,InterpolationMethodBaseTypeBinding.class);
//        container.registerComponentImplementation(WCS.InterpolationMethodType,InterpolationMethodTypeBinding.class);
//        container.registerComponentImplementation(WCS.OutputType,OutputTypeBinding.class);
//        container.registerComponentImplementation(WCS.RangeSubsetType,RangeSubsetTypeBinding.class);
//        container.registerComponentImplementation(WCS.RangeType,RangeTypeBinding.class);
//        container.registerComponentImplementation(WCS.RequestBaseType,RequestBaseTypeBinding.class);
//        container.registerComponentImplementation(WCS.SpatialDomainType,SpatialDomainTypeBinding.class);
//        container.registerComponentImplementation(WCS.TimeDurationType,TimeDurationTypeBinding.class);
//        container.registerComponentImplementation(WCS.TimePeriodType,TimePeriodTypeBinding.class);
//        container.registerComponentImplementation(WCS.TimeSequenceType,TimeSequenceTypeBinding.class);
//        container.registerComponentImplementation(WCS._AvailableKeys,_AvailableKeysBinding.class);
//        container.registerComponentImplementation(WCS._AxisSubset,_AxisSubsetBinding.class);
//        container.registerComponentImplementation(WCS._Capabilities,_CapabilitiesBinding.class);
//        container.registerComponentImplementation(WCS._Contents,_ContentsBinding.class);
//        container.registerComponentImplementation(WCS._CoverageDescriptions,_CoverageDescriptionsBinding.class);
//        container.registerComponentImplementation(WCS._GetCapabilities,_GetCapabilitiesBinding.class);
//        container.registerComponentImplementation(WCS._GetCoverage,_GetCoverageBinding.class);
//        container.registerComponentImplementation(WCS._InterpolationMethods,_InterpolationMethodsBinding.class);
//        container.registerComponentImplementation(WCS.RangeSubsetType_FieldSubset,RangeSubsetType_FieldSubsetBinding.class);
    }
    
    @Override
    protected void registerBindings(Map bindings) {
        super.registerBindings(bindings);
        
        final EFactory wcsFactory = Wcs111Factory.eINSTANCE;
        register(bindings, wcsFactory, WCS._DescribeCoverage);
        register(bindings, wcsFactory, WCS._GetCoverage);
        register(bindings, wcsFactory, WCS.DomainSubsetType);
        register(bindings, wcsFactory, WCS.TimeDurationType);
        register(bindings, wcsFactory, WCS.TimePeriodType);
        register(bindings, wcsFactory, WCS.TimeSequenceType);
        register(bindings, wcsFactory, WCS.RangeSubsetType);
        register(bindings, wcsFactory, WCS.RangeSubsetType_FieldSubset);
        register(bindings, wcsFactory, WCS._AxisSubset);
        register(bindings, wcsFactory, WCS.OutputType);

        // todo: move these to the geotools ows module when done
        final EFactory owsFactory = Ows11Factory.eINSTANCE; 
        register(bindings, owsFactory, OWS.BoundingBoxType);
        register(bindings, owsFactory, OWS.CodeType);
        
    }
    
    private void register(Map bindings, EFactory factory, QName qname) {
        bindings.put(qname, new ComplexEMFBinding(factory, qname));
    }

    protected void configureContext(MutablePicoContainer container) {
        container.registerComponentInstance(Wcs111Factory.eINSTANCE);
    }
} 