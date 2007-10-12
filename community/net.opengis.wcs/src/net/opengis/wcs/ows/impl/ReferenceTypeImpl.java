/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs.ows.impl;

import java.util.Collection;

import net.opengis.wcs.ows.ReferenceType;
import net.opengis.wcs.ows.owcsPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Reference Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs.ows.impl.ReferenceTypeImpl#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.ReferenceTypeImpl#getAbstract <em>Abstract</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.ReferenceTypeImpl#getFormat <em>Format</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.ReferenceTypeImpl#getMetadata <em>Metadata</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ReferenceTypeImpl extends AbstractReferenceBaseTypeImpl implements ReferenceType {
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
     * The default value of the '{@link #getAbstract() <em>Abstract</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAbstract()
     * @generated
     * @ordered
     */
    protected static final Object ABSTRACT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getAbstract() <em>Abstract</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAbstract()
     * @generated
     * @ordered
     */
    protected Object abstract_ = ABSTRACT_EDEFAULT;

    /**
     * The default value of the '{@link #getFormat() <em>Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFormat()
     * @generated
     * @ordered
     */
    protected static final Object FORMAT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFormat() <em>Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFormat()
     * @generated
     * @ordered
     */
    protected Object format = FORMAT_EDEFAULT;

    /**
     * The cached value of the '{@link #getMetadata() <em>Metadata</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMetadata()
     * @generated
     * @ordered
     */
    protected EList metadata;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ReferenceTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return owcsPackage.Literals.REFERENCE_TYPE;
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
            eNotify(new ENotificationImpl(this, Notification.SET, owcsPackage.REFERENCE_TYPE__IDENTIFIER, oldIdentifier, identifier));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getAbstract() {
        return abstract_;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAbstract(Object newAbstract) {
        Object oldAbstract = abstract_;
        abstract_ = newAbstract;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, owcsPackage.REFERENCE_TYPE__ABSTRACT, oldAbstract, abstract_));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getFormat() {
        return format;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFormat(Object newFormat) {
        Object oldFormat = format;
        format = newFormat;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, owcsPackage.REFERENCE_TYPE__FORMAT, oldFormat, format));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getMetadata() {
        if (metadata == null) {
            metadata = new EDataTypeEList(Object.class, this, owcsPackage.REFERENCE_TYPE__METADATA);
        }
        return metadata;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case owcsPackage.REFERENCE_TYPE__IDENTIFIER:
                return getIdentifier();
            case owcsPackage.REFERENCE_TYPE__ABSTRACT:
                return getAbstract();
            case owcsPackage.REFERENCE_TYPE__FORMAT:
                return getFormat();
            case owcsPackage.REFERENCE_TYPE__METADATA:
                return getMetadata();
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
            case owcsPackage.REFERENCE_TYPE__IDENTIFIER:
                setIdentifier(newValue);
                return;
            case owcsPackage.REFERENCE_TYPE__ABSTRACT:
                setAbstract(newValue);
                return;
            case owcsPackage.REFERENCE_TYPE__FORMAT:
                setFormat(newValue);
                return;
            case owcsPackage.REFERENCE_TYPE__METADATA:
                getMetadata().clear();
                getMetadata().addAll((Collection)newValue);
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
            case owcsPackage.REFERENCE_TYPE__IDENTIFIER:
                setIdentifier(IDENTIFIER_EDEFAULT);
                return;
            case owcsPackage.REFERENCE_TYPE__ABSTRACT:
                setAbstract(ABSTRACT_EDEFAULT);
                return;
            case owcsPackage.REFERENCE_TYPE__FORMAT:
                setFormat(FORMAT_EDEFAULT);
                return;
            case owcsPackage.REFERENCE_TYPE__METADATA:
                getMetadata().clear();
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
            case owcsPackage.REFERENCE_TYPE__IDENTIFIER:
                return IDENTIFIER_EDEFAULT == null ? identifier != null : !IDENTIFIER_EDEFAULT.equals(identifier);
            case owcsPackage.REFERENCE_TYPE__ABSTRACT:
                return ABSTRACT_EDEFAULT == null ? abstract_ != null : !ABSTRACT_EDEFAULT.equals(abstract_);
            case owcsPackage.REFERENCE_TYPE__FORMAT:
                return FORMAT_EDEFAULT == null ? format != null : !FORMAT_EDEFAULT.equals(format);
            case owcsPackage.REFERENCE_TYPE__METADATA:
                return metadata != null && !metadata.isEmpty();
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
        result.append(", abstract: ");
        result.append(abstract_);
        result.append(", format: ");
        result.append(format);
        result.append(", metadata: ");
        result.append(metadata);
        result.append(')');
        return result.toString();
    }

} //ReferenceTypeImpl
