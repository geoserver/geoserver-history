/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows.v1_0_0.impl;

import java.util.Collection;

import net.opengis.ows.v1_0_0.CodeType;
import net.opengis.ows.v1_0_0.IdentificationType;
import net.opengis.ows.v1_0_0.MetadataType;
import net.opengis.ows.v1_0_0.OWSPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Identification Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.IdentificationTypeImpl#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.IdentificationTypeImpl#getBoundingBoxGroup <em>Bounding Box Group</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.IdentificationTypeImpl#getBoundingBox <em>Bounding Box</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.IdentificationTypeImpl#getOutputFormat <em>Output Format</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.IdentificationTypeImpl#getAvailableCRSGroup <em>Available CRS Group</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.IdentificationTypeImpl#getAvailableCRS <em>Available CRS</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.IdentificationTypeImpl#getMetadata <em>Metadata</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IdentificationTypeImpl extends DescriptionTypeImpl implements IdentificationType {
	/**
	 * The cached value of the '{@link #getIdentifier() <em>Identifier</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIdentifier()
	 * @generated
	 * @ordered
	 */
	protected CodeType identifier = null;

	/**
	 * The cached value of the '{@link #getBoundingBoxGroup() <em>Bounding Box Group</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBoundingBoxGroup()
	 * @generated
	 * @ordered
	 */
	protected FeatureMap boundingBoxGroup = null;

	/**
	 * The cached value of the '{@link #getOutputFormat() <em>Output Format</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutputFormat()
	 * @generated
	 * @ordered
	 */
	protected EList outputFormat = null;

	/**
	 * The cached value of the '{@link #getAvailableCRSGroup() <em>Available CRS Group</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAvailableCRSGroup()
	 * @generated
	 * @ordered
	 */
	protected FeatureMap availableCRSGroup = null;

	/**
	 * The cached value of the '{@link #getMetadata() <em>Metadata</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMetadata()
	 * @generated
	 * @ordered
	 */
	protected EList metadata = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IdentificationTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return OWSPackage.eINSTANCE.getIdentificationType();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CodeType getIdentifier() {
		return identifier;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetIdentifier(CodeType newIdentifier, NotificationChain msgs) {
		CodeType oldIdentifier = identifier;
		identifier = newIdentifier;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OWSPackage.IDENTIFICATION_TYPE__IDENTIFIER, oldIdentifier, newIdentifier);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIdentifier(CodeType newIdentifier) {
		if (newIdentifier != identifier) {
			NotificationChain msgs = null;
			if (identifier != null)
				msgs = ((InternalEObject)identifier).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OWSPackage.IDENTIFICATION_TYPE__IDENTIFIER, null, msgs);
			if (newIdentifier != null)
				msgs = ((InternalEObject)newIdentifier).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OWSPackage.IDENTIFICATION_TYPE__IDENTIFIER, null, msgs);
			msgs = basicSetIdentifier(newIdentifier, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OWSPackage.IDENTIFICATION_TYPE__IDENTIFIER, newIdentifier, newIdentifier));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getBoundingBoxGroup() {
		if (boundingBoxGroup == null) {
			boundingBoxGroup = new BasicFeatureMap(this, OWSPackage.IDENTIFICATION_TYPE__BOUNDING_BOX_GROUP);
		}
		return boundingBoxGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getBoundingBox() {
		return ((FeatureMap)getBoundingBoxGroup()).list(OWSPackage.eINSTANCE.getIdentificationType_BoundingBox());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getOutputFormat() {
		if (outputFormat == null) {
			outputFormat = new EDataTypeEList(String.class, this, OWSPackage.IDENTIFICATION_TYPE__OUTPUT_FORMAT);
		}
		return outputFormat;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getAvailableCRSGroup() {
		if (availableCRSGroup == null) {
			availableCRSGroup = new BasicFeatureMap(this, OWSPackage.IDENTIFICATION_TYPE__AVAILABLE_CRS_GROUP);
		}
		return availableCRSGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getAvailableCRS() {
		return ((FeatureMap)getAvailableCRSGroup()).list(OWSPackage.eINSTANCE.getIdentificationType_AvailableCRS());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getMetadata() {
		if (metadata == null) {
			metadata = new EObjectContainmentEList(MetadataType.class, this, OWSPackage.IDENTIFICATION_TYPE__METADATA);
		}
		return metadata;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case OWSPackage.IDENTIFICATION_TYPE__KEYWORDS:
					return ((InternalEList)getKeywords()).basicRemove(otherEnd, msgs);
				case OWSPackage.IDENTIFICATION_TYPE__IDENTIFIER:
					return basicSetIdentifier(null, msgs);
				case OWSPackage.IDENTIFICATION_TYPE__BOUNDING_BOX_GROUP:
					return ((InternalEList)getBoundingBoxGroup()).basicRemove(otherEnd, msgs);
				case OWSPackage.IDENTIFICATION_TYPE__BOUNDING_BOX:
					return ((InternalEList)getBoundingBox()).basicRemove(otherEnd, msgs);
				case OWSPackage.IDENTIFICATION_TYPE__AVAILABLE_CRS_GROUP:
					return ((InternalEList)getAvailableCRSGroup()).basicRemove(otherEnd, msgs);
				case OWSPackage.IDENTIFICATION_TYPE__METADATA:
					return ((InternalEList)getMetadata()).basicRemove(otherEnd, msgs);
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
			case OWSPackage.IDENTIFICATION_TYPE__TITLE:
				return getTitle();
			case OWSPackage.IDENTIFICATION_TYPE__ABSTRACT:
				return getAbstract();
			case OWSPackage.IDENTIFICATION_TYPE__KEYWORDS:
				return getKeywords();
			case OWSPackage.IDENTIFICATION_TYPE__IDENTIFIER:
				return getIdentifier();
			case OWSPackage.IDENTIFICATION_TYPE__BOUNDING_BOX_GROUP:
				return getBoundingBoxGroup();
			case OWSPackage.IDENTIFICATION_TYPE__BOUNDING_BOX:
				return getBoundingBox();
			case OWSPackage.IDENTIFICATION_TYPE__OUTPUT_FORMAT:
				return getOutputFormat();
			case OWSPackage.IDENTIFICATION_TYPE__AVAILABLE_CRS_GROUP:
				return getAvailableCRSGroup();
			case OWSPackage.IDENTIFICATION_TYPE__AVAILABLE_CRS:
				return getAvailableCRS();
			case OWSPackage.IDENTIFICATION_TYPE__METADATA:
				return getMetadata();
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
			case OWSPackage.IDENTIFICATION_TYPE__TITLE:
				setTitle((String)newValue);
				return;
			case OWSPackage.IDENTIFICATION_TYPE__ABSTRACT:
				setAbstract((String)newValue);
				return;
			case OWSPackage.IDENTIFICATION_TYPE__KEYWORDS:
				getKeywords().clear();
				getKeywords().addAll((Collection)newValue);
				return;
			case OWSPackage.IDENTIFICATION_TYPE__IDENTIFIER:
				setIdentifier((CodeType)newValue);
				return;
			case OWSPackage.IDENTIFICATION_TYPE__BOUNDING_BOX_GROUP:
				getBoundingBoxGroup().clear();
				getBoundingBoxGroup().addAll((Collection)newValue);
				return;
			case OWSPackage.IDENTIFICATION_TYPE__BOUNDING_BOX:
				getBoundingBox().clear();
				getBoundingBox().addAll((Collection)newValue);
				return;
			case OWSPackage.IDENTIFICATION_TYPE__OUTPUT_FORMAT:
				getOutputFormat().clear();
				getOutputFormat().addAll((Collection)newValue);
				return;
			case OWSPackage.IDENTIFICATION_TYPE__AVAILABLE_CRS_GROUP:
				getAvailableCRSGroup().clear();
				getAvailableCRSGroup().addAll((Collection)newValue);
				return;
			case OWSPackage.IDENTIFICATION_TYPE__AVAILABLE_CRS:
				getAvailableCRS().clear();
				getAvailableCRS().addAll((Collection)newValue);
				return;
			case OWSPackage.IDENTIFICATION_TYPE__METADATA:
				getMetadata().clear();
				getMetadata().addAll((Collection)newValue);
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
			case OWSPackage.IDENTIFICATION_TYPE__TITLE:
				setTitle(TITLE_EDEFAULT);
				return;
			case OWSPackage.IDENTIFICATION_TYPE__ABSTRACT:
				setAbstract(ABSTRACT_EDEFAULT);
				return;
			case OWSPackage.IDENTIFICATION_TYPE__KEYWORDS:
				getKeywords().clear();
				return;
			case OWSPackage.IDENTIFICATION_TYPE__IDENTIFIER:
				setIdentifier((CodeType)null);
				return;
			case OWSPackage.IDENTIFICATION_TYPE__BOUNDING_BOX_GROUP:
				getBoundingBoxGroup().clear();
				return;
			case OWSPackage.IDENTIFICATION_TYPE__BOUNDING_BOX:
				getBoundingBox().clear();
				return;
			case OWSPackage.IDENTIFICATION_TYPE__OUTPUT_FORMAT:
				getOutputFormat().clear();
				return;
			case OWSPackage.IDENTIFICATION_TYPE__AVAILABLE_CRS_GROUP:
				getAvailableCRSGroup().clear();
				return;
			case OWSPackage.IDENTIFICATION_TYPE__AVAILABLE_CRS:
				getAvailableCRS().clear();
				return;
			case OWSPackage.IDENTIFICATION_TYPE__METADATA:
				getMetadata().clear();
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
			case OWSPackage.IDENTIFICATION_TYPE__TITLE:
				return TITLE_EDEFAULT == null ? title != null : !TITLE_EDEFAULT.equals(title);
			case OWSPackage.IDENTIFICATION_TYPE__ABSTRACT:
				return ABSTRACT_EDEFAULT == null ? abstract_ != null : !ABSTRACT_EDEFAULT.equals(abstract_);
			case OWSPackage.IDENTIFICATION_TYPE__KEYWORDS:
				return keywords != null && !keywords.isEmpty();
			case OWSPackage.IDENTIFICATION_TYPE__IDENTIFIER:
				return identifier != null;
			case OWSPackage.IDENTIFICATION_TYPE__BOUNDING_BOX_GROUP:
				return boundingBoxGroup != null && !boundingBoxGroup.isEmpty();
			case OWSPackage.IDENTIFICATION_TYPE__BOUNDING_BOX:
				return !getBoundingBox().isEmpty();
			case OWSPackage.IDENTIFICATION_TYPE__OUTPUT_FORMAT:
				return outputFormat != null && !outputFormat.isEmpty();
			case OWSPackage.IDENTIFICATION_TYPE__AVAILABLE_CRS_GROUP:
				return availableCRSGroup != null && !availableCRSGroup.isEmpty();
			case OWSPackage.IDENTIFICATION_TYPE__AVAILABLE_CRS:
				return !getAvailableCRS().isEmpty();
			case OWSPackage.IDENTIFICATION_TYPE__METADATA:
				return metadata != null && !metadata.isEmpty();
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
		result.append(" (boundingBoxGroup: ");
		result.append(boundingBoxGroup);
		result.append(", outputFormat: ");
		result.append(outputFormat);
		result.append(", availableCRSGroup: ");
		result.append(availableCRSGroup);
		result.append(')');
		return result.toString();
	}

} //IdentificationTypeImpl
