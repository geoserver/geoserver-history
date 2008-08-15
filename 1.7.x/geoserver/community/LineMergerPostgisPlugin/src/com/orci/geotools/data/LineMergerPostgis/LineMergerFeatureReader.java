package com.orci.geotools.data.LineMergerPostgis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import org.geotools.data.FeatureReader;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureType;
import org.geotools.feature.IllegalAttributeException;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.operation.linemerge.LineMerger;

/**
 * This class is used to create OpenTMSDataStore objects
 * Copyright (c) 2005, Open Roads Consulting, Inc.
 */
public class LineMergerFeatureReader implements FeatureReader {
    private static final Logger LOG = org.geotools.util.logging.Logging.getLogger(LineMergerFeatureReader.class.getName());
    
    protected FeatureReader baseFeatureReader;
    protected String featureUniqueKey;
    
    protected Map<String, Feature> uniqueFeatureMap = new HashMap<String, Feature>();
    protected Map<String, LineMerger> uniqueFeatureMerger = new HashMap<String, LineMerger>();
    protected Iterator<Feature> featureIterator;
    
    /**
     * Constructor.
     * @param baseFeatureReader the base feature reader
     * @param featureUniqueKey the key attribute
     */
    public LineMergerFeatureReader(FeatureReader baseFeatureReader, String featureUniqueKey) {
        this.baseFeatureReader = baseFeatureReader;
        this.featureUniqueKey = featureUniqueKey;
        
        // read and merge the features
        try {
            while (baseFeatureReader.hasNext()) {
                try {
                    Feature feature = baseFeatureReader.next();
                    String uniqueKey = (String)feature.getAttribute(featureUniqueKey);
                    //LOG.info("Read feature " + featureUniqueKey + ": " + uniqueKey);
                    if (!uniqueFeatureMap.containsKey(uniqueKey)) uniqueFeatureMap.put(uniqueKey, feature);
                    if (!uniqueFeatureMerger.containsKey(uniqueKey)) uniqueFeatureMerger.put(uniqueKey, new LineMerger());
                    LineMerger merger = uniqueFeatureMerger.get(uniqueKey);
                    merger.add(feature.getDefaultGeometry());
                } catch (NoSuchElementException e) {
                    LOG.warning("Error reading feature: " + e);
                } catch (IllegalAttributeException e) {
                    LOG.warning("Error reading feature: " + e);
                }
            }
        } catch (IOException e) {
            LOG.warning("Error reading features: " + e);
        }
        for (String uniqueKey : uniqueFeatureMerger.keySet()) {
            //LOG.info("Get merged: " + uniqueKey);
            Feature feature = uniqueFeatureMap.get(uniqueKey);
            LineMerger merger = uniqueFeatureMerger.get(uniqueKey);
            try {
                feature.setDefaultGeometry(new MultiLineString((LineString[])merger.getMergedLineStrings().toArray(new LineString[0]), feature.getDefaultGeometry().getFactory()));
            } catch (IllegalAttributeException e) {
                LOG.warning("Prolem setting new geometry: " + e);
            }
        }
        //LOG.info("Features="+uniqueFeatureMap.size());
        featureIterator = uniqueFeatureMap.values().iterator();
    }

    /**
     * {@inheritDoc}
     */
    public void close() throws IOException {
        if (baseFeatureReader != null) baseFeatureReader.close();
    }

    /**
     * {@inheritDoc}
     */
    public FeatureType getFeatureType() {
        return (baseFeatureReader != null) ? baseFeatureReader.getFeatureType() : null;
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasNext() throws IOException {
        return featureIterator.hasNext();
    }

    /**
     * {@inheritDoc}
     */
    public Feature next() throws IOException, IllegalAttributeException {
        return featureIterator.next();
    }

}
