package org.geotools.validation;

import org.geotools.feature.FeatureType;
import org.geotools.feature.Feature;

/**
 * FeatureValidation PlugIns are used to check geospatial information
 * for internal consistency.
 * 
 * Each ValidationPlugIn is very specific in nature: it performs
 * one test extermly well.  This simplifies design decisions, documenation
 * configuration and use.
 * 
 * Following the lead the excelent design work in the JUnit testing framework
 * validation results are collected by a ValidationResults object. This interface for the
 * ValidationResults object also allows it to collect warning information.
 *   
 * The PlugIn is also required to supply some metadata to aid in its deployment,
 * scripting, logging and execution and error recovery.
 * 
 * @author jgarnett
 */
public interface FeatureValidation extends Validation
{
   
    /**
     * Used to check features against this validation rule.
     * 
     * @param features FeatureCollection to validated
     * @param type FeatureType used to aquire metadata such as schema information
     * @param results Used to coallate results information 
     * @return True if all the features pass this test.
     */
    public boolean validate( Feature feature, FeatureType type, ValidationResults results ) throws Exception;
    
}
