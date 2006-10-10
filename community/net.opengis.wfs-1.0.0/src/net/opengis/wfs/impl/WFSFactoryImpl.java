/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import javax.xml.namespace.QName;

import net.opengis.wfs.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;

import org.opengis.filter.FeatureId;
import org.opengis.filter.Filter;

import org.opengis.filter.expression.PropertyName;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class WFSFactoryImpl extends EFactoryImpl implements WFSFactory {
	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WFSFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case WFSPackage.DELETE_ELEMENT_TYPE: return createDeleteElementType();
			case WFSPackage.DESCRIBE_FEATURE_TYPE_TYPE: return createDescribeFeatureTypeType();
			case WFSPackage.DOCUMENT_ROOT: return createDocumentRoot();
			case WFSPackage.EMPTY_TYPE: return createEmptyType();
			case WFSPackage.FEATURE_COLLECTION_TYPE: return createFeatureCollectionType();
			case WFSPackage.FEATURES_LOCKED_TYPE: return createFeaturesLockedType();
			case WFSPackage.FEATURES_NOT_LOCKED_TYPE: return createFeaturesNotLockedType();
			case WFSPackage.GET_CAPABILITIES_TYPE: return createGetCapabilitiesType();
			case WFSPackage.GET_FEATURE_TYPE: return createGetFeatureType();
			case WFSPackage.GET_FEATURE_WITH_LOCK_TYPE: return createGetFeatureWithLockType();
			case WFSPackage.INSERT_ELEMENT_TYPE: return createInsertElementType();
			case WFSPackage.INSERT_RESULT_TYPE: return createInsertResultType();
			case WFSPackage.LOCK_FEATURE_TYPE: return createLockFeatureType();
			case WFSPackage.LOCK_TYPE: return createLockType();
			case WFSPackage.NATIVE_TYPE: return createNativeType();
			case WFSPackage.PROPERTY_TYPE: return createPropertyType();
			case WFSPackage.QUERY_TYPE: return createQueryType();
			case WFSPackage.STATUS_TYPE: return createStatusType();
			case WFSPackage.TRANSACTION_RESULT_TYPE: return createTransactionResultType();
			case WFSPackage.TRANSACTION_TYPE: return createTransactionType();
			case WFSPackage.UPDATE_ELEMENT_TYPE: return createUpdateElementType();
			case WFSPackage.WFS_LOCK_FEATURE_RESPONSE_TYPE: return createWFSLockFeatureResponseType();
			case WFSPackage.WFS_TRANSACTION_RESPONSE_TYPE: return createWFSTransactionResponseType();
			case WFSPackage.TRANSACTION_OPERATION: return createTransactionOperation();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case WFSPackage.ALL_SOME_TYPE: {
				AllSomeType result = AllSomeType.get(initialValue);
				if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
				return result;
			}
			case WFSPackage.ALL_SOME_TYPE_OBJECT:
				return createAllSomeTypeObjectFromString(eDataType, initialValue);
			case WFSPackage.QNAME:
				return createQNameFromString(eDataType, initialValue);
			case WFSPackage.FILTER:
				return createFilterFromString(eDataType, initialValue);
			case WFSPackage.PROPERTY_NAME:
				return createPropertyNameFromString(eDataType, initialValue);
			case WFSPackage.FEATURE:
				return createFeatureFromString(eDataType, initialValue);
			case WFSPackage.FEATURE_COLLECTION:
				return createFeatureCollectionFromString(eDataType, initialValue);
			case WFSPackage.FEATURE_ID:
				return createFeatureIdFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case WFSPackage.ALL_SOME_TYPE:
				return instanceValue == null ? null : instanceValue.toString();
			case WFSPackage.ALL_SOME_TYPE_OBJECT:
				return convertAllSomeTypeObjectToString(eDataType, instanceValue);
			case WFSPackage.QNAME:
				return convertQNameToString(eDataType, instanceValue);
			case WFSPackage.FILTER:
				return convertFilterToString(eDataType, instanceValue);
			case WFSPackage.PROPERTY_NAME:
				return convertPropertyNameToString(eDataType, instanceValue);
			case WFSPackage.FEATURE:
				return convertFeatureToString(eDataType, instanceValue);
			case WFSPackage.FEATURE_COLLECTION:
				return convertFeatureCollectionToString(eDataType, instanceValue);
			case WFSPackage.FEATURE_ID:
				return convertFeatureIdToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeleteElementType createDeleteElementType() {
		DeleteElementTypeImpl deleteElementType = new DeleteElementTypeImpl();
		return deleteElementType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DescribeFeatureTypeType createDescribeFeatureTypeType() {
		DescribeFeatureTypeTypeImpl describeFeatureTypeType = new DescribeFeatureTypeTypeImpl();
		return describeFeatureTypeType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DocumentRoot createDocumentRoot() {
		DocumentRootImpl documentRoot = new DocumentRootImpl();
		return documentRoot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EmptyType createEmptyType() {
		EmptyTypeImpl emptyType = new EmptyTypeImpl();
		return emptyType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureCollectionType createFeatureCollectionType() {
		FeatureCollectionTypeImpl featureCollectionType = new FeatureCollectionTypeImpl();
		return featureCollectionType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeaturesLockedType createFeaturesLockedType() {
		FeaturesLockedTypeImpl featuresLockedType = new FeaturesLockedTypeImpl();
		return featuresLockedType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeaturesNotLockedType createFeaturesNotLockedType() {
		FeaturesNotLockedTypeImpl featuresNotLockedType = new FeaturesNotLockedTypeImpl();
		return featuresNotLockedType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GetCapabilitiesType createGetCapabilitiesType() {
		GetCapabilitiesTypeImpl getCapabilitiesType = new GetCapabilitiesTypeImpl();
		return getCapabilitiesType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GetFeatureType createGetFeatureType() {
		GetFeatureTypeImpl getFeatureType = new GetFeatureTypeImpl();
		return getFeatureType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GetFeatureWithLockType createGetFeatureWithLockType() {
		GetFeatureWithLockTypeImpl getFeatureWithLockType = new GetFeatureWithLockTypeImpl();
		return getFeatureWithLockType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InsertElementType createInsertElementType() {
		InsertElementTypeImpl insertElementType = new InsertElementTypeImpl();
		return insertElementType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InsertResultType createInsertResultType() {
		InsertResultTypeImpl insertResultType = new InsertResultTypeImpl();
		return insertResultType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LockFeatureType createLockFeatureType() {
		LockFeatureTypeImpl lockFeatureType = new LockFeatureTypeImpl();
		return lockFeatureType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LockType createLockType() {
		LockTypeImpl lockType = new LockTypeImpl();
		return lockType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NativeType createNativeType() {
		NativeTypeImpl nativeType = new NativeTypeImpl();
		return nativeType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PropertyType createPropertyType() {
		PropertyTypeImpl propertyType = new PropertyTypeImpl();
		return propertyType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QueryType createQueryType() {
		QueryTypeImpl queryType = new QueryTypeImpl();
		return queryType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StatusType createStatusType() {
		StatusTypeImpl statusType = new StatusTypeImpl();
		return statusType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TransactionResultType createTransactionResultType() {
		TransactionResultTypeImpl transactionResultType = new TransactionResultTypeImpl();
		return transactionResultType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TransactionType createTransactionType() {
		TransactionTypeImpl transactionType = new TransactionTypeImpl();
		return transactionType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UpdateElementType createUpdateElementType() {
		UpdateElementTypeImpl updateElementType = new UpdateElementTypeImpl();
		return updateElementType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WFSLockFeatureResponseType createWFSLockFeatureResponseType() {
		WFSLockFeatureResponseTypeImpl wfsLockFeatureResponseType = new WFSLockFeatureResponseTypeImpl();
		return wfsLockFeatureResponseType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WFSTransactionResponseType createWFSTransactionResponseType() {
		WFSTransactionResponseTypeImpl wfsTransactionResponseType = new WFSTransactionResponseTypeImpl();
		return wfsTransactionResponseType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TransactionOperation createTransactionOperation() {
		TransactionOperationImpl transactionOperation = new TransactionOperationImpl();
		return transactionOperation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AllSomeType createAllSomeTypeObjectFromString(EDataType eDataType, String initialValue) {
		return (AllSomeType)WFSFactory.eINSTANCE.createFromString(WFSPackage.eINSTANCE.getAllSomeType(), initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertAllSomeTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return WFSFactory.eINSTANCE.convertToString(WFSPackage.eINSTANCE.getAllSomeType(), instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QName createQNameFromString(EDataType eDataType, String initialValue) {
		return (QName)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertQNameToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Filter createFilterFromString(EDataType eDataType, String initialValue) {
		return (Filter)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertFilterToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PropertyName createPropertyNameFromString(EDataType eDataType, String initialValue) {
		return (PropertyName)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertPropertyNameToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Feature createFeatureFromString(EDataType eDataType, String initialValue) {
		return (Feature)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertFeatureToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureCollection createFeatureCollectionFromString(EDataType eDataType, String initialValue) {
		return (FeatureCollection)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertFeatureCollectionToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureId createFeatureIdFromString(EDataType eDataType, String initialValue) {
		return (FeatureId)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertFeatureIdToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WFSPackage getWFSPackage() {
		return (WFSPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	public static WFSPackage getPackage() {
		return WFSPackage.eINSTANCE;
	}

} //WFSFactoryImpl
