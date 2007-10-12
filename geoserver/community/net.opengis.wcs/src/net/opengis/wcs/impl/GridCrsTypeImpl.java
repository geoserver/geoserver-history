/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs.impl;

import net.opengis.wcs.GridCrsType;
import net.opengis.wcs.wcsPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Grid Crs Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs.impl.GridCrsTypeImpl#getSrsName <em>Srs Name</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.GridCrsTypeImpl#getGridBaseCRS <em>Grid Base CRS</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.GridCrsTypeImpl#getGridType <em>Grid Type</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.GridCrsTypeImpl#getGridOrigin <em>Grid Origin</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.GridCrsTypeImpl#getGridOffsets <em>Grid Offsets</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.GridCrsTypeImpl#getGridCS <em>Grid CS</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.GridCrsTypeImpl#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GridCrsTypeImpl extends EObjectImpl implements GridCrsType {
    /**
     * The default value of the '{@link #getSrsName() <em>Srs Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSrsName()
     * @generated
     * @ordered
     */
    protected static final Object SRS_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSrsName() <em>Srs Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSrsName()
     * @generated
     * @ordered
     */
    protected Object srsName = SRS_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getGridBaseCRS() <em>Grid Base CRS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGridBaseCRS()
     * @generated
     * @ordered
     */
    protected static final String GRID_BASE_CRS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getGridBaseCRS() <em>Grid Base CRS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGridBaseCRS()
     * @generated
     * @ordered
     */
    protected String gridBaseCRS = GRID_BASE_CRS_EDEFAULT;

    /**
     * The default value of the '{@link #getGridType() <em>Grid Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGridType()
     * @generated
     * @ordered
     */
    protected static final String GRID_TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getGridType() <em>Grid Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGridType()
     * @generated
     * @ordered
     */
    protected String gridType = GRID_TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #getGridOrigin() <em>Grid Origin</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGridOrigin()
     * @generated
     * @ordered
     */
    protected static final Object GRID_ORIGIN_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getGridOrigin() <em>Grid Origin</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGridOrigin()
     * @generated
     * @ordered
     */
    protected Object gridOrigin = GRID_ORIGIN_EDEFAULT;

    /**
     * The default value of the '{@link #getGridOffsets() <em>Grid Offsets</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGridOffsets()
     * @generated
     * @ordered
     */
    protected static final Object GRID_OFFSETS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getGridOffsets() <em>Grid Offsets</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGridOffsets()
     * @generated
     * @ordered
     */
    protected Object gridOffsets = GRID_OFFSETS_EDEFAULT;

    /**
     * The default value of the '{@link #getGridCS() <em>Grid CS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGridCS()
     * @generated
     * @ordered
     */
    protected static final String GRID_CS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getGridCS() <em>Grid CS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGridCS()
     * @generated
     * @ordered
     */
    protected String gridCS = GRID_CS_EDEFAULT;

    /**
     * The default value of the '{@link #getId() <em>Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getId()
     * @generated
     * @ordered
     */
    protected static final Object ID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getId()
     * @generated
     * @ordered
     */
    protected Object id = ID_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected GridCrsTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return wcsPackage.Literals.GRID_CRS_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getSrsName() {
        return srsName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSrsName(Object newSrsName) {
        Object oldSrsName = srsName;
        srsName = newSrsName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wcsPackage.GRID_CRS_TYPE__SRS_NAME, oldSrsName, srsName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getGridBaseCRS() {
        return gridBaseCRS;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGridBaseCRS(String newGridBaseCRS) {
        String oldGridBaseCRS = gridBaseCRS;
        gridBaseCRS = newGridBaseCRS;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wcsPackage.GRID_CRS_TYPE__GRID_BASE_CRS, oldGridBaseCRS, gridBaseCRS));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getGridType() {
        return gridType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGridType(String newGridType) {
        String oldGridType = gridType;
        gridType = newGridType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wcsPackage.GRID_CRS_TYPE__GRID_TYPE, oldGridType, gridType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getGridOrigin() {
        return gridOrigin;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGridOrigin(Object newGridOrigin) {
        Object oldGridOrigin = gridOrigin;
        gridOrigin = newGridOrigin;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wcsPackage.GRID_CRS_TYPE__GRID_ORIGIN, oldGridOrigin, gridOrigin));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getGridOffsets() {
        return gridOffsets;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGridOffsets(Object newGridOffsets) {
        Object oldGridOffsets = gridOffsets;
        gridOffsets = newGridOffsets;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wcsPackage.GRID_CRS_TYPE__GRID_OFFSETS, oldGridOffsets, gridOffsets));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getGridCS() {
        return gridCS;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGridCS(String newGridCS) {
        String oldGridCS = gridCS;
        gridCS = newGridCS;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wcsPackage.GRID_CRS_TYPE__GRID_CS, oldGridCS, gridCS));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getId() {
        return id;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setId(Object newId) {
        Object oldId = id;
        id = newId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wcsPackage.GRID_CRS_TYPE__ID, oldId, id));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case wcsPackage.GRID_CRS_TYPE__SRS_NAME:
                return getSrsName();
            case wcsPackage.GRID_CRS_TYPE__GRID_BASE_CRS:
                return getGridBaseCRS();
            case wcsPackage.GRID_CRS_TYPE__GRID_TYPE:
                return getGridType();
            case wcsPackage.GRID_CRS_TYPE__GRID_ORIGIN:
                return getGridOrigin();
            case wcsPackage.GRID_CRS_TYPE__GRID_OFFSETS:
                return getGridOffsets();
            case wcsPackage.GRID_CRS_TYPE__GRID_CS:
                return getGridCS();
            case wcsPackage.GRID_CRS_TYPE__ID:
                return getId();
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
            case wcsPackage.GRID_CRS_TYPE__SRS_NAME:
                setSrsName(newValue);
                return;
            case wcsPackage.GRID_CRS_TYPE__GRID_BASE_CRS:
                setGridBaseCRS((String)newValue);
                return;
            case wcsPackage.GRID_CRS_TYPE__GRID_TYPE:
                setGridType((String)newValue);
                return;
            case wcsPackage.GRID_CRS_TYPE__GRID_ORIGIN:
                setGridOrigin(newValue);
                return;
            case wcsPackage.GRID_CRS_TYPE__GRID_OFFSETS:
                setGridOffsets(newValue);
                return;
            case wcsPackage.GRID_CRS_TYPE__GRID_CS:
                setGridCS((String)newValue);
                return;
            case wcsPackage.GRID_CRS_TYPE__ID:
                setId(newValue);
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
            case wcsPackage.GRID_CRS_TYPE__SRS_NAME:
                setSrsName(SRS_NAME_EDEFAULT);
                return;
            case wcsPackage.GRID_CRS_TYPE__GRID_BASE_CRS:
                setGridBaseCRS(GRID_BASE_CRS_EDEFAULT);
                return;
            case wcsPackage.GRID_CRS_TYPE__GRID_TYPE:
                setGridType(GRID_TYPE_EDEFAULT);
                return;
            case wcsPackage.GRID_CRS_TYPE__GRID_ORIGIN:
                setGridOrigin(GRID_ORIGIN_EDEFAULT);
                return;
            case wcsPackage.GRID_CRS_TYPE__GRID_OFFSETS:
                setGridOffsets(GRID_OFFSETS_EDEFAULT);
                return;
            case wcsPackage.GRID_CRS_TYPE__GRID_CS:
                setGridCS(GRID_CS_EDEFAULT);
                return;
            case wcsPackage.GRID_CRS_TYPE__ID:
                setId(ID_EDEFAULT);
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
            case wcsPackage.GRID_CRS_TYPE__SRS_NAME:
                return SRS_NAME_EDEFAULT == null ? srsName != null : !SRS_NAME_EDEFAULT.equals(srsName);
            case wcsPackage.GRID_CRS_TYPE__GRID_BASE_CRS:
                return GRID_BASE_CRS_EDEFAULT == null ? gridBaseCRS != null : !GRID_BASE_CRS_EDEFAULT.equals(gridBaseCRS);
            case wcsPackage.GRID_CRS_TYPE__GRID_TYPE:
                return GRID_TYPE_EDEFAULT == null ? gridType != null : !GRID_TYPE_EDEFAULT.equals(gridType);
            case wcsPackage.GRID_CRS_TYPE__GRID_ORIGIN:
                return GRID_ORIGIN_EDEFAULT == null ? gridOrigin != null : !GRID_ORIGIN_EDEFAULT.equals(gridOrigin);
            case wcsPackage.GRID_CRS_TYPE__GRID_OFFSETS:
                return GRID_OFFSETS_EDEFAULT == null ? gridOffsets != null : !GRID_OFFSETS_EDEFAULT.equals(gridOffsets);
            case wcsPackage.GRID_CRS_TYPE__GRID_CS:
                return GRID_CS_EDEFAULT == null ? gridCS != null : !GRID_CS_EDEFAULT.equals(gridCS);
            case wcsPackage.GRID_CRS_TYPE__ID:
                return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
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
        result.append(" (srsName: ");
        result.append(srsName);
        result.append(", gridBaseCRS: ");
        result.append(gridBaseCRS);
        result.append(", gridType: ");
        result.append(gridType);
        result.append(", gridOrigin: ");
        result.append(gridOrigin);
        result.append(", gridOffsets: ");
        result.append(gridOffsets);
        result.append(", gridCS: ");
        result.append(gridCS);
        result.append(", id: ");
        result.append(id);
        result.append(')');
        return result.toString();
    }

} //GridCrsTypeImpl
