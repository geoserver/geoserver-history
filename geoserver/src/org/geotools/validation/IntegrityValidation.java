package org.geotools.validation;

import java.util.Map;

import com.vividsolutions.jts.geom.Envelope;
/**
 * IntegrityValidation PlugIns are used to check geospatial information
 * for integrity.
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
public interface IntegrityValidation extends Validation
{

    /**
     * Used to check features against this validation rule.
     * 
     * @param layers Allows access to FeatureType and Colleaction by "name"     
     * @param results Used to coallate results information
     * @param the bounding box that encloses the unvalidated data
     * @return True if all the features pass this test.
     */
    public boolean validate( Map layers, Envelope envelope, ValidationResults results ) throws Exception;
    
}
