/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs.ows.util;

import net.opengis.wcs.ows.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see net.opengis.wcs.ows.owcsPackage
 * @generated
 */
public class owcsAdapterFactory extends AdapterFactoryImpl {
    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static owcsPackage modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public owcsAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = owcsPackage.eINSTANCE;
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
    protected owcsSwitch modelSwitch =
        new owcsSwitch() {
            public Object caseAbstractReferenceBaseType(AbstractReferenceBaseType object) {
                return createAbstractReferenceBaseTypeAdapter();
            }
            public Object caseAcceptFormatsType(AcceptFormatsType object) {
                return createAcceptFormatsTypeAdapter();
            }
            public Object caseAcceptVersionsType(AcceptVersionsType object) {
                return createAcceptVersionsTypeAdapter();
            }
            public Object caseAllowedValuesType(AllowedValuesType object) {
                return createAllowedValuesTypeAdapter();
            }
            public Object caseAnyValueType(AnyValueType object) {
                return createAnyValueTypeAdapter();
            }
            public Object caseBasicIdentificationType(BasicIdentificationType object) {
                return createBasicIdentificationTypeAdapter();
            }
            public Object caseCapabilitiesBaseType(CapabilitiesBaseType object) {
                return createCapabilitiesBaseTypeAdapter();
            }
            public Object caseCoveragesType(CoveragesType object) {
                return createCoveragesTypeAdapter();
            }
            public Object caseDCPType(DCPType object) {
                return createDCPTypeAdapter();
            }
            public Object caseDescriptionType(DescriptionType object) {
                return createDescriptionTypeAdapter();
            }
            public Object caseDocumentRoot(DocumentRoot object) {
                return createDocumentRootAdapter();
            }
            public Object caseDomainMetadataType(DomainMetadataType object) {
                return createDomainMetadataTypeAdapter();
            }
            public Object caseDomainType(DomainType object) {
                return createDomainTypeAdapter();
            }
            public Object caseGetCapabilitiesType(GetCapabilitiesType object) {
                return createGetCapabilitiesTypeAdapter();
            }
            public Object caseHTTPType(HTTPType object) {
                return createHTTPTypeAdapter();
            }
            public Object caseIdentificationType(IdentificationType object) {
                return createIdentificationTypeAdapter();
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
            public Object caseManifestType(ManifestType object) {
                return createManifestTypeAdapter();
            }
            public Object caseNoValuesType(NoValuesType object) {
                return createNoValuesTypeAdapter();
            }
            public Object caseOperationsMetadataType(OperationsMetadataType object) {
                return createOperationsMetadataTypeAdapter();
            }
            public Object caseOperationType(OperationType object) {
                return createOperationTypeAdapter();
            }
            public Object caseRangeType(RangeType object) {
                return createRangeTypeAdapter();
            }
            public Object caseReferenceGroupType(ReferenceGroupType object) {
                return createReferenceGroupTypeAdapter();
            }
            public Object caseReferenceType(ReferenceType object) {
                return createReferenceTypeAdapter();
            }
            public Object caseRequestMethodType(RequestMethodType object) {
                return createRequestMethodTypeAdapter();
            }
            public Object caseSectionsType(SectionsType object) {
                return createSectionsTypeAdapter();
            }
            public Object caseServiceIdentificationType(ServiceIdentificationType object) {
                return createServiceIdentificationTypeAdapter();
            }
            public Object caseServiceReferenceType(ServiceReferenceType object) {
                return createServiceReferenceTypeAdapter();
            }
            public Object caseUnNamedDomainType(UnNamedDomainType object) {
                return createUnNamedDomainTypeAdapter();
            }
            public Object caseValuesReferenceType(ValuesReferenceType object) {
                return createValuesReferenceTypeAdapter();
            }
            public Object caseValueType(ValueType object) {
                return createValueTypeAdapter();
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
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.AbstractReferenceBaseType <em>Abstract Reference Base Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.AbstractReferenceBaseType
     * @generated
     */
    public Adapter createAbstractReferenceBaseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.AcceptFormatsType <em>Accept Formats Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.AcceptFormatsType
     * @generated
     */
    public Adapter createAcceptFormatsTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.AcceptVersionsType <em>Accept Versions Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.AcceptVersionsType
     * @generated
     */
    public Adapter createAcceptVersionsTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.AllowedValuesType <em>Allowed Values Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.AllowedValuesType
     * @generated
     */
    public Adapter createAllowedValuesTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.AnyValueType <em>Any Value Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.AnyValueType
     * @generated
     */
    public Adapter createAnyValueTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.BasicIdentificationType <em>Basic Identification Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.BasicIdentificationType
     * @generated
     */
    public Adapter createBasicIdentificationTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.CapabilitiesBaseType <em>Capabilities Base Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.CapabilitiesBaseType
     * @generated
     */
    public Adapter createCapabilitiesBaseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.CoveragesType <em>Coverages Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.CoveragesType
     * @generated
     */
    public Adapter createCoveragesTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.DCPType <em>DCP Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.DCPType
     * @generated
     */
    public Adapter createDCPTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.DescriptionType <em>Description Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.DescriptionType
     * @generated
     */
    public Adapter createDescriptionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.DocumentRoot <em>Document Root</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.DocumentRoot
     * @generated
     */
    public Adapter createDocumentRootAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.DomainMetadataType <em>Domain Metadata Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.DomainMetadataType
     * @generated
     */
    public Adapter createDomainMetadataTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.DomainType <em>Domain Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.DomainType
     * @generated
     */
    public Adapter createDomainTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.GetCapabilitiesType <em>Get Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.GetCapabilitiesType
     * @generated
     */
    public Adapter createGetCapabilitiesTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.HTTPType <em>HTTP Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.HTTPType
     * @generated
     */
    public Adapter createHTTPTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.IdentificationType <em>Identification Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.IdentificationType
     * @generated
     */
    public Adapter createIdentificationTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.InterpolationMethodBaseType <em>Interpolation Method Base Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.InterpolationMethodBaseType
     * @generated
     */
    public Adapter createInterpolationMethodBaseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.InterpolationMethodsType <em>Interpolation Methods Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.InterpolationMethodsType
     * @generated
     */
    public Adapter createInterpolationMethodsTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.InterpolationMethodType <em>Interpolation Method Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.InterpolationMethodType
     * @generated
     */
    public Adapter createInterpolationMethodTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.ManifestType <em>Manifest Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.ManifestType
     * @generated
     */
    public Adapter createManifestTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.NoValuesType <em>No Values Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.NoValuesType
     * @generated
     */
    public Adapter createNoValuesTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.OperationsMetadataType <em>Operations Metadata Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.OperationsMetadataType
     * @generated
     */
    public Adapter createOperationsMetadataTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.OperationType <em>Operation Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.OperationType
     * @generated
     */
    public Adapter createOperationTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.RangeType <em>Range Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.RangeType
     * @generated
     */
    public Adapter createRangeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.ReferenceGroupType <em>Reference Group Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.ReferenceGroupType
     * @generated
     */
    public Adapter createReferenceGroupTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.ReferenceType <em>Reference Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.ReferenceType
     * @generated
     */
    public Adapter createReferenceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.RequestMethodType <em>Request Method Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.RequestMethodType
     * @generated
     */
    public Adapter createRequestMethodTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.SectionsType <em>Sections Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.SectionsType
     * @generated
     */
    public Adapter createSectionsTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.ServiceIdentificationType <em>Service Identification Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.ServiceIdentificationType
     * @generated
     */
    public Adapter createServiceIdentificationTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.ServiceReferenceType <em>Service Reference Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.ServiceReferenceType
     * @generated
     */
    public Adapter createServiceReferenceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.UnNamedDomainType <em>Un Named Domain Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.UnNamedDomainType
     * @generated
     */
    public Adapter createUnNamedDomainTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.ValuesReferenceType <em>Values Reference Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.ValuesReferenceType
     * @generated
     */
    public Adapter createValuesReferenceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wcs.ows.ValueType <em>Value Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wcs.ows.ValueType
     * @generated
     */
    public Adapter createValueTypeAdapter() {
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

} //owcsAdapterFactory
