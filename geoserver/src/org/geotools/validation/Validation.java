package org.geotools.validation;

/**
 * 
 * 
 * @author jgarnett
 */
public interface Validation 
{
	String 	setName( String name );
	String 	getName();
	String 	setDescription( String description );
	String 	getDescription();
	int 		getPriority();
	String[] getFeatureTypes();
}
