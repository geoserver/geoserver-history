/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs.impl;

import java.util.Collection;

import net.opengis.wcs.GridCrsType;
import net.opengis.wcs.ImageCRSRefType;
import net.opengis.wcs.SpatialDomainType;
import net.opengis.wcs.wcsPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Spatial Domain Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs.impl.SpatialDomainTypeImpl#getBoundingBox <em>Bounding Box</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.SpatialDomainTypeImpl#getGridCRS <em>Grid CRS</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.SpatialDomainTypeImpl#getTransformation <em>Transformation</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.SpatialDomainTypeImpl#getImageCRS <em>Image CRS</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.SpatialDomainTypeImpl#getPolygon <em>Polygon</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SpatialDomainTypeImpl extends EObjectImpl implements SpatialDomainType {
    /**
     * The cached value of the '{@link #getBoundingBox() <em>Bounding Box</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBoundingBox()
     * @generated
     * @ordered
     */
    protected EList boundingBox;

    /**
     * The cached value of the '{@link #getGridCRS() <em>Grid CRS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGridCRS()
     * @generated
     * @ordered
     */
    protected GridCrsType gridCRS;

    /**
     * The default value of the '{@link #getTransformation() <em>Transformation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTransformation()
     * @generated
     * @ordered
     */
    protected static final Object TRANSFORMATION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTransformation() <em>Transformation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTransformation()
     * @generated
     * @ordered
     */
    protected Object transformation = TRANSFORMATION_EDEFAULT;

    /**
     * The cached value of the '{@link #getImageCRS() <em>Image CRS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getImageCRS()
     * @generated
     * @ordered
     */
    protected ImageCRSRefType imageCRS;

    /**
     * The cached value of the '{@link #getPolygon() <em>Polygon</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPolygon()
     * @generated
     * @ordered
     */
    protected EList polygon;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SpatialDomainTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return wcsPackage.Literals.SPATIAL_DOMAIN_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getBoundingBox() {
        if (boundingBox == null) {
            boundingBox = new EDataTypeEList(Object.class, this, wcsPackage.SPATIAL_DOMAIN_TYPE__BOUNDING_BOX);
        }
        return boundingBox;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GridCrsType getGridCRS() {
        return gridCRS;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetGridCRS(GridCrsType newGridCRS, NotificationChain msgs) {
        GridCrsType oldGridCRS = gridCRS;
        gridCRS = newGridCRS;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, wcsPackage.SPATIAL_DOMAIN_TYPE__GRID_CRS, oldGridCRS, newGridCRS);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGridCRS(GridCrsType newGridCRS) {
        if (newGridCRS != gridCRS) {
            NotificationChain msgs = null;
            if (gridCRS != null)
                msgs = ((InternalEObject)gridCRS).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - wcsPackage.SPATIAL_DOMAIN_TYPE__GRID_CRS, null, msgs);
            if (newGridCRS != null)
                msgs = ((InternalEObject)newGridCRS).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - wcsPackage.SPATIAL_DOMAIN_TYPE__GRID_CRS, null, msgs);
            msgs = basicSetGridCRS(newGridCRS, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wcsPackage.SPATIAL_DOMAIN_TYPE__GRID_CRS, newGridCRS, newGridCRS));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getTransformation() {
        return transformation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTransformation(Object newTransformation) {
        Object oldTransformation = transformation;
        transformation = newTransformation;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wcsPackage.SPATIAL_DOMAIN_TYPE__TRANSFORMATION, oldTransformation, transformation));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ImageCRSRefType getImageCRS() {
        return imageCRS;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetImageCRS(ImageCRSRefType newImageCRS, NotificationChain msgs) {
        ImageCRSRefType oldImageCRS = imageCRS;
        imageCRS = newImageCRS;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, wcsPackage.SPATIAL_DOMAIN_TYPE__IMAGE_CRS, oldImageCRS, newImageCRS);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setImageCRS(ImageCRSRefType newImageCRS) {
        if (newImageCRS != imageCRS) {
            NotificationChain msgs = null;
            if (imageCRS != null)
                msgs = ((InternalEObject)imageCRS).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - wcsPackage.SPATIAL_DOMAIN_TYPE__IMAGE_CRS, null, msgs);
            if (newImageCRS != null)
                msgs = ((InternalEObject)newImageCRS).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - wcsPackage.SPATIAL_DOMAIN_TYPE__IMAGE_CRS, null, msgs);
            msgs = basicSetImageCRS(newImageCRS, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wcsPackage.SPATIAL_DOMAIN_TYPE__IMAGE_CRS, newImageCRS, newImageCRS));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getPolygon() {
        if (polygon == null) {
            polygon = new EDataTypeEList(Object.class, this, wcsPackage.SPATIAL_DOMAIN_TYPE__POLYGON);
        }
        return polygon;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case wcsPackage.SPATIAL_DOMAIN_TYPE__GRID_CRS:
                return basicSetGridCRS(null, msgs);
            case wcsPackage.SPATIAL_DOMAIN_TYPE__IMAGE_CRS:
                return basicSetImageCRS(null, msgs);
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
            case wcsPackage.SPATIAL_DOMAIN_TYPE__BOUNDING_BOX:
                return getBoundingBox();
            case wcsPackage.SPATIAL_DOMAIN_TYPE__GRID_CRS:
                return getGridCRS();
            case wcsPackage.SPATIAL_DOMAIN_TYPE__TRANSFORMATION:
                return getTransformation();
            case wcsPackage.SPATIAL_DOMAIN_TYPE__IMAGE_CRS:
                return getImageCRS();
            case wcsPackage.SPATIAL_DOMAIN_TYPE__POLYGON:
                return getPolygon();
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
            case wcsPackage.SPATIAL_DOMAIN_TYPE__BOUNDING_BOX:
                getBoundingBox().clear();
                getBoundingBox().addAll((Collection)newValue);
                return;
            case wcsPackage.SPATIAL_DOMAIN_TYPE__GRID_CRS:
                setGridCRS((GridCrsType)newValue);
                return;
            case wcsPackage.SPATIAL_DOMAIN_TYPE__TRANSFORMATION:
                setTransformation(newValue);
                return;
            case wcsPackage.SPATIAL_DOMAIN_TYPE__IMAGE_CRS:
                setImageCRS((ImageCRSRefType)newValue);
                return;
            case wcsPackage.SPATIAL_DOMAIN_TYPE__POLYGON:
                getPolygon().clear();
                getPolygon().addAll((Collection)newValue);
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
            case wcsPackage.SPATIAL_DOMAIN_TYPE__BOUNDING_BOX:
                getBoundingBox().clear();
                return;
            case wcsPackage.SPATIAL_DOMAIN_TYPE__GRID_CRS:
                setGridCRS((GridCrsType)null);
                return;
            case wcsPackage.SPATIAL_DOMAIN_TYPE__TRANSFORMATION:
                setTransformation(TRANSFORMATION_EDEFAULT);
                return;
            case wcsPackage.SPATIAL_DOMAIN_TYPE__IMAGE_CRS:
                setImageCRS((ImageCRSRefType)null);
                return;
            case wcsPackage.SPATIAL_DOMAIN_TYPE__POLYGON:
                getPolygon().clear();
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
            case wcsPackage.SPATIAL_DOMAIN_TYPE__BOUNDING_BOX:
                return boundingBox != null && !boundingBox.isEmpty();
            case wcsPackage.SPATIAL_DOMAIN_TYPE__GRID_CRS:
                return gridCRS != null;
            case wcsPackage.SPATIAL_DOMAIN_TYPE__TRANSFORMATION:
                return TRANSFORMATION_EDEFAULT == null ? transformation != null : !TRANSFORMATION_EDEFAULT.equals(transformation);
            case wcsPackage.SPATIAL_DOMAIN_TYPE__IMAGE_CRS:
                return imageCRS != null;
            case wcsPackage.SPATIAL_DOMAIN_TYPE__POLYGON:
                return polygon != null && !polygon.isEmpty();
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
        result.append(", transformation: ");
        result.append(transformation);
        result.append(", polygon: ");
        result.append(polygon);
        result.append(')');
        return result.toString();
    }

} //SpatialDomainTypeImpl
