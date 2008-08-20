/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wcs.response;

import java.awt.Rectangle;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.media.jai.Interpolation;

import net.opengis.gml.GridType;
import net.opengis.wcs.AxisSubsetType;
import net.opengis.wcs.GetCoverageType;
import net.opengis.wcs.IntervalType;
import net.opengis.wcs.TypedLiteralType;

import org.geoserver.data.util.CoverageUtils;
import org.geoserver.ows.Response;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geotools.coverage.grid.GeneralGridRange;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.coverage.Coverage;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.vfny.geoserver.global.CoverageInfo;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.WCS;
import org.vfny.geoserver.util.WCSUtils;
import org.vfny.geoserver.wcs.WcsException;
import org.vfny.geoserver.wcs.responses.CoverageResponseDelegate;
import org.vfny.geoserver.wcs.responses.CoverageResponseDelegateFactory;

/**
 * Response object for the store=true path, that is, one that stores the
 * coverage on disk and returns its path thru the Coverages document
 * 
 * @author Andrea Aime - TOPP
 */
public class Wcs10GetCoverageResponse extends Response {
    private final static Hints LENIENT_HINT = new Hints(
            Hints.LENIENT_DATUM_SHIFT, Boolean.TRUE);

    private final static Hints IGNORE_OVERVIEWS = new Hints(
            Hints.IGNORE_COVERAGE_OVERVIEW, Boolean.TRUE);

    private final static Hints hints = new Hints(new HashMap(5));

    static {
        // ///////////////////////////////////////////////////////////////////
        //
        // HINTS
        //
        // ///////////////////////////////////////////////////////////////////
        hints.add(LENIENT_HINT);
        hints.add(IGNORE_OVERVIEWS);
    }

    Data catalog;

    WCS wcs;

    /**
     * 
     */
    CoverageResponseDelegate delegate;

    public Wcs10GetCoverageResponse(WCS wcs, Data catalog) {
        super(GridCoverage[].class);
        this.wcs = wcs;
        this.catalog = catalog;
    }

    @Override
    public String getMimeType(Object value, Operation operation)
            throws ServiceException {
        if (!(operation.getParameters()[0] instanceof GetCoverageType))
            throw new WcsException("Cannot handle object of type: " + operation.getParameters()[0].getClass());

        GetCoverageType getCoverage = (GetCoverageType) operation.getParameters()[0];
        String outputFormat = getCoverage.getOutput().getFormat().getValue();
        if (delegate == null)
            this.delegate = CoverageResponseDelegateFactory
                    .encoderFor(outputFormat);

        if (delegate == null)
            throw new WcsException("Could not find encoder for output format "
                    + outputFormat);

        return delegate.getMimeFormatFor(outputFormat);
    }

    @Override
    public boolean canHandle(Operation operation) {
        if (!(operation.getParameters()[0] instanceof GetCoverageType))
            return false;

        GetCoverageType getCoverage = (GetCoverageType) operation.getParameters()[0];
        String outputFormat = getCoverage.getOutput().getFormat().getValue();
        if (delegate == null)
            this.delegate = CoverageResponseDelegateFactory
                    .encoderFor(outputFormat);

        if (delegate == null)
            throw new WcsException("Could not find encoder for output format "
                    + outputFormat);

        return delegate.canProduce(outputFormat);
    }

    @Override
    public void write(Object value, OutputStream output, Operation operation)
            throws IOException, ServiceException {
        GridCoverage[] coverages = (GridCoverage[]) value;

        // grab the delegate for coverage encoding
        GetCoverageType request = (GetCoverageType) operation.getParameters()[0];
        String outputFormat = request.getOutput().getFormat().getValue();
        if (delegate == null)
            delegate = CoverageResponseDelegateFactory.encoderFor(outputFormat);

        if (delegate == null)
            throw new WcsException("Could not find encoder for output format "
                    + outputFormat);

        // grab the coverage info for Coverages document encoding
        final GridCoverage2D coverage = (GridCoverage2D) coverages[0];
        
        // write the coverage
        try {
            delegate.prepare(outputFormat, coverage);
            delegate.encode(output);
            output.flush();
        } finally {
//            if(output != null) output.close();
        }
    }

    /**
     * getFinalCoverage
     * 
     * @param request
     * @param meta
     * @param coverageReader
     * @param parameters
     * @return
     * @throws WcsException
     * @throws IOException
     * @throws IndexOutOfBoundsException
     * @throws FactoryException
     * @throws TransformException
     */
    private static GridCoverage2D getFinalCoverage(
            GetCoverageType request,
            CoverageInfo meta,
            AbstractGridCoverage2DReader coverageReader /* GridCoverage coverage */,
            Map parameters) throws WcsException, IOException,
            IndexOutOfBoundsException, FactoryException, TransformException {
        // This is the final Response CRS
        final String responseCRS = request.getOutput().getCrs().getValue();

        // - first check if the responseCRS is present on the Coverage
        // ResponseCRSs list
        if (!meta.getResponseCRSs().contains(responseCRS)) {
            throw new WcsException(
                    "This Coverage does not support the requested Response-CRS.");
        }

        // - then create the Coordinate Reference System
        final CoordinateReferenceSystem targetCRS = CRS.decode(responseCRS, true);

        // This is the CRS of the requested Envelope
        GeneralEnvelope envelope = (GeneralEnvelope) request.getDomainSubset().getSpatialSubset().getEnvelope().get(0);
        final CoordinateReferenceSystem requestCRS = envelope.getCoordinateReferenceSystem();

        // - first check if the requestCRS is present on the Coverage
        // RequestCRSs list
        if (!meta.getResponseCRSs().contains(CRS.lookupIdentifier(requestCRS, false))) {
            throw new WcsException("This Coverage does not support the requested CRS.");
        }

        // - then create the Coordinate Reference System
        final CoordinateReferenceSystem sourceCRS = requestCRS;

        // This is the CRS of the Coverage Envelope
        final CoordinateReferenceSystem cvCRS = ((GeneralEnvelope) coverageReader.getOriginalEnvelope()).getCoordinateReferenceSystem();
        final MathTransform GCCRSTodeviceCRSTransformdeviceCRSToGCCRSTransform = CRS.findMathTransform(cvCRS, sourceCRS, true);
        final MathTransform GCCRSTodeviceCRSTransform = CRS.findMathTransform(cvCRS, targetCRS, true);
        final MathTransform deviceCRSToGCCRSTransform = GCCRSTodeviceCRSTransformdeviceCRSToGCCRSTransform.inverse();

        GeneralEnvelope destinationEnvelope;
        final boolean lonFirst = sourceCRS.getCoordinateSystem().getAxis(0).getDirection().absolute().equals(AxisDirection.EAST);

        // the envelope we are provided with is lon, lat always
        if (!lonFirst) {
            destinationEnvelope = new GeneralEnvelope(
                    new double[] {envelope.getLowerCorner().getOrdinate(1), envelope.getLowerCorner().getOrdinate(0) }, 
                    new double[] {envelope.getUpperCorner().getOrdinate(1), envelope.getUpperCorner().getOrdinate(0) }
            );
        } else {
            destinationEnvelope = new GeneralEnvelope(
                    new double[] {envelope.getLowerCorner().getOrdinate(0), envelope.getLowerCorner().getOrdinate(1) }, 
                    new double[] {envelope.getUpperCorner().getOrdinate(0), envelope.getUpperCorner().getOrdinate(1) }
            );
        }

        destinationEnvelope.setCoordinateReferenceSystem(sourceCRS);

        // this is the destination envelope in the coverage crs
        final GeneralEnvelope destinationEnvelopeInSourceCRS = (!deviceCRSToGCCRSTransform.isIdentity()) ? CRS.transform(deviceCRSToGCCRSTransform, destinationEnvelope) : new GeneralEnvelope(destinationEnvelope);
        destinationEnvelopeInSourceCRS.setCoordinateReferenceSystem(cvCRS);

        /**
         * Reading Coverage on Requested Envelope
         */
        Rectangle destinationSize = null;
        GridType grid = (GridType) request.getDomainSubset().getSpatialSubset().getGrid().get(0);
        
        if ((grid != null) && (grid.getLimits() != null)) {
            final int[] lowers = new int[] {
                    (int)grid.getLimits().getMinX(),
                    (int)grid.getLimits().getMinY()};
            final int[] highers = new int[] {
                    (int)grid.getLimits().getMaxX(),
                    (int)grid.getLimits().getMaxY()};

            destinationSize = new Rectangle(lowers[0], lowers[1], highers[0], highers[1]);
        } else {
            /*
             * destinationSize =
             * coverageReader.getOriginalGridRange().toRectangle();
             */
            throw new WcsException("Neither Grid Size nor Grid Resolution have been specified.");
        }

        /**
         * Checking for supported Interpolation Methods
         */
        Interpolation interpolation = Interpolation.getInstance(Interpolation.INTERP_NEAREST);
        final String interpolationType = request.getInterpolationMethod().getLiteral();

        if (interpolationType != null) {
            boolean interpolationSupported = false;
            Iterator internal = meta.getInterpolationMethods().iterator();

            while (internal.hasNext()) {
                if (interpolationType.equalsIgnoreCase((String) internal.next())) {
                    interpolationSupported = true;
                }
            }

            if (!interpolationSupported) {
                throw new WcsException("The requested Interpolation method is not supported by this Coverage.");
            } else {
                if (interpolationType.equalsIgnoreCase("bilinear")) {
                    interpolation = Interpolation.getInstance(Interpolation.INTERP_BILINEAR);
                } else if (interpolationType.equalsIgnoreCase("bicubic")) {
                    interpolation = Interpolation.getInstance(Interpolation.INTERP_BICUBIC);
                }
            }
        }

        // /////////////////////////////////////////////////////////
        //
        // Reading the coverage
        //
        // /////////////////////////////////////////////////////////
        parameters.put(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName().toString(), new GridGeometry2D(new GeneralGridRange(destinationSize), destinationEnvelopeInSourceCRS));

        final GridCoverage coverage = coverageReader.read(CoverageUtils.getParameters(coverageReader.getFormat().getReadParameters(), parameters, true));

        if ((coverage == null) || !(coverage instanceof GridCoverage2D)) {
            throw new IOException("The requested coverage could not be found.");
        }

        /**
         * Band Select
         */
        Coverage bandSelectedCoverage = null;
        if (request.getRangeSubset() != null) {
            Map params = new HashMap();
            List<AxisSubsetType> axisSubset = request.getRangeSubset().getAxisSubset();
            for (AxisSubsetType axis : axisSubset) {
                StringBuilder values = new StringBuilder();
                if (axis.getSingleValue().size() > 0) {
                    for (int s=0; s<axis.getSingleValue().size(); s++) {
                        if (s > 0)
                            values.append(",");
                        values.append(((TypedLiteralType)axis.getSingleValue().get(s)).getValue());
                    }
                } else if (axis.getInterval().size() > 0) {
                    IntervalType interval = (IntervalType) axis.getInterval().get(0);
                    if (interval.getRes() != null) {
                        values
                            .append(interval.getMin().getValue())
                            .append("/")
                            .append(interval.getMax().getValue())
                            .append("/")
                            .append(interval.getRes().getValue());
                    } else {
                        values
                        .append(interval.getMin().getValue())
                        .append(",")
                        .append(interval.getMax().getValue());
                    }
                }
                
                params.put(axis.getName(), values.toString());
            }
            bandSelectedCoverage = WCSUtils.bandSelect(params, coverage);
        } else {
            bandSelectedCoverage = coverage;
        }

        /**
         * Crop
         */
        final GridCoverage2D croppedGridCoverage = WCSUtils.crop(bandSelectedCoverage, (GeneralEnvelope) coverage.getEnvelope(), cvCRS, destinationEnvelopeInSourceCRS, Boolean.TRUE);

        /**
         * Scale/Resampling (if necessary)
         */
        GridCoverage2D subCoverage = croppedGridCoverage;
        final GeneralGridRange newGridrange = new GeneralGridRange(destinationSize);

        /*
         * if (!newGridrange.equals(croppedGridCoverage.getGridGeometry().getGridRange())) {
         */
        subCoverage = WCSUtils.scale(croppedGridCoverage, newGridrange, croppedGridCoverage, cvCRS, destinationEnvelopeInSourceCRS);
        // }

        /**
         * Reproject
         */
        subCoverage = WCSUtils.reproject(subCoverage, sourceCRS, targetCRS, interpolation);

        return subCoverage;
    }
}
