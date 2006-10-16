/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import java.net.URI;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.namespace.QName;

import net.opengis.wfs.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;

import org.opengis.filter.FeatureId;
import org.opengis.filter.Filter;

import org.opengis.filter.expression.Function;

import org.opengis.filter.expression.PropertyName;

import org.opengis.filter.sort.SortBy;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class WfsFactoryImpl extends EFactoryImpl implements WfsFactory {
	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WfsFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case WfsPackage.ACTION_TYPE: return createActionType();
			case WfsPackage.DELETE_ELEMENT_TYPE: return createDeleteElementType();
			case WfsPackage.DESCRIBE_FEATURE_TYPE_TYPE: return createDescribeFeatureTypeType();
			case WfsPackage.DOCUMENT_ROOT: return createDocumentRoot();
			case WfsPackage.FEATURE_COLLECTION_TYPE: return createFeatureCollectionType();
			case WfsPackage.FEATURE_TYPE_LIST_TYPE: return createFeatureTypeListType();
			case WfsPackage.FEATURE_TYPE_TYPE: return createFeatureTypeType();
			case WfsPackage.FEATURES_LOCKED_TYPE: return createFeaturesLockedType();
			case WfsPackage.FEATURES_NOT_LOCKED_TYPE: return createFeaturesNotLockedType();
			case WfsPackage.GML_OBJECT_TYPE_LIST_TYPE: return createGMLObjectTypeListType();
			case WfsPackage.GML_OBJECT_TYPE_TYPE: return createGMLObjectTypeType();
			case WfsPackage.GET_CAPABILITIES_TYPE: return createGetCapabilitiesType();
			case WfsPackage.GET_FEATURE_TYPE: return createGetFeatureType();
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE: return createGetFeatureWithLockType();
			case WfsPackage.GET_GML_OBJECT_TYPE: return createGetGmlObjectType();
			case WfsPackage.INSERT_ELEMENT_TYPE: return createInsertElementType();
			case WfsPackage.INSERT_RESULT_TYPE: return createInsertResultType();
			case WfsPackage.INSERT_RESULTS_TYPE: return createInsertResultsType();
			case WfsPackage.INSERTED_FEATURE_TYPE: return createInsertedFeatureType();
			case WfsPackage.LOCK_FEATURE_RESPONSE_TYPE: return createLockFeatureResponseType();
			case WfsPackage.LOCK_FEATURE_TYPE: return createLockFeatureType();
			case WfsPackage.LOCK_TYPE: return createLockType();
			case WfsPackage.METADATA_URL_TYPE: return createMetadataURLType();
			case WfsPackage.NATIVE_TYPE: return createNativeType();
			case WfsPackage.NO_SRS_TYPE: return createNoSRSType();
			case WfsPackage.OPERATIONS_TYPE: return createOperationsType();
			case WfsPackage.OUTPUT_FORMAT_LIST_TYPE: return createOutputFormatListType();
			case WfsPackage.PROPERTY_TYPE: return createPropertyType();
			case WfsPackage.QUERY_TYPE: return createQueryType();
			case WfsPackage.TRANSACTION_RESPONSE_TYPE: return createTransactionResponseType();
			case WfsPackage.TRANSACTION_RESULTS_TYPE: return createTransactionResultsType();
			case WfsPackage.TRANSACTION_SUMMARY_TYPE: return createTransactionSummaryType();
			case WfsPackage.TRANSACTION_TYPE: return createTransactionType();
			case WfsPackage.UPDATE_ELEMENT_TYPE: return createUpdateElementType();
			case WfsPackage.WFS_CAPABILITIES_TYPE: return createWFSCapabilitiesType();
			case WfsPackage.XLINK_PROPERTY_NAME_TYPE: return createXlinkPropertyNameType();
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
			case WfsPackage.ALL_SOME_TYPE: {
				AllSomeType result = AllSomeType.get(initialValue);
				if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
				return result;
			}
			case WfsPackage.IDENTIFIER_GENERATION_OPTION_TYPE: {
				IdentifierGenerationOptionType result = IdentifierGenerationOptionType.get(initialValue);
				if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
				return result;
			}
			case WfsPackage.OPERATION_TYPE: {
				OperationType result = OperationType.get(initialValue);
				if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
				return result;
			}
			case WfsPackage.RESULT_TYPE_TYPE: {
				ResultTypeType result = ResultTypeType.get(initialValue);
				if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
				return result;
			}
			case WfsPackage.TYPE_TYPE:
				return createTypeTypeFromString(eDataType, initialValue);
			case WfsPackage.FEATURE_COLLECTION:
				return createFeatureCollectionFromString(eDataType, initialValue);
			case WfsPackage.FEATURE:
				return createFeatureFromString(eDataType, initialValue);
			case WfsPackage.FORMAT_TYPE:
				return createFormatTypeFromString(eDataType, initialValue);
			case WfsPackage.CALENDAR:
				return createCalendarFromString(eDataType, initialValue);
			case WfsPackage.QNAME:
				return createQNameFromString(eDataType, initialValue);
			case WfsPackage.TYPE_NAME_LIST_TYPE:
				return createTypeNameListTypeFromString(eDataType, initialValue);
			case WfsPackage.FEATURE_ID:
				return createFeatureIdFromString(eDataType, initialValue);
			case WfsPackage.FUNCTION:
				return createFunctionFromString(eDataType, initialValue);
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
			case WfsPackage.ALL_SOME_TYPE:
				return instanceValue == null ? null : instanceValue.toString();
			case WfsPackage.IDENTIFIER_GENERATION_OPTION_TYPE:
				return instanceValue == null ? null : instanceValue.toString();
			case WfsPackage.OPERATION_TYPE:
				return instanceValue == null ? null : instanceValue.toString();
			case WfsPackage.RESULT_TYPE_TYPE:
				return instanceValue == null ? null : instanceValue.toString();
			case WfsPackage.TYPE_TYPE:
				return convertTypeTypeToString(eDataType, instanceValue);
			case WfsPackage.FEATURE_COLLECTION:
				return convertFeatureCollectionToString(eDataType, instanceValue);
			case WfsPackage.FEATURE:
				return convertFeatureToString(eDataType, instanceValue);
			case WfsPackage.FORMAT_TYPE:
				return convertFormatTypeToString(eDataType, instanceValue);
			case WfsPackage.CALENDAR:
				return convertCalendarToString(eDataType, instanceValue);
			case WfsPackage.QNAME:
				return convertQNameToString(eDataType, instanceValue);
			case WfsPackage.TYPE_NAME_LIST_TYPE:
				return convertTypeNameListTypeToString(eDataType, instanceValue);
			case WfsPackage.FEATURE_ID:
				return convertFeatureIdToString(eDataType, instanceValue);
			case WfsPackage.FUNCTION:
				return convertFunctionToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ActionType createActionType() {
		ActionTypeImpl actionType = new ActionTypeImpl();
		return actionType;
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
	public FeatureTypeListType createFeatureTypeListType() {
		FeatureTypeListTypeImpl featureTypeListType = new FeatureTypeListTypeImpl();
		return featureTypeListType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureTypeType createFeatureTypeType() {
		FeatureTypeTypeImpl featureTypeType = new FeatureTypeTypeImpl();
		return featureTypeType;
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
	public GetGmlObjectType createGetGmlObjectType() {
		GetGmlObjectTypeImpl getGmlObjectType = new GetGmlObjectTypeImpl();
		return getGmlObjectType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GMLObjectTypeListType createGMLObjectTypeListType() {
		GMLObjectTypeListTypeImpl gmlObjectTypeListType = new GMLObjectTypeListTypeImpl();
		return gmlObjectTypeListType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GMLObjectTypeType createGMLObjectTypeType() {
		GMLObjectTypeTypeImpl gmlObjectTypeType = new GMLObjectTypeTypeImpl();
		return gmlObjectTypeType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InsertedFeatureType createInsertedFeatureType() {
		InsertedFeatureTypeImpl insertedFeatureType = new InsertedFeatureTypeImpl();
		return insertedFeatureType;
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
	public InsertResultsType createInsertResultsType() {
		InsertResultsTypeImpl insertResultsType = new InsertResultsTypeImpl();
		return insertResultsType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LockFeatureResponseType createLockFeatureResponseType() {
		LockFeatureResponseTypeImpl lockFeatureResponseType = new LockFeatureResponseTypeImpl();
		return lockFeatureResponseType;
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
	public MetadataURLType createMetadataURLType() {
		MetadataURLTypeImpl metadataURLType = new MetadataURLTypeImpl();
		return metadataURLType;
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
	public NoSRSType createNoSRSType() {
		NoSRSTypeImpl noSRSType = new NoSRSTypeImpl();
		return noSRSType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OperationsType createOperationsType() {
		OperationsTypeImpl operationsType = new OperationsTypeImpl();
		return operationsType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OutputFormatListType createOutputFormatListType() {
		OutputFormatListTypeImpl outputFormatListType = new OutputFormatListTypeImpl();
		return outputFormatListType;
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
	public TransactionResponseType createTransactionResponseType() {
		TransactionResponseTypeImpl transactionResponseType = new TransactionResponseTypeImpl();
		return transactionResponseType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TransactionResultsType createTransactionResultsType() {
		TransactionResultsTypeImpl transactionResultsType = new TransactionResultsTypeImpl();
		return transactionResultsType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TransactionSummaryType createTransactionSummaryType() {
		TransactionSummaryTypeImpl transactionSummaryType = new TransactionSummaryTypeImpl();
		return transactionSummaryType;
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
	public WFSCapabilitiesType createWFSCapabilitiesType() {
		WFSCapabilitiesTypeImpl wfsCapabilitiesType = new WFSCapabilitiesTypeImpl();
		return wfsCapabilitiesType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public XlinkPropertyNameType createXlinkPropertyNameType() {
		XlinkPropertyNameTypeImpl xlinkPropertyNameType = new XlinkPropertyNameTypeImpl();
		return xlinkPropertyNameType;
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
	public String createFormatTypeFromString(EDataType eDataType, String initialValue) {
		return (String)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertFormatTypeToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List createTypeNameListTypeFromString(EDataType eDataType, String initialValue) {
		return (List)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertTypeNameListTypeToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String createTypeTypeFromString(EDataType eDataType, String initialValue) {
		return (String)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertTypeTypeToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
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
	public Function createFunctionFromString(EDataType eDataType, String initialValue) {
		return (Function)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertFunctionToString(EDataType eDataType, Object instanceValue) {
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
	public Calendar createCalendarFromString(EDataType eDataType, String initialValue) {
		return (Calendar)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertCalendarToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WfsPackage getWfsPackage() {
		return (WfsPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	public static WfsPackage getPackage() {
		return WfsPackage.eINSTANCE;
	}

} //WfsFactoryImpl
