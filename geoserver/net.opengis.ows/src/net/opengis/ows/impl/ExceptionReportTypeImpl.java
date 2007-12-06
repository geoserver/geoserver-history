/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows.impl;

import net.opengis.ows.ExceptionReportType;
import net.opengis.ows.ExceptionType;
import net.opengis.ows.OwsPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import java.util.Collection;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Exception Report Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows.impl.ExceptionReportTypeImpl#getException <em>Exception</em>}</li>
 *   <li>{@link net.opengis.ows.impl.ExceptionReportTypeImpl#getLanguage <em>Language</em>}</li>
 *   <li>{@link net.opengis.ows.impl.ExceptionReportTypeImpl#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ExceptionReportTypeImpl extends EObjectImpl
    implements ExceptionReportType {
    /**
     * The default value of the '{@link #getLanguage() <em>Language</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLanguage()
     * @generated
     * @ordered
     */
    protected static final String LANGUAGE_EDEFAULT = null;

    /**
     * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVersion()
     * @generated
     * @ordered
     */
    protected static final String VERSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getException() <em>Exception</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getException()
     * @generated
     * @ordered
     */
    protected EList exception;

    /**
     * The cached value of the '{@link #getLanguage() <em>Language</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLanguage()
     * @generated
     * @ordered
     */
    protected String language = LANGUAGE_EDEFAULT;

    /**
     * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVersion()
     * @generated
     * @ordered
     */
    protected String version = VERSION_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ExceptionReportTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return OwsPackage.Literals.EXCEPTION_REPORT_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getException() {
        if (exception == null) {
            exception = new EObjectContainmentEList(ExceptionType.class, this,
                    OwsPackage.EXCEPTION_REPORT_TYPE__EXCEPTION);
        }

        return exception;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getLanguage() {
        return language;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLanguage(String newLanguage) {
        String oldLanguage = language;
        language = newLanguage;

        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET,
                    OwsPackage.EXCEPTION_REPORT_TYPE__LANGUAGE, oldLanguage,
                    language));
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getVersion() {
        return version;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setVersion(String newVersion) {
        String oldVersion = version;
        version = newVersion;

        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET,
                    OwsPackage.EXCEPTION_REPORT_TYPE__VERSION, oldVersion,
                    version));
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd,
        int featureID, NotificationChain msgs) {
        switch (featureID) {
        case OwsPackage.EXCEPTION_REPORT_TYPE__EXCEPTION:
            return ((InternalEList) getException()).basicRemove(otherEnd, msgs);
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
        case OwsPackage.EXCEPTION_REPORT_TYPE__EXCEPTION:
            return getException();

        case OwsPackage.EXCEPTION_REPORT_TYPE__LANGUAGE:
            return getLanguage();

        case OwsPackage.EXCEPTION_REPORT_TYPE__VERSION:
            return getVersion();
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
        case OwsPackage.EXCEPTION_REPORT_TYPE__EXCEPTION:
            getException().clear();
            getException().addAll((Collection) newValue);

            return;

        case OwsPackage.EXCEPTION_REPORT_TYPE__LANGUAGE:
            setLanguage((String) newValue);

            return;

        case OwsPackage.EXCEPTION_REPORT_TYPE__VERSION:
            setVersion((String) newValue);

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
        case OwsPackage.EXCEPTION_REPORT_TYPE__EXCEPTION:
            getException().clear();

            return;

        case OwsPackage.EXCEPTION_REPORT_TYPE__LANGUAGE:
            setLanguage(LANGUAGE_EDEFAULT);

            return;

        case OwsPackage.EXCEPTION_REPORT_TYPE__VERSION:
            setVersion(VERSION_EDEFAULT);

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
        case OwsPackage.EXCEPTION_REPORT_TYPE__EXCEPTION:
            return (exception != null) && !exception.isEmpty();

        case OwsPackage.EXCEPTION_REPORT_TYPE__LANGUAGE:
            return (LANGUAGE_EDEFAULT == null) ? (language != null)
                                               : (!LANGUAGE_EDEFAULT.equals(language));

        case OwsPackage.EXCEPTION_REPORT_TYPE__VERSION:
            return (VERSION_EDEFAULT == null) ? (version != null)
                                              : (!VERSION_EDEFAULT.equals(version));
        }

        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String toString() {
        if (eIsProxy()) {
            return super.toString();
        }

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (language: ");
        result.append(language);
        result.append(", version: ");
        result.append(version);
        result.append(')');

        return result.toString();
    }
} //ExceptionReportTypeImpl
