/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import java.net.URI;

import java.util.Collection;
import java.util.List;

import javax.xml.namespace.QName;

import net.opengis.wfs.QueryType;
import net.opengis.wfs.WfsPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

import org.opengis.filter.Filter;

import org.opengis.filter.expression.Function;

import org.opengis.filter.expression.PropertyName;

import org.opengis.filter.sort.SortBy;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Query Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.impl.QueryTypeImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.QueryTypeImpl#getPropertyName <em>Property Name</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.QueryTypeImpl#getXlinkPropertyName <em>Xlink Property Name</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.QueryTypeImpl#getFunction <em>Function</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.QueryTypeImpl#getFilter <em>Filter</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.QueryTypeImpl#getSortBy <em>Sort By</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.QueryTypeImpl#getFeatureVersion <em>Feature Version</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.QueryTypeImpl#getHandle <em>Handle</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.QueryTypeImpl#getSrsName <em>Srs Name</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.QueryTypeImpl#getTypeName <em>Type Name</em>}</li>
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
	 * The default value of the '{@link #getPropertyName() <em>Property Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPropertyName()
	 * @generated
	 * @ordered
	 */
	protected static final String PROPERTY_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFunction() <em>Function</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFunction()
	 * @generated
	 * @ordered
	 */
	protected EList function = null;

	/**
	 * The default value of the '{@link #getFilter() <em>Filter</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFilter()
	 * @generated
	 * @ordered
	 */
	protected static final Filter FILTER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFilter() <em>Filter</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFilter()
	 * @generated
	 * @ordered
	 */
	protected Filter filter = FILTER_EDEFAULT;

	/**
	 * The default value of the '{@link #getSortBy() <em>Sort By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSortBy()
	 * @generated
	 * @ordered
	 */
	protected static final SortBy SORT_BY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSortBy() <em>Sort By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSortBy()
	 * @generated
	 * @ordered
	 */
	protected SortBy sortBy = SORT_BY_EDEFAULT;

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
	protected static final URI SRS_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSrsName() <em>Srs Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSrsName()
	 * @generated
	 * @ordered
	 */
	protected URI srsName = SRS_NAME_EDEFAULT;

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
		return WfsPackage.eINSTANCE.getQueryType();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getGroup() {
		if (group == null) {
			group = new BasicFeatureMap(this, WfsPackage.QUERY_TYPE__GROUP);
		}
		return group;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPropertyName() {
		return (String)getGroup().get(WfsPackage.eINSTANCE.getQueryType_PropertyName(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPropertyName(String newPropertyName) {
		((FeatureMap.Internal)getGroup()).set(WfsPackage.eINSTANCE.getQueryType_PropertyName(), newPropertyName);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getXlinkPropertyName() {
		return ((FeatureMap)getGroup()).list(WfsPackage.eINSTANCE.getQueryType_XlinkPropertyName());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getFunction() {
		if (function == null) {
			function = new EDataTypeUniqueEList(Function.class, this, WfsPackage.QUERY_TYPE__FUNCTION);
		}
		return function;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Filter getFilter() {
		return filter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFilter(Filter newFilter) {
		Filter oldFilter = filter;
		filter = newFilter;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.QUERY_TYPE__FILTER, oldFilter, filter));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SortBy getSortBy() {
		return sortBy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSortBy(SortBy newSortBy) {
		SortBy oldSortBy = sortBy;
		sortBy = newSortBy;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.QUERY_TYPE__SORT_BY, oldSortBy, sortBy));
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
			eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.QUERY_TYPE__FEATURE_VERSION, oldFeatureVersion, featureVersion));
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
			eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.QUERY_TYPE__HANDLE, oldHandle, handle));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public URI getSrsName() {
		return srsName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSrsName(URI newSrsName) {
		URI oldSrsName = srsName;
		srsName = newSrsName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.QUERY_TYPE__SRS_NAME, oldSrsName, srsName));
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
			eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.QUERY_TYPE__TYPE_NAME, oldTypeName, typeName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case WfsPackage.QUERY_TYPE__GROUP:
					return ((InternalEList)getGroup()).basicRemove(otherEnd, msgs);
				case WfsPackage.QUERY_TYPE__XLINK_PROPERTY_NAME:
					return ((InternalEList)getXlinkPropertyName()).basicRemove(otherEnd, msgs);
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
			case WfsPackage.QUERY_TYPE__GROUP:
				return getGroup();
			case WfsPackage.QUERY_TYPE__PROPERTY_NAME:
				return getPropertyName();
			case WfsPackage.QUERY_TYPE__XLINK_PROPERTY_NAME:
				return getXlinkPropertyName();
			case WfsPackage.QUERY_TYPE__FUNCTION:
				return getFunction();
			case WfsPackage.QUERY_TYPE__FILTER:
				return getFilter();
			case WfsPackage.QUERY_TYPE__SORT_BY:
				return getSortBy();
			case WfsPackage.QUERY_TYPE__FEATURE_VERSION:
				return getFeatureVersion();
			case WfsPackage.QUERY_TYPE__HANDLE:
				return getHandle();
			case WfsPackage.QUERY_TYPE__SRS_NAME:
				return getSrsName();
			case WfsPackage.QUERY_TYPE__TYPE_NAME:
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
			case WfsPackage.QUERY_TYPE__GROUP:
				getGroup().clear();
				getGroup().addAll((Collection)newValue);
				return;
			case WfsPackage.QUERY_TYPE__PROPERTY_NAME:
				setPropertyName((String)newValue);
				return;
			case WfsPackage.QUERY_TYPE__XLINK_PROPERTY_NAME:
				getXlinkPropertyName().clear();
				getXlinkPropertyName().addAll((Collection)newValue);
				return;
			case WfsPackage.QUERY_TYPE__FUNCTION:
				getFunction().clear();
				getFunction().addAll((Collection)newValue);
				return;
			case WfsPackage.QUERY_TYPE__FILTER:
				setFilter((Filter)newValue);
				return;
			case WfsPackage.QUERY_TYPE__SORT_BY:
				setSortBy((SortBy)newValue);
				return;
			case WfsPackage.QUERY_TYPE__FEATURE_VERSION:
				setFeatureVersion((String)newValue);
				return;
			case WfsPackage.QUERY_TYPE__HANDLE:
				setHandle((String)newValue);
				return;
			case WfsPackage.QUERY_TYPE__SRS_NAME:
				setSrsName((URI)newValue);
				return;
			case WfsPackage.QUERY_TYPE__TYPE_NAME:
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
			case WfsPackage.QUERY_TYPE__GROUP:
				getGroup().clear();
				return;
			case WfsPackage.QUERY_TYPE__PROPERTY_NAME:
				setPropertyName(PROPERTY_NAME_EDEFAULT);
				return;
			case WfsPackage.QUERY_TYPE__XLINK_PROPERTY_NAME:
				getXlinkPropertyName().clear();
				return;
			case WfsPackage.QUERY_TYPE__FUNCTION:
				getFunction().clear();
				return;
			case WfsPackage.QUERY_TYPE__FILTER:
				setFilter(FILTER_EDEFAULT);
				return;
			case WfsPackage.QUERY_TYPE__SORT_BY:
				setSortBy(SORT_BY_EDEFAULT);
				return;
			case WfsPackage.QUERY_TYPE__FEATURE_VERSION:
				setFeatureVersion(FEATURE_VERSION_EDEFAULT);
				return;
			case WfsPackage.QUERY_TYPE__HANDLE:
				setHandle(HANDLE_EDEFAULT);
				return;
			case WfsPackage.QUERY_TYPE__SRS_NAME:
				setSrsName(SRS_NAME_EDEFAULT);
				return;
			case WfsPackage.QUERY_TYPE__TYPE_NAME:
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
			case WfsPackage.QUERY_TYPE__GROUP:
				return group != null && !group.isEmpty();
			case WfsPackage.QUERY_TYPE__PROPERTY_NAME:
				return PROPERTY_NAME_EDEFAULT == null ? getPropertyName() != null : !PROPERTY_NAME_EDEFAULT.equals(getPropertyName());
			case WfsPackage.QUERY_TYPE__XLINK_PROPERTY_NAME:
				return !getXlinkPropertyName().isEmpty();
			case WfsPackage.QUERY_TYPE__FUNCTION:
				return function != null && !function.isEmpty();
			case WfsPackage.QUERY_TYPE__FILTER:
				return FILTER_EDEFAULT == null ? filter != null : !FILTER_EDEFAULT.equals(filter);
			case WfsPackage.QUERY_TYPE__SORT_BY:
				return SORT_BY_EDEFAULT == null ? sortBy != null : !SORT_BY_EDEFAULT.equals(sortBy);
			case WfsPackage.QUERY_TYPE__FEATURE_VERSION:
				return FEATURE_VERSION_EDEFAULT == null ? featureVersion != null : !FEATURE_VERSION_EDEFAULT.equals(featureVersion);
			case WfsPackage.QUERY_TYPE__HANDLE:
				return HANDLE_EDEFAULT == null ? handle != null : !HANDLE_EDEFAULT.equals(handle);
			case WfsPackage.QUERY_TYPE__SRS_NAME:
				return SRS_NAME_EDEFAULT == null ? srsName != null : !SRS_NAME_EDEFAULT.equals(srsName);
			case WfsPackage.QUERY_TYPE__TYPE_NAME:
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
		result.append(", function: ");
		result.append(function);
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
