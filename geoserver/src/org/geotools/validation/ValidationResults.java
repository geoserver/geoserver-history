/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geotools.validation;

import org.geotools.feature.Feature;


/**
 * Collates results of validation operations.
 *
 * @author jgarnett
 */
public interface ValidationResults {
    /**
     * Called to configure ValidationResults according to the provided
     * FeatureValidation
     *
     * @param validation Provided FeatureValidation
     */
    public void setValidation(Validation validation);

    /**
     * Returns a validation error on against the provided feature, An optional
     * error message may be provided.  The validating web feature server will:
     * 
     * <ul>
     * <li>
     * Collate all errors for transaction result
     * </li>
     * <li>
     * Log all errors
     * </li>
     * <li>
     * Transaction may be canceled or have partial success depending on
     * transaction request
     * </li>
     * </ul>
     * 
     * Please note:<br>
     * The FeatureResults object has been provided with a Validation object
     * allowing it access to the user's name for the test, and the users
     * decription of the test. Use the message information only to provide
     * specific failure information.
     *
     * @param feature Feature found invalid
     * @param message Optional error message. Use a non null message to provide
     *        specific failure information.
     */
    public void error(Feature feature, String message);

    /**
     * Returns a validation warning against the provided feature. An optional
     * warning message may be provided  The validating web feature server
     * will:
     * 
     * <ul>
     * <li>
     * Collate all warnings for a comment in the transaction result
     * </li>
     * <li>
     * Log all warnings
     * </li>
     * </ul>
     * 
     * The FeatureResults object has been provided with a Validation object
     * allowing it access to the user's name for the test, and the users
     * decription of the test. Use the message information only to provide
     * specific failure information.
     *
     * @param feature Feature found to be in error
     * @param message Optional warning message. Use a non null message to
     *        provide specific warning information.
     */
    public void warning(Feature feature, String message);
}
