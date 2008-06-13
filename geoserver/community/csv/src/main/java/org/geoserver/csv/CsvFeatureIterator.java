/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.csv;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.geotools.feature.Feature;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.FeatureType;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.util.Converters;

import au.com.bytecode.opencsv.CSVReader;

public class CsvFeatureIterator implements FeatureIterator {

    private FeatureType ft;

    private CSVReader csv;

    private String[] lastLine;

    public CsvFeatureIterator(FeatureType ft, CSVReader csv) {
        this.ft = ft;
        this.csv = csv;
    }

    public void close() {
        try {
            csv.close();
        } catch (IOException e) {
            // we tried...
        }
    }

    public boolean hasNext() {
        try {
            if (lastLine == null)
                lastLine = csv.readNext();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lastLine != null;
    }

    public Feature next() throws NoSuchElementException {
        if (lastLine == null && !hasNext())
            throw new NoSuchElementException("No more lines in this csv file");

        // grab the line
        String[] line = lastLine;
        lastLine = null;

        // build the feature values by converting each string to the expected
        // class
        Object[] values = new Object[ft.getAttributeCount()];
        for (int i = 0; i < values.length; i++) {
            values[i] = Converters.convert(line[i], ft.getAttributeType(i)
                    .getBinding());
            if (values[i] == null)
                values[i] = line[i];
        }

        try {
            return ft.create(values);
        } catch (IllegalAttributeException e) {
            throw new RuntimeException(
                    "At least one attribute value is invalid", e);
        }
    }

}
