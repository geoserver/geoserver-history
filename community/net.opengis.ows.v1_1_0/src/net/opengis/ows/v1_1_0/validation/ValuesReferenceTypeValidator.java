/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows.v1_1_0.validation;


/**
 * A sample validator interface for {@link net.opengis.ows.v1_1_0.ValuesReferenceType}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface ValuesReferenceTypeValidator {
    boolean validate();

    boolean validateValue(String value);
    boolean validateReference(String value);
}
