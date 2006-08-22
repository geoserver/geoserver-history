/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.v1_1_0.impl;

import java.math.BigInteger;

import java.util.Collection;

import net.opengis.wfs.v1_1_0.GetFeatureType;
import net.opengis.wfs.v1_1_0.QueryType;
import net.opengis.wfs.v1_1_0.ResultTypeType;
import net.opengis.wfs.v1_1_0.WFSPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Get Feature Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.GetFeatureTypeImpl#getQuery <em>Query</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.GetFeatureTypeImpl#getMaxFeatures <em>Max Features</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.GetFeatureTypeImpl#getOutputFormat <em>Output Format</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.GetFeatureTypeImpl#getResultType <em>Result Type</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.GetFeatureTypeImpl#getTraverseXlinkDepth <em>Traverse Xlink Depth</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.GetFeatureTypeImpl#getTraverseXlinkExpiry <em>Traverse Xlink Expiry</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GetFeatureTypeImpl extends BaseRequestTypeImpl implements GetFeatureType {
	/**
	 * The cached value of the '{@link #getQuery() <em>Query</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQuery()
	 * @generated
	 * @ordered
	 */
	protected EList query = null;

	/**
	 * The default value of the '{@link #getMaxFeatures() <em>Max Features</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxFeatures()
	 * @generated
	 * @ordered
	 */
	protected static final BigInteger MAX_FEATURES_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMaxFeatures() <em>Max Features</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxFeatures()
	 * @generated
	 * @ordered
	 */
	protected BigInteger maxFeatures = MAX_FEATURES_EDEFAULT;

	/**
	 * The default value of the '{@link #getOutputFormat() <em>Output Format</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutputFormat()
	 * @generated
	 * @ordered
	 */
	protected static final String OUTPUT_FORMAT_EDEFAULT = "text/xml; subtype=gml/3.1.1";

	/**
	 * The cached value of the '{@link #getOutputFormat() <em>Output Format</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutputFormat()
	 * @generated
	 * @ordered
	 */
	protected String outputFormat = OUTPUT_FORMAT_EDEFAULT;

	/**
	 * This is true if the Output Format attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean outputFormatESet = false;

	/**
	 * The default value of the '{@link #getResultType() <em>Result Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResultType()
	 * @generated
	 * @ordered
	 */
	protected static final ResultTypeType RESULT_TYPE_EDEFAULT = ResultTypeType.RESULTS_LITERAL;

	/**
	 * The cached value of the '{@link #getResultType() <em>Result Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResultType()
	 * @generated
	 * @ordered
	 */
	protected ResultTypeType resultType = RESULT_TYPE_EDEFAULT;

	/**
	 * This is true if the Result Type attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean resultTypeESet = false;

	/**
	 * The default value of the '{@link #getTraverseXlinkDepth() <em>Traverse Xlink Depth</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTraverseXlinkDepth()
	 * @generated
	 * @ordered
	 */
	protected static final String TRAVERSE_XLINK_DEPTH_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTraverseXlinkDepth() <em>Traverse Xlink Depth</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTraverseXlinkDepth()
	 * @generated
	 * @ordered
	 */
	protected String traverseXlinkDepth = TRAVERSE_XLINK_DEPTH_EDEFAULT;

	/**
	 * The default value of the '{@link #getTraverseXlinkExpiry() <em>Traverse Xlink Expiry</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTraverseXlinkExpiry()
	 * @generated
	 * @ordered
	 */
	protected static final BigInteger TRAVERSE_XLINK_EXPIRY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTraverseXlinkExpiry() <em>Traverse Xlink Expiry</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTraverseXlinkExpiry()
	 * @generated
	 * @ordered
	 */
	protected BigInteger traverseXlinkExpiry = TRAVERSE_XLINK_EXPIRY_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GetFeatureTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return WFSPackage.eINSTANCE.getGetFeatureType();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getQuery() {
		if (query == null) {
			query = new EObjectContainmentEList(QueryType.class, this, WFSPackage.GET_FEATURE_TYPE__QUERY);
		}
		return query;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BigInteger getMaxFeatures() {
		return maxFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxFeatures(BigInteger newMaxFeatures) {
		BigInteger oldMaxFeatures = maxFeatures;
		maxFeatures = newMaxFeatures;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.GET_FEATURE_TYPE__MAX_FEATURES, oldMaxFeatures, maxFeatures));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getOutputFormat() {
		return outputFormat;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOutputFormat(String newOutputFormat) {
		String oldOutputFormat = outputFormat;
		outputFormat = newOutputFormat;
		boolean oldOutputFormatESet = outputFormatESet;
		outputFormatESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.GET_FEATURE_TYPE__OUTPUT_FORMAT, oldOutputFormat, outputFormat, !oldOutputFormatESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetOutputFormat() {
		String oldOutputFormat = outputFormat;
		boolean oldOutputFormatESet = outputFormatESet;
		outputFormat = OUTPUT_FORMAT_EDEFAULT;
		outputFormatESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, WFSPackage.GET_FEATURE_TYPE__OUTPUT_FORMAT, oldOutputFormat, OUTPUT_FORMAT_EDEFAULT, oldOutputFormatESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetOutputFormat() {
		return outputFormatESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResultTypeType getResultType() {
		return resultType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setResultType(ResultTypeType newResultType) {
		ResultTypeType oldResultType = resultType;
		resultType = newResultType == null ? RESULT_TYPE_EDEFAULT : newResultType;
		boolean oldResultTypeESet = resultTypeESet;
		resultTypeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.GET_FEATURE_TYPE__RESULT_TYPE, oldResultType, resultType, !oldResultTypeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetResultType() {
		ResultTypeType oldResultType = resultType;
		boolean oldResultTypeESet = resultTypeESet;
		resultType = RESULT_TYPE_EDEFAULT;
		resultTypeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, WFSPackage.GET_FEATURE_TYPE__RESULT_TYPE, oldResultType, RESULT_TYPE_EDEFAULT, oldResultTypeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetResultType() {
		return resultTypeESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTraverseXlinkDepth() {
		return traverseXlinkDepth;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTraverseXlinkDepth(String newTraverseXlinkDepth) {
		String oldTraverseXlinkDepth = traverseXlinkDepth;
		traverseXlinkDepth = newTraverseXlinkDepth;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.GET_FEATURE_TYPE__TRAVERSE_XLINK_DEPTH, oldTraverseXlinkDepth, traverseXlinkDepth));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BigInteger getTraverseXlinkExpiry() {
		return traverseXlinkExpiry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTraverseXlinkExpiry(BigInteger newTraverseXlinkExpiry) {
		BigInteger oldTraverseXlinkExpiry = traverseXlinkExpiry;
		traverseXlinkExpiry = newTraverseXlinkExpiry;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.GET_FEATURE_TYPE__TRAVERSE_XLINK_EXPIRY, oldTraverseXlinkExpiry, traverseXlinkExpiry));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case WFSPackage.GET_FEATURE_TYPE__QUERY:
					return ((InternalEList)getQuery()).basicRemove(otherEnd, msgs);
				default:
					return eDynamicInverseRemove(otherEnd, featureID, baseClass, msgs);
			}
		}
		return eBasicSetContainer(null, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case WFSPackage.GET_FEATURE_TYPE__HANDLE:
				return getHandle();
			case WFSPackage.GET_FEATURE_TYPE__SERVICE:
				return getService();
			case WFSPackage.GET_FEATURE_TYPE__VERSION:
				return getVersion();
			case WFSPackage.GET_FEATURE_TYPE__QUERY:
				return getQuery();
			case WFSPackage.GET_FEATURE_TYPE__MAX_FEATURES:
				return getMaxFeatures();
			case WFSPackage.GET_FEATURE_TYPE__OUTPUT_FORMAT:
				return getOutputFormat();
			case WFSPackage.GET_FEATURE_TYPE__RESULT_TYPE:
				return getResultType();
			case WFSPackage.GET_FEATURE_TYPE__TRAVERSE_XLINK_DEPTH:
				return getTraverseXlinkDepth();
			case WFSPackage.GET_FEATURE_TYPE__TRAVERSE_XLINK_EXPIRY:
				return getTraverseXlinkExpiry();
		}
		return eDynamicGet(eFeature, resolve);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(EStructuralFeature eFeature, Object newValue) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case WFSPackage.GET_FEATURE_TYPE__HANDLE:
				setHandle((String)newValue);
				return;
			case WFSPackage.GET_FEATURE_TYPE__SERVICE:
				setService((String)newValue);
				return;
			case WFSPackage.GET_FEATURE_TYPE__VERSION:
				setVersion((String)newValue);
				return;
			case WFSPackage.GET_FEATURE_TYPE__QUERY:
				getQuery().clear();
				getQuery().addAll((Collection)newValue);
				return;
			case WFSPackage.GET_FEATURE_TYPE__MAX_FEATURES:
				setMaxFeatures((BigInteger)newValue);
				return;
			case WFSPackage.GET_FEATURE_TYPE__OUTPUT_FORMAT:
				setOutputFormat((String)newValue);
				return;
			case WFSPackage.GET_FEATURE_TYPE__RESULT_TYPE:
				setResultType((ResultTypeType)newValue);
				return;
			case WFSPackage.GET_FEATURE_TYPE__TRAVERSE_XLINK_DEPTH:
				setTraverseXlinkDepth((String)newValue);
				return;
			case WFSPackage.GET_FEATURE_TYPE__TRAVERSE_XLINK_EXPIRY:
				setTraverseXlinkExpiry((BigInteger)newValue);
				return;
		}
		eDynamicSet(eFeature, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case WFSPackage.GET_FEATURE_TYPE__HANDLE:
				setHandle(HANDLE_EDEFAULT);
				return;
			case WFSPackage.GET_FEATURE_TYPE__SERVICE:
				unsetService();
				return;
			case WFSPackage.GET_FEATURE_TYPE__VERSION:
				unsetVersion();
				return;
			case WFSPackage.GET_FEATURE_TYPE__QUERY:
				getQuery().clear();
				return;
			case WFSPackage.GET_FEATURE_TYPE__MAX_FEATURES:
				setMaxFeatures(MAX_FEATURES_EDEFAULT);
				return;
			case WFSPackage.GET_FEATURE_TYPE__OUTPUT_FORMAT:
				unsetOutputFormat();
				return;
			case WFSPackage.GET_FEATURE_TYPE__RESULT_TYPE:
				unsetResultType();
				return;
			case WFSPackage.GET_FEATURE_TYPE__TRAVERSE_XLINK_DEPTH:
				setTraverseXlinkDepth(TRAVERSE_XLINK_DEPTH_EDEFAULT);
				return;
			case WFSPackage.GET_FEATURE_TYPE__TRAVERSE_XLINK_EXPIRY:
				setTraverseXlinkExpiry(TRAVERSE_XLINK_EXPIRY_EDEFAULT);
				return;
		}
		eDynamicUnset(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case WFSPackage.GET_FEATURE_TYPE__HANDLE:
				return HANDLE_EDEFAULT == null ? handle != null : !HANDLE_EDEFAULT.equals(handle);
			case WFSPackage.GET_FEATURE_TYPE__SERVICE:
				return isSetService();
			case WFSPackage.GET_FEATURE_TYPE__VERSION:
				return isSetVersion();
			case WFSPackage.GET_FEATURE_TYPE__QUERY:
				return query != null && !query.isEmpty();
			case WFSPackage.GET_FEATURE_TYPE__MAX_FEATURES:
				return MAX_FEATURES_EDEFAULT == null ? maxFeatures != null : !MAX_FEATURES_EDEFAULT.equals(maxFeatures);
			case WFSPackage.GET_FEATURE_TYPE__OUTPUT_FORMAT:
				return isSetOutputFormat();
			case WFSPackage.GET_FEATURE_TYPE__RESULT_TYPE:
				return isSetResultType();
			case WFSPackage.GET_FEATURE_TYPE__TRAVERSE_XLINK_DEPTH:
				return TRAVERSE_XLINK_DEPTH_EDEFAULT == null ? traverseXlinkDepth != null : !TRAVERSE_XLINK_DEPTH_EDEFAULT.equals(traverseXlinkDepth);
			case WFSPackage.GET_FEATURE_TYPE__TRAVERSE_XLINK_EXPIRY:
				return TRAVERSE_XLINK_EXPIRY_EDEFAULT == null ? traverseXlinkExpiry != null : !TRAVERSE_XLINK_EXPIRY_EDEFAULT.equals(traverseXlinkExpiry);
		}
		return eDynamicIsSet(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (maxFeatures: ");
		result.append(maxFeatures);
		result.append(", outputFormat: ");
		if (outputFormatESet) result.append(outputFormat); else result.append("<unset>");
		result.append(", resultType: ");
		if (resultTypeESet) result.append(resultType); else result.append("<unset>");
		result.append(", traverseXlinkDepth: ");
		result.append(traverseXlinkDepth);
		result.append(", traverseXlinkExpiry: ");
		result.append(traverseXlinkExpiry);
		result.append(')');
		return result.toString();
	}

} //GetFeatureTypeImpl
