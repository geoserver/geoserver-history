package org.geotools.validation;

/**
 * Validation An interfaces used to define a type ov validation test that
 * is performed on Features.
 * <p>
 * Validation provides functionality for a ValidationProcessor to hand <br>
 * each Validation test a list of FeatureTypes that it is supposed to validate.
 * The validation test takes it upon itself to validate the data when it is called.
 * <p>
 * Capabilities:
 * <ul>
 * </li></li>
 * </ul>
 * Example Use:
 * <pre><code>
 * </code></pre>
 * 
 * @author bowens, Refractions Research, Inc.
 * @author $Author: sploreg $ (last modification)
 * @version $Id: Validation.java,v 1.1.2.4 2003/11/26 20:43:34 sploreg Exp $
 */
public interface Validation
{
	
	static final String ALL[] = null;	// test all featureTypes
	
	
	/**
	 * setName
	 * <p>
	 * Sets the name of the validation.
	 * </p>
	 * @param name the name of the validation.
	 */
	void 	setName( String name );
	
	/**
	 * getName
	 * <p>
	 * Returns the name of the validation.
	 * </p>
	 * @return the name of the validation.
	 */
	String 	getName();
	
	
	
	/**
	 * setDescription
	 * <p>
	 * Sets the description of the validation.
	 * </p>
	 * @param description of the validation
	 */
	void 	setDescription( String description );
	
	/**
	 * getDescription
	 * <p>
	 * Returns the description of the validation.
	 * </p>
	 * @return the description of the validation.
	 */
	String 	getDescription();
	
	
	
	/**
	 * getPriority
	 * <p>
	 * Returns thepriority (time cost) of the validation test
	 * </p>
	 * @return The priority (time cost) of the validation test
	 */
	int 	getPriority();
	
	
	
	/**
	 * setTypeNames
	 * <p>
	 * Sets the FeatureType names that this validation test is run against.
	 * </p>
	 * @param names FeatureType names that this validation test is run against.
	 */
	void 	setTypeNames(String[] names);
	
	/**
	 * getTypeNames
	 * <p>
	 * Returns the FeatureType names that this validation test is run against.
	 * </p>
	 * @return the FeatureType names that this validation test is run against.
	 */
	String[] getTypeNames();
}
