/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.v1_1_0.impl;

import javax.xml.namespace.QName;

import java.util.Collection;

import net.opengis.wfs.v1_1_0.DescribeFeatureTypeType;
import net.opengis.wfs.v1_1_0.WFSPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

import org.eclipse.emf.ecore.util.EDataTypeEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Describe Feature Type Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.DescribeFeatureTypeTypeImpl#getTypeName <em>Type Name</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.DescribeFeatureTypeTypeImpl#getOutputFormat <em>Output Format</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DescribeFeatureTypeTypeImpl extends BaseRequestTypeImpl implements DescribeFeatureTypeType {
	/**
	 * The cached value of the '{@link #getTypeName() <em>Type Name</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypeName()
	 * @generated
	 * @ordered
	 */
	protected EList typeName = null;

	/**
	 * The default value of the '{@link #getOutputFormat() <em>Output Format</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutputFormat()
	 * @generated
	 * @ordered
	 */
	protected static final String OUTPUT_FORMAT_EDEFAULT = "text/xml; subtype=gml/3.1.1";

	/**
	 * The cached value of the '{@link #getOutputFormat() <em>Output Format</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutputFormat()
	 * @generated
	 * @ordered
	 */
	protected String outputFormat = OUTPUT_FORMAT_EDEFAULT;

	/**
	 * This is true if the Output Format attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean outputFormatESet = false;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DescribeFeatureTypeTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return WFSPackage.eINSTANCE.getDescribeFeatureTypeType();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getTypeName() {
		if (typeName == null) {
			typeName = new EDataTypeUniqueEList(QName.class, this, WFSPackage.DESCRIBE_FEATURE_TYPE_TYPE__TYPE_NAME);
		}
		return typeName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getOutputFormat() {
		return outputFormat;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOutputFormat(String newOutputFormat) {
		String oldOutputFormat = outputFormat;
		outputFormat = newOutputFormat;
		boolean oldOutputFormatESet = outputFormatESet;
		outputFormatESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.DESCRIBE_FEATURE_TYPE_TYPE__OUTPUT_FORMAT, oldOutputFormat, outputFormat, !oldOutputFormatESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetOutputFormat() {
		String oldOutputFormat = outputFormat;
		boolean oldOutputFormatESet = outputFormatESet;
		outputFormat = OUTPUT_FORMAT_EDEFAULT;
		outputFormatESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, WFSPackage.DESCRIBE_FEATURE_TYPE_TYPE__OUTPUT_FORMAT, oldOutputFormat, OUTPUT_FORMAT_EDEFAULT, oldOutputFormatESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetOutputFormat() {
		return outputFormatESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case WFSPackage.DESCRIBE_FEATURE_TYPE_TYPE__HANDLE:
				return getHandle();
			case WFSPackage.DESCRIBE_FEATURE_TYPE_TYPE__SERVICE:
				return getService();
			case WFSPackage.DESCRIBE_FEATURE_TYPE_TYPE__VERSION:
				return getVersion();
			case WFSPackage.DESCRIBE_FEATURE_TYPE_TYPE__TYPE_NAME:
				return getTypeName();
			case WFSPackage.DESCRIBE_FEATURE_TYPE_TYPE__OUTPUT_FORMAT:
				return getOutputFormat();
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
			case WFSPackage.DESCRIBE_FEATURE_TYPE_TYPE__HANDLE:
				setHandle((String)newValue);
				return;
			case WFSPackage.DESCRIBE_FEATURE_TYPE_TYPE__SERVICE:
				setService((String)newValue);
				return;
			case WFSPackage.DESCRIBE_FEATURE_TYPE_TYPE__VERSION:
				setVersion((String)newValue);
				return;
			case WFSPackage.DESCRIBE_FEATURE_TYPE_TYPE__TYPE_NAME:
				getTypeName().clear();
				getTypeName().addAll((Collection)newValue);
				return;
			case WFSPackage.DESCRIBE_FEATURE_TYPE_TYPE__OUTPUT_FORMAT:
				setOutputFormat((String)newValue);
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
			case WFSPackage.DESCRIBE_FEATURE_TYPE_TYPE__HANDLE:
				setHandle(HANDLE_EDEFAULT);
				return;
			case WFSPackage.DESCRIBE_FEATURE_TYPE_TYPE__SERVICE:
				unsetService();
				return;
			case WFSPackage.DESCRIBE_FEATURE_TYPE_TYPE__VERSION:
				unsetVersion();
				return;
			case WFSPackage.DESCRIBE_FEATURE_TYPE_TYPE__TYPE_NAME:
				getTypeName().clear();
				return;
			case WFSPackage.DESCRIBE_FEATURE_TYPE_TYPE__OUTPUT_FORMAT:
				unsetOutputFormat();
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
			case WFSPackage.DESCRIBE_FEATURE_TYPE_TYPE__HANDLE:
				return HANDLE_EDEFAULT == null ? handle != null : !HANDLE_EDEFAULT.equals(handle);
			case WFSPackage.DESCRIBE_FEATURE_TYPE_TYPE__SERVICE:
				return isSetService();
			case WFSPackage.DESCRIBE_FEATURE_TYPE_TYPE__VERSION:
				return isSetVersion();
			case WFSPackage.DESCRIBE_FEATURE_TYPE_TYPE__TYPE_NAME:
				return typeName != null && !typeName.isEmpty();
			case WFSPackage.DESCRIBE_FEATURE_TYPE_TYPE__OUTPUT_FORMAT:
				return isSetOutputFormat();
		}
		return eDynamicIsSet(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (typeName: ");
		result.append(typeName);
		result.append(", outputFormat: ");
		if (outputFormatESet) result.append(outputFormat); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //DescribeFeatureTypeTypeImpl
