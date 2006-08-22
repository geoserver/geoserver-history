/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.v1_1_0.impl;

import java.util.Collection;

import net.opengis.wfs.v1_1_0.PropertyType;
import net.opengis.wfs.v1_1_0.UpdateElementType;
import net.opengis.wfs.v1_1_0.WFSPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Update Element Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.UpdateElementTypeImpl#getProperty <em>Property</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.UpdateElementTypeImpl#getFilter <em>Filter</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.UpdateElementTypeImpl#getHandle <em>Handle</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.UpdateElementTypeImpl#getInputFormat <em>Input Format</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.UpdateElementTypeImpl#getSrsName <em>Srs Name</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.UpdateElementTypeImpl#getTypeName <em>Type Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UpdateElementTypeImpl extends EObjectImpl implements UpdateElementType {
	/**
	 * The cached value of the '{@link #getProperty() <em>Property</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProperty()
	 * @generated
	 * @ordered
	 */
	protected EList property = null;

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
	 * The default value of the '{@link #getInputFormat() <em>Input Format</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInputFormat()
	 * @generated
	 * @ordered
	 */
	protected static final String INPUT_FORMAT_EDEFAULT = "x-application/gml:3";

	/**
	 * The cached value of the '{@link #getInputFormat() <em>Input Format</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInputFormat()
	 * @generated
	 * @ordered
	 */
	protected String inputFormat = INPUT_FORMAT_EDEFAULT;

	/**
	 * This is true if the Input Format attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean inputFormatESet = false;

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
	protected static final Object TYPE_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTypeName() <em>Type Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypeName()
	 * @generated
	 * @ordered
	 */
	protected Object typeName = TYPE_NAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UpdateElementTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return WFSPackage.eINSTANCE.getUpdateElementType();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getProperty() {
		if (property == null) {
			property = new EObjectContainmentEList(PropertyType.class, this, WFSPackage.UPDATE_ELEMENT_TYPE__PROPERTY);
		}
		return property;
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
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.UPDATE_ELEMENT_TYPE__FILTER, oldFilter, filter));
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
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.UPDATE_ELEMENT_TYPE__HANDLE, oldHandle, handle));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getInputFormat() {
		return inputFormat;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInputFormat(String newInputFormat) {
		String oldInputFormat = inputFormat;
		inputFormat = newInputFormat;
		boolean oldInputFormatESet = inputFormatESet;
		inputFormatESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.UPDATE_ELEMENT_TYPE__INPUT_FORMAT, oldInputFormat, inputFormat, !oldInputFormatESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetInputFormat() {
		String oldInputFormat = inputFormat;
		boolean oldInputFormatESet = inputFormatESet;
		inputFormat = INPUT_FORMAT_EDEFAULT;
		inputFormatESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, WFSPackage.UPDATE_ELEMENT_TYPE__INPUT_FORMAT, oldInputFormat, INPUT_FORMAT_EDEFAULT, oldInputFormatESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetInputFormat() {
		return inputFormatESet;
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
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.UPDATE_ELEMENT_TYPE__SRS_NAME, oldSrsName, srsName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getTypeName() {
		return typeName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTypeName(Object newTypeName) {
		Object oldTypeName = typeName;
		typeName = newTypeName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.UPDATE_ELEMENT_TYPE__TYPE_NAME, oldTypeName, typeName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case WFSPackage.UPDATE_ELEMENT_TYPE__PROPERTY:
					return ((InternalEList)getProperty()).basicRemove(otherEnd, msgs);
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
			case WFSPackage.UPDATE_ELEMENT_TYPE__PROPERTY:
				return getProperty();
			case WFSPackage.UPDATE_ELEMENT_TYPE__FILTER:
				return getFilter();
			case WFSPackage.UPDATE_ELEMENT_TYPE__HANDLE:
				return getHandle();
			case WFSPackage.UPDATE_ELEMENT_TYPE__INPUT_FORMAT:
				return getInputFormat();
			case WFSPackage.UPDATE_ELEMENT_TYPE__SRS_NAME:
				return getSrsName();
			case WFSPackage.UPDATE_ELEMENT_TYPE__TYPE_NAME:
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
			case WFSPackage.UPDATE_ELEMENT_TYPE__PROPERTY:
				getProperty().clear();
				getProperty().addAll((Collection)newValue);
				return;
			case WFSPackage.UPDATE_ELEMENT_TYPE__FILTER:
				setFilter((Object)newValue);
				return;
			case WFSPackage.UPDATE_ELEMENT_TYPE__HANDLE:
				setHandle((String)newValue);
				return;
			case WFSPackage.UPDATE_ELEMENT_TYPE__INPUT_FORMAT:
				setInputFormat((String)newValue);
				return;
			case WFSPackage.UPDATE_ELEMENT_TYPE__SRS_NAME:
				setSrsName((String)newValue);
				return;
			case WFSPackage.UPDATE_ELEMENT_TYPE__TYPE_NAME:
				setTypeName((Object)newValue);
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
			case WFSPackage.UPDATE_ELEMENT_TYPE__PROPERTY:
				getProperty().clear();
				return;
			case WFSPackage.UPDATE_ELEMENT_TYPE__FILTER:
				setFilter(FILTER_EDEFAULT);
				return;
			case WFSPackage.UPDATE_ELEMENT_TYPE__HANDLE:
				setHandle(HANDLE_EDEFAULT);
				return;
			case WFSPackage.UPDATE_ELEMENT_TYPE__INPUT_FORMAT:
				unsetInputFormat();
				return;
			case WFSPackage.UPDATE_ELEMENT_TYPE__SRS_NAME:
				setSrsName(SRS_NAME_EDEFAULT);
				return;
			case WFSPackage.UPDATE_ELEMENT_TYPE__TYPE_NAME:
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
			case WFSPackage.UPDATE_ELEMENT_TYPE__PROPERTY:
				return property != null && !property.isEmpty();
			case WFSPackage.UPDATE_ELEMENT_TYPE__FILTER:
				return FILTER_EDEFAULT == null ? filter != null : !FILTER_EDEFAULT.equals(filter);
			case WFSPackage.UPDATE_ELEMENT_TYPE__HANDLE:
				return HANDLE_EDEFAULT == null ? handle != null : !HANDLE_EDEFAULT.equals(handle);
			case WFSPackage.UPDATE_ELEMENT_TYPE__INPUT_FORMAT:
				return isSetInputFormat();
			case WFSPackage.UPDATE_ELEMENT_TYPE__SRS_NAME:
				return SRS_NAME_EDEFAULT == null ? srsName != null : !SRS_NAME_EDEFAULT.equals(srsName);
			case WFSPackage.UPDATE_ELEMENT_TYPE__TYPE_NAME:
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
		result.append(" (filter: ");
		result.append(filter);
		result.append(", handle: ");
		result.append(handle);
		result.append(", inputFormat: ");
		if (inputFormatESet) result.append(inputFormat); else result.append("<unset>");
		result.append(", srsName: ");
		result.append(srsName);
		result.append(", typeName: ");
		result.append(typeName);
		result.append(')');
		return result.toString();
	}

} //UpdateElementTypeImpl
