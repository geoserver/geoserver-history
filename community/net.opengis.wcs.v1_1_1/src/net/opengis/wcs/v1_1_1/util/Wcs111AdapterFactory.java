/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs.v1_1_1.util;

import net.opengis.ows.v1_1_0.CapabilitiesBaseType;
import net.opengis.ows.v1_1_0.CodeType;
import net.opengis.ows.v1_1_0.DescriptionType;

import net.opengis.wcs.v1_1_1.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see net.opengis.wcs.v1_1_1.Wcs111Package
 * @generated
 */
public class Wcs111AdapterFactory extends AdapterFactoryImpl {
    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static Wcs111Package modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Wcs111AdapterFactory() {
        if (modelPackage == null) {
            modelPackage = Wcs111Package.eINSTANCE;
        }
    }

    /**
     * Returns whether this factory is applicable for the type of the object.
     * <!-- begin-user-doc -->
     * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
     * <!-- end-user-doc -->
     * @return whether this factory is applicable for the type of the object.
     * @generated
     */
    public boolean isFactoryForType(Object object) {
        if (object == modelPackage) {
            return true;
        }
        if (object instanceof EObject) {
            return ((EObject)object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
     * The switch the delegates to the <code>createXXX</code> methods.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected Wcs111Switch modelSwitch =
        new Wcs111Switch() {
            public Object caseAvailableKeysType(AvailableKeysType object) {
                return createAvailableKeysTypeAdapter();
            }
            public Object caseAxisSubsetType(AxisSubsetType object) {
                return createAxisSubsetTypeAdapter();
            }
            public Object caseAxisType(AxisType object) {
                return createAxisTypeAdapter();
            }
            public Object caseCapabilitiesType(CapabilitiesType object) {
                return createCapabilitiesTypeAdapter();
            }
            public Object caseContentsType(ContentsType object) {
                return createContentsTypeAdapter();
            }
            public Object caseCoverageDescriptionsType(CoverageDescriptionsType object) {
                return createCoverageDescriptionsTypeAdapter();
            }
            public Object caseCoverageDescriptionType(CoverageDescriptionType object) {
                return createCoverageDescriptionTypeAdapter();
            }
            public Object caseCoverageDomainType(CoverageDomainType object) {
                return createCoverageDomainTypeAdapter();
            }
            public Object caseCoveragesType(CoveragesType object) {
                return createCoveragesTypeAdapter();
            }
            public Object caseCoverageSummaryType(CoverageSummaryType object) {
                return createCoverageSummaryTypeAdapter();
            }
            public Object caseDescribeCoverageType(DescribeCoverageType object) {
                return createDescribeCoverageTypeAdapter();
            }
            public Object caseDocumentRoot(DocumentRoot object) {
                return createDocumentRootAdapter();
            }
            public Object caseDomainSubsetType(DomainSubsetType object) {
                return createDomainSubsetTypeAdapter();
            }
            public Object caseFieldSubsetType(FieldSubsetType object) {
                return createFieldSubsetTypeAdapter();
            }
            public Object caseFieldType(FieldType object) {
                return createFieldTypeAdapter();
            }
            public Object caseGetCapabilitiesType(GetCapabilitiesType object) {
                return createGetCapabilitiesTypeAdapter();
            }
            public Object caseGetCoverageType(GetCoverageType object) {
                return createGetCoverageTypeAdapter();
            }
            public Object caseGridCrsType(GridCrsType object) {
                return createGridCrsTypeAdapter();
            }
            public Object caseImageCRSRefType(ImageCRSRefType object) {
                return createImageCRSRefTypeAdapter();
            }
            public Object caseInterpolationMethodBaseType(InterpolationMethodBaseType object) {
                return createInterpolationMethodBaseTypeAdapter();
            }
            public Object caseInterpolationMethodsType(InterpolationMethodsType object) {
                return createInterpolationMethodsTypeAdapter();
            }
            public Object caseInterpolationMethodType(InterpolationMethodType object) {
                return createInterpolationMethodTypeAdapter();
            }
            public Object caseOutputType(OutputType object) {
                return createOutputTypeAdapter();
            }
            public Object caseRangeSubsetType(RangeSubsetType object) {
                return createRangeSubsetTypeAdapter();
            }
            public Object caseRangeType(RangeType object) {
                return createRangeTypeAdapter();
            }
            public Object caseRequestBaseType(RequestBaseType object) {
                return createRequestBaseTypeAdapter();
            }
            public Object caseSpatialDomainType(SpatialDomainType object) {
                return createSpatialDomainTypeAdapter();
            }
            public Object caseTimePeriodType(TimePeriodType object) {
                return createTimePeriodTypeAdapter();
            }
            public Object caseTimeSequenceType(TimeSequenceType object) {
                return createTimeSequenceTypeAdapter();
            }
            public Object caseDescriptionType(DescriptionType object) {
                return createDescriptionTypeAdapter();
            }
            public Object caseCapabilitiesBaseType(CapabilitiesBaseType object) {
                return createCapabilitiesBaseTypeAdapter();
            }
            public Object caseGetCapabilitiesType_1(net.opengis.ows.v1_1_0.GetCapabilitiesType object) {
                return createGetCapabilitiesType_1Adapter();
            }
            public Object caseCodeType(CodeType object) {
                return createCodeTypeAdapter();
            }
            public Object defaultCase(EObject object) {
                return createEObjectAdapter();
            }
        };

    /**
     * Creates an adapter for the <code>target</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param target the object to adapt.
     * @return the adapter for the <code>target</code>.
     * @generated
     */
    public Adapter createAdapter(Notifier target) {
        return (Adapter)modelSwitch.doSwitch((EObject)target);
    }


    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.v1_1_1.AvailableKeysType <em>Available Keys Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.v1_1_1.AvailableKeysType
     * @generated
     */
    public Adapter createAvailableKeysTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.v1_1_1.AxisSubsetType <em>Axis Subset Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.v1_1_1.AxisSubsetType
     * @generated
     */
    public Adapter createAxisSubsetTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.v1_1_1.AxisType <em>Axis Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.v1_1_1.AxisType
     * @generated
     */
    public Adapter createAxisTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.v1_1_1.CapabilitiesType <em>Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.v1_1_1.CapabilitiesType
     * @generated
     */
    public Adapter createCapabilitiesTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.v1_1_1.ContentsType <em>Contents Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.v1_1_1.ContentsType
     * @generated
     */
    public Adapter createContentsTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.v1_1_1.CoverageDescriptionsType <em>Coverage Descriptions Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.v1_1_1.CoverageDescriptionsType
     * @generated
     */
    public Adapter createCoverageDescriptionsTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.v1_1_1.CoverageDescriptionType <em>Coverage Description Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.v1_1_1.CoverageDescriptionType
     * @generated
     */
    public Adapter createCoverageDescriptionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.v1_1_1.CoverageDomainType <em>Coverage Domain Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.v1_1_1.CoverageDomainType
     * @generated
     */
    public Adapter createCoverageDomainTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.v1_1_1.CoveragesType <em>Coverages Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.v1_1_1.CoveragesType
     * @generated
     */
    public Adapter createCoveragesTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.v1_1_1.CoverageSummaryType <em>Coverage Summary Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.v1_1_1.CoverageSummaryType
     * @generated
     */
    public Adapter createCoverageSummaryTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.v1_1_1.DescribeCoverageType <em>Describe Coverage Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.v1_1_1.DescribeCoverageType
     * @generated
     */
    public Adapter createDescribeCoverageTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.v1_1_1.DocumentRoot <em>Document Root</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.v1_1_1.DocumentRoot
     * @generated
     */
    public Adapter createDocumentRootAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.v1_1_1.DomainSubsetType <em>Domain Subset Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.v1_1_1.DomainSubsetType
     * @generated
     */
    public Adapter createDomainSubsetTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.v1_1_1.FieldSubsetType <em>Field Subset Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.v1_1_1.FieldSubsetType
     * @generated
     */
    public Adapter createFieldSubsetTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.v1_1_1.FieldType <em>Field Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.v1_1_1.FieldType
     * @generated
     */
    public Adapter createFieldTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.v1_1_1.GetCapabilitiesType <em>Get Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.v1_1_1.GetCapabilitiesType
     * @generated
     */
    public Adapter createGetCapabilitiesTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.v1_1_1.GetCoverageType <em>Get Coverage Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.v1_1_1.GetCoverageType
     * @generated
     */
    public Adapter createGetCoverageTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.v1_1_1.GridCrsType <em>Grid Crs Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.v1_1_1.GridCrsType
     * @generated
     */
    public Adapter createGridCrsTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.v1_1_1.ImageCRSRefType <em>Image CRS Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.v1_1_1.ImageCRSRefType
     * @generated
     */
    public Adapter createImageCRSRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.v1_1_1.InterpolationMethodBaseType <em>Interpolation Method Base Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.v1_1_1.InterpolationMethodBaseType
     * @generated
     */
    public Adapter createInterpolationMethodBaseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.v1_1_1.InterpolationMethodsType <em>Interpolation Methods Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.v1_1_1.InterpolationMethodsType
     * @generated
     */
    public Adapter createInterpolationMethodsTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.v1_1_1.InterpolationMethodType <em>Interpolation Method Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.v1_1_1.InterpolationMethodType
     * @generated
     */
    public Adapter createInterpolationMethodTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.v1_1_1.OutputType <em>Output Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.v1_1_1.OutputType
     * @generated
     */
    public Adapter createOutputTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.v1_1_1.RangeSubsetType <em>Range Subset Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.v1_1_1.RangeSubsetType
     * @generated
     */
    public Adapter createRangeSubsetTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.v1_1_1.RangeType <em>Range Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.v1_1_1.RangeType
     * @generated
     */
    public Adapter createRangeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.v1_1_1.RequestBaseType <em>Request Base Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.v1_1_1.RequestBaseType
     * @generated
     */
    public Adapter createRequestBaseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.v1_1_1.SpatialDomainType <em>Spatial Domain Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.v1_1_1.SpatialDomainType
     * @generated
     */
    public Adapter createSpatialDomainTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.v1_1_1.TimePeriodType <em>Time Period Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.v1_1_1.TimePeriodType
     * @generated
     */
    public Adapter createTimePeriodTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.v1_1_1.TimeSequenceType <em>Time Sequence Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.v1_1_1.TimeSequenceType
     * @generated
     */
    public Adapter createTimeSequenceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.DescriptionType <em>Description Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.DescriptionType
     * @generated
     */
    public Adapter createDescriptionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.CapabilitiesBaseType <em>Capabilities Base Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.CapabilitiesBaseType
     * @generated
     */
    public Adapter createCapabilitiesBaseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.GetCapabilitiesType <em>Get Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.GetCapabilitiesType
     * @generated
     */
    public Adapter createGetCapabilitiesType_1Adapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.CodeType <em>Code Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.CodeType
     * @generated
     */
    public Adapter createCodeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for the default case.
     * <!-- begin-user-doc -->
     * This default implementation returns null.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @generated
     */
    public Adapter createEObjectAdapter() {
        return null;
    }

} //Wcs111AdapterFactory
