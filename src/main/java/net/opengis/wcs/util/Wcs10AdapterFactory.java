/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs.util;

import net.opengis.gml.AbstractGMLType;
import net.opengis.gml.AbstractGeometryBaseType;
import net.opengis.gml.AbstractGeometryType;
import net.opengis.gml.EnvelopeType;
import net.opengis.gml.MetaDataPropertyType;

import net.opengis.wcs.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see net.opengis.wcs.Wcs10Package
 * @generated
 */
public class Wcs10AdapterFactory extends AdapterFactoryImpl {
    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static Wcs10Package modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Wcs10AdapterFactory() {
        if (modelPackage == null) {
            modelPackage = Wcs10Package.eINSTANCE;
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
    protected Wcs10Switch modelSwitch =
        new Wcs10Switch() {
            public Object caseAbstractDescriptionBaseType(AbstractDescriptionBaseType object) {
                return createAbstractDescriptionBaseTypeAdapter();
            }
            public Object caseAbstractDescriptionType(AbstractDescriptionType object) {
                return createAbstractDescriptionTypeAdapter();
            }
            public Object caseAddressType(AddressType object) {
                return createAddressTypeAdapter();
            }
            public Object caseAxisDescriptionType(AxisDescriptionType object) {
                return createAxisDescriptionTypeAdapter();
            }
            public Object caseAxisDescriptionType1(AxisDescriptionType1 object) {
                return createAxisDescriptionType1Adapter();
            }
            public Object caseAxisSubsetType(AxisSubsetType object) {
                return createAxisSubsetTypeAdapter();
            }
            public Object caseContactType(ContactType object) {
                return createContactTypeAdapter();
            }
            public Object caseContentMetadataType(ContentMetadataType object) {
                return createContentMetadataTypeAdapter();
            }
            public Object caseCoverageDescriptionType(CoverageDescriptionType object) {
                return createCoverageDescriptionTypeAdapter();
            }
            public Object caseCoverageOfferingBriefType(CoverageOfferingBriefType object) {
                return createCoverageOfferingBriefTypeAdapter();
            }
            public Object caseCoverageOfferingType(CoverageOfferingType object) {
                return createCoverageOfferingTypeAdapter();
            }
            public Object caseDCPTypeType(DCPTypeType object) {
                return createDCPTypeTypeAdapter();
            }
            public Object caseDescribeCoverageType(DescribeCoverageType object) {
                return createDescribeCoverageTypeAdapter();
            }
            public Object caseDescribeCoverageType1(DescribeCoverageType1 object) {
                return createDescribeCoverageType1Adapter();
            }
            public Object caseDocumentRoot(DocumentRoot object) {
                return createDocumentRootAdapter();
            }
            public Object caseDomainSetType(DomainSetType object) {
                return createDomainSetTypeAdapter();
            }
            public Object caseDomainSubsetType(DomainSubsetType object) {
                return createDomainSubsetTypeAdapter();
            }
            public Object caseExceptionType(ExceptionType object) {
                return createExceptionTypeAdapter();
            }
            public Object caseGetCapabilitiesType(GetCapabilitiesType object) {
                return createGetCapabilitiesTypeAdapter();
            }
            public Object caseGetCapabilitiesType1(GetCapabilitiesType1 object) {
                return createGetCapabilitiesType1Adapter();
            }
            public Object caseGetCoverageType(GetCoverageType object) {
                return createGetCoverageTypeAdapter();
            }
            public Object caseGetCoverageType1(GetCoverageType1 object) {
                return createGetCoverageType1Adapter();
            }
            public Object caseGetType(GetType object) {
                return createGetTypeAdapter();
            }
            public Object caseHTTPType(HTTPType object) {
                return createHTTPTypeAdapter();
            }
            public Object caseIntervalType(IntervalType object) {
                return createIntervalTypeAdapter();
            }
            public Object caseKeywordsType(KeywordsType object) {
                return createKeywordsTypeAdapter();
            }
            public Object caseLonLatEnvelopeBaseType(LonLatEnvelopeBaseType object) {
                return createLonLatEnvelopeBaseTypeAdapter();
            }
            public Object caseLonLatEnvelopeType(LonLatEnvelopeType object) {
                return createLonLatEnvelopeTypeAdapter();
            }
            public Object caseMetadataAssociationType(MetadataAssociationType object) {
                return createMetadataAssociationTypeAdapter();
            }
            public Object caseMetadataLinkType(MetadataLinkType object) {
                return createMetadataLinkTypeAdapter();
            }
            public Object caseOnlineResourceType(OnlineResourceType object) {
                return createOnlineResourceTypeAdapter();
            }
            public Object caseOutputType(OutputType object) {
                return createOutputTypeAdapter();
            }
            public Object casePostType(PostType object) {
                return createPostTypeAdapter();
            }
            public Object caseRangeSetType(RangeSetType object) {
                return createRangeSetTypeAdapter();
            }
            public Object caseRangeSetType1(RangeSetType1 object) {
                return createRangeSetType1Adapter();
            }
            public Object caseRangeSubsetType(RangeSubsetType object) {
                return createRangeSubsetTypeAdapter();
            }
            public Object caseRequestType(RequestType object) {
                return createRequestTypeAdapter();
            }
            public Object caseResponsiblePartyType(ResponsiblePartyType object) {
                return createResponsiblePartyTypeAdapter();
            }
            public Object caseServiceType(ServiceType object) {
                return createServiceTypeAdapter();
            }
            public Object caseSpatialDomainType(SpatialDomainType object) {
                return createSpatialDomainTypeAdapter();
            }
            public Object caseSpatialSubsetType(SpatialSubsetType object) {
                return createSpatialSubsetTypeAdapter();
            }
            public Object caseSupportedCRSsType(SupportedCRSsType object) {
                return createSupportedCRSsTypeAdapter();
            }
            public Object caseSupportedFormatsType(SupportedFormatsType object) {
                return createSupportedFormatsTypeAdapter();
            }
            public Object caseSupportedInterpolationsType(SupportedInterpolationsType object) {
                return createSupportedInterpolationsTypeAdapter();
            }
            public Object caseTelephoneType(TelephoneType object) {
                return createTelephoneTypeAdapter();
            }
            public Object caseTimePeriodType(TimePeriodType object) {
                return createTimePeriodTypeAdapter();
            }
            public Object caseTimeSequenceType(TimeSequenceType object) {
                return createTimeSequenceTypeAdapter();
            }
            public Object caseTypedLiteralType(TypedLiteralType object) {
                return createTypedLiteralTypeAdapter();
            }
            public Object caseValueEnumBaseType(ValueEnumBaseType object) {
                return createValueEnumBaseTypeAdapter();
            }
            public Object caseValueEnumType(ValueEnumType object) {
                return createValueEnumTypeAdapter();
            }
            public Object caseValueRangeType(ValueRangeType object) {
                return createValueRangeTypeAdapter();
            }
            public Object caseValuesType(ValuesType object) {
                return createValuesTypeAdapter();
            }
            public Object caseVendorSpecificCapabilitiesType(VendorSpecificCapabilitiesType object) {
                return createVendorSpecificCapabilitiesTypeAdapter();
            }
            public Object caseWCSCapabilitiesType(WCSCapabilitiesType object) {
                return createWCSCapabilitiesTypeAdapter();
            }
            public Object caseWCSCapabilityType(WCSCapabilityType object) {
                return createWCSCapabilityTypeAdapter();
            }
            public Object caseAbstractGMLType(AbstractGMLType object) {
                return createAbstractGMLTypeAdapter();
            }
            public Object caseAbstractGeometryBaseType(AbstractGeometryBaseType object) {
                return createAbstractGeometryBaseTypeAdapter();
            }
            public Object caseAbstractGeometryType(AbstractGeometryType object) {
                return createAbstractGeometryTypeAdapter();
            }
            public Object caseEnvelopeType(EnvelopeType object) {
                return createEnvelopeTypeAdapter();
            }
            public Object caseMetaDataPropertyType(MetaDataPropertyType object) {
                return createMetaDataPropertyTypeAdapter();
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
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.AbstractDescriptionBaseType <em>Abstract Description Base Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.AbstractDescriptionBaseType
     * @generated
     */
    public Adapter createAbstractDescriptionBaseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.AbstractDescriptionType <em>Abstract Description Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.AbstractDescriptionType
     * @generated
     */
    public Adapter createAbstractDescriptionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.AddressType <em>Address Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.AddressType
     * @generated
     */
    public Adapter createAddressTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.AxisDescriptionType <em>Axis Description Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.AxisDescriptionType
     * @generated
     */
    public Adapter createAxisDescriptionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.AxisDescriptionType1 <em>Axis Description Type1</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.AxisDescriptionType1
     * @generated
     */
    public Adapter createAxisDescriptionType1Adapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.AxisSubsetType <em>Axis Subset Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.AxisSubsetType
     * @generated
     */
    public Adapter createAxisSubsetTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ContactType <em>Contact Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ContactType
     * @generated
     */
    public Adapter createContactTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ContentMetadataType <em>Content Metadata Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ContentMetadataType
     * @generated
     */
    public Adapter createContentMetadataTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.CoverageDescriptionType <em>Coverage Description Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.CoverageDescriptionType
     * @generated
     */
    public Adapter createCoverageDescriptionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.CoverageOfferingBriefType <em>Coverage Offering Brief Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.CoverageOfferingBriefType
     * @generated
     */
    public Adapter createCoverageOfferingBriefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.CoverageOfferingType <em>Coverage Offering Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.CoverageOfferingType
     * @generated
     */
    public Adapter createCoverageOfferingTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.DCPTypeType <em>DCP Type Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.DCPTypeType
     * @generated
     */
    public Adapter createDCPTypeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.DescribeCoverageType <em>Describe Coverage Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.DescribeCoverageType
     * @generated
     */
    public Adapter createDescribeCoverageTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.DescribeCoverageType1 <em>Describe Coverage Type1</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.DescribeCoverageType1
     * @generated
     */
    public Adapter createDescribeCoverageType1Adapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.DocumentRoot <em>Document Root</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.DocumentRoot
     * @generated
     */
    public Adapter createDocumentRootAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.DomainSetType <em>Domain Set Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.DomainSetType
     * @generated
     */
    public Adapter createDomainSetTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.DomainSubsetType <em>Domain Subset Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.DomainSubsetType
     * @generated
     */
    public Adapter createDomainSubsetTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ExceptionType <em>Exception Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ExceptionType
     * @generated
     */
    public Adapter createExceptionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.GetCapabilitiesType <em>Get Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.GetCapabilitiesType
     * @generated
     */
    public Adapter createGetCapabilitiesTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.GetCapabilitiesType1 <em>Get Capabilities Type1</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.GetCapabilitiesType1
     * @generated
     */
    public Adapter createGetCapabilitiesType1Adapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.GetCoverageType <em>Get Coverage Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.GetCoverageType
     * @generated
     */
    public Adapter createGetCoverageTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.GetCoverageType1 <em>Get Coverage Type1</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.GetCoverageType1
     * @generated
     */
    public Adapter createGetCoverageType1Adapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.GetType <em>Get Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.GetType
     * @generated
     */
    public Adapter createGetTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.HTTPType <em>HTTP Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.HTTPType
     * @generated
     */
    public Adapter createHTTPTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.IntervalType <em>Interval Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.IntervalType
     * @generated
     */
    public Adapter createIntervalTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.KeywordsType <em>Keywords Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.KeywordsType
     * @generated
     */
    public Adapter createKeywordsTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.LonLatEnvelopeBaseType <em>Lon Lat Envelope Base Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.LonLatEnvelopeBaseType
     * @generated
     */
    public Adapter createLonLatEnvelopeBaseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.LonLatEnvelopeType <em>Lon Lat Envelope Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.LonLatEnvelopeType
     * @generated
     */
    public Adapter createLonLatEnvelopeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.MetadataAssociationType <em>Metadata Association Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.MetadataAssociationType
     * @generated
     */
    public Adapter createMetadataAssociationTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.MetadataLinkType <em>Metadata Link Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.MetadataLinkType
     * @generated
     */
    public Adapter createMetadataLinkTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.OnlineResourceType <em>Online Resource Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.OnlineResourceType
     * @generated
     */
    public Adapter createOnlineResourceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.OutputType <em>Output Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.OutputType
     * @generated
     */
    public Adapter createOutputTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.PostType <em>Post Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.PostType
     * @generated
     */
    public Adapter createPostTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.RangeSetType <em>Range Set Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.RangeSetType
     * @generated
     */
    public Adapter createRangeSetTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.RangeSetType1 <em>Range Set Type1</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.RangeSetType1
     * @generated
     */
    public Adapter createRangeSetType1Adapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.RangeSubsetType <em>Range Subset Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.RangeSubsetType
     * @generated
     */
    public Adapter createRangeSubsetTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.RequestType <em>Request Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.RequestType
     * @generated
     */
    public Adapter createRequestTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ResponsiblePartyType <em>Responsible Party Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ResponsiblePartyType
     * @generated
     */
    public Adapter createResponsiblePartyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ServiceType <em>Service Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ServiceType
     * @generated
     */
    public Adapter createServiceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.SpatialDomainType <em>Spatial Domain Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.SpatialDomainType
     * @generated
     */
    public Adapter createSpatialDomainTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.SpatialSubsetType <em>Spatial Subset Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.SpatialSubsetType
     * @generated
     */
    public Adapter createSpatialSubsetTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.SupportedCRSsType <em>Supported CR Ss Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.SupportedCRSsType
     * @generated
     */
    public Adapter createSupportedCRSsTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.SupportedFormatsType <em>Supported Formats Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.SupportedFormatsType
     * @generated
     */
    public Adapter createSupportedFormatsTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.SupportedInterpolationsType <em>Supported Interpolations Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.SupportedInterpolationsType
     * @generated
     */
    public Adapter createSupportedInterpolationsTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.TelephoneType <em>Telephone Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.TelephoneType
     * @generated
     */
    public Adapter createTelephoneTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.TimePeriodType <em>Time Period Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.TimePeriodType
     * @generated
     */
    public Adapter createTimePeriodTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.TimeSequenceType <em>Time Sequence Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.TimeSequenceType
     * @generated
     */
    public Adapter createTimeSequenceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.TypedLiteralType <em>Typed Literal Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.TypedLiteralType
     * @generated
     */
    public Adapter createTypedLiteralTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ValueEnumBaseType <em>Value Enum Base Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ValueEnumBaseType
     * @generated
     */
    public Adapter createValueEnumBaseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ValueEnumType <em>Value Enum Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ValueEnumType
     * @generated
     */
    public Adapter createValueEnumTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ValueRangeType <em>Value Range Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ValueRangeType
     * @generated
     */
    public Adapter createValueRangeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ValuesType <em>Values Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ValuesType
     * @generated
     */
    public Adapter createValuesTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.VendorSpecificCapabilitiesType <em>Vendor Specific Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.VendorSpecificCapabilitiesType
     * @generated
     */
    public Adapter createVendorSpecificCapabilitiesTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.WCSCapabilitiesType <em>WCS Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.WCSCapabilitiesType
     * @generated
     */
    public Adapter createWCSCapabilitiesTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.WCSCapabilityType <em>WCS Capability Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.WCSCapabilityType
     * @generated
     */
    public Adapter createWCSCapabilityTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml.AbstractGMLType <em>Abstract GML Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml.AbstractGMLType
     * @generated
     */
    public Adapter createAbstractGMLTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml.AbstractGeometryBaseType <em>Abstract Geometry Base Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml.AbstractGeometryBaseType
     * @generated
     */
    public Adapter createAbstractGeometryBaseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml.AbstractGeometryType <em>Abstract Geometry Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml.AbstractGeometryType
     * @generated
     */
    public Adapter createAbstractGeometryTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml.EnvelopeType <em>Envelope Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml.EnvelopeType
     * @generated
     */
    public Adapter createEnvelopeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml.MetaDataPropertyType <em>Meta Data Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml.MetaDataPropertyType
     * @generated
     */
    public Adapter createMetaDataPropertyTypeAdapter() {
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

} //Wcs10AdapterFactory
