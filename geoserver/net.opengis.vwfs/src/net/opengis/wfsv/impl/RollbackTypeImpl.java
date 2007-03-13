/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfsv.impl;

import net.opengis.wfs.impl.NativeTypeImpl;

import net.opengis.wfsv.DifferenceQueryType;
import net.opengis.wfsv.RollbackType;
import net.opengis.wfsv.WfsvPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Rollback Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfsv.impl.RollbackTypeImpl#getDifferenceQuery <em>Difference Query</em>}</li>
 *   <li>{@link net.opengis.wfsv.impl.RollbackTypeImpl#getHandle <em>Handle</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RollbackTypeImpl extends NativeTypeImpl implements RollbackType {
    /**
     * The cached value of the '{@link #getDifferenceQuery() <em>Difference Query</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDifferenceQuery()
     * @generated
     * @ordered
     */
    protected DifferenceQueryType differenceQuery = null;

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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected RollbackTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return WfsvPackage.Literals.ROLLBACK_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DifferenceQueryType getDifferenceQuery() {
        return differenceQuery;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDifferenceQuery(DifferenceQueryType newDifferenceQuery, NotificationChain msgs) {
        DifferenceQueryType oldDifferenceQuery = differenceQuery;
        differenceQuery = newDifferenceQuery;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, WfsvPackage.ROLLBACK_TYPE__DIFFERENCE_QUERY, oldDifferenceQuery, newDifferenceQuery);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDifferenceQuery(DifferenceQueryType newDifferenceQuery) {
        if (newDifferenceQuery != differenceQuery) {
            NotificationChain msgs = null;
            if (differenceQuery != null)
                msgs = ((InternalEObject)differenceQuery).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - WfsvPackage.ROLLBACK_TYPE__DIFFERENCE_QUERY, null, msgs);
            if (newDifferenceQuery != null)
                msgs = ((InternalEObject)newDifferenceQuery).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - WfsvPackage.ROLLBACK_TYPE__DIFFERENCE_QUERY, null, msgs);
            msgs = basicSetDifferenceQuery(newDifferenceQuery, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsvPackage.ROLLBACK_TYPE__DIFFERENCE_QUERY, newDifferenceQuery, newDifferenceQuery));
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
            eNotify(new ENotificationImpl(this, Notification.SET, WfsvPackage.ROLLBACK_TYPE__HANDLE, oldHandle, handle));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case WfsvPackage.ROLLBACK_TYPE__DIFFERENCE_QUERY:
                return basicSetDifferenceQuery(null, msgs);
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
            case WfsvPackage.ROLLBACK_TYPE__DIFFERENCE_QUERY:
                return getDifferenceQuery();
            case WfsvPackage.ROLLBACK_TYPE__HANDLE:
                return getHandle();
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
            case WfsvPackage.ROLLBACK_TYPE__DIFFERENCE_QUERY:
                setDifferenceQuery((DifferenceQueryType)newValue);
                return;
            case WfsvPackage.ROLLBACK_TYPE__HANDLE:
                setHandle((String)newValue);
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
            case WfsvPackage.ROLLBACK_TYPE__DIFFERENCE_QUERY:
                setDifferenceQuery((DifferenceQueryType)null);
                return;
            case WfsvPackage.ROLLBACK_TYPE__HANDLE:
                setHandle(HANDLE_EDEFAULT);
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
            case WfsvPackage.ROLLBACK_TYPE__DIFFERENCE_QUERY:
                return differenceQuery != null;
            case WfsvPackage.ROLLBACK_TYPE__HANDLE:
                return HANDLE_EDEFAULT == null ? handle != null : !HANDLE_EDEFAULT.equals(handle);
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
        result.append(" (handle: ");
        result.append(handle);
        result.append(')');
        return result.toString();
    }

} //RollbackTypeImpl