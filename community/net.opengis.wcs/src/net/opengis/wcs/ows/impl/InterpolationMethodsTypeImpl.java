/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs.ows.impl;

import java.util.Collection;

import net.opengis.wcs.ows.InterpolationMethodType;
import net.opengis.wcs.ows.InterpolationMethodsType;
import net.opengis.wcs.ows.owcsPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Interpolation Methods Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs.ows.impl.InterpolationMethodsTypeImpl#getDefaultMethod <em>Default Method</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.InterpolationMethodsTypeImpl#getOtherMethod <em>Other Method</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InterpolationMethodsTypeImpl extends EObjectImpl implements InterpolationMethodsType {
    /**
     * The cached value of the '{@link #getDefaultMethod() <em>Default Method</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDefaultMethod()
     * @generated
     * @ordered
     */
    protected InterpolationMethodType defaultMethod;

    /**
     * The cached value of the '{@link #getOtherMethod() <em>Other Method</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOtherMethod()
     * @generated
     * @ordered
     */
    protected EList otherMethod;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected InterpolationMethodsTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return owcsPackage.Literals.INTERPOLATION_METHODS_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public InterpolationMethodType getDefaultMethod() {
        return defaultMethod;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDefaultMethod(InterpolationMethodType newDefaultMethod, NotificationChain msgs) {
        InterpolationMethodType oldDefaultMethod = defaultMethod;
        defaultMethod = newDefaultMethod;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, owcsPackage.INTERPOLATION_METHODS_TYPE__DEFAULT_METHOD, oldDefaultMethod, newDefaultMethod);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDefaultMethod(InterpolationMethodType newDefaultMethod) {
        if (newDefaultMethod != defaultMethod) {
            NotificationChain msgs = null;
            if (defaultMethod != null)
                msgs = ((InternalEObject)defaultMethod).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - owcsPackage.INTERPOLATION_METHODS_TYPE__DEFAULT_METHOD, null, msgs);
            if (newDefaultMethod != null)
                msgs = ((InternalEObject)newDefaultMethod).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - owcsPackage.INTERPOLATION_METHODS_TYPE__DEFAULT_METHOD, null, msgs);
            msgs = basicSetDefaultMethod(newDefaultMethod, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, owcsPackage.INTERPOLATION_METHODS_TYPE__DEFAULT_METHOD, newDefaultMethod, newDefaultMethod));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getOtherMethod() {
        if (otherMethod == null) {
            otherMethod = new EObjectContainmentEList(InterpolationMethodType.class, this, owcsPackage.INTERPOLATION_METHODS_TYPE__OTHER_METHOD);
        }
        return otherMethod;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case owcsPackage.INTERPOLATION_METHODS_TYPE__DEFAULT_METHOD:
                return basicSetDefaultMethod(null, msgs);
            case owcsPackage.INTERPOLATION_METHODS_TYPE__OTHER_METHOD:
                return ((InternalEList)getOtherMethod()).basicRemove(otherEnd, msgs);
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
            case owcsPackage.INTERPOLATION_METHODS_TYPE__DEFAULT_METHOD:
                return getDefaultMethod();
            case owcsPackage.INTERPOLATION_METHODS_TYPE__OTHER_METHOD:
                return getOtherMethod();
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
            case owcsPackage.INTERPOLATION_METHODS_TYPE__DEFAULT_METHOD:
                setDefaultMethod((InterpolationMethodType)newValue);
                return;
            case owcsPackage.INTERPOLATION_METHODS_TYPE__OTHER_METHOD:
                getOtherMethod().clear();
                getOtherMethod().addAll((Collection)newValue);
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
            case owcsPackage.INTERPOLATION_METHODS_TYPE__DEFAULT_METHOD:
                setDefaultMethod((InterpolationMethodType)null);
                return;
            case owcsPackage.INTERPOLATION_METHODS_TYPE__OTHER_METHOD:
                getOtherMethod().clear();
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
            case owcsPackage.INTERPOLATION_METHODS_TYPE__DEFAULT_METHOD:
                return defaultMethod != null;
            case owcsPackage.INTERPOLATION_METHODS_TYPE__OTHER_METHOD:
                return otherMethod != null && !otherMethod.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //InterpolationMethodsTypeImpl
