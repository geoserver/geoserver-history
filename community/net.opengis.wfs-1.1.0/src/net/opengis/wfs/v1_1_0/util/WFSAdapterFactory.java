/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.v1_1_0.util;

import net.opengis.ows.v1_0_0.CapabilitiesBaseType;

import net.opengis.wfs.v1_1_0.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see net.opengis.wfs.v1_1_0.WFSPackage
 * @generated
 */
public class WFSAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static WFSPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WFSAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = WFSPackage.eINSTANCE;
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
	protected WFSSwitch modelSwitch =
		new WFSSwitch() {
			public Object caseActionType(ActionType object) {
				return createActionTypeAdapter();
			}
			public Object caseBaseRequestType(BaseRequestType object) {
				return createBaseRequestTypeAdapter();
			}
			public Object caseDeleteElementType(DeleteElementType object) {
				return createDeleteElementTypeAdapter();
			}
			public Object caseDescribeFeatureTypeType(DescribeFeatureTypeType object) {
				return createDescribeFeatureTypeTypeAdapter();
			}
			public Object caseDocumentRoot(DocumentRoot object) {
				return createDocumentRootAdapter();
			}
			public Object caseFeatureCollectionType(FeatureCollectionType object) {
				return createFeatureCollectionTypeAdapter();
			}
			public Object caseFeaturesLockedType(FeaturesLockedType object) {
				return createFeaturesLockedTypeAdapter();
			}
			public Object caseFeaturesNotLockedType(FeaturesNotLockedType object) {
				return createFeaturesNotLockedTypeAdapter();
			}
			public Object caseFeatureTypeListType(FeatureTypeListType object) {
				return createFeatureTypeListTypeAdapter();
			}
			public Object caseFeatureTypeType(FeatureTypeType object) {
				return createFeatureTypeTypeAdapter();
			}
			public Object caseGetCapabilitiesType(GetCapabilitiesType object) {
				return createGetCapabilitiesTypeAdapter();
			}
			public Object caseGetFeatureType(GetFeatureType object) {
				return createGetFeatureTypeAdapter();
			}
			public Object caseGetFeatureWithLockType(GetFeatureWithLockType object) {
				return createGetFeatureWithLockTypeAdapter();
			}
			public Object caseGetGmlObjectType(GetGmlObjectType object) {
				return createGetGmlObjectTypeAdapter();
			}
			public Object caseGMLObjectTypeListType(GMLObjectTypeListType object) {
				return createGMLObjectTypeListTypeAdapter();
			}
			public Object caseGMLObjectTypeType(GMLObjectTypeType object) {
				return createGMLObjectTypeTypeAdapter();
			}
			public Object caseInsertedFeatureType(InsertedFeatureType object) {
				return createInsertedFeatureTypeAdapter();
			}
			public Object caseInsertElementType(InsertElementType object) {
				return createInsertElementTypeAdapter();
			}
			public Object caseInsertResultType(InsertResultType object) {
				return createInsertResultTypeAdapter();
			}
			public Object caseLockFeatureResponseType(LockFeatureResponseType object) {
				return createLockFeatureResponseTypeAdapter();
			}
			public Object caseLockFeatureType(LockFeatureType object) {
				return createLockFeatureTypeAdapter();
			}
			public Object caseLockType(LockType object) {
				return createLockTypeAdapter();
			}
			public Object caseMetadataURLType(MetadataURLType object) {
				return createMetadataURLTypeAdapter();
			}
			public Object caseNativeType(NativeType object) {
				return createNativeTypeAdapter();
			}
			public Object caseNoSRSType(NoSRSType object) {
				return createNoSRSTypeAdapter();
			}
			public Object caseOperationsType(OperationsType object) {
				return createOperationsTypeAdapter();
			}
			public Object caseOutputFormatListType(OutputFormatListType object) {
				return createOutputFormatListTypeAdapter();
			}
			public Object casePropertyType(PropertyType object) {
				return createPropertyTypeAdapter();
			}
			public Object caseQueryType(QueryType object) {
				return createQueryTypeAdapter();
			}
			public Object caseTransactionResponseType(TransactionResponseType object) {
				return createTransactionResponseTypeAdapter();
			}
			public Object caseTransactionResultsType(TransactionResultsType object) {
				return createTransactionResultsTypeAdapter();
			}
			public Object caseTransactionSummaryType(TransactionSummaryType object) {
				return createTransactionSummaryTypeAdapter();
			}
			public Object caseTransactionType(TransactionType object) {
				return createTransactionTypeAdapter();
			}
			public Object caseUpdateElementType(UpdateElementType object) {
				return createUpdateElementTypeAdapter();
			}
			public Object caseWFSCapabilitiesType(WFSCapabilitiesType object) {
				return createWFSCapabilitiesTypeAdapter();
			}
			public Object caseOWS_GetCapabilitiesType(net.opengis.ows.v1_0_0.GetCapabilitiesType object) {
				return createOWS_GetCapabilitiesTypeAdapter();
			}
			public Object caseCapabilitiesBaseType(CapabilitiesBaseType object) {
				return createCapabilitiesBaseTypeAdapter();
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
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.ActionType <em>Action Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.ActionType
	 * @generated
	 */
	public Adapter createActionTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.BaseRequestType <em>Base Request Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.BaseRequestType
	 * @generated
	 */
	public Adapter createBaseRequestTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.DeleteElementType <em>Delete Element Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.DeleteElementType
	 * @generated
	 */
	public Adapter createDeleteElementTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.DescribeFeatureTypeType <em>Describe Feature Type Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.DescribeFeatureTypeType
	 * @generated
	 */
	public Adapter createDescribeFeatureTypeTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.DocumentRoot <em>Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.DocumentRoot
	 * @generated
	 */
	public Adapter createDocumentRootAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.FeatureCollectionType <em>Feature Collection Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.FeatureCollectionType
	 * @generated
	 */
	public Adapter createFeatureCollectionTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.FeaturesLockedType <em>Features Locked Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.FeaturesLockedType
	 * @generated
	 */
	public Adapter createFeaturesLockedTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.FeaturesNotLockedType <em>Features Not Locked Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.FeaturesNotLockedType
	 * @generated
	 */
	public Adapter createFeaturesNotLockedTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.FeatureTypeListType <em>Feature Type List Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.FeatureTypeListType
	 * @generated
	 */
	public Adapter createFeatureTypeListTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.FeatureTypeType <em>Feature Type Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.FeatureTypeType
	 * @generated
	 */
	public Adapter createFeatureTypeTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.GetCapabilitiesType <em>Get Capabilities Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.GetCapabilitiesType
	 * @generated
	 */
	public Adapter createGetCapabilitiesTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.GetFeatureType <em>Get Feature Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.GetFeatureType
	 * @generated
	 */
	public Adapter createGetFeatureTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.GetFeatureWithLockType <em>Get Feature With Lock Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.GetFeatureWithLockType
	 * @generated
	 */
	public Adapter createGetFeatureWithLockTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.GetGmlObjectType <em>Get Gml Object Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.GetGmlObjectType
	 * @generated
	 */
	public Adapter createGetGmlObjectTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.GMLObjectTypeListType <em>GML Object Type List Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.GMLObjectTypeListType
	 * @generated
	 */
	public Adapter createGMLObjectTypeListTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.GMLObjectTypeType <em>GML Object Type Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.GMLObjectTypeType
	 * @generated
	 */
	public Adapter createGMLObjectTypeTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.InsertedFeatureType <em>Inserted Feature Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.InsertedFeatureType
	 * @generated
	 */
	public Adapter createInsertedFeatureTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.InsertElementType <em>Insert Element Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.InsertElementType
	 * @generated
	 */
	public Adapter createInsertElementTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.InsertResultType <em>Insert Result Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.InsertResultType
	 * @generated
	 */
	public Adapter createInsertResultTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.LockFeatureResponseType <em>Lock Feature Response Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.LockFeatureResponseType
	 * @generated
	 */
	public Adapter createLockFeatureResponseTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.LockFeatureType <em>Lock Feature Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.LockFeatureType
	 * @generated
	 */
	public Adapter createLockFeatureTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.LockType <em>Lock Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.LockType
	 * @generated
	 */
	public Adapter createLockTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.MetadataURLType <em>Metadata URL Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.MetadataURLType
	 * @generated
	 */
	public Adapter createMetadataURLTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.NativeType <em>Native Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.NativeType
	 * @generated
	 */
	public Adapter createNativeTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.NoSRSType <em>No SRS Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.NoSRSType
	 * @generated
	 */
	public Adapter createNoSRSTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.OperationsType <em>Operations Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.OperationsType
	 * @generated
	 */
	public Adapter createOperationsTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.OutputFormatListType <em>Output Format List Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.OutputFormatListType
	 * @generated
	 */
	public Adapter createOutputFormatListTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.PropertyType <em>Property Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.PropertyType
	 * @generated
	 */
	public Adapter createPropertyTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.QueryType <em>Query Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.QueryType
	 * @generated
	 */
	public Adapter createQueryTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.TransactionResponseType <em>Transaction Response Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.TransactionResponseType
	 * @generated
	 */
	public Adapter createTransactionResponseTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.TransactionResultsType <em>Transaction Results Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.TransactionResultsType
	 * @generated
	 */
	public Adapter createTransactionResultsTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.TransactionSummaryType <em>Transaction Summary Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.TransactionSummaryType
	 * @generated
	 */
	public Adapter createTransactionSummaryTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.TransactionType <em>Transaction Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.TransactionType
	 * @generated
	 */
	public Adapter createTransactionTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.UpdateElementType <em>Update Element Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.UpdateElementType
	 * @generated
	 */
	public Adapter createUpdateElementTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wfs.v1_1_0.WFSCapabilitiesType <em>Capabilities Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wfs.v1_1_0.WFSCapabilitiesType
	 * @generated
	 */
	public Adapter createWFSCapabilitiesTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_0_0.GetCapabilitiesType <em>Get Capabilities Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.ows.v1_0_0.GetCapabilitiesType
	 * @generated
	 */
	public Adapter createOWS_GetCapabilitiesTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_0_0.CapabilitiesBaseType <em>Capabilities Base Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.ows.v1_0_0.CapabilitiesBaseType
	 * @generated
	 */
	public Adapter createCapabilitiesBaseTypeAdapter() {
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

} //WFSAdapterFactory
