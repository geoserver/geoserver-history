/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.v1_1_0;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>All Some Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see net.opengis.wfs.v1_1_0.WFSPackage#getAllSomeType()
 * @model
 * @generated
 */
public final class AllSomeType extends AbstractEnumerator {
	/**
	 * The '<em><b>ALL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>ALL</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #ALL_LITERAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int ALL = 0;

	/**
	 * The '<em><b>SOME</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>SOME</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #SOME_LITERAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int SOME = 1;

	/**
	 * The '<em><b>ALL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #ALL
	 * @generated
	 * @ordered
	 */
	public static final AllSomeType ALL_LITERAL = new AllSomeType(ALL, "ALL");

	/**
	 * The '<em><b>SOME</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #SOME
	 * @generated
	 * @ordered
	 */
	public static final AllSomeType SOME_LITERAL = new AllSomeType(SOME, "SOME");

	/**
	 * An array of all the '<em><b>All Some Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final AllSomeType[] VALUES_ARRAY =
		new AllSomeType[] {
			ALL_LITERAL,
			SOME_LITERAL,
		};

	/**
	 * A public read-only list of all the '<em><b>All Some Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>All Some Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static AllSomeType get(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			AllSomeType result = VALUES_ARRAY[i];
			if (result.toString().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>All Some Type</b></em>' literal with the specified value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static AllSomeType get(int value) {
		switch (value) {
			case ALL: return ALL_LITERAL;
			case SOME: return SOME_LITERAL;
		}
		return null;	
	}

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private AllSomeType(int value, String name) {
		super(value, name);
	}

} //AllSomeType
