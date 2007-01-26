/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows.v1_0_0.impl;

import java.util.Collection;

import net.opengis.ows.v1_0_0.CodeType;
import net.opengis.ows.v1_0_0.OWSPackage;
import net.opengis.ows.v1_0_0.ServiceIdentificationType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Service Identification Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.ServiceIdentificationTypeImpl#getServiceType <em>Service Type</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.ServiceIdentificationTypeImpl#getServiceTypeVersion <em>Service Type Version</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.ServiceIdentificationTypeImpl#getFees <em>Fees</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.ServiceIdentificationTypeImpl#getAccessConstraints <em>Access Constraints</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ServiceIdentificationTypeImpl extends DescriptionTypeImpl implements ServiceIdentificationType {
	/**
	 * The cached value of the '{@link #getServiceType() <em>Service Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getServiceType()
	 * @generated
	 * @ordered
	 */
	protected CodeType serviceType = null;

	/**
	 * The cached value of the '{@link #getServiceTypeVersion() <em>Service Type Version</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getServiceTypeVersion()
	 * @generated
	 * @ordered
	 */
	protected EList serviceTypeVersion = null;

	/**
	 * The default value of the '{@link #getFees() <em>Fees</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFees()
	 * @generated
	 * @ordered
	 */
	protected static final String FEES_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFees() <em>Fees</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFees()
	 * @generated
	 * @ordered
	 */
	protected String fees = FEES_EDEFAULT;

	/**
	 * The cached value of the '{@link #getAccessConstraints() <em>Access Constraints</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAccessConstraints()
	 * @generated
	 * @ordered
	 */
	protected EList accessConstraints = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ServiceIdentificationTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return OWSPackage.eINSTANCE.getServiceIdentificationType();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CodeType getServiceType() {
		return serviceType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetServiceType(CodeType newServiceType, NotificationChain msgs) {
		CodeType oldServiceType = serviceType;
		serviceType = newServiceType;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OWSPackage.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE, oldServiceType, newServiceType);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setServiceType(CodeType newServiceType) {
		if (newServiceType != serviceType) {
			NotificationChain msgs = null;
			if (serviceType != null)
				msgs = ((InternalEObject)serviceType).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OWSPackage.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE, null, msgs);
			if (newServiceType != null)
				msgs = ((InternalEObject)newServiceType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OWSPackage.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE, null, msgs);
			msgs = basicSetServiceType(newServiceType, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OWSPackage.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE, newServiceType, newServiceType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getServiceTypeVersion() {
		if (serviceTypeVersion == null) {
			serviceTypeVersion = new EDataTypeEList(String.class, this, OWSPackage.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE_VERSION);
		}
		return serviceTypeVersion;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getFees() {
		return fees;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFees(String newFees) {
		String oldFees = fees;
		fees = newFees;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OWSPackage.SERVICE_IDENTIFICATION_TYPE__FEES, oldFees, fees));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getAccessConstraints() {
		if (accessConstraints == null) {
			accessConstraints = new EDataTypeEList(String.class, this, OWSPackage.SERVICE_IDENTIFICATION_TYPE__ACCESS_CONSTRAINTS);
		}
		return accessConstraints;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case OWSPackage.SERVICE_IDENTIFICATION_TYPE__KEYWORDS:
					return ((InternalEList)getKeywords()).basicRemove(otherEnd, msgs);
				case OWSPackage.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE:
					return basicSetServiceType(null, msgs);
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
			case OWSPackage.SERVICE_IDENTIFICATION_TYPE__TITLE:
				return getTitle();
			case OWSPackage.SERVICE_IDENTIFICATION_TYPE__ABSTRACT:
				return getAbstract();
			case OWSPackage.SERVICE_IDENTIFICATION_TYPE__KEYWORDS:
				return getKeywords();
			case OWSPackage.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE:
				return getServiceType();
			case OWSPackage.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE_VERSION:
				return getServiceTypeVersion();
			case OWSPackage.SERVICE_IDENTIFICATION_TYPE__FEES:
				return getFees();
			case OWSPackage.SERVICE_IDENTIFICATION_TYPE__ACCESS_CONSTRAINTS:
				return getAccessConstraints();
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
			case OWSPackage.SERVICE_IDENTIFICATION_TYPE__TITLE:
				setTitle((String)newValue);
				return;
			case OWSPackage.SERVICE_IDENTIFICATION_TYPE__ABSTRACT:
				setAbstract((String)newValue);
				return;
			case OWSPackage.SERVICE_IDENTIFICATION_TYPE__KEYWORDS:
				getKeywords().clear();
				getKeywords().addAll((Collection)newValue);
				return;
			case OWSPackage.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE:
				setServiceType((CodeType)newValue);
				return;
			case OWSPackage.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE_VERSION:
				getServiceTypeVersion().clear();
				getServiceTypeVersion().addAll((Collection)newValue);
				return;
			case OWSPackage.SERVICE_IDENTIFICATION_TYPE__FEES:
				setFees((String)newValue);
				return;
			case OWSPackage.SERVICE_IDENTIFICATION_TYPE__ACCESS_CONSTRAINTS:
				getAccessConstraints().clear();
				getAccessConstraints().addAll((Collection)newValue);
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
			case OWSPackage.SERVICE_IDENTIFICATION_TYPE__TITLE:
				setTitle(TITLE_EDEFAULT);
				return;
			case OWSPackage.SERVICE_IDENTIFICATION_TYPE__ABSTRACT:
				setAbstract(ABSTRACT_EDEFAULT);
				return;
			case OWSPackage.SERVICE_IDENTIFICATION_TYPE__KEYWORDS:
				getKeywords().clear();
				return;
			case OWSPackage.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE:
				setServiceType((CodeType)null);
				return;
			case OWSPackage.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE_VERSION:
				getServiceTypeVersion().clear();
				return;
			case OWSPackage.SERVICE_IDENTIFICATION_TYPE__FEES:
				setFees(FEES_EDEFAULT);
				return;
			case OWSPackage.SERVICE_IDENTIFICATION_TYPE__ACCESS_CONSTRAINTS:
				getAccessConstraints().clear();
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
			case OWSPackage.SERVICE_IDENTIFICATION_TYPE__TITLE:
				return TITLE_EDEFAULT == null ? title != null : !TITLE_EDEFAULT.equals(title);
			case OWSPackage.SERVICE_IDENTIFICATION_TYPE__ABSTRACT:
				return ABSTRACT_EDEFAULT == null ? abstract_ != null : !ABSTRACT_EDEFAULT.equals(abstract_);
			case OWSPackage.SERVICE_IDENTIFICATION_TYPE__KEYWORDS:
				return keywords != null && !keywords.isEmpty();
			case OWSPackage.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE:
				return serviceType != null;
			case OWSPackage.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE_VERSION:
				return serviceTypeVersion != null && !serviceTypeVersion.isEmpty();
			case OWSPackage.SERVICE_IDENTIFICATION_TYPE__FEES:
				return FEES_EDEFAULT == null ? fees != null : !FEES_EDEFAULT.equals(fees);
			case OWSPackage.SERVICE_IDENTIFICATION_TYPE__ACCESS_CONSTRAINTS:
				return accessConstraints != null && !accessConstraints.isEmpty();
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
		result.append(" (serviceTypeVersion: ");
		result.append(serviceTypeVersion);
		result.append(", fees: ");
		result.append(fees);
		result.append(", accessConstraints: ");
		result.append(accessConstraints);
		result.append(')');
		return result.toString();
	}

} //ServiceIdentificationTypeImpl
