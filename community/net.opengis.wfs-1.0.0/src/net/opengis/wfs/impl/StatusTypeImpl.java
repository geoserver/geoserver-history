/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import net.opengis.wfs.EmptyType;
import net.opengis.wfs.StatusType;
import net.opengis.wfs.WFSPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Status Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.impl.StatusTypeImpl#getSUCCESS <em>SUCCESS</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.StatusTypeImpl#getFAILED <em>FAILED</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.StatusTypeImpl#getPARTIAL <em>PARTIAL</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class StatusTypeImpl extends EObjectImpl implements StatusType {
	/**
	 * The cached value of the '{@link #getSUCCESS() <em>SUCCESS</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSUCCESS()
	 * @generated
	 * @ordered
	 */
	protected EmptyType sUCCESS = null;

	/**
	 * The cached value of the '{@link #getFAILED() <em>FAILED</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFAILED()
	 * @generated
	 * @ordered
	 */
	protected EmptyType fAILED = null;

	/**
	 * The cached value of the '{@link #getPARTIAL() <em>PARTIAL</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPARTIAL()
	 * @generated
	 * @ordered
	 */
	protected EmptyType pARTIAL = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected StatusTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return WFSPackage.eINSTANCE.getStatusType();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EmptyType getSUCCESS() {
		return sUCCESS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSUCCESS(EmptyType newSUCCESS, NotificationChain msgs) {
		EmptyType oldSUCCESS = sUCCESS;
		sUCCESS = newSUCCESS;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, WFSPackage.STATUS_TYPE__SUCCESS, oldSUCCESS, newSUCCESS);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSUCCESS(EmptyType newSUCCESS) {
		if (newSUCCESS != sUCCESS) {
			NotificationChain msgs = null;
			if (sUCCESS != null)
				msgs = ((InternalEObject)sUCCESS).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - WFSPackage.STATUS_TYPE__SUCCESS, null, msgs);
			if (newSUCCESS != null)
				msgs = ((InternalEObject)newSUCCESS).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - WFSPackage.STATUS_TYPE__SUCCESS, null, msgs);
			msgs = basicSetSUCCESS(newSUCCESS, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.STATUS_TYPE__SUCCESS, newSUCCESS, newSUCCESS));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EmptyType getFAILED() {
		return fAILED;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetFAILED(EmptyType newFAILED, NotificationChain msgs) {
		EmptyType oldFAILED = fAILED;
		fAILED = newFAILED;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, WFSPackage.STATUS_TYPE__FAILED, oldFAILED, newFAILED);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFAILED(EmptyType newFAILED) {
		if (newFAILED != fAILED) {
			NotificationChain msgs = null;
			if (fAILED != null)
				msgs = ((InternalEObject)fAILED).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - WFSPackage.STATUS_TYPE__FAILED, null, msgs);
			if (newFAILED != null)
				msgs = ((InternalEObject)newFAILED).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - WFSPackage.STATUS_TYPE__FAILED, null, msgs);
			msgs = basicSetFAILED(newFAILED, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.STATUS_TYPE__FAILED, newFAILED, newFAILED));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EmptyType getPARTIAL() {
		return pARTIAL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPARTIAL(EmptyType newPARTIAL, NotificationChain msgs) {
		EmptyType oldPARTIAL = pARTIAL;
		pARTIAL = newPARTIAL;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, WFSPackage.STATUS_TYPE__PARTIAL, oldPARTIAL, newPARTIAL);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPARTIAL(EmptyType newPARTIAL) {
		if (newPARTIAL != pARTIAL) {
			NotificationChain msgs = null;
			if (pARTIAL != null)
				msgs = ((InternalEObject)pARTIAL).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - WFSPackage.STATUS_TYPE__PARTIAL, null, msgs);
			if (newPARTIAL != null)
				msgs = ((InternalEObject)newPARTIAL).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - WFSPackage.STATUS_TYPE__PARTIAL, null, msgs);
			msgs = basicSetPARTIAL(newPARTIAL, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.STATUS_TYPE__PARTIAL, newPARTIAL, newPARTIAL));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case WFSPackage.STATUS_TYPE__SUCCESS:
					return basicSetSUCCESS(null, msgs);
				case WFSPackage.STATUS_TYPE__FAILED:
					return basicSetFAILED(null, msgs);
				case WFSPackage.STATUS_TYPE__PARTIAL:
					return basicSetPARTIAL(null, msgs);
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
			case WFSPackage.STATUS_TYPE__SUCCESS:
				return getSUCCESS();
			case WFSPackage.STATUS_TYPE__FAILED:
				return getFAILED();
			case WFSPackage.STATUS_TYPE__PARTIAL:
				return getPARTIAL();
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
			case WFSPackage.STATUS_TYPE__SUCCESS:
				setSUCCESS((EmptyType)newValue);
				return;
			case WFSPackage.STATUS_TYPE__FAILED:
				setFAILED((EmptyType)newValue);
				return;
			case WFSPackage.STATUS_TYPE__PARTIAL:
				setPARTIAL((EmptyType)newValue);
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
			case WFSPackage.STATUS_TYPE__SUCCESS:
				setSUCCESS((EmptyType)null);
				return;
			case WFSPackage.STATUS_TYPE__FAILED:
				setFAILED((EmptyType)null);
				return;
			case WFSPackage.STATUS_TYPE__PARTIAL:
				setPARTIAL((EmptyType)null);
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
			case WFSPackage.STATUS_TYPE__SUCCESS:
				return sUCCESS != null;
			case WFSPackage.STATUS_TYPE__FAILED:
				return fAILED != null;
			case WFSPackage.STATUS_TYPE__PARTIAL:
				return pARTIAL != null;
		}
		return eDynamicIsSet(eFeature);
	}

} //StatusTypeImpl
