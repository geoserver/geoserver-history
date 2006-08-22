/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see net.opengis.wfs.WFSPackage
 * @generated
 */
public interface WFSFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	WFSFactory eINSTANCE = new net.opengis.wfs.impl.WFSFactoryImpl();

	/**
	 * Returns a new object of class '<em>Describe Feature Type Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Describe Feature Type Type</em>'.
	 * @generated
	 */
	DescribeFeatureTypeType createDescribeFeatureTypeType();

	/**
	 * Returns a new object of class '<em>Document Root</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Document Root</em>'.
	 * @generated
	 */
	DocumentRoot createDocumentRoot();

	/**
	 * Returns a new object of class '<em>Feature Collection Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Feature Collection Type</em>'.
	 * @generated
	 */
	FeatureCollectionType createFeatureCollectionType();

	/**
	 * Returns a new object of class '<em>Get Capabilities Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Get Capabilities Type</em>'.
	 * @generated
	 */
	GetCapabilitiesType createGetCapabilitiesType();

	/**
	 * Returns a new object of class '<em>Get Feature Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Get Feature Type</em>'.
	 * @generated
	 */
	GetFeatureType createGetFeatureType();

	/**
	 * Returns a new object of class '<em>Query Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Query Type</em>'.
	 * @generated
	 */
	QueryType createQueryType();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	WFSPackage getWFSPackage();

} //WFSFactory
