/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.v1_1_0.impl;

import java.util.Collection;

import net.opengis.wfs.v1_1_0.IdentifierGenerationOptionType;
import net.opengis.wfs.v1_1_0.InsertElementType;
import net.opengis.wfs.v1_1_0.WFSPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Insert Element Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.InsertElementTypeImpl#getFeatureCollection <em>Feature Collection</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.InsertElementTypeImpl#getFeature <em>Feature</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.InsertElementTypeImpl#getHandle <em>Handle</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.InsertElementTypeImpl#getIdgen <em>Idgen</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.InsertElementTypeImpl#getInputFormat <em>Input Format</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.InsertElementTypeImpl#getSrsName <em>Srs Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InsertElementTypeImpl extends EObjectImpl implements InsertElementType {
	/**
	 * The default value of the '{@link #getFeatureCollection() <em>Feature Collection</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFeatureCollection()
	 * @generated
	 * @ordered
	 */
	protected static final Object FEATURE_COLLECTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFeatureCollection() <em>Feature Collection</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFeatureCollection()
	 * @generated
	 * @ordered
	 */
	protected Object featureCollection = FEATURE_COLLECTION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getFeature() <em>Feature</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFeature()
	 * @generated
	 * @ordered
	 */
	protected EList feature = null;

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
	 * The default value of the '{@link #getIdgen() <em>Idgen</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIdgen()
	 * @generated
	 * @ordered
	 */
	protected static final IdentifierGenerationOptionType IDGEN_EDEFAULT = IdentifierGenerationOptionType.GENERATE_NEW_LITERAL;

	/**
	 * The cached value of the '{@link #getIdgen() <em>Idgen</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIdgen()
	 * @generated
	 * @ordered
	 */
	protected IdentifierGenerationOptionType idgen = IDGEN_EDEFAULT;

	/**
	 * This is true if the Idgen attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean idgenESet = false;

	/**
	 * The default value of the '{@link #getInputFormat() <em>Input Format</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInputFormat()
	 * @generated
	 * @ordered
	 */
	protected static final String INPUT_FORMAT_EDEFAULT = "text/xml; subtype=gml/3.1.1";

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InsertElementTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return WFSPackage.eINSTANCE.getInsertElementType();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getFeatureCollection() {
		return featureCollection;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFeatureCollection(Object newFeatureCollection) {
		Object oldFeatureCollection = featureCollection;
		featureCollection = newFeatureCollection;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.INSERT_ELEMENT_TYPE__FEATURE_COLLECTION, oldFeatureCollection, featureCollection));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getFeature() {
		if (feature == null) {
			feature = new EDataTypeEList(Object.class, this, WFSPackage.INSERT_ELEMENT_TYPE__FEATURE);
		}
		return feature;
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
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.INSERT_ELEMENT_TYPE__HANDLE, oldHandle, handle));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IdentifierGenerationOptionType getIdgen() {
		return idgen;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIdgen(IdentifierGenerationOptionType newIdgen) {
		IdentifierGenerationOptionType oldIdgen = idgen;
		idgen = newIdgen == null ? IDGEN_EDEFAULT : newIdgen;
		boolean oldIdgenESet = idgenESet;
		idgenESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.INSERT_ELEMENT_TYPE__IDGEN, oldIdgen, idgen, !oldIdgenESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetIdgen() {
		IdentifierGenerationOptionType oldIdgen = idgen;
		boolean oldIdgenESet = idgenESet;
		idgen = IDGEN_EDEFAULT;
		idgenESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, WFSPackage.INSERT_ELEMENT_TYPE__IDGEN, oldIdgen, IDGEN_EDEFAULT, oldIdgenESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetIdgen() {
		return idgenESet;
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
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.INSERT_ELEMENT_TYPE__INPUT_FORMAT, oldInputFormat, inputFormat, !oldInputFormatESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, WFSPackage.INSERT_ELEMENT_TYPE__INPUT_FORMAT, oldInputFormat, INPUT_FORMAT_EDEFAULT, oldInputFormatESet));
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
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.INSERT_ELEMENT_TYPE__SRS_NAME, oldSrsName, srsName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case WFSPackage.INSERT_ELEMENT_TYPE__FEATURE_COLLECTION:
				return getFeatureCollection();
			case WFSPackage.INSERT_ELEMENT_TYPE__FEATURE:
				return getFeature();
			case WFSPackage.INSERT_ELEMENT_TYPE__HANDLE:
				return getHandle();
			case WFSPackage.INSERT_ELEMENT_TYPE__IDGEN:
				return getIdgen();
			case WFSPackage.INSERT_ELEMENT_TYPE__INPUT_FORMAT:
				return getInputFormat();
			case WFSPackage.INSERT_ELEMENT_TYPE__SRS_NAME:
				return getSrsName();
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
			case WFSPackage.INSERT_ELEMENT_TYPE__FEATURE_COLLECTION:
				setFeatureCollection((Object)newValue);
				return;
			case WFSPackage.INSERT_ELEMENT_TYPE__FEATURE:
				getFeature().clear();
				getFeature().addAll((Collection)newValue);
				return;
			case WFSPackage.INSERT_ELEMENT_TYPE__HANDLE:
				setHandle((String)newValue);
				return;
			case WFSPackage.INSERT_ELEMENT_TYPE__IDGEN:
				setIdgen((IdentifierGenerationOptionType)newValue);
				return;
			case WFSPackage.INSERT_ELEMENT_TYPE__INPUT_FORMAT:
				setInputFormat((String)newValue);
				return;
			case WFSPackage.INSERT_ELEMENT_TYPE__SRS_NAME:
				setSrsName((String)newValue);
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
			case WFSPackage.INSERT_ELEMENT_TYPE__FEATURE_COLLECTION:
				setFeatureCollection(FEATURE_COLLECTION_EDEFAULT);
				return;
			case WFSPackage.INSERT_ELEMENT_TYPE__FEATURE:
				getFeature().clear();
				return;
			case WFSPackage.INSERT_ELEMENT_TYPE__HANDLE:
				setHandle(HANDLE_EDEFAULT);
				return;
			case WFSPackage.INSERT_ELEMENT_TYPE__IDGEN:
				unsetIdgen();
				return;
			case WFSPackage.INSERT_ELEMENT_TYPE__INPUT_FORMAT:
				unsetInputFormat();
				return;
			case WFSPackage.INSERT_ELEMENT_TYPE__SRS_NAME:
				setSrsName(SRS_NAME_EDEFAULT);
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
			case WFSPackage.INSERT_ELEMENT_TYPE__FEATURE_COLLECTION:
				return FEATURE_COLLECTION_EDEFAULT == null ? featureCollection != null : !FEATURE_COLLECTION_EDEFAULT.equals(featureCollection);
			case WFSPackage.INSERT_ELEMENT_TYPE__FEATURE:
				return feature != null && !feature.isEmpty();
			case WFSPackage.INSERT_ELEMENT_TYPE__HANDLE:
				return HANDLE_EDEFAULT == null ? handle != null : !HANDLE_EDEFAULT.equals(handle);
			case WFSPackage.INSERT_ELEMENT_TYPE__IDGEN:
				return isSetIdgen();
			case WFSPackage.INSERT_ELEMENT_TYPE__INPUT_FORMAT:
				return isSetInputFormat();
			case WFSPackage.INSERT_ELEMENT_TYPE__SRS_NAME:
				return SRS_NAME_EDEFAULT == null ? srsName != null : !SRS_NAME_EDEFAULT.equals(srsName);
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
		result.append(" (featureCollection: ");
		result.append(featureCollection);
		result.append(", feature: ");
		result.append(feature);
		result.append(", handle: ");
		result.append(handle);
		result.append(", idgen: ");
		if (idgenESet) result.append(idgen); else result.append("<unset>");
		result.append(", inputFormat: ");
		if (inputFormatESet) result.append(inputFormat); else result.append("<unset>");
		result.append(", srsName: ");
		result.append(srsName);
		result.append(')');
		return result.toString();
	}

} //InsertElementTypeImpl
