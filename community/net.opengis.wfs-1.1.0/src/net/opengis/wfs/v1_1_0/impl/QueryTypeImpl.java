/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.v1_1_0.impl;

import java.util.Collection;
import java.util.List;

import net.opengis.wfs.v1_1_0.QueryType;
import net.opengis.wfs.v1_1_0.WFSPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Query Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.QueryTypeImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.QueryTypeImpl#getPropertyName <em>Property Name</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.QueryTypeImpl#getFunction <em>Function</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.QueryTypeImpl#getFilter <em>Filter</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.QueryTypeImpl#getSortBy <em>Sort By</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.QueryTypeImpl#getFeatureVersion <em>Feature Version</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.QueryTypeImpl#getHandle <em>Handle</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.QueryTypeImpl#getSrsName <em>Srs Name</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.QueryTypeImpl#getTypeName <em>Type Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class QueryTypeImpl extends EObjectImpl implements QueryType {
	/**
	 * The cached value of the '{@link #getGroup() <em>Group</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGroup()
	 * @generated
	 * @ordered
	 */
	protected FeatureMap group = null;

	/**
	 * The default value of the '{@link #getFilter() <em>Filter</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFilter()
	 * @generated
	 * @ordered
	 */
	protected static final Object FILTER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFilter() <em>Filter</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFilter()
	 * @generated
	 * @ordered
	 */
	protected Object filter = FILTER_EDEFAULT;

	/**
	 * The default value of the '{@link #getSortBy() <em>Sort By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSortBy()
	 * @generated
	 * @ordered
	 */
	protected static final Object SORT_BY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSortBy() <em>Sort By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSortBy()
	 * @generated
	 * @ordered
	 */
	protected Object sortBy = SORT_BY_EDEFAULT;

	/**
	 * The default value of the '{@link #getFeatureVersion() <em>Feature Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFeatureVersion()
	 * @generated
	 * @ordered
	 */
	protected static final String FEATURE_VERSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFeatureVersion() <em>Feature Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFeatureVersion()
	 * @generated
	 * @ordered
	 */
	protected String featureVersion = FEATURE_VERSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getHandle() <em>Handle</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHandle()
	 * @generated
	 * @ordered
	 */
	protected static final String HANDLE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getHandle() <em>Handle</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHandle()
	 * @generated
	 * @ordered
	 */
	protected String handle = HANDLE_EDEFAULT;

	/**
	 * The default value of the '{@link #getSrsName() <em>Srs Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSrsName()
	 * @generated
	 * @ordered
	 */
	protected static final String SRS_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSrsName() <em>Srs Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSrsName()
	 * @generated
	 * @ordered
	 */
	protected String srsName = SRS_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getTypeName() <em>Type Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypeName()
	 * @generated
	 * @ordered
	 */
	protected static final List TYPE_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTypeName() <em>Type Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypeName()
	 * @generated
	 * @ordered
	 */
	protected List typeName = TYPE_NAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected QueryTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return WFSPackage.eINSTANCE.getQueryType();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getGroup() {
		if (group == null) {
			group = new BasicFeatureMap(this, WFSPackage.QUERY_TYPE__GROUP);
		}
		return group;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getPropertyName() {
		return ((FeatureMap)getGroup()).list(WFSPackage.eINSTANCE.getQueryType_PropertyName());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getFunction() {
		return ((FeatureMap)getGroup()).list(WFSPackage.eINSTANCE.getQueryType_Function());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getFilter() {
		return filter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFilter(Object newFilter) {
		Object oldFilter = filter;
		filter = newFilter;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.QUERY_TYPE__FILTER, oldFilter, filter));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getSortBy() {
		return sortBy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSortBy(Object newSortBy) {
		Object oldSortBy = sortBy;
		sortBy = newSortBy;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.QUERY_TYPE__SORT_BY, oldSortBy, sortBy));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getFeatureVersion() {
		return featureVersion;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFeatureVersion(String newFeatureVersion) {
		String oldFeatureVersion = featureVersion;
		featureVersion = newFeatureVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.QUERY_TYPE__FEATURE_VERSION, oldFeatureVersion, featureVersion));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getHandle() {
		return handle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHandle(String newHandle) {
		String oldHandle = handle;
		handle = newHandle;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.QUERY_TYPE__HANDLE, oldHandle, handle));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSrsName() {
		return srsName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSrsName(String newSrsName) {
		String oldSrsName = srsName;
		srsName = newSrsName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.QUERY_TYPE__SRS_NAME, oldSrsName, srsName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List getTypeName() {
		return typeName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTypeName(List newTypeName) {
		List oldTypeName = typeName;
		typeName = newTypeName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.QUERY_TYPE__TYPE_NAME, oldTypeName, typeName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case WFSPackage.QUERY_TYPE__GROUP:
					return ((InternalEList)getGroup()).basicRemove(otherEnd, msgs);
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
			case WFSPackage.QUERY_TYPE__GROUP:
				return getGroup();
			case WFSPackage.QUERY_TYPE__PROPERTY_NAME:
				return getPropertyName();
			case WFSPackage.QUERY_TYPE__FUNCTION:
				return getFunction();
			case WFSPackage.QUERY_TYPE__FILTER:
				return getFilter();
			case WFSPackage.QUERY_TYPE__SORT_BY:
				return getSortBy();
			case WFSPackage.QUERY_TYPE__FEATURE_VERSION:
				return getFeatureVersion();
			case WFSPackage.QUERY_TYPE__HANDLE:
				return getHandle();
			case WFSPackage.QUERY_TYPE__SRS_NAME:
				return getSrsName();
			case WFSPackage.QUERY_TYPE__TYPE_NAME:
				return getTypeName();
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
			case WFSPackage.QUERY_TYPE__GROUP:
				getGroup().clear();
				getGroup().addAll((Collection)newValue);
				return;
			case WFSPackage.QUERY_TYPE__PROPERTY_NAME:
				getPropertyName().clear();
				getPropertyName().addAll((Collection)newValue);
				return;
			case WFSPackage.QUERY_TYPE__FUNCTION:
				getFunction().clear();
				getFunction().addAll((Collection)newValue);
				return;
			case WFSPackage.QUERY_TYPE__FILTER:
				setFilter((Object)newValue);
				return;
			case WFSPackage.QUERY_TYPE__SORT_BY:
				setSortBy((Object)newValue);
				return;
			case WFSPackage.QUERY_TYPE__FEATURE_VERSION:
				setFeatureVersion((String)newValue);
				return;
			case WFSPackage.QUERY_TYPE__HANDLE:
				setHandle((String)newValue);
				return;
			case WFSPackage.QUERY_TYPE__SRS_NAME:
				setSrsName((String)newValue);
				return;
			case WFSPackage.QUERY_TYPE__TYPE_NAME:
				setTypeName((List)newValue);
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
			case WFSPackage.QUERY_TYPE__GROUP:
				getGroup().clear();
				return;
			case WFSPackage.QUERY_TYPE__PROPERTY_NAME:
				getPropertyName().clear();
				return;
			case WFSPackage.QUERY_TYPE__FUNCTION:
				getFunction().clear();
				return;
			case WFSPackage.QUERY_TYPE__FILTER:
				setFilter(FILTER_EDEFAULT);
				return;
			case WFSPackage.QUERY_TYPE__SORT_BY:
				setSortBy(SORT_BY_EDEFAULT);
				return;
			case WFSPackage.QUERY_TYPE__FEATURE_VERSION:
				setFeatureVersion(FEATURE_VERSION_EDEFAULT);
				return;
			case WFSPackage.QUERY_TYPE__HANDLE:
				setHandle(HANDLE_EDEFAULT);
				return;
			case WFSPackage.QUERY_TYPE__SRS_NAME:
				setSrsName(SRS_NAME_EDEFAULT);
				return;
			case WFSPackage.QUERY_TYPE__TYPE_NAME:
				setTypeName(TYPE_NAME_EDEFAULT);
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
			case WFSPackage.QUERY_TYPE__GROUP:
				return group != null && !group.isEmpty();
			case WFSPackage.QUERY_TYPE__PROPERTY_NAME:
				return !getPropertyName().isEmpty();
			case WFSPackage.QUERY_TYPE__FUNCTION:
				return !getFunction().isEmpty();
			case WFSPackage.QUERY_TYPE__FILTER:
				return FILTER_EDEFAULT == null ? filter != null : !FILTER_EDEFAULT.equals(filter);
			case WFSPackage.QUERY_TYPE__SORT_BY:
				return SORT_BY_EDEFAULT == null ? sortBy != null : !SORT_BY_EDEFAULT.equals(sortBy);
			case WFSPackage.QUERY_TYPE__FEATURE_VERSION:
				return FEATURE_VERSION_EDEFAULT == null ? featureVersion != null : !FEATURE_VERSION_EDEFAULT.equals(featureVersion);
			case WFSPackage.QUERY_TYPE__HANDLE:
				return HANDLE_EDEFAULT == null ? handle != null : !HANDLE_EDEFAULT.equals(handle);
			case WFSPackage.QUERY_TYPE__SRS_NAME:
				return SRS_NAME_EDEFAULT == null ? srsName != null : !SRS_NAME_EDEFAULT.equals(srsName);
			case WFSPackage.QUERY_TYPE__TYPE_NAME:
				return TYPE_NAME_EDEFAULT == null ? typeName != null : !TYPE_NAME_EDEFAULT.equals(typeName);
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
		result.append(" (group: ");
		result.append(group);
		result.append(", filter: ");
		result.append(filter);
		result.append(", sortBy: ");
		result.append(sortBy);
		result.append(", featureVersion: ");
		result.append(featureVersion);
		result.append(", handle: ");
		result.append(handle);
		result.append(", srsName: ");
		result.append(srsName);
		result.append(", typeName: ");
		result.append(typeName);
		result.append(')');
		return result.toString();
	}

} //QueryTypeImpl
