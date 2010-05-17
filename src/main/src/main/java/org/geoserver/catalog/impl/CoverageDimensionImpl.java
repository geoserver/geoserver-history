/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.catalog.impl;

import java.util.ArrayList;
import java.util.List;

import org.geoserver.catalog.CoverageDimensionInfo;
import org.geotools.util.NumberRange;

public class CoverageDimensionImpl implements CoverageDimensionInfo {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2993765933856195894L;

	String id;

    String name;

    String description;

    NumberRange range;

    List<Double> nullValues = new ArrayList();
    
    public CoverageDimensionImpl() {
    }

    public CoverageDimensionImpl(String id) {
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public NumberRange getRange() {
        return range;
    }

    public void setRange(NumberRange range) {
        this.range = range;
    }

    public List<Double> getNullValues() {
        return nullValues;
    }

    public void setNullValues(List<Double> nullValues) {
        this.nullValues = nullValues;
    }
}
