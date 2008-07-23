package org.geoserver.catalog.impl;

import java.util.ArrayList;
import java.util.List;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CoverageDimensionInfo;
import org.geotools.util.NumberRange;

public class CoverageDimensionImpl implements CoverageDimensionInfo {

    String id;

    String name;

    String description;

    NumberRange range;

    List nullValues = new ArrayList();
    
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

    public List getNullValues() {
        return nullValues;
    }

    public void setNullValues(List nullValues) {
        this.nullValues = nullValues;
    }
}
