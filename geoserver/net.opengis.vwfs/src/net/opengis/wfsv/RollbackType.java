/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfsv;

import net.opengis.wfs.NativeType;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Rollback Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfsv.RollbackType#getDifferenceQuery <em>Difference Query</em>}</li>
 *   <li>{@link net.opengis.wfsv.RollbackType#getHandle <em>Handle</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfsv.WfsvPackage#getRollbackType()
 * @model extendedMetaData="name='RollbackType' kind='elementOnly'"
 * @generated
 */
public interface RollbackType extends NativeType {
    /**
     * Returns the value of the '<em><b>Difference Query</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                         The difference filter will be used to compute a diff to be applied
     *                         in order to perform the rollback. A rollback is conceptually just
     *                         a Transaction applied on the result of a back-diff between two
     *                         revisions.
     *                      
     * <!-- end-model-doc -->
     * @return the value of the '<em>Difference Query</em>' containment reference.
     * @see #setDifferenceQuery(DifferenceQueryType)
     * @see net.opengis.wfsv.WfsvPackage#getRollbackType_DifferenceQuery()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='DifferenceQuery' namespace='##targetNamespace'"
     * @generated
     */
    DifferenceQueryType getDifferenceQuery();

    /**
     * Sets the value of the '{@link net.opengis.wfsv.RollbackType#getDifferenceQuery <em>Difference Query</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Difference Query</em>' containment reference.
     * @see #getDifferenceQuery()
     * @generated
     */
    void setDifferenceQuery(DifferenceQueryType value);

    /**
     * Returns the value of the '<em><b>Handle</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                      The handle attribute allows a client application
     *                      to assign a client-generated request identifier
     *                      to an Insert action.  The handle is included to
     *                      facilitate error reporting.  If a Rollback action
     *                      in a Transaction request fails, then a Versioning WFS may
     *                      include the handle in an exception report to localize
     *                      the error.  If no handle is included of the offending
     *                      Rollback element then a WFS may employee other means of
     *                      localizing the error (e.g. line number).
     *                   
     * <!-- end-model-doc -->
     * @return the value of the '<em>Handle</em>' attribute.
     * @see #setHandle(String)
     * @see net.opengis.wfsv.WfsvPackage#getRollbackType_Handle()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='handle'"
     * @generated
     */
    String getHandle();

    /**
     * Sets the value of the '{@link net.opengis.wfsv.RollbackType#getHandle <em>Handle</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Handle</em>' attribute.
     * @see #getHandle()
     * @generated
     */
    void setHandle(String value);

} // RollbackType