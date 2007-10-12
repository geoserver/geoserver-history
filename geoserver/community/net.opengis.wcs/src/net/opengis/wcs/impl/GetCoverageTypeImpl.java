/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs.impl;

import net.opengis.wcs.DomainSubsetType;
import net.opengis.wcs.GetCoverageType;
import net.opengis.wcs.OutputType;
import net.opengis.wcs.RangeSubsetType;
import net.opengis.wcs.wcsPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Get Coverage Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs.impl.GetCoverageTypeImpl#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.GetCoverageTypeImpl#getDomainSubset <em>Domain Subset</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.GetCoverageTypeImpl#getRangeSubset <em>Range Subset</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.GetCoverageTypeImpl#getOutput <em>Output</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GetCoverageTypeImpl extends RequestBaseTypeImpl implements GetCoverageType {
    /**
     * The default value of the '{@link #getIdentifier() <em>Identifier</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIdentifier()
     * @generated
     * @ordered
     */
    protected static final Object IDENTIFIER_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getIdentifier() <em>Identifier</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIdentifier()
     * @generated
     * @ordered
     */
    protected Object identifier = IDENTIFIER_EDEFAULT;

    /**
     * The cached value of the '{@link #getDomainSubset() <em>Domain Subset</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDomainSubset()
     * @generated
     * @ordered
     */
    protected DomainSubsetType domainSubset;

    /**
     * The cached value of the '{@link #getRangeSubset() <em>Range Subset</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRangeSubset()
     * @generated
     * @ordered
     */
    protected RangeSubsetType rangeSubset;

    /**
     * The cached value of the '{@link #getOutput() <em>Output</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOutput()
     * @generated
     * @ordered
     */
    protected OutputType output;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected GetCoverageTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return wcsPackage.Literals.GET_COVERAGE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getIdentifier() {
        return identifier;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIdentifier(Object newIdentifier) {
        Object oldIdentifier = identifier;
        identifier = newIdentifier;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wcsPackage.GET_COVERAGE_TYPE__IDENTIFIER, oldIdentifier, identifier));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DomainSubsetType getDomainSubset() {
        return domainSubset;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDomainSubset(DomainSubsetType newDomainSubset, NotificationChain msgs) {
        DomainSubsetType oldDomainSubset = domainSubset;
        domainSubset = newDomainSubset;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, wcsPackage.GET_COVERAGE_TYPE__DOMAIN_SUBSET, oldDomainSubset, newDomainSubset);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDomainSubset(DomainSubsetType newDomainSubset) {
        if (newDomainSubset != domainSubset) {
            NotificationChain msgs = null;
            if (domainSubset != null)
                msgs = ((InternalEObject)domainSubset).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - wcsPackage.GET_COVERAGE_TYPE__DOMAIN_SUBSET, null, msgs);
            if (newDomainSubset != null)
                msgs = ((InternalEObject)newDomainSubset).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - wcsPackage.GET_COVERAGE_TYPE__DOMAIN_SUBSET, null, msgs);
            msgs = basicSetDomainSubset(newDomainSubset, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wcsPackage.GET_COVERAGE_TYPE__DOMAIN_SUBSET, newDomainSubset, newDomainSubset));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RangeSubsetType getRangeSubset() {
        return rangeSubset;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetRangeSubset(RangeSubsetType newRangeSubset, NotificationChain msgs) {
        RangeSubsetType oldRangeSubset = rangeSubset;
        rangeSubset = newRangeSubset;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, wcsPackage.GET_COVERAGE_TYPE__RANGE_SUBSET, oldRangeSubset, newRangeSubset);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRangeSubset(RangeSubsetType newRangeSubset) {
        if (newRangeSubset != rangeSubset) {
            NotificationChain msgs = null;
            if (rangeSubset != null)
                msgs = ((InternalEObject)rangeSubset).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - wcsPackage.GET_COVERAGE_TYPE__RANGE_SUBSET, null, msgs);
            if (newRangeSubset != null)
                msgs = ((InternalEObject)newRangeSubset).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - wcsPackage.GET_COVERAGE_TYPE__RANGE_SUBSET, null, msgs);
            msgs = basicSetRangeSubset(newRangeSubset, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wcsPackage.GET_COVERAGE_TYPE__RANGE_SUBSET, newRangeSubset, newRangeSubset));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OutputType getOutput() {
        return output;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetOutput(OutputType newOutput, NotificationChain msgs) {
        OutputType oldOutput = output;
        output = newOutput;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, wcsPackage.GET_COVERAGE_TYPE__OUTPUT, oldOutput, newOutput);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOutput(OutputType newOutput) {
        if (newOutput != output) {
            NotificationChain msgs = null;
            if (output != null)
                msgs = ((InternalEObject)output).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - wcsPackage.GET_COVERAGE_TYPE__OUTPUT, null, msgs);
            if (newOutput != null)
                msgs = ((InternalEObject)newOutput).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - wcsPackage.GET_COVERAGE_TYPE__OUTPUT, null, msgs);
            msgs = basicSetOutput(newOutput, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wcsPackage.GET_COVERAGE_TYPE__OUTPUT, newOutput, newOutput));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case wcsPackage.GET_COVERAGE_TYPE__DOMAIN_SUBSET:
                return basicSetDomainSubset(null, msgs);
            case wcsPackage.GET_COVERAGE_TYPE__RANGE_SUBSET:
                return basicSetRangeSubset(null, msgs);
            case wcsPackage.GET_COVERAGE_TYPE__OUTPUT:
                return basicSetOutput(null, msgs);
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
            case wcsPackage.GET_COVERAGE_TYPE__IDENTIFIER:
                return getIdentifier();
            case wcsPackage.GET_COVERAGE_TYPE__DOMAIN_SUBSET:
                return getDomainSubset();
            case wcsPackage.GET_COVERAGE_TYPE__RANGE_SUBSET:
                return getRangeSubset();
            case wcsPackage.GET_COVERAGE_TYPE__OUTPUT:
                return getOutput();
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
            case wcsPackage.GET_COVERAGE_TYPE__IDENTIFIER:
                setIdentifier(newValue);
                return;
            case wcsPackage.GET_COVERAGE_TYPE__DOMAIN_SUBSET:
                setDomainSubset((DomainSubsetType)newValue);
                return;
            case wcsPackage.GET_COVERAGE_TYPE__RANGE_SUBSET:
                setRangeSubset((RangeSubsetType)newValue);
                return;
            case wcsPackage.GET_COVERAGE_TYPE__OUTPUT:
                setOutput((OutputType)newValue);
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
            case wcsPackage.GET_COVERAGE_TYPE__IDENTIFIER:
                setIdentifier(IDENTIFIER_EDEFAULT);
                return;
            case wcsPackage.GET_COVERAGE_TYPE__DOMAIN_SUBSET:
                setDomainSubset((DomainSubsetType)null);
                return;
            case wcsPackage.GET_COVERAGE_TYPE__RANGE_SUBSET:
                setRangeSubset((RangeSubsetType)null);
                return;
            case wcsPackage.GET_COVERAGE_TYPE__OUTPUT:
                setOutput((OutputType)null);
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
            case wcsPackage.GET_COVERAGE_TYPE__IDENTIFIER:
                return IDENTIFIER_EDEFAULT == null ? identifier != null : !IDENTIFIER_EDEFAULT.equals(identifier);
            case wcsPackage.GET_COVERAGE_TYPE__DOMAIN_SUBSET:
                return domainSubset != null;
            case wcsPackage.GET_COVERAGE_TYPE__RANGE_SUBSET:
                return rangeSubset != null;
            case wcsPackage.GET_COVERAGE_TYPE__OUTPUT:
                return output != null;
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
        result.append(" (identifier: ");
        result.append(identifier);
        result.append(')');
        return result.toString();
    }

} //GetCoverageTypeImpl
