/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.responses;

import org.geotools.coverage.grid.GeneralGridRange;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridCoverageNDReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.resources.CRSUtilities;
import org.opengis.coverage.Coverage;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.Response;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.CoverageInfo;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.util.CoverageUtils;
import org.vfny.geoserver.util.WCSUtils;
import org.vfny.geoserver.wcs.WcsException;
import org.vfny.geoserver.wcs.requests.CoverageRequest;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.jai.Interpolation;


/**
 * DOCUMENT ME!
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last
 *         modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last
 *         modification)
 */
public class CoverageResponse implements Response {
    /** Standard logging instance for class */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses");
    private final static Hints LENIENT_HINT = new Hints(Hints.LENIENT_DATUM_SHIFT,
            Boolean.TRUE);
    private final static Hints IGNORE_OVERVIEWS = new Hints(Hints.IGNORE_COVERAGE_OVERVIEW,
            Boolean.TRUE);
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

    /**
     * Tolerance for NOT drawing a coverage.
     *
     * If after a scaling a coverage has all dimensions smaller than
     * {@link GridCoverageRenderer#MIN_DIM_TOLERANCE} we just do not draw it.
     */
    private static final int MIN_DIM_TOLERANCE = 1;

    /**
     *
     */
    CoverageResponseDelegate delegate;

    /**
     * This is the request provided to the execute( Request ) method.
     *
     * <p>
     * We save it so we can access the handle provided by the user for error
     * reporting during the writeTo( OutputStream ) opperation.
     * </p>
     *
     * <p>
     * This value will be <code>null</code> until execute is called.
     * </p>
     *
     * @uml.property name="request"
     * @uml.associationEnd multiplicity="(0 1)"
     */
    private CoverageRequest request;

    /**
     * Empty constructor
     */
    public CoverageResponse() {
        request = null;
    }

    /**
     * Returns any extra headers that this service might want to set in the HTTP response object.
     * @see org.vfny.geoserver.Response#getResponseHeaders()
     */
    public HashMap getResponseHeaders() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param gs
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContentType(GeoServer gs) {
        return /*delegate.getContentType(gs)*/ "";
    }

    public String getContentEncoding() {
        return delegate.getContentEncoding();
    }

    public String getContentDisposition() {
        return delegate.getContentDisposition();
    }

    /**
     * Jody here with one pass replacement for writeTo.
     *
     * <p>
     * This code is a discussion point, when everyone has had there input we
     * will try and set things up properly.
     * </p>
     *
     * <p>
     * I am providing a mirror of the existing desing: - execute gathers the
     * resultList - sets up the header
     * </p>
     *
     * @param out
     *            DOCUMENT ME!
     *
     * @throws WcsException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     * @throws IllegalStateException
     *             DOCUMENT ME!
     */
    public void writeTo(OutputStream out) throws ServiceException, IOException {
        if ((request == null) || (delegate == null)) {
            throw new IllegalStateException(
                "execute has not been called prior to writeTo");
        }

        delegate.encode(out);
    }

    /**
     * Executes CoverageRequest.
     *
     * <p>
     * Willing to execute a CoverageRequest.
     * </p>
     *
     * @param req
     *            DOCUMENT ME!
     *
     * @throws WcsException
     *             DOCUMENT ME!
     */
    public void execute(Request req) throws WcsException {
        execute((CoverageRequest) req);
    }

    public void execute(CoverageRequest request) throws WcsException {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest(new StringBuffer(
                    "execute CoverageRequest response. Called request is: ").append(
                    request).toString());
        }

        this.request = request;

        final String outputFormat = request.getOutputFormat();

        try {
            delegate = CoverageResponseDelegateFactory.encoderFor(outputFormat);
        } catch (NoSuchElementException ex) {
            WcsException newEx = new WcsException(new StringBuffer(
                        "output format: ").append(outputFormat).append(" not ")
                                                                                     .append("supported by geoserver for this Coverage")
                                                                                     .toString(),
                    ex);
            throw newEx;
        }

        final Data catalog = request.getWCS().getData();
        CoverageInfo meta = null;
        GridCoverage coverage = null;

        try {
            meta = catalog.getCoverageInfo(request.getCoverage());

            boolean isFormatSupported = false;
            final List suppFormats = meta.getSupportedFormats();
            for (Iterator formatName = suppFormats.iterator(); formatName.hasNext(); ) {
                isFormatSupported = ((String) formatName.next()).equalsIgnoreCase(outputFormat);

                if (isFormatSupported) {
                    break;
                }
            }

            if (!isFormatSupported) {
                WcsException newEx = new WcsException(new StringBuffer(
                            "output format: ").append(outputFormat)
                                                                                         .append(" not ")
                                                                                         .append("supported by geoserver for this Coverage")
                                                                                         .toString());
                throw newEx;
            }

            final Format format = meta.getFormatInfo().getFormat();
            final GridCoverageReader reader = meta.createReader(hints);

            // /////////////////////////////////////////////////////////
            //
            // Setting coverage reading params.
            //
            // /////////////////////////////////////////////////////////
            final ParameterValueGroup params = (ParameterValueGroup) reader.getFormat()
                                                                           .getReadParameters()
                                                                           .clone();

            try {
                if (params.parameter("Coverage") != null) {
                    params.parameter("Coverage").setValue(meta.getRealName());
                }
            } catch (ParameterNotFoundException e) {
            }

            try {
                if ((params.parameter("Times") != null)
                        && (request.getTime() != null)) {
                    List times = request.getTime();
                    Object[] timePositions = new Object[times.size()];

                    for (int t = 0; t < times.size(); t++)
                        timePositions[t] = times.get(t);

                    params.parameter("Times").setValue(timePositions);
                }
            } catch (ParameterNotFoundException e) {
            }

            try {
                if ((params.parameter("Bands") != null)
                        && (request.getParameters() != null)
                        && !request.getParameters().isEmpty()) {
                    params.parameter("Bands")
                          .setValue(request.getParameters().keySet()
                                           .toArray(new String[1]));
                }
            } catch (ParameterNotFoundException e) {
            }

            try {
                if ((params.parameter("Elevations") != null)
                        && (request.getElevations() != null)) {
                    params.parameter("Elevations").setValue(request.getElevations());
                }
            } catch (ParameterNotFoundException e) {
            }

            final GridCoverage2D finalCoverage = getFinalCoverage(request,
                    meta, reader, params);
            delegate.prepare(outputFormat, finalCoverage);
        } catch (IOException e) {
            final WcsException newEx = new WcsException(e,
                    "problem with CoverageResults", request.getHandle());

            throw newEx;
        } catch (NoSuchElementException e) {
            final WcsException newEx = new WcsException(e,
                    "problem with CoverageResults", request.getHandle());
            throw newEx;
        } catch (IllegalArgumentException e) {
            final WcsException newEx = new WcsException(e,
                    "problem with CoverageResults", request.getHandle());
            throw newEx;
        } catch (SecurityException e) {
            final WcsException newEx = new WcsException(e,
                    "problem with CoverageResults", request.getHandle());
            throw newEx;
        } catch (WcsException e) {
            final WcsException newEx = new WcsException(e,
                    "problem with CoverageResults", request.getHandle());
            throw newEx;
        } catch (FactoryException e) {
            final WcsException newEx = new WcsException(e,
                    "problem with CoverageResults", request.getHandle());
            throw newEx;
        } catch (IndexOutOfBoundsException e) {
            final WcsException newEx = new WcsException(e,
                    "problem with CoverageResults", request.getHandle());
            throw newEx;
        } catch (TransformException e) {
            final WcsException newEx = new WcsException(e,
                    "problem with CoverageResults", request.getHandle());
            throw newEx;
        }
    }

    /**
     * Release locks if we are into that sort of thing.
     *
     * @see org.vfny.geoserver.responses.Response#abort()
     */
    public void abort(Service gs) {
        if (request == null) {
            return; // request was not attempted
        }

        Data catalog = gs.getData();
    }

    /**
     * GetCroppedCoverage
     *
     * @param request CoverageRequest
     * @param meta CoverageInfo
     * @param params
     * @param coverage GridCoverage
     * @return GridCoverage2D
     * @throws WcsException
     * @throws IOException
     * @throws IndexOutOfBoundsException
     * @throws FactoryException
     * @throws TransformException
     */
    private static GridCoverage2D getFinalCoverage(CoverageRequest request,
        CoverageInfo meta,
        GridCoverageReader coverageReader /*GridCoverage coverage*/,
        ParameterValueGroup params)
        throws WcsException, IOException, IndexOutOfBoundsException,
            FactoryException, TransformException {
        // This is the final Response CRS
        final String responseCRS = request.getResponseCRS();

        // - first check if the responseCRS is present on the Coverage
        // ResponseCRSs list
        if (!meta.getResponseCRSs().contains(responseCRS)) {
            throw new WcsException(
                "This Coverage does not support the requested Response-CRS.");
        }

        // - then create the Coordinate Reference System
        final CoordinateReferenceSystem targetCRS = CRS.decode(responseCRS);

        // This is the CRS of the requested Envelope
        final String requestCRS = request.getCRS();

        // - first check if the requestCRS is present on the Coverage
        // RequestCRSs list
        if (!meta.getResponseCRSs().contains(requestCRS)) {
            throw new WcsException(
                "This Coverage does not support the requested CRS.");
        }

        // - then create the Coordinate Reference System
        final CoordinateReferenceSystem sourceCRS = CRS.decode(requestCRS);

        // This is the CRS of the Coverage Envelope
        final CoordinateReferenceSystem cvCRS = ((coverageReader instanceof AbstractGridCoverage2DReader)
            ? ((GeneralEnvelope) ((AbstractGridCoverage2DReader) coverageReader)
            .getOriginalEnvelope()).getCoordinateReferenceSystem()
            : ((AbstractGridCoverageNDReader) coverageReader).getCrs(meta
                .getRealName()));
        final MathTransform GCCRSTodeviceCRSTransformdeviceCRSToGCCRSTransform = CRS
            .findMathTransform(cvCRS, sourceCRS, true);
        final MathTransform GCCRSTodeviceCRSTransform = CRS.findMathTransform(cvCRS,
                targetCRS, true);
        final MathTransform deviceCRSToGCCRSTransform = GCCRSTodeviceCRSTransformdeviceCRSToGCCRSTransform
            .inverse();

        com.vividsolutions.jts.geom.Envelope envelope = request.getEnvelope();
        GeneralEnvelope destinationEnvelope;
        final boolean lonFirst = sourceCRS.getCoordinateSystem().getAxis(0)
                                          .getDirection().absolute()
                                          .equals(AxisDirection.EAST);

        // the envelope we are provided with is lon,lat always
        if (!lonFirst) {
            destinationEnvelope = new GeneralEnvelope(new double[] {
                        envelope.getMinY(), envelope.getMinX()
                    }, new double[] { envelope.getMaxY(), envelope.getMaxX() });
        } else {
            destinationEnvelope = new GeneralEnvelope(new double[] {
                        envelope.getMinX(), envelope.getMinY()
                    }, new double[] { envelope.getMaxX(), envelope.getMaxY() });
        }

        destinationEnvelope.setCoordinateReferenceSystem(sourceCRS);

        // this is the destination envelope in the coverage crs
        final GeneralEnvelope destinationEnvelopeInSourceCRS = (!deviceCRSToGCCRSTransform
            .isIdentity())
            ? CRSUtilities.transform(deviceCRSToGCCRSTransform,
                destinationEnvelope) : new GeneralEnvelope(destinationEnvelope);
        destinationEnvelopeInSourceCRS.setCoordinateReferenceSystem(cvCRS);

        /**
         * Reading Coverage on Requested Envelope
         */
        Rectangle destinationSize = null;

        if ((request.getGridLow() != null) && (request.getGridHigh() != null)) {
            final int[] lowers = new int[] {
                    request.getGridLow()[0].intValue(),
                    request.getGridLow()[1].intValue()
                };
            final int[] highers = new int[] {
                    request.getGridHigh()[0].intValue(),
                    request.getGridHigh()[1].intValue()
                };

            destinationSize = new Rectangle(lowers[0], lowers[1], highers[0],
                    highers[1]);
        } else {
            /*destinationSize = coverageReader.getOriginalGridRange().toRectangle();*/
            throw new WcsException(
                "Neither Grid Size nor Grid Resolution have been specified.");
        }

        /**
         * Checking for supported Interpolation Methods
         */
        Interpolation interpolation = Interpolation.getInstance(Interpolation.INTERP_NEAREST);
        final String interpolationType = request.getInterpolation();

        if (interpolationType != null) {
            boolean interpolationSupported = false;
            Iterator internal = meta.getInterpolationMethods().iterator();

            while (internal.hasNext()) {
                if (interpolationType.equalsIgnoreCase((String) internal.next())) {
                    interpolationSupported = true;
                }
            }

            if (!interpolationSupported) {
                throw new WcsException(
                    "The requested Interpolation method is not supported by this Coverage.");
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
        if (params.parameter(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName()
                                                                       .toString()) != null) {
            params.parameter(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName()
                                                                   .toString())
                  .setValue(new GridGeometry2D(
                    new GeneralGridRange(destinationSize),
                    destinationEnvelopeInSourceCRS));
        }

        final GridCoverage coverage = coverageReader.read(CoverageUtils
                .getParameters(params));

        if ((coverage == null) || !(coverage instanceof GridCoverage2D)) {
            throw new IOException("The requested coverage could not be found.");
        }

        /**
         * Band Select
         *
         * TODO: better refactoring for ND-Coverages
         */
        Coverage bandSelectedCoverage = null;

        try {
            bandSelectedCoverage = WCSUtils.bandSelect(request.getParameters(),
                    coverage);
        } catch (WcsException e) {
            throw new WcsException(e.getLocalizedMessage());
        }

        /**
         * Crop
         */
        final GridCoverage2D croppedGridCoverage = WCSUtils.crop(bandSelectedCoverage,
                (GeneralEnvelope) coverage.getEnvelope(), cvCRS,
                destinationEnvelopeInSourceCRS, Boolean.TRUE);

        /**
         * Scale/Resampling (if necessary)
         */
        GridCoverage2D subCoverage = croppedGridCoverage;
        final GeneralGridRange newGridrange = new GeneralGridRange(destinationSize);

        /*if (!newGridrange.equals(croppedGridCoverage.getGridGeometry()
           .getGridRange())) {*/
        subCoverage = WCSUtils.scale(croppedGridCoverage, newGridrange,
                croppedGridCoverage, cvCRS, destinationEnvelopeInSourceCRS);
        //}

        /**
         * Reproject
         */
        subCoverage = WCSUtils.reproject(subCoverage, sourceCRS, targetCRS,
                interpolation);

        return subCoverage;
    }
}
