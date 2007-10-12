/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs.impl;

import net.opengis.wcs.DomainSubsetType;
import net.opengis.wcs.TimeSequenceType;
import net.opengis.wcs.wcsPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Domain Subset Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs.impl.DomainSubsetTypeImpl#getBoundingBox <em>Bounding Box</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.DomainSubsetTypeImpl#getTemporalSubset <em>Temporal Subset</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DomainSubsetTypeImpl extends EObjectImpl implements DomainSubsetType {
    /**
     * The default value of the '{@link #getBoundingBox() <em>Bounding Box</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBoundingBox()
     * @generated
     * @ordered
     */
    protected static final Object BOUNDING_BOX_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getBoundingBox() <em>Bounding Box</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBoundingBox()
     * @generated
     * @ordered
     */
    protected Object boundingBox = BOUNDING_BOX_EDEFAULT;

    /**
     * The cached value of the '{@link #getTemporalSubset() <em>Temporal Subset</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTemporalSubset()
     * @generated
     * @ordered
     */
    protected TimeSequenceType temporalSubset;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DomainSubsetTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return wcsPackage.Literals.DOMAIN_SUBSET_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getBoundingBox() {
        return boundingBox;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBoundingBox(Object newBoundingBox) {
        Object oldBoundingBox = boundingBox;
        boundingBox = newBoundingBox;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wcsPackage.DOMAIN_SUBSET_TYPE__BOUNDING_BOX, oldBoundingBox, boundingBox));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeSequenceType getTemporalSubset() {
        return temporalSubset;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTemporalSubset(TimeSequenceType newTemporalSubset, NotificationChain msgs) {
        TimeSequenceType oldTemporalSubset = temporalSubset;
        temporalSubset = newTemporalSubset;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, wcsPackage.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET, oldTemporalSubset, newTemporalSubset);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTemporalSubset(TimeSequenceType newTemporalSubset) {
        if (newTemporalSubset != temporalSubset) {
            NotificationChain msgs = null;
            if (temporalSubset != null)
                msgs = ((InternalEObject)temporalSubset).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - wcsPackage.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET, null, msgs);
            if (newTemporalSubset != null)
                msgs = ((InternalEObject)newTemporalSubset).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - wcsPackage.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET, null, msgs);
            msgs = basicSetTemporalSubset(newTemporalSubset, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wcsPackage.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET, newTemporalSubset, newTemporalSubset));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case wcsPackage.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET:
                return basicSetTemporalSubset(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case wcsPackage.DOMAIN_SUBSET_TYPE__BOUNDING_BOX:
                return getBoundingBox();
            case wcsPackage.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET:
                return getTemporalSubset();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case wcsPackage.DOMAIN_SUBSET_TYPE__BOUNDING_BOX:
                setBoundingBox(newValue);
                return;
            case wcsPackage.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET:
                setTemporalSubset((TimeSequenceType)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void eUnset(int featureID) {
        switch (featureID) {
            case wcsPackage.DOMAIN_SUBSET_TYPE__BOUNDING_BOX:
                setBoundingBox(BOUNDING_BOX_EDEFAULT);
                return;
            case wcsPackage.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET:
                setTemporalSubset((TimeSequenceType)null);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case wcsPackage.DOMAIN_SUBSET_TYPE__BOUNDING_BOX:
                return BOUNDING_BOX_EDEFAULT == null ? boundingBox != null : !BOUNDING_BOX_EDEFAULT.equals(boundingBox);
            case wcsPackage.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET:
                return temporalSubset != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (boundingBox: ");
        result.append(boundingBox);
        result.append(')');
        return result.toString();
    }

} //DomainSubsetTypeImpl
