/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wps.gs;

import jaitools.media.jai.rangelookup.RangeLookupDescriptor;
import jaitools.media.jai.rangelookup.RangeLookupTable;
import jaitools.numeric.Range;

import java.awt.Color;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.util.List;

import javax.media.jai.RenderedOp;

import org.geoserver.wps.jts.DescribeParameter;
import org.geoserver.wps.jts.DescribeProcess;
import org.geoserver.wps.jts.DescribeResult;
import org.geotools.coverage.Category;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.util.NumberRange;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.util.ProgressListener;

/**
 * A raster reclassified process
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 * @author Emanuele Tajariol (GeoSolutions)
 * @author Andrea Aime - GeoSolutions
 */
@DescribeProcess(title = "reclassify", description = "Reclassifies a continous coverage into a set of ranges identified by a number")
public class RangeLookupProcess implements GeoServerProcess {

    @DescribeResult(name = "reclassified", description = "The resulting reclassified coverage")
    public GridCoverage2D execute(
            @DescribeParameter(name = "coverage", description = "The continuous coverage to be reclassified") GridCoverage2D coverage,
            @DescribeParameter(name = "band", description = "The band to be used for classification "
                    + "(defaults to 0)", min = 0) Integer classificationBand,
            @DescribeParameter(name = "ranges", description = "The list of ranges to be applied. \n"
                    + "Each range is expressed as 'OPEN START ; END CLOSE'\n"
                    + "where 'OPEN:=(|[, CLOSE=)|]',\n "
                    + "START is the low value, or nothing to imply -INF,\n"
                    + "CLOSE is the biggest value, or nothing to imply +INF", collectionType=Range.class) List<Range> classificationRanges,
            ProgressListener listener) {
        // parse the band
        int band = 0;
        if (classificationBand != null) {
            band = classificationBand;
        }

        // decide what kind of data we're dealing with
        GridSampleDimension sampleDimension = coverage.getSampleDimension(band);
        NumberRange sampleRange = sampleDimension.getRange();
        Class sampleClass = sampleRange == null ? null : sampleRange.getElementClass();
        Class typeClass = mapDataBufferType(coverage.getRenderedImage().getData().getDataBuffer()
                .getDataType());
        if (!typeClass.equals(sampleClass)) {
            System.out.println("====== sampleDimension range class [" + sampleClass
                    + "] and databuffer type [" + typeClass + "] differ. Choosing the latter one.");
            sampleClass = typeClass;
        }

        // Builds the range lookup table and reclassifies the image
        RenderedImage inputImage = coverage.getRenderedImage();
        RangeLookupTable lookupTable = getRangeLookupTable(classificationRanges, sampleClass);
        RenderedOp indexedClassification = RangeLookupDescriptor.create(inputImage, lookupTable, null);
        
        // build the output sample dimensions
        Category noDataCategory = new Category(Vocabulary
                .formatInternational(VocabularyKeys.NODATA), new Color[] { new Color(0, 0, 0, 0) },
                NumberRange.create(0d, 0d), NumberRange.create(0d, 0d));
        GridSampleDimension outSampleDimension = new GridSampleDimension("classification",
                new Category[] { noDataCategory }, null).geophysics(true);

        // build the output coverage
        GridCoverageFactory factory = CoverageFactoryFinder.getGridCoverageFactory(null);
        GridCoverage2D output = factory.create("reclassified", indexedClassification, coverage
                .getGridGeometry(), new GridSampleDimension[] { outSampleDimension },
                new GridCoverage[] { coverage }, null);
        return output;
    }

    private <T extends Number & Comparable> RangeLookupTable<T> getRangeLookupTable(
            List<Range> classificationRanges, Class<T> type) {
        RangeLookupTable<T> rlt = new RangeLookupTable<T>(convert(0d, type)); // 0 is norange
        for (int i = 0; i < classificationRanges.size(); i++) {
            Range<T> range = convertRange(classificationRanges.get(i), type);
            rlt.add(range, convert(i + 1d, type));
        }
        return rlt;
    }

    private <T extends Number & Comparable> T convert(Double val, Class<T> type) {
        if (val == null) {
            return null;
        } else if (Double.class.equals(type)) {
            return (T) Double.valueOf(val);
        } else if (Float.class.equals(type)) {
            return (T) Float.valueOf(val.floatValue());
        } else if (Integer.class.equals(type)) {
            return (T) Integer.valueOf(val.intValue());
        } else if (Byte.class.equals(type)) {
            return (T) Byte.valueOf(val.byteValue());
        } else {
            throw new UnsupportedOperationException("Class " + type
                    + " can't be used in a value Range");
        }
    }

    <T extends Number & Comparable> Range<T> convertRange(Range<Double> src, Class<T> type) {
        return new Range<T>(convert(src.getMin(), type), src.isMinIncluded(), convert(src.getMax(),
                type), src.isMaxIncluded());
    }

    Class mapDataBufferType(int type) {

        switch (type) {
        case DataBuffer.TYPE_BYTE:
            return Byte.class;
        case DataBuffer.TYPE_USHORT:
            return Integer.class;
        case DataBuffer.TYPE_SHORT:
            return Integer.class;
        case DataBuffer.TYPE_INT:
            return Integer.class;
        case DataBuffer.TYPE_FLOAT:
            return Float.class;
        case DataBuffer.TYPE_DOUBLE:
            return Double.class;
        default:
            throw new IllegalArgumentException("Unknown DataBuffer type " + type);
        }
    }

}
