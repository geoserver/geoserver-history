/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows.v1_1_0.validation;

import net.opengis.ows.v1_1_0.CodeType;

import org.eclipse.emf.common.util.EList;

/**
 * A sample validator interface for {@link net.opengis.ows.v1_1_0.ReferenceType}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface ReferenceTypeValidator {
    boolean validate();

    boolean validateIdentifier(CodeType value);
    boolean validateAbstract(EList value);
    boolean validateFormat(String value);
    boolean validateMetadata(EList value);
}
