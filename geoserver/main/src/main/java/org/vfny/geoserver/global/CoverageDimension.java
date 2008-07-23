/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.util.Arrays;

import org.geoserver.catalog.CoverageDimensionInfo;
import org.geotools.util.NumberRange;


/**
 * Represents a CoverageDimension Attribute.
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 * @version $Id$
 * 
 * @deprecated use {@link CoverageDimensionInfo}.
 */
public class CoverageDimension extends GlobalLayerSupertype {
    ///**
    // *
    // */
    //private String name;
    //
    ///**
    // *
    // */
    //private String description;
    //
    ///**
    // *
    // */
    //private Double[] nullValues;
    //private NumberRange range;

    CoverageDimensionInfo cd;
    
    public CoverageDimension(CoverageDimensionInfo cd) {
        this.cd = cd;
    }

    /* (non-Javadoc)
     * @see org.vfny.geoserver.global.GlobalLayerSupertype#toDTO()
     */
    Object toDTO() {
        return null;
    }

    public void load(CoverageDimension other) {
        setDescription(other.getDescription());
        setName(other.getName());
        setNullValues(other.getNullValues());
        setRange(other.getRange());
    }
    
    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return cd.getDescription();
        //return description;
    }

    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        cd.setDescription(description);
        //this.description = description;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return cd.getName();
        //return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        cd.setName( name );
        //this.name = name;
    }

    /**
     * @return Returns the nullValues.
     */
    public Double[] getNullValues() {
        return cd.getNullValues().toArray( new Double[ cd.getNullValues().size() ]);
        //return nullValues;
    }

    /**
     * @param nullValues The nullValues to set.
     */
    public void setNullValues(Double[] nullValues) {
        cd.getNullValues().clear();
        cd.getNullValues().addAll( Arrays.asList(nullValues) );
        //this.nullValues = nullValues;
    }

    /**
     * @param range
     */
    public void setRange(NumberRange range) {
        cd.setRange(range);
        //this.range = range;
    }

    public NumberRange getRange() {
        return cd.getRange();
        //return range;
    }
}
