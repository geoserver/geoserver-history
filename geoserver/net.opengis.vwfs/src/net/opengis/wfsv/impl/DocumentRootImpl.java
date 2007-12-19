/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfsv.impl;

import net.opengis.wfsv.DifferenceQueryType;
import net.opengis.wfsv.DocumentRoot;
import net.opengis.wfsv.GetDiffType;
import net.opengis.wfsv.GetLogType;
import net.opengis.wfsv.RollbackType;
import net.opengis.wfsv.VersionedDeleteElementType;
import net.opengis.wfsv.VersionedUpdateElementType;
import net.opengis.wfsv.WfsvPackage;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfsv.impl.DocumentRootImpl#getMixed <em>Mixed</em>}</li>
 *   <li>{@link net.opengis.wfsv.impl.DocumentRootImpl#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link net.opengis.wfsv.impl.DocumentRootImpl#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link net.opengis.wfsv.impl.DocumentRootImpl#getDifferenceQuery <em>Difference Query</em>}</li>
 *   <li>{@link net.opengis.wfsv.impl.DocumentRootImpl#getGetDiff <em>Get Diff</em>}</li>
 *   <li>{@link net.opengis.wfsv.impl.DocumentRootImpl#getGetLog <em>Get Log</em>}</li>
 *   <li>{@link net.opengis.wfsv.impl.DocumentRootImpl#getRollback <em>Rollback</em>}</li>
 *   <li>{@link net.opengis.wfsv.impl.DocumentRootImpl#getVersionedDelete <em>Versioned Delete</em>}</li>
 *   <li>{@link net.opengis.wfsv.impl.DocumentRootImpl#getVersionedUpdate <em>Versioned Update</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DocumentRootImpl extends EObjectImpl implements DocumentRoot {
    /**
     * The cached value of the '{@link #getMixed() <em>Mixed</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMixed()
     * @generated
     * @ordered
     */
    protected FeatureMap mixed;

    /**
     * The cached value of the '{@link #getXMLNSPrefixMap() <em>XMLNS Prefix Map</em>}' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getXMLNSPrefixMap()
     * @generated
     * @ordered
     */
    protected EMap xMLNSPrefixMap;

    /**
     * The cached value of the '{@link #getXSISchemaLocation() <em>XSI Schema Location</em>}' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getXSISchemaLocation()
     * @generated
     * @ordered
     */
    protected EMap xSISchemaLocation;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DocumentRootImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return WfsvPackage.Literals.DOCUMENT_ROOT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getMixed() {
        if (mixed == null) {
            mixed = new BasicFeatureMap(this, WfsvPackage.DOCUMENT_ROOT__MIXED);
        }

        return mixed;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EMap getXMLNSPrefixMap() {
        if (xMLNSPrefixMap == null) {
            xMLNSPrefixMap = new EcoreEMap(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY,
                    EStringToStringMapEntryImpl.class, this,
                    WfsvPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
        }

        return xMLNSPrefixMap;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EMap getXSISchemaLocation() {
        if (xSISchemaLocation == null) {
            xSISchemaLocation = new EcoreEMap(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY,
                    EStringToStringMapEntryImpl.class, this,
                    WfsvPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
        }

        return xSISchemaLocation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DifferenceQueryType getDifferenceQuery() {
        return (DifferenceQueryType) getMixed()
                                         .get(WfsvPackage.Literals.DOCUMENT_ROOT__DIFFERENCE_QUERY,
            true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDifferenceQuery(DifferenceQueryType newDifferenceQuery,
        NotificationChain msgs) {
        return ((FeatureMap.Internal) getMixed()).basicAdd(WfsvPackage.Literals.DOCUMENT_ROOT__DIFFERENCE_QUERY,
            newDifferenceQuery, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDifferenceQuery(DifferenceQueryType newDifferenceQuery) {
        ((FeatureMap.Internal) getMixed()).set(WfsvPackage.Literals.DOCUMENT_ROOT__DIFFERENCE_QUERY,
            newDifferenceQuery);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GetDiffType getGetDiff() {
        return (GetDiffType) getMixed().get(WfsvPackage.Literals.DOCUMENT_ROOT__GET_DIFF, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetGetDiff(GetDiffType newGetDiff, NotificationChain msgs) {
        return ((FeatureMap.Internal) getMixed()).basicAdd(WfsvPackage.Literals.DOCUMENT_ROOT__GET_DIFF,
            newGetDiff, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGetDiff(GetDiffType newGetDiff) {
        ((FeatureMap.Internal) getMixed()).set(WfsvPackage.Literals.DOCUMENT_ROOT__GET_DIFF,
            newGetDiff);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GetLogType getGetLog() {
        return (GetLogType) getMixed().get(WfsvPackage.Literals.DOCUMENT_ROOT__GET_LOG, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetGetLog(GetLogType newGetLog, NotificationChain msgs) {
        return ((FeatureMap.Internal) getMixed()).basicAdd(WfsvPackage.Literals.DOCUMENT_ROOT__GET_LOG,
            newGetLog, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGetLog(GetLogType newGetLog) {
        ((FeatureMap.Internal) getMixed()).set(WfsvPackage.Literals.DOCUMENT_ROOT__GET_LOG,
            newGetLog);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RollbackType getRollback() {
        return (RollbackType) getMixed().get(WfsvPackage.Literals.DOCUMENT_ROOT__ROLLBACK, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetRollback(RollbackType newRollback, NotificationChain msgs) {
        return ((FeatureMap.Internal) getMixed()).basicAdd(WfsvPackage.Literals.DOCUMENT_ROOT__ROLLBACK,
            newRollback, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRollback(RollbackType newRollback) {
        ((FeatureMap.Internal) getMixed()).set(WfsvPackage.Literals.DOCUMENT_ROOT__ROLLBACK,
            newRollback);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public VersionedDeleteElementType getVersionedDelete() {
        return (VersionedDeleteElementType) getMixed()
                                                .get(WfsvPackage.Literals.DOCUMENT_ROOT__VERSIONED_DELETE,
            true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetVersionedDelete(
        VersionedDeleteElementType newVersionedDelete, NotificationChain msgs) {
        return ((FeatureMap.Internal) getMixed()).basicAdd(WfsvPackage.Literals.DOCUMENT_ROOT__VERSIONED_DELETE,
            newVersionedDelete, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setVersionedDelete(VersionedDeleteElementType newVersionedDelete) {
        ((FeatureMap.Internal) getMixed()).set(WfsvPackage.Literals.DOCUMENT_ROOT__VERSIONED_DELETE,
            newVersionedDelete);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public VersionedUpdateElementType getVersionedUpdate() {
        return (VersionedUpdateElementType) getMixed()
                                                .get(WfsvPackage.Literals.DOCUMENT_ROOT__VERSIONED_UPDATE,
            true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetVersionedUpdate(
        VersionedUpdateElementType newVersionedUpdate, NotificationChain msgs) {
        return ((FeatureMap.Internal) getMixed()).basicAdd(WfsvPackage.Literals.DOCUMENT_ROOT__VERSIONED_UPDATE,
            newVersionedUpdate, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setVersionedUpdate(VersionedUpdateElementType newVersionedUpdate) {
        ((FeatureMap.Internal) getMixed()).set(WfsvPackage.Literals.DOCUMENT_ROOT__VERSIONED_UPDATE,
            newVersionedUpdate);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID,
        NotificationChain msgs) {
        switch (featureID) {
        case WfsvPackage.DOCUMENT_ROOT__MIXED:
            return ((InternalEList) getMixed()).basicRemove(otherEnd, msgs);

        case WfsvPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
            return ((InternalEList) getXMLNSPrefixMap()).basicRemove(otherEnd, msgs);

        case WfsvPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
            return ((InternalEList) getXSISchemaLocation()).basicRemove(otherEnd, msgs);

        case WfsvPackage.DOCUMENT_ROOT__DIFFERENCE_QUERY:
            return basicSetDifferenceQuery(null, msgs);

        case WfsvPackage.DOCUMENT_ROOT__GET_DIFF:
            return basicSetGetDiff(null, msgs);

        case WfsvPackage.DOCUMENT_ROOT__GET_LOG:
            return basicSetGetLog(null, msgs);

        case WfsvPackage.DOCUMENT_ROOT__ROLLBACK:
            return basicSetRollback(null, msgs);

        case WfsvPackage.DOCUMENT_ROOT__VERSIONED_DELETE:
            return basicSetVersionedDelete(null, msgs);

        case WfsvPackage.DOCUMENT_ROOT__VERSIONED_UPDATE:
            return basicSetVersionedUpdate(null, msgs);
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
        case WfsvPackage.DOCUMENT_ROOT__MIXED:

            if (coreType) {
                return getMixed();
            }

            return ((FeatureMap.Internal) getMixed()).getWrapper();

        case WfsvPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:

            if (coreType) {
                return getXMLNSPrefixMap();
            } else {
                return getXMLNSPrefixMap().map();
            }

        case WfsvPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:

            if (coreType) {
                return getXSISchemaLocation();
            } else {
                return getXSISchemaLocation().map();
            }

        case WfsvPackage.DOCUMENT_ROOT__DIFFERENCE_QUERY:
            return getDifferenceQuery();

        case WfsvPackage.DOCUMENT_ROOT__GET_DIFF:
            return getGetDiff();

        case WfsvPackage.DOCUMENT_ROOT__GET_LOG:
            return getGetLog();

        case WfsvPackage.DOCUMENT_ROOT__ROLLBACK:
            return getRollback();

        case WfsvPackage.DOCUMENT_ROOT__VERSIONED_DELETE:
            return getVersionedDelete();

        case WfsvPackage.DOCUMENT_ROOT__VERSIONED_UPDATE:
            return getVersionedUpdate();
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
        case WfsvPackage.DOCUMENT_ROOT__MIXED:
            ((FeatureMap.Internal) getMixed()).set(newValue);

            return;

        case WfsvPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
            ((EStructuralFeature.Setting) getXMLNSPrefixMap()).set(newValue);

            return;

        case WfsvPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
            ((EStructuralFeature.Setting) getXSISchemaLocation()).set(newValue);

            return;

        case WfsvPackage.DOCUMENT_ROOT__DIFFERENCE_QUERY:
            setDifferenceQuery((DifferenceQueryType) newValue);

            return;

        case WfsvPackage.DOCUMENT_ROOT__GET_DIFF:
            setGetDiff((GetDiffType) newValue);

            return;

        case WfsvPackage.DOCUMENT_ROOT__GET_LOG:
            setGetLog((GetLogType) newValue);

            return;

        case WfsvPackage.DOCUMENT_ROOT__ROLLBACK:
            setRollback((RollbackType) newValue);

            return;

        case WfsvPackage.DOCUMENT_ROOT__VERSIONED_DELETE:
            setVersionedDelete((VersionedDeleteElementType) newValue);

            return;

        case WfsvPackage.DOCUMENT_ROOT__VERSIONED_UPDATE:
            setVersionedUpdate((VersionedUpdateElementType) newValue);

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
        case WfsvPackage.DOCUMENT_ROOT__MIXED:
            getMixed().clear();

            return;

        case WfsvPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
            getXMLNSPrefixMap().clear();

            return;

        case WfsvPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
            getXSISchemaLocation().clear();

            return;

        case WfsvPackage.DOCUMENT_ROOT__DIFFERENCE_QUERY:
            setDifferenceQuery((DifferenceQueryType) null);

            return;

        case WfsvPackage.DOCUMENT_ROOT__GET_DIFF:
            setGetDiff((GetDiffType) null);

            return;

        case WfsvPackage.DOCUMENT_ROOT__GET_LOG:
            setGetLog((GetLogType) null);

            return;

        case WfsvPackage.DOCUMENT_ROOT__ROLLBACK:
            setRollback((RollbackType) null);

            return;

        case WfsvPackage.DOCUMENT_ROOT__VERSIONED_DELETE:
            setVersionedDelete((VersionedDeleteElementType) null);

            return;

        case WfsvPackage.DOCUMENT_ROOT__VERSIONED_UPDATE:
            setVersionedUpdate((VersionedUpdateElementType) null);

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
        case WfsvPackage.DOCUMENT_ROOT__MIXED:
            return (mixed != null) && !mixed.isEmpty();

        case WfsvPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
            return (xMLNSPrefixMap != null) && !xMLNSPrefixMap.isEmpty();

        case WfsvPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
            return (xSISchemaLocation != null) && !xSISchemaLocation.isEmpty();

        case WfsvPackage.DOCUMENT_ROOT__DIFFERENCE_QUERY:
            return getDifferenceQuery() != null;

        case WfsvPackage.DOCUMENT_ROOT__GET_DIFF:
            return getGetDiff() != null;

        case WfsvPackage.DOCUMENT_ROOT__GET_LOG:
            return getGetLog() != null;

        case WfsvPackage.DOCUMENT_ROOT__ROLLBACK:
            return getRollback() != null;

        case WfsvPackage.DOCUMENT_ROOT__VERSIONED_DELETE:
            return getVersionedDelete() != null;

        case WfsvPackage.DOCUMENT_ROOT__VERSIONED_UPDATE:
            return getVersionedUpdate() != null;
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
        result.append(" (mixed: ");
        result.append(mixed);
        result.append(')');

        return result.toString();
    }
} //DocumentRootImpl
