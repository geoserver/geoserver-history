/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows.v1_0_0.impl;

import java.util.Collection;

import net.opengis.ows.v1_0_0.MetadataType;
import net.opengis.ows.v1_0_0.OWSPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Metadata Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.MetadataTypeImpl#getAbstractMetaDataGroup <em>Abstract Meta Data Group</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.MetadataTypeImpl#getAbstractMetaData <em>Abstract Meta Data</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.MetadataTypeImpl#getAbout <em>About</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MetadataTypeImpl extends EObjectImpl implements MetadataType {
	/**
	 * The cached value of the '{@link #getAbstractMetaDataGroup() <em>Abstract Meta Data Group</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAbstractMetaDataGroup()
	 * @generated
	 * @ordered
	 */
	protected FeatureMap abstractMetaDataGroup = null;

	/**
	 * The default value of the '{@link #getAbout() <em>About</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAbout()
	 * @generated
	 * @ordered
	 */
	protected static final String ABOUT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAbout() <em>About</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAbout()
	 * @generated
	 * @ordered
	 */
	protected String about = ABOUT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MetadataTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return OWSPackage.eINSTANCE.getMetadataType();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getAbstractMetaDataGroup() {
		if (abstractMetaDataGroup == null) {
			abstractMetaDataGroup = new BasicFeatureMap(this, OWSPackage.METADATA_TYPE__ABSTRACT_META_DATA_GROUP);
		}
		return abstractMetaDataGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getAbstractMetaData() {
		return (EObject)getAbstractMetaDataGroup().get(OWSPackage.eINSTANCE.getMetadataType_AbstractMetaData(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAbstractMetaData(EObject newAbstractMetaData, NotificationChain msgs) {
		return ((FeatureMap.Internal)getAbstractMetaDataGroup()).basicAdd(OWSPackage.eINSTANCE.getMetadataType_AbstractMetaData(), newAbstractMetaData, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getAbout() {
		return about;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAbout(String newAbout) {
		String oldAbout = about;
		about = newAbout;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OWSPackage.METADATA_TYPE__ABOUT, oldAbout, about));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case OWSPackage.METADATA_TYPE__ABSTRACT_META_DATA_GROUP:
					return ((InternalEList)getAbstractMetaDataGroup()).basicRemove(otherEnd, msgs);
				case OWSPackage.METADATA_TYPE__ABSTRACT_META_DATA:
					return basicSetAbstractMetaData(null, msgs);
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
			case OWSPackage.METADATA_TYPE__ABSTRACT_META_DATA_GROUP:
				return getAbstractMetaDataGroup();
			case OWSPackage.METADATA_TYPE__ABSTRACT_META_DATA:
				return getAbstractMetaData();
			case OWSPackage.METADATA_TYPE__ABOUT:
				return getAbout();
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
			case OWSPackage.METADATA_TYPE__ABSTRACT_META_DATA_GROUP:
				getAbstractMetaDataGroup().clear();
				getAbstractMetaDataGroup().addAll((Collection)newValue);
				return;
			case OWSPackage.METADATA_TYPE__ABOUT:
				setAbout((String)newValue);
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
			case OWSPackage.METADATA_TYPE__ABSTRACT_META_DATA_GROUP:
				getAbstractMetaDataGroup().clear();
				return;
			case OWSPackage.METADATA_TYPE__ABOUT:
				setAbout(ABOUT_EDEFAULT);
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
			case OWSPackage.METADATA_TYPE__ABSTRACT_META_DATA_GROUP:
				return abstractMetaDataGroup != null && !abstractMetaDataGroup.isEmpty();
			case OWSPackage.METADATA_TYPE__ABSTRACT_META_DATA:
				return getAbstractMetaData() != null;
			case OWSPackage.METADATA_TYPE__ABOUT:
				return ABOUT_EDEFAULT == null ? about != null : !ABOUT_EDEFAULT.equals(about);
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
		result.append(" (abstractMetaDataGroup: ");
		result.append(abstractMetaDataGroup);
		result.append(", about: ");
		result.append(about);
		result.append(')');
		return result.toString();
	}

} //MetadataTypeImpl
